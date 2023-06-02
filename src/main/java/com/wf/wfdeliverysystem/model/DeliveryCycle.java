package com.wf.wfdeliverysystem.model;

import com.wf.wfdeliverysystem.Launcher;
import com.wf.wfdeliverysystem.exceptions.VertexNotAchievableException;
import com.wf.wfdeliverysystem.exceptions.VertexNotFoundException;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

public class DeliveryCycle extends Character {
    private boolean moving;
    private Point2D movement;

    private int currentTour;
    private final double SPEED = 0.05;
    private final Point2D defaultPosition;

    ArrayList<Pair<Point2D, Point2D>> tour;
    int id;
    Pair<Point2D, Point2D> currentPoints;

    public DeliveryCycle(double size, GraphicsContext context, Point2D coords, Image picture) {
        super(size, context, coords, picture);
        this.moving = false;
        this.movement = new Point2D(0, 0);
        this.currentTour = -1;
        this.tour = new ArrayList<>();
        defaultPosition = coords;
        this.id = 0;
        this.currentPoints = null;
    }

    public ArrayList<Pair<Point2D, Point2D>> getTour() {
        return tour;
    }

    public void setTour(ArrayList<Pair<House, House>> edges, boolean isTree) throws VertexNotAchievableException, VertexNotFoundException {
        ArrayList<Pair<House, House>> bfsEdges = new ArrayList<>();
        if(isTree) {
            Queue<Pair<House, House>> s = new LinkedList<>();
            House initial = edges.get(0).getKey();
            HashMap<House, Boolean> h = new HashMap<>();
            for(Pair<House, House> p : edges) {
                h.put(p.getValue(), false);
                if( p.getKey().equals(initial) ) s.add(p);
            }
            h.put(initial, false);
            while(!s.isEmpty()) {
                Pair<House, House> curr = s.remove();
                if(h.get(curr.getValue())) continue;
                h.put(curr.getValue(), true);
                h.put(curr.getKey(), true);
                bfsEdges.add(curr);
                // System.out.println(curr.getKey() + " " + curr.getValue());
                for(Pair<House, House> p : edges) {
                    if( p.getKey().getCoords().equals(curr.getValue().getCoords()) ) s.add(p);
                }
            }
            this.tour = parseToPoints(bfsEdges);
        } else {
            this.tour = parseToPoints(edges);
        }
        currentTour = 0;
        moveThrough(currentTour);
    }

    private ArrayList<Pair<Point2D, Point2D>> parseToPoints(ArrayList<Pair<House, House>> edges) {
        ArrayList<Pair<Point2D, Point2D>> points = new ArrayList<>();
        for(Pair<House, House> p : edges) {
            points.add( new Pair<>( p.getKey().getCoords(), p.getValue().getCoords()) );
        }
        return points;
    }

    public Pair<Point2D, Point2D> move() {
        if(currentTour != -1) {
            setCoords( new Point2D( getCoords().getX() + movement.getX(), getCoords().getY() + movement.getY() ) );
            checkDistance();
            return currentPoints;
        }
        return null;
    }

    public void moveThrough(int currentTour) {
        currentPoints = tour.get(currentTour);
        setCoords( tour.get(currentTour).getKey() );
        setMovement(calculateMovement(tour.get(currentTour).getKey(), tour.get(currentTour).getValue()));
    }

    public void checkDistance() {
        if(calculateDistance(getCoords(), tour.get(currentTour).getValue()) < SPEED) {
            currentTour++;
            if(currentTour >= tour.size()) {
                currentTour = -1;
                setCoords( tour.get(0).getKey() );
                return;
            }
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
        this.currentTour = -1;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
