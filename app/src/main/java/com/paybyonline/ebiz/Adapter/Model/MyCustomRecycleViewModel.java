package com.paybyonline.ebiz.Adapter.Model;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by Anish on 8/17/2016.
 */
public class MyCustomRecycleViewModel {

    String payTypeIds;
    String userPayName;
    String userPayLogo;
    String address,accountNumber;
    Bundle bundle;

    public MyCustomRecycleViewModel(String payTypeIds, String userPayName, String userPayLogo, String address, String accountNumber, Bundle bundle) {
        this.payTypeIds = payTypeIds;
        this.userPayName = userPayName;
        this.userPayLogo = userPayLogo;
        this.address = address;
        this.accountNumber = accountNumber;
        this.bundle = bundle;
    }

    public String getPayTypeIds() {
        return payTypeIds;
    }

    public void setPayTypeIds(String payTypeIds) {
        this.payTypeIds = payTypeIds;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
