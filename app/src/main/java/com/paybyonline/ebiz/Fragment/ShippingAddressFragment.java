package com.paybyonline.ebiz.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingAddressFragment extends Fragment {


    public ShippingAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping_address, container, false);

        return view;
    }

}
