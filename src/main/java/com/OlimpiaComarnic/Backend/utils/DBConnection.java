package com.OlimpiaComarnic.Backend.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class DBConnection {

    private final static String connStr = "mongodb+srv://application:SldBoKoldBDTLYFp@sdapcluster.5axsh.mongodb.net/projectDB?retryWrites=true&w=majority";

    public static MongoClient openConn() {
        MongoClientURI url = new MongoClientURI(connStr);
        try {
            MongoClient mongoClient = new MongoClient(url);
            return mongoClient;
        } catch (Exception ex) {
            System.out.println("Erroare in conectare: " + ex.getMessage());
            return null;
        }
    }

    public static void closeConn(MongoClient mongcl) {
        if(mongcl != null) {
            try {
                mongcl.close();
            } catch (Exception ignored) { };
        }
    }
}