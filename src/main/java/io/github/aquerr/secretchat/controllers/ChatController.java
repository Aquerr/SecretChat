package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.services.ChatService;
import io.github.aquerr.secretchat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@Controller
public class ChatController
{
    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    public ChatController(final ChatService chatService, final UserService userService)
    {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/user/{id}")
    public void sendMessage(@PathVariable(name = "id") String id, @RequestParam(name = "message") String message) throws IOException
    {
        //TODO: Forward the message to the correct user.

        Socket socket = new Socket("localhost", 25565);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        printWriter.println(message);
        printWriter.close();
        socket.close();

//        HttpSession session = SessionStorage.getActiveSessions().stream().filter(x->x.getId().equals(id)).findFirst().orElseGet(null);
//        if(session != null)
//        {

        //Send message somehow :o
//        }
    }

    @GetMapping("/profile/{username}")
    public String getUserProfile(@PathVariable(name = "username") String username, final Model model, final HttpServletRequest httpServletRequest)
    {
        User user = this.userService.getUser(username);
        if(user != null)
        {
            model.addAttribute("content", "profile");
            model.addAttribute("user", user);
            model.addAttribute("title", "Profil " + username);
            model.addAttribute("webSocketUrl", environment.getProperty("chatSocketUrl"));
        }
        else
        {
            model.addAttribute("title", "Strona Główna");
            model.addAttribute("content", "index");
        }

        final Object sessionUsername = httpServletRequest.getSession().getAttribute("username");
        if (sessionUsername != null && !sessionUsername.equals(""))
            model.addAttribute("username", sessionUsername);
        else
            model.addAttribute("username", "");

        return "main";
    }

    @PostMapping("/broadcast/{message}")
    public ResponseEntity<?> broadcastMessage(@PathVariable(name = "message") String message)
    {
        this.chatService.broadcastMessage(message);
        return ResponseEntity.ok().build();
    }
}
