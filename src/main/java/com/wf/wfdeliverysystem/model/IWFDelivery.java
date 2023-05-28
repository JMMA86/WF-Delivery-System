package com.wf.wfdeliverysystem.model;

import javafx.util.Pair;
import java.util.ArrayList;

public interface IWFDelivery<V> {
    /**
     * Checks if two houses are connected directly or with intermediates
     * @param h1 The initial house
     * @param h2 The target house
     * @return true if the houses are connected.
     */
    boolean checkPathBetweenHouses(House h1, House h2);

    /**
     * Calculates the path that have the minimum weight (duration) between two houses
     * @param h1 The initial house
     * @param h2 The target house
     * @return A list of the houses to be visited in order.
     */
    ArrayList<House> calculateMinimumPath(House h1, House h2);

    /**
     * @param h0 The house from which the tour starts
     * @return A list of house pairs, which represents the edges of the minimum spanning tree, in order.
     */
    ArrayList<Pair<House,House>>  generateDeliveryTour(House h0);
}
