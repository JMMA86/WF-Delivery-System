package com.wf.wfdeliverysystem.implementations;

public class MatrixVertex<T> {
    private final T Value;
    private int distance;
    private Color color;
    private MatrixVertex<T> father;

    public MatrixVertex(T Value) {
        this.Value = Value;
    }

    public T getValue() {
        return Value;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public MatrixVertex<T> getFather() {
        return father;
    }

    public void setFather(MatrixVertex<T> father) {
        this.father = father;
    }
}
