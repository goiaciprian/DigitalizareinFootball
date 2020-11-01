package com.OlimpiaComarnic.Backend.entity;

import java.util.HashMap;
import java.util.List;


/**
 *  Clasa player contine:
 *      - nume prenume jucator
 *      - numar tricou
 *      - numar cartonase rosii si galbene
 *      - nuamr goluri si pase de gol
 *      - numar paritii (minute pe meci)
 */
public class Player {

    private String nume;
    private String prenume;
    private int goluri;
    private int paseGol;
    private int cartonaseRosii, cartonaseGalbene;
    private HashMap<Match, Integer> aparitii;
    private int numarTricou;

    public Player(String nume, String prenume, int numarTricou) {
        this.nume = nume;
        this.prenume = prenume;
        this.numarTricou = numarTricou;
        this.goluri = 0;
        this.paseGol = 0;
        this.cartonaseGalbene = 0;
        this.cartonaseRosii = 0;
        this.aparitii = new HashMap<>();
    }

    /**
     * Afisare numar de goluri
     *
     * @return numarul total de goluri
     */
    public int getGoluri() {
        return goluri;
    }

    /**
     * Adauga numarul de goluri noi la totalul de goluri
     *
     * @param goluri numar de goluri noi
     */
    public void addGoluri(int goluri) {
        if (goluri > 0)
            this.goluri += goluri;
    }

    /**
     * Afisare numar de pase de gol
     *
     * @return numarul total de pase de gol
     */
    public int getPaseGol() {
        return paseGol;
    }

    /**
     * Adauga numarul de pase de gol noi la totalul de pase date de acest jcator
     *
     * @param paseGol numar de pase de gol noi
     */
    public void addPaseGol(int paseGol) {
        if (paseGol > 0)
            this.paseGol += paseGol;
    }

    /**
     * Returneaza numarul total de cartonase rosii
     *
     * @return numarul total de cartonase rosii
     */
    public int getCartonaseRosii() {
        return cartonaseRosii;
    }

    /**
     * Adauga noile cartonase rosii la total
     *
     * @param cartonaseRosii numar de cartonase rosii noi
     */
    public void addCartonaseRosii(int cartonaseRosii) {
        if (cartonaseRosii > 0) this.cartonaseRosii += cartonaseRosii;
    }

    /**
     * Returneaza numarul total de cartonase galbele
     *
     * @return numarul total de carnotase galbene
     */
    public int getCartonaseGalbene() {
        return cartonaseGalbene;
    }

    /**
     * Adauga noile cartonase galbele la totalul de cartonase galbene
     *
     * @param cartonaseGalbene numar nou de cartonase galbene
     */
    public void setCartonaseGalbene(int cartonaseGalbene) {
        this.cartonaseGalbene = cartonaseGalbene;
    }

    /**
     * Returneaza numarul curent de pe tricoul unui jucator
     *
     * @return nuamrul de pe tricoul jucatorului
     */
    public int getNumarTricou() {
        return numarTricou;
    }

    /**
     * Schimbare numarului de pe tricoul unui jucator
     *
     * @param numarTricou numar nou pentru tricou
     */
    public void setNumarTricou(int numarTricou) {
        this.numarTricou = numarTricou;
    }


    /**
     * Schimba numele jucatorului, daca este cazul
     *
     * @param nume    numele nou pentru jucator
     * @param prenume prenumele nou pentru jucator
     */
    public void setNume(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }

    /**
     * Rezurneaza numele jucatorului
     *
     * @return numele jucatorului
     */
    public String getNume() {
        return this.nume + " " + this.prenume;
    }

    /**
     * Metoda care adauga numarul de minute jucate intr-un meci
     * @param minute numarul de minute jucate
     * @param meci micul in care a jucat
     */
    public void addAparitie(int minute, Match meci) {
        if(minute < 0)
            return;
        aparitii.put(meci, minute);
    }

    /**
     *  Returneaza un hashmap cu toate meciurile si minutele jucate
     * @return toate meciuile si minutele jucte
     */
    public HashMap<Match, Integer> getAparitii() {
        return aparitii;
    }

    public int minuteJucateInMeci(Match meci) {
        for(Match m: aparitii.keySet()) {
            if(m == meci)
                return aparitii.get(meci);
        }
        return 0;
    }

}
