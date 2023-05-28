package com.wf.wfdeliverysystem.implementations;

public class MatrixVertex<T> {
    private T Value;
    private int distance;
    private int time;
    private Color color;

    public MatrixVertex(T Value) {
        this.Value = Value;
    }

    public T getValue() {
        return Value;
    }

    public void setValue(T value) {
        Value = value;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
