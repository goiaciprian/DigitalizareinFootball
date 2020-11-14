package com.OlimpiaComarnic.Backend.utils;

import com.OlimpiaComarnic.GUI.GUIRun;
import com.OlimpiaComarnic.GUI.Popup.Popup1;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoConfigurationException;
import com.mongodb.client.MongoDatabase;


/**
 * Clasa care realizeaza conexiunea la baza de date
 *
 */
public class DBConnection {

    private final static String connStr = "mongodb+srv://application:SldBoKoldBDTLYFp@sdapcluster.5axsh.mongodb.net/projectDB?retryWrites=true&w=majority";
    private static MongoClient con = null;
    /**
     * Metoda de creare a conexinii catre baza de date
     *
     * @return MongoDatabase, daca conexiune a fost cu success, si NULL, daca a aparut o erroare
     */
    public static MongoDatabase getDatabase()  {

        try {
            if (con == null) {
                MongoClientURI url = new MongoClientURI(connStr);
                con =  new MongoClient(url);
            }
            return con.getDatabase("projectDB");
        }
        catch (MongoConfigurationException ignored) {

            //todo create popup
            try {
                new Popup1().start(GUIRun.currStage);
            } catch (Exception ignored1) { }
        }
        catch (Exception ex) {
            System.out.println("Erroare in conectare: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Metoda de inchidere a conexiunii catre bazade date
     *
     */
    public static void closeConn() {
        if(con != null) {
            try {
                con.close();
            } catch (Exception ignored) { }
        }
    }
}