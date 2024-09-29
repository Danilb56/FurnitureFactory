package com.example.furniture_factory.controllers;

import com.example.furniture_factory.enums.MainViewWindowEnum;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainViewController {
    public AnchorPane anchorPane;
    private MainViewWindowEnum currentWindow = null;

    public static final Map<MainViewWindowEnum, Controller<?>> controllerMap = new HashMap<>();

    public MainViewController() {
    }

    @FXML
    public void initialize() {
        switchToFurnitureList();
    }

    @FXML
    public void switchToFurnitureList() {
        if (MainViewWindowEnum.FURNITURE_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.FURNITURE_LIST;
        setContent();
    }

    @FXML
    public void switchToFurnitureLineList() {
        if (MainViewWindowEnum.FURNITURE_LINE_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.FURNITURE_LINE_LIST;
        setContent();
    }

    @FXML
    public void switchToComponentList() {
        if (MainViewWindowEnum.COMPONENT_LIST.equals(currentWindow)) {
            return;
        }
        this.currentWindow = MainViewWindowEnum.COMPONENT_LIST;
        setContent();
    }

    @FXML
    public void switchToAccountPage() {
        if (MainViewWindowEnum.ACCOUNT_PAGE.equals(currentWindow)) {
            return;
        }
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
