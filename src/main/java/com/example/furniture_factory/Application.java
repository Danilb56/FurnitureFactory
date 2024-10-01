package com.example.furniture_factory;

import com.example.furniture_factory.controllers.*;
import com.example.furniture_factory.enums.MainViewWindowEnum;
import com.example.furniture_factory.services.*;

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

            // Service creation
            ComponentService componentService = new ComponentService(connection);

            FurnitureService furnitureService = new FurnitureService(connection, componentService);

            FurnitureLineService furnitureLineService = new FurnitureLineService(connection);

            UserService userService = new UserService(connection);

            ShopService shopService = new ShopService(connection);

            // Controller creation
            JavaFXApplication.loginController = new LoginController(userService);

            MainViewController.controllerMap.put(
                    MainViewWindowEnum.FURNITURE_LIST, new FurnitureController(furnitureService, furnitureLineService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.FURNITURE_LINE_LIST, new FurnitureLineController(furnitureLineService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.COMPONENT_LIST, new ComponentController(componentService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.ACCOUNT_PAGE, new UserController(userService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.SHOP_LIST, new ShopController(shopService)
            );
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.ORDER_LIST, null
            );

            JavaFXApplication.init(args);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
