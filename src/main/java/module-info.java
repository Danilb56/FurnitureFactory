module com.example.projectjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    requires java.sql;
    requires mysql.connector.j;

    opens com.example.furniture_factory to javafx.fxml;
    exports com.example.furniture_factory;
    exports com.example.furniture_factory.controllers;
    exports com.example.furniture_factory.services;
    opens com.example.furniture_factory.controllers to javafx.fxml;
}
