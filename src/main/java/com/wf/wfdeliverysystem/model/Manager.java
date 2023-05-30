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
    private final IGraph<House> list;
    private final IGraph<House> matrix;

    private Manager(int vertices) {
        list = new ListGraph<>(false, false, false);
        matrix = new MatrixGraph<>(false, vertices);
    }

    @Override
    public boolean checkPathBetweenHouses(House h1, House h2) {
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

    @Override
    public ArrayList<House> calculateMinimumPath(House h1, House h2) {
        return null;
    }

    @Override
    public ArrayList<Pair<House, House>> generateDeliveryTour(House h0) {
        return null;
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
