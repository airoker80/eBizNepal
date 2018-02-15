//package com.paybyonline.ebiz.Fragment;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.loopj.android.http.RequestParams;
//import com.paybyonline.ebiz.Activity.DashBoardActivity;
//import com.paybyonline.ebiz.Adapter.Model.TextViewStyling;
//import com.paybyonline.ebiz.R;
//import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
//import com.paybyonline.ebiz.serverdata.ApiIndex;
//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;
//import com.paybyonline.ebiz.serverdata.RetrofitHelper;
//import com.paybyonline.ebiz.usersession.MyUserSessionManager;
//import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import cz.msebera.android.httpclient.Header;
//
///**
// * Created by Swatin on 11/15/2017.
// */
//
//public class NeaBillFragment extends Fragment implements DialogInterface.OnClickListener {
//
//    private Button payNowBtn;
//    private TextView customerId;
//    private TextView customerName;
//    private TextView officeId;
//    private TextView officeName;
//    private TextView scno;
//    private TextView serviceCharge;
//    private TextView serviceChargeValue;
//    private TextView total;
//    private View serviceChargeView;
//    private EditText confirmationPinEditTxt;
//    private AlertDialog dialog;
//    private TextView pinRequest;
//    private TextView pinCodeHeading;
//    private String pinCodePresent;
//    private EditText pinEnterEditText;
//    private EditText pinReenterEditText;
//    private EditText enterPasswordEditText;
//    private MyUserSessionManager myUserSessionManager;
//    private CoordinatorLayout coordinatorLayout;
//    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
//    private RequestParams sendParams;
//    private ImageView logo;
//    private RetrofitHelper retrofitHelper;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_nea_bill, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
//        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
//        retrofitHelper = new RetrofitHelper();
//        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
//        payNowBtn = (Button) view.findViewById(R.id.payNow);
//        payNowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                transactionConfirmModel();
//            }
//        });
//        logo = (ImageView) view.findViewById(R.id.logo);
//        customerId = (TextView) view.findViewById(R.id.customerId);
//        customerName = (TextView) view.findViewById(R.id.customerName);
//        officeId = (TextView) view.findViewById(R.id.officeId);
//        scno = (TextView) view.findViewById(R.id.scno);
//        serviceChargeView = view.findViewById(R.id.scPerView);
//        serviceCharge = (TextView) view.findViewById(R.id.sc);
//        serviceChargeValue = (TextView) view.findViewById(R.id.scAmount);
//        total = (TextView) view.findViewById(R.id.totalPayment);
//        myUserSessionManager = new MyUserSessionManager(getActivity());
//        parseBundleJson();
//    }
//
//    private void parseBundleJson() {
//        try {
//            JSONObject jsonObject = new JSONObject(getArguments().getString("formJson"));
//            JSONObject billingData = jsonObject.getJSONObject("billingData");
//            String customerIdVal = billingData.getString("consumerId");
//            String customerNameVal = billingData.getString("customer-name");
//            pinCodePresent = billingData.getString("pinCode");
//            String officeIdVal = billingData.getString("officeCode");
//            String subscriberNo = billingData.getString("subscriberNo");
//            JSONObject commisionDetails = jsonObject.getJSONObject("commisionDetails");
//            JSONObject showDiscount = commisionDetails.getJSONObject("showDiscount");
//            JSONObject sCatSType = commisionDetails.getJSONObject("sCatSType");
//            JSONObject serviceType = sCatSType.getJSONObject("serviceType");
//            String serviceTypeId = serviceType.getString("id");
//            JSONObject serviceCategory = sCatSType.getJSONObject("serviceCategory");
//            String serviceCategoryId = serviceCategory.getString("id");
//            boolean isPercent = showDiscount.getBoolean("isPercent");
//            String serviceChargePercent = "";
//            if (isPercent) {
//                serviceChargePercent = showDiscount.getString("disPer");
//            }
//            String serviceChargeAmount = commisionDetails.getString("amount");
//            String totalVal = commisionDetails.getString("netAmount");
//            String logoName = PayByOnlineConfig.BASE_URL + "serviceCategoryServiceTypeLogo/" + sCatSType.getString("logoName");
//
//            sendParams = new RequestParams();
//            if (getActivity() instanceof DashBoardActivity) {
//                sendParams.put("currency", ((DashBoardActivity) getActivity()).getUserCurrency());
//            }
//            sendParams.put("discountAmount", serviceChargeAmount);
//            if (isPercent) {
//                sendParams.put("discountPercent", serviceChargePercent);
//            }
//            sendParams.put("customerId", customerIdVal);
//            sendParams.put("sCategory", serviceCategoryId);
//            sendParams.put("serviceType", serviceTypeId);
//            sendParams.put("scno", subscriberNo);
//            sendParams.put("officeCode", officeIdVal);
//            sendParams.put("purchasedValue", totalVal);
//            sendParams.put("paidAmount", totalVal);
//            sendParams.put("serviceCharge", "0");
//            sendParams.put("rechargeNumber", "Scno:" + subscriberNo + " (CusId:" + customerIdVal + ")");
//
//            Picasso.with(getContext())
//                    .load(logoName)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .into(logo);
//            customerId.setText(customerIdVal);
//            customerName.setText(customerNameVal);
//            officeId.setText(officeIdVal);
//            scno.setText(subscriberNo);
//            if (isPercent) {
//                serviceCharge.setText(serviceChargePercent);
//            } else {
//                serviceChargeView.setVisibility(View.GONE);
//            }
//            serviceChargeValue.setText(serviceChargeAmount);
//            total.setText(totalVal);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void transactionConfirmModel() {
//        // rechargeTargetNumberErrorMsg="";
//
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View dialogview = inflater.inflate(R.layout.confirmation_pin_code_form, null);
//        confirmationPinEditTxt = (EditText) dialogview
//                .findViewById(R.id.confirmationPin);
//        Log.i("msgss", "transactionConfirm");
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        dialog = dialogBuilder
//                // .setMessage(ss)
//                // .setIcon(R.drawable.ic_launcher)
//                .setView(dialogview).setTitle("Transaction Confirmation")
//                .setPositiveButton("Yes", this)
//                .setNegativeButton("Cancel", this).setCancelable(false)
//                .create();
//        dialog.show();
//        dialog.setCanceledOnTouchOutside(true);
//
//        pinRequest = (TextView) dialogview
//                .findViewById(R.id.pinCodeRequest);
//        pinCodeHeading = (TextView) dialogview
//                .findViewById(R.id.pinCodeHeading);
//
//        if (pinCodePresent.equals("YES")) {
//
//            Log.i("msgss", "transactionConfirm");
//            pinRequest.setVisibility(View.GONE);
//            pinCodeHeading.setVisibility(View.GONE);
//
//        } else {
//
//            pinRequest.setVisibility(View.VISIBLE);
//            pinCodeHeading.setVisibility(View.VISIBLE);
//            pinRequest.setText(new TextViewStyling()
//                    .textViewLink("Create Pin Code "));
//            pinRequest.setTextColor(Color.parseColor("#5F86C4"));
//            pinRequest.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    pinRequest();
//                }
//            });
//
//        }
//
//
//        Button yesBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        yesBtn.setOnClickListener(new CustomListener(dialog));
//    }
//
//    @Override
//    public void onClick(DialogInterface dialogInterface, int i) {
//
//    }
//
//    class CustomListener implements View.OnClickListener {
//        private final Dialog dialog;
//
//        public CustomListener(Dialog dialog) {
//            this.dialog = dialog;
//        }
//
//        @Override
//        public void onClick(View v) {
//
//            // Do whatever you want here
//            // If you want to close the dialog, uncomment the line below
//            String minLength = "4";
//            if (minLength.equals(confirmationPinEditTxt.getText().toString().length()
//                    + "")) {
//                dialog.dismiss();
//                // for hiding keyboard on button click
//                // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                sendFieldContent();
//                //then send back to byproduct
//            } else {
//                confirmationPinEditTxt.setError("4 Digit Transaction Pin Code Required");
//            }
//
//        }
//    }
//
//    public void sendFieldContent() {
//
//        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,
//                getActivity());
//        sendParams.put("parentTask", "rechargeApp");
////         sendParams.put("childTask", "saveRecharge");
//        sendParams.put("childTask", "performRecharge");
////        sendParams.put("childTask", "saveRecharge");
//        sendParams.put("userCode", myUserSessionManager.getSecurityCode());
//        sendParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        sendParams.put("confirmationPin", confirmationPinEditTxt.getText().toString());
//        // String url="http://192.168.1.52:8080/MerchantPlugin/serviceRecharge/saveRecharge";
//        Log.i("params", "" + sendParams.toString());
//        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "Processing...", sendParams,
//                new PboServerRequestListener() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//
//                            Log.i("ListresponseT", "response");
//
//                            handleSendFieldContentResponse(response);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.e("exp", e + "");
//
//                        }
//
//                    }
//                });
//    }
//
//    public void pinRequest() {
//
//        dialog.dismiss();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View dialogview = inflater.inflate(R.layout.request_pin_layout, null);
//        pinEnterEditText = (EditText) dialogview.findViewById(R.id.enterPin);
//        pinReenterEditText = (EditText) dialogview
//                .findViewById(R.id.reenterPin);
//        enterPasswordEditText = (EditText) dialogview
//                .findViewById(R.id.confirmpassword);
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
//                getActivity());
//        AlertDialog pindialog = dialogBuilder
//                // .setMessage(ss)
//                // .setIcon(R.drawable.ic_launcher)
//                .setView(dialogview).setTitle("Pin Code Request")
//                .setPositiveButton("Save", this)
//                .setNegativeButton("Cancel", this).setCancelable(false)
//                .create();
//        pindialog.show();
//        pindialog.setCanceledOnTouchOutside(true);
//
//        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        saveBtn.setOnClickListener(new CustomListener2(pindialog));
//
//    }
//
//    class CustomListener2 implements View.OnClickListener {
//
//        private final Dialog dialog;
//
//        public CustomListener2(Dialog dialog) {
//            this.dialog = dialog;
//        }
//
//        @Override
//        public void onClick(View v) {
//
//            // Do whatever you want here
//            // If you want to close the dialog, uncomment the line below
//            String minLength = "4";
//
//            if (!minLength.equals(pinEnterEditText.getText().toString()
//                    .length()
//                    + ""))
//                pinEnterEditText
//                        .setError("4 Digit Transaction Pin Code Required");
//
//            if (!minLength.equals(pinReenterEditText.getText().toString()
//                    .length()
//                    + ""))
//                pinReenterEditText
//                        .setError("4 Digit Transaction Pin Code Required");
//
//            if (enterPasswordEditText.getText().toString().isEmpty())
//                enterPasswordEditText.setError("password required");
//
//            if (pinEnterEditText.getText().toString().length() > 0
//                    && pinReenterEditText.getText().toString().length() > 0
//                    && enterPasswordEditText.getText().toString().length() > 0) {
//
//                if (minLength.equals(pinEnterEditText.getText().toString()
//                        .length()
//                        + "")
//                        && minLength.equals(pinReenterEditText.getText()
//                        .toString().length()
//                        + "")) {
//
//                    if (pinEnterEditText.getText().toString()
//                            .equals(pinReenterEditText.getText().toString())) {
//
//						/*
//                         * new HttpAsyncTaskSaveRechargePayDetails().execute(url
//						 * + "/saveRecharge");
//						 */
//                        try {
//
//                            RequestParams params = new RequestParams();
//                            params.put("parentTask", "rechargeApp");
//                            params.put("childTask", "saveUserPinCode");
//                            params.put("userCode", myUserSessionManager.getSecurityCode());
//                            params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//                            params.put("pin", pinEnterEditText.getText()
//                                    .toString());
//                            // Make Http call
//                            PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//                            handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Saving Pin Details", params,
//                                    new PboServerRequestListener() {
//                                        @Override
//                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                                            try {
//                                                handleSendFieldContentResponse(response);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                                        }
//                                    });
//
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        pinEnterEditText.setError("Pin not matched");
//                    }
//
//                } else {
//                    pinEnterEditText
//                            .setError(" 4 Digit Transaction Pin Code Required");
//                }
//
//            } else {
//                enterPasswordEditText.setError("enter the data");
//            }
//        }
//    }
//
//    public void handleSendFieldContentResponse(JSONObject response) throws JSONException {
//
//        try {
//
//            JSONObject json = response;
//
//            if (json.getString("msgTitle").equals("Failed")) {
//
//
//                if (json.has("reason")) {
//                    if (json.getString("reason").equals("No Code")) {
//                        showUserAlertDialogWithView(json.getString("msg"),
//                                "msgTitle");
//                    } else {
//
//                        showMyAlertProgressDialog.showUserAlertDialog(
//                                json.getString("msg"), json.getString("msgTitle"));
//                    }
//                } else {
//
//                    showMyAlertProgressDialog.showUserAlertDialog(
//                            json.getString("msg"), json.getString("msgTitle"));
//                }
//
//            } else {
//                if (response.has("accountBalance")) {
//                    DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
//                    dashBoardActivity.updateUserBalance(response.getString("currency"), response.getString("accountBalance"), Double.parseDouble(response.getString("holdMoneyAmount")));
//                }
//                showMyAlertProgressDialog.showUserAlertDialog(
//                        json.getString("msg"), json.getString("msgTitle"));
////                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_LONG).show();
//                Fragment fragment = new BuyProductFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame, fragment);
//                fragmentTransaction.commit();
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public void showUserAlertDialogWithView(String message, String title) {
//
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View dialogview = inflater.inflate(R.layout.redirect_website_message,
//                null);
//        TextView redirectMsg = (TextView) dialogview
//                .findViewById(R.id.redirectMsg);
//
//        redirectMsg.setText(message);
//
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
//                getActivity());
//        final AlertDialog pindialog = dialogBuilder.setTitle(title)
//                .setView(dialogview).setPositiveButton("Request Pin", this)
//                .setNegativeButton("Cancel", this).setCancelable(false)
//                .create();
//
//        pindialog.show();
//        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                pindialog.dismiss();
//
//                pinRequest();
//            }
//        });
//
//    }
//}
