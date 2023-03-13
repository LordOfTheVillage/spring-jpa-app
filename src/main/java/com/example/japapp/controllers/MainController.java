package com.example.japapp.controllers;

import com.example.japapp.dto.UserDto;
import com.example.japapp.models.Book;
import com.example.japapp.models.User;
import com.example.japapp.exeptions.MainException;
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
        model.addAttribute("suspect", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String postUser(@ModelAttribute("suspect") User suspect, HttpServletRequest request, Model model) {
        try {
            UserDto savedUser = userService.saveUser(suspect);
            request.getSession().setAttribute("user", savedUser);
        } catch (MainException e) {
            model.addAttribute("registerError", e.getMessage());
            return "redirect:/registration";
        }

        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("suspect", new User());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("suspect") User suspect, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        try {
            UserDto user = userService.authenticate(suspect.getEmail(), suspect.getPassword());
            request.getSession().setAttribute("user", user);
            return "redirect:/profile";
        } catch (MainException e) {
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
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        List<Book> books = this.userService.findAllBooksByUserId(user.getId());
        model.addAttribute("profiler", user);
        model.addAttribute("books", books);
        return "profile";
    }

    @ModelAttribute("profileLink")
    public String profileLink(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return user != null ? "/profile" : null;
    }

}
