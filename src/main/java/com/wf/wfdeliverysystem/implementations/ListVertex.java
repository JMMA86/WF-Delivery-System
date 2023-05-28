package com.wf.wfdeliverysystem.implementations;

import java.util.ArrayList;

public class ListVertex<T> {
    private T value;
    private ArrayList<ListEdge<T>> edges;

    public ListVertex(T value, ArrayList<ListEdge<T>> edges) {
        this.value = value;
        this.edges = edges;
    }

    public T getValue() {
        return value;
    }

    public ArrayList<ListEdge<T>> getEdges() {
        return edges;
    }
}
