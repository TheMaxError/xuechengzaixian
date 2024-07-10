package com.xuecheng.content;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCategoryMapperTest {
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Test
    public void testCourseCategoryMapper(){
        List<CourseCategoryDto> courseCategoryDtos = courseCategoryMapper.selectTreeNodes("1");
        System.out.println(courseCategoryDtos);
    }

}
