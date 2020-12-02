package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.EvenimentDAO;
import com.OlimpiaComarnic.Backend.entity.Eveniment;
import com.OlimpiaComarnic.Backend.utils.DBConnection;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException {

//        Eveniment ad = EvenimentDAO.findAll().get(0);
//        System.out.println(ad);
//        try {
//            Date a = ad.getDate();
//            Date curr = new Date();
//            System.out.println(a);
//            System.out.println(curr);
//
//            System.out.println(curr.compareTo(a));
//        } catch (Exception e) {
//            throw e;
//        }
        EvenimentDAO.updateEventById("5fc7fbef2a8b293a227f5903", new Eveniment("asdsad", new Date())).get();
        DBConnection.closeConn();
    }
}
