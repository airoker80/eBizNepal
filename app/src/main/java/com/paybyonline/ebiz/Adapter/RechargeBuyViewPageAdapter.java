package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.paybyonline.ebiz.Fragment.BuyProductFragment;
import com.paybyonline.ebiz.Fragment.MyOwnServicesFragment;
import com.paybyonline.ebiz.Fragment.RechargeGridFragment;
import com.paybyonline.ebiz.Fragment.ServiceByCategoryFragment;

/**
 * Created by mefriend24 on 12/5/16.
 */
public class RechargeBuyViewPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;

    private String titles[] = new String[] {"Recharge","Buy / Pay","Service By Category","My Tagged Services"};
    Context context;

    public RechargeBuyViewPageAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                RechargeGridFragment fragmenttab0 = new RechargeGridFragment();
                return fragmenttab0;
            case 1:
                BuyProductFragment fragmenttab1 = new BuyProductFragment();
                return fragmenttab1;
            case 2:
                ServiceByCategoryFragment fragmenttab2 = new ServiceByCategoryFragment();
                return fragmenttab2;
            case 3:
                MyOwnServicesFragment fragmenttab3 = new MyOwnServicesFragment();
                return fragmenttab3;
        }
        return null;
    }

    public CharSequence getPageTitle(int position){
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}

