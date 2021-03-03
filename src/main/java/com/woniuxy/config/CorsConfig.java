package com.woniuxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    //设置跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置哪些路径跨域
        registry.addMapping("/**")
                //哪些源头可以跨域
                .allowedOrigins("*")
                //哪些请求可以跨域
                .allowedHeaders("*")
                //跨域请求方式
                .allowedMethods("*")
                //是否允许ajax带cookies
                .allowCredentials(true)
                //设置重发跨域请求的最大时长
                .maxAge(3600);
    }
}
