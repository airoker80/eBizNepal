package com.paybyonline.ebiz.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.Model.TextViewStyling;
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
 * A simple {@link Fragment} subclass.
 */
public class SendMoneyConfirmFragment extends Fragment implements
        View.OnClickListener, OnClickListener {


    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
//    PboServerRequestHandler handler;

    TextView purpose;
    TextView sendTo;
    TextView sendReceiveCurrency;
    TextView walletSentAmount;
    TextView senderCharge;
    TextView receiverCharge;
    TextView exchangedReceivedAmt;
    TextView exchangeRateDetails;
    EditText sentRemarks;
    TextView sentMoneyWalletAfterCharge;
    TextView walletReceiveAmount;
    TextView receivedMoneyWalletAfterCharge;
    CheckBox payChargeMyselfCB;
    CheckBox receiveNetSendAmountCB;
    Button confirmSendMoneyBtn;

    String sendMoneyDetails = "";

    Double walletSentAmountVal = 0.00;
    Double walletSenderChargeVal = 0.00;
    Double walletReceiverChargeVal = 0.00;
    Double currencyConversionRate = 0.00;

    LinearLayout exchangeRateLL;

    DecimalFormat formatter = new DecimalFormat("0.00");

    String receiverUserId = "";
    String totalSentAmount = "";
    String eWalletPurposeDetails = "";
    String pspPurposeDetails = "";


    String rechargeTargetNumberErrorMsg = "";
    EditText confirmationPin;
    AlertDialog dialog;
    TextView pinRequest;
    String pinCodePresent = "NO";
    EditText pinEnterEditText;
    EditText pinReenterEditText;
    EditText enterPasswordEditText;
    Dialog pinRequestDialog;
    private RetrofitHelper retrofitHelper;

    public SendMoneyConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_money_confirm, container, false);
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);


        purpose = (TextView) view.findViewById(R.id.purpose);
        sendTo = (TextView) view.findViewById(R.id.sendTo);
        sendReceiveCurrency = (TextView) view.findViewById(R.id.sendReceiveCurrency);
        walletSentAmount = (TextView) view.findViewById(R.id.walletSentAmount);
        exchangeRateDetails = (TextView) view.findViewById(R.id.exchangeRateDetails);
        exchangedReceivedAmt = (TextView) view.findViewById(R.id.exchangedReceivedAmt);
        senderCharge = (TextView) view.findViewById(R.id.senderCharge);
        receiverCharge = (TextView) view.findViewById(R.id.receiverCharge);
        confirmSendMoneyBtn = (Button) view.findViewById(R.id.confirmSendMoneyBtn);
        exchangeRateLL = (LinearLayout) view.findViewById(R.id.exchangeRateLL);


        sentRemarks = (EditText) view.findViewById(R.id.sentRemarks);
        sentMoneyWalletAfterCharge = (TextView) view.findViewById(R.id.sentMoneyWalletAfterCharge);
        walletReceiveAmount = (TextView) view.findViewById(R.id.walletReceiveAmount);
        receivedMoneyWalletAfterCharge = (TextView) view.findViewById(R.id.receivedMoneyWalletAfterCharge);

        payChargeMyselfCB = (CheckBox) view.findViewById(R.id.payChargeMyselfCB);
        receiveNetSendAmountCB = (CheckBox) view.findViewById(R.id.receiveNetSendAmountCB);

        Bundle bundle = getArguments();
        sendMoneyDetails = bundle.getString("sendMoneyDetails");

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        try {

            JSONObject jsonObject = new JSONObject(sendMoneyDetails);
            receiverUserId = jsonObject.getString("receiverUserId");
            totalSentAmount = jsonObject.getString("totalSentAmount");
            eWalletPurposeDetails = jsonObject.getString("eWalletPurposeDetails");
            currencyConversionRate = Double.parseDouble(jsonObject.getString("currencyConversionRate"));
            if (currencyConversionRate != 1) {
                exchangeRateLL.setVisibility(View.VISIBLE);
            }
            walletSentAmountVal = Double.parseDouble(jsonObject.getString("walletSentAmount"));
            walletSenderChargeVal = Double.parseDouble(jsonObject.getString("walletSenderCharge"));
            walletReceiverChargeVal = Double.parseDouble(jsonObject.getString("walletReceiverCharge"));

            purpose.setText(jsonObject.getString("purposeName"));
            sendReceiveCurrency.setText(jsonObject.getString("senderCurrency") + " / " + jsonObject.getString("receiveCurrency"));
            sendTo.setText(jsonObject.getString("sendTo"));

            senderCharge.setText(formatter.format(walletSenderChargeVal) + " (Paid By Sender)");
            receiverCharge.setText(formatter.format(walletReceiverChargeVal) + " (Paid By Receiver)");
            walletSentAmount.setText(formatter.format(walletSentAmountVal));

            sentMoneyWalletAfterCharge.setText(formatter.format(Double.parseDouble(jsonObject.getString("walletSentAmtAfterCharge"))));
            walletReceiveAmount.setText(formatter.format(Double.parseDouble(jsonObject.getString("walletSentAmtAfterCharge"))));
            receivedMoneyWalletAfterCharge.setText(formatter.format(Double.parseDouble(jsonObject.getString("walletReceivedAmtAfterCharge"))));

            Double excRate = Double.parseDouble(jsonObject.getString("walletReceivedAmtAfterCharge")) * currencyConversionRate;
            exchangedReceivedAmt.setText(formatter.format(excRate));
            exchangeRateDetails.setText("Exchange rate @ 1 " + currencyConversionRate + " (" + jsonObject.getString("senderCurrency") + " / " + jsonObject.getString("receiveCurrency") + " )");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("error", e + "");
        }

        payChargeMyselfCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePriceDetails();
            }
        });
        receiveNetSendAmountCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updatePriceDetails();
            }
        });

        confirmSendMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionConfirmModel();
