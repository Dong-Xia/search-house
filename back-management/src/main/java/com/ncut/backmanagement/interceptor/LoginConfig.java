package com.ncut.backmanagement.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>LoginConfig<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/7 17:18<br/>
 */
@Configuration
public class LoginConfig  implements WebMvcConfigurer {

    @Autowired
    private LoginIntercepter loginIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginIntercepter);
        loginRegistry.addPathPatterns("/*");
        loginRegistry.excludePathPatterns("/admin/login");
        loginRegistry.excludePathPatterns("/house/dataSyn");
    }
}
