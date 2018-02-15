package com.paybyonline.ebiz.Adapter.Model;

import java.io.Serializable;

public class SpinnerNameValueList implements Serializable {

	 String text;
     String value;
    

    public SpinnerNameValueList(String text, String value){
    	this.text = text;
    	this.value = value;
    }
    
    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}

