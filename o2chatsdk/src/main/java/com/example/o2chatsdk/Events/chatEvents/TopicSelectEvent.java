package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.GetTopicsByConversationModel;

public class TopicSelectEvent {
    public GetTopicsByConversationModel getTopicsByChannelIDModel;
    public String eventType;

    public TopicSelectEvent(GetTopicsByConversationModel getTopicsByChannelIDModel, String eventType) {
        this.getTopicsByChannelIDModel = getTopicsByChannelIDModel;
        this.eventType = eventType;
    }
}
