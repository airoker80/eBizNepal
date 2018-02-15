package com.paybyonline.ebiz.Adapter.Model;

public class HeadingWithIcon {

	String heading;
	Integer icon;
	public HeadingWithIcon(String heading, Integer icon) {
		super();
		this.heading = heading;
		this.icon = icon;
	}
	public HeadingWithIcon(String heading) {
		super();
		this.heading = heading;
		this.icon = icon;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public Integer getIcon() {
		return icon;
	}
	public void setIcon(Integer icon) {
		this.icon = icon;
	}

}
