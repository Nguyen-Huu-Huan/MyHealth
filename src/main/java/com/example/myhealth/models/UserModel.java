package com.example.myhealth.models;

import com.example.myhealth.DatabaseConnection;
import com.example.myhealth.entities.User;
import com.example.myhealth.entities.UserSetting;

import java.sql.*;
import java.util.HashMap;

public class UserModel {
    Connection connection;

    public UserModel() {
        this.connection = new DatabaseConnection().connectToDatabase();
    }

    // Register database account
    public HashMap<Boolean, String> registerUser(User user) {
        HashMap<Boolean, String> queryStatus = new HashMap<Boolean, String>();
        String query =
                "INSERT INTO User(userId, firstName, lastName, username, hashPassword) " +
                        "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            // Set the user id to the random id
            preparedStatement.setString(1, user.getUserId());
            // Set the user first name
            preparedStatement.setString(2, user.getFirstName());
            // Set the user last name
            preparedStatement.setString(3, user.getLastName());
            // Set the user username
            preparedStatement.setString(4, user.getUsername());
            // Set the user password
            preparedStatement.setString(5, user.getPassword());

            // Execute, close the statement and return true
            preparedStatement.execute();
            preparedStatement.close();
            queryStatus.put(true, "");
            return queryStatus;
        } catch (SQLException e) {
            queryStatus.put(false, "The username already exists in the database. Please try again!");
            return queryStatus;
        } catch (Exception e) {
            queryStatus.put(false, "Error connecting to the database. Please try later!");
            return queryStatus;
        }
    }

    public boolean loginAccount(User user) {
        String query =
                "SELECT userId, firstName, lastName, username " +
                        "FROM User " +
                        "WHERE username = ? AND hashPassword = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            // Set the user username
            preparedStatement.setString(1, user.getUsername());
            // Set the user password
            preparedStatement.setString(2, user.getPassword());
            // Execute and close the query
            ResultSet userRecord = preparedStatement.executeQuery();
            while (userRecord.next()) {
                User.userInstance.setUserId(userRecord.getString(1));
                User.userInstance.setFirstName(userRecord.getString(2));
                User.userInstance.setLastName(userRecord.getString(3));
                User.userInstance.setUsername(userRecord.getString(3));
                preparedStatement.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUserProfile(User newUser) throws SQLException {
        String query =
                "UPDATE User " +
                        "SET firstName = ?, lastName = ? " +
                        "WHERE userId = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, newUser.getFirstName());
            preparedStatement.setString(2, newUser.getLastName());
            preparedStatement.setString(3, newUser.getUserId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createUserSetting(User user) throws SQLException {
        String query =
                "INSERT INTO UserSetting(userId, fontFamily, fontWeight, fontPosture, fontColor, fontSize) " +
                        "VALUES (?, ?, ?, ?, ?, ?) ";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getUserSetting().getFontFamily());
            preparedStatement.setString(3, user.getUserSetting().getFontWeight());
            preparedStatement.setString(4, user.getUserSetting().getFontPosture());
            preparedStatement.setString(5, user.getUserSetting().getFontColor());
            preparedStatement.setString(6, String.valueOf(user.getUserSetting().getFontSize()));
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getUserSetting(User user) {
        String query =
                "SELECT userId, fontFamily, fontWeight, fontPosture, fontColor, fontSize FROM UserSetting " +
                "WHERE userId = ? ";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUserId());
            ResultSet userSettingResult = preparedStatement.executeQuery();
            while (userSettingResult.next()) {
                UserSetting userSetting = new UserSetting();
                userSetting.setFontFamily(userSettingResult.getString(2));
                userSetting.setFontWeight(userSettingResult.getString(3));
                userSetting.setFontPosture(userSettingResult.getString(4));
                userSetting.setFontColor(userSettingResult.getString(5));
                userSetting.setFontSize(Integer.parseInt(userSettingResult.getString(6)));
                User.userInstance.setUserSetting(userSetting);
            }
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    public boolean updateUserSetting(User user) {
        String query =
                "UPDATE UserSetting " +
                "SET fontFamily= ?, fontWeight= ?, fontPosture= ?, fontColor= ?, fontSize= ? "+
                "WHERE userId = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUserSetting().getFontFamily());
            preparedStatement.setString(2, user.getUserSetting().getFontWeight());
            preparedStatement.setString(3, user.getUserSetting().getFontPosture());
            preparedStatement.setString(4, user.getUserSetting().getFontColor());
            preparedStatement.setString(5, String.valueOf(user.getUserSetting().getFontSize()));
            preparedStatement.setString(6, user.getUserId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean updateApplicationVersion() {
        String query =
                "UPDATE User " +
                "SET applicationVersion= ? "+
                "WHERE userId = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(User.userInstance.getApplicationVersion()));
            preparedStatement.setString(2, User.userInstance.getUserId());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}