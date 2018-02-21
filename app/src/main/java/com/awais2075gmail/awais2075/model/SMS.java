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
    private boolean isSpam;

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

    public SMS(long smsId, long smsThreadId, String smsNumber, String smsAddress, String smsBody, String smsReadState, String smsDate, String smsType, boolean isSpam) {
        this.smsId = smsId;
        this.smsThreadId = smsThreadId;
        this.smsNumber = smsNumber;
        this.smsAddress = smsAddress;
        this.smsBody = smsBody;
        this.smsReadState = smsReadState;
        this.smsDate = smsDate;
        this.smsType = smsType;
        this.isSpam = isSpam;
    }

    public long getSmsId() {
        return smsId;
    }

    public long getSmsThreadId() {
        return smsThreadId;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public String getSmsAddress() {
        return smsAddress;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public String getSmsReadState() {
        return smsReadState;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public String getSmsType() {
        return smsType;
    }

    public boolean isSpam() {
        return isSpam;
    }
}
