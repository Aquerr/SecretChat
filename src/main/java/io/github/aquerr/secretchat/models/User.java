package io.github.aquerr.secretchat.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class User
{
    @JsonProperty(defaultValue = "0")
    private final int id;

    @JsonProperty(required = true)
    private final String name;

    @JsonProperty(required = true)
    private final int age;

    @JsonProperty(required = true)
    private final Gender gender;

    @JsonProperty(required = true)
    private final String location;

    @JsonProperty(required = true)
    private final String description;

    @JsonProperty(required = true)
    private final String email;

    @JsonProperty(required = true)
    private final LocalDate registrationDate;

    @JsonProperty(required = true)
    private final String avatarPath;

    public User(final int id, final String name, String email, final int age, final Gender gender, final String location, final String description, final LocalDate registrationDate, final String avatarPath)
    {
        this.id = id; //0 = new user in the storage

        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.description = description;
        this.registrationDate = registrationDate;
        this.avatarPath = avatarPath;
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

    public LocalDate getRegistrationDate()
    {
        return registrationDate;
    }

    public String getAvatarPath()
    {
        return this.avatarPath;
    }

    public Gender getGender()
    {
        return gender;
    }
}
