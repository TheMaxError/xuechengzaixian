package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//全局异常处理器
@Slf4j
@ControllerAdvice
//@RestControllerAdvice =@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {
    //对项目自定义异常处理
    @ResponseBody
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//响应给前端的状态码
    public RestErrorResponse customException(XueChengPlusException e) {
        //记录异常
        log.error("【系统异常】{}",e.getErrMessage(),e);
        //解析异常
        return new RestErrorResponse(e.getErrMessage());

    }
    //通用异常处理
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        //记录异常
        log.error("【系统异常】{}", e.getMessage(), e);
        if ("不允许访问".equals(e.getMessage()))
            return new RestErrorResponse("您没有权限操作此功能");
        //解析异常
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

    }
    //JSR303异常处理
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(MethodArgumentNotValidException e) {
        //获取全部异常并拼接
        BindingResult bindingResult = e.getBindingResult();
        List<String> errMessageArr=new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item->{
            errMessageArr.add(item.getDefaultMessage());
        });
        //进行拼接
        String resultErr = StringUtils.join(errMessageArr, ",");
        //记录异常
        log.error("【系统异常】{}", e.getMessage(), resultErr);
        //解析异常
        return new RestErrorResponse(resultErr);

    }



}
