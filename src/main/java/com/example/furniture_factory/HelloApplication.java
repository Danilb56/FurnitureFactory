package com.example.furniture_factory;

import com.example.furniture_factory.controllers.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static HelloController helloController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/furniture-list-view.fxml"));
        loader.setControllerFactory(i -> helloController);
        Parent content = loader.load();

        Scene scene = new Scene(content);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void init(String[] args) {
        launch();
    }
}
