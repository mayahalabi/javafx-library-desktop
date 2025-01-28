module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.Classes;
    opens com.example.project.Classes to javafx.fxml;
    exports com.example.project.Services;
    opens com.example.project.Services to javafx.fxml;
    exports com.example.project.HomePages;
    opens com.example.project.HomePages to javafx.fxml;
    exports com.example.project.Settings;
    opens com.example.project.Settings to javafx.fxml;
    exports com.example.project.Connection;
    opens com.example.project.Connection to javafx.fxml;
}