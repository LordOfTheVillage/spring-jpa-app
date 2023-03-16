package com.example.japapp.controllers;

import com.example.japapp.dto.UserDto;
import com.example.japapp.exceptions.MainException;
import com.example.japapp.services.impl.UsersService;
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

    private UsersService usersService;
    public AdminController(UsersService usersService) {
        this.usersService = usersService;
    }
    @GetMapping("/users")
    public String getUsersPage(Model model) {
        try {
            List<UserDto> users = usersService.findAllUsers();
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
