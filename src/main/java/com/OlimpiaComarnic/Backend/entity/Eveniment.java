package com.OlimpiaComarnic.Backend.entity;

import java.util.Date;

/**
 * Clasa pentru evenimente viitoare:
 * -_id - id-ul din baza de date;
 * - event - tipul de eveniment: Meci: Barcelona, Antrenament, Amical: Barcelona
 * - date - data si ora evenimentului
 */
public class Eveniment implements Comparable<Eveniment> {
    private String _id;
    private String event; // antrenament, meci
    private Date date;

    public Eveniment(String id) {
        _id = id;
        this.event = "null";
        this.date = new Date();
    }

    public Eveniment(String event, Date date) {
        this._id = "null";
        this.event = event;
        this.date = date;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return "Eveniment{" +
                "event='" + event + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(Eveniment e) {
        return getDate().compareTo(e.getDate());
    }
}
