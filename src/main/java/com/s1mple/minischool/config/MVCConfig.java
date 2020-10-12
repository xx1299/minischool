package com.s1mple.minischool.config;

import com.s1mple.minischool.web.interceptor.AuthorityInterceptor;
import com.s1mple.minischool.web.interceptor.CompleteInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Bean
    public AuthorityInterceptor getAuthorityInterceptor(){
        return new AuthorityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getAuthorityInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/user/**","/trendsImgs","/trendsImg/**");
//        registry.addInterceptor(new CompleteInterceptor()).addPathPatterns("/user/complete");
    }
}
