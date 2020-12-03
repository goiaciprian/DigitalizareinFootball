package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException {


        PlayerDAO.insertPlayer(new Player("Dany", "dany123", 11));
        PlayerDAO.worker.join();
        UserDAO.insertUser(new User("dany123", "dany123", false));
        UserDAO.worker.join();
        DBConnection.closeConn();
    }
}
