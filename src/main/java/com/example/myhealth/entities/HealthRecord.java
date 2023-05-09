package com.example.myhealth.entities;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

public class HealthRecord {
    private String healthRecordId;
    private SimpleDoubleProperty weight;
    private SimpleDoubleProperty temperature;
    private SimpleIntegerProperty lowBloodPressure;
    private SimpleIntegerProperty highBloodPressure;
    private SimpleStringProperty note;
    private SimpleStringProperty createdAt;

    public HealthRecord() {
        this.healthRecordId = UUID.randomUUID().toString();
        this.createdAt = new SimpleStringProperty(new Date(System.currentTimeMillis()).toString());
    }


    private FileWriter fileWriter;

    public void exportToCSV(String username) {
        String fileName = "HealthRecords_" + username + ".csv";
        BufferedReader bufferedReader;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                // If the file does not exist, create a new file
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            // The file exists, read the file and append the header data if there is no existing data
            FileReader fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            if (bufferedReader.readLine() == null) {
                StringBuffer test = new StringBuffer();
                test.append("healthRecordId,weight,temperature,lowBloodPressure,highBloodPressure,note,createdAt");
                bufferedWriter.write(test.toString());
                bufferedWriter.newLine();
            }
            StringBuffer oneLine = new StringBuffer();
            oneLine.append(this.getHealthRecordId() + ",");
            oneLine.append(this.getWeight() + ",");
            oneLine.append(this.getTemperature() + ",");
            oneLine.append(this.getLowBloodPressure() + ",");
            oneLine.append(this.getHighBloodPressure() + ",");
            oneLine.append(this.getNote() + ",");
            oneLine.append(this.getCreatedAt());
            bufferedWriter.write(oneLine.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (FileNotFoundException e) {
            System.out.println("second error");
        } catch (IOException e) {
            System.out.println("third error");
        }
    }


    public String getWeight() {
        try {
            return String.valueOf(weight.get());
        } catch (Exception e) {
            return "";
        }
    }

    public void setWeight(String weight) {
        if (!weight.isBlank())
            this.weight = new SimpleDoubleProperty(Double.parseDouble(weight));
    }

    public void validateEmptyRecord() throws Exception {
        // If the blood pressure is not null and the high blood pressure is null, throw an error
        if (this.lowBloodPressure != null && this.highBloodPressure == null)
            throw new Exception("If you specify the blood pressure, make sure to fill in both blood pressure fields");
        if (this.lowBloodPressure == null && this.highBloodPressure != null)
            throw new Exception("If you specify the blood pressure, make sure to fill in both blood pressure fields");
        if (this.note.get().isBlank() && this.temperature == null && this.weight == null) {
            // If low blood pressure (LBP) is null, high blood pressure which is validated based on low BP will be null when user specifies the blood pressure, throw error
            if (this.lowBloodPressure == null)
                throw new Exception("At least one input must be filled in.");
        }
    }

    public String getTemperature() {
        try {
            return String.valueOf(this.temperature.get());
        } catch (Exception e) {
            return "";
        }
    }

    public void setTemperature(String temperature) {
        if (!temperature.isBlank())
            this.temperature = new SimpleDoubleProperty(Double.parseDouble(temperature));
    }

    public String getLowBloodPressure() {
        try {
            return String.valueOf(this.lowBloodPressure.get());
        } catch (Exception e) {
            return "";
        }

    }

    public String getHighBloodPressure() {
        try {
            return String.valueOf(this.highBloodPressure.get());
        } catch (Exception e) {
            return "";
        }
    }

    public void setLowBloodPressure(String lowBloodPressure) {
        if (!lowBloodPressure.isBlank())
            this.lowBloodPressure = new SimpleIntegerProperty(Integer.parseInt(lowBloodPressure));
    }

    public void setHighBloodPressure(String highBloodPressure) throws Exception {
        if (!highBloodPressure.isBlank() && this.lowBloodPressure != null)
            if (Integer.parseInt(highBloodPressure) <= this.lowBloodPressure.get())
                throw new Exception("The Low Blood Pressure must be lower than the High Blood Pressure");
            else this.highBloodPressure = new SimpleIntegerProperty(Integer.parseInt(highBloodPressure));
    }

    public String getNote() {
        try {
            return String.valueOf(this.note.get());
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String toString() {
        return "HealthRecord{" +
                "healthRecordId='" + healthRecordId + '\'' +
                ", weight=" + weight +
                ", temperature=" + temperature +
                ", lowBloodPressure=" + lowBloodPressure +
                ", highBloodPressure=" + highBloodPressure +
                ", note=" + note +
                ", createdAt=" + createdAt +
                '}';
    }

    public String getHealthRecordId() {
        return healthRecordId;
    }

    public void setNote(String note) throws Exception {
        if (note.trim().split("\\s+").length > 50)
            throw new Exception("Health Record Note must not exceed 50 words");
        this.note = new SimpleStringProperty(note);
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public void setHealthRecordId(String healthRecordId) {
        this.healthRecordId = healthRecordId;
    }
}
