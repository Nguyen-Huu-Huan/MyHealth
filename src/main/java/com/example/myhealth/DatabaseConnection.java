package com.example.myhealth;

import java.sql.*;

public class DatabaseConnection {
    public Connection connectToDatabase() {
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:MyHealth.db");
            this.createTables(connection);
            return connection;
        } catch (Exception e) {
            System.out.println("Cannot connect to database");
        }
        return null;
    }
    public void createTables(Connection connection) {
        String createUserTableQuery = "" +
                "CREATE TABLE IF NOT EXISTS User " +
                "   (userId            VARCHAR(50) PRIMARY KEY UNIQUE NOT NULL," +
                "   firstName          VARCHAR(50) UNIQUE NOT NULL,"+
                "   lastName           VARCHAR(50) UNIQUE NOT NULL,"+
                "   username           VARCHAR(50) UNIQUE NOT NULL," +
                "   applicationVersion VARCHAR(200),"+
                "   hashPassword       VARCHAR(1000) NOT NULL);";
        String createHealthRecordTableQuery = "" +
                "CREATE TABLE IF NOT EXISTS HealthRecord " +
                "   (userId       VARCHAR(50) NOT NULL," +
                "   healthRecordId VARCHAR(50) PRIMARY KEY UNIQUE NOT NULL," +
                "   weight        DOUBLE(50, 2),"+
                "   temperature   DOUBLE(50, 2),"+
                "   lowerBloodPressure INTEGER(1000)," +
                "   higherBloodPressure INTEGER(1000)," +
                "   note          VARCHAR(9999)," +
                "   createdAt     DATETIME," +
                "FOREIGN KEY (userId) REFERENCES User(userId));";
        String createUserSettingTableQuery = "" +
                "CREATE TABLE IF NOT EXISTS UserSetting " +
                "   (userId     VARCHAR(50) PRIMARY KEY NOT NULL," +
                "   fontFamily  VARCHAR(50)," +
                "   fontWeight  VARCHAR(50),"+
                "   fontPosture VARCHAR(50)," +
                "   fontColor   VARCHAR(50),"+
                "   fontSize    INTEGER(20)," +
                "FOREIGN KEY (userId) REFERENCES User(userId));";
        PreparedStatement preparedStatement = null;
        try {
            // Prepare and create the User table
            preparedStatement = connection.prepareStatement(createUserTableQuery);
            // Prepare and create the HealthRecord table
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(createHealthRecordTableQuery);
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement(createUserSettingTableQuery);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}