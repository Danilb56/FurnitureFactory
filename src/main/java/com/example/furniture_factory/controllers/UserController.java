package com.example.furniture_factory.controllers;

import com.example.furniture_factory.models.User;
import com.example.furniture_factory.services.Service;
import com.example.furniture_factory.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserController extends Controller<User> {
    public static User user = null;

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField loginField;
    @FXML
    public Label incorrectPasswordLabel;

    public UserController(Service<User> service) {
        super(service);
    }

    @Override
    public void initialize() {
        incorrectPasswordLabel.setVisible(false);
    }

    public void tryLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();
        User user = ((UserService) service).findByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            UserController.user = user;
            //Вход в систему
        } else {
            incorrectPasswordLabel.setVisible(true);
        }
    }


}
