package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;


    public List<TeachplanDto> findTeachplanTree(Long courseId){
        return teachplanMapper.selectTreeNodes(courseId);
    }
    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过课程id识别是新增还是修改
        Long id = saveTeachplanDto.getId();
        if(id==null){
            //新增
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(saveTeachplanDto.getCourseId(), saveTeachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(saveTeachplanDto,teachplanNew);
            teachplanMapper.insert(teachplanNew);

        }else{
            //修改
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);

        }

    }
    @Transactional
    @Override
    public void deleteTeachplanById(Long id) {
        if(id==null){
            throw new XueChengPlusException("课程计划id为空");
        }
        Teachplan teachplan = teachplanMapper.selectById(id);
        if(teachplan.getGrade()==1){//大章节处理
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid,teachplan.getId());
            Integer num = teachplanMapper.selectCount(queryWrapper);//查看大章节是否含有子章节
            if(num!=0){
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }else{
                teachplanMapper.deleteById(id);
            }
        }else{//小章节处理
            teachplanMapper.deleteById(id);
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId,teachplan.getId());
            teachplanMediaMapper.delete(queryWrapper);//对小章节关联的媒资表进行删除
        }
    }
    @Transactional
    @Override
    public void sortTeachplanById(String type, Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();//用于封装sql语句
        Integer grade = teachplan.getGrade();//进行判断是大章节还是小章节

        if(grade==1){//大章节
            if("moveup".equals(type)){//上移动

                queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId())//同课程
                        .eq(Teachplan::getGrade,teachplan.getGrade())//同等级(大章节)
                        .lt(Teachplan::getOrderby,teachplan.getOrderby())//小于本章节的排序字段（越小越上）
                        .orderByDesc(Teachplan::getOrderby)//倒序方便拿数据
                        .last("LIMIT 1");//限制只取1个数据

                Teachplan teachplan1 = teachplanMapper.selectOne(queryWrapper);//查询
                //交换两个数据的orderby，并更新数据库
                exchangeTeachplanOrder(teachplan1, teachplan);

            }else if("movedown".equals(type)){//向下移动

                queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId())//同课程
                        .eq(Teachplan::getGrade,teachplan.getGrade())//同等级(大章节)
                        .gt(Teachplan::getOrderby,teachplan.getOrderby())//大于本章节的排序字段（越大越下）
                        .orderByAsc(Teachplan::getOrderby)//顺序方便拿数据
                        .last("LIMIT 1");//限制只取1个数据

                Teachplan teachplan1 = teachplanMapper.selectOne(queryWrapper);//查询
                //交换两个数据的orderby，并更新数据库
                exchangeTeachplanOrder(teachplan1, teachplan);

            }
        }else if (grade==2){//小章节
            if("moveup".equals(type)){//上移动

                queryWrapper.eq(Teachplan::getParentid,teachplan.getParentid())//同大章节(不用同等级，因为同大章节就是同等级了是2)
                        .lt(Teachplan::getOrderby,teachplan.getOrderby())//小于本章节的排序字段（越小越上）
                        .orderByDesc(Teachplan::getOrderby)//倒序方便拿数据
                        .last("LIMIT 1");//限制只取1个数据

                Teachplan teachplan1 = teachplanMapper.selectOne(queryWrapper);//查询
                //交换两个数据的orderby，并更新数据库
                exchangeTeachplanOrder(teachplan1, teachplan);

            }else if("movedown".equals(type)){//向下移动

                queryWrapper.eq(Teachplan::getParentid,teachplan.getParentid())//同大章节
                        .gt(Teachplan::getOrderby,teachplan.getOrderby())//大于本章节的排序字段（越大越下）
                        .orderByAsc(Teachplan::getOrderby)//顺序方便拿数据
                        .last("LIMIT 1");//限制只取1个数据

                Teachplan teachplan1 = teachplanMapper.selectOne(queryWrapper);//查询
                //交换两个数据的orderby，并更新数据库
                exchangeTeachplanOrder(teachplan1, teachplan);

            }
        }
    }

    @Override
    public void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        long teachplanId=bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if(teachplan==null){
            throw new XueChengPlusException("未查询到该课程计划");
        }
        if (teachplan.getGrade()!=2){
            new XueChengPlusException("只有小节允许绑定媒资信息");
        }
        LambdaQueryWrapper<TeachplanMedia> queryWrapper=new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId);
        //先查询该课程计划原先绑定的媒资，然后进行先删后插入
        teachplanMediaMapper.delete(queryWrapper);
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        //数据拷贝
        BeanUtils.copyProperties(bindTeachplanMediaDto,teachplanMedia);
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
    }

    @Override
    public void unassociationMedia(Long teachPlanId, String mediaId) {
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId, teachPlanId)
                .eq(TeachplanMedia::getMediaId, mediaId);
        teachplanMediaMapper.delete(queryWrapper);
    }

    //进行两个数据间排序字段的交换
    private void exchangeTeachplanOrder(Teachplan teachplan1, Teachplan teachplan) {
        if(teachplan1 ==null){//不存在说明已经是最上层
            throw new XueChengPlusException("已经到极限了，不能再移动了");
        }else{//存在则进行排序字段交换
            int temp= teachplan.getOrderby();
            teachplan.setOrderby(teachplan1.getOrderby());
            teachplan1.setOrderby(temp);
            teachplanMapper.updateById(teachplan);
            teachplanMapper.updateById(teachplan1);
        }
    }


    //封装sql进行查询数据条数
    private int getTeachplanCount(long courseId,long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }


}
