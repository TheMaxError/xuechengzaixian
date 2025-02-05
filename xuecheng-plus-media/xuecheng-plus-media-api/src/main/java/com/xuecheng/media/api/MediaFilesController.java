package com.xuecheng.media.api;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 *
 * @version 1.0
 * @description 媒资文件管理接口
 *
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
public class MediaFilesController {
    @Autowired
    MediaFileService mediaFileService;

    @ApiOperation("媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiels(companyId, pageParams, queryMediaParamsDto);
    }

    @ApiOperation("上传课程文件")
    @RequestMapping(value = "/upload/coursefile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public UploadFileResultDto uploadCourseFile(@RequestPart("filedata") MultipartFile upload,
                                                @RequestParam(value = "folder",required=false) String folder,
                                                @RequestParam(value = "objectName",required=false) String objectName) {





        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        //文件大小
        uploadFileParamsDto.setFileSize(upload.getSize());
        String contentType = upload.getContentType();
        if(contentType.contains("image")){//图片
            uploadFileParamsDto.setFileType("001001");
        }
        //文件名称
        uploadFileParamsDto.setFilename(upload.getOriginalFilename());
        //文件类型
        uploadFileParamsDto.setContentType(contentType);
        //创建临时文件
        try {
            File tempFile = File.createTempFile("minio", "temp");
            upload.transferTo(tempFile);
            //文件路径
            String absolutePath = tempFile.getAbsolutePath();
            //上传文件
            UploadFileResultDto uploadFileResultDto = mediaFileService.uploadFile(companyId, uploadFileParamsDto, absolutePath,folder,objectName);
            return uploadFileResultDto;
        }catch (Exception e){
            new XueChengPlusException("上传文件过程出错");
        }

        return null;

    }

}
