package com.github.lujs.auth;

import com.github.lujs.auth.interceptor.PermissionInterceptor;
import com.github.lujs.auth.resolver.CzTokenMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/8 20:01
 */
@Configuration
public class AuthConfig implements WebMvcConfigurer {


    @Resource
    private CzTokenMethodArgumentResolver czTokenMethodArgumentResolver;


    @Resource
    private PermissionInterceptor permissionInterceptor;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(czTokenMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
    }

}

