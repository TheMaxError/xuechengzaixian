package com.xuecheng.media.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

 /**
  * @description 媒资文件查询方法
  * @param pageParams 分页参数
  * @param queryMediaParamsDto 查询条件
  * @return 分页插曲全部结果
 */
 public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

 /**
  * 上传文件
  * @param companyId 机构id
  * @param uploadFileParamsDto 上传文件信息
  * @param localFilePath 文件磁盘路径
  * @param objectName 对象名
  * @return 文件信息
  */

 public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath,String folder,String objectName);

 /**
  *
  *
  * @description 将文件信息添加到文件表
  * @param companyId  机构id
  * @param fileMd5  文件md5值
  * @param uploadFileParamsDto  上传文件的信息
  * @param bucket  桶
  * @param objectName 对象名称
  * @return 插入成功的结果对象
  */
 public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName);

 /**
  *
  *
  * @description 检查文件是否存在
  * @param fileMd5  文件md5值
  * @return 响应文件是否存在 true则存在反之不存在
  */
 public RestResponse<Boolean>checkFile(String fileMd5);

 /**
  *
  *
  * @description 检查文件分块是否存在
  * @param fileMd5 文件md5值
  * @param chunkIndex 分块文件的序号
  * @return 响应分块文件是否存在 true则存在反之不存在
  */
 public RestResponse<Boolean> checkchunk(String fileMd5, int chunkIndex);

 /**
  *
  *
  * @description 上传分块
  * @param fileMd5 文件md5值
  * @param chunkIndex 分块文件的序号
  * @param localChunkFilePath 分块文件在本地的路径
  * @return 响应分块文件是否存在 true则存在反之不存在
  */
 public RestResponse uploadChunk(String fileMd5, int chunkIndex,String localChunkFilePath);

 /**
  *
  *
  * @description 分块合并
  * @param companyId 机构id
  * @param fileMd5 文件md5值
  * @param chunkTotal 分块文件的多少
  * @param uploadFileParamsDto 文件信息
  * @return 响应分块文件是否存在 true则存在反之不存在
  */
 public RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);
 /**
  *
  *
  * @description 通过id获取视频文件
  * @param mediaId 文件id
  * @return 文件信息
  */
 MediaFiles getFileById(String mediaId);
 /**
  * 从Minio中下载文件
  * @param tempFile       下载储存的目标文件
  * @param bucket        桶
  * @param objectName    桶内文件路径
  * @return
  */
  File downloadFromMinio(File tempFile, String bucket, String objectName);
 /**
  * @description 将文件写入minIO
  * @param localFilePath  文件地址
  * @param mimeType 媒资类型
  * @param bucket  桶
  * @param objectName 对象名称
  * @return 是否添加成功
  */
 public boolean addMediaFilesToMinIO(String localFilePath,String mimeType,String bucket, String objectName);


}
