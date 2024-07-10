package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@Api(value = "课程发布接口",tags = "课程发布接口")
public class CoursePublishController {
    @Autowired
    CoursePublishService coursePublishService;

    @ApiOperation("课程详细信息页面展示接口")
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId){
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("course_template");
        modelAndView.addObject("model", coursePreviewInfo);
        modelAndView.getModel();
        return modelAndView;
    }
    @PostMapping("/courseaudit/commit/{courseId}")
    @ApiOperation("课程提交审核接口")
    public void commitAudit(@PathVariable Long courseId) {
        Long companyID=1232141425L;
        coursePublishService.commitAudit(courseId,companyID);
    }

    @PostMapping("/coursepublish/{courseId}")
    @ApiOperation("课程发布接口")
    public void coursePublish(@PathVariable Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.publishCourse(courseId, companyId);
    }

    @ApiOperation("查询课程发布信息")
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
        return coursePublishService.getCoursePunlish(courseId);
    }

    @ApiOperation("获取课程发布信息")
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getCoursePreviewDto(@PathVariable("courseId") Long courseId) {
        return coursePublishService.getCoursePreviewInfo(courseId);
    }

}
