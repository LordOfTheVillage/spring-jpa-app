package com.example.japapp.filters;

import com.example.japapp.dto.UserDto;
import com.example.japapp.models.Role;
import com.example.japapp.models.User;
import com.example.japapp.services.impl.UsersService;
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

        String requestURI = httpRequest.getRequestURI();

        if (requestURI.startsWith("/users")) {
            UserDto user = (UserDto) httpRequest.getSession().getAttribute("user");
            if (user != null) {
                if (user.hasRole("ADMIN")) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            httpResponse.sendRedirect("/");
            return;
        }

        chain.doFilter(request, response);
    }
}
