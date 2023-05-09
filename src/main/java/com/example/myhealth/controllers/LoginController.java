package com.example.myhealth.controllers;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.Main;
import com.example.myhealth.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.example.myhealth.models.UserModel;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public UserModel loginModel = new UserModel();
    @FXML
    private ImageView connectionStatus;
    @FXML
    private Label loginStatus;
    @FXML
    private Label registerButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;


    // Check database connection
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = new DatabaseConnection().connectToDatabase();
        File file;
        String connectionStatusImageSource;
        if (connection != null) {
            connectionStatusImageSource = "assets/onlineStatus.png";
//            file = new File(connectionStatusImageSource);
        } else {
            connectionStatusImageSource = "assets/offlineStatus.png";
//            file = new File(connectionStatusImageSource);
        }
//        Image image = new Image(file.toURI().toString());
//        connectionStatus.setImage(image);
    }

    public void goToRegister() throws Exception {
        try {
            Main.changeScene("fxml/Register.fxml");
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

    public void login() throws Exception {
        try {
            // Validate if the inputs are blank
            if (usernameInput.getText().isBlank() || passwordInput.getText().isBlank()) {
                throw new Exception("Input must not be empty or contains at least a character");
            }
            // Pass data to user object
            User inputUser = new User(usernameInput.getText(), passwordInput.getText());
            boolean loginData = loginModel.loginAccount(inputUser);
            // If the request to database fails, set error message for the login status.
            // Otherwise, navigate the user to the profile page
            if (loginData) {
                try {
                    Main.changeScene("fxml/Profile.fxml");
                } catch (Exception e) {
                }
            } else {
                loginStatus.setText("Username or Password is incorrect. Please try again");
            }
        } catch (Exception error) {
            loginStatus.setText(error.getMessage());
        }
    }
}