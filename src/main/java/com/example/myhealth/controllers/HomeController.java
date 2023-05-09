package com.example.myhealth.controllers;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.Main;
import com.example.myhealth.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.myhealth.models.UserModel;
import javafx.scene.layout.AnchorPane;

import static com.example.myhealth.controllers.ProfileController.applyUserSetting;

public class HomeController implements Initializable {
    public UserModel loginModel = new UserModel();

    @FXML
    private AnchorPane homeAnchorPane;
    @FXML
    private Label loginButton;
    @FXML
    private Label registerButton;
    @FXML
    private ImageView connectionStatus;
    @FXML
    private AnchorPane registerLoginPane;
    @FXML
    private AnchorPane displayMenuButton;
    @FXML
    private AnchorPane menuDropdownPane;
    @FXML
    private Button homeMenuButton;
    @FXML
    private Button profileMenuButton;
    @FXML
    private Button healthRecordMenuButton;
    @FXML
    private Button logoutMenuButton;
    @FXML
    private Label usernameMenuLabel;

    // Check database connection
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // If the user already logged in, display the dropdown menu (with navigation to other pages)
        // Apply settings for the font style
        if (User.userInstance != null) {
            registerLoginPane.setVisible(false);
            displayMenuButton.setVisible(true);
            usernameMenuLabel.setText(User.userInstance.getUserFullname());
            applyUserSetting(homeAnchorPane);
        }
        Connection connection = new DatabaseConnection().connectToDatabase();
        File file;
        String connectionStatusImageSource;
        if (connection != null) {
            connectionStatusImageSource = "assets/onlineStatus.png";
            file = new File(connectionStatusImageSource);
        } else {
            connectionStatusImageSource = "assets/offlineStatus.png";
            file = new File(connectionStatusImageSource);
        }
        Image image = new Image(file.toURI().toString());
//        connectionStatus.setImage(image);
    }

    public void goToLogin() throws Exception {
        try {
            Main.changeScene("fxml/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToRegister() throws Exception {
        try {
            Main.changeScene("fxml/Register.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleDisplayMenuOptions() throws Exception {
        menuDropdownPane.setVisible(!menuDropdownPane.isVisible());
    }

    public void goToHome() throws Exception {
        try {
            Main.changeScene("fxml/Home.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToProfile() throws Exception {
        try {
            Main.changeScene("fxml/Profile.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() throws Exception {
        // Delete current login credentials and navigate user to the login page
        User.setUserInstance(null);
        try {
            Main.changeScene("fxml/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToHealthRecord() {
        try {
            Main.changeScene("fxml/HealthRecord.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}