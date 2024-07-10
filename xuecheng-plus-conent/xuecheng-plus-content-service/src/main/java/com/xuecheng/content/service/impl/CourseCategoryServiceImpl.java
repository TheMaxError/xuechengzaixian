package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Override
    public List<CourseCategoryDto> queryTreeNodes(String id) {
        //通过递归查询出分类信息
        List<CourseCategoryDto> courseCategoryDtos = courseCategoryMapper.selectTreeNodes(id);
        //由于从数据库查询并不能将子节点封装入父节点只是单纯将数据封装入数据库
        //因此需要找到每个节点的子节点然后封装为List<CourseCategoryDto>
        //思路:先将list转为map，key为id值，value为CourseCategoryDto对象 因为map比较容易获取节点
        //从头便利List<CourseCategoryDto>然后将子节点放在父节点的List<CourseCategoryDto>中

        //将List装为map
        //通过stream流的collect方法转map（除根节点），调用参数采用Lamba，key和vallue其实都是CourseCategoryDto对象，第三个参数是当key相同时存入新的对象而不是旧的对象
        Map<String, CourseCategoryDto> mapTemp = courseCategoryDtos.stream().
                filter(item->!id.equals(item.getId())).
                collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));


        //创建一个返回最终结果的list
        List<CourseCategoryDto> resultCourseCategory=new ArrayList<>();

        //遍历courseCategoryDtos（除根节点）
        courseCategoryDtos.stream().filter(item->!id.equals(item.getId()))
                .forEach(item->{
                    //进行节点处理（找到根节点的子节点）
                    if(item.getParentid().equals(id)){
                        resultCourseCategory.add(item);
                    }
                    //找到父节点
                    CourseCategoryDto courseCategoryParent = mapTemp.get(item.getParentid());
                    if(courseCategoryParent!=null){

                        if(courseCategoryParent.getChildrenTreeNodes()==null){
                            //如果父节点ChildrenTreeNode为空则需要初始化
                            courseCategoryParent.setChildrenTreeNodes(new ArrayList<>());
                        }
                        //找到每个除根节点的子节点并放在父节点的List<CourseCategoryDto>中
                        courseCategoryParent.getChildrenTreeNodes().add(item);
                    }
                });

        return resultCourseCategory;
    }
}
