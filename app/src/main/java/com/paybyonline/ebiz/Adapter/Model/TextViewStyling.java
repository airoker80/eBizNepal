package com.paybyonline.ebiz.Adapter.Model;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

public class TextViewStyling {

	public SpannableString textViewLink(String tempString) {
		SpannableString spanString = new SpannableString(tempString);
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0,
				spanString.length(), 0);
		/*spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
				spanString.length(), 0);*/
		return spanString;
	}
	
	public SpannableString setTextCustomColor(String tempString) {
		SpannableString spanString = new SpannableString(tempString);		
		spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#5F86C4")), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}
	
	
	
}
