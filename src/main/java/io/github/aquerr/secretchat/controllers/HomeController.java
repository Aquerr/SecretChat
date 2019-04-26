package io.github.aquerr.secretchat.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController
{
    @Value("${welcome.message}")
    private String message;

    @GetMapping("/")
    public String index(final Model model, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse)
    {
        model.addAttribute("message", message);
        model.addAttribute("username", "");
        model.addAttribute("content", "index");

        //TODO: If user is not logged in then show default index page
        //TODO: If user is logged in then show page with user name in the upper right corner.

//        final Cookie sessionId = WebUtils.getCookie(httpServletRequest, "session");
        final Object username = httpServletRequest.getSession().getAttribute("username");
        if (username != null && !username.equals(""))
        {
            model.addAttribute("username", username);
        }

        return "main";
    }
}
