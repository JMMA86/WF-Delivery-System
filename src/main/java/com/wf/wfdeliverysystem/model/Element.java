package com.wf.wfdeliverysystem.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Element {

    private int state;
    private Color background;
    private GraphicsContext context;

    public Element(GraphicsContext context) {
        setState(0);
        this.context = context;
    }

    public int getState() {
        return state;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public GraphicsContext getContext() {
        return context;
    }

    public void setContext(GraphicsContext context) {
        this.context = context;
    }

    public abstract void setState(int state);
}
