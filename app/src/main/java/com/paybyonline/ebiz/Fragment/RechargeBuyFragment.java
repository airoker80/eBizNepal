package com.paybyonline.ebiz.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.RechargeBuyViewPageAdapter;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeBuyFragment extends Fragment {

    UserDeviceDetails userDeviceDetails;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    MyUserSessionManager myUserSessionManager;
    private HashMap<String, String> userDetails;
    CoordinatorLayout coordinatorLayout;
    //    private static String url;
    ViewPager mViewPager;
    AppCompatActivity mActivity;
    RechargeBuyViewPageAdapter rechargeBuyViewPageAdapter;

    public RechargeBuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge_buy, container, false);

        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
//        url = PayByOnlineConfig.SERVER_URL;
        // to reset menu item
        setHasOptionsMenu(true);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Payments");
        ((DashBoardActivity) getActivity()).setFragmentName("Buy/Recharge");
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        rechargeBuyViewPageAdapter = new RechargeBuyViewPageAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(rechargeBuyViewPageAdapter);

        Bundle b = getArguments();
        if (b != null) {
            String returnPage = b.getString("returnPage");
            if (returnPage != null) {
                mViewPager.setCurrentItem(Integer.parseInt(returnPage));
            }
        }


        PagerTabStrip pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.red_color));


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (AppCompatActivity) context;
        }
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Payments");
        ((DashBoardActivity) getActivity()).setFragmentName("Buy/Recharge");
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {

            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }

   /* public void callService(){

        int index = mViewPager.getCurrentItem();

        RechargeBuyViewPageAdapter adapter = (RechargeBuyViewPageAdapter) mViewPager.getAdapter();

        Log.i("msgsss","adapter.getItem(mViewPager.getCurrentItem()))\n" +
                "                .updateFragment() "+adapter.getItem(mViewPager.getCurrentItem()));

        Fragment fragment = adapter.getItem(mViewPager.getCurrentItem());

        if (fragment instanceof MyOwnServicesFragment) {

            Log.i("fragment", "General Fragment");

            MyOwnServicesFragment myOwnServicesFragment = (MyOwnServicesFragment)fragment;
            myOwnServicesFragment.addServicesOnListModel(getContext());


        }
    }*/


}
