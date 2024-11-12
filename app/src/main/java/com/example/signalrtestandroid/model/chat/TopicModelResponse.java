package com.example.signalrtestandroid.model.chat;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TopicModelResponse {
    @Expose
    public boolean status;
    @Expose
    public ArrayList<GetTopicsByConversationModel> topicList;
}
