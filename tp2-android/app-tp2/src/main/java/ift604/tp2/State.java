package ift604.tp2;

import java.util.ArrayList;

import ift604.common.cargo.Boat;

/**
 * Created by taig1501 on 15-10-13.
 */
public class State {
    private static State ourInstance = new State();

    public static State getInstance() {
        return ourInstance;
    }

    private State() {
    }

    private ArrayList<Boat> boatArrayList;

    public ArrayList<Boat> getBoatArrayList() {
        return boatArrayList;
    }

    public void setBoatArrayList(ArrayList<Boat> boatArrayList) {
        this.boatArrayList = boatArrayList;
    }
}
