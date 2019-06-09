package io.github.aquerr.secretchat.models;

import java.util.UUID;

public class ChatUser
{
    private final UUID hiddenBehindUUID;
    private final String username;

    public ChatUser(UUID hiddenBehindUUID, String username)
    {
        this.hiddenBehindUUID = hiddenBehindUUID;
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public UUID getHiddenBehindUUID()
    {
        return hiddenBehindUUID;
    }
}
