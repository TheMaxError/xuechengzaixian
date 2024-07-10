package com.xuecheng.content.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class ResourceServiceConfig  extends ResourceServerConfigurerAdapter {

    public static  final  String RESOURCE_ID = "xuecheng-plus";//资源标识需要与xuecheng-plus-auth中的资源标识一致

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)
                .tokenStore(tokenStore)
                .stateless(true);
    }

    //配置安全拦截机制
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()           // 禁用 CSRF 保护
                .authorizeRequests()    //配置对请求的授权策略
//                .antMatchers("/r/**", "/course/**")//在网关进行拦截认证，网关拦截认证完毕不需要在content再次拦截认证

                .anyRequest().
                permitAll();  // 允许所有其他请求（除了上面指定的路径之外）都可以被访问，不需要进行身份认证。
    }

}
