package com.wf.wfdeliverysystem.implementations;

import java.util.ArrayList;

public class ListVertex<T> implements Comparable<ListVertex<T>> {
    //Vertex values
    private T value;
    private ArrayList<ListEdge<T>> edges;
    //For algorithm proposals
    private Color color;
    private int distance;
    private ListVertex<T> father;

    public ListVertex(T value) {
        this.value = value;
        edges = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public ArrayList<ListEdge<T>> getEdges() {
        return edges;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public ListVertex<T> getFather() {
        return father;
    }

    public void setFather(ListVertex<T> father) {
        this.father = father;
    }

    @Override
    public int compareTo(ListVertex<T> o) {
        return Integer.compare(this.distance, o.getDistance());
    }
}
