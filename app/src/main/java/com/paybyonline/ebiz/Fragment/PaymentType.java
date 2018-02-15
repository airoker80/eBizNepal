//package com.paybyonline.ebiz.Fragment;
//
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTabHost;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.paybyonline.ebiz.Activity.DashBoardActivity;
//import com.paybyonline.ebiz.R;
//
//import java.lang.reflect.Field;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class PaymentType extends Fragment {
//
//    private FragmentTabHost mTabHost;
//
//    public PaymentType() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        ((DashBoardActivity) getActivity())
//                .setTitle("Recharge");
//
//
//        mTabHost = new FragmentTabHost(getActivity());
//        // Locate fragment1.xml to create FragmentTabHost
//        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_payment_type);
//        // Create Tab 1
//        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Payment Option"), PaymentOptionFragment.class, null);
//        // Create Tab 2
//        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Load Profile"), LoadProfileFragment.class, null);
//        // Create Tab 3
//
//
//        return mTabHost;
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            Field childFragmentManager = Fragment.class
//                    .getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Remove FragmentTabHost
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mTabHost = null;
//    }
//}
