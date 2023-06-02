package com.wf.wfdeliverysystem.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Street extends Element {
    private Point2D p1;
    private Point2D p2;
    private int weight;

    public Street(GraphicsContext context, Point2D p1, Point2D p2, int weight) {
        super(context);
        this.p1 = p1;
        this.p2 = p2;
        this.weight = weight;
    }

    @Override
    public void setState(int state) {
        switch (state) {
            case 0 -> setBackground(Color.GRAY);
            case 1 -> setBackground(Color.TURQUOISE);
        }
    }



    public Point2D getP1() {
        return p1;
    }

    public void setP1(Point2D p1) {
        this.p1 = p1;
    }

    public Point2D getP2() {
        return p2;
    }

    public void setP2(Point2D p2) {
        this.p2 = p2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
