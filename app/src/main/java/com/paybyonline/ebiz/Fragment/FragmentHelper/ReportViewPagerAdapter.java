package com.paybyonline.ebiz.Fragment.FragmentHelper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.paybyonline.ebiz.Fragment.RechargedDetailsFragment;
import com.paybyonline.ebiz.Fragment.ReportChartFragment;
import com.paybyonline.ebiz.Fragment.TransactionReportFragment;

/**
 * Created by Anish on 8/17/2016.
 */
public class ReportViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private String titles[] = new String[] {"Transaction List","Transaction Report","Chart"};
//    private String titles[] = new String[] {"Transaction List","Purchase Report","Transaction Report","Chart"};
//    private String titles[] = new String[] {"Transaction List","Summary Report","Purchase Report","Transaction Report","Chart"};
    Context context;

    public ReportViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {


           /* case 0:
                RechargedDetailsFragment fragmenttab0 = new RechargedDetailsFragment();
//                AccountSummaryFragment fragmenttab0 = new AccountSummaryFragment();
                return fragmenttab0;*/

            // Open FragmentTab1.java
            case 0:
                RechargedDetailsFragment fragmenttab1 = new RechargedDetailsFragment();
//                PurchaseReportFragment fragmenttab1 = new PurchaseReportFragment();
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                TransactionReportFragment fragmenttab2 = new TransactionReportFragment();
                return fragmenttab2;

            case 2:
                ReportChartFragment fragmenttab3 = new ReportChartFragment();
                return fragmenttab3;

        }
        return null;
    }

    public CharSequence getPageTitle(int position){

        // Generate title based on item position
  /*      String s=titles[position];
        SpannableString ss=  new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, s.length(), 0);*/

        return titles[position];

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
