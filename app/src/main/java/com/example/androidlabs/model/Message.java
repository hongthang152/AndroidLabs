package com.example.androidlabs.model;

public class Message {
    private String text;
    private boolean isSend;

    public Message(String text, boolean isSend) {
        setText(text);
        setSend(isSend);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
