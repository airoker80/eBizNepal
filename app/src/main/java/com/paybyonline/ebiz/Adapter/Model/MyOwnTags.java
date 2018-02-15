package com.paybyonline.ebiz.Adapter.Model;

import android.util.Log;

public class MyOwnTags {

	String tagName;
	String tagCount;
	String tagServices;

	public MyOwnTags(String tagName, String tagCount) {
		super();
		this.tagName = tagName;
		this.tagCount = tagCount;
		Log.i("tagName",""+tagName);
	}

	public MyOwnTags(String tagName, String tagCount, String tagServices) {
		this.tagName = tagName;
		this.tagCount = tagCount;
		this.tagServices = tagServices;
	}

	public String getTagServices() {
		return tagServices;
	}

	public void setTagServices(String tagServices) {
		this.tagServices = tagServices;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagCount() {
		return tagCount;
	}

	public void setTagCount(String tagCount) {
		this.tagCount = tagCount;
	}

}
