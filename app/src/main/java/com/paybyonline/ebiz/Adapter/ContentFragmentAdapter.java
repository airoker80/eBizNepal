package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.paybyonline.ebiz.Fragment.MyOwnServicesFragment;
import com.paybyonline.ebiz.Fragment.RechargeFragment;
import com.paybyonline.ebiz.Fragment.ServiceByCategoryFragment;

public class ContentFragmentAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 1;
    private final Context c;
    private String titles[] = new String[] {"Recharge","My Own Service","Service By Category"};

    public ContentFragmentAdapter(FragmentManager fragmentManager, Context context, int item_count) {
        super(fragmentManager);
        NUM_ITEMS = item_count;
        c = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {


            case 0:
                RechargeFragment fragmenttab0 = new RechargeFragment();
                return fragmenttab0;

            // Open FragmentTab1.java
            case 1:
                MyOwnServicesFragment fragmenttab1 = new MyOwnServicesFragment();
                return fragmenttab1;

            // Open FragmentTab2.java
            case 2:
                ServiceByCategoryFragment fragmenttab2 = new ServiceByCategoryFragment();
                return fragmenttab2;


        }
        return null;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
