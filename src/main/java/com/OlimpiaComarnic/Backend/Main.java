package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;



public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = DBConnection.openConn();
        assert mongoClient != null;
        MongoDatabase projectDB = mongoClient.getDatabase("projectDB");
        MongoCollection players = projectDB.getCollection("players");
        for (Object o : players.find()) {
            System.out.println(o);
        }
        System.out.println("DB open successfully");
        DBConnection.closeConn(mongoClient);
        System.out.println("DB closed successfully");

    }
}
