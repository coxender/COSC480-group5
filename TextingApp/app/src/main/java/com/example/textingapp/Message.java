package com.example.textingapp;

public class Message {
    /*

     */
    public String msg;
    public String usrSent;
    public String usrReceive;
    public int method;

    public Message() {

    }
    public Message(String msg, String usrSent, String usrReceive, int method) {
        this.msg = msg;
        this.usrSent = usrSent;
        this.usrReceive = usrReceive;
        this.method = method;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", usrSent='" + usrSent + '\'' +
                ", usrReceive='" + usrReceive + '\'' +
                ", method=" + method +
                '}';
    }
}
