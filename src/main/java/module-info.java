module com.example.projectjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    requires java.sql;
    requires mysql.connector.j;

    opens com.example.furniture_factory.controllers to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens com.example.furniture_factory.enums to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens com.example.furniture_factory.exceptions to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens com.example.furniture_factory.models to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens com.example.furniture_factory.services to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens com.example.furniture_factory.utils to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    opens com.example.furniture_factory to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;

    exports com.example.furniture_factory.controllers to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports com.example.furniture_factory.enums to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports com.example.furniture_factory.exceptions to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports com.example.furniture_factory.models to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports com.example.furniture_factory.services to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports com.example.furniture_factory.utils to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports com.example.furniture_factory to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
}
