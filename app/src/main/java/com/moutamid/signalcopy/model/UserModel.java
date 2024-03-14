package com.moutamid.signalcopy.model;

import android.net.Uri;

public class UserModel {
    public String iconName, name, number;
    public String image;
    public int textColor, bgColor;

    public UserModel(String iconName, String name, String number, String image, int textColor, int bgColor) {
        this.iconName = iconName;
        this.name = name;
        this.number = number;
        this.image = image;
        this.textColor = textColor;
        this.bgColor = bgColor;
    }
}
