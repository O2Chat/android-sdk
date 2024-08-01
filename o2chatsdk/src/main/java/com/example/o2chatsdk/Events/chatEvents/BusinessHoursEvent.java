package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.BusinessHourModel;

public class BusinessHoursEvent {

    public BusinessHourModel businessHourModel;

    public BusinessHoursEvent(BusinessHourModel isValidHour) {
        this.businessHourModel = isValidHour;
    }
}
