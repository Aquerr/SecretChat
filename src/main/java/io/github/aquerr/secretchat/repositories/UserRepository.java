package io.github.aquerr.secretchat.repositories;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.models.UserCredentials;

import java.util.List;
import java.util.Optional;

public interface UserRepository
{
    List<UserCredentials> getUserCredentials();

    List<User> getUsers();

    Optional<User> getUser(String id);

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(String id);

    void addUserCredentials(UserCredentials userCredentials);

    void updateUserCredentials(UserCredentials userCredentials);

    void deleteUserCredentials(String id);
}
