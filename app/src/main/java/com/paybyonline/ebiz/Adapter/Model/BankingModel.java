package com.paybyonline.ebiz.Adapter.Model;

/**
 * Created by hp on 7/14/2017.
 */

public class BankingModel {

    /**
     * id : 18
     * userPayName : SCT
     * userPayLogo : 2016-06-23010921atM.png
     * bankCode : nic
     */

    private int id;
    private String userPayName;
    private String userPayLogo;

    public BankingModel(int id, String userPayName, String userPayLogo) {
        this.id = id;
        this.userPayName = userPayName;
        this.userPayLogo = userPayLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserPayName() {
        return userPayName;
    }

    public void setUserPayName(String userPayName) {
        this.userPayName = userPayName;
    }

    public String getUserPayLogo() {
        return userPayLogo;
    }

    public void setUserPayLogo(String userPayLogo) {
        this.userPayLogo = userPayLogo;
    }

}
