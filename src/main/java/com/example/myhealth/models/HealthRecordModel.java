package com.example.myhealth.models;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.entities.HealthRecord;
import com.example.myhealth.entities.User;

import java.sql.*;
import java.util.ArrayList;

public class HealthRecordModel {
    Connection connection;

    public HealthRecordModel() {
        connection = new DatabaseConnection().connectToDatabase();
    }
    public boolean editHealthRecord(HealthRecord healthRecord) {
        if (this.connection == null) return false;
        PreparedStatement preparedStatement = null;
        String query = "" +
                "UPDATE HealthRecord " +
                "SET weight = ?, temperature = ?, lowBloodPressure = ?, highBloodPressure = ?, note = ?" +
                "WHERE healthRecordId = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(healthRecord.getWeight()));
            preparedStatement.setString(2, String.valueOf(healthRecord.getTemperature()));
            preparedStatement.setString(3, String.valueOf(healthRecord.getLowBloodPressure()));
            preparedStatement.setString(4, String.valueOf(healthRecord.getHighBloodPressure()));
            preparedStatement.setString(5, healthRecord.getNote());
            preparedStatement.setString(6, healthRecord.getHealthRecordId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean addHealthRecord(HealthRecord healthRecord) throws Exception {
        if (this.connection == null) return false;
        PreparedStatement preparedStatement = null;
        String query = "" +
                "INSERT INTO HealthRecord(weight, temperature, lowBloodPressure, highBloodPressure, note, userId, createdAt, healthRecordId)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(healthRecord.getWeight()));
            preparedStatement.setString(2, String.valueOf(healthRecord.getTemperature()));
            preparedStatement.setString(3, String.valueOf(healthRecord.getLowBloodPressure()));
            preparedStatement.setString(4, String.valueOf(healthRecord.getHighBloodPressure()));
            preparedStatement.setString(5, healthRecord.getNote());
            preparedStatement.setString(6, User.userInstance.getUserId());
            preparedStatement.setString(7, healthRecord.getCreatedAt());
            preparedStatement.setString(8, healthRecord.getHealthRecordId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new Exception("Error adding health record to the server. Please try again");
        }
    }
    public boolean getHealthRecordByUser(User user) {
        if (this.connection == null) return false;
        PreparedStatement preparedStatement;
        String query = "" +
                "SELECT weight, temperature, lowBloodPressure, highBloodPressure, note, createdAt, healthRecordId " +
                "FROM HealthRecord " +
                "WHERE userId = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUserId());
            User.userInstance.setHealthRecords(new ArrayList<HealthRecord>());
            ResultSet healthRecords = preparedStatement.executeQuery();
            while (healthRecords.next()) {
                HealthRecord healthRecord = new HealthRecord();
                healthRecord.setWeight(healthRecords.getString(1));
                healthRecord.setTemperature(healthRecords.getString(2));
                healthRecord.setLowBloodPressure(healthRecords.getString(3));
                healthRecord.setHighBloodPressure(healthRecords.getString(4));
                healthRecord.setNote(healthRecords.getString(5));
                healthRecord.setCreatedAt(healthRecords.getString(6));
                healthRecord.setHealthRecordId(healthRecords.getString(7));
                User.userInstance.addHealthRecord(healthRecord);
            }
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean deleteHealthRecord(String healthRecordId) {
        if (this.connection == null) return false;
        PreparedStatement preparedStatement = null;
        String query = "" +
                "DELETE FROM HealthRecord " +
                "WHERE healthRecordId = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, healthRecordId);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
