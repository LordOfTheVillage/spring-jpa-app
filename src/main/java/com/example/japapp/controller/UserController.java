package com.example.japapp.controller;

import com.example.japapp.dto.UserDto;
import com.example.japapp.model.Book;
import com.example.japapp.model.User;
import com.example.japapp.exception.MainException;
import com.example.japapp.service.impl.EmailService;
import com.example.japapp.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.util.List;

@RequestMapping
@Controller
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
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
            String verificationCode = emailService.generateVerificationCode();
            emailService.sendVerificationEmail(suspect.getEmail(), verificationCode);
            UserDto savedUser = userService.saveUser(suspect);
            request.getSession().setAttribute("user", savedUser);
        } catch (MainException e) {
            model.addAttribute("registerError", e.getMessage());
            return "registration";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
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

    @PostMapping("/profile/setAdmin")
    public String setAdmin(HttpServletRequest request) {
        try {
            UserDto user = (UserDto) request.getSession().getAttribute("user");
            user = userService.setAdminRole(user.getId());
            request.getSession().setAttribute("user", user);

            return "redirect:/users";
        } catch (MainException e) {
            return "redirect:/profile";
        }
    }

    @ModelAttribute("profileLink")
    public String profileLink(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return user != null ? "/profile" : null;
    }

}
