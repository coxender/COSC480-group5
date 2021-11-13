package com.example.textingapp;

public class Message {
    public String msg;
    public String usr;

    public Message() {

    }
    public Message(String msg, String usr) {
        this.msg = msg;
        this.usr = usr;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", usr='" + usr + '\'' +
                '}';
    }
}
