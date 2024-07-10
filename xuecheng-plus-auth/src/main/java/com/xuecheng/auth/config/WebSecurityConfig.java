package com.xuecheng.auth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * @description 安全管理配置
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//采用自定义的daoAuthenticationProviderCustom
        //原来的DaoAuthenticationProvider 会进行密码校验，现在采用自己创建的DaoAuthenticationProviderCustom取消密码验证
        auth.authenticationProvider(daoAuthenticationProviderCustom);
    }


//    //配置用户信息服务
//    @Bean
//    public UserDetailsService userDetailsService() {
//        //这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
//        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
//        return manager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        //密码为明文方式
//        return NoOpPasswordEncoder.getInstance();
        //将用户输入的密码编码为BCrypt格式与数据库密码进行比对
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //配置安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/r/**")
                .authenticated()//访问/r开始的请求需要认证通过
                .anyRequest()
                .permitAll()//其它请求全部放行
                .and()
                .formLogin()
                .successForwardUrl("/login-success");//登录成功跳转到/login-success

        //http.logout().logoutUrl("/logout");
        /**
         * 配置说明：
         * 通过 authorizeRequests() 方法来配置请求授权规则。
         * 使用 antMatchers() 方法指定需要进行访问控制的 URL 路径模式。在这里，/r/** 表示所有以 /r/ 开头的 URL 都需要进行授权访问。
         * 使用 authenticated() 方法指定需要进行身份验证的请求。
         * 使用 anyRequest() 方法配置除了 /r/** 以外的所有请求都不需要进行身份验证。
         * 使用 permitAll() 方法表示任何用户都可以访问不需要进行身份验证的 URL。
         * 使用 formLogin() 方法配置登录页表单认证，其中 successForwardUrl() 方法指定登录成功后的跳转页面。
         * 使用 logout() 方法配置退出登录，其中 logoutUrl() 方法指定退出登录的 URL。
         * */
    }



}
