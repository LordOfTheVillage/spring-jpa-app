package com.example.japapp.filter;

import com.example.japapp.dto.UserDto;
import com.example.japapp.service.impl.UsersService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleFilter implements Filter {

    private final UsersService usersService;

    public RoleFilter(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        UserDto user = (UserDto) httpRequest.getSession().getAttribute("user");
        if (user != null) {
            if (user.hasRole("ADMIN")) {
                chain.doFilter(request, response);
                return;
            }
        }

        httpResponse.sendRedirect("/");

        chain.doFilter(request, response);
    }
}
