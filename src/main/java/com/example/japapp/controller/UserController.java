package com.example.japapp.controller;

import com.example.japapp.dto.UserDto;
import com.example.japapp.model.Book;
import com.example.japapp.model.User;
import com.example.japapp.exception.MainException;
import com.example.japapp.service.impl.EmailService;
import com.example.japapp.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.util.List;

@RequestMapping
@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("")
    public String getHomePage() {
        logger.info("GET HOME PAGE");
        return "home";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        logger.info("GET REGISTRATION PAGE");
        model.addAttribute("suspect", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String postUser(@ModelAttribute("suspect") User suspect, HttpServletRequest request, Model model) {
        logger.info("POST REGISTRATION PAGE");
        logger.debug("SUSPECT {}", suspect);
        try {
            userService.saveUser(suspect);
        } catch (MainException e) {
            logger.error("ERROR {}", e.getMessage());
            model.addAttribute("registerError", e.getMessage());
            return "registration";
        }

        return "redirect:/";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token, HttpServletRequest request) {
        UserDto user = userService.confirm(token);
        request.getSession().setAttribute("user", user);
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
