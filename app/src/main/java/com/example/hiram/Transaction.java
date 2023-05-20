package com.example.hiram;

import java.util.ArrayList;

public class Transaction {
    private static String dateTime, qr, room;
    private static ArrayList<String> items;

    public void setDateTime(String dateTime) {
        this.dateTime= dateTime;
    }
    public void setQr(String qr) {
        this.qr= qr;
    }
    public void setRoom(String room) {
        this.room= room;
    }
    public void setItems(ArrayList<String> items) {
        this.items= items;
    }

    public String getDateTime() {
        return dateTime;
    }
    public String getQr() {
        return qr;
    }
    public String getRoom() {
        return room;
    }
    public ArrayList<String> getItems() {
        return items;
    }
    public void clearTransaction() {
        dateTime= null;
        qr= null;
        room= null;
        items= new ArrayList<String>();
    }
    public void clearData() {
        dateTime= null;
        room= null;
        items= new ArrayList<String>();
    }
}
