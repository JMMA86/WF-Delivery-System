package com.wf.wfdeliverysystem.implementations;

import com.wf.wfdeliverysystem.model.House;
import com.wf.wfdeliverysystem.model.IWFDelivery;
import javafx.util.Pair;

import java.util.ArrayList;

public class ListGraph<T> implements IWFDelivery<T> {
    private final ArrayList<ListVertex<T>> list;

    public ListGraph() {
        list = new ArrayList<>();
    }

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
}
