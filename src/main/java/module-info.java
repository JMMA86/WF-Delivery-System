module com.wf.wfdeliverysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wf.wfdeliverysystem to javafx.fxml;
    exports com.wf.wfdeliverysystem;
    exports com.wf.wfdeliverysystem.controller;
    opens com.wf.wfdeliverysystem.controller to javafx.fxml;
}