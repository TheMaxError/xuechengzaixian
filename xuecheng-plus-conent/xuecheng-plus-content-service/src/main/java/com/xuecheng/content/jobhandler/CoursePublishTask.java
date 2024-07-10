package com.xuecheng.content.jobhandler;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {
    @Autowired
    CoursePublishService coursePublishService;

    @XxlJob("CoursePublishJobHandler")
    private void coursePublishJobHandler() {
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        //todo:将固定的课程发布信息改为可接受任意的课程信息，如删除，
        // 对于删除，获取到删除的信息后，查询课程发布表，若已经发布，将在es的索引和minio的存储文件删除
        process(shardIndex, shardTotal, "course_publish", 5, 60);
        log.debug("测试任务执行中...");
    }

    @Override
    public boolean execute(MqMessage mqMessage) {
        log.debug("开始执行课程发布任务，课程id：{}", mqMessage.getBusinessKey1());
        // TODO 将课程信息静态页面上传至MinIO
        String courseId = mqMessage.getBusinessKey1();
        generateCourseHtml(mqMessage,Long.valueOf(courseId));

        // TODO 存储到Redis

        // TODO 存储到ElasticSearch
        saveCourseIndex(mqMessage,Long.valueOf(courseId));
        return true;
    }

    private void generateCourseHtml(MqMessage mqMessage, Long courseId) {
        //幂等性比较
        //1.1获取任务id
        Long id = mqMessage.getId();
        //1.2获取任务完成情况
        MqMessageService mqMessageService = getMqMessageService();
        int stageOne = mqMessageService.getStageOne(id);
        if(stageOne==1){//本次任务为阶段任务1，查看任务1状态即可
            log.debug("当前阶段为静态化课程信息任务，已完成，无需再次处理，任务信息：{}", mqMessage);
            return;
        }
        //2.确定任务未完成，进行上传minio操作
        //2.1生成静态页面
        File file = coursePublishService.generateCourseHtml(courseId);
        if (file == null) {
            throw new XueChengPlusException("课程静态化异常");
        }
        //将静态页面上传入minio
        coursePublishService.uploadCourseHtml(courseId,file);
        //保存阶段1的任务状态
        mqMessageService.completedStageOne(id);
    }
    public void saveCourseIndex(MqMessage mqMessage, Long courseId) {
        //幂等性比较
        //1.1获取任务id
        Long id = mqMessage.getId();
        //1.2获取任务完成情况
        MqMessageService mqMessageService = getMqMessageService();
        int stageTwo= mqMessageService.getStageTwo(id);
        if(stageTwo==1){//本次任务为阶段任务1，查看任务1状态即可
            log.debug("当前阶段为创建课程索引任务，已完成，无需再次处理，任务信息：{}", mqMessage);
            return;
        }
        //2.确定任务未完成，进行es索引对象增加操作
        Boolean result = coursePublishService.saveCourseIndex(courseId);
        //远程调用保存课程索引接口，将课程信息上传至ElasticSearch
        if (result) {
            mqMessageService.completedStageTwo(id);
        }
    }
}
