package io.github.aquerr.secretchat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@Controller
public class ChatController
{
//    @Autowired
//    private MongoTestUserRepository mongoTestUserRepository;

//    @GetMapping(path = "/chat")
//    public String test(Model model)
//    {
//        return "BOOM";
//    }

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
}
