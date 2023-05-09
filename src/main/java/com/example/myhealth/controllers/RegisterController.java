package com.example.myhealth.controllers;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.Main;
import com.example.myhealth.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.example.myhealth.models.UserModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public UserModel userModel = new UserModel();
    @FXML
    private Label isConnected;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginButton;

    @FXML
    private VBox errorMessageVBox;
    @FXML
    private Label errorMessageLabel;


    // Check database connection
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = new DatabaseConnection().connectToDatabase();
        if (connection != null) {
//            Connected
        } else {
//            Not Connected
        }

    }

    public void register() {
        try {
            // Validate if at least 1 of the input fields is blank
            if (firstNameInput.getText().isBlank() || lastNameInput.getText().isBlank() || usernameInput.getText().isBlank() || passwordInput.getText().isBlank()) {
                throw new Exception("Input must not be empty or contains at least a character");
            }
            // Pass data to a user object and get status from the database request
            User inputUser = new User(firstNameInput.getText(), lastNameInput.getText(), usernameInput.getText(), passwordInput.getText());
            HashMap<Boolean, String> registerStatus = userModel.registerUser(inputUser);
            // If the status is false (get(true) == null), throw an exception with a message
            if (registerStatus.get(true) == null) throw new Exception(registerStatus.get(false));
            // Navigate to Profile
            errorMessageVBox.setVisible(false);
            try {
                Main.changeScene("fxml/Profile.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception error) {
            errorMessageVBox.setVisible(true);
            errorMessageLabel.setText(error.getMessage());
        }
    }

    public void goToLogin() throws Exception {
        try {
            Main.changeScene("fxml/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToHome() throws Exception {
        try {
            Main.changeScene("fxml/Home.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}