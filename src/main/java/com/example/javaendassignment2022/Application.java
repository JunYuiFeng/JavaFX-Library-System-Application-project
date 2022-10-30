package com.example.javaendassignment2022;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 360, 220);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Library system");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            closeAppAlert(stage);
        });
    }

    public void closeAppAlert(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Closing application");
        alert.setHeaderText("Are you sure you want to close the application?");
        alert.setContentText("Your data is going to be saved when you close the application");

        if (alert.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}