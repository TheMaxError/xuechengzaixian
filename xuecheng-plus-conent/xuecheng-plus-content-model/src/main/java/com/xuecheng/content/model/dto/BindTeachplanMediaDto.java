package com.xuecheng.content.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BindTeachplanMediaDto", description = "教学计划-媒资绑定DTO")
public class BindTeachplanMediaDto {
    @ApiModelProperty(value = "媒资文件id", required = true)
    private String mediaId;

    @ApiModelProperty(value = "媒资文件名称", required = true)
    @JsonProperty("fileName")
    //进行前端字段的手动映射到后端类数据中，原先需要两字段数据名称相同，这样注解后前端发送fileName字段，后端映射成自己起名字的字段
    private String mediaFilename;

    @ApiModelProperty(value = "课程计划标识", required = true)
    private Long teachplanId;
}
