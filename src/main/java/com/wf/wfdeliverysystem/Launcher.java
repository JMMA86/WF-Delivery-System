package com.wf.wfdeliverysystem;

import com.wf.wfdeliverysystem.controller.InitController;
import com.wf.wfdeliverysystem.model.Manager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    private static final Manager manager = new Manager();

    public static Manager getManager() {
        return manager;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("init-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("WF Delivery System");
        // stage.setFullScreen(true);
        stage.setScene(scene);
        InitController controller = fxmlLoader.getController();
        stage.setOnCloseRequest(windowEvent -> {
            controller.setRunning(false);
        });
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}