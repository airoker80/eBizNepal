package com.paybyonline.ebiz.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.Model.TextViewStyling;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swatin on 11/15/2017.
 */

public class NeaBillFragmentNew extends Fragment implements DialogInterface.OnClickListener, RetrofitHelper.OnRetrofitResponse {

    private Button payNowBtn;
    private TextView customerId;
    private TextView customerName;
    private TextView officeId;
    private TextView officeName;
    private TextView scno;
    private TextView serviceCharge;
    private TextView serviceChargeValue;
    private TextView total;
    private View serviceChargeView;
    private EditText confirmationPinEditTxt;
    private AlertDialog dialog;
    private TextView pinRequest;
    private TextView pinCodeHeading;
    private String pinCodePresent;
    private EditText pinEnterEditText;
    private EditText pinReenterEditText;
    private EditText enterPasswordEditText;
    private MyUserSessionManager myUserSessionManager;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private Map<String, String> sendParams;
    private ImageView logo;
    private RetrofitHelper retrofitHelper;
    private LinearLayout monthsLayout, chargeLayout, amountLayout;
    private String currency = "Rs.";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nea_bill_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        payNowBtn = (Button) view.findViewById(R.id.payNow);
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionConfirmModel();
            }
        });
        logo = (ImageView) view.findViewById(R.id.logo);
        customerId = (TextView) view.findViewById(R.id.customerId);
        customerName = (TextView) view.findViewById(R.id.customerName);
        officeId = (TextView) view.findViewById(R.id.officeId);
        scno = (TextView) view.findViewById(R.id.scno);
