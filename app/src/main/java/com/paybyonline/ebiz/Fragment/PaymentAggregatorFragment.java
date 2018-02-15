package com.paybyonline.ebiz.Fragment;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * Created by Anish on 9/8/2016.
 */


public class PaymentAggregatorFragment extends Fragment implements View.OnClickListener {

    EditText payment_note;
    EditText profile_name;
    RadioButton paymentMethod_radio;
    CheckBox chkSave;
    CheckBox chkAgree;
    Button payNowBtn;
    TextView amtVal;
    String paymentNote;
    String amtValue;
    String profileName;
    String errorMessage = "";
    CoordinatorLayout coordinatorLayout;
    private MyUserSessionManager myUserSessionManager;
    DecimalFormat formatter = new DecimalFormat("0.00");
    private UserDeviceDetails userDeviceDetails;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private HashMap<String, String> userDetails;
    //    private static String url;
    Bundle bundle;
    TextView profile_name_title;
    private RetrofitHelper retrofitHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_aggregator, container, false);
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
//        url = PayByOnlineConfig.SERVER_URL;


        bundle = getArguments();
        payment_note = (EditText) view.findViewById(R.id.payment_note);
        amtVal = (TextView) view.findViewById(R.id.amt_val);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        profile_name = (EditText) view.findViewById(R.id.profile_name);
        paymentMethod_radio = (RadioButton) view.findViewById(R.id.paymentMethod_radio);
        chkSave = (CheckBox) view.findViewById(R.id.chkSave);
        chkAgree = (CheckBox) view.findViewById(R.id.chkAgree);
        payNowBtn = (Button) view.findViewById(R.id.payNowBtn);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        amtVal.setText(bundle.getString("amtPayingVal"));
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processFieldInformation();
            }
        });

        profile_name_title = (TextView) view.findViewById(R.id.profile_name_title);
        profile_name_title.setVisibility(View.GONE);
        profile_name.setVisibility(View.GONE);

        chkSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    profile_name_title.setVisibility(View.VISIBLE);
                    profile_name.setVisibility(View.VISIBLE);
                } else {
                    profile_name_title.setVisibility(View.GONE);
                    profile_name.setVisibility(View.GONE);

                }
            }
        });

        return view;
    }

    public Boolean verifyFields() {

        if (payment_note.getText().length() > 0) {
            paymentNote = payment_note.getText().toString();
        }

        if (profile_name.getText().length() > 0) {
            profileName = profile_name.getText().toString();
            profile_name.setError(null);
        } else {
            profile_name.setError("Name Required");
            return false;
        }
        if (chkAgree.isChecked()) {
            chkAgree.setError(null);
        } else {
            chkAgree.setError("Please agree the requirements");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.chkSave:

                if (chkSave.isChecked()) {
                    profile_name.setFocusable(true);
                    profile_name.setFocusableInTouchMode(true);
                    profile_name.setFocusable(true);
                } else {

                    profile_name.setFocusableInTouchMode(false);
                    profile_name.setFocusable(false);
                    profile_name.setText("");
                }
                break;
        }
    }

    public void processFieldInformation() {

        Boolean result = validateFormData();

        if (result) {

            showConfirmOrderFormPaypal();

        } else {

            userDeviceDetails.showToast(errorMessage);

        }
    }

    public Boolean validateFormData() {

        if (chkSave.isChecked() && !(profile_name.getText().toString().length() > 0)) {
            errorMessage = "Please Enter Profile Name";
            return false;
        }
        if (!chkAgree.isChecked()) {
            errorMessage = "Please Agree Payment Terms";
            return false;
        }

        return true;

    }

    public void showConfirmOrderFormPaypal() {


        // SharedPreferences pref = getSharedPreferences("PAYMENTDETAILS", 0);
        // SharedPreferences pref1 = getSharedPreferences("USERPAYPROVIDERDETAILS", 0);


//            RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "showConfirmOrderFormPayPal");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        //  params.put("payUsingIds", payTypeIds[selectedUserPayTypeIndex]);
        params.put("payUsingIds", "10");
        params.put("amtPayingVal", bundle.getString("amtPayingVal"));
        // Make Http call
//            PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,
//                    getActivity());
//            handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
//                    params, new PboServerRequestListener() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                    try {
//                        handleShowConfirmOrderFormPaypal(response);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                }
//            });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleShowConfirmOrderFormPaypal(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Payment Aggregator:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleShowConfirmOrderFormPaypal(JSONObject response) throws JSONException {

        // userDeviceDetails.showToast(response);

        try {
            JSONObject jo = response;


            Bundle infoBundle = new Bundle();
            Double confirmTotalAmount = Double.parseDouble(bundle.getString("amtPayingVal")) +
                    Double.parseDouble(jo.get("payTypeAmount").toString());
            Double confirmTotalAmountUSD = confirmTotalAmount / Double.parseDouble(jo.get("userExchangeRates").toString());

            infoBundle.putString("username", userDetails.get(MyUserSessionManager.KEY_USERNAME));
            infoBundle.putString("payUsingIds", jo.get("userPayTypesIds").toString());
            infoBundle.putString("confirmPayUsings", jo.get("confirmPayUsings").toString());
            infoBundle.putString("confirmPaymentNotes", profile_name.getText().toString());
            infoBundle.putString("confirmPurchasedAmount", bundle.getString("purchasedAmt"));
            infoBundle.putString("confirmDiscountAmount", bundle.getString("disAmt"));
            infoBundle.putString("confirmPaymentProfileNames", profile_name.getText().toString());
            infoBundle.putString("confirmDepositingAmount", bundle.getString("walletDepositingVal"));
            infoBundle.putString("confirmPayingAmount", bundle.getString("amtPayingVal"));
            infoBundle.putString("totalAmt", bundle.getString("totalAmt"));
            infoBundle.putString("payAmt", bundle.getString("amtPayingVal"));
            infoBundle.putString("addDiscountWallet", bundle.getString("addDiscountWallet"));
            // editor.putString("payUsingIds", payTypeIds[selectedUserPayTypeIndex]);
            infoBundle.putString("userIp", userDeviceDetails.getLocalIpAddress());
            infoBundle.putString("confirmTotalAmountUSD", formatter.format(confirmTotalAmountUSD));


            String profileName;

            if (profile_name.getText().toString().length() > 0) {
                profileName = profile_name.getText().toString();
            } else {
                profileName = "";
            }

            String confirmOrderText = "Payment Amount : " + Double.parseDouble(bundle.getString("amtPayingVal")) +
                    "<br>Payment Type " + jo.get("payTypeHeading").toString() +
                    "<br>Value : " + jo.get("payTypeValue").toString() +
                    "<br>Amount :\n" + formatter.format(Double.parseDouble(jo.get("payTypeAmount").toString())) + "<br>" +
                    "<br>Net Payment Amount: " + formatter.format(confirmTotalAmount) +
                    "<br>Payment Profile Name:" + "&nbsp;" + profileName +
                    "<br>Payment Gateway : " + jo.get("paymentGateway").toString() +
                    "<br>Payment Method : " + jo.get("payTypeMethod").toString() +
                    "<br>Payment Currency : " + jo.get("paymentCurrency").toString() +
                    "<br>Exchange Rate : " + jo.get("userExchangeRates").toString() +
                    "<br>Paying Amount : USD " + formatter.format(confirmTotalAmountUSD);

            Fragment fragment = new ConfirmOrderFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            infoBundle.putString("requestFrom", "Paypal");
            infoBundle.putString("confirmOrderText", confirmOrderText);
            fragment.setArguments(infoBundle);
            fragmentTransaction.commit();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




   /* public  void processFieldInformation(){

        Boolean result = validateFormData();

        if(result){
            showConfirmOrderFormPaypal();
        } else {
            userDeviceDetails
                    .showToast(errorMessage);
        }
    }

    public void showConfirmOrderFormPaypal(){

        showMyAlertProgressDialog.showProgressDialog("Processing...",
                "Please wait.");

        SharedPreferences pref = getSharedPreferences("PAYMENTDETAILS", 0);
        SharedPreferences pref1 = getSharedPreferences("USERPAYPROVIDERDETAILS", 0);


        RequestParams params = new RequestParams();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "showConfirmOrderFormPayPal");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        //  params.put("payUsingIds", payTypeIds[selectedUserPayTypeIndex]);
        params.put("payUsingIds",  pref1.getString("payTypeIds", ""));
        params.put("amtPayingVal", pref.getString("amtPayingVal", ""));
        // Make Http call
        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,PaypalPaymentFormActivity.this);
        handler.makeRequest("authenticateUser", "Obtaining Confirm Order", params, new PboServerRequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    handleShowConfirmOrderFormPaypal(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void handleShowConfirmOrderFormPaypal(JSONObject response) throws JSONException{

        // userDeviceDetails.showToast(response);

        try {
            JSONObject jo = response;

            SharedPreferences pref = getSharedPreferences("PAYMENTDETAILS", 0);
            SharedPreferences finalPayData = getSharedPreferences("PAYMENTDETAILSFINAL", 0);
            SharedPreferences.Editor editor = finalPayData.edit();
            editor.clear();
            editor.commit();

            editor = finalPayData.edit();

            Double confirmTotalAmount = Double.parseDouble(pref.getString("amtPayingVal", ""))+Double.parseDouble(jo.get("payTypeAmount").toString());
            Double confirmTotalAmountUSD = confirmTotalAmount/Double.parseDouble(jo.get("userExchangeRates").toString());

            editor.putString("username", userDetails.get(MyUserSessionManager.KEY_USERNAME).toString());
            editor.putString("payUsingIds", jo.get("userPayTypesIds").toString());
            editor.putString("confirmPayUsings", jo.get("confirmPayUsings").toString());
            editor.putString("confirmPaymentNotes", paymentNoteWrapper.getEditText().getText().toString());
            editor.putString("confirmPurchasedAmount", pref.getString("purchasedAmt", ""));
            editor.putString("confirmDiscountAmount", pref.getString("disAmt", ""));
            editor.putString("confirmPaymentProfileNames", paymentProfileNameWrapper.getEditText().getText().toString());
            editor.putString("confirmDepositingAmount",pref.getString("walletDepositingVal", "") );
            editor.putString("confirmPayingAmount",pref.getString("amtPayingVal", "") );
            editor.putString("totalAmt",  pref.getString("totalAmt", ""));
            editor.putString("payAmt", pref.getString("amtPayingVal", ""));
            editor.putString("addDiscountWallet",pref.getString("addDiscountWallet", ""));
            // editor.putString("payUsingIds", payTypeIds[selectedUserPayTypeIndex]);
            editor.putString("userIp", userDeviceDetails.getLocalIpAddress());
            editor.putString("confirmTotalAmountUSD", formatter.format(confirmTotalAmountUSD));

            editor.commit();
            String profileName;
            if(paymentProfileNameWrapper.getEditText().getText().toString().length()>0){
                profileName= paymentProfileNameWrapper.getEditText().getText().toString();
            }else{
                profileName="-";
            }

            String confirmOrderText = "<b>Payment Details</b><br>\n" +
                    "<br>Payment Amount : " +Double.parseDouble(pref.getString("amtPayingVal", ""))+
                    "<br>Payment Type " +jo.get("payTypeHeading").toString()+
                    "<br>Value : " +jo.get("payTypeValue").toString()+
                    "<br>Amount :\n" +formatter.format(Double.parseDouble(jo.get("payTypeAmount").toString()))+"<br>"+
                    "<br>Net Payment Amount: " +formatter.format(confirmTotalAmount)+
                    "<br>Payment Profile Name:"+"&nbsp;"+profileName+
                    "<br>Payment Gateway : " +jo.get("paymentGateway").toString()+
                    "<br>Payment Method : " +jo.get("payTypeMethod").toString()+
                    "<br>Payment Currency : " +jo.get("paymentCurrency").toString()+
                    "<br>Exchange Rate : " +jo.get("userExchangeRates").toString()+
                    "<br>Paying Amount : USD "+formatter.format(confirmTotalAmountUSD);


           *//* Intent mIntent = new Intent(getApplicationContext(), ConfirmOrderActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("requestFrom", "Paypal");
            mBundle.putString("confirmOrderText", confirmOrderText);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);*//*
            Fragment fragment = new ConfirmOrderFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            infoBundle.putString("requestFrom", "CreditDebitCard");
            infoBundle.putString("confirmOrderText", confirmOrderText);
            fragment.setArguments(infoBundle);
            fragmentTransaction.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/


/*
    public Boolean validateFormData(){

        if(addPayProfile.isChecked() && !(paymentProfileNameWrapper.getEditText().getText().toString().length()>0)){
            errorMessage = "Please Enter Profile Name";
            return  false;
        }





        *//*if(selectedUserPayTypeIndex>0){
            errorMessage = "Please Select Payment Method";
            return  false;
        }*//*

        if(!agreePayAgrement.isChecked()){
            errorMessage = "Please Agree Payment Terms";
            return  false;
        }

        return true;

//        return  false;

    }*/
}
