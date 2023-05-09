package com.example.myhealth.controllers;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.Main;
import com.example.myhealth.entities.HealthRecord;
import com.example.myhealth.entities.User;
import com.example.myhealth.models.HealthRecordModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.example.myhealth.controllers.ProfileController.applyUserSetting;
import static javafx.collections.FXCollections.observableArrayList;

public class HealthRecordController implements Initializable {
    public HealthRecordModel healthRecordModel = new HealthRecordModel();
    @FXML
    private AnchorPane healthRecordAnchorPane;
    @FXML
    private AnchorPane addNewRecordPane;
    @FXML
    private TextField addNewRecordWeightInput;
    @FXML
    private TextField addNewRecordTemperatureInput;
    @FXML
    private TextField addNewRecordLowBloodPressureInput;
    @FXML
    private TextField addNewRecordHighBloodPressureInput;
    @FXML
    private TextArea addNewRecordNoteInput;
    @FXML
    private AnchorPane menuDropdownPane;
    @FXML
    private Label usernameMenuLabel;

    /* Table Views */
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn createdAtColumn;

    @FXML
    private TableColumn weightColumn;

    @FXML
    private TableColumn temperatureColumn;

    @FXML
    private TableColumn lowBloodPressureColumn;
    @FXML
    private TableColumn highBloodPressureColumn;

    @FXML
    private TableColumn viewRecordsColumn;

    @FXML
    private TableColumn deleteRecordsColumn;

    /* Edit Health Records */
    @FXML
    private AnchorPane editHealthRecordPane;
    @FXML
    private Label healthRecordIdLabel;
    @FXML
    private TextField editHealthRecordWeightInput;
    @FXML
    private TextField editHealthRecordTemperatureInput;
    @FXML
    private TextField editHealthRecordLowBloodPressureInput;
    @FXML
    private TextField editHealthRecordHighBloodPressureInput;
    @FXML
    private TextArea editHealthRecordNoteInput;
    /* Delete Health Record */
    @FXML
    private AnchorPane deleteHealthRecordPane;

