package com.example.myhealth.controllers;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.Main;
import com.example.myhealth.entities.User;
import com.example.myhealth.entities.UserSetting;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.converter.FontConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import com.example.myhealth.models.UserModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProfileController implements Initializable {
    public UserModel userModel = new UserModel();

    @FXML
    private AnchorPane profileAnchorPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label usernameMenuLabel;

    /* Edit profile dialogue components */
    @FXML
    private AnchorPane editProfilePane;
    @FXML
    private ImageView editProfileIconButton;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private Button editProfileConfirmButton;
    @FXML
    private Button editProfileCancelButton;
    @FXML
    private VBox errorMessageVBox;
    @FXML
    private Label errorMessageLabel;

    /* Menu dropdown components */
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
    private Image test;
    /* User Setting */
    @FXML
    private AnchorPane userSettingPane;
    @FXML
    private ComboBox userSettingFontFamily;
    @FXML
    private ComboBox userSettingFontPosture;
    @FXML
    private Slider userSettingFontSize;
    @FXML
    private ColorPicker userSettingFontColor;
    @FXML
    private Button userSettingConfirmButton;
    @FXML
    private Button userSettingCancelButton;
    @FXML
    private ComboBox userSettingFontWeight;
    @FXML
    private Label previewUserSetting;
    /* Application Version */
    @FXML
    private Label javaFxVersion;
    @FXML
    private Label javaFxRuntimeVersion;

    /* Connection status image */
    @FXML
    private ImageView connectionStatus;

    // Check database connection
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = new DatabaseConnection().connectToDatabase();
        String connectionStatusImageSource;
        if (connection != null) {
            connectionStatusImageSource = "assets/offlineStatus.png";
            userModel.getUserSetting(User.userInstance);
            usernameLabel.setText(User.userInstance.getUserFullname());
            // Get user's application version upon successful login attempts and update it in the database
            HashMap<String, String> newApplicationVersion = new HashMap<String, String>();
            newApplicationVersion.put("JavaFx Version", System.getProperty("javafx.version"));
            newApplicationVersion.put("JavaFx Runtime Version", System.getProperty("javafx.runtime.version"));
            User.userInstance.setApplicationVersion(newApplicationVersion);
            userModel.updateApplicationVersion();
            // Update user's full name for the profile page
            usernameMenuLabel.setText(User.userInstance.getUserFullname());
            // Initialize user settings and apply
            initializeUserSetting();
            applyUserSetting(profileAnchorPane);
        } else {
            connectionStatusImageSource = "assets/offlineStatus.png";
        }
//        Image image = new Image(getClass().getResourceAsStream("../assets/offlineStatus.png"));
//        System.out.println("the image is: " +image);
//        connectionStatus.setImage(image);
    }

    public void goToHome() throws Exception {
        try {
            Main.changeScene("fxml/Home.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleDisplayMenuOptions() throws Exception {
        menuDropdownPane.setVisible(!menuDropdownPane.isVisible());
    }

    public void closeMenuOptions() throws Exception {
        menuDropdownPane.setVisible(false);
    }

    public void toggleDisplayEditUser() {
        editProfilePane.setVisible(!editProfilePane.isVisible());
        if (editProfilePane.isVisible()) {
            firstNameInput.setText(User.userInstance.getFirstName());
            lastNameInput.setText(User.userInstance.getLastName());
        }
    }

    public void closeEditUser() {
        editProfilePane.setVisible(false);
    }

    public void editUserProfile() {
        try {
            // Validate if the inputs are blank
            if (firstNameInput.getText().isBlank() || lastNameInput.getText().isBlank()) {
                throw new Exception("Input must not be empty or contains at least a character");
            }
            // Set data for the user object
            User.userInstance.setFirstName(firstNameInput.getText());
            User.userInstance.setLastName(lastNameInput.getText());
            // Throw an error if the database cannot update
            if (!userModel.updateUserProfile(User.userInstance)) throw new Exception("The firstname or lastname has been taken");
            // Update changes for the labels and close the edit profile anchor pane
            usernameLabel.setText(User.userInstance.getUserFullname());
            usernameMenuLabel.setText(User.userInstance.getUserFullname());
            editProfilePane.setVisible(false);
        } catch (Exception e) {
            errorMessageVBox.setVisible(true);
            errorMessageLabel.setText(e.getMessage());
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

    public void goToProfile() throws Exception {
        try {
            Main.changeScene("fxml/Profile.fxml");
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

    public void closeUserSetting() {
        menuDropdownPane.setVisible(false);
        userSettingPane.setVisible(false);
    }

    public void openUserSetting() {
        menuDropdownPane.setVisible(false);
        userSettingPane.setVisible(true);
    }

    public void saveUserSetting() {
        menuDropdownPane.setVisible(false);
        try {
            // Update user settings locally and make changes in the database
            User.userInstance.getUserSetting().setFontFamily((String) userSettingFontFamily.getValue());
            User.userInstance.getUserSetting().setFontWeight(userSettingFontWeight.getValue().toString());
            User.userInstance.getUserSetting().setFontColor("#"+userSettingFontColor.getValue().toString().substring(2, 8));
            User.userInstance.getUserSetting().setFontPosture(userSettingFontPosture.getValue().toString());
            User.userInstance.getUserSetting().setFontSize((int) userSettingFontSize.getValue());
            // If the record exists, update it
            if (userModel.updateUserSetting(User.userInstance)) {
            }else {
                // New record, add new user setting
                userModel.createUserSetting(User.userInstance);
            }
            applyUserSetting(profileAnchorPane);
            userSettingPane.setVisible(false);
        } catch (Exception e) {
            userSettingPane.setVisible(false);
        }
    }

    public void initializeUserSetting() {
        // Pop up font weight data for the combo box and set default data as the user's setting
        userSettingFontWeight.getItems().addAll(FontWeight.values());
        userSettingFontWeight.setValue(User.userInstance.getUserSetting().getFontWeight());
        // Pop up font posture data for the combo box and set default data as the user's setting
        userSettingFontPosture.getItems().addAll(FontPosture.values());
        userSettingFontPosture.setValue(User.userInstance.getUserSetting().getFontPosture());
        // Set default font size as the user's setting
        if (User.userInstance.getUserSetting().getFontSize() == 0) {
            userSettingFontSize.setValue(13);
        } else {
            userSettingFontSize.setValue(User.userInstance.getUserSetting().getFontSize());
        }
        // Set default font color as the user's setting
        if (User.userInstance.getUserSetting().convertHexToRGB() != null)
            userSettingFontColor.setValue(User.userInstance.getUserSetting().convertHexToRGB());
        // Round the value of the font size slider to integer
        userSettingFontSize.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {
                userSettingFontSize.setValue((int) userSettingFontSize.getValue());
                UserSetting userSetting = User.userInstance.getUserSetting();
                userSetting.setFontSize((int) userSettingFontSize.getValue());
                previewSetting(userSetting);
            }
        });
        // previewUserTesting is used to store the user's settings (before saving) for preview text
        UserSetting previewUserSetting = User.userInstance.getUserSetting();
//      // Add listeners for each font style input fields to update the preview text
        userSettingFontFamily.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            previewUserSetting.setFontFamily((String) newValue);
            previewSetting(previewUserSetting);
        });
        userSettingFontWeight.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            previewUserSetting.setFontWeight(String.valueOf(newValue));
            previewSetting(previewUserSetting);
        });
        userSettingFontPosture.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            previewUserSetting.setFontPosture(String.valueOf(newValue));
            previewSetting(previewUserSetting);
        });
        userSettingFontColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                previewUserSetting.setFontColor("#"+userSettingFontColor.getValue().toString().substring(2, 8));
                previewSetting(previewUserSetting);
            }
        });
        // Pop up font family data for the combo box and set default data as the user's setting
        userSettingFontFamily.getItems().addAll(Font.getFamilies());
        userSettingFontFamily.setValue(User.userInstance.getUserSetting().getFontFamily());
    }

    public static void applyUserSetting(AnchorPane anchorPane) {
        // This function is used universally for all controllers to apply user font style setting
        if (
            User.userInstance.getUserSetting().getFontFamily() != null &&
            FontWeight.valueOf(User.userInstance.getUserSetting().getFontWeight()) != null &&
            FontPosture.valueOf(User.userInstance.getUserSetting().getFontPosture()) != null &&
            Double.parseDouble(Integer.toString(User.userInstance.getUserSetting().getFontSize())) > 0) {
            // Look up for all Label text and update each with the new font style
            for (Node label : anchorPane.lookupAll("Label")) {
                if (label instanceof Label) {
                    ((Label) label).setFont(Font.font(
                            User.userInstance.getUserSetting().getFontFamily(),
                            FontWeight.valueOf(User.userInstance.getUserSetting().getFontWeight()),
                            FontPosture.valueOf(User.userInstance.getUserSetting().getFontPosture()),
                            Double.parseDouble(Integer.toString(User.userInstance.getUserSetting().getFontSize()))
                    ));
                    ((Label) label).setTextFill(User.userInstance.getUserSetting().convertHexToRGB());
                }
            }
        }
    }
    public void previewSetting(UserSetting userSetting) {
        // Update preview text with new font style
        try {
            previewUserSetting.setFont(Font.font(
                    userSetting.getFontFamily(),
                    FontWeight.valueOf(userSetting.getFontWeight()),
                    FontPosture.valueOf(userSetting.getFontPosture()),
                    Double.parseDouble(Integer.toString(userSetting.getFontSize()))
            ));
            // Update preview text with new color
            previewUserSetting.setTextFill(userSetting.convertHexToRGB());
        }catch (Exception e){}
    }
    public void toggleDisplayVersion() {
        // Toggle user's application version
        javaFxVersion.setText("JavaFx: " + User.userInstance.getApplicationVersion().get("JavaFx Version"));
        javaFxRuntimeVersion.setText("JavaFx Runtime: " + User.userInstance.getApplicationVersion().get("JavaFx Runtime Version"));
        javaFxRuntimeVersion.setVisible(!javaFxRuntimeVersion.isVisible());
        javaFxVersion.setVisible(!javaFxVersion.isVisible());
    }
}