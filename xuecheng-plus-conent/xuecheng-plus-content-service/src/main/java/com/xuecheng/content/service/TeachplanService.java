package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

public interface TeachplanService {
    /**
     * 教育计划树节点查询
     * 参数：课程id
     * 返回值:课程计划树
     * */
    List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 教育计划大章节小章节的新增与修改
     * 参数：大章节小章节的新增与修改的信息
     *
     * */
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 根据课程计划id删除教育计划（大小章节）
     * 参数：课程计划id
     *
     * */
    void deleteTeachplanById(Long courseId);

    /**
     * 根据课程计划id将课程计划（大小章节）下移
     * 参数：移动模式(上移or下移) 课程计划id
     *
     * */
    void sortTeachplanById(String type,Long id);

    /**
     * 教学计划绑定媒资信息
     * @param bindTeachplanMediaDto 封装课程计划与绑定的媒资信息的参数
     */
    void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /** 解绑教学计划与媒资信息
     * @param teachPlanId       教学计划id
     * @param mediaId           媒资信息id
     */
    void unassociationMedia(Long teachPlanId, String mediaId);

}
