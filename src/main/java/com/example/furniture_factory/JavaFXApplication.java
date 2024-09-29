package com.example.furniture_factory;

import com.example.furniture_factory.controllers.FurnitureController;
import com.example.furniture_factory.controllers.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApplication extends Application {
    public static FurnitureController furnitureController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main-view.fxml"));
        loader.setControllerFactory(i -> this.createMainController());
        Parent content = loader.load();
        stage.setMaximized(true);

        Scene scene = new Scene(content);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private MainViewController createMainController() {
        return new MainViewController();
    }

    public static void init(String[] args) {
        launch();
    }
}
