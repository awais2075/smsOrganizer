package com.awais2075gmail.awais2075.model;

/**
 * Created by Muhammad Awais Rashi on 25-Jan-18.
 */

public class Group {
    private String groupId;
    private String groupName;
    private String userId;

    public Group() {
    }

    public Group(String groupId, String groupName, String userId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
