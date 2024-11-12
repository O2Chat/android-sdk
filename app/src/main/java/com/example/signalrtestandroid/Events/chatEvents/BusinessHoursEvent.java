package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.BusinessHourModel;

public class BusinessHoursEvent {

    public BusinessHourModel businessHourModel;

    public BusinessHoursEvent(BusinessHourModel isValidHour) {
        this.businessHourModel = isValidHour;
    }
}