    /* Error Message */
    @FXML
    private Label addNewRecordErrorMessage;
    @FXML
    private Label editHealthRecordErrorMessage;
    @FXML
    private VBox addNewRecordErrorMessagePane;
    @FXML
    private VBox editHealthRecordErrorMessagePane;
    @FXML
    private AnchorPane exportHealthRecordSuccess;
    @FXML
    private DatePicker addNewRecordDatePicker;
    @FXML
    private DatePicker editHealthRecordDatePicker;
    /* Connection Status */
    @FXML
    private ImageView connectionStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = new DatabaseConnection().connectToDatabase();
        File file;
        String connectionStatusImageSource;
        if (connection != null) {
            applyUserSetting(healthRecordAnchorPane);
            // Initialize menu button data
            usernameMenuLabel.setText(User.userInstance.getUserFullname());
            healthRecordModel.getHealthRecordByUser(User.userInstance);
            addNewRecordDatePicker.setValue(LocalDate.now());
            createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
            weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
            temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
            lowBloodPressureColumn.setCellValueFactory(new PropertyValueFactory<>("lowBloodPressure"));
            highBloodPressureColumn.setCellValueFactory(new PropertyValueFactory<>("highBloodPressure"));
            tableView.setItems(observableArrayList(User.userInstance.getHealthRecords()));
            this.addButtonsToViewTableColumn();
            this.addButtonsToDeleteRecordColumn();
            // Connection status
            connectionStatusImageSource = "assets/onlineStatus.png";
            file = new File(connectionStatusImageSource);
        } else {
            connectionStatusImageSource = "assets/offlineStatus.png";
            file = new File(connectionStatusImageSource);
        }
        Image image = new Image(file.toURI().toString());
//        connectionStatus.setImage(image);
    }

    public void toggleDisplayAddHealthRecord() {
        addNewRecordPane.setVisible(!addNewRecordPane.isVisible());
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

    public void goToHealthRecord() {
        try {
            Main.changeScene("fxml/HealthRecord.fxml");
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

    public void closeEditHealthRecord() {
        editHealthRecordPane.setVisible(false);
    }

    public void closeAddNewRecordPane() {
        addNewRecordPane.setVisible(false);
    }

    public void addNewRecord() {
        try {
            HealthRecord newHealthRecord = new HealthRecord();
            newHealthRecord.setWeight(addNewRecordWeightInput.getText());
            newHealthRecord.setTemperature(addNewRecordTemperatureInput.getText());
            newHealthRecord.setLowBloodPressure(addNewRecordLowBloodPressureInput.getText());
            newHealthRecord.setHighBloodPressure(addNewRecordHighBloodPressureInput.getText());
            newHealthRecord.setNote(addNewRecordNoteInput.getText());
            newHealthRecord.setCreatedAt(String.valueOf(Date.valueOf(addNewRecordDatePicker.getValue())));
            newHealthRecord.validateEmptyRecord();
            User.userInstance.addHealthRecord(newHealthRecord);
            healthRecordModel.addHealthRecord(newHealthRecord);
            tableView.setItems(observableArrayList(User.userInstance.getHealthRecords()));
            addNewRecordWeightInput.clear();
            addNewRecordTemperatureInput.clear();
            addNewRecordLowBloodPressureInput.clear();
            addNewRecordHighBloodPressureInput.clear();
            addNewRecordPane.setVisible(false);
            addNewRecordErrorMessage.setText("");
            addNewRecordErrorMessagePane.setVisible(false);
        } catch (NumberFormatException e) {
            addNewRecordErrorMessagePane.setVisible(true);
            addNewRecordErrorMessage.setText("Invalid input. Please enter the correct data type");
        } catch (Exception e) {
            addNewRecordErrorMessagePane.setVisible(true);
            addNewRecordErrorMessage.setText(e.getMessage());
        }

    }

    public void editHealthRecord() {
        try {
            // Create new health record and set its data using the input fields
            HealthRecord newHealthRecord = new HealthRecord();
            newHealthRecord.setWeight(editHealthRecordWeightInput.getText());
            newHealthRecord.setTemperature(editHealthRecordTemperatureInput.getText());
            newHealthRecord.setLowBloodPressure(editHealthRecordLowBloodPressureInput.getText());
            newHealthRecord.setHighBloodPressure(editHealthRecordHighBloodPressureInput.getText());
            newHealthRecord.setNote(editHealthRecordNoteInput.getText());
            newHealthRecord.setCreatedAt(String.valueOf(Date.valueOf(editHealthRecordDatePicker.getValue())));
            // Validate the health record
            newHealthRecord.validateEmptyRecord();
            // Update the health record with the new health record with the same id
            ArrayList<HealthRecord> updatedHealthRecords = User.userInstance.getHealthRecords().stream().map(healthRecord -> {
                if (healthRecord.getHealthRecordId().equals(healthRecordIdLabel.getText())) {
                    // Set createdAt and healthRecordId for the new record
                    newHealthRecord.setCreatedAt(healthRecord.getCreatedAt());
                    newHealthRecord.setHealthRecordId(healthRecord.getHealthRecordId());
                    healthRecord = newHealthRecord;
                }
                return healthRecord;
            }).collect(Collectors.toCollection(ArrayList::new));
            // Update table data and refresh
            tableView.setItems(observableArrayList(updatedHealthRecords));
            tableView.refresh();
            // Pass the new health record to update in the database
            healthRecordModel.editHealthRecord(updatedHealthRecords.stream().filter(healthRecord -> healthRecord.getHealthRecordId().equals(healthRecordIdLabel.getText())).collect(Collectors.toList()).get(0));
            // Clear all input fields and close the Anchor Pane
            editHealthRecordWeightInput.clear();
            editHealthRecordTemperatureInput.clear();
            editHealthRecordLowBloodPressureInput.clear();
            editHealthRecordHighBloodPressureInput.clear();
            editHealthRecordPane.setVisible(false);
            // Remove errors
            editHealthRecordErrorMessage.setText("");
            editHealthRecordErrorMessagePane.setVisible(false);
        } catch (NumberFormatException e) {
            // Catch not-a-number error
            editHealthRecordErrorMessagePane.setVisible(true);
            editHealthRecordErrorMessage.setText("Invalid input. Please enter the correct data type");
        } catch (Exception e) {
            // Catch other error
            editHealthRecordErrorMessagePane.setVisible(true);
            editHealthRecordErrorMessage.setText(e.getMessage());
        }
    }

    public void exportDataToCSV() {
        // For each of the health records, write its data into the file with the ending of the username
        String username = User.userInstance.getUsername();
        String fileName = "HealthRecords_" + username + ".csv";
        System.out.println("the file name is: " + fileName);
        try {
            File file = new File(fileName);
            new FileWriter(file);
            for (HealthRecord healthRecord : User.userInstance.getHealthRecords()) {
                healthRecord.exportToCSV(username);
            }
            exportHealthRecordSuccess.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeExportHealthRecordSuccess() {
        exportHealthRecordSuccess.setVisible(false);
    }

    public void closeEditRecordPane() {
        editHealthRecordPane.setVisible(false);
    }

    public void deleteHealthRecord() {
        healthRecordModel.deleteHealthRecord(healthRecordIdLabel.getText());
        // Remove the health records
        ArrayList<HealthRecord> updatedHealthRecords = User.userInstance.getHealthRecords().stream().filter(healthRecord -> !healthRecord.getHealthRecordId().equals(healthRecordIdLabel.getText())).collect(Collectors.toCollection(ArrayList::new));
        User.userInstance.setHealthRecords(updatedHealthRecords);
        // Update table view with new data from
        tableView.setItems(observableArrayList(User.userInstance.getHealthRecords()));
        deleteHealthRecordPane.setVisible(false);
    }

    public void closeDeleteHealthRecordPane() {
        deleteHealthRecordPane.setVisible(false);
    }

    private void addButtonsToViewTableColumn() {
        Callback<TableColumn<HealthRecord, Void>, TableCell<HealthRecord, Void>> cellFactory = new Callback<TableColumn<HealthRecord, Void>, TableCell<HealthRecord, Void>>() {
            @Override
            public TableCell<HealthRecord, Void> call(final TableColumn<HealthRecord, Void> param) {
                final TableCell<HealthRecord, Void> cell = new TableCell<HealthRecord, Void>() {
                    private final Button viewRecordsButton = new Button("View Records");

                    {
                        viewRecordsButton.setOnAction((ActionEvent event) -> {
                            // Get health record data from the table
                            HealthRecord healthRecord = getTableView().getItems().get(getIndex());
                            // Update the data from the record in the input fields
                            healthRecordIdLabel.setText(healthRecord.getHealthRecordId());
                            editHealthRecordPane.setVisible(true);
                            editHealthRecordNoteInput.setText(healthRecord.getNote());
                            editHealthRecordLowBloodPressureInput.setText(String.valueOf(healthRecord.getLowBloodPressure()));
                            editHealthRecordHighBloodPressureInput.setText(String.valueOf(healthRecord.getHighBloodPressure()));
                            editHealthRecordWeightInput.setText(String.valueOf(healthRecord.getWeight()));
                            editHealthRecordTemperatureInput.setText(String.valueOf(healthRecord.getTemperature()));
                            editHealthRecordDatePicker.setValue(LocalDate.parse(healthRecord.getCreatedAt()));
                            editHealthRecordPane.setVisible(true);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            viewRecordsButton.setStyle("-fx-background-color: rgb(132, 204, 22); -fx-text-fill: white; -fx-padding: 1; -fx-cursor: hand;");
                            setGraphic(viewRecordsButton);
                        }
                    }
                };
                return cell;
            }
        };
        viewRecordsColumn.setCellFactory(cellFactory);
    }

    private void addButtonsToDeleteRecordColumn() {
        Callback<TableColumn<HealthRecord, Void>, TableCell<HealthRecord, Void>> cellFactory = new Callback<TableColumn<HealthRecord, Void>, TableCell<HealthRecord, Void>>() {
            @Override
            public TableCell<HealthRecord, Void> call(final TableColumn<HealthRecord, Void> param) {
                final TableCell<HealthRecord, Void> cell = new TableCell<HealthRecord, Void>() {
                    private final Button deleteRecordsButton = new Button("");

                    {
                        deleteRecordsButton.setOnAction((ActionEvent event) -> {
                            // Get health record data from the table
                            HealthRecord healthRecord = getTableView().getItems().get(getIndex());
                            // Update the data from the record in the input fields
                            healthRecordIdLabel.setText(healthRecord.getHealthRecordId());
                            deleteHealthRecordPane.setVisible(true);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            try {
                                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/removeHealthRecord.png")));
                                deleteIcon.setFitWidth(20);
                                deleteIcon.setFitHeight(20);
                                deleteRecordsButton.setGraphic(deleteIcon);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            deleteRecordsButton.setStyle("-fx-padding: 1; -fx-cursor: hand; -fx-background-color: transparent;");
                            setGraphic(deleteRecordsButton);
                        }
                    }
                };
                return cell;
            }
        };
        deleteRecordsColumn.setCellFactory(cellFactory);
    }
}
