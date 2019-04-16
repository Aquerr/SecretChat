package io.github.aquerr.secretchat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController
{
//    @GetMapping("/error")
//    public String error(String stacktrace, Model model)
//    {
//        model.addAttribute("stacktrace", stacktrace);
//        return "error";
//    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest httpServletRequest, Exception ex)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", httpServletRequest.getRequestURL());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
