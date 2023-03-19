package com.example.japapp.config;

import com.example.japapp.filter.RoleFilter;
import com.example.japapp.service.impl.UsersService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public FilterRegistrationBean<RoleFilter> roleBasedFilterRegistrationBean(UsersService usersService) {
        FilterRegistrationBean<RoleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RoleFilter(usersService));
        registrationBean.addUrlPatterns("/users/*");
        return registrationBean;
    }

}
