package com.wf.wfdeliverysystem;

import com.wf.wfdeliverysystem.controller.InitController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = renderView("init-view.fxml", 1280, 720);
        InitController controller = loader.getController();
        controller.initialize();
    }

    public static FXMLLoader renderView(String fxml, int width, int height) {
        FXMLLoader fxmlLoader;
        try {
            fxmlLoader = new FXMLLoader(Launcher.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }
}