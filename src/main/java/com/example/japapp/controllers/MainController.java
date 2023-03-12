package com.example.japapp.controllers;

import com.example.japapp.models.Book;
import com.example.japapp.models.User;
import com.example.japapp.exeptions.AuthenticationException;
import com.example.japapp.exeptions.RegistrationException;
import com.example.japapp.services.impl.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping
@Controller
public class MainController {
    private UsersService userService;
    public MainController(UsersService userService) {
        this.userService = userService;
    }
    @GetMapping("")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String postUser(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
        try {
            userService.saveUser(user);
            request.getSession().setAttribute("user", user);
        } catch (RegistrationException e) {
            model.addAttribute("registerError", e.getMessage());
            return "redirect:/registration";
        }

        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("user") User suspect, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        try {
            User user = userService.authenticate(suspect.getEmail(), suspect.getPassword());
            request.getSession().setAttribute("user", user);
            return "redirect:/profile";
        } catch (AuthenticationException e) {
            redirectAttrs.addAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        List<Book> books = this.userService.findAllBooksByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("books", books);
        return "profile";
    }

}
