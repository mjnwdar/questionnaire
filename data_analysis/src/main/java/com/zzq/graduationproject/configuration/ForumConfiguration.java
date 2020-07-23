package com.zzq.graduationproject.configuration;

import com.zzq.graduationproject.interceptor.LoginRequiredInterceptor;
import com.zzq.graduationproject.interceptor.PassportInterceptor;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zzq
 */
@Component
public class ForumConfiguration implements WebMvcConfigurer {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor)
            .addPathPatterns("/*")
            .excludePathPatterns(Arrays.asList("/register", "/login", "/error"));
    }
}
