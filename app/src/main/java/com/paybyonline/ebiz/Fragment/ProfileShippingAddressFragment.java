package com.paybyonline.ebiz.Fragment;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.Model.ProfileShippingAddress;
import com.paybyonline.ebiz.Adapter.ProfileShippingAddressAdapter;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileShippingAddressFragment extends Fragment {

    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
    RecyclerView shippingAddView;
    //    PboServerRequestHandler handler;
    ArrayList<ProfileShippingAddress> profileShippingAddresses;
    RelativeLayout data_not_available;
    private RetrofitHelper retrofitHelper;

    public ProfileShippingAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.user_profile_shipping_address, container, false);
        View view = inflater.inflate(R.layout.shipping_address, container, false);
        ((DashBoardActivity) getActivity())
                .setTitle("Shipping Address");

        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        shippingAddView = (RecyclerView) view.findViewById(R.id.shippingAddView);
        data_not_available = (RelativeLayout) view.findViewById(R.id.data_not_available);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        shippingAddView.setHasFixedSize(true);
        shippingAddView.setLayoutManager(mLayoutManager);
        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        obtainShippingAddressInfo();
        return view;
    }

    public void obtainShippingAddressInfo() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getProfileShippingAddress");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleObtainBillingAddressResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainBillingAddressResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Atm Banking:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainBillingAddressResponse(JSONObject response) throws JSONException {

        profileShippingAddresses = new ArrayList<ProfileShippingAddress>();

        try {

            JSONObject dataSource = response;
            Log.i("data", "" + dataSource);

            JSONArray jsonArray = dataSource.getJSONArray("data");
            Log.i("data", "jsonArray.length() " + jsonArray.length());
            if (jsonArray.length() > 0) {

                if (dataSource.getString("msgTitle").equals("Success")) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obs = jsonArray.getJSONObject(i);

                        profileShippingAddresses.add(new ProfileShippingAddress(obs.getString("id"),
                                obs.getString("preference")
                                , obs.getString("status"), obs.getString("remark")
                                , obs.getString("name"), obs.getString("state")
                                , obs.getString("zipPostalCode"), obs.getString("companyName")
                                , obs.getString("addressLine2"), obs.getString("addressLine1")
                                , obs.getString("country"), obs.getString("city")
                        ));

                    }

                    ProfileShippingAddressAdapter profileShippingAddressAdapter =
                            new ProfileShippingAddressAdapter(
                                    getActivity(), profileShippingAddresses);

                    shippingAddView.setAdapter(profileShippingAddressAdapter);

                } else {

                    data_not_available.setVisibility(View.VISIBLE);
                }

            } else {
                Log.i("msgsss", "------------------");
                data_not_available.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
