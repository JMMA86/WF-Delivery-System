package com.wf.wfdeliverysystem.model;

import javafx.util.Pair;

import java.util.ArrayList;

public class Manager implements IWFDelivery {
    static private Manager manager = new Manager();
    private boolean isMatrix;

    private Manager() {}

    @Override
    public boolean checkPathBetweenHouses(House h1, House h2) {
        return false;
    }

    @Override
    public ArrayList<House> calculateMinimumPath(House h1, House h2) {
        return null;
    }

    @Override
    public ArrayList<Pair<House, House>> generateDeliveryTour(House h0) {
        return null;
    }

    static public Manager getInstance() {
        return manager;
    }
}
