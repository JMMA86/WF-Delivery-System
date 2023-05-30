package com.wf.wfdeliverysystem.model;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryCycle extends Element {
    private boolean moving;
    private Point2D movement;

    private int currentTour;
    private double SPEED = 0.05;

    ArrayList<Pair<Point2D, Point2D>> tour;

    public DeliveryCycle(Point2D coords, Image picture) {
        super(coords, picture);
        this.moving = false;
        this.movement = new Point2D(0, 0);
        this.currentTour = -1;
        this.tour = new ArrayList<>();
    }

    public ArrayList<Pair<Point2D, Point2D>> getTour() {
        return tour;
    }

    public void setTour(ArrayList<Pair<Point2D, Point2D>> points) {
        for(int i=0; i<points.size()-1; i++) {
            int j = i;
            while (j >= 0 && !points.get(i + 1).getKey().equals(points.get(j).getValue())) {
                i++;
                points.add(i, new Pair<>(points.get(j).getValue(), points.get(j).getKey()));
                j--;
            }
        }
        this.tour = points;
        currentTour = 0;
        moveThrough(currentTour);
    }

    public void move() {
        if(currentTour != -1) {
            setCoords( new Point2D( getCoords().getX() + movement.getX(), getCoords().getY() + movement.getY() ) );
            checkDistance();
        }
    }

    public void moveThrough(int currentTour) {
        setCoords( tour.get(currentTour).getKey() );
        setMovement(calculateMovement(tour.get(currentTour).getKey(), tour.get(currentTour).getValue()));
    }

    public void checkDistance() {
        if(calculateDistance(getCoords(), tour.get(currentTour).getValue()) < SPEED) {
            currentTour++;
            if(currentTour >= tour.size()) currentTour = 0;
            moveThrough(currentTour);
        }
    }

    private double calculateDistance(Point2D p0, Point2D p1) {
        return Math.sqrt( Math.pow( p1.getX() - p0.getX() , 2 ) + Math.pow( p1.getY() - p0.getY() , 2 ) );
    }

    private Point2D calculateMovement(Point2D p0, Point2D p1) {
        double dx = p1.getX() - p0.getX(), dy = p1.getY() - p0.getY();
        double magnitude = Math.sqrt( Math.pow(dx, 2) + Math.pow(dy, 2) );
        dx /= magnitude; dy /= magnitude;
        dx *= SPEED; dy *= SPEED;
        return new Point2D(dx, dy);
    }



    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }
}
