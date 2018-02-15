package com.paybyonline.ebiz.Adapter.Model;

/**
 * Created by Anish on 6/9/2016.
 */
public class ServiceCategoryDetailsGrid {

    String serviceCategoryName;
    String serviceCategoryId;
    String serviceCatImage;
    String isMerchant;

    public ServiceCategoryDetailsGrid(String serviceCategoryId, String serviceCategoryName, String serviceCatImage, String isMerchant) {

        this.serviceCategoryName = serviceCategoryName;
        this.serviceCategoryId = serviceCategoryId;
        this.serviceCatImage = serviceCatImage;
        this.isMerchant = isMerchant;
    }

    public ServiceCategoryDetailsGrid(String serviceCategoryId, String serviceCategoryName, String serviceCatImage) {

        this.serviceCategoryName = serviceCategoryName;
        this.serviceCategoryId = serviceCategoryId;
        this.serviceCatImage = serviceCatImage;
        this.isMerchant = isMerchant;
    }
    public String getIsMerchant() {
        return isMerchant;
    }

    public void setIsMerchant(String isMerchant) {
        this.isMerchant = isMerchant;
    }

    public String getServiceCategoryName() {
        return serviceCategoryName;
    }

    public void setServiceCategoryName(String serviceCategoryName) {
        this.serviceCategoryName = serviceCategoryName;
    }

    public String getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(String serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public String getServiceCatImage() {
        return serviceCatImage;
    }

    public void setServiceCatImage(String serviceCatImage) {
        this.serviceCatImage = serviceCatImage;
    }




}


