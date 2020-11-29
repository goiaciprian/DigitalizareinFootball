package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.utils.DBConnection;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) {

        System.out.println(UserDAO.findAll());

        DBConnection.closeConn();
    }
}
