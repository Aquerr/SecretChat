package io.github.aquerr.secretchat.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User
{
    @JsonProperty(defaultValue = "0")
    private final int id;

    @JsonProperty(required = true)
    private final String name;

    @JsonProperty(required = true)
    private final int age;

    @JsonProperty(required = true)
    private final String location;

    @JsonProperty(required = true)
    private final String description;

    @JsonProperty(required = true)
    private final String email;

    public User(final int id, final String name, String email, final int age, final String location, final String description)
    {
        this.id = id; //0 = new user in the storage

        this.name = name;
        this.email = email;
        this.age = age;
        this.location = location;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public String getLocation()
    {
        return location;
    }

    public String getDescription()
    {
        return description;
    }

    public String getEmail()
    {
        return email;
    }
}
