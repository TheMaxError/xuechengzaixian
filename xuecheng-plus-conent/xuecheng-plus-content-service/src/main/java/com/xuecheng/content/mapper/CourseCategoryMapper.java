package com.xuecheng.content.mapper;

import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    //树形结构的递归查询
    List<CourseCategoryDto> selectTreeNodes(String id);

}
