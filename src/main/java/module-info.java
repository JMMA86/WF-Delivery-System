module com.wf.wfdeliverysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wf.wfdeliverysystem to javafx.fxml;
    exports com.wf.wfdeliverysystem;
    exports com.wf.wfdeliverysystem.exceptions;
    exports com.wf.wfdeliverysystem.controller;
    exports com.wf.wfdeliverysystem.implementations;
    exports com.wf.wfdeliverysystem.model;
    opens com.wf.wfdeliverysystem.controller to javafx.fxml;
}