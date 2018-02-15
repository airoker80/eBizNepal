package com.paybyonline.ebiz.Adapter.Model;

/**
 * Created by mefriend24 on 10/20/16.
 */
public class UserNotification {

    String message;
    String logo;
    String category;
    String createdDate;


    public UserNotification(String message, String logo, String category, String createdDate) {
        this.message = message;
        this.logo = logo;
        this.category = category;
        this.createdDate = createdDate;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
