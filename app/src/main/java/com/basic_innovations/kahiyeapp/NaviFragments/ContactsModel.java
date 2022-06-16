package com.basic_innovations.kahiyeapp.NaviFragments;

public class ContactsModel {
    String contactName;
    String contactStatus;
    String contactEmail;
    String contactID;
    //String contactImg;
    int contactImg;

    public ContactsModel() {
    }

    public ContactsModel(String contactName, String contactStatus,String contactEmail, String contactID, int contactImg) {
        this.contactName = contactName;
        this.contactStatus = contactStatus;
        this.contactEmail = contactEmail;
        this.contactID = contactID;
        this.contactImg = contactImg;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public int getContactImg() {
        return contactImg;
    }

    public void setContactImg(int contactImg) {
        this.contactImg = contactImg;
    }
}
