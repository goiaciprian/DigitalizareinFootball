package com.OlimpiaComarnic.Backend.dao;

import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {


    public synchronized static List<User> findAll() {
        List<User> rez = new ArrayList<>();

        MongoClient con = DBConnection.openConn();
        assert con != null;
        MongoDatabase project =  con.getDatabase("projectDB");
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
        } finally {
            DBConnection.closeConn(con);
        }

        return rez;
    }

    public synchronized static User findUser(String username) {
        User userO = null;
        MongoClient con = DBConnection.openConn();
        assert con != null;
        MongoDatabase proiect = con.getDatabase("projectDB");
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

        } finally {
            DBConnection.closeConn(con);
        }

        return userO;
    }

    public synchronized static void insertUser(User user) {
        new Thread(() -> {
            if(findUser(user.getUsername()).equals(user))
                return;
            Document userDB = new Document()
                    .append("username", user.getUsername())
                    .append("password", user.getEncPassword())
                    .append("isAdmin", user.isAdmin());

            MongoClient con = DBConnection.openConn();
            assert con != null;
            MongoDatabase project = con.getDatabase("projectDB");
            MongoCollection<Document> users = project.getCollection("users");
            users.insertOne(userDB);

            DBConnection.closeConn(con);

        }).start();
    }

    public synchronized static void updateUser(User oldUser, User newUser) {
        new Thread( ()-> {
            if(!(findUser(oldUser.getUsername()).equals(oldUser)))
                return;

            MongoClient con = DBConnection.openConn();
            assert con != null;
            MongoDatabase proiect = con.getDatabase("projectDB");
            MongoCollection<Document> collection = proiect.getCollection("users");
            if(!oldUser.getUsername().equals(newUser.getUsername())) {

            }

        }).start();
    }

}
