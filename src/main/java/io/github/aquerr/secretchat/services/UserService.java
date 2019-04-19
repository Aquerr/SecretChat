package io.github.aquerr.secretchat.services;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.models.UserCredentials;
import io.github.aquerr.secretchat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    private UserService()
    {

    }

    public User login(final UserCredentials userCredentials)
    {
        return null;
    }

    public User register(final String login, final String password, final String emailAddress)
    {
        //Hash password and save user credentials
        //Create new User object and save it as well. Id must be equal to user credentials id.

        int newId = getLastFreeUserId();
        final UserCredentials userCredentials = new UserCredentials(newId, login, emailAddress, password);
        final User user = new User(newId, login, emailAddress, 0, "", "");

        this.userRepository.addUserCredentials(userCredentials);
        this.userRepository.addUser(user);

        return user;

//        int test = 0;
//        for (byte value : password.getBytes())
//        {
//            test+= value;
//        }
        //Check if login and email exists

        //Hash password and store user info.
//            final Provider[] alghoritms = Security.getProviders();
//            final KeyFactory keyFactory = KeyFactory.getInstance("DSA");
//            PrivateKey privateKey = keyFactory.generatePrivate(new DSAPrivateKeySpec(BigInteger.ONE, BigInteger.TEN, BigInteger.ONE, BigInteger.TEN));
//            System.out.println(privateKey);

//            final Cipher cipher = new Cipher();

//            for (final Provider provider : alghoritms)
//            {
//                for (final Provider.Service service : provider.getServices())
//                {
//                    try
//                    {
//                        System.out.println(service.getClassName());
//                        final KeyGenerator keyGenerator = KeyGenerator.getInstance(service.getAlgorithm());
//                        Cipher cipher = Cipher.getInstance(service.getAlgorithm());
//                        final byte[] bytes = cipher.doFinal(password.getBytes());
//                        System.out.println(Arrays.toString(bytes));
//                        keyGenerator.init(test);
//                        final KeyFactory keyFactory = KeyFactory.getInstance("DSA");
//                        final SecretKey secretKey = keyGenerator.generateKey();
//                        System.out.println(secretKey);
//                        System.out.println(new String(secretKey.getEncoded()));
//                    }
//                    catch (NoSuchAlgorithmException e)
//                    {
//                    e.printStackTrace();
//                    }
//                }
//            }
    }

    public synchronized boolean isLoginAvailable(final String login)
    {
        final List<UserCredentials> userCredentialsList = this.userRepository.getUserCredentialsList();
        for (final UserCredentials userCredentials : userCredentialsList)
        {
            if (userCredentials.getUsername().equals(login))
                return false;
        }

        return true;
    }

    public synchronized boolean isEmailAvailable(final String email)
    {
        final List<UserCredentials> userCredentialsList = this.userRepository.getUserCredentialsList();
        for (final UserCredentials userCredentials : userCredentialsList)
        {
            if (userCredentials.getEmail().equals(email))
                return false;
        }

        return true;
    }

    private synchronized int getLastFreeUserId()
    {
        int id = 1;
        while (existsId(id))
        {
            id++;
        }
        return id;
    }

    private synchronized boolean existsId(int id)
    {
        final List<UserCredentials> userCredentialsList = this.userRepository.getUserCredentialsList();
        for (final UserCredentials userCredentials : userCredentialsList)
        {
            if (userCredentials.getUserId() == id)
                return true;
        }
        return false;
    }
}
