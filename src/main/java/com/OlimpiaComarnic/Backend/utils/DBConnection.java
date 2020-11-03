package com.OlimpiaComarnic.Backend.utils;

import com.OlimpiaComarnic.GUI.Popup.Popup1;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoConfigurationException;


/**
 * Clasa care realizeaza conexiunea la baza de date
 *
 */
public class DBConnection {

    private final static String connStr = "mongodb+srv://application:SldBoKoldBDTLYFp@sdapcluster.5axsh.mongodb.net/projectDB?retryWrites=true&w=majority";

    /**
     * Metoda de creare a conexinii catre baza de date
     *
     * @return MongoClient, daca conexiune a fost cu success, si NULL, daca a aparut o erroare
     */
    public static MongoClient openConn()  {

        try {
            MongoClientURI url = new MongoClientURI(connStr);
            return new MongoClient(url);
        }
        catch (MongoConfigurationException ignored) {
            try {
                new Popup1().start(Popup1.returnMainStage());
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
     * @param mongcl Conexiunea care trebuie sa fie inchisa
     */
    public static void closeConn(MongoClient mongcl) {
        if(mongcl != null) {
            try {
                mongcl.close();
            } catch (Exception ignored) { }
        }
    }
}