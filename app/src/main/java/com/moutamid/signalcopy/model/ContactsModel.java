package com.moutamid.signalcopy.model;

public class ContactsModel {
    public String id, iconName, name, image, lastMessage, number;
    public String time;
    public int textColor, bgColor;

    public ContactsModel(String id, String iconName, String name, String image, String lastMessage, String number, String time, int textColor, int bgColor) {
        this.id = id;
        this.iconName = iconName;
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.number = number;
        this.time = time;
        this.textColor = textColor;
        this.bgColor = bgColor;
    }
}