//                confirmSendMoney();

            }
        });

        return view;
    }

    public void confirmSendMoney() {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "saveSendMoneyDetails");
        params.put("confirmationPin", confirmationPin.getText()
                .toString());
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("purposeName", purpose.getText().toString());
        params.put("sendTo", sendTo.getText().toString());
        params.put("receiverUserId", receiverUserId);
        params.put("totalSentAmount", totalSentAmount);
        params.put("receiveRemarks", "");
        params.put("eWalletPurposeDetails", eWalletPurposeDetails);
        params.put("pspPurposeDetails", "");
        params.put("currencyConversionRate", currencyConversionRate.toString());
        params.put("sentRemarks", sentRemarks.getText().toString());
        params.put("walletSentAmount", String.valueOf(walletSentAmountVal));
        params.put("payChargeCheckbox", payChargeMyselfCB.isChecked() ? "on" : "");
        params.put("sendNetAmtCheckbox", receiveNetSendAmountCB.isChecked() ? "on" : "");
        params.put("walletSenderCharge", String.valueOf(walletSenderChargeVal));
        params.put("walletReceiverCharge", String.valueOf(walletReceiverChargeVal));
        params.put("sentMoneyWalletAfterCharge", sentMoneyWalletAfterCharge.getText().toString());
        params.put("walletReceiveAmount", walletReceiveAmount.getText().toString());
        params.put("receivedMoneyWalletAfterCharge", receivedMoneyWalletAfterCharge.getText().toString());

        Log.i("msgsss", "params " + params);

//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());

