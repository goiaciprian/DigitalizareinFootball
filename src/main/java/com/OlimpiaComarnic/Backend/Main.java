package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException {

        Player p1 = new Player("Emy Dumitrasc", "emy112", 5);
        p1.setPaseGol(3);
        p1.setCartonaseRosii(1);
        p1.setCartonaseGalbene(3);
        p1.setGoluri(1);
        p1.addAparitie(90, "meci1");
        p1.addAparitie(90, "meci2");
        p1.addAparitie(70, "meci3");
        p1.addAparitie(85, "meci4");
        p1.addAparitie(78, "meci5");
        p1.addAparitie(80, "meci6");
        p1.addAparitie(55, "meci7");

        PlayerDAO.insertPlayer(p1);
        UserDAO.insertUser(new User("emy112", "emy112", false));
        UserDAO.worker.join();
        PlayerDAO.worker.join();
        DBConnection.closeConn();
    }
}
