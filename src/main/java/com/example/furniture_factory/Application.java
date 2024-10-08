package com.example.furniture_factory;

import com.example.furniture_factory.controllers.*;
import com.example.furniture_factory.enums.MainViewWindowEnum;
import com.example.furniture_factory.services.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
//                    "jdbc:mysql://std-mysql.ist.mospolytech.ru:3306/std_1620_staff",
//                    "std_1620_staff", "qwerty");
                    "jdbc:mysql://localhost:3306/furniture_factory",
                    "root", "Mysql_password");

            Statement statement = connection.createStatement();

            String filePath = Application.class.getResource("/db/init.sql")
                    .getFile().replaceAll("%20", " ");
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            StringBuilder query = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                query.append(line).append("\n");
                if (line.trim().endsWith(";")) {
                    System.out.println("Executed query: \n" + query.toString().trim());
                    statement.execute(query.toString().trim());
                    query = new StringBuilder();
                }
            }

            System.out.println("init.sql executed");


            // Service creation
            ComponentService componentService = new ComponentService(connection);

            FurnitureService furnitureService = new FurnitureService(connection, componentService);

            FurnitureLineService furnitureLineService = new FurnitureLineService(connection);

            UserService userService = new UserService(connection);

            ShopService shopService = new ShopService(connection);

            OrderService orderService = new OrderService(connection, furnitureService);

            // Controller creation
            JavaFXApplication.loginController = new LoginController(userService, shopService);

            MainViewController.controllerMap.put(
                    MainViewWindowEnum.FURNITURE_LIST, new FurnitureController(furnitureService, furnitureLineService, componentService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.FURNITURE_LINE_LIST, new FurnitureLineController(furnitureLineService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.COMPONENT_LIST, new ComponentController(componentService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.ACCOUNT_PAGE, new UserController(userService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.SHOP_LIST, new ShopController(shopService));
            MainViewController.controllerMap.put(
                    MainViewWindowEnum.ORDER_LIST, new OrderController(orderService, furnitureService));

            JavaFXApplication.init(args);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
