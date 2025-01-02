package com.arittek.o2chatsdk.Events.chatEvents;

import com.arittek.o2chatsdk.model.chat.BusinessHourModel;

public class BusinessHoursEvent {

    public BusinessHourModel businessHourModel;

    public BusinessHoursEvent(BusinessHourModel isValidHour) {
        this.businessHourModel = isValidHour;
    }
}
