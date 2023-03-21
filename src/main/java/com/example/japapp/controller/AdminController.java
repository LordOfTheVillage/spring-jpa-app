package com.example.japapp.controller;

import com.example.japapp.dto.UserDto;
import com.example.japapp.exception.MainException;
import com.example.japapp.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
@Controller
public class AdminController {

    private final UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public String getUsersPage(Model model) {
        try {
            List<UserDto> users = userService.findAllUsers();
            model.addAttribute("users", users);
            return "users";
        } catch (MainException e) {
            return "redirect:/error/500";
        }
    }

    @ModelAttribute("profileLink")
    public String profileLink(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return user != null ? "/profile" : null;
    }
}
