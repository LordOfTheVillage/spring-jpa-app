package com.example.japapp.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleInternalServerError(HttpServletResponse response) {
        int status = response.getStatus();
        if (status == HttpStatus.NOT_FOUND.value()) {
            return new ModelAndView("error/404");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return new ModelAndView("error/500");
        } else {
            return new ModelAndView("error/error");
        }
    }
}
