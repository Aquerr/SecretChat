package io.github.aquerr.secretchat.models;

public enum Gender
{
    MALE("male"),
    FAMALE("famale"),
    UNDEFINED("undefined");

    private String gender;

    Gender(final String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
}
