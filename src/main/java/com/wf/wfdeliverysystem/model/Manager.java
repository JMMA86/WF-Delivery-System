package com.wf.wfdeliverysystem.model;

import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.implementations.IGraph;
import com.wf.wfdeliverysystem.implementations.ListGraph;
import com.wf.wfdeliverysystem.implementations.MatrixGraph;
import javafx.util.Pair;

import java.util.ArrayList;

public class Manager implements IWFDelivery {
    static private final Manager manager = new Manager(60);
    //False = listGraph
    //True = matrixGraph
    private boolean isMatrix;
    private final IGraph<House> list;
    private final IGraph<House> matrix;

    public Manager(int vertices) {
        list = new ListGraph<>(false, false, false);
        matrix = new MatrixGraph<>(false, vertices);
    }

    public void addHouse(House h) throws VertexAlreadyAddedException {
        list.addVertex(h);
        matrix.addVertex(h);
    }

    public void addEdge(House h1, House h2, String id, int weight) throws LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        list.addEdge(h1, h2, id, weight);
        matrix.addEdge(h1, h2, id, weight);
    }

    public String[] printRepresentation() {
        return new String[] {
                list.toString(),
                matrix.toString()
        };
    }

    //Using BFS algorithm
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

    //Using Dijkstra algorithm
    @Override
    public ArrayList<Pair<House, House>> calculateMinimumPath(House h1, House h2) throws VertexNotAchievableException, VertexNotFoundException {
        if (isMatrix) {
            return matrix.dijkstra(h1, h2);
        } else {
            return list.dijkstra(h1, h2);
        }
    }

    //Using Prim algorithm
    @Override
    public ArrayList<Pair<House, House>> generateDeliveryTour(House h0) throws VertexNotFoundException {
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
        isMatrix = matrix;
    }

    static public Manager getInstance() {
        return manager;
    }
}
