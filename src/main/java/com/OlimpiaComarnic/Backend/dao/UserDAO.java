package com.OlimpiaComarnic.Backend.dao;

import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UserDAO {

    /**
     * Gets all the user from database
     * @return list with all the users
     */
    public static List<User> findAll() {
        List<User> rez = new ArrayList<>();

        MongoDatabase project =  DBConnection.getDatabase();
        MongoCollection<Document> users = project.getCollection("users");

        try (MongoCursor<Document> cursor = users.find().iterator()) {
            while (cursor.hasNext()) {
                Document userDB = cursor.next();
                User user = new User(userDB.get("_id").toString());
                user.setUsername(userDB.getString("username"));
                user.setEncPassword(userDB.getString("password"));
                user.setAdmin(userDB.getBoolean("isAdmin"));
                rez.add(user);
            }
        }

        return rez;
    }

    /**
     * Finds an user in database by name
     * @param username name of the user
     * @return the user found or null
     */
    public static User findUser(String username) {
        User userO = null;
        MongoDatabase proiect = DBConnection.getDatabase();
        if(proiect == null)
            return null;
        MongoCollection<Document> users = proiect.getCollection("users");

        try (MongoCursor<Document> cursor = users.find().iterator()) {
            while (cursor.hasNext()) {
                Document currUser = cursor.next();
                if(currUser.getString("username").equals(username)) {
                    userO = new User(currUser.get("_id").toString());
                    userO.setAdmin(currUser.getBoolean("isAdmin"));
                    userO.setUsername(currUser.getString("username"));
                    userO.setEncPassword(currUser.getString("password"));
                    break;
                }
            }

        }

        return userO;
    }

    /**
     * Add a new user to the database async
     *
     * @param user new user
     * @return Awaitable object
     */
    public synchronized static CompletableFuture<Void> insertUser(User user) {
        return CompletableFuture.runAsync(() -> {
            if (Objects.equals(findUser(user.getUsername()), user))
                return;
            Document userDB = new Document()
                    .append("username", user.getUsername())
                    .append("password", user.getEncPassword())
                    .append("isAdmin", user.isAdmin());

            MongoDatabase project = DBConnection.getDatabase();
            MongoCollection<Document> users = project.getCollection("users");
            users.insertOne(userDB);


        });
    }

    /**
     * Updates the a user async
     *
     * @param user_id old user id that needs to be updated
     * @param newUser new user that will be replaced with
     * @return Awaitable object
     */
    public synchronized static CompletableFuture<Void> updateUserById(String user_id, User newUser) {
        return CompletableFuture.runAsync(() -> {

            Document toReplace = new Document()
                    .append("username", newUser.getUsername())
                    .append("password", newUser.getEncPassword())
                    .append("isAdmin", newUser.isAdmin());

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> collection = proiect.getCollection("users");

            collection.replaceOne(Filters.eq("_id", new ObjectId(user_id)), toReplace);

        });
    }

    public synchronized static CompletableFuture<Void> updateUserByUsername(String username, User newUser) {
        return CompletableFuture.runAsync(() -> {

            Document toReplace = new Document()
                    .append("username", newUser.getUsername())
                    .append("password", newUser.getEncPassword())
                    .append("isAdmin", newUser.isAdmin());

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> collection = proiect.getCollection("users");

            collection.replaceOne(Filters.eq("username", username), toReplace);

        });
    }

    /**
     * Deletes an user from database
     *
     * @param username the user username that needs to be deleted
     */
    public static CompletableFuture<Void> deleteUser(String username) {
        return CompletableFuture.runAsync(() -> {

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> users = proiect.getCollection("users");

            users.deleteOne(Filters.eq("username", username));
        });
    }

}
