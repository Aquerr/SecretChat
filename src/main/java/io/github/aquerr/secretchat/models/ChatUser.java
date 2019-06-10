package io.github.aquerr.secretchat.models;

import org.java_websocket.WebSocket;

import java.util.Objects;
import java.util.UUID;

public class ChatUser
{
    private final UUID hiddenBehindUUID;
    private final String username;
    private final WebSocket webSocket;

    public ChatUser(WebSocket webSocket, UUID hiddenBehindUUID, String username)
    {
        this.hiddenBehindUUID = hiddenBehindUUID;
        this.username = username;
        this.webSocket = webSocket;
    }

    public String getUsername()
    {
        return username;
    }

    public UUID getHiddenBehindUUID()
    {
        return hiddenBehindUUID;
    }

    public WebSocket getWebSocket()
    {
        return webSocket;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChatUser chatUser = (ChatUser) o;
        return hiddenBehindUUID.equals(chatUser.hiddenBehindUUID) &&
                username.equals(chatUser.username) &&
                webSocket.equals(chatUser.webSocket);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(hiddenBehindUUID, username, webSocket);
    }
}
