package com.wf.wfdeliverysystem.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class House extends Element implements Comparable<House> {
    private String id;

    public House(Point2D coords, Image picture, String id) {
        super(coords, picture);
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
        return "House{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public int compareTo(House h) {
        return this.getId().compareTo(h.getId());
    }
}
