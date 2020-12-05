package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.utils.DBConnection;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException {


        System.out.println(EvenimentDAO.findAll());
        DBConnection.closeConn();
    }
}
