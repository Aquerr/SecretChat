package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.repositories.MongoTestUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@org.springframework.web.bind.annotation.ChatController
@Controller
public class ChatController
{
    @Autowired
    private MongoTestUserRepository mongoTestUserRepository;

    @GetMapping(path = "/chat")
    public String test(Model model)
    {
        return "errorpage";
    }
}
