package com.example.furniture_factory.controllers;

import com.example.furniture_factory.JavaFXApplication;
import com.example.furniture_factory.enums.Role;
import com.example.furniture_factory.models.User;
import com.example.furniture_factory.services.Service;
import com.example.furniture_factory.services.ShopService;
import com.example.furniture_factory.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class LoginController extends Controller<User> {
    public static User user = null;
    public static Long usersShopId = null;
    private final ShopService shopService;

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField loginField;
    @FXML
    public Label incorrectPasswordLabel;

    public LoginController(Service<User> service,
                           ShopService shopService) {
        super(service);
        this.shopService = shopService;
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
            LoginController.user = user;
            LoginController.usersShopId = shopService.getShopIdByOwnerId(user.getId())
                            .orElse(null);
            System.out.println("Выполнен вход в систему: " + login);
            JavaFXApplication.showMainApplicationAfterLogin();
        } else if (user == null) {
            ButtonType buttonType = showDialog();
            if (buttonType.equals(ButtonType.OK)) {
                User newUser = new User(
                        login,
                        password,
                        Role.DEFAULT
                );
                LoginController.user = service.create(newUser);
                LoginController.usersShopId =
                        shopService.getShopIdByOwnerId(LoginController.user.getId())
                        .orElse(null);
                System.out.println("Выполнен вход в систему: " + login);
                JavaFXApplication.showMainApplicationAfterLogin();
            } else {
                incorrectPasswordLabel.setVisible(true);
            }
        } else {
            incorrectPasswordLabel.setVisible(true);
        }
    }

    private ButtonType showDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Пользователь не найден");
        alert.setContentText("Хотите зарегистрировать нового пользователя?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(ButtonType.CANCEL);
    }
}
