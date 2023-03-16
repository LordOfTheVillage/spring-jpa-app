package com.example.japapp.config;

import com.example.japapp.filters.RoleFilter;
import com.example.japapp.services.impl.UsersService;
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
