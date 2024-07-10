package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryDto;

import java.util.List;

public interface CourseCategoryService {

    /*
     * 课程分类树形结构查询
     * 参数：课程树的起始查询id
     * 返回值:课程分类树结果
     * */
     List<CourseCategoryDto> queryTreeNodes(String id);


}
