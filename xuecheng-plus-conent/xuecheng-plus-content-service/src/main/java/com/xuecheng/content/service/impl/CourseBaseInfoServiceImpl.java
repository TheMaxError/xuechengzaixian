package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Autowired
    CourseTeacherMapper courseTeacherMapper;
    @Override
    public PageResult<CourseBase> queryCourseBaseList(Long companyId,PageParams pageParams, QueryCourseParamsDto courseParamsDto) {
        //分页查询
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseBase::getCompanyId, companyId);
        //1.通过名称模糊查询
        queryWrapper.like(!StringUtils.isBlank(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
        //2.根据审核状态查询（精确查询）
        queryWrapper.eq(!StringUtils.isBlank(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());
        //more:按课程发布状态查询
        queryWrapper.eq(!StringUtils.isBlank(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());

        //3创建Page分页对象 参数：页码 每一页数据量
        Page<CourseBase> courseBasePage = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        //开始分页查询
        Page<CourseBase> courseBasePageResult = courseBaseMapper.selectPage(courseBasePage, queryWrapper);
        //获取数据列表
        List<CourseBase> records = courseBasePageResult.getRecords();
        //获取总数据量
        long total = courseBasePageResult.getTotal();

        //构建返回参数 需求参数为：List<T> items, long counts, long page, long pageSize
        return new PageResult<CourseBase>(records, total, pageParams.getPageNo(), pageParams.getPageSize());

    }

    @Transactional
    @Override
    public CourseBaseInfoDto creatCourseBase(Long companyId, AddCourseDto addCourseDto) {
//        //合法性校验
//        if (StringUtils.isBlank(addCourseDto.getName())) {
//            throw new XueChengPlusException("课程名称为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getMt())) {
//            throw new XueChengPlusException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getSt())) {
//            throw new XueChengPlusException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getGrade())) {
//            throw new XueChengPlusException("课程等级为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
//            throw new XueChengPlusException("教育模式为空");
//        }
//
//        if (StringUtils.isBlank(addCourseDto.getUsers())) {
//            throw new XueChengPlusException("适应人群");
//        }


        //向课程基本数据course_base表写入信息
        CourseBase newCourseBase = new CourseBase();
        //从原始数据中拷贝数据到newCourseBase（元素名称一样即可拷贝）注意:会把原来的元素进行覆盖
        BeanUtils.copyProperties(addCourseDto, newCourseBase);
        newCourseBase.setCompanyId(companyId);
        newCourseBase.setCreateDate(LocalDateTime.now());
        //设置新添加课程的发布状态和审核状态
        newCourseBase.setAuditStatus("202002");
        newCourseBase.setStatus("203001");
        //插入数据库
        int insert = courseBaseMapper.insert(newCourseBase);
        if (insert <= 0) {
            throw new RuntimeException("添加课程失败");
        }

        //向课程营销表course_market写入信息
        CourseMarket newCourseMarket = new CourseMarket();
        //将页面数据进行拷贝
        BeanUtils.copyProperties(addCourseDto, newCourseMarket);
        //获取课程主键id
        Long id = newCourseBase.getId();
        newCourseMarket.setId(id);
        //通过方法实现保存营销信息若课程营销表已经存在则更新，否则新增
        saveCourseMarket(newCourseMarket);
        //通过课程ID在数据库查询课程详细的信息
        return getCourseBaseInfo(id);

    }
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        //从课程基本信息表查询
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            throw  new XueChengPlusException("该课程不存在");
        }
        //从课程营销表查询
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //通过从课程基本信息表查询得到的courseBase的mt和st从分类字典表里查询大小分类信息
        CourseCategory MtCourseCategory = courseCategoryMapper.selectById(courseBase.getMt());
        CourseCategory StCourseCategory = courseCategoryMapper.selectById(courseBase.getSt());
        //整合
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }
        courseBaseInfoDto.setMtName(MtCourseCategory.getName());
        courseBaseInfoDto.setStName(StCourseCategory.getName());
        return courseBaseInfoDto;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        //更具课程id查询到课程基本信息
        Long courseId = dto.getId();
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        if(courseBaseInfo==null){
            throw new XueChengPlusException("课程不存在");
        }
        //数据校验
        //本机构只能修改本机构的课程
        if (!courseBaseInfo.getCompanyId().equals(companyId)) {
            throw new XueChengPlusException("无法修改不同机构的课程");
        }
        // todo:价格应该做数据校验，原价比现价高且均要>=0
        //封装数据
        BeanUtils.copyProperties(dto,courseBaseInfo);
        courseBaseInfo.setChangeDate(LocalDateTime.now());
        //更新基础数据库
        int i = courseBaseMapper.updateById(courseBaseInfo);
        if(i<=0){
            throw new XueChengPlusException("更新基本数据库信息失败");
        }
        //封装营销数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(courseBaseInfo,courseMarket);
        //修改营销表信息
        int i1 = courseMarketMapper.updateById(courseMarket);
        if(i1<=0){
            throw new XueChengPlusException("更新营销数据库信息失败");
        }
        return getCourseBaseInfo(courseBaseInfo.getId());
    }

    @Override
    public void deleteCourseBase(Long companyId,Long courseId) {
        //参数校验
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(!courseBase.getCompanyId().equals(companyId)){
            throw new XueChengPlusException("只能修改本机构的课程");
        }
        //删除教师数据库
        LambdaQueryWrapper<CourseTeacher> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(CourseTeacher::getCourseId,courseBase.getId());
        courseTeacherMapper.delete(queryWrapper1);
        //课程计划数据库
        LambdaQueryWrapper<Teachplan> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(Teachplan::getCourseId,courseBase.getId());
        teachplanMapper.delete(queryWrapper2);
        //删除营销数据库
        courseMarketMapper.deleteById(courseId);
        //删除课程数据库
        courseBaseMapper.deleteById(courseId);
    }

    private int saveCourseMarket(CourseMarket newCourseMarket) {
        //校验参数合法性
        if (StringUtils.isBlank(newCourseMarket.getCharge())) {
            throw new XueChengPlusException("收费规则为空");
        }
        if (newCourseMarket.getCharge().equals("201001")) {// todo: 原价应该比现价高，且原价>=0
            if (newCourseMarket.getPrice() == null || newCourseMarket.getPrice().floatValue() <= 0) {
                throw new XueChengPlusException("课程的价格不能为空并且必须大于0");
            }
        }
        //从数据库查询营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(newCourseMarket.getId());
        if (courseMarket == null) {
            //不存在，插入数据库
            return courseMarketMapper.insert(newCourseMarket);
        } else {
            //更新数据库
            //将newCourseMarket拷贝到courseMarket
            BeanUtils.copyProperties(newCourseMarket, courseMarket);
            courseMarket.setId(newCourseMarket.getId());
            return courseMarketMapper.updateById(courseMarket);
        }
    }
}
