package com.wf.wfdeliverysystem.model;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class DeliveryCycle extends Element {
    private boolean moving;
    private Point2D movement;

    private int currentTour;
    private double SPEED = 0.05;
    private Point2D defaultPosition;

    ArrayList<Pair<Point2D, Point2D>> tour;

    public DeliveryCycle(Point2D coords, Image picture) {
        super(coords, picture);
        this.moving = false;
        this.movement = new Point2D(0, 0);
        this.currentTour = -1;
        this.tour = new ArrayList<>();
        defaultPosition = coords;
    }

    public ArrayList<Pair<Point2D, Point2D>> getTour() {
        return tour;
    }

    public void setTour(ArrayList<Pair<Element, Element>> edges) {
        ArrayList<Pair<Point2D, Point2D>> points = parseToPoints(edges);
        // TODO: fix the tour so that it goes through the tree in depth.
        this.tour = points;
        currentTour = 0;
        moveThrough(currentTour);
    }

    private ArrayList<Pair<Point2D, Point2D>> parseToPoints(ArrayList<Pair<Element, Element>> edges) {
        ArrayList<Pair<Point2D, Point2D>> points = new ArrayList<>();
        for(Pair<Element, Element> p : edges) {
            points.add( new Pair<>( p.getKey().getCoords(), p.getValue().getCoords()) );
        }
        return points;
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

    public void resetMovement() {
        setCoords(defaultPosition);
        setMoving(false);
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
