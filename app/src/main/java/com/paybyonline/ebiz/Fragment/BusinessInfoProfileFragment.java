package com.paybyonline.ebiz.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.R;


/**
 * Created by Anish on 10/3/2016.
 */
public class BusinessInfoProfileFragment extends Fragment {


    public BusinessInfoProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_info, container, false);
        return view;
    }
}
