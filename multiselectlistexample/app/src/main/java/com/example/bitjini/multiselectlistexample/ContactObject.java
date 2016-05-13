package com.example.bitjini.multiselectlistexample;

/**
 * Created by bitjini on 29/4/16.
 */
public class ContactObject {

    private String contactName;
    private String contactNo;
    private String image;
    private boolean selected;

    public String getName() {
        return contactName;
    }

    public void setName(String contactName) {
        this.contactName = contactName;
    }

    public String getNumber() {
        return contactNo;
    }

    public void setNumber(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
