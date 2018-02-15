package com.paybyonline.ebiz.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.R;


public class AboutUsFragment extends Fragment {
    TextView textDetails;
    ImageView logoImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        textDetails = (TextView) view.findViewById(R.id.textDetails);
        logoImage = (ImageView) view.findViewById(R.id.textTop);
        if (getActivity().getPackageName().contains("ebiz")) {
            logoImage.setImageResource(R.drawable.ic_new_logo);
        }else {
            logoImage.setImageResource(R.mipmap.ic_launcher);
        }

//        if (getActivity().getPackageName().contains("guheshwori")) {
//            logoImage.setImageResource(R.mipmap.ic_launcher);
//        }else if (getActivity().getPackageName().contains("tirnus")){
//            logoImage.setImageResource(R.drawable.tirnus);
//        }else if (getActivity().getPackageName().contains("aimspay")){
//            logoImage.setImageResource(R.drawable.aimspay);
//        }else if (getActivity().getPackageName().contains("ebiz")){
//            logoImage.setImageResource(R.drawable.ic_new_logo);
//        }
//        else {
//            logoImage.setImageResource(R.drawable.ic_new_logo);
//        }
        //((DashBoardActivity)getActivity()).setTitle("About Us");
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("About Us");
        ((DashBoardActivity) getActivity()).setFragmentName("About Us");
//        textDetails.setText(Html.fromHtml("<b>Mobalet (Mobile Wallet)</b> is a Mobile Application developed by Nepal E-Biz Management Pvt Ltd using its in-house proprietary technology ‘Paybyonline’. The Application allows customers to hold funds in Mobalet (Mobile Wallet) and pay for available products and services such as Telecom/Mobile top ups, ISP recharge/renewal, Satellite/Cable TV charges top up and renewals, Utility Bills, School Fees, Airline Tickets, Bus Tickets, Hotel Booking, Restaurant Payment, Taxi payments, and e-Commerce Shopping"));
        textDetails.setText(Html.fromHtml(getString(R.string.about_heading_details)));
        return view;
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("About Us");
        ((DashBoardActivity) getActivity()).setFragmentName("About Us");
        super.onResume();
    }
}
