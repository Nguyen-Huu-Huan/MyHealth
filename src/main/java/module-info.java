module com.example.myhealth {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.myhealth;

    opens com.example.myhealth.controllers to javafx.fxml, com.example.myhealth.fxml;
    exports com.example.myhealth.controllers;
    opens com.example.myhealth.entities to javafx.base;

    opens com.example.myhealth.fxml to javafx.fxml;
}