package com.moutamid.signalcopy.model;

import android.net.Uri;

public class UserModel {
    public String name, number;
    public String image;

    public UserModel(String name, String number, String image) {
        this.name = name;
        this.number = number;
        this.image = image;
    }
}
