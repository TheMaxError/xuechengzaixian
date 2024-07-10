package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService {

    /*
     * 根据课程id 查询课程老师
     * 参数： 课程id
     * 返回值: 查询到的所有老师信息
     * */
    List<CourseTeacher> selectByCourseId(Long id);

    /*
     * 根据前端发送的数据添加教师数据
     * 参数：提供的教师数据
     * 返回值: 添加的老师信息
     * */
    CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher);

    /*
     * 根据前端发送的数据修改教师数据
     * 参数：提供的教师数据
     * 返回值: 修改的老师信息
     * */
    void deleteCourseTeacher(Long courseId,Long teacherId);
}
