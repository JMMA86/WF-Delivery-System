package com.wf.wfdeliverysystem.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class House extends Character implements Comparable<House> {
    private String id;

    public House(double size, GraphicsContext context, Point2D coords, Image picture, String id) {
        super(size, context, coords, picture);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("(%s)", id);
    }

    @Override
    public int compareTo(House h) {
        return this.getId().compareTo(h.getId());
    }

}
