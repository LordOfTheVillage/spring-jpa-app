package com.example.japapp.controllers;

import com.example.japapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class MainController {
    private UserService userService;
    public MainController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("")
    public String getIndexPage(Model model) {
        return "index";
    }

//    @GetMapping("/signup")
//    public String getSignupPage(Model model) {
//        model.addAttribute("user", new User());
//        return "signup";
//    }

//    @PostMapping("/signup")
//    public String postUserEntity(@ModelAttribute("user") User user, Model model) {
//        Optional<User> candidate = null;
//        try {
//            candidate = Optional.ofNullable(userService.registerUser(user));
//        } catch (UserAlreadyExistsException e) {
//            throw new RuntimeException(e);
//        }
//        return "redirect:/profile/" + candidate.get().getId();
//    }
//
//    @GetMapping("/signin")
//    public String getSigninPage(Model model) {
//        logger.log(Level.INFO, "GET SIGNUP PAGE");
//        model.addAttribute("user", new User());
//        return "signin";
//    }
//
//    @PostMapping("/signin")
//    public String loginWithData(@ModelAttribute("user") User user, Model model) {
//        Optional<User> candidate = userService.logIn(user.getEmail(), user.getPassword());
//        if (candidate.isEmpty()) {
//            return "redirect:/signin";
//        } else {
//            return "redirect:/profile/" + candidate.get().getId();
//        }
//    }
}
