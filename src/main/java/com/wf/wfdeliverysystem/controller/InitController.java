package com.wf.wfdeliverysystem.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class InitController {

    // FXML buttons
    @FXML
    private Canvas canvas;
    @FXML
    private Button checkDeliveryBtn;
    @FXML
    private Button generateTourBtn;
    @FXML
    private Button viewGraphsBtn;
    @FXML
    private RadioButton hq1TB;
    @FXML
    private RadioButton h12TB;
    @FXML
    private RadioButton hq3TB;
    @FXML
    private RadioButton matrixTB;
    @FXML
    private RadioButton listTB;
    @FXML
    private ToggleGroup hqTG;
    @FXML
    private ToggleGroup typeTG;

    public boolean running;

    // Init, refresh and close

    public void initialize() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        canvas.setHeight(bounds.getHeight() * 0.7);
        canvas.setWidth(bounds.getWidth() - 48);
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        this.running = true;
        new Thread( () -> {
            while(running) {
                Platform.runLater( () -> {
                    paint();
                } );
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } ).start();
    }
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private void paint() {

    }

    // Requirement Buttons
    public void onVertexSelected(MouseEvent mouseEvent) {
    }
    public void onCheckDelivery(ActionEvent actionEvent) {
    }

    public void onGenerateTour(ActionEvent actionEvent) {
    }

    public void onViewGraphs(ActionEvent actionEvent) {
    }
}