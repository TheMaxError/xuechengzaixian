package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class NacosConfigTest {
    @Autowired
    CourseBaseInfoService courseBaseInfoService;
    @Test
    void contextQueryCourseTest() {
//        PageResult<CourseBase> result = courseBaseInfoService.queryCourseBaseList(new PageParams(1L, 10L), new QueryCourseParamsDto());
//        log.info("查询到数据：{}", result);
    }
}
