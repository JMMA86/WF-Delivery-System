package com.wf.wfdeliverysystem.implementations;

public class ListEdge<T> {
    private final ListVertex<T> leftVertex;
    private final ListVertex<T> rightVertex;
    private final int weight;

    public ListEdge(ListVertex<T> leftVertex, ListVertex<T> rightVertex, int weight) {
        this.leftVertex = leftVertex;
        this.rightVertex = rightVertex;
        this.weight = weight;
    }

    public ListVertex<T> getLeftVertex() {
        return leftVertex;
    }

    public ListVertex<T> getRightVertex() {
        return rightVertex;
    }

    public int getWeight() {
        return weight;
    }
}
