module com.example.javaendassignment2022 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javaendassignment2022 to javafx.fxml;
    exports com.example.javaendassignment2022;
}