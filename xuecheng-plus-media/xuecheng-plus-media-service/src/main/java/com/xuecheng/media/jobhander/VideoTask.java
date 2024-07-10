package com.xuecheng.media.jobhander;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileService;
import com.xuecheng.media.service.MediaProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class VideoTask {
    @Autowired
    MediaProcessService mediaProcessService;
    @Autowired
    MediaFileService mediaFileService;


    @Value("${videoprocess.ffmpegpath}")
    String ffmpegpath;

    // 从配置文件中获取ffmpeg的安装位置
    @XxlJob("videoJobHandler")
    public void videoJobHandler() {
        // 分片序号
        int shardIndex = XxlJobHelper.getShardIndex();
        // 分片总数
        int shardTotal = XxlJobHelper.getShardTotal();
        List<MediaProcess> mediaProcessList =null;
        //取出待处理任务的数量
        int size = 0;
        try {
            //取出cpu核心数作为一次处理数据的条数
            int processors = Runtime.getRuntime().availableProcessors();
            //一次处理视频数量不要超过cpu核心数
            mediaProcessList = mediaProcessService.getMediaProcessList(shardIndex, shardTotal, processors);
            size = mediaProcessList.size();
            log.debug("取出待处理视频任务{}条", size);
            if (size < 0) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //启动一个大小为size的线程池
        ExecutorService threadPool= Executors.newFixedThreadPool(size);
        //创建计数器
        CountDownLatch countDownLatch=new CountDownLatch(size);

        //将处理任务加入线程池进行多线程处理
        mediaProcessList.forEach(mediaProcess -> {
            threadPool.execute(()->{
                //获取任务id
                Long taskId=mediaProcess.getId();
                //抢占任务
                boolean b = mediaProcessService.startTask(taskId);
                if(!b){//抢占失败
                    countDownLatch.countDown();
                    return;
                }
                //抢占成功，进行文件处理
                //获取minio通
                String bucket = mediaProcess.getBucket();
                //获取文件md5值（即文件id）
                String fileMd5= mediaProcess.getFileId();
                //获取文件存储路径
                String filePath = mediaProcess.getFilePath();
                //获取文件原始名称
                String filename = mediaProcess.getFilename();
                File originalFile = null;
                File mp4File = null;
                //临时文件下载
                try {
                    // 将原始视频下载到本地，创建临时文件
                    originalFile = File.createTempFile("original", null);
                    // 处理完成后的文件
                    mp4File = File.createTempFile("mp4", ".mp4");

                } catch (IOException e) {
                    log.error("处理视频前创建临时文件失败");
                    mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileMd5, null, "创建mp4临时文件失败");
                    countDownLatch.countDown();
                    new XueChengPlusException("处理视频前创建临时文件失败");
                }

                //进行原始文件下载
                try {
                    mediaFileService.downloadFromMinio(originalFile, bucket, filePath);
                    if (originalFile == null) {
                        log.debug("下载待处理文件失败,originalFile:{}", mediaProcess.getBucket().concat(mediaProcess.getFilePath()));
                        mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileMd5, null, "下载待处理文件失败");
                        countDownLatch.countDown();
                        return;
                    }
                }catch (Exception e) {
                    log.error("下载原始文件过程中出错：{}，文件信息：{}", e.getMessage(), mediaProcess);
                    countDownLatch.countDown();
                    new XueChengPlusException("下载原始文件过程出错");
                }

                //todo:将合成后的文件储存在与原文件相同的目录或者创建一个转化目录再储存
                //调用工具类将avi转为mp4
                //记录结果
                String result="";
                try{
                    Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegpath, originalFile.getAbsolutePath(), filename, mp4File.getAbsolutePath());
                    result=mp4VideoUtil.generateMp4();
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("处理视频文件:{},出错:{}", mediaProcess.getFilePath(), e.getMessage());
                    mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileMd5, null, "视频转化时出错");
                    countDownLatch.countDown();
                    new XueChengPlusException("视频转化时出错");
                }

                if (!result.equals("success")) {//失败
                    //记录错误信息
                    log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + filePath, result);
                    mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileMd5, null, result);
                    countDownLatch.countDown();
                    return;
                }

                //将mp4文件存入minio中
                String objectName=getFilePath(fileMd5,".mp4");
                //访问的url
                String url="/"+bucket+"/"+objectName;
                try{
                    //存入minio中
                    mediaFileService.addMediaFilesToMinIO(mp4File.getAbsolutePath(),"video/mp4",bucket,objectName);
                    //将url存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史
                    mediaProcessService.saveProcessFinishStatus(taskId,"2",fileMd5,url,null);
                }catch (Exception e){
                    log.error("上传视频失败或入库失败,视频地址:{},错误信息:{}", bucket + objectName, e.getMessage());
                    mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileMd5, null,"文件传入minio失败");
                    new XueChengPlusException("上传文件失败");
                }finally {
                    countDownLatch.countDown();
                }
            });

        });

        try {
            countDownLatch.await(30,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private String getFilePath(String fileMd5,String fileExt){
        return  fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 +fileExt;
    }

}
