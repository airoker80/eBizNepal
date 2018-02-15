package com.paybyonline.ebiz.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.MyCustomRecycleViewModel;
import com.paybyonline.ebiz.Adapter.MyCustomGridAdapter;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
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
import java.util.List;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankDepositFragment extends Fragment {

    CoordinatorLayout coordinateLayout;
    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    String userPayName[];
    String userPayLogo[];
    RecyclerView ServiceProviderListView;
    String payTypeIds[];
    List<MyCustomRecycleViewModel> myCustomList = new ArrayList<MyCustomRecycleViewModel>();
    Bundle bundle;
    private RetrofitHelper retrofitHelper;

    public BankDepositFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bank_deposit, container, false);
        getActivity().setTitle("Bank Deposit");

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        myUserSessionManager = new MyUserSessionManager(getActivity());
        userDeviceDetails = new UserDeviceDetails(getActivity());
        coordinateLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        ServiceProviderListView = (RecyclerView) view.findViewById(R.id.ServiceProviderListView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ServiceProviderListView.setHasFixedSize(true);
        ServiceProviderListView.setLayoutManager(mLayoutManager);
        ServiceProviderListView.setItemAnimator(new DefaultItemAnimator());
        bundle = getArguments();
        Log.i("msg", "" + bundle);

        obtainUserPaymentType();

        return view;
    }

    public void obtainUserPaymentType() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "obtainUserPaymentType");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("payMethod", "Bank Deposit");
        //params.put("payMethod", "Atm Banking");

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinateLayout,getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
//                params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleObtainUserPaymentType(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.i("response:", "" + response);
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainUserPaymentType(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Bank Deposit", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainUserPaymentType(JSONObject response) throws JSONException {

        try {
            Log.i("response:", "" + response);

            JSONArray jsonArray = response.getJSONArray("dataList");
            myCustomList = new ArrayList<MyCustomRecycleViewModel>();
            if (jsonArray.length() != 0) {
                payTypeIds = new String[jsonArray.length()];
                userPayName = new String[jsonArray.length()];
                userPayLogo = new String[jsonArray.length()];
                // Loop through each array element, get JSON object
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    payTypeIds[i] = obj.get("id").toString();
                    userPayName[i] = obj.get("userPayName").toString();
                    userPayLogo[i] = PayByOnlineConfig.BASE_URL + "documents/" +
                            Uri.encode(obj.get("userPayLogo").toString());

                    myCustomList.add(new MyCustomRecycleViewModel(obj.get("id").toString(),
                            obj.get("userPayName").toString(), PayByOnlineConfig.BASE_URL + "documents/" +
                            Uri.encode(obj.get("userPayLogo").toString()), obj.getString("address"), obj.getString("accountNumber"), bundle));

                }


                MyCustomGridAdapter adapter = new MyCustomGridAdapter(getActivity(), myCustomList);

                ServiceProviderListView.setAdapter(adapter);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
