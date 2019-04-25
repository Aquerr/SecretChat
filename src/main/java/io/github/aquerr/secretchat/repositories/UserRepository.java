package io.github.aquerr.secretchat.repositories;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.models.UserCredentials;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository
{
    //This is a temporary repository class which contain some predefined users and user-credentials.
    //TODO: Make a connection to real database and remove predefined entities.

    private final List<User> userList = new ArrayList<>();
    private final List<UserCredentials> userCredentialsList = new ArrayList<>();

    {
        userList.add(new User(1, "Bartek", "nerdi@gmail.com",23,  "Łódzkie", "Jestem krejzol. Jak chcesz pogadać to pisz! :D", LocalDate.now()));
        userList.add(new User(2, "Zbyszko", "zbyszko@bogdaniec.pl" ,34, "Świat", "DO BOJU!", LocalDate.of(2019, 1, 4)));
        userList.add(new User(3, "Kasia", "kasiuniaaa@onet.pl" ,28, "Wrocław", "Lubię psy, kino i zabawę makijażem.", LocalDate.of(2018, 5, 24)));

        userCredentialsList.add(new UserCredentials(1, "Nerdi", "nerdi@gmail.com" ,"brzuszek"));
        userCredentialsList.add(new UserCredentials(2, "Zbyszko", "zbyszko@bogdaniec.pl" , "Bogdaniec"));
        userCredentialsList.add(new UserCredentials(3, "kasiuniaaa", "kasiuniaaa@onet.pl","kochampieski"));
    }

    public List<User> getUserList()
    {
        return userList;
    }

    public List<UserCredentials> getUserCredentialsList()
    {
        return userCredentialsList;
    }

    public void addUser(final User user)
    {
        this.userList.add(user);
    }

    public void addUserCredentials(final UserCredentials userCredentials)
    {
        this.userCredentialsList.add(userCredentials);
    }
}
