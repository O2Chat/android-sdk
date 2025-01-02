package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.BusinessHourModel;

public class BusinessHoursEvent {

    public BusinessHourModel businessHourModel;

    public BusinessHoursEvent(BusinessHourModel isValidHour) {
        this.businessHourModel = isValidHour;
    }
}
