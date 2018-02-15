package com.paybyonline.ebiz.Adapter.Model;

public class RechargeDetailsData {
	
	public  String transactionDate;
	public  String transactionNo;
	public  String serviceCategoryType;	
	public  String serviceCategoryAmount;
	public  String purchasedAmount;
	public  String payTypeDiscount;
	public  String payTypeCommisssion;
	public  String netCostAmount;
	public String  dealDiscount;
	public String reward;
	public  String rechargedNumber;
	public  String remarks;
	public  String pspAmount;
	public  String eWalletAmount;

	public RechargeDetailsData(String transactionDate, String transactionNo,
							   String serviceCategoryType, String serviceCategoryAmount,
							   String purchasedAmount, String payTypeDiscount,
							   String payTypeCommisssion, String netCostAmount,
							   String rechargedNumber, String remarks, String dealDiscount,
							   String reward,String pspAmount, String eWalletAmount) {
		super();
		this.transactionDate = transactionDate;
		this.transactionNo = transactionNo;
		this.serviceCategoryType = serviceCategoryType;
		this.serviceCategoryAmount = serviceCategoryAmount;
		this.purchasedAmount = purchasedAmount;
		this.payTypeDiscount = payTypeDiscount;
		this.payTypeCommisssion = payTypeCommisssion;
		this.netCostAmount = netCostAmount;
		this.rechargedNumber = rechargedNumber;
		this.remarks = remarks;
		this.dealDiscount=dealDiscount;
		this.reward=reward;
		this.pspAmount = pspAmount;
		this.eWalletAmount = eWalletAmount;
	}

	public String getPspAmount() {
		return pspAmount;
	}

	public void setPspAmount(String pspAmount) {
		this.pspAmount = pspAmount;
	}

	public String geteWalletAmount() {
		return eWalletAmount;
	}

	public void seteWalletAmount(String eWalletAmount) {
		this.eWalletAmount = eWalletAmount;
	}

	public String getDealDiscount() {
		return dealDiscount;
	}

	public void setDealDiscount(String dealDiscount) {
		this.dealDiscount = dealDiscount;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}


	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getServiceCategoryType() {
		return serviceCategoryType;
	}

	public void setServiceCategoryType(String serviceCategoryType) {
		this.serviceCategoryType = serviceCategoryType;
	}

	public String getServiceCategoryAmount() {
		return serviceCategoryAmount;
	}

	public void setServiceCategoryAmount(String serviceCategoryAmount) {
		this.serviceCategoryAmount = serviceCategoryAmount;
	}

	public String getPurchasedAmount() {
		return purchasedAmount;
	}

	public void setPurchasedAmount(String purchasedAmount) {
		this.purchasedAmount = purchasedAmount;
	}

	public String getPayTypeDiscount() {
		return payTypeDiscount;
	}

	public void setPayTypeDiscount(String payTypeDiscount) {
		this.payTypeDiscount = payTypeDiscount;
	}

	public String getPayTypeCommisssion() {
		return payTypeCommisssion;
	}

	public void setPayTypeCommisssion(String payTypeCommisssion) {
		this.payTypeCommisssion = payTypeCommisssion;
	}

	public String getNetCostAmount() {
		return netCostAmount;
	}

	public void setNetCostAmount(String netCostAmount) {
		this.netCostAmount = netCostAmount;
	}

	public String getRechargedNumber() {
		return rechargedNumber;
	}

	public void setRechargedNumber(String rechargedNumber) {
		this.rechargedNumber = rechargedNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	

}
