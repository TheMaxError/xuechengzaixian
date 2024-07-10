package com.xuecheng.content.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 使用freemarker渲染生成视图的数据模型
 */
@Data
public class CoursePreviewDto {
    /**
     * 课程基本计划、课程营销信息
     */
    CourseBaseInfoDto courseBase;

    /**
     * 课程计划信息
     */
    List<TeachplanDto> teachplans;

    /**
     * 师资信息暂时不加
     */
}
