package com.wf.wfdeliverysystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class GraphRepresentationController {

    @FXML
    private TextArea listGraphTA;
    @FXML
    private TextArea matrixGraphTA;

    public void initialize(String list, String matrix) {
        listGraphTA.setText(list);
        matrixGraphTA.setText(matrix);
    }

}
