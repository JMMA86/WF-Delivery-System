package com.wf.wfdeliverysystem.implementations;

public class ListEdge<T> {
    private final ListVertex<T> leftVertex;
    private final ListVertex<T> rightVertex;
    private final String id;
    private final int weight;

    public ListEdge(ListVertex<T> leftVertex, ListVertex<T> rightVertex, String id, int weight) {
        this.leftVertex = leftVertex;
        this.rightVertex = rightVertex;
        this.id = id;
        this.weight = weight;
    }

    public ListVertex<T> getLeftVertex() {
        return leftVertex;
    }

    public ListVertex<T> getRightVertex() {
        return rightVertex;
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }
}
