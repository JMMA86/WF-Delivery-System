package com.wf.wfdeliverysystem;

import com.wf.wfdeliverysystem.controller.InitController;
import com.wf.wfdeliverysystem.model.Manager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class Launcher extends Application {
    public static final Manager manager = new Manager(90);

    public static Manager getManager() {
        return manager;
    }

    public static void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Pair<FXMLLoader, Stage> handlers = renderView("init-view.fxml", 1280, 720);
        InitController controller = handlers.getKey().getController();
        stage.setOnCloseRequest(windowEvent -> {
            controller.setRunning(false);
        });
        controller.setStage(handlers.getValue());
    }

    public static Pair<FXMLLoader, Stage> renderView(String fxml, double width, double height) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxml));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
            stage.setTitle("WF Delivery System");
            // stage.setFullScreen(true);
            stage.setScene(scene);
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Pair<>(fxmlLoader, stage);
    }

    public static void main(String[] args) {
        launch();
    }

}