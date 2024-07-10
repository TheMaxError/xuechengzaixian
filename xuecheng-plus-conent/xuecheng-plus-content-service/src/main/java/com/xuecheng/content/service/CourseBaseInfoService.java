package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

//课程信息管理接口
public interface CourseBaseInfoService {
    /*
    * 分页查询
    * 参数：分页查询参数（包含页码，每一页数据量） 查询条件
    * 返回值:分页查询的结果
    * */

    PageResult<CourseBase> queryCourseBaseList(Long companyId,PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /*
     * 新增课程
     * 参数：机构id 添加的课程信息
     * 返回值:课程的详细结果
     * */
    CourseBaseInfoDto creatCourseBase(Long companyId,AddCourseDto addCourseDto);
    /*
     * 查询课程
     * 参数：课程id
     * 返回值:课程的详细结果
     * */
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    /*
     * 修改课程
     * 参数：机构id 修改的信息
     * 返回值:课程的详细结果
     * */

    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    /*
     * 删除课程
     * 参数：课程id
     *
     * */
    void deleteCourseBase(Long companyId,Long courseId);
}
