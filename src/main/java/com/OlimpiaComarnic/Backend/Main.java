package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Scanner;


//TODO metode de alterat informati in baza de date

// ? metoda de stergere din baza de date sa fie statica pentru orice obiect ? ia ca parametru obiectul pe care sa-l stearga

/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) {

        User a = new User("asd", "sad", true);
        List<User> as = UserDAO.findAll();

        System.out.println("inainte de");
        for(User us: as) {
            System.out.println(us);
        }

    }
}
