package com.example.furniture_factory.controllers;

import com.example.furniture_factory.JavaFXApplication;
import com.example.furniture_factory.models.User;
import com.example.furniture_factory.services.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static com.example.furniture_factory.controllers.LoginController.user;

public class UserController extends Controller<User>{
    @FXML
    public TextField accountLoginField;
    @FXML
    public TextField accountPasswordField;

    public UserController(Service<User> service) {
        super(service);
    }

    @Override
    public void initialize() {
        accountLoginField.setText(user.getLogin());
        accountPasswordField.setText(user.getPassword());
    }

    public void updateUser() {
        try {
            User updatedUser = new User(
                    user.getId(),
                    accountLoginField.getText(),
                    accountPasswordField.getText(),
                    user.getRole()
            );
            service.update(updatedUser);
            user = updatedUser;
        } catch (Exception e) {
            e.printStackTrace();
            // Показать окно с ошибкой
        }
    }

    public void logout() {
        JavaFXApplication.showLoginPageAfterLogout();
    }
}
