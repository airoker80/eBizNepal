package com.paybyonline.ebiz.Interface;

import android.view.View;

/**
 * Created  on 15/11/15.
 * <p/>
 * This interface will be overridden in viewHolder classes
 * to be able to call onClick method on corresponding recyclerView items
 */
public interface ItemClickListener {
    void onClick(View view, int position);
}
