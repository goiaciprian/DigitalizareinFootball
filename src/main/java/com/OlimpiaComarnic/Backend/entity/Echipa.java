package com.OlimpiaComarnic.Backend.entity;

import java.util.ArrayList;

/**
 * Clasa echipa contine:
 *      - Numele si prescurtarea echipei
 *      - Lista jucatori titulari;
 *      - Lista jucatori rezerve
 *      - Stadionul
 *      - Antrenorul
 */
public class Echipa {
    //TODO obiect echipa folosit ca parametru pentru Match.java (echipe)

    ArrayList<Player> titulari;
    ArrayList<Player> rezerve;
    private String numeEchipa;
    private String numePrescurtat;
    private String antrenor;
    private String Stadion;

    //TODO Echipa builder
}
