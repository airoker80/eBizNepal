package com.paybyonline.ebiz.Adapter.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anish on 5/18/2016.
 */
public class ServiceCatagoryDetails {

    String serviceCategoryName;
    String serviceCategoryId;
    String serviceCatImage;

    List<ServiceType> serviceTypeArrayList=new ArrayList<ServiceType>();
    public ServiceCatagoryDetails(String serviceCategoryId, String serviceCategoryName, String serviceCatImage) {

        this.serviceCategoryName = serviceCategoryName;
        this.serviceCategoryId = serviceCategoryId;
        this.serviceCatImage = serviceCatImage;

    }

 public ServiceCatagoryDetails(String serviceCategoryId, String serviceCategoryName,
                               String serviceCatImage, List<ServiceType> serviceTypeArrayList) {

        this.serviceCategoryName = serviceCategoryName;
        this.serviceCategoryId = serviceCategoryId;
        this.serviceCatImage = serviceCatImage;
     this.serviceTypeArrayList = serviceTypeArrayList;
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

    public List<ServiceType> getServiceTypeArrayList() {
        return serviceTypeArrayList;
    }

    public void setServiceTypeArrayList(List<ServiceType> serviceTypeArrayList) {
        this.serviceTypeArrayList = serviceTypeArrayList;
    }
}