//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait...", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    userDeviceDetails.showToast(response.getString("msg"));
//                    if (response.getString("msgTitle").equals("Success")) {
//
//                        if (response.has("accountBalance")) {
//                            DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
//                            dashBoardActivity.updateUserBalance(response.getString("currency"), response.getString("accountBalance"), Double.parseDouble(response.getString("holdMoneyAmount")));
//                        }
//
//                        Intent intent = new Intent(getActivity(), DashBoardActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("defaultPage", "SendMoney");
//                        intent.putExtras(bundle);
//                        getActivity().finish();
//                        startActivity(intent);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    userDeviceDetails.showToast(jsonObject.getString("msg"));
                    if (jsonObject.getString("msgTitle").equals("Success")) {

                        if (jsonObject.has("accountBalance")) {
                            DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
                            dashBoardActivity.updateUserBalance(jsonObject.getString("currency"), jsonObject.getString("accountBalance"), Double.parseDouble(jsonObject.getString("holdMoneyAmount")));
                        }

                        Intent intent = new Intent(getActivity(), DashBoardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("defaultPage", "SendMoney");
                        intent.putExtras(bundle);
                        getActivity().finish();
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Send Money Confirm", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void updatePriceDetails() {

        if (payChargeMyselfCB.isChecked() && receiveNetSendAmountCB.isChecked()) {
            Double walletSentAmtAfterCharge = walletSentAmountVal + walletSenderChargeVal + walletReceiverChargeVal;
            sentMoneyWalletAfterCharge.setText(formatter.format(walletSentAmtAfterCharge));
            walletReceiveAmount.setText(formatter.format(walletSentAmountVal));
            receivedMoneyWalletAfterCharge.setText(formatter.format(walletSentAmountVal));
        } else if (payChargeMyselfCB.isChecked()) {       ////pay charge by myself only
            Double walletSentAmtAfterCharge = walletSentAmountVal - walletSenderChargeVal - walletReceiverChargeVal;
            sentMoneyWalletAfterCharge.setText(formatter.format(walletSentAmtAfterCharge));
            walletReceiveAmount.setText(formatter.format(walletSentAmtAfterCharge));
            receivedMoneyWalletAfterCharge.setText(formatter.format(walletSentAmtAfterCharge));
        } else if (receiveNetSendAmountCB.isChecked()) {      //receiver receives net sent amount
            Double walletSentAmtAfterCharge = walletSentAmountVal + walletSenderChargeVal;
            Double walletReceivedAmtAfterCharge = walletSentAmountVal - walletReceiverChargeVal;
            sentMoneyWalletAfterCharge.setText(formatter.format(walletSentAmtAfterCharge));
            walletReceiveAmount.setText(formatter.format(walletSentAmountVal));
            receivedMoneyWalletAfterCharge.setText(formatter.format(walletReceivedAmtAfterCharge));
        } else {
            Double walletSentAmtAfterCharge = walletSentAmountVal - walletSenderChargeVal;
            Double walletReceivedAmtAfterCharge = walletSentAmtAfterCharge - walletReceiverChargeVal;
            sentMoneyWalletAfterCharge.setText(formatter.format(walletSentAmtAfterCharge));
            walletReceiveAmount.setText(formatter.format(walletSentAmtAfterCharge));
            receivedMoneyWalletAfterCharge.setText(formatter.format(walletReceivedAmtAfterCharge));
        }

        Double totalWalletAmt = Double.parseDouble(receivedMoneyWalletAfterCharge.getText().toString());
        Double afterExchange = totalWalletAmt * currencyConversionRate;
        exchangedReceivedAmt.setText(formatter.format(afterExchange));

    }


    /////////////////////////////////////////Pin code confirmation////////////////////////////////////////////////////////
    public void transactionConfirmModel() {

        rechargeTargetNumberErrorMsg = "";
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.confirmation_pin_code_form,
                null);
        confirmationPin = (EditText) dialogview
                .findViewById(R.id.confirmationPin);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        dialog = dialogBuilder
                .setView(dialogview).setTitle("Transaction Confirmation")
                .setPositiveButton("Yes", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        pinRequest = (TextView) dialogview
                .findViewById(R.id.pinCodeRequest);
        TextView pinCodeHeading = (TextView) dialogview
                .findViewById(R.id.pinCodeHeading);

        pinCodePresent = ((DashBoardActivity) getActivity()).getPinCodeStatus();

        if (pinCodePresent.equals("YES")) {
            pinRequest.setVisibility(View.GONE);
            pinCodeHeading.setVisibility(View.GONE);

        } else {

            pinRequest.setVisibility(View.VISIBLE);
            pinCodeHeading.setVisibility(View.VISIBLE);
            pinRequest.setText(new TextViewStyling()
                    .textViewLink("Create Pin Code"));
            pinRequest.setTextColor(Color.parseColor("#5F86C4"));
            pinRequest.setOnClickListener(this);
        }

        Button yesBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        yesBtn.setOnClickListener(new CustomListener(dialog));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.pinCodeRequest:
                pinRequest();
                break;

        }
    }

    public void pinRequest() {

        dialog.dismiss();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.request_pin_layout, null);

        pinEnterEditText = (EditText) dialogview.findViewById(R.id.enterPin);
        pinReenterEditText = (EditText) dialogview.findViewById(R.id.reenterPin);

        enterPasswordEditText = (EditText) dialogview
                .findViewById(R.id.confirmpassword);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        AlertDialog pindialog = dialogBuilder
                .setView(dialogview).setTitle("Pin Code Request")
                .setPositiveButton("Save", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();
        pindialog.show();
        pindialog.setCanceledOnTouchOutside(true);

        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
        saveBtn.setOnClickListener(new CustomListener2(pindialog));

    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            String minLength = "4";
            if (minLength.equals(confirmationPin.getText().toString().length() + "")) {
                dialog.dismiss();
                confirmSendMoney();
            } else {
                confirmationPin.setError("4 Digit Transaction Pin Code Required");
            }

        }
    }


    class CustomListener2 implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener2(Dialog dialog) {
            this.dialog = dialog;
            pinRequestDialog = dialog;
        }

        @Override
        public void onClick(View v) {

            String minLength = "4";
            if (!minLength.equals(pinEnterEditText.getText().toString().length() + ""))
                pinEnterEditText.setError("4 Digit Transaction Pin Code Required");

            if (!minLength.equals(pinReenterEditText.getText().toString().length() + ""))
                pinReenterEditText
                        .setError("4 Digit Transaction Pin Code Required");
            if (enterPasswordEditText.getText().toString().isEmpty())
                enterPasswordEditText.setError("password required");
            if (pinEnterEditText.getText().toString().length() > 0
                    && pinReenterEditText.getText().toString().length() > 0
                    && enterPasswordEditText.getText().toString().length() > 0) {

                if (minLength.equals(pinEnterEditText.getText().toString()
                        .length()
                        + "")
                        && minLength.equals(pinReenterEditText.getText()
                        .toString().length()
                        + "")) {

                    if (pinEnterEditText.getText().toString()
                            .equals(pinReenterEditText.getText().toString())) {

                        try {

//                            RequestParams params = new RequestParams();
                            Map<String, String> params = new HashMap<>();
                            params.put("parentTask", "rechargeApp");
                            params.put("childTask", "saveUserPinCode");
                            params.put("userCode", myUserSessionManager.getSecurityCode());
                            params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
                            params.put("pin", pinEnterEditText.getText()
                                    .toString());
                            // Make Http call
//                            PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//                            handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                                    try {
//                                        JSONObject json = response;
//                                        if (json.getString("msgTitle").equals("FAILED")) {
//
//                                            if (json.getString("reason").equals("No Code")) {
//                                                userDeviceDetails.showToast(json.getString("msg"));
//                                            } else {
//                                                showMyAlertProgressDialog.showUserAlertDialog(
//                                                        json.getString("msg"), json.getString("msgTitle"));
//                                            }
//
//                                        } else {
//                                            if (pinRequestDialog != null) {
//                                                pinRequestDialog.dismiss();
//                                            }
//                                            userDeviceDetails.showToast(json.getString("msg"));
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                                }
//                            });
                            retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
                                @Override
                                public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                                    try {
                                        if (jsonObject.getString("msgTitle").equals("FAILED")) {
                                            if (jsonObject.getString("reason").equals("No Code")) {
                                                userDeviceDetails.showToast(jsonObject.getString("msg"));
                                            } else {
                                                showMyAlertProgressDialog.showUserAlertDialog(
                                                        jsonObject.getString("msg"), jsonObject.getString("msgTitle"));
                                            }
                                        } else {
                                            if (pinRequestDialog != null) {
                                                pinRequestDialog.dismiss();
                                            }
                                            userDeviceDetails.showToast(jsonObject.getString("msg"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onRetrofitFailure(String errorMessage, int apiCode) {

                                }
                            });
                            retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        pinEnterEditText.setError(" Pin not matched");
                    }

                } else {
                    pinEnterEditText
                            .setError(" 4 Digit Transaction Pin Code Required");
                }

            } else {
                enterPasswordEditText.setError("enter the data");
            }
        }
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

}
