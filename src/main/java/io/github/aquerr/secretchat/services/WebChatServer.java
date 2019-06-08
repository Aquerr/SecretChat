package io.github.aquerr.secretchat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebChatServer extends WebSocketServer
{
    private static final int TCP_PORT = 4444;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Set<WebSocket> connections;
    private Map<String, WebSocket> usernameWebsocketMap = new HashMap<>();
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
        ObjectNode objectNode = OBJECT_MAPPER.valueToTree(message);

        if(objectNode.has("type") && objectNode.get("type").textValue().equals("handshake"))
        {
            final String toUserName = objectNode.get("to").textValue();
            
//            usernameWebsocketMap.put()
        }

        System.out.println("Message from client: " + message);
        for(WebSocket webSocket : connections)
        {
            webSocket.send(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
        if(conn != null)
        {
            connections.remove(conn);
        }
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onStart()
    {

    }
}
