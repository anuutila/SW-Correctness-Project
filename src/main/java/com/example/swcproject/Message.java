package com.example.swcproject;

public class Message {

    private final String message;
    private final MESSAGE_TYPE type;

    public enum MESSAGE_TYPE {
        INFO,
        WARNING,
        ERROR
    }

    public Message(String message, MESSAGE_TYPE type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public MESSAGE_TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return  type + ": " + message;
    }
}
