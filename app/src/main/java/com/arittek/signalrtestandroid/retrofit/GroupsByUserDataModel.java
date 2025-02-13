package com.arittek.signalrtestandroid.retrofit;

import com.arittek.signalrtestandroid.model.chat.GroupsArrayModel;
import com.arittek.signalrtestandroid.model.chat.UserArrayModel;

import java.util.ArrayList;

public class GroupsByUserDataModel {

    private ArrayList<GroupsArrayModel> groupsList = new ArrayList<>();
    private ArrayList<UserArrayModel> usersList = new ArrayList<>();

    public ArrayList<GroupsArrayModel> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(ArrayList<GroupsArrayModel> groupsList) {
        this.groupsList = groupsList;
    }

    public ArrayList<UserArrayModel> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<UserArrayModel> usersList) {
        this.usersList = usersList;
    }
}
