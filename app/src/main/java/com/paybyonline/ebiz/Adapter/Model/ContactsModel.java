package com.paybyonline.ebiz.Adapter.Model;

/**
 * Created by Sameer on 1/30/2018.
 */

public class ContactsModel {
    String contactName,contactPhone;

    public ContactsModel(String contactName, String contactPhone) {
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
