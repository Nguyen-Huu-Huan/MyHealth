package com.example.myhealth.entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class User {
    private ArrayList<HealthRecord> healthRecords;
    private UserSetting userSetting;
    private String userId;
    private String firstName;
    private String lastName;

    private String username;
    private String password;
    private HashMap<String, String> applicationVersion;

    public static User userInstance;

    public User(String firstName, String lastName, String username, String password) {
        this.userId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = hashPassword(password);
        this.userInstance = this;
        this.healthRecords = new ArrayList<HealthRecord>();
        this.userSetting = new UserSetting();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = hashPassword(password);
        this.userInstance = this;
        this.healthRecords = new ArrayList<HealthRecord>();
        this.userSetting = new UserSetting();
    }

    public User() {
        this.userInstance = this;
        this.healthRecords = new ArrayList<HealthRecord>();
        this.userSetting = new UserSetting();
    }

    @Override
    public String toString() {
        return "User{" +
                "healthRecord=" + healthRecords +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String hashPassword(String password) {
        try {
            // Hash password with SHA-256 algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] hashByte = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashByte) {
                stringBuilder.append(Integer.toHexString(0xFF & b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException error) {
            error.printStackTrace();
        }
        return null;
    }

    public String getUserFullname() {
        return this.firstName.substring(0, 1).toUpperCase() + this.firstName.substring(1) + ", " + this.lastName.substring(0, 1).toUpperCase() + this.lastName.substring(1);
    }

    public ArrayList<HealthRecord> getHealthRecords() {
        return healthRecords;
    }

    public void setHealthRecords(ArrayList<HealthRecord> healthRecords) {
        this.healthRecords = healthRecords;
    }

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void setUserInstance(User userInstance) {
        User.userInstance = userInstance;
    }

    public void addHealthRecord(HealthRecord newHealthRecord) {
        this.healthRecords.add(newHealthRecord);
    }

    public void removeHealthRecord(HealthRecord recordToBeRemoved) {
        this.healthRecords.remove(recordToBeRemoved);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public UserSetting getUserSetting() {
        return this.userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

    public HashMap<String, String> getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(HashMap<String, String> applicationVersion) {
        this.applicationVersion = applicationVersion;
    }
}
