package com.example.myhealth.entities;

import javafx.scene.paint.Color;

public class UserSetting {
    private String fontFamily;
    private String fontWeight;
    private String fontPosture;
    private String fontColor;
    private int fontSize;

    public UserSetting() {
    }

    public UserSetting(String fontFamily, String fontWeight, String fontPosture, String fontColor, int fontSize) {
        this.fontFamily = fontFamily;
        this.fontWeight = fontWeight;
        this.fontPosture = fontPosture;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
    }
    public Color convertHexToRGB() {
        if (this.fontColor != null)
            return Color.rgb(
                Integer.valueOf(this.fontColor.substring(1, 3), 16),
                Integer.valueOf(this.fontColor.substring(3, 5), 16),
                Integer.valueOf(this.fontColor.substring(5, 7), 16));
        return null;
    }
    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontPosture() {
        return fontPosture;
    }

    public void setFontPosture(String fontPosture) {
        this.fontPosture = fontPosture;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String toString() {
        return "UserSetting{" +
                "fontFamily='" + fontFamily + '\'' +
                ", fontWeight='" + fontWeight + '\'' +
                ", fontPosture='" + fontPosture + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", fontSize=" + fontSize +
                '}';
    }
}
