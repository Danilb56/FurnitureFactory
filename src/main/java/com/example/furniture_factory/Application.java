package com.example.furniture_factory;

import com.example.furniture_factory.controllers.ComponentController;
import com.example.furniture_factory.controllers.FurnitureController;
import com.example.furniture_factory.controllers.FurnitureLineController;
import com.example.furniture_factory.controllers.MainViewController;
import com.example.furniture_factory.enums.MainViewWindowEnum;
import com.example.furniture_factory.services.ComponentService;
import com.example.furniture_factory.services.FurnitureLineService;
import com.example.furniture_factory.services.FurnitureService;

import java.sql.Connection;
import java.sql.DriverManager;

public class Application {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
//                    "jdbc:mysql://std-mysql.ist.mospolytech.ru:3306/std_1620_staff",
//                    "std_1620_staff", "qwerty");
                    "jdbc:mysql://localhost:3306/furniture_factory",
                    "root", "Mysql_password");

            // Service code
            ComponentService componentService = new ComponentService(connection);

            FurnitureService furnitureService = new FurnitureService(connection, componentService);

            FurnitureLineService furnitureLineService = new FurnitureLineService(connection);

            MainViewController.controllerMap.put(
                    MainViewWindowEnum.FURNITURE_LIST, new FurnitureController(furnitureService, furnitureLineService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.FURNITURE_LINE_LIST, new FurnitureLineController(furnitureLineService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.COMPONENT_LIST, new ComponentController(componentService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.ACCOUNT_PAGE, null);

            JavaFXApplication.init(args);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
