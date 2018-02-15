package com.paybyonline.ebiz.Fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

public class UseRechargeCodeFragment extends Fragment implements
        OnClickListener {

    //    private static String url;
    private Button btnUseRechargeCode;
    private EditText couponCode;
    private EditText mobileNo;
    private EditText reMobileNo;
    CoordinatorLayout coordinatorLayout;


    private MyUserSessionManager myUserSessionManager;
    private UserDeviceDetails userDeviceDetails;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private HashMap<String, String> userDetails;
    private String username = null;
    private RetrofitHelper retrofitHelper;

    public UseRechargeCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_use_recharge_code,
                container, false);


        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        //userDeviceDetails.showToast("UseRechargeCodeFragment");

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        username = userDetails
                .get(MyUserSessionManager.KEY_USERNAME);

//        url = PayByOnlineConfig.SERVER_URL;

        couponCode = (EditText) view.findViewById(R.id.couponCode);
        mobileNo = (EditText) view.findViewById(R.id.mobileNo);
        reMobileNo = (EditText) view.findViewById(R.id.reMobileNo);
        btnUseRechargeCode = (Button) view.findViewById(R.id.btnUseRechargeCode);

        btnUseRechargeCode.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.btnUseRechargeCode:

                if ((couponCode.getText().toString().length() > 0)
                        && (mobileNo.getText().toString().length() > 0)
                        && (reMobileNo.getText().toString().length() > 0)) {
                    if (mobileNo.getText().toString()
                            .equals(reMobileNo.getText().toString())) {

                        rechargeUsingCode();

					/*new HttpAsyncTaskUseRechargeCode().execute(url
                            + "/useRechargeCode");*/
                        HttpAsyncTaskUseRechargeCode();

                    } else {
                        userDeviceDetails.showToast("Confirm field doesnot match");
                    }

                } else {
                    userDeviceDetails.showToast("All Fields are required");
                }
                break;

            default:
                break;
        }
    }

    public void rechargeUsingCode() {


//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "rechargeUsingRechargeCode");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("couponCode", couponCode
                .getText().toString());
        params.put("phoneNo", mobileNo
                .getText().toString());
        params.put("reMobileNo", reMobileNo
                .getText().toString());
        params.put("androidDetails", userDeviceDetails.getUserOsDetails());
        params.put("userIP", userDeviceDetails.getLocalIpAddress());
        params.put("usrFrom", "Mobile");

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Authenticating user", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleRechargeUsingCodeResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleRechargeUsingCodeResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Use Recharge Code", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleRechargeUsingCodeResponse(JSONObject response) throws JSONException {

        JSONObject json;
        try {

            json = response;

            String serverResultMsgs = "";
            String serverResultMsgsTitle = "";

            serverResultMsgs = json.getString("msgs");
            serverResultMsgsTitle = json.getString("messageTitles");

            if (json.getString("messageTitles")
                    .equals("Success")) {
                couponCode.setText("");
                mobileNo.setText("");
                reMobileNo.setText("");

            }

            showMyAlertProgressDialog.showUserAlertDialog(serverResultMsgs,
                    serverResultMsgsTitle);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void HttpAsyncTaskUseRechargeCode() {


//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "androidApp");
        params.put("childTask", "useRechargeCode");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());


        params.put("couponCode", couponCode
                .getText().toString());
        params.put("phoneNo", mobileNo
                .getText().toString());
        params.put("reMobileNo", reMobileNo
                .getText().toString());


        params.put("androidDetails",
                userDeviceDetails.getUserOsDetails());

        params.put("userIP",
                userDeviceDetails.getLocalIpAddress());
        params.put("usrFrom", "Mobile");


        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Working On Process", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    HttpAsyncTaskUseRechargeCodeResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    HttpAsyncTaskUseRechargeCodeResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Use Recharge Code", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void HttpAsyncTaskUseRechargeCodeResponse(JSONObject response) throws JSONException {

        JSONObject json;
        String connectionStatus;
        String serverResultMsgs = "";
        String serverResultMsgsTitle = "";

        showMyAlertProgressDialog.hideProgressDialog();
        json = new JSONObject();
        try {

            Log.i("result", "" + response);
            json = response;


            serverResultMsgs = json.getString("msgs");
            serverResultMsgsTitle = json.getString("messageTitles");

            if (json.getString("messageTitles")
                    .equals("Success")) {
                couponCode.setText("");
                mobileNo.setText("");
                reMobileNo.setText("");

            }


            showMyAlertProgressDialog.showUserAlertDialog(serverResultMsgs,
                    serverResultMsgsTitle);

        } catch (JSONException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            JSONObject res = new JSONObject();
            try {
                res.put("connectionStatus", "failed");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

}
