package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcMenu;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    XcUserMapper xcUserMapper;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    XcMenuMapper xcMenuMapper;
    /**
     * @description 根据账号查询用户信息
     * @param s  AuthParamsDto类型的json数据
     * @return org.springframework.security.core.userdetails.UserDetails
     *
     *
     */

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        AuthParamsDto authParamsDto = null;
        try {
            //将认证参数转为AuthParamsDto类型
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            log.info("认证请求不符合项目要求:{}",s);
            throw new RuntimeException("认证请求数据格式不对");
        }
        //获取认证方式
        String authType = authParamsDto.getAuthType();
        //根据认证方式获取对应的服务
        AuthService authService  = applicationContext.getBean(authType + "_authservice", AuthService.class);
        //在数据库中进行匹配
        XcUserExt user = authService.execute(authParamsDto);



        //虽然springsSecurity会对密码和用户名校验并提示错误，但实际上它的提示有点问题，最好是上面获取用户名失败提示用户名错，下面校验密码，密码错则报密码错误

//        SecurityContextHolder.getContext().getAuthentication().
        //由于UserDetails中只有权限，用户名，密码等信息，但具体的用户有许多信息，为了将这些信息进行封装入JWTtoken中，需要对username进行扩展
        //或者对userDetails进行扩展，但会破坏其结构，为了不破坏结构，采用在username进行扩展的方式进行
        return getUserPrincipal(user);
    }

    //封装成数据返回
    private UserDetails getUserPrincipal(XcUserExt user) {
        String password = user.getPassword();
        //用户权限,如果不加报Cannot pass a null GrantedAuthority collection
//        String[] authorities={"test"};
        //获取用户权限并设置
        String userId = user.getId();
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        ArrayList<String> permissions = new ArrayList<>();
        if (xcMenus.isEmpty()){
            permissions.add("test");

        }else{
            xcMenus.forEach(xcMenu -> {
                permissions.add(xcMenu.getCode());
            });
        }
        user.setPermissions(permissions);
        //为了安全，将user的password设置为空
        user.setPassword(null);
        String userString = JSON.toJSONString(user);
        String[] authorities = permissions.toArray(new String[0]);

        UserDetails userDetails = User.withUsername(userString)
                .password(password)
                .authorities(authorities)
                .build();
        return userDetails;
    }
}
