package com.example.gamelife;

import java.util.ArrayList;
import java.util.Set;

/**
 * Trieda popisujúca jednu vykreslenú bunku.
 */
public class Cell {
    private final ArrayList<Cell> neighbours = new ArrayList<>();
    private boolean live = false;
    private boolean tempLive = false;
    private final Set<Integer> rules = LifePane.getRules();

    /**
     * Metóda pridá bunke nového suseda do zoznamu neighbours.
     * @param cell Nová susedná bunka
     */
    public void addNeighbour(Cell cell) {
        neighbours.add(cell);
    }

    /**
     * Metóda prepína stav bunky.
     */
    public void switchLive() {
        live = !live;
        tempLive = live;
    }

    /**
     * Metóda vracia aktuálny stav bunky.
     *
     * @return Ak bunka žije, vráti true, inak vráti false.
     */
    public boolean alive() {
        return live;
    }

    /**
     * Metóda vypočíta stav bunky v ďalšej generácii na základe pravidiel.
     */
    public void newGen() {
        int living = 0;
        for (Cell cell : neighbours) {
            if (cell.alive()) living++;

            if (rules.contains(0) && live && living > 3) {
                tempLive = false;
                return;
            }
        }

        if (rules.contains(1) && live && living < 2) {
            tempLive = false;
            return;
        }

        if (rules.contains(2) && live && (living == 2 || living == 3)) {
            tempLive = true;
            return;
        }

        if (rules.contains(3) && !live && living == 3) {
            tempLive = true;
        }
    }

    /**
     * Metóda aktualizuje stav bunky do ďalšej generácie.
     */
    public void update() {
        live = tempLive;
    }
}
