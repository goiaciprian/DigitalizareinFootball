package com.OlimpiaComarnic.Backend.dao;

import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAO {

    public static Thread worker;

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
                User user = new User();
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
        MongoCollection<Document> users = proiect.getCollection("users");

        try (MongoCursor<Document> cursor = users.find().iterator()) {
            while (cursor.hasNext()) {
                Document currUser = cursor.next();
                if(currUser.getString("username").equals(username)) {
                    userO = new User();
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
     * Add a new user to the database
     * @param user new user
     */
    public synchronized static void insertUser(User user) {
        worker = new Thread(() -> {
            if(Objects.equals(findUser(user.getUsername()), user))
                return;
            Document userDB = new Document()
                    .append("username", user.getUsername())
                    .append("password", user.getEncPassword())
                    .append("isAdmin", user.isAdmin());

            MongoDatabase project = DBConnection.getDatabase();
            MongoCollection<Document> users = project.getCollection("users");
            users.insertOne(userDB);


        });
        worker.start();
    }

    /**
     *  Updates the a user
     * @param oldUser old user that needs to be updated
     * @param newUser new user that will be replaced with
     */
    public synchronized static void updateUser(User oldUser, User newUser) {
        worker = new Thread( ()-> {

            String oldUsername = oldUser.getUsername();
            String oldEncPass = oldUser.getEncPassword();

            String newUsername = newUser.getUsername();
            String newEncPass = newUser.getEncPassword();

            if(findUser(oldUsername) == null)
                return;

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> collection = proiect.getCollection("users");
            if(!oldUsername.equals(newUsername)) {
                collection.updateOne(Filters.eq("username", oldUsername), Updates.set("username", newUsername));
            }
            if(!oldEncPass.equals(newEncPass)) {
                collection.updateOne(Filters.eq("password", oldEncPass), Updates.set("password", newEncPass));
            }

        });
        worker.start();
    }

    /**
     * Deletes an user from database
     * @param user the user that needs to be deleted
     */
    public static synchronized void deleteUser(User user) {
        worker = new Thread( () -> {
            if(findUser(user.getUsername()) == null)
                return;

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> users = proiect.getCollection("users");

            users.deleteOne(
                    Filters.and(
                            Filters.eq("username", user.getUsername()),
                            Filters.eq("password", user.getEncPassword()),
                            Filters.eq("isAdmin", user.isAdmin())
                    )
            );
        });
        worker.start();
    }

}
