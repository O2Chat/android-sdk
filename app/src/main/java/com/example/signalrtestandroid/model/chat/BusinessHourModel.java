package com.example.signalrtestandroid.model.chat;

import com.google.gson.annotations.Expose;

public class BusinessHourModel {

    public String getMessage() {
        return message;
    }

    public boolean isOnline() {
        return isOnline;
    }

    @Expose
    public String message;

    @Expose
    public boolean isOnline;


}
