package com.paybyonline.ebiz.Adapter.Model;

public class ServiceCategoryServiceTypeDetails {

	String serviceCategoryName;
	String serviceTypeName;
	String serviceCategoryId;
	String serviceTypeId;
	String scstId;
	String serviceLogo;
	String isProductEnable;

	public ServiceCategoryServiceTypeDetails(String serviceCategoryName, String serviceTypeName,
											 String serviceCategoryId, String serviceTypeId, String scstId) {
		super();
		this.serviceCategoryName = serviceCategoryName;
		this.serviceTypeName = serviceTypeName;
		this.serviceCategoryId = serviceCategoryId;
		this.serviceTypeId = serviceTypeId;
		this.scstId = scstId;
	}
	
	public ServiceCategoryServiceTypeDetails(String serviceCategoryName, String serviceTypeName, String serviceCategoryId, String serviceTypeId, String scstId, String serviceLogo, String isProductEnable) {
		this.serviceCategoryName = serviceCategoryName;
		this.serviceTypeName = serviceTypeName;
		this.serviceCategoryId = serviceCategoryId;
		this.serviceTypeId = serviceTypeId;
		this.scstId = scstId;
		this.serviceLogo = serviceLogo;
		this.isProductEnable = isProductEnable;
	}

	public String getIsProductEnable() {
		return isProductEnable;
	}

	public void setIsProductEnable(String isProductEnable) {
		this.isProductEnable = isProductEnable;
	}

	public String getServiceLogo() {
		return serviceLogo;
	}

	public void setServiceLogo(String serviceLogo) {
		this.serviceLogo = serviceLogo;
	}

	public String getScstId() {
		return scstId;
	}


	public void setScstId(String scstId) {
		this.scstId = scstId;
	}


	public String getServiceCategoryName() {
		return serviceCategoryName;
	}

	public void setServiceCategoryName(String serviceCategoryName) {
		this.serviceCategoryName = serviceCategoryName;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public  String getServiceCategoryId() {
		return serviceCategoryId;
	}

	public void setServiceCategoryId(String serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceTitleName() {
		return  serviceCategoryName+"  "+serviceTypeName;
	}
}
