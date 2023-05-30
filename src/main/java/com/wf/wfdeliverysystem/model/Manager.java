package com.wf.wfdeliverysystem.model;

import com.wf.wfdeliverysystem.exceptions.VertexNotAchievableException;
import com.wf.wfdeliverysystem.exceptions.VertexNotFoundException;
import com.wf.wfdeliverysystem.implementations.IGraph;
import com.wf.wfdeliverysystem.implementations.ListGraph;
import com.wf.wfdeliverysystem.implementations.MatrixGraph;
import javafx.util.Pair;

import java.util.ArrayList;

public class Manager implements IWFDelivery {
    static private final Manager manager = new Manager(50);
    //False = listGraph
    //True = matrixGraph
    private boolean isMatrix;
    private final IGraph<Element> list;
    private final IGraph<Element> matrix;

    public IGraph<Element> getList() {
        return list;
    }

    public IGraph<Element> getMatrix() {
        return matrix;
    }


    public Manager(int vertices) {
        list = new ListGraph<>(false, false, false);
        matrix = new MatrixGraph<>(false, vertices);
    }

    //Using BFS algorithm
    @Override
    public boolean checkPathBetweenHouses(Element h1, Element h2) {
        if (isMatrix) {
            try {
                matrix.calculateDistance(h1, h2);
                return true;
            } catch (VertexNotAchievableException | VertexNotFoundException e) {
                return false;
            }
        } else {
            try {
                list.calculateDistance(h1, h2);
                return true;
            } catch (VertexNotAchievableException | VertexNotFoundException e) {
                return false;
            }
        }
    }

    //Using Dijkstra algorithm
    @Override
    public ArrayList<Pair<Element, Element>> calculateMinimumPath(Element h1, Element h2) throws VertexNotAchievableException, VertexNotFoundException {
        if (isMatrix) {
            return matrix.dijkstra(h1, h2);
        } else {
            return list.dijkstra(h1, h2);
        }
    }

    //Using Prim algorithm
    @Override
    public ArrayList<Pair<Element, Element>> generateDeliveryTour(Element h0) throws VertexNotFoundException {
        if (isMatrix) {
            return matrix.prim(h0);
        } else {
            return list.prim(h0);
        }
    }

    public boolean isMatrix() {
        return this.isMatrix;
    }

    public void setMatrix(boolean matrix) {
        this.isMatrix = matrix;
    }

    static public Manager getInstance() {
        return manager;
    }
}
