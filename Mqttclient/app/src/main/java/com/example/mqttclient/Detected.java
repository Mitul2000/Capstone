package com.example.mqttclient;

public class Detected {
    public String name;
    public String timestamp;
    public String image;

    public Detected() {

    }
    public Detected(String Name, String timestamp, String image){
        this.name = Name;
        this.timestamp = timestamp;
        this.image = image;
    }


}

