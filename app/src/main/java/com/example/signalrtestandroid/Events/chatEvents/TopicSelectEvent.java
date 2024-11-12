package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.GetTopicsByConversationModel;

public class TopicSelectEvent {
    public GetTopicsByConversationModel getTopicsByChannelIDModel;
    public String eventType;

    public TopicSelectEvent(GetTopicsByConversationModel getTopicsByChannelIDModel, String eventType) {
        this.getTopicsByChannelIDModel = getTopicsByChannelIDModel;
        this.eventType = eventType;
    }
}