//        serviceChargeView = view.findViewById(R.id.scPerView);
//        serviceCharge = (TextView) view.findViewById(R.id.sc);
//        serviceChargeValue = (TextView) view.findViewById(R.id.scAmount);
        total = (TextView) view.findViewById(R.id.totalPayment);
        monthsLayout = (LinearLayout) view.findViewById(R.id.monthsLayout);
        chargeLayout = (LinearLayout) view.findViewById(R.id.chargeLayout);
        amountLayout = (LinearLayout) view.findViewById(R.id.amountLayout);
        myUserSessionManager = new MyUserSessionManager(getActivity());
        parseBundleJson();
    }

    class MonthlyBill {

        private String billDate;
        private String monthName;
        private String noOfDays;
        private String payableAmount;
        private String status;

        public MonthlyBill(String billDate, String monthName, String noOfDays, String payableAmount, String status) {
            this.billDate = billDate;
            this.monthName = monthName;
            this.noOfDays = noOfDays;
            this.payableAmount = payableAmount;
            this.status = status;
        }

        public String getBillDate() {
            return billDate;
        }

        public String getMonthName() {
            return monthName;
        }

        public String getNoOfDays() {
            return noOfDays;
        }

        public String getPayableAmount() {
            return payableAmount;
        }

        public String getStatus() {
            return status;
        }
    }

    private void parseBundleJson() {
        try {
            JSONObject jsonObject = new JSONObject(getArguments().getString("formJson"));
            JSONObject billingData = jsonObject.getJSONObject("billingData");
            String customerIdVal = billingData.getString("consumerId");
            String customerNameVal = billingData.getString("customer-name");
            pinCodePresent = billingData.getString("pinCode");
            String officeIdVal = billingData.getString("officeCode");
            String subscriberNo = billingData.getString("subscriberNo");
            JSONObject commisionDetails = jsonObject.getJSONObject("commisionDetails");
            JSONObject showDiscount = commisionDetails.getJSONObject("showDiscount");
            JSONObject sCatSType = commisionDetails.getJSONObject("sCatSType");
            JSONObject serviceType = sCatSType.getJSONObject("serviceType");
            String serviceTypeId = serviceType.getString("id");
            JSONObject serviceCategory = sCatSType.getJSONObject("serviceCategory");
            String serviceCategoryId = serviceCategory.getString("id");
            boolean isPercent = showDiscount.getBoolean("isPercent");
            String serviceChargePercent = "";
            if (isPercent) {
                serviceChargePercent = showDiscount.getString("disPer");
            }
            String serviceChargeAmount = commisionDetails.getString("amount");
            String totalVal = commisionDetails.getString("netAmount");
            String logoName = PayByOnlineConfig.BASE_URL + "serviceCategoryServiceTypeLogo/" + sCatSType.getString("logoName");

            JSONArray billInfo = jsonObject.getJSONArray("billInfo");
            List<MonthlyBill> monthlyBillList = new ArrayList<>();
            for (int i = 0; i < billInfo.length(); i++) {
                JSONObject monthBill = billInfo.getJSONObject(i);
                String status = monthBill.getString("Status");
                String payableAmount = monthBill.getString("Payable amount");
                String noOfDays = monthBill.getString("No of days");
                String monthName = monthBill.getString("Due bill of");
                if (monthName.toLowerCase().trim().equals("has old bills")) {
                    monthName = "Old Bill";
                }
                monthName = monthName.replace("\\", "");
                monthName = monthName.replace("/", " ");
                String billDate = monthBill.getString("Bill date");
                MonthlyBill monthlyBill = new MonthlyBill(billDate, monthName, noOfDays, payableAmount, status);
                monthlyBillList.add(monthlyBill);
            }

            sendParams = new HashMap<>();
            if (getActivity() instanceof DashBoardActivity) {
                sendParams.put("currency", ((DashBoardActivity) getActivity()).getUserCurrency());
            }
            sendParams.put("discountAmount", serviceChargeAmount);
            if (isPercent) {
                sendParams.put("discountPercent", serviceChargePercent);
            }
            sendParams.put("customerID", customerIdVal);
            sendParams.put("sCategory", serviceCategoryId);
            sendParams.put("serviceType", serviceTypeId);
            sendParams.put("scno", subscriberNo);
            sendParams.put("officeCode", officeIdVal);
            sendParams.put("purchasedValue", totalVal);
            sendParams.put("paidAmount", totalVal);
            sendParams.put("serviceCharge", "0");
            sendParams.put("rechargeNumber", "Scno:" + subscriberNo + " (CusId:" + customerIdVal + ")");

            Picasso.with(getContext())
                    .load(logoName)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(logo);
            customerId.setText(customerIdVal);
            customerName.setText(customerNameVal);
            officeId.setText(officeIdVal);
            scno.setText(subscriberNo);
//            if (isPercent) {
//                serviceCharge.setText(serviceChargePercent);
//            } else {
//                serviceChargeView.setVisibility(View.GONE);
//            }
//            serviceChargeValue.setText(serviceChargeAmount);
            total.setText(currency + totalVal);
            addChartViews(true, null);
            for (MonthlyBill monthlyBill : monthlyBillList) {
                addChartViews(false, monthlyBill);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void addChartViews(boolean isTitle, MonthlyBill monthlyBill) {

        TextView monthNameTv = new TextView(getActivity());
        monthNameTv.setGravity(Gravity.CENTER);
        monthNameTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        monthNameTv.setBackgroundResource(R.drawable.box_grey_bottom_only);
        monthNameTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(50)));
        if (isTitle) {
            monthNameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColorPrimary));
            monthNameTv.setText("Month");
            monthNameTv.setTypeface(monthNameTv.getTypeface(), Typeface.BOLD);
        } else {
            monthNameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
            monthNameTv.setText(monthlyBill.getMonthName());
        }
        monthsLayout.addView(monthNameTv);

        TextView chargeTv = new TextView(getActivity());
        chargeTv.setGravity(Gravity.CENTER);
        chargeTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        chargeTv.setBackgroundResource(R.drawable.box_grey_bottom_only);
        chargeTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(50)));
        if (isTitle) {
            chargeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColorPrimary));
            chargeTv.setText("Charges");
            chargeTv.setTypeface(chargeTv.getTypeface(), Typeface.BOLD);
        } else {
            chargeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
            chargeTv.setText(monthlyBill.getStatus());
        }
        chargeLayout.addView(chargeTv);

        TextView amountTv = new TextView(getActivity());
        amountTv.setGravity(Gravity.CENTER);
        amountTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        amountTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
        amountTv.setBackgroundResource(R.drawable.box_grey_bottom_only);
        amountTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(50)));
        if (isTitle) {
            amountTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColorPrimary));
            amountTv.setText("Amount");
            amountTv.setTypeface(amountTv.getTypeface(), Typeface.BOLD);
        } else {
            amountTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
            amountTv.setText(currency + monthlyBill.getPayableAmount());
        }
        amountLayout.addView(amountTv);
    }

    public void transactionConfirmModel() {
        // rechargeTargetNumberErrorMsg="";

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.confirmation_pin_code_form, null);
        confirmationPinEditTxt = (EditText) dialogview
                .findViewById(R.id.confirmationPin);
        Log.i("msgss", "transactionConfirm");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialog = dialogBuilder
                // .setMessage(ss)
                // .setIcon(R.drawable.ic_launcher)
                .setView(dialogview).setTitle("Transaction Confirmation")
                .setPositiveButton("Yes", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        pinRequest = (TextView) dialogview
                .findViewById(R.id.pinCodeRequest);
        pinCodeHeading = (TextView) dialogview
                .findViewById(R.id.pinCodeHeading);

        if (pinCodePresent.equals("Yes")) {

            Log.i("msgss", "transactionConfirm");
            pinRequest.setVisibility(View.GONE);
            pinCodeHeading.setVisibility(View.GONE);

        } else {

            pinRequest.setVisibility(View.VISIBLE);
            pinCodeHeading.setVisibility(View.VISIBLE);
            pinRequest.setText(new TextViewStyling()
                    .textViewLink("Create Pin Code "));
            pinRequest.setTextColor(Color.parseColor("#5F86C4"));
            pinRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pinRequest();
                }
            });

        }


        Button yesBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        yesBtn.setOnClickListener(new CustomListener(dialog));
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
        try {
            Log.i("ListresponseT", apiCode + ":: response");
            handleSendFieldContentResponse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRetrofitFailure(String errorMessage, int apiCode) {
        Log.d("NeaBillFragmentNew", errorMessage);
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            // Do whatever you want here
            // If you want to close the dialog, uncomment the line below
            String minLength = "4";
            if (minLength.equals(confirmationPinEditTxt.getText().toString().length()
                    + "")) {
                dialog.dismiss();
                // for hiding keyboard on button click
                // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                sendFieldContent();
                //then send back to byproduct
            } else {
                confirmationPinEditTxt.setError("4 Digit Transaction Pin Code Required");
            }

        }
    }

    public void sendFieldContent() {

        // Make Http call
        sendParams.put("parentTask", "rechargeApp");
//         sendParams.put("childTask", "saveRecharge");
        sendParams.put("childTask", "performRecharge");
//        sendParams.put("childTask", "saveRecharge");
        sendParams.put("userCode", myUserSessionManager.getSecurityCode());
        sendParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        sendParams.put("confirmationPin", confirmationPinEditTxt.getText().toString());
        // String url="http://192.168.1.52:8080/MerchantPlugin/serviceRecharge/saveRecharge";
        Log.i("params", "" + sendParams.toString());
        retrofitHelper.setOnResponseListener(this);
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, sendParams);
    }

    public void pinRequest() {

        dialog.dismiss();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.request_pin_layout, null);
        pinEnterEditText = (EditText) dialogview.findViewById(R.id.enterPin);
        pinReenterEditText = (EditText) dialogview
                .findViewById(R.id.reenterPin);
        enterPasswordEditText = (EditText) dialogview
                .findViewById(R.id.confirmpassword);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        AlertDialog pindialog = dialogBuilder
                // .setMessage(ss)
                // .setIcon(R.drawable.ic_launcher)
                .setView(dialogview).setTitle("Pin Code Request")
                .setPositiveButton("Save", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();
        pindialog.show();
        pindialog.setCanceledOnTouchOutside(true);

        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
        saveBtn.setOnClickListener(new CustomListener2(pindialog));

    }

    class CustomListener2 implements View.OnClickListener {

        private final Dialog dialog;

        public CustomListener2(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            // Do whatever you want here
            // If you want to close the dialog, uncomment the line below
            String minLength = "4";

            if (!minLength.equals(pinEnterEditText.getText().toString()
                    .length()
                    + ""))
                pinEnterEditText
                        .setError("4 Digit Transaction Pin Code Required");

            if (!minLength.equals(pinReenterEditText.getText().toString()
                    .length()
                    + ""))
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

						/*
                         * new HttpAsyncTaskSaveRechargePayDetails().execute(url
						 * + "/saveRecharge");
						 */
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
                            retrofitHelper.setOnResponseListener(NeaBillFragmentNew.this);
                            retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        pinEnterEditText.setError("Pin not matched");
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

    public void handleSendFieldContentResponse(JSONObject response) throws JSONException {

        try {

            JSONObject json = response;

            if (json.getString("msgTitle").equals("Failed")) {


                if (json.has("reason")) {
                    if (json.getString("reason").equals("No Code")) {
                        showUserAlertDialogWithView(json.getString("msg"),
                                "msgTitle");
                    } else {

                        showMyAlertProgressDialog.showUserAlertDialog(
                                json.getString("msg"), json.getString("msgTitle"));
                    }
                } else {

                    showMyAlertProgressDialog.showUserAlertDialog(
                            json.getString("msg"), json.getString("msgTitle"));
                }

            } else {
                if (response.has("accountBalance")) {
                    DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
                    dashBoardActivity.updateUserBalance(response.getString("currency"), response.getString("accountBalance"), Double.parseDouble(response.getString("holdMoneyAmount")));
                }
                showMyAlertProgressDialog.showUserAlertDialog(
                        json.getString("msg"), json.getString("msgTitle"));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(this);
                fragmentTransaction.commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void showUserAlertDialogWithView(String message, String title) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.redirect_website_message,
                null);
        TextView redirectMsg = (TextView) dialogview
                .findViewById(R.id.redirectMsg);

        redirectMsg.setText(message);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        final AlertDialog pindialog = dialogBuilder.setTitle(title)
                .setView(dialogview).setPositiveButton("Request Pin", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();

        pindialog.show();
        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pindialog.dismiss();

                pinRequest();
            }
        });

    }
}
