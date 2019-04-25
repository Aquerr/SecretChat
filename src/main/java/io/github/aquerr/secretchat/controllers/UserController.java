package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;

@Controller
public class UserController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

//    private final UserService userService;
//
//    public UserController(final UserService userService)
//    {
//        this.userService = userService;
//    }

    @GetMapping(path = "/myprofile")
    public String myprofile(final Model model)
    {
        //TODO: Add to model all necessary profile fields.
        //model.addAttribute("name", user.getName());

        model.addAttribute("content", "myprofile");
//        model.addAttribute("username", UserService.INLOGGED_USERS.get(RequestContextHolder.currentRequestAttributes().getSessionId()));
        model.addAttribute("username", String.valueOf(RequestContextHolder.currentRequestAttributes().getAttribute("username", RequestAttributes.SCOPE_SESSION)));

        return "main";
    }
}
