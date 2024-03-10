package com.moutamid.signalcopy.model;

import android.net.Uri;

public class UserModel {
    String id, name, number;
    Uri image;

    public UserModel(String id, String name, String number, Uri image) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
