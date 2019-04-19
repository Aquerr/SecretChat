package io.github.aquerr.secretchat.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
    @Value("${welcome.message}")
    private String message;

    @GetMapping("/")
    public String index(final Model model)
    {
        //TODO: If user is not logged in then show default index page
        //TODO: If user is logged in then show page with user name in the upper right corner.

        model.addAttribute("message", message);
        return "index";
    }
}
