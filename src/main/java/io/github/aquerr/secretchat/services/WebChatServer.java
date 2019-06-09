package io.github.aquerr.secretchat.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.aquerr.secretchat.models.ChatUser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class WebChatServer extends WebSocketServer
{
    private static final int TCP_PORT = 4444;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Set<WebSocket> connections;
    private Map<ChatUser, WebSocket> chatUserWebSocketMap = new HashMap<>();
//    private Map<String, WebSocket>

    public WebChatServer()
    {
        super(new InetSocketAddress(TCP_PORT));
        connections = new HashSet<>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {
        connections.add(conn);
        System.out.println("New chat connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {
        connections.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
        //Convert message to json
        System.out.println(message);
        JsonNode objectNode = null;
        try
        {
            objectNode = OBJECT_MAPPER.readTree(message);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        if(objectNode.has("type") && objectNode.get("type").textValue().equals("handshake"))
        {
            final String from = objectNode.get("from").textValue();

            final UUID uuid = UUID.randomUUID();
            final ChatUser chatUser = new ChatUser(uuid, from);
            chatUserWebSocketMap.put(chatUser, conn);

            if(objectNode.has("to") && !objectNode.get("to").textValue().equals(""))
            {
                final String toUserName = objectNode.get("to").textValue();

                //Inform other user about handshake
                for(ChatUser chatUserInSet : chatUserWebSocketMap.keySet())
                {
                    if(chatUserInSet.getUsername().equals(toUserName))
                    {
                        final WebSocket webSocket = chatUserWebSocketMap.get(chatUserInSet);
                        ObjectNode newMessage = OBJECT_MAPPER.createObjectNode();
                        newMessage.put("from", chatUser.getHiddenBehindUUID().toString());
                        newMessage.put("type", "handshake");
                        try
                        {
                            webSocket.send(OBJECT_MAPPER.writeValueAsString(newMessage));
                        }
                        catch(JsonProcessingException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else if(objectNode.has("type") && objectNode.get("type").textValue().equals("message"))
        {
            final String chatMessage = objectNode.get("message").textValue();
            final String from = objectNode.get("from").textValue();
            final String to = objectNode.get("to").textValue();


            if(to.split("-").length != 5)
            {
                //Not UUID - User is probably visible...
                //TODO: Implement code...
            }
            else
            {
                //UUID - Send it further to the user.
                for(ChatUser chatUserInSet : chatUserWebSocketMap.keySet())
                {
                    if(chatUserInSet.getHiddenBehindUUID().equals(UUID.fromString(to)))
                    {
                        final WebSocket webSocket = chatUserWebSocketMap.get(chatUserInSet);
                        try
                        {
                            webSocket.send(OBJECT_MAPPER.writeValueAsString(objectNode));
                        }
                        catch(JsonProcessingException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        System.out.println("Message from client: " + message);
//        for(WebSocket webSocket : connections)
//        {
//            webSocket.send(message);
//        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
        if(conn != null)
        {
            connections.remove(conn);
        }
        System.out.println(ex);
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onStart()
    {

    }
}
