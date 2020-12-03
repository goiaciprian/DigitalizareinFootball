package com.OlimpiaComarnic.Backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException {


        Date a = new Date();
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        System.out.println(SimpleDateFormat.format(a));
//        DBConnection.closeConn();
    }
}
