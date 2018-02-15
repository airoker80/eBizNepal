package com.paybyonline.ebiz.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimDetailsFragment extends Fragment {


    public SimDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Sim Details");
        ((DashBoardActivity) getActivity()).setFragmentName("Sim Details");
        return inflater.inflate(R.layout.fragment_sim_details, container, false);
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Sim Details");
        ((DashBoardActivity) getActivity()).setFragmentName("Sim Details");
        super.onResume();
    }
}
