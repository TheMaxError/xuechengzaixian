package com.xuecheng.content.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description 获取当前用户工具类
 *
 * */
@Slf4j
public class SecurityUtil {
    public static XcUser getUser(){

        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principalObj instanceof String){
                //获取用户信息
                String principal = principalObj.toString();
                //转化为user类
                XcUser xcUser = JSON.parseObject(principal, XcUser.class);
                return xcUser;
            }


        }catch (Exception e) {
            log.error("获取当前登录用户身份出错:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;

    }
    // 这里使用内部类，是为了不让content工程去依赖auth工程因为auth工程中的部分设置影响了content的设置
    @Data
    public static class XcUser implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id;

        private String username;

        private String password;

        private String salt;

        private String name;
        private String nickname;
        private String wxUnionid;
        private String companyId;
        /**
         * 头像
         */
        private String userpic;

        private String utype;

        private LocalDateTime birthday;

        private String sex;

        private String email;

        private String cellphone;

        private String qq;

        /**
         * 用户状态
         */
        private String status;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
    }

}
