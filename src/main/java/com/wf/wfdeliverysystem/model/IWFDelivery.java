package com.wf.wfdeliverysystem.model;

import com.wf.wfdeliverysystem.exceptions.VertexNotAchievableException;
import com.wf.wfdeliverysystem.exceptions.VertexNotFoundException;
import javafx.util.Pair;
import java.util.ArrayList;

public interface IWFDelivery<V> {
    /**
     * Checks if two houses are connected directly or with intermediates
     * @param h1 The initial house
     * @param h2 The target house
     * @return true if the houses are connected.
     */
    boolean checkPathBetweenHouses(Element h1, Element h2);

    /**
     * Calculates the path that have the minimum weight (duration) between two houses
     * @param h1 The initial house
     * @param h2 The target house
     * @return A list of the houses to be visited in order.
     */
    ArrayList<Element> calculateMinimumPath(Element h1, Element h2) throws VertexNotAchievableException, VertexNotFoundException;

    /**
     * @param h0 The house from which the tour starts
     * @return A list of house pairs, which represents the edges of the minimum spanning tree, in order.
     */
    ArrayList<Pair<Element,Element>>  generateDeliveryTour(Element h0) throws VertexNotFoundException;
}
