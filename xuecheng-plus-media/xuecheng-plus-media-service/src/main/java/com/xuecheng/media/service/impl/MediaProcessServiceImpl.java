package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessHistoryMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.model.po.MediaProcessHistory;
import com.xuecheng.media.service.MediaProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class MediaProcessServiceImpl implements MediaProcessService {
    @Autowired
    MediaProcessMapper mediaProcessMapper;
    @Autowired
    MediaFilesMapper mediaFilesMapper;
    @Autowired
    MediaProcessHistoryMapper mediaProcessHistoryMapper;
    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
        return mediaProcessMapper.selectListByShardIndex(shardTotal,shardIndex,count);
    }

    @Override
    public boolean startTask(long id) {
        int count = mediaProcessMapper.startTask(id);
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        //查询任务此时状态
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if(mediaProcess==null){
            log.debug("更新任务状态时，此任务：{}，为空", taskId);
            return;
        }
        LambdaQueryWrapper<MediaProcess> queryWrapper=new LambdaQueryWrapper<MediaProcess>().eq(MediaProcess::getId,taskId);
        if("3".equals(status)){//任务失败
            MediaProcess updataMediaProcess=new MediaProcess();
            updataMediaProcess.setStatus("3");
            updataMediaProcess.setErrormsg(errorMsg);
            updataMediaProcess.setFailCount(mediaProcess.getFailCount()+1);
            mediaProcessMapper.update(updataMediaProcess,queryWrapper);
            log.debug("更新任务处理状态为失败，任务信息:{}",updataMediaProcess);
            return ;

        }else if("2".equals(status)){//任务成功
            MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
            if(mediaFiles!=null){
                //更新媒资文件中的访问url
                mediaFiles.setUrl(url);
                mediaFilesMapper.updateById(mediaFiles);
            }
            mediaProcess.setStatus("2");
            mediaProcess.setUrl(url);
            mediaProcess.setFinishDate(LocalDateTime.now());

            //更新数据
            mediaProcessMapper.updateById(mediaProcess);
            //创建任务处理历史数据
            MediaProcessHistory mediaProcessHistory=new MediaProcessHistory();
            //两个表数据一样直接copy
            BeanUtils.copyProperties(mediaProcess,mediaProcessHistory);
            //任务处理历史表添加数据
            mediaProcessHistoryMapper.insert(mediaProcessHistory);
            //删除待处理表数据
            mediaProcessMapper.deleteById(mediaProcess);
            return;
        }

    }
}
