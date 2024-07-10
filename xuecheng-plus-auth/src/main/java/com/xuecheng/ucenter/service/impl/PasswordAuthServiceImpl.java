package com.xuecheng.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xuecheng.ucenter.feignclient.CheckCodeClient;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("password_authservice")//一个接口的多种实现，我们依靠beanName来做区分，例如这里的password_authservice，见名知意就知道是密码登录方式
public class PasswordAuthServiceImpl implements AuthService {
    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CheckCodeClient checkCodeClient;
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        //校验验证码
        //获取用户输入的验证码
        String checkCode = authParamsDto.getCheckcode();
        String checkCodeKey = authParamsDto.getCheckcodekey();

        if (StringUtils.isBlank(checkCode) || StringUtils.isBlank(checkCodeKey)){
            throw new RuntimeException("验证码为空");
        }

        Boolean verify = checkCodeClient.verify(checkCodeKey, checkCode);
        if(!verify){
            throw new RuntimeException("验证码输入错误");
        }

        //用户名校验
        XcUser user = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, authParamsDto.getUsername()));
        if(user==null){
            throw new RuntimeException("用户名不存在");
        }

        //密码校验
        //获取数据库密码
        String passwordDb = user.getPassword();
        //获取用户输入的密码
        String passwordForm = authParamsDto.getPassword();
        //进行密码校验
        boolean matches = passwordEncoder.matches(passwordForm, passwordDb);
        if (!matches) {
            throw new RuntimeException("账号或密码错误");
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(user, xcUserExt);
        return xcUserExt;
    }
}
