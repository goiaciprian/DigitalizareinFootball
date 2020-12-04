package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.utils.DBConnection;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException {


        System.out.println(UserDAO.findUser("user"));
        System.out.println(PlayerDAO.findOneByUsername("user"));
        DBConnection.closeConn();
    }
}
