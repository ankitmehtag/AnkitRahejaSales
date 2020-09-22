package com.ChatServerModel;

/**
 * Created by Mohit on 6/22/2018.
 */

public class SendMessage {
    private String message;

    public SendMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
