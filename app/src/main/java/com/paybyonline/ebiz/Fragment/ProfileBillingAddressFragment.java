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
import com.paybyonline.ebiz.Adapter.Model.ProfileBillingAddress;
import com.paybyonline.ebiz.Adapter.ProfileBillingAddressAdapter;
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
public class ProfileBillingAddressFragment extends Fragment {

    CoordinatorLayout coordinatorLayout;
    RecyclerView billingAddView;
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    RelativeLayout data_not_available;
    //    PboServerRequestHandler handler;
    ArrayList<ProfileBillingAddress> profileBillingAddress;
    private RetrofitHelper retrofitHelper;

    public ProfileBillingAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_billing_address, container, false);
        ((DashBoardActivity) getActivity())
                .setTitle("Billing Address");
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        data_not_available = (RelativeLayout) view.findViewById(R.id.data_not_available);
        billingAddView = (RecyclerView) view.findViewById(R.id.billingAddView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        billingAddView.setHasFixedSize(true);
        billingAddView.setLayoutManager(mLayoutManager);
        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        obtainBillingAddressInfo();
        return view;
    }

    public void obtainBillingAddressInfo() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getProfileBillingAddress");
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
                Log.i("Profile Billing:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainBillingAddressResponse(JSONObject response) throws JSONException {
        // public void handleObtainUserProfileInfoResponse(JSONObject response) throws JSONException{

//        String jsonData= GenerateJsonData.getJsonData(getContext(), R.raw.profile_billing_address);
        profileBillingAddress = new ArrayList<ProfileBillingAddress>();

        try {

            JSONObject dataSource = response;
//            JSONObject dataSource = new JSONObject(jsonData);
            Log.i("data", "" + dataSource);

            JSONArray jsonArray = dataSource.getJSONArray("data");

            if (jsonArray.length() > 0) {

                if (dataSource.getString("msgTitle").equals("Success")) {
                    // JSONObject data = response.getJSONObject("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obs = jsonArray.getJSONObject(i);

                        profileBillingAddress.add(new ProfileBillingAddress(obs.getString("id"),
                                obs.getString("preference")
                                , obs.getString("status"), obs.getString("remark")
                                , obs.getString("name"), obs.getString("state")
                                , obs.getString("zipPostalCode"), obs.getString("companyName")
                                , obs.getString("addressLine2"), obs.getString("addressLine1")
                                , obs.getString("country"), obs.getString("city")
                        ));

                    }

                    ProfileBillingAddressAdapter profileBillingAddressAdapter = new ProfileBillingAddressAdapter(
                            getActivity(), profileBillingAddress);

                    billingAddView.setAdapter(profileBillingAddressAdapter);

                } else {

                    data_not_available.setVisibility(View.VISIBLE);
                }

            } else {

                data_not_available.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
