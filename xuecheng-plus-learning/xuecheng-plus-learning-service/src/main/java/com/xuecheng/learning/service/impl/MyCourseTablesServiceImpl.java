package com.xuecheng.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.mapper.XcCourseTablesMapper;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MyCourseTablesServiceImpl implements MyCourseTablesService {
    @Autowired
    ContentServiceClient contentServiceClient;
    @Autowired
    XcChooseCourseMapper xcChooseCourseMapper;
    @Autowired
    XcCourseTablesMapper xcCourseTablesMapper;
    @Override
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId) {
        //1.获取选中课程
        CoursePublish coursePublish = contentServiceClient.getCoursePublish(courseId);
        //2.查看课程是否收费
        String charge = coursePublish.getCharge();
        XcChooseCourse chooseCourse=null;
        if("201000".equals(charge)){// 免费课程
            //向选课记录表、我的课程表添加数据
            log.info("添加免费课程..");
            chooseCourse=addFreeCourse(userId, coursePublish);
            XcCourseTables courseTables=addCourseTable(chooseCourse);

        }else{//收费课程
            log.info("添加收费课程..");
            chooseCourse=addChargeCourse(userId, coursePublish);
        }

        //获取学生学习资格
        XcCourseTablesDto courseTablesDto = getLearningStatus(userId,courseId);
        // 封装返回值
        XcChooseCourseDto chooseCourseDto = new XcChooseCourseDto();
        BeanUtils.copyProperties(chooseCourse, chooseCourseDto);
        chooseCourseDto.setLearnStatus(courseTablesDto.learnStatus);
        return chooseCourseDto;
    }




    @Override
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId) {
        //先在我的课表中进行查询，若没有则没有选课成功，返回返回状态码为”702002”的对象
        //若查询到则，查看当前的学习状态，若过期则不可学习，返回返回状态码为”702003”的对象
        XcCourseTablesDto xcCourseTablesDto=new XcCourseTablesDto();
        XcCourseTables myCourse=getXcCourseTables(userId, courseId);
        if(myCourse==null){//未查到
            xcCourseTablesDto=new XcCourseTablesDto();
            xcCourseTablesDto.setLearnStatus("702002");
            return xcCourseTablesDto;

        }
        //查到了查看结束时间与当前时间的比较
        boolean isExpires = LocalDateTime.now().isAfter(myCourse.getValidtimeEnd());
        if(isExpires){//课程有效时间已过
            BeanUtils.copyProperties(myCourse, xcCourseTablesDto);
            xcCourseTablesDto.setLearnStatus("702003");
            return xcCourseTablesDto;
        }else{
            BeanUtils.copyProperties(myCourse, xcCourseTablesDto);
            xcCourseTablesDto.setLearnStatus("702001");
            return xcCourseTablesDto;
        }

    }

    @Override
    @Transactional
    public boolean saveChooseCourseStatus(String chooseCourseId) {
        // 1. 根据选课id，查询选课表
        XcChooseCourse chooseCourse = xcChooseCourseMapper.selectById(chooseCourseId);
        if (chooseCourse == null) {
            log.error("接收到购买课程的消息，根据选课id未查询到课程，选课id：{}", chooseCourseId);
            return false;
        }
        // 2. 选课状态为未支付时，更新选课状态为选课成功
        if ("701002".equals(chooseCourse.getStatus())) {
            chooseCourse.setStatus("701001");
            int update = xcChooseCourseMapper.updateById(chooseCourse);
            if (update <= 0) {
                log.error("更新选课记录失败：{}", chooseCourse);
            }
        }
        // 3. 向我的课程表添加记录
        addCourseTable(chooseCourse);
        return true;
    }

    @Override
    public PageResult<XcCourseTables> myCourseTables(MyCourseTableParams params) {
        // 1. 获取页码
        int pageNo = params.getPage();
        // 2. 设置每页记录数，固定为4
        long pageSize = 4;
        // 3. 分页条件
        Page<XcCourseTables> page = new Page<>(pageNo, pageSize);
        // 4. 根据用户id查询课程
        String userId = params.getUserId();
        LambdaQueryWrapper<XcCourseTables> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XcCourseTables::getUserId, userId);
        // 5. 分页查询
        Page<XcCourseTables> pageResult = xcCourseTablesMapper.selectPage(page, queryWrapper);
        // 6. 获取记录总数
        long total = pageResult.getTotal();
        // 7. 获取记录
        List<XcCourseTables> records = pageResult.getRecords();
        // 8. 封装返回
        return new PageResult<>(records, total, pageNo, pageSize);
    }
    /**
     * 将付费课程加入到选课记录表
     *
     * @param userId   用户id
     * @param coursePublish 课程id
     * @return 选课记录
     */
    private XcChooseCourse addChargeCourse(String userId, CoursePublish coursePublish) {
        LambdaQueryWrapper<XcChooseCourse> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(XcChooseCourse::getCourseId,coursePublish.getId())
                .eq(XcChooseCourse::getUserId,userId)
                .eq(XcChooseCourse::getOrderType,"700002")//收费课程
                .eq(XcChooseCourse::getStatus, "701002");// 待支付
        // 由于可能存在多条，所以这里用selectList
        List<XcChooseCourse> chooseCourses = xcChooseCourseMapper.selectList(queryWrapper);
        //  如果已经存在对应的选课数据，返回一条即可
        if (!chooseCourses.isEmpty()) {
            return chooseCourses.get(0);
        }
        //数据库中没有数据，进行存储
        XcChooseCourse chooseCourse=new XcChooseCourse();
        chooseCourse.setCourseId(coursePublish.getId());
        chooseCourse.setCourseName(coursePublish.getName());
        chooseCourse.setUserId(userId);
        chooseCourse.setCompanyId(coursePublish.getCompanyId());
        chooseCourse.setOrderType("700002");
        chooseCourse.setCreateDate(LocalDateTime.now());
        chooseCourse.setCoursePrice(coursePublish.getPrice());
        chooseCourse.setValidDays(365);
        chooseCourse.setStatus("701002");
        chooseCourse.setValidtimeStart(LocalDateTime.now());
        chooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));
        int insert = xcChooseCourseMapper.insert(chooseCourse);
        if(insert<0){
            throw new XueChengPlusException("添加选课记录失败");
        }
        return chooseCourse;
    }
    /**
     * 将免费课程加入到选课记录表
     *
     * @param userId   用户id
     * @param coursePublish 课程id
     * @return 选课记录
     */
    private XcChooseCourse addFreeCourse(String userId, CoursePublish coursePublish) {
        //由于数据库中没有约束，所以可能存在重复添加的情况，我们需要事先做一下判断
        LambdaQueryWrapper<XcChooseCourse> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(XcChooseCourse::getCourseId,coursePublish.getId())
                .eq(XcChooseCourse::getUserId,userId)
                .eq(XcChooseCourse::getOrderType,"700001")//免费课程
                .eq(XcChooseCourse::getStatus, "701001");//选课成功
        //由于可能存在多条，所以这里用selectList接收
        List<XcChooseCourse> chooseCourses = xcChooseCourseMapper.selectList(queryWrapper);
        if (!chooseCourses.isEmpty()) {//若已经选中了直接返回，不需要在数据库中做修改
            return chooseCourses.get(0);
        }
        // 数据库中不存在数据，添加选课信息，对照着数据库中的属性挨个set即可
        XcChooseCourse chooseCourse = new XcChooseCourse();
        chooseCourse.setCourseId(coursePublish.getId());
        chooseCourse.setCourseName(coursePublish.getName());
        chooseCourse.setUserId(userId);
        chooseCourse.setCompanyId(coursePublish.getCompanyId());
        chooseCourse.setOrderType("700001");
        chooseCourse.setCreateDate(LocalDateTime.now());
        chooseCourse.setCoursePrice(coursePublish.getPrice());
        chooseCourse.setValidDays(365);
        chooseCourse.setStatus("701001");
        chooseCourse.setValidtimeStart(LocalDateTime.now());
        chooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));
        int insert = xcChooseCourseMapper.insert(chooseCourse);
        if(insert<0){
            throw new XueChengPlusException("添加选课记录失败");
        }
        return chooseCourse;
    }
    /**
     * 添加到我的课程表
     * @param chooseCourse 选课记录
     */
    private XcCourseTables addCourseTable(XcChooseCourse chooseCourse) {
        //我的选课表的记录来源于选课记录，选课记录成功，将课程信息添加到我的课程表
        //如果我的课程表已经存在课程，课程可能已经过期，如果有新的选课记录，则需要更新我的课程表中的现有信息
        String status = chooseCourse.getStatus();
        if (!"701001".equals(status)) {
            throw new XueChengPlusException("选课未成功，无法添加到课程表");
        }
        XcCourseTables courseTables = getXcCourseTables(chooseCourse.getUserId(), chooseCourse.getCourseId());
        if (courseTables != null) {//我的课程表中已经有该课程了
            return courseTables;
        }
        //课程表中没有该课程，添加
        courseTables = new XcCourseTables();
        BeanUtils.copyProperties(chooseCourse, courseTables);
        courseTables.setChooseCourseId(chooseCourse.getId());
        courseTables.setCourseType(chooseCourse.getOrderType());
        courseTables.setUpdateDate(LocalDateTime.now());
        int insert = xcCourseTablesMapper.insert(courseTables);
        if (insert <= 0) {
            throw new XueChengPlusException("添加我的课程表失败");
        }
        return courseTables;
    }
    /**
     * 根据用户id和课程id查询我的课程表中的某一门课程
     *
     * @param userId   用户id
     * @param courseId 课程id
     * @return 我的课程表中的课程
     */
    private XcCourseTables getXcCourseTables(String userId, Long courseId) {
        return xcCourseTablesMapper.selectOne(new LambdaQueryWrapper<XcCourseTables>()
                .eq(XcCourseTables::getCourseId,courseId)
                .eq(XcCourseTables::getUserId,userId));
    }

}
