package com.OlimpiaComarnic.GUI.Managers.Utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Aparitie {
    SimpleStringProperty numeMeci;
    SimpleIntegerProperty minuteJucate;

    public Aparitie(String numeMeci, int minuteJucate) {
        this.numeMeci = new SimpleStringProperty(numeMeci);
        this.minuteJucate = new SimpleIntegerProperty(minuteJucate);
    }

    public Aparitie() {
    }

    public String getNumeMeci() {
        return numeMeci.get();
    }


    public void setNumeMeci(String numeMeci) {
        this.numeMeci.set(numeMeci);
    }

    public int getMinuteJucate() {
        return minuteJucate.get();
    }


    public void setMinuteJucate(int minuteJucate) {
        this.minuteJucate.set(minuteJucate);
    }

    @Override
    public String toString() {
        return "Aparitie{" +
                "numeMeci=" + numeMeci +
                ", minuteJucate=" + minuteJucate +
                '}';
    }
}
