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
import com.paybyonline.ebiz.Adapter.Model.PaymentProfile;
import com.paybyonline.ebiz.Adapter.ProfilePaymentProfileAdapter;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
import com.paybyonline.ebiz.serverdata.PboServerRequestListener;
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
public class ProfilePaymentProfileFragment extends Fragment {

    RecyclerView recycleView;
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    RelativeLayout data_not_available;
    CoordinatorLayout coordinatorLayout;
    //    PboServerRequestHandler handler;
    ArrayList<PaymentProfile> listOfProfiles = new ArrayList<>();
    private RetrofitHelper retrofitHelper;

    public ProfilePaymentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile_payment_profile, container, false);
        ((DashBoardActivity) getActivity()) .setTitle("Payment Profile");
        recycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        data_not_available = (RelativeLayout) view.findViewById(R.id.data_not_available);

        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(mLayoutManager);
        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        getProfilePaymentProfile();
        return view;
    }

    public void getProfilePaymentProfile() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        // Make Http call
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getProfilePaymentProfile");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());

/*        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait", params,
                new PboServerRequestListener() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            Log.i("Listresponse", "response");
                            handleProfilePaymentProfileContentResponse(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });*/


        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("Listresponse", "response");
                    handleProfilePaymentProfileContentResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile Payment:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, params);
    }

    // public void handleProfilePaymentProfileContentResponse(JSONObject response) throws JSONException {
    public void handleProfilePaymentProfileContentResponse(JSONObject response) throws JSONException {

        Log.i("Listresponse", "response");
        // Log.i("Listresponse", response + "");
        listOfProfiles = new ArrayList<PaymentProfile>();
//        String jsonString= GenerateJsonData.getJsonData(getContext(), R.raw.profile_payment_profile);
        try {
            JSONObject jsonObj = response;
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obs = jsonArray.getJSONObject(i);

                    Log.i("obs", obs + "");

                    // PaymentProfile(String usedBy,String profImg,String profName,String bankName,String paymentMethod)
                    listOfProfiles.add(new PaymentProfile(obs.getString("preference"),
                            obs.getString("logo"), obs.getString("profileName"), obs.getString("bankName")
                            , obs.getString("paymentMethod")));

                }
                ProfilePaymentProfileAdapter profileAdapter = new ProfilePaymentProfileAdapter(
                        getActivity(), listOfProfiles);

                recycleView.setAdapter(profileAdapter);

            } else {
                data_not_available.setVisibility(View.VISIBLE);


            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }


    }


}
