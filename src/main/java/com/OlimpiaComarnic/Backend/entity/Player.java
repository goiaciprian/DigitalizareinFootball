package com.OlimpiaComarnic.Backend.entity;

import java.util.HashMap;
import java.util.Map;


/**
 * Clasa player contine:
 * - nume prenume jucator
 * - numar tricou
 * - numar cartonase rosii si galbene
 * - nuamr goluri si pase de gol
 * - numar paritii (minute pe meci)
 */
public class Player {

    private String nume, username;
    private int goluri;
    private int paseGol;
    private int cartonaseRosii, cartonaseGalbene;
    private HashMap<String, Integer> aparitii;
    private int numarTricou;

    public Player(String nume, String username, int numarTricou) {
        this.nume = nume;
        this.username = username;
        this.numarTricou = numarTricou;
        this.goluri = 0;
        this.paseGol = 0;
        this.cartonaseGalbene = 0;
        this.cartonaseRosii = 0;
        this.aparitii = new HashMap<>();
    }

    public Player(String username) {
        this.nume = "null";
        this.username = username;
        this.numarTricou = -1;
        this.goluri = 0;
        this.paseGol = 0;
        this.cartonaseGalbene = 0;
        this.cartonaseRosii = 0;
        this.aparitii = new HashMap<>();
    }

    public Player() {
        this.nume = "null";
        this.username = "null";
        this.numarTricou = -1;
        this.goluri = 0;
        this.paseGol = 0;
        this.cartonaseGalbene = 0;
        this.cartonaseRosii = 0;
        this.aparitii = new HashMap<>();
    }

    /**
     * Gets the username associated with the user account
     *
     * @return username associated the account
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets associated username to player account
     *
     * @param username associate username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Adauga la totalul de goluri
     *
     * @param goluri nr gouri date
     */
    public void setGoluri(int goluri) {
        this.goluri = goluri;
    }

    /**
     * Adauga la totalul de pase de gol
     * @param paseGol nr pase de gol
     */
    public void setPaseGol(int paseGol) {
        this.paseGol = paseGol;
    }

    /**
     * Adauga la totalul de cartonase rosii
     * @param cartonaseRosii nr cartonase rosii
     */
    public void setCartonaseRosii(int cartonaseRosii) {
        this.cartonaseRosii = cartonaseRosii;
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
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     * Rezurneaza numele jucatorului
     *
     * @return numele jucatorului
     */
    public String getNume() {
        return this.nume;
    }

    /**
     * Metoda care adauga numarul de minute jucate intr-un meci
     * @param minute numarul de minute jucate
     * @param titluMeci titlul meciului in care a jucat
     */
    public void addAparitie(int minute, String titluMeci) {
        if(minute < 0)
            return;
        aparitii.put(titluMeci, minute);
    }

    /**
     *  Returneaza un hashmap cu toate meciurile si minutele jucate
     * @return toate meciuile si minutele jucte
     */
    public HashMap<String, Integer> getAparitii() {
        return aparitii;
    }

    public int minuteJucateInMeci(String titluMeci) {
        for(String m: aparitii.keySet()) {
            if(m.equals(titluMeci))
                return aparitii.get(titluMeci);
        }
        return 0;
    }

    @Override
    public String toString() {

        StringBuilder apar = new StringBuilder();
        for(Map.Entry<String, Integer> kv: aparitii.entrySet()) {
            apar.append(" ").append(kv.getKey()).append(" - ").append(kv.getValue()).append(" ");
        }

        return "Player{" +
                "nume='" + nume + '\'' +
                ", username='" + username + '\'' +
                ", goluri=" + goluri +
                ", paseGol=" + paseGol +
                ", cartonaseRosii=" + cartonaseRosii +
                ", cartonaseGalbene=" + cartonaseGalbene +
                ", numarTricou=" + numarTricou +
                ",\n aparitii: " + apar.toString() + '}';
    }
}
