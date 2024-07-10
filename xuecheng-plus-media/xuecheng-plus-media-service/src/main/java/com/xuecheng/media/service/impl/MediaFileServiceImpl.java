package com.xuecheng.media.service.impl;

import com.alibaba.nacos.client.config.utils.ContentUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.DigestUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/10 8:58
 */
@Service
@Slf4j
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    MediaFilesMapper mediaFilesMapper;
    @Autowired
    MediaProcessMapper mediaProcessMapper;

    @Autowired
    MinioClient minioClient;
    @Value("${minio.bucket.files}")
    private String bucket_files;

    @Autowired
    MediaFileService mediaFileService;

    @Value("${minio.bucket.videofiles}")
    private String bucket_videoFiles;
    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isBlank(queryMediaParamsDto.getFilename()),MediaFiles::getFilename,queryMediaParamsDto.getFilename());
        queryWrapper.eq(!StringUtils.isBlank(queryMediaParamsDto.getFileType()),MediaFiles::getFileType,queryMediaParamsDto.getFileType());
        queryWrapper.orderByDesc(MediaFiles::getCreateDate);
        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }

    @Override
//    @Transactional  在该上传文件方法中，由于对事务管理中，需要向minio传入数据后再在数据库中写入，若传ME大量数据会导致数据库的释放时间过长
    //故仅需要对数据库写入时进行事务管理即可，由于在执行时若直接调用addMediaFilesToDb方法并非通过代理对象执行，故哪怕addMediaFilesToDb方法
    //加了注释也没用，因此需要通过代理对象来执行，故通过将addMediaFilesToDb方法提取到MediaFileService注册为接口并通过MediaFileService
    //代理对象进行执行   开启事务代理条件：1.通过代理对象执行 2.@Transactional
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath,String folder,String objectName) {
        File file=new File(localFilePath);
        if (!file.exists()) {
            new XueChengPlusException("文件不存在");
        }
        //获取文件名称
        String filename = uploadFileParamsDto.getFilename();
        //获取文件扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        //获取媒资类型
        String mimeType = getMimeType(extension);
        //获取文件md5值
        String fileMd5 = getFileMd5(file);
        //获取文件默认储存路径
        String defaultFolderPath = getDefaultFolderPath();
        //存储到minio中的对象名(带目录)

        if(StringUtils.isEmpty(objectName)){
            objectName =  defaultFolderPath + fileMd5 + extension;
        }

        if(!StringUtils.isEmpty(folder)){
            objectName="/"+folder+"/"+objectName;
        }
        //上传文件至minio
        boolean b = addMediaFilesToMinIO(localFilePath, mimeType, bucket_files, objectName);
        //设置文件大小
        uploadFileParamsDto.setFileSize(file.length());
        //存储至数据库中
        MediaFiles mediaFiles = mediaFileService.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_files, objectName);
        //返回数据
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
        return uploadFileResultDto;

    }


    //获取文件默认存储目录路径 年/月/日
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/")+"/";
        return folder;
    }

    //获取文件的md5
    private String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5DigestAsHex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取媒资类型
    private String getMimeType(String extension){
        if(extension==null)
            extension = "";
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        //通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }
    /**
     * @description 将文件写入minIO
     * @param localFilePath  文件地址
     * @param mimeType 媒资类型
     * @param bucket  桶
     * @param objectName 对象名称
     * @return 是否添加成功
    */
    public boolean addMediaFilesToMinIO(String localFilePath,String mimeType,String bucket, String objectName) {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket(bucket)//桶
                    .object(objectName)//上传至minio的路径
                    .filename(localFilePath)//本地文件的路径
                    .contentType(mimeType)
                    .build();
            minioClient.uploadObject(testbucket);
            log.debug("上传文件到minio成功,bucket:{},objectName:{}", bucket, objectName);
            System.out.println("上传成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件到minio出错,bucket:{},objectName:{},错误原因:{}", bucket, objectName, e.getMessage(), e);
            new XueChengPlusException("上传文件到文件系统失败");
        }
        return false;
    }


    /**
     * @description 将文件信息添加到文件表
     * @param companyId  机构id
     * @param fileMd5  文件md5值
     * @param uploadFileParamsDto  上传文件的信息
     * @param bucket  桶
     * @param objectName 对象名称
     * @return 插入成功的
    */
    @Transactional
    public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if(mediaFiles==null){
            mediaFiles=new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamsDto,mediaFiles);
            mediaFiles.setId(fileMd5);
            mediaFiles.setFileId(fileMd5);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setBucket(bucket);
            // 获取源文件名的contentType
            String contentType = getContentType(objectName);
            if(contentType.contains("video")||contentType.contains("image")){//对图片和视频进行url保存
                mediaFiles.setUrl("/"+bucket+"/"+objectName);
            }
            mediaFiles.setFilePath(objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setAuditStatus("002003");
            mediaFiles.setStatus("1");
            int insert = mediaFilesMapper.insert(mediaFiles);
            if (insert < 0) {
                log.error("保存文件信息到数据库失败,{}",mediaFiles.toString());
                new XueChengPlusException("保存文件信息失败");
            }
            addWaitingTask(mediaFiles);
            log.debug("保存文件信息到数据库成功,{}",mediaFiles.toString());
        }
        return mediaFiles;

    }
    /**
     * 添加待处理任务
     * @param mediaFiles 媒资文件信息
     */

    private void addWaitingTask(MediaFiles mediaFiles) {
        //文件名称
        String filename = mediaFiles.getFilename();
        //文件扩展名
        String exension = filename.substring(filename.lastIndexOf("."));
        //文件mimeType
        String mimeType = getMimeType(exension);
        //如果是avi视频添加到视频待处理表
        if(mimeType.equals("video/x-msvideo")){
            MediaProcess mediaProcess = new MediaProcess();
            BeanUtils.copyProperties(mediaFiles,mediaProcess);
            mediaProcess.setStatus("1");//未处理
            mediaProcess.setFailCount(0);//失败次数默认为0
            mediaProcessMapper.insert(mediaProcess);
        }

    }

    private String getContentType(String objectName) {
        String extension=objectName.substring(objectName.lastIndexOf("."));//获取扩展文件名
        String mimetype=MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用mimeType，字节流
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);//匹配文件类型
        if(extensionMatch!=null){
            mimetype=extensionMatch.getMimeType();
        }
        return mimetype;//返回文件类型
    }

    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        //查询数据库中是否存在该文件
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if(mediaFiles==null){//无数据
            return RestResponse.success(false);
        }

        //查询到数据查看minio中是否有数据记录
        GetObjectArgs check = GetObjectArgs.builder()
                .bucket(mediaFiles.getBucket())
                .object(mediaFiles.getFilePath())
                .build();

        try {
            InputStream stream = minioClient.getObject(check);
            if(stream==null){
                return RestResponse.success(false);
            }
        }catch (Exception e){
            return RestResponse.success(false);
        }

        return RestResponse.success(true);
    }


    @Override
    public RestResponse<Boolean> checkchunk(String fileMd5, int chunkIndex) {
        //获取分块目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //获取分块路径
        String chunkFilePath=chunkFileFolderPath+chunkIndex;
        //在minio中查询分块是否存在
        try {
            GetObjectArgs chunk = GetObjectArgs.builder()
                    .bucket(bucket_videoFiles)
                    .object(chunkFilePath)
                    .build();
            InputStream stream = minioClient.getObject(chunk);
            if(stream==null){//分块不存在
                return RestResponse.success(false);
            }
        }catch (Exception e){
            return RestResponse.success(false);
        }
        return RestResponse.success(true);
    }

    @Override
    public RestResponse uploadChunk(String fileMd5, int chunkIndex, String localChunkFilePath) {
        //得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;
        try{
            boolean b = addMediaFilesToMinIO(localChunkFilePath, getMimeType(null), bucket_videoFiles, chunkFilePath);
            if(!b){
                return RestResponse.validfail(false,"上传分块失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.debug("上传分块文件:{},失败:{}",chunkFilePath,e.getMessage());
        }
        return RestResponse.success(true);

    }

    @Override
    public RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        //从minio中下载文件
        File[] chunkFiles = checkChunkStatus(fileMd5, chunkTotal);
        // 获取源文件名
        String fileName = uploadFileParamsDto.getFilename();
        // 获取源文件扩展名
        String extension = fileName.substring(fileName.lastIndexOf("."));
        // 创建出临时文件，准备合并
        File mergeFile=null;
        try{
            mergeFile = File.createTempFile(fileName, extension);
        }catch (Exception e){
            new XueChengPlusException("创建临时文件出错");
        }
        // 缓冲区
        byte[] buffer = new byte[1024];
        RandomAccessFile rafWriter = null;
        RandomAccessFile rafRead=null;
        try{
            // 写入流，向临时文件写入
            rafWriter = new RandomAccessFile(mergeFile, "rw");
        }catch (Exception e){
            new XueChengPlusException("创建文件io出错");
        }
        // 遍历分块文件数组
        for (File chunkFile : chunkFiles) {
            try{
                // 读取流，读分块文件
                rafRead = new RandomAccessFile(chunkFile, "r");
            }catch (Exception e){
                new XueChengPlusException("创建文件io出错");
            }
            //进行合并
            int len;
            try{
                while ((len = rafRead.read(buffer)) != -1) {
                    rafWriter.write(buffer, 0, len);
                }
            }catch (Exception e){
                new XueChengPlusException("合并文件时出错");
            }

        }
        //设置文件大小
        uploadFileParamsDto.setFileSize(mergeFile.length());
        //进行数合并后据校验
        FileInputStream mergeStream=null;
        try{
            mergeStream=new FileInputStream(mergeFile);
        }catch (Exception e){
            new XueChengPlusException("创建合并文件io出错");
        }

        String mergeMd5=null;
        try{
            //获取合并文件的md5值并与原文件的md5值进行比较
            mergeMd5 = DigestUtils.md5DigestAsHex(mergeStream);
            if(!mergeMd5.equals(fileMd5)){
                new XueChengPlusException("合并文件校验失败");
            }
            log.debug("合并文件校验通过：{}", mergeFile.getAbsolutePath());
        }catch (Exception e){
            new XueChengPlusException("合并文件校验失败");
        }
        //拼接文件路径
        String mergeFilePath=getFilePathByMd5(mergeMd5,extension);
        //将合并好的文件传入minio中
        addMediaFilesToMinIO(mergeFile.getAbsolutePath(), getMimeType(extension),bucket_videoFiles, mergeFilePath);
        log.debug("合并文件上传至MinIO完成{}", mergeFile.getAbsolutePath());

        //将文件信息写入数据库中
        MediaFiles mediaFiles = addMediaFilesToDb(companyId,fileMd5,uploadFileParamsDto,bucket_videoFiles,mergeFilePath);
        if (mediaFiles == null) {
            new XueChengPlusException("媒资文件入库出错");
        }
        log.debug("媒资文件入库完成");
        //TODO：分块合并后将minio中的分块文件删除

        //分块文件删除
        for (File f:chunkFiles){
            try {
                f.delete();
            }catch (Exception e){
                log.debug("临时分块文件删除错误：{}", e.getMessage());
            }
        }
        //临时文件删除
        try {
            mergeFile.delete();
        }catch (Exception e){
            log.debug("临时合并文件删除错误：{}", e.getMessage());
        }


        return RestResponse.success();
    }

    @Override
    public MediaFiles getFileById(String mediaId) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(mediaId);
        if (mediaFiles == null || StringUtils.isEmpty(mediaFiles.getUrl())) {
            new XueChengPlusException("视频还没有转码处理");
        }
        return mediaFiles;
    }

    /**
     * 根据MD5和文件扩展名，生成文件路径，例 /2/f/2f6451sdg/2f6451sdg.mp4
     * @param fileMd5       文件MD5
     * @param extension     文件扩展名
     * @return
     */
    private String getFilePathByMd5(String fileMd5, String extension) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 +extension;
    }

    //得到分块文件的目录
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    /**
    // * 下载分块文件
    // * @param fileMd5       文件的MD5
    // * @param chunkTotal    总块数
    // * @return 分块文件数组
    // */
    private File[] checkChunkStatus(String fileMd5, int chunkTotal) {
        File []result=new File[chunkTotal];
        //获取分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        for (int i = 0; i < chunkTotal; i++) {
            //分块文件路径
            String chunkFilePath = chunkFileFolderPath + i;
            //采用临时文件储存
            File tempFile=null;
            try{
                tempFile=File.createTempFile("chunk"+i,null);
            }catch (Exception e){
                new XueChengPlusException("创建临时文件出错");
            }
            //下载分块文件
            tempFile=downloadFromMinio(tempFile,bucket_videoFiles,chunkFilePath);
            //封装入结果集
            result[i]=tempFile;
        }
        return result;
    }
    /**
     * 从Minio中下载文件
     * @param tempFile       下载储存的目标文件
     * @param bucket        桶
     * @param objectName    桶内文件路径
     * @return
     */
    public File downloadFromMinio(File tempFile, String bucket, String objectName) {
        //建立查询语句
        GetObjectArgs build = GetObjectArgs.builder().bucket(bucket).object(objectName).build();
        try{
            //获取对象io
            InputStream inputStream = minioClient.getObject(build);
            FileOutputStream outputStream=new FileOutputStream(tempFile);
            //写入文件
            IOUtils.copy(inputStream,outputStream);
            return tempFile;
        }catch (Exception e){
            new XueChengPlusException("查询文件分块出错");
        }

        return null;
    }
}
