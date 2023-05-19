package com.wf.wfdeliverysystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InitController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void initialize() {

    }
}