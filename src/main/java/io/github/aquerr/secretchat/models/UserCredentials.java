package io.github.aquerr.secretchat.models;

public class UserCredentials
{
    private final int userId;

    private final String username;

    private final String email;

    private final String passwordHash;

    public UserCredentials(final int userId, final String username, final String email, final String passwordHash)
    {
        this.userId = userId; //Equal to UserId

        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPasswordHash()
    {
        return passwordHash;
    }

    public String getEmail()
    {
        return email;
    }
}
