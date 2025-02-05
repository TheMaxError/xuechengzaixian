package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

import java.io.File;

/**
 * 课程预览、发布接口
 */
public interface CoursePublishService {

    /**
     * 根据课程id获取课程预览信息
     * @param courseId  课程id
     * @return CoursePreviewDto 渲染生成视图的数据模型
     */
    CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * 提交审核
     * @param courseId  课程id
     */
    void commitAudit(Long courseId ,Long companyId);

    /**
     * 发布课程
     * @param companyId
     * @param courseId
     */
    void publishCourse(Long courseId, Long companyId);

    /**
     * 课程静态化
     * @param courseId  课程id
     * @return  静态化文件
     */
    File generateCourseHtml(Long courseId);

    /**
     * 上传课程静态化页面
     * @param courseId  课程id
     * @param file  静态化文件
     */
    void uploadCourseHtml(Long courseId, File file);

    /**
     * 保存课程索引
     * @param courseId  课程id
     * @return
     */
    Boolean saveCourseIndex(Long courseId);

    CoursePublish getCoursePunlish(Long courseId);

    /**
     * 查询缓存中的课程发布信息
     * @param courseId
     * @return  课程发布信息
     */
    CoursePublish getCoursePublishCache(Long courseId);
}
