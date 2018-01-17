package com.awais2075gmail.awais2075.model;

/**
 * Created by Muhammad Awais Rashi on 24-Nov-17.
 */

public class Contact{
    private String contactId;
    private String contactName;
    private String contactNumber;
    private String groupId;
    private boolean isSelected;

    public Contact() {
    }

    public Contact(String contactName) {
        this.contactName = contactName;
    }
    
    public Contact(String contactId, String contactName, String contactNumber, String groupId) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.groupId = groupId;
    }

    public Contact(String contactName, String contactNumber, boolean isSelected) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.isSelected = isSelected;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
