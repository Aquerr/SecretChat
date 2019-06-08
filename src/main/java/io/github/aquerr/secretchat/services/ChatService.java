package io.github.aquerr.secretchat.services;

import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ChatService
{
    private final Map<String, ServerSocket> chatServerSockets = new HashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Future> socketListenerFutures = new HashMap<>();

//    private final ServerSocket serverSocket;
    private final WebChatServer webChatServer;

    public ChatService() throws IOException
    {
        this.webChatServer = new WebChatServer();
        this.webChatServer.start();
//        this.serverSocket = new ServerSocket(4444);
//        Runnable runnable = getListenTask(serverSocket);
//        Future<?> future = executorService.submit(runnable);
//        socketListenerFutures.put(name, future);
//        chatServerSockets.put(name, serverSocket);
    }

    public Map<String, ServerSocket> getChatServerSockets()
    {
        return chatServerSockets;
    }

    public void addServerSocket(String name, WebSocket webSocket)
    {
        //If serverSocket exists for this name then delete it.
        if(socketListenerFutures.containsKey(name))
        {
            socketListenerFutures.get(name).cancel(true);
            socketListenerFutures.remove(name);
            try
            {
                chatServerSockets.get(name).close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            chatServerSockets.remove(name);
        }
//        Runnable runnable = getListenTask(webSocket);
//        Future<?> future = executorService.submit(runnable);
//        socketListenerFutures.put(name, future);
//        chatServerSockets.put(name, webSocket);
    }

    public void broadcastMessage(final String message)
    {
        this.webChatServer.broadcast(message);
    }

//    private Runnable getListenTask(WebSocket serverSocket)
//    {
//        return new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                while(!Thread.interrupted())
//                {
//                    try
//                    {
//                        final Socket socket = serverSocket.accept();
//                        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//                        printWriter.println("TEST!");
//                        printWriter.close();
//                        socket.close();
//                    }
//                    catch(IOException ex)
//                    {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        };
//    }
}
