package com.example.furniture_factory;

import com.example.furniture_factory.controllers.MainViewController;
import com.example.furniture_factory.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApplication extends Application {
    public static LoginController loginController;
    private static Stage mainStage;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/login-view.fxml"));
        loader.setControllerFactory(i -> loginController);
        Parent content = loader.load();
        stage.setMaximized(true);

        Scene scene = new Scene(content);
        stage.setTitle("Вход в систему");
        stage.setScene(scene);
        stage.show();
        mainStage = stage;
    }

    public static void showMainApplicationAfterLogin() {
        changeStage(createMainController(), "/main-view.fxml");
    }

    public static void showLoginPageAfterLogout() {
        LoginController.user = null;
        changeStage(loginController, "/login-view.fxml");
    }

    private static void changeStage(Object controller, String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JavaFXApplication.class.getResource(fxmlFilePath));
            loader.setControllerFactory(i -> controller);
            Parent content = loader.load();
            mainStage.close();

            Scene scene = new Scene(content);
            mainStage.setTitle("Мебельная фабрика");
            mainStage.setScene(scene);
            mainStage.setMaximized(true);
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MainViewController createMainController() {
        return new MainViewController();
    }

    public static void init(String[] args) {
        launch();
    }
}
