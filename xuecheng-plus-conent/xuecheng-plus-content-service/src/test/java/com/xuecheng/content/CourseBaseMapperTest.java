package com.xuecheng.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.apache.commons.lang.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseBaseMapperTest {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseBaseMapper.selectById(18);
        Assertions.assertNotNull(courseBase);

        //分页查询
        //查询条件
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //1.通过名称模糊查询
        queryWrapper.like(!StringUtils.isBlank(courseParamsDto.getCourseName()),CourseBase::getName,courseParamsDto.getCourseName());
        //2.根据审核状态查询（精确查询）
        queryWrapper.eq(!StringUtils.isBlank(courseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,courseParamsDto.getAuditStatus());
        //more:按课程发布状态查询
        queryWrapper.eq(!StringUtils.isBlank(courseParamsDto.getPublishStatus()),CourseBase::getStatus,courseParamsDto.getPublishStatus());
        //3.1创建分页参数对象

        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(20L);

        //3.2创建Page分页对象 参数：页码 每一页数据量
        Page<CourseBase> courseBasePage = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        //开始分页查询
        Page<CourseBase> courseBasePageResult = courseBaseMapper.selectPage(courseBasePage, queryWrapper);
        //数据列表
        List<CourseBase> records = courseBasePageResult.getRecords();
        //总数据量
        long total = courseBasePageResult.getTotal();

        //构建返回参数 需求参数为：List<T> items, long counts, long page, long pageSize
        System.out.println(new PageResult<CourseBase>(records, total, pageParams.getPageNo(), pageParams.getPageSize()));

    }

}
