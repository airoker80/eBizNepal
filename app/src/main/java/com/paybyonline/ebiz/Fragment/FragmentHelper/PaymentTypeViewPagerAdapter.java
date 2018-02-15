package com.paybyonline.ebiz.Fragment.FragmentHelper;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.paybyonline.ebiz.Fragment.AtmBankingFragment;
import com.paybyonline.ebiz.Fragment.BankDepositFragment;
import com.paybyonline.ebiz.Fragment.CreditCardFragment;
import com.paybyonline.ebiz.Fragment.DebitCardFragment;
import com.paybyonline.ebiz.Fragment.LocalDebitCreditCardFragment;
import com.paybyonline.ebiz.Fragment.NetBankingFragment;
import com.paybyonline.ebiz.Fragment.PaymentAggregatorFragment;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;

import java.util.HashMap;

/**
 * Created by Anish on 8/16/2016.
 */

public class  PaymentTypeViewPagerAdapter extends  FragmentPagerAdapter {


    final int PAGE_COUNT = 4;
    Bundle bundle=new Bundle();
    CoordinatorLayout coordinatorLayout;
    /*private String titles[] = new String[] {"Bank Deposit","Credit Card","Debit Card",
            "PaymentAggregator","AtmBanking","NetBanking"};*/
    private String titles[] = new String[] {"Online Banking","Counter Deposit","ATM Banking","Debit/Credit Card"};
    //private String titles[] = new String[] {"Bank Deposit","Credit Card","Debit Card",
      //      "PaymentAggregator","Online Banking", "ATM Banking"};
    MyUserSessionManager myUserSessionManager;
    // The key to this is to return a SpannableString,
    // containing your icon in an ImageSpan, from your PagerAdapter's getPageTitle(position) method:
    private HashMap<String, String> userDetails;
    Context context;


    public PaymentTypeViewPagerAdapter(FragmentManager fm, Context context,Bundle bundle ,
                                       CoordinatorLayout coordinatorLayout) {

        super(fm);
        this.context = context;
        this.coordinatorLayout = coordinatorLayout;
        this.bundle=bundle;
        Log.i("bundleList",""+bundle);
        myUserSessionManager = new MyUserSessionManager(context);
        userDetails = myUserSessionManager.getUserDetails();

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
               /* BankDepositFragment fragmenttab0 = new BankDepositFragment();
                Log.i("BankDepositFragment", "" + bundle.getString("amount"));
                fragmenttab0.setArguments(bundle);*/
                NetBankingFragment fragmenttab0 = new NetBankingFragment();
                fragmenttab0.setArguments(bundle);
                return fragmenttab0;

            // Open FragmentTab1.java
            case 1:
                BankDepositFragment fragmenttab1 = new BankDepositFragment();
                Log.i("BankDepositFragment", "" + bundle.getString("amount"));
                fragmenttab1.setArguments(bundle);
               /* CreditCardFragment fragmenttab1 = new CreditCardFragment();
                fragmenttab1.setArguments(bundle);*/
                return fragmenttab1;

//             Open FragmentTab2.java


//            case 3:
//                PaymentAggregatorFragment fragmenttab3 = new PaymentAggregatorFragment();
//                fragmenttab3.setArguments(bundle);
//                return fragmenttab3;
//            case 4:
//                NetBankingFragment fragmenttab5 = new NetBankingFragment();
//                fragmenttab5.setArguments(bundle);
//                return fragmenttab5;
            case 2:
                AtmBankingFragment fragmenttab4 = new AtmBankingFragment();
                fragmenttab4.setArguments(bundle);
                return fragmenttab4;

            case 3:
//                DebitCardFragment fragmenttab2 = new DebitCardFragment();
                LocalDebitCreditCardFragment fragmenttab2 = new LocalDebitCreditCardFragment();
                fragmenttab2.setArguments(bundle);
                return fragmenttab2;

        }
        return null;
    }

    public CharSequence getPageTitle(int position){

        // Generate title based on item position
        return titles[position];

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}


