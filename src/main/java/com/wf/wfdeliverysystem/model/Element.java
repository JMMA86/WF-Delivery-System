package com.wf.wfdeliverysystem.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Element {
    private Point2D coords;
    private Image picture;

    public Element(Point2D coords, Image picture) {
        this.coords = coords;
        this.picture = picture;
    }

    public Point2D getCoords() {
        return coords;
    }

    public void setCoords(Point2D coords) {
        this.coords = coords;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public void drawCircle(Color color, double size, GraphicsContext context) {
        // set coordinates
        double x = coords.getX()*size-size/4, y = coords.getY()*size-size/4;
        // fill border
        context.setFill(Color.BLACK);
        context.fillOval(x-size/20, y-size/20, size/2+size/10, size/2+size/10);
        // fill background
        context.setFill(color);
        context.fillOval(x, y, size/2, size/2);
        // draw the image
        context.drawImage(picture, x, y, size/2, size/2);
    }
}
