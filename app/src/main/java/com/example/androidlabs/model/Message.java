package com.example.androidlabs.model;

public class Message {
    private Long id;
    private String text;
    private boolean isSend;

    public Message(String text, boolean isSend, Long id) {
        setText(text);
        setSend(isSend);
        setId(id);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
