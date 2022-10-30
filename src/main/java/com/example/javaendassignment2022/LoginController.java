package com.example.javaendassignment2022;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Scene scene;
    private Stage stage;

    private Parent root;
    private Database db;
    private User loggedinUser;
    @FXML
    private TextField userNameTextfield;
    @FXML
    private PasswordField userPasswordField;
    @FXML
    private Label labelInvalidUserNameAndPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelInvalidUserNameAndPassword.setText("");
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        db = new Database();
        if (login(db)) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainWindow-view.fxml"));
            root = fxmlLoader.load();

            MainWindowController mainWindowController = fxmlLoader.getController();
            mainWindowController.displayWelcomeAndName(loggedinUser.getUserName());

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        else {
            labelInvalidUserNameAndPassword.setText("invalid username or password");
        }
    }

    private boolean login(Database db) {
        for (User user: db.getUsers()) {
            if (userNameTextfield.getText().equals(user.getUserName()) && userPasswordField.getText().equals(user.getPassword())) {
                loggedinUser = user;
                return true;
            }
        }
        return false;
    }
}
