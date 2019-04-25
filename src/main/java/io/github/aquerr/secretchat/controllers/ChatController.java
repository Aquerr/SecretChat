package io.github.aquerr.secretchat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@org.springframework.web.bind.annotation.ChatController
@Controller
public class ChatController
{
    @GetMapping(path = "/chat")
    public String test(Model model)
    {
        return "errorpage";
    }
}
