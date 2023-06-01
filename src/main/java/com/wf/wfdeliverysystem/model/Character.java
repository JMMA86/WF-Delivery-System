package com.wf.wfdeliverysystem.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Character extends Element {
    private Point2D coords;
    private Image picture;

    public Character(double size, GraphicsContext context, Point2D coords, Image picture) {
        super(context);
        this.coords = coords;
        this.picture = picture;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public void draw(double size, double scale) {
        // set coordinates
        double x = coords.getX()*size-size/4*scale, y = coords.getY()*size-size/4*scale;
        // fill border
        getContext().setFill(Color.BLACK);
        getContext().fillOval(x-size/20*scale, y-size/20*scale, (size/2+size/10)*scale, (size/2+size/10)*scale);
        // fill background
        getContext().setFill(getBackground());
        getContext().fillOval(x, y, size/2*scale, size/2*scale);
        // draw the image
        getContext().drawImage(picture, x, y, size/2*scale, size/2*scale);
    }

    public Point2D getCoords() {
        return coords;
    }

    public void setCoords(Point2D coords) {
        this.coords = coords;
    }

    @Override
    public void setState(int state) {
        switch (state) {
            case 0 -> setBackground(Color.BEIGE);
            case 1 -> setBackground(Color.RED);
        }
    }
}

