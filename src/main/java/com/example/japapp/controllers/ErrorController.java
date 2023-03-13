package com.example.japapp.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleInternalServerError(HttpServletResponse response, Exception ex) {
        int status = response.getStatus();
        ModelAndView modelAndView;
        if (status == HttpStatus.NOT_FOUND.value()) {
            modelAndView = new ModelAndView("error/404");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            modelAndView = new ModelAndView("error/500");
        } else {
            modelAndView = new ModelAndView("error/error");
        }
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }
}
