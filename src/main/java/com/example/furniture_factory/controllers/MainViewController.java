package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.MainViewWindowEnum;
import com.example.furniture_factory.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainViewController {
    public AnchorPane anchorPane;
    public Button orderButton;
    public Label userLabel;
    public Label listLabel;
    private MainViewWindowEnum currentWindow = null;

    public static final Map<MainViewWindowEnum, Controller<?>> controllerMap = new HashMap<>();

    public MainViewController() {
    }

    @FXML
    public void initialize() {
        switchToFurnitureList();
        if (LoginController.usersShopId != null) {
            this.orderButton.setText("Мои заказы");
        }
        User user = LoginController.user;
        userLabel.setText("Пользователь: " + user.getLogin() +
                " (" + user.getRole().getLocalization() + ")");
    }

    @FXML
    public void switchToFurnitureList() {
        if (MainViewWindowEnum.FURNITURE_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.FURNITURE_LIST;
        this.listLabel.setText(this.currentWindow.getLabel());
        setContent();
    }

    @FXML
    public void switchToFurnitureLineList() {
        if (MainViewWindowEnum.FURNITURE_LINE_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.FURNITURE_LINE_LIST;
        this.listLabel.setText(this.currentWindow.getLabel());
        setContent();
    }

    @FXML
    public void switchToComponentList() {
        if (MainViewWindowEnum.COMPONENT_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.COMPONENT_LIST;
        this.listLabel.setText(this.currentWindow.getLabel());
        setContent();
    }

    @FXML
    public void switchToShopList() {
        if (MainViewWindowEnum.SHOP_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.SHOP_LIST;
        this.listLabel.setText(this.currentWindow.getLabel());
        setContent();
    }

    @FXML
    public void switchToOrderList() {
        if (MainViewWindowEnum.ORDER_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.ORDER_LIST;
        this.listLabel.setText(this.currentWindow.getLabel());
        setContent();
    }

    @FXML
    public void switchToAccountPage() {
        if (MainViewWindowEnum.ACCOUNT_PAGE.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.ACCOUNT_PAGE;
        this.listLabel.setText(this.currentWindow.getLabel());
        setContent();
    }

    private void setContent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("/" + currentWindow.getFxmlFileName() + ".fxml"));
            loader.setControllerFactory(i -> controllerMap.get(currentWindow));
            Parent content = loader.load();

            anchorPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
