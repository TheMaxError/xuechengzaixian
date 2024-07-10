package com.xuecheng.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.base.exception.CommonError;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaClient.MediaServiceClient;
import com.xuecheng.content.feignclient.SearchClient.SearchServiceClient;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.CourseTeacherService;
import com.xuecheng.content.service.TeachplanService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.search.po.CourseIndex;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CoursePublishServiceImpl implements CoursePublishService {
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;
    @Autowired
    private TeachplanService teachplanService;
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseTeacherService courseTeacherService;
    @Autowired
    CoursePublishPreMapper coursePublishPreMapper;
    @Autowired
    CoursePublishMapper coursePublishMapper;
    @Autowired
    MqMessageService mqMessageService;
    @Autowired
    CoursePublishService coursePublishService;
    @Autowired
    MediaServiceClient mediaServiceClient;
    @Autowired
    SearchServiceClient searchServiceClient;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        //查询课程信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        //获取课程计划信息
        List<TeachplanDto> teachplanDtos = teachplanService.findTeachplanTree(courseId);
        //封装数据
        coursePreviewDto.setCourseBase(courseBaseInfo);
        coursePreviewDto.setTeachplans(teachplanDtos);
        return coursePreviewDto;
    }

    @Override
    public void commitAudit(Long courseId, Long companyId) {
        //查询课程基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        //查询课程的营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //查询课程基本信息和营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        //获取课程教育计划信息
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        //查询课程教师信息
        List<CourseTeacher> courseTeachers = courseTeacherService.selectByCourseId(courseId);
        //进行约束条件判断
        // 获取 审核状态
        String auditStatus = courseBaseInfo.getAuditStatus();
        // 1.1 审核完后，方可提交审核
        if ("202003".equals(auditStatus)) {
            throw new XueChengPlusException("该课程现在属于待审核状态，审核完成后可再次提交");
        }
        // 1.2 本机构只允许提交本机构的课程
        if (!companyId.equals(courseBaseInfo.getCompanyId())) {
            throw new XueChengPlusException("本机构只允许提交本机构的课程");
        }
        // 1.3 没有上传图片，不允许提交
        if (StringUtils.isEmpty(courseBaseInfo.getPic())) {
            throw new XueChengPlusException("没有上传课程封面，不允许提交审核");
        }
        // 1.4 没有添加课程计划，不允许提交审核
        if (teachplanTree.isEmpty()) {
            throw new XueChengPlusException("没有添加课程计划，不允许提交审核");
        }

        //准备课程预处理表的信息
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        coursePublishPre.setMarket(JSON.toJSONString(courseMarket));
        coursePublishPre.setTeachplan(JSON.toJSONString(courseMarket));
        coursePublishPre.setTeachers(JSON.toJSONString(courseTeachers));
        coursePublishPre.setCompanyId(companyId);
        coursePublishPre.setCreateDate(LocalDateTime.now());
        // 3. 设置预发布记录状态为已提交
        coursePublishPre.setStatus("202003");
        // 判断是否已经存在预发布记录，若存在，则更新
        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreUpdate == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        // 4. 设置课程基本信息审核状态为已提交
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }

    @Transactional
    @Override
    public void publishCourse(Long courseId, Long companyId) {
        //获取课程的预发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        //数据约束
        String status = coursePublishPre.getStatus();//获取审核状态
        if (coursePublishPre == null) {
            throw new XueChengPlusException("请先提交课程审核，审核通过后方可发布");
        }
        // 1.2 课程审核通过后，方可发布
        if (!"202004".equals(status)) {
            throw new XueChengPlusException("操作失败，课程审核通过后方可发布");
        }
        // 1.3 本机构只允许发布本机构的课程
        if (!coursePublishPre.getCompanyId().equals(companyId)) {
            throw new XueChengPlusException("操作失败，本机构只允许发布本机构的课程");
        }

        // 2. 向课程发布表插入数据
        saveCoursePublish(coursePublishPre);
        // 3. 向消息表插入数据
        saveCoursePublishMessage(courseId);
        // 4. 删除课程预发布表对应记录
        coursePublishPreMapper.deleteById(courseId);
    }

    @Override
    public File generateCourseHtml(Long courseId) {
        File htmlFile = null;
        try {
            //配置freemarker
            Configuration configuration = new Configuration(Configuration.getVersion());
            //加载模板
            //选指定模板路径,classpath下templates下
            //得到classpath路径 由于带有中文路径，故此处采用 System.getProperty("user.dir");
            String property = System.getProperty("user.dir");
            String classpath = property + "/xuecheng-plus-conent/xuecheng-plus-content-service/src/main/resources/";
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
            //设置字符编码
            configuration.setDefaultEncoding("utf-8");

            //指定模板文件名称
            Template template = configuration.getTemplate("course_template.ftl");

            //准备数据
            CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);

            Map<String, Object> map = new HashMap<>();
            map.put("model", coursePreviewInfo);

            //静态化
            //参数1：模板，参数2：数据模型
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);//转为html文件
            //将静态化内容输出到文件中
            InputStream inputStream = IOUtils.toInputStream(content);
            //输出流
            htmlFile = File.createTempFile("course", ".html");
            FileOutputStream outputStream = new FileOutputStream(htmlFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.debug("课程静态化失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return htmlFile;
    }

    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        String course = mediaServiceClient.upload(multipartFile, "course", courseId + ".html");
        if (course == null) {
            throw new XueChengPlusException("远程调用媒资服务上传文件失败");
        }

    }

    @Override
    public Boolean saveCourseIndex(Long courseId) {
        //取出课程发布表的数据
        CoursePublish coursePublish = coursePublishMapper.selectById(courseId);
        //封装入课程索引对象
        CourseIndex courseIndex = new CourseIndex();
        BeanUtils.copyProperties(coursePublish, courseIndex);
        //在es中进行课程索引对象的新建
        Boolean result = searchServiceClient.add(courseIndex);
        if (!result) {
            throw new XueChengPlusException("添加索引失败");
        }
        return true;
    }

    @Override
    public CoursePublish getCoursePunlish(Long courseId) {
        return coursePublishMapper.selectById(courseId);
    }

    @Override
    public CoursePublish getCoursePublishCache(Long courseId) {
//        synchronized (this) {
        // 1. 先从缓存中查询
        String courseCacheJson = (String) redisTemplate.opsForValue().get("course:" + courseId);
        // 2. 如果缓存里有，直接返回
        if (!StringUtils.isEmpty(courseCacheJson)) {
            log.debug("从缓存中查询");
            if ("null".equals(courseCacheJson)) {
                return null;
            }

            CoursePublish coursePublish = JSON.parseObject(courseCacheJson, CoursePublish.class);
            return coursePublish;
        } else {
//                synchronized (this) {
            RLock lock = redissonClient.getLock("courseQueryLock" + courseId);
            lock.lock();
            try {


                // double check
                courseCacheJson = (String) redisTemplate.opsForValue().get("course:" + courseId);
                if (!StringUtils.isEmpty(courseCacheJson)) {
                    log.debug("从缓存中查询");
                    if ("null".equals(courseCacheJson)) {
                        return null;
                    }
                    CoursePublish coursePublish = JSON.parseObject(courseCacheJson, CoursePublish.class);
                    return coursePublish;
                }

                log.debug("缓存中没有，查询数据库");
                // 3. 如果缓存里没有，查询数据库
                CoursePublish coursePublish = coursePublishMapper.selectById(courseId);
                if (coursePublish == null) {
                    redisTemplate.opsForValue().set("course:" + courseId, "null", 30, TimeUnit.SECONDS);
                    return null;
                }
                String jsonString = JSON.toJSONString(coursePublish);
                // 3.1 将查询结果缓存
                redisTemplate.opsForValue().set("course:" + courseId, jsonString);
                // 3.1 返回查询结果
                return coursePublish;
            }finally {
                lock.unlock();
            }
        }
//            }
//        }
    }

    private void saveCoursePublishMessage(Long courseId) {
        MqMessage mqMessage = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
        if (mqMessage == null) {
            throw new XueChengPlusException(CommonError.UNKOWN_ERROR.getErrMessage());
        }
    }

    private void saveCoursePublish(CoursePublishPre coursePublishPre) {
        CoursePublish coursePublish = new CoursePublish();
        // 拷贝数据
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        //设置发布状态
        coursePublish.setStatus("203002");
        //查询课程发布表中信息，看是更新还是插入
        CoursePublish oldCoursePublish = coursePublishMapper.selectById(coursePublishPre.getId());
        if (oldCoursePublish == null) {
            coursePublishMapper.insert(coursePublish);
        } else {
            coursePublishMapper.updateById(coursePublish);
        }
        // 更新课程基本信息表的发布状态为已发布
        CourseBase courseBase = courseBaseMapper.selectById(coursePublishPre.getId());
        courseBase.setAuditStatus("203002");
        courseBaseMapper.updateById(courseBase);
    }
}
