package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import java.util.Scanner;


//TODO metode de alterat informati in baza de date

// ? metoda de stergere din baza de date sa fie statica pentru orice obiect ? ia ca parametru obiectul pe care sa-l stearga

/***
Clasa main pentru teste in backend
 */
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

        Player a = new Player("Andrei");
        Player b = new Player("Ionut");

        a.insertIntoDB();
        b.insertIntoDB();

        Scanner scan = new Scanner(System.in);
        int aasd = 0;
        while ( aasd != 10 ) {
            System.out.print(">");
            aasd = scan.nextInt();
            System.out.println("Input " + aasd);
        }

        DBConnection.closeConn(mongoClient);
        System.out.println("DB closed successfully");

    }
}
