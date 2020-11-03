package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
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


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) {

        Player pl1 = PlayerDAO.findOne("da da");
        Player pl2 = new Player("da da", 12);
        pl2.setPaseGol(12);
        pl2.setGoluri(5);
        pl2.addAparitie(90, "meci1");
        pl2.addAparitie(44, "meci2");
        pl2.addAparitie( 60,"meci3");
//        pl1.setCartonaseGalbene(4);
//        pl1.setCartonaseRosii(1);
//        pl1.setGoluri(3);
//        pl1.setPaseGol(4);
//        pl1.addAparitie(90, "primulMeci");
//        pl1.addAparitie(85, "al doilea meci");
//
        PlayerDAO.deleteOne(pl2);


    }
}
