package com.awais2075gmail.awais2075.model;

public class AutoResponse {
    private String autoResponseName;
    private String autoResponseNumber;
    private String autoResponseText;

    public AutoResponse(String autoResponseName, String autoResponseNumber, String autoResponseText) {
        this.autoResponseName = autoResponseName;
        this.autoResponseNumber = autoResponseNumber;
        this.autoResponseText = autoResponseText;
    }

    public String getAutoResponseName() {
        return autoResponseName;
    }

    public String getAutoResponseNumber() {
        return autoResponseNumber;
    }

    public String getAutoResponseText() {
        return autoResponseText;
    }
}
