package com.xuecheng.content;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class CourseCategoryServiceTest {
    @Autowired
    CourseCategoryService courseCategoryService;
    @Test
    public void testCourseCategoryService(){
        List<CourseCategoryDto> courseCategoryDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryDtos);
    }

}
