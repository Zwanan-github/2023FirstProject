package com.zwan.studyprojectbackend.config;

import com.zwan.studyprojectbackend.interceptor.AuthorizeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    AuthorizeInterceptor authorizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authorizeInterceptor)
                .addPathPatterns("/**")
                // 放行auth接口
                .excludePathPatterns("/api/auth/**");
    }
}
