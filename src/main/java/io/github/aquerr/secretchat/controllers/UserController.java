package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class UserController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(final UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping(path = "/myprofile")
    public String myprofile(final Model model, final HttpServletResponse httpServletResponse)
    {
        final Object usernameObject = RequestContextHolder.currentRequestAttributes().getAttribute("username", RequestAttributes.SCOPE_SESSION);

        if (usernameObject == null)
        {
            //TODO: Throw an error here...
            try
            {
                httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "You must be logged in to view this page.");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            model.addAttribute("content", "index");
            return "main";
        }

        final String username = (String) usernameObject;
        final User user = this.userService.getUser(username);

        model.addAttribute("content", "myprofile");
        model.addAttribute("username", username);
        model.addAttribute("user", user);
        model.addAttribute("title", "Mój Profil");
        return "main";
    }
}
