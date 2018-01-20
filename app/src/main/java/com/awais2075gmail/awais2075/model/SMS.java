package com.awais2075gmail.awais2075.model;

public class SMS {
    private long smsId;
    private long smsThreadId;
    private String smsNumber;
    private String smsAddress;
    private String smsBody;
    private String smsReadState;
    private String smsDate;
    private String smsType;

    public SMS() {

    }



    public SMS(long smsId, long smsThreadId, String smsNumber, String smsAddress, String smsBody, String smsReadState, String smsDate, String smsType) {
        this.smsId = smsId;
        this.smsThreadId = smsThreadId;
        this.smsNumber = smsNumber;
        this.smsAddress = smsAddress;
        this.smsBody = smsBody;
        this.smsReadState = smsReadState;
        this.smsDate = smsDate;
        this.smsType = smsType;
    }

    public long getSmsId() {
        return smsId;
    }

    public void setSmsId(long smsId) {
        this.smsId = smsId;
    }

    public long getSmsThreadId() {
        return smsThreadId;
    }

    public void setSmsThreadId(long smsThreadId) {
        this.smsThreadId = smsThreadId;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public String getSmsReadState() {
        return smsReadState;
    }

    public void setSmsReadState(String smsReadState) {
        this.smsReadState = smsReadState;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }
}
