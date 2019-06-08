package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.security.RequireAuthorization;
import io.github.aquerr.secretchat.services.ChatService;
import io.github.aquerr.secretchat.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Controller
public class UserController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ChatService chatService;

    public UserController(final UserService userService, final ChatService chatService)
    {
        this.userService = userService;
        this.chatService = chatService;
    }

    @RequireAuthorization
    @GetMapping(path = "/myprofile")
    public String myprofile(final Model model, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws IOException
    {
        final Object usernameObject = RequestContextHolder.currentRequestAttributes().getAttribute("username", RequestAttributes.SCOPE_SESSION);

        final String username = (String) usernameObject;
        final User user = this.userService.getUser(username);

        model.addAttribute("content", "myprofile");
        model.addAttribute("username", username);
        model.addAttribute("user", user);
        model.addAttribute("title", "Mój Profil");
//        Socket socket = new Socket();
//        ServerSocket serverSocket = new ServerSocket(25565);
//        this.chatService.addServerSocket(username, serverSocket);

        return "main";
    }
}
