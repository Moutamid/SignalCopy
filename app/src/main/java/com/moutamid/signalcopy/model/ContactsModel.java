package com.moutamid.signalcopy.model;

public class ContactsModel {
    public String id, name, image, lastMessage, number;
    public String time;

    public ContactsModel(String id, String name, String image, String lastMessage, String number, String time) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.number = number;
        this.time = time;
    }
}
