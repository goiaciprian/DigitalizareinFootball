package com.OlimpiaComarnic.Backend.entity;

import com.OlimpiaComarnic.Backend.utils.Positions;

import java.util.ArrayList;


/**
 *  Clasa player contine:
 *      - nume prenume jucator
 *      - pozitiile pe care poate sa joace
 *      - daca este suspendat sau accidentat
 */
public class Player implements Runnable{

    private String nume;
    private String prenume;
    private ArrayList<Positions> positions;
    private boolean accidentat;
    private boolean suspendat;

    public Player(String nume) {
        this.nume = nume;
    }

    /**
     * Method from Runnable interface
     */
    @Override
    public void run() {
        //TODO create document for mongo, add clss variables to it and push it to mongo;
        for(int i = 0; i < 100; i++) {
            System.out.println("new thread + " + i);
        }
    }

    /**
     *  Creats a new thread that inserts the object to database
     */
    public void insertIntoDB() {
        new Thread(this).start();
    }

    //TODO Player builder

    // TODO instanciate metode form builder should start a new thread that inserts into db if not found, else update


}
