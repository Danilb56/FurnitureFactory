package com.example.furniture_factory;

import com.example.furniture_factory.controllers.HelloController;
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
                    "jdbc:mysql://localhost:3306",
                    "root", "Mysql_password");

            // Service code
            FurnitureService furnitureService = new FurnitureService(connection);

            HelloApplication.helloController = new HelloController(furnitureService);

            HelloApplication.init(args);

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
