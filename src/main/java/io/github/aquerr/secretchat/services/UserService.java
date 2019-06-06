package io.github.aquerr.secretchat.services;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.models.UserCredentials;
import io.github.aquerr.secretchat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    private UserService()
    {

    }

    public User login(final String username, final String emailAddress, final String plainPassword)
    {
        UserCredentials userCredentials = null;
        for (final UserCredentials userCreds : this.userRepository.getUserCredentials())
        {
            if (!username.equals(""))
            {
                if (userCreds.getUsername().equals(username))
                {
                    userCredentials = userCreds;
                    break;
                }
            }
            else if (!emailAddress.equals(""))
            {
                if (userCreds.getEmail().equals(emailAddress))
                {
                    userCredentials = userCreds;
                    break;
                }
            }
        }

        if (userCredentials == null)
            return null; //Or error

        try
        {
            final boolean isCorrect = checkPassword(plainPassword, userCredentials.getPasswordHash());

            if (isCorrect)
            {
                final UserCredentials finalUserCredentials = userCredentials;
                final Optional<User> optionalUser = this.userRepository.getUsers().stream().filter(x->x.getId() == finalUserCredentials.getUserId()).findFirst();
                if (optionalUser.isPresent())
                {
                    return optionalUser.get();
                }
                else
                {
                    return null; //Or error
                }
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public User register(final String login, final String password, final String emailAddress)
    {
        //Hash password and save user credentials
        //Create new User object and save it as well. Id must be equal to user credentials id.

        int newId = getLastFreeUserId();
        String base64EncodedPasswordWithSalt = "";
        try
        {
            base64EncodedPasswordWithSalt = hashPassword(password);
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//            messageDigest.update(password.getBytes());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }

        if (base64EncodedPasswordWithSalt.equals(""))
            return null;

        final UserCredentials userCredentials = new UserCredentials(newId, login, emailAddress, base64EncodedPasswordWithSalt);
        final User user = new User(newId, login, emailAddress, 0, "", "", LocalDate.now());

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

    public User getUser(final String username)
    {
        final List<User> userList = this.userRepository.getUsers();
        return userList.stream().filter(x->x.getName().equals(username)).findFirst().orElse(null);
    }

    public boolean deleteUser(final int userId)
    {
        return this.userRepository.deleteUser(userId);
    }

    public synchronized boolean isLoginAvailable(final String login)
    {
        final List<UserCredentials> userCredentialsList = this.userRepository.getUserCredentials();
        for (final UserCredentials userCredentials : userCredentialsList)
        {
            if (userCredentials.getUsername().equals(login))
                return false;
        }

        return true;
    }

    public synchronized boolean isEmailAvailable(final String email)
    {
        final List<UserCredentials> userCredentialsList = this.userRepository.getUserCredentials();
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
        final List<UserCredentials> userCredentialsList = this.userRepository.getUserCredentials();
        for (final UserCredentials userCredentials : userCredentialsList)
        {
            if (userCredentials.getUserId() == id)
                return true;
        }
        return false;
    }

    private byte[] generateSalt(int size)
    {
        byte[] saltBytes = new byte[size];
        final SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(saltBytes);
        return saltBytes;
    }

    private String hashPassword(final String password) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        byte[] salt = generateSalt(20);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,65536, 128);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        final Base64.Encoder base64Encoder = Base64.getEncoder();
        String base64EncodedPassword = base64Encoder.encodeToString(hashBytes);
        String encodedSalt = base64Encoder.encodeToString(salt);
        System.out.println(base64EncodedPassword);
        System.out.println(encodedSalt);

        base64EncodedPassword = encodedSalt + "$" + base64EncodedPassword;

        System.out.println(base64EncodedPassword);
        return base64EncodedPassword;
    }

    private boolean checkPassword(final String password, final String storedPasswordHash) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        final Base64.Decoder decoder = Base64.getDecoder();
        final String[] saltAndPasswordHash = storedPasswordHash.split("\\$");

        final byte[] saltBytes = decoder.decode(saltAndPasswordHash[0]);
        final byte[] passwordBytes = decoder.decode(saltAndPasswordHash[1]);

        final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes,65536, 128);
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();

        return Arrays.equals(passwordBytes, hashBytes);
    }
}
