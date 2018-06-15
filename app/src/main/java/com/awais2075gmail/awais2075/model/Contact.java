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



    public Contact(String contactId, String contactName, String groupId) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.groupId = groupId;
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

    public Contact(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
