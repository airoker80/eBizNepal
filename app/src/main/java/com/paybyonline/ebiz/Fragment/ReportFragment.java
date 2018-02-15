//
//package com.paybyonline.ebiz.Fragment;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.PagerTabStrip;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.paybyonline.ebiz.Activity.DashBoardActivity;
//import com.paybyonline.ebiz.Fragment.FragmentHelper.ReportViewPagerAdapter;
//import com.paybyonline.ebiz.R;
//
//import java.lang.reflect.Field;
//
///**
// * A simple {@link android.support.v4.app.Fragment} subclass.
// *
// */
//public class ReportFragment extends Fragment {
//
//	public ReportFragment() {
//		// Required empty public constructor
//	}
//
//	ViewPager mViewPager ;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//		final View view = inflater.inflate(R.layout.fragment_report, container, false);
//		// Locate the ViewPager in viewpager_main.xml
//		// to reset menu item
//		setHasOptionsMenu(true);
//		((DashBoardActivity) getActivity())
//				.setTitle("Report");
//
//		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
//		PagerTabStrip pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pagerTabStrip);
//		pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.red_color));
//		//pagerTabStrip.setTabIndicatorColor(Color.parseColor("#FF8300"));
//		mViewPager.setAdapter(new ReportViewPagerAdapter(getChildFragmentManager()));
//
//		return view;
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		try {
//			Field childFragmentManager = Fragment.class
//					.getDeclaredField("mChildFragmentManager");
//			childFragmentManager.setAccessible(true);
//			childFragmentManager.set(this, null);
//		} catch (NoSuchFieldException e) {
//			throw new RuntimeException(e);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		// Set title
//		//getActivity().getActionBar().setTitle(R.string.app_name);
//	}
//
//}
