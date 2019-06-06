package io.github.aquerr.secretchat.repositories;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.models.UserCredentials;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoUserRepository implements UserRepository
{
    //This is a temporary repository class which contain some predefined users and user-credentials.
    //TODO: Make a connection to real database and remove predefined entities.

//    private final List<User> userList = new ArrayList<>();
//    private final List<UserCredentials> userCredentialsList = new ArrayList<>();

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public MongoUserRepository()
    {
//        userList.add(new User(1, "Bartek", "nerdi@gmail.com",23,  "Łódzkie", "Jestem krejzol. Jak chcesz pogadać to pisz! :D", LocalDate.now()));
//        userList.add(new User(2, "Zbyszko", "zbyszko@bogdaniec.pl" ,34, "Świat", "DO BOJU!", LocalDate.of(2019, 1, 4)));
//        userList.add(new User(3, "Kasia", "kasiuniaaa@onet.pl" ,28, "Wrocław", "Lubię psy, kino i zabawę makijażem.", LocalDate.of(2018, 5, 24)));

//        userCredentialsList.add(new UserCredentials(1, "Nerdi", "nerdi@gmail.com" ,"6g2ByiBPoWW6Pus4Ud0/Aee1Bjk=$fK4BoiHoXVDM8ZiLbuTOpg==")); //Password = "brzuszek"
//        userCredentialsList.add(new UserCredentials(2, "Zbyszko", "zbyszko@bogdaniec.pl" , "Bogdaniec"));
//        userCredentialsList.add(new UserCredentials(3, "kasiuniaaa", "kasiuniaaa@onet.pl","kochampieski"));

        mongoClient = MongoClients.create();
        mongoDatabase = mongoClient.getDatabase("secretchat");

        prepareDatabase();
    }

    private void prepareDatabase()
    {
        Document document = mongoDatabase.getCollection("users").find(Filters.eq("_id", 1)).first();

        if (document == null)
        {
            document = new Document()
                    .append("_id", 1)
                    .append("name", "Bartek")
                    .append("email", "nerdi@gmail.com")
                    .append("age", 23)
                    .append("location", "Łódzkie")
                    .append("description", "Jestem krejzol. Jak chcesz się poznać to pisz! :D")
                    .append("registrationDate", LocalDate.now().toString());

            mongoDatabase.getCollection("users").insertOne(document);
        }

        document = mongoDatabase.getCollection("usercredentials").find(Filters.eq("_id", 1)).first();

        if (document == null)
        {
            document = new Document()
                    .append("_id", 1)
                    .append("username", "Nerdi")
                    .append("email", "nerdi@gmail.com")
                    .append("passwordHash", "6g2ByiBPoWW6Pus4Ud0/Aee1Bjk=$fK4BoiHoXVDM8ZiLbuTOpg==");

            mongoDatabase.getCollection("usercredentials").insertOne(document);
        }
    }

    public List<User> getUsers()
    {
        final List<User> users = new ArrayList<>();
        final MongoCollection<Document> usersDocs = this.mongoDatabase.getCollection("users");
        final MongoCursor<Document> usersIterator = usersDocs.find().iterator();
        while (usersIterator.hasNext())
        {
            final Document document = usersIterator.next();
            final Integer id = document.getInteger("_id");
            final String name = document.getString("name");
            final String email = document.getString("email");
            final Integer age = document.getInteger("age");
            final String location = document.getString("location");
            final String description = document.getString("description");
            final LocalDate registrationDate = LocalDate.parse(document.getString("registrationDate"));
            users.add(new User(id, name, email, age, location, description, registrationDate));
        }
        return users;
    }

    public List<UserCredentials> getUserCredentials()
    {
        final List<UserCredentials> userCredentialsListsers = new ArrayList<>();
        final MongoCollection<Document> usersDocs = this.mongoDatabase.getCollection("usercredentials");
        final MongoCursor<Document> usersIterator = usersDocs.find().iterator();
        while (usersIterator.hasNext())
        {
            final Document document = usersIterator.next();
            final Integer id = document.getInteger("_id");
            final String username = document.getString("username");
            final String email = document.getString("email");
            final String passwordHash = document.getString("passwordHash");
            userCredentialsListsers.add(new UserCredentials(id, username, email, passwordHash));
        }
        return userCredentialsListsers;
    }

    public void addUser(final User user)
    {
        final Document document = new Document("_id", user.getId())
                .append("name", user.getName())
                .append("age", user.getAge())
                .append("description", user.getDescription())
                .append("email", user.getEmail())
                .append("location", user.getLocation())
                .append("registrationDate", user.getRegistrationDate().toString());

        final MongoCollection<Document> usersDocs = this.mongoDatabase.getCollection("users");
        usersDocs.insertOne(document);
    }

    @Override
    public void updateUser(final User user)
    {

    }

    @Override
    public boolean deleteUser(final int id)
    {
        boolean hasDeletedCredentials = false;
        boolean hasDeletedUser = false;

        final MongoCollection<Document> usersCredentials = this.mongoDatabase.getCollection("usercredentials");
        DeleteResult deleteResult = usersCredentials.deleteOne(Filters.eq("_id", id));
        if(deleteResult.getDeletedCount() == 1)
            hasDeletedCredentials = true;

        final MongoCollection<Document> users = this.mongoDatabase.getCollection("users");
        DeleteResult deleteResult1 = users.deleteOne(Filters.eq("_id", id));
        if(deleteResult1.getDeletedCount() == 1)
            hasDeletedUser = true;

        return hasDeletedCredentials && hasDeletedUser;
    }

    public void addUserCredentials(final UserCredentials userCredentials)
    {
        final Document document = new Document("_id", userCredentials.getUserId())
                .append("username", userCredentials.getUsername())
                .append("email", userCredentials.getEmail())
                .append("passwordHash", userCredentials.getPasswordHash());

        final MongoCollection<Document> usersDocs = this.mongoDatabase.getCollection("usercredentials");
        usersDocs.insertOne(document);
    }

    @Override
    public void updateUserCredentials(final UserCredentials userCredentials)
    {

    }

    @Override
    public void deleteUserCredentials(final int id)
    {

    }

    @Override
    public Optional<User> getUser(final int id)
    {
        final User user = this.mongoDatabase.getCollection("users").find(Filters.eq("_id", id), User.class).first();
        if (user != null)
            return Optional.of(user);
        return Optional.empty();
    }
}
