//package com.paybyonline.ebiz.Fragment;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.paybyonline.ebiz.Activity.DashBoardActivity;
//import com.paybyonline.ebiz.Activity.PboWebViewActivity;
//import com.paybyonline.ebiz.R;
//import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
//import com.paybyonline.ebiz.serverdata.ApiIndex;
//import com.paybyonline.ebiz.serverdata.RetrofitHelper;
//import com.paybyonline.ebiz.usersession.MyUserSessionManager;
//import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
//import com.paybyonline.ebiz.usersession.UserDeviceDetails;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
//import inficare.net.sctlib.SCTPaymentActivity;
//import inficare.net.sctlib.StaticVariables;
//
////import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
////import com.paybyonline.ebiz.serverdata.PboServerRequestListener;
//
///**
// * Created by Swatin on 10/18/2016.
// */
//
//public class ConfirmOrderFragmentNew extends Fragment implements DialogInterface.OnClickListener {
//
//
//    AlertDialog.Builder alertDialog;
//    private MyUserSessionManager myUserSessionManager;
//    private UserDeviceDetails userDeviceDetails;
//    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
//    private HashMap<String, String> userDetails;
//    private static String url;
//    FragmentManager myFragmentManagerSupport;
//    CoordinatorLayout coordinatorLayout;
//    String msgTitle = "";
//    String requestFrom;
//    String bankName;
//    Bundle bundleData;
//    private static final String PAYPAL_TAG = PayByOnlineConfig.PAYPAL_TAG;
//    private static final String CONFIG_ENVIRONMENT = PayByOnlineConfig.PAYPAL_ENVIRONMENT;
//    private static final String CONFIG_CLIENT_ID = PayByOnlineConfig.PAYPAL_CLIENT_ID;
//    private static final int REQUEST_CODE_PAYMENT = 10;
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(CONFIG_ENVIRONMENT)
//            .clientId(CONFIG_CLIENT_ID);
//    Bundle bundlePayPalData = new Bundle();
//    private static final int SCT_REQUEST_CODE = 0;
//    private RetrofitHelper retrofitHelper;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);
//        // View view= inflater.inflate(R.layout.payment_details, container, false);
//
//        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
//        myFragmentManagerSupport = getActivity().getSupportFragmentManager();
//        userDeviceDetails = new UserDeviceDetails(getActivity());
//        myUserSessionManager = new MyUserSessionManager(getActivity());
//        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
//        userDetails = myUserSessionManager.getUserDetails();
//        url = PayByOnlineConfig.SERVER_URL;
//
//        retrofitHelper = new RetrofitHelper();
//        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
//
//
//        //  Bundle   requestFromBundle = getArguments();
//        bundleData = getArguments();
//        Log.i("bundleData", "" + bundleData);
//        if (null != bundleData) {
//            //Null Checking
//
//            requestFrom = bundleData.getString("requestFrom");
//            bankName = bundleData.getString("bankName");
//
//            ((DashBoardActivity) getActivity()).setTitle("Confirmation");
//
//            String confirmOrderText = bundleData.getString("confirmOrderText");
//
//            FrameLayout myLayout = (FrameLayout) view.findViewById(R.id.confirmPaymentForm);
//            @SuppressLint("RestrictedApi") View paymentView = getLayoutInflater(bundleData).inflate(R.layout.confirm_payment_form_credit_debit,
//                    myLayout, false);
//
//            TextView payDetails = (TextView) paymentView.findViewById(R.id.confirmPayDetails);
//            payDetails.setText(Html.fromHtml(confirmOrderText));
//
//            Button confirmOrderBtn = (Button) paymentView.findViewById(R.id.confirmOrderBtn);
//            confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (requestFrom.equals("Paypal")) {
//
//                        createUserPaymentServer();
//
//                    } else if (requestFrom.equals("OnlineBanking") && !bankName.equals("")) {
//
//                        createUserPaymentServerNibl(bankName);
//
//                    } else if (requestFrom.equals("AtmBanking")) {
//
//                        createUserPaymentSct();
//
//                    } else {
//
//                        addMoneyWalletServer();
//                    }
//
//                }
//            });
//
//            myLayout.addView(paymentView);
//
//
//        }
//        return view;
//    }
//
//    public void createUserPaymentServerNibl(String bankName) {
//        Intent intent = new Intent(getActivity(), PboWebViewActivity.class);
//        bundleData.putString("userIp", userDeviceDetails.getLocalIpAddress());
//        bundleData.putString("webViewPage", "Online Banking");
//        //bundleData.putString("pageTitle","NIBL Checkout");
//
//        bundleData.putString("pageTitle", bankName);
//        intent.putExtras(bundleData);
//        startActivity(intent);
//        //startActivity(new Intent(getActivity(),OnlineBankingActivity.class));
//    }
//
//    public void createUserPaymentServer() {
//
////        RequestParams finalPayParams = new RequestParams();
//        Map<String, String> finalPayParams = new HashMap<>();
//        finalPayParams.put("parentTask", "rechargeApp");
//        finalPayParams.put("childTask", "createUserPaymentDetailsPaypal");
//        finalPayParams.put("userCode", myUserSessionManager.getSecurityCode());
//        finalPayParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        finalPayParams.put("payUsingIds", bundleData.getString("payUsingIds"));
//        finalPayParams.put("confirmPayUsings", bundleData.getString("confirmPayUsings"));
//        finalPayParams.put("confirmPaymentNotes", bundleData.getString("confirmPaymentNotes"));
//        finalPayParams.put("confirmPurchasedAmount", bundleData.getString("confirmPurchasedAmount"));
//        finalPayParams.put("confirmDiscountAmount", bundleData.getString("confirmDiscountAmount"));
//        finalPayParams.put("confirmDepositingAmount", bundleData.getString("confirmDepositingAmount"));
//        finalPayParams.put("confirmPayingAmount", bundleData.getString("confirmPayingAmount"));
//        finalPayParams.put("totalAmt", bundleData.getString("totalAmt"));
//        finalPayParams.put("payAmt", bundleData.getString("payAmt"));
//        finalPayParams.put("addDiscountWallet", bundleData.getString("addDiscountWallet"));
//        finalPayParams.put("confirmPaymentProfileNames", bundleData.getString("confirmPaymentProfileNames"));
//        finalPayParams.put("payUsingIds", bundleData.getString("payUsingIds"));
//        finalPayParams.put("userIp", userDeviceDetails.getLocalIpAddress());
//
//        // Make Http call
////        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
////        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
////                finalPayParams, new PboServerRequestListener() {
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////
////                        try {
////                            handleCreateUserPaymentServer(response);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
////                    }
////                });
//        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
//            @Override
//            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
//                try {
//                    handleCreateUserPaymentServer(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrofitFailure(String errorMessage, int apiCode) {
//                Log.d("ConfirmOrderFragmentNew", errorMessage);
//            }
//        });
//        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, finalPayParams, null, null);
//    }
//
//    public void handleCreateUserPaymentServer(JSONObject response) throws JSONException {
//
//        JSONObject jo = null;
//        try {
//
//            jo = response;
//            String saveStatus = jo.getString("saveStatus");
//
//            if (saveStatus.equals("Success")) {
//
//                bundlePayPalData = new Bundle();
//                bundlePayPalData.putString("purchasedAmt", jo.get("purchasedAmt").toString());
//                bundlePayPalData.putString("pAmt", jo.get("pAmt").toString());
//                bundlePayPalData.putString("payOptionId", jo.get("payOptionId").toString());
//                bundlePayPalData.putString("transactionId", jo.get("transactionId").toString());
//                bundlePayPalData.putString("paidFrom", jo.get("paidFrom").toString());
//                bundlePayPalData.putString("payTypeId", jo.get("payTypeId").toString());
//                bundlePayPalData.putString("addDiscountWallet", jo.get("addDiscountWallet").toString());
//                bundlePayPalData.putString("paypalNetPayFinal", jo.get("paypalNetPayFinal").toString());
//
//                callPaypalActivityForPayment(jo.get("paypalNetPayFinal").toString());
//
//            } else {
//                userDeviceDetails
//                        .showToast("Some Error Occurred");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void callPaypalActivityForPayment(String paypalNetPayFinal) {
//
//        SharedPreferences finalPayData = getActivity().getSharedPreferences("PAYMENTDETAILSFINAL", 0);
//
//        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(paypalNetPayFinal),
//                PayByOnlineConfig.DEFAULT_CURRENCY, "Wallet Payment",
//                PayByOnlineConfig.PAYMENT_INTENT);
//
//        Intent intent = new Intent(getActivity(), PaymentActivity.class);
//
//
//        // send the same configuration for restart resiliency
//
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
////        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//        ConfirmOrderFragmentNew.this.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//
////        getActivity().finish();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("msgsss", "onActivityResult ------------ " + resultCode);
//        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm =
//                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//
//                if (confirm != null) {
//
//                    try {
//
//                        Log.i(PAYPAL_TAG, confirm.toJSONObject().toString(4));
//                        Log.i(PAYPAL_TAG, confirm.getPayment().toJSONObject().toString(4));
//
//                        /**
//                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
//                         * or consent completion.
//                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
//                         * for more details.
//                         *
//                         * For sample mobile backend interactions, see
//                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
//                         */
//
//
//                        JSONObject result = confirm.toJSONObject();
//                        JSONObject paypalResponse = result.getJSONObject("response");
//
//                        String paypalPaymentId = paypalResponse.getString("id");
//                        String approved = paypalResponse.getString("state");
//
//                        if (approved.equals("approved")) {
//
//                            updateUserPaymentDetailsPaypal(paypalPaymentId);
//
//                        } else {
//
//                            Toast.makeText(getContext().getApplicationContext(),
//                                    "Payment Failed", Toast.LENGTH_LONG)
//                                    .show();
//                        }
//
//
//                    } catch (JSONException e) {
//                        Log.e(PAYPAL_TAG, "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i(PAYPAL_TAG, "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i(
//                        PAYPAL_TAG,
//                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        } else if (requestCode == SCT_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Log.d("enter", "entered..");
//                String status = data.getStringExtra(StaticVariables.STATUS_CODE);
//                String msg = data.getStringExtra(StaticVariables.MESSAGE);
//                String ref = data.getStringExtra(StaticVariables.GTWREFNO);
//                String tnx = data.getStringExtra(StaticVariables.MERCHANT_TNX_ID);
//
//                Log.d("result --", status + "---" + msg + " -" + ref + "--" + tnx);
//                if (status.equals("0") && msg.equals("Process Completed. Check the transaction status in your own system.")) {
//                    confirmSCTPayment(status, msg, ref, tnx);
//                }
//
//            }
//        }
//    }
//
//    public void updateUserPaymentDetailsPaypal(String paypalPaymentId) {
//
//
////       userDeviceDetails.showToast("updateUserPaymentDetailsPaypal");
////       userDeviceDetails.showToast("paypalPaymentId "+paypalPaymentId);
//
//        Log.i("bundlePaymentDetails", "" + bundleData);
//
//        //SharedPreferences paypalTempData = getSharedPreferences("PAYMENTDETAILSPAYPAL", 0);
//
//        Map<String, String> finalPayParams = new HashMap<>();
////        RequestParams finalPayParams = new RequestParams();
////        finalPayParams.put("parentTask", "AndroidApp");
//        finalPayParams.put("parentTask", "rechargeApp");
//        finalPayParams.put("childTask", "updateUserPaymentDetailsPaypal");
//        finalPayParams.put("userCode", myUserSessionManager.getSecurityCode());
//        finalPayParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        finalPayParams.put("purchasedAmt", bundlePayPalData.getString("purchasedAmt", ""));
//        finalPayParams.put("pAmt", bundlePayPalData.getString("pAmt", ""));
//        finalPayParams.put("payOptionId", bundlePayPalData.getString("payOptionId", ""));
//        finalPayParams.put("transactionId", bundlePayPalData.getString("transactionId", ""));
//        finalPayParams.put("paidFrom", bundlePayPalData.getString("paidFrom", ""));
//        finalPayParams.put("payTypeId", bundlePayPalData.getString("payTypeId", ""));
//        finalPayParams.put("addDiscountWallet", bundlePayPalData.getString("addDiscountWallet", ""));
//        finalPayParams.put("paypalPaymentId", paypalPaymentId);
//
//        // Make Http call
////        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getContext());
////        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", finalPayParams,
////                new PboServerRequestListener() {
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////
////                        try {
////                            handleUpdateUserPaymentDetailsPaypal(response);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//////                Toast.makeText(getActivity(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
////                    }
////                });
//        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
//            @Override
//            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
//                try {
//                    handleUpdateUserPaymentDetailsPaypal(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrofitFailure(String errorMessage, int apiCode) {
//                Log.d("ConfirmOrderFragmentNew", errorMessage);
//            }
//        });
//        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, finalPayParams, null, null);
//    }
//
//    public void handleUpdateUserPaymentDetailsPaypal(JSONObject response) throws JSONException {
//        JSONObject jo = null;
//        try {
//            jo = response;
//            msgTitle = jo.getString("msgTitle");
//
//            alertDialog = new AlertDialog.Builder(getContext());
//            alertDialog.setMessage(Html.fromHtml(jo.getString("msg"))).setTitle(jo.getString("msgTitle"))
//                    // .setNeutralButton("OK",null).create();
//                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//
//                            if (msgTitle.equals("Success")) {
//
//                                Intent intent = new Intent(getActivity(), DashBoardActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("defaultPage", "AddMoney");
//                                intent.putExtras(bundle);
//                                getActivity().finish();
//                                startActivity(intent);
//
////                                userDeviceDetails.showToast("Payment Successful");
//                                /*Intent mIntent = new Intent(getActivity(), DashBoardActivity.class);
//                                Bundle mBundle = new Bundle();
//                                mBundle.putString("jumpToWallet", "onClickSave");
//                                mIntent.putExtras(mBundle);
//                                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(mIntent);
//                                getActivity().finish();*/
//
//
//                            }
//
//
//                        }
//                    }).setCancelable(false).create().show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addMoneyWalletServer() {
//
//        // SharedPreferences finalPayData = getSharedPreferences("PAYMENTDETAILSFINAL", 0);
////        RequestParams finalPayParams = new RequestParams();
//        Map<String, String> finalPayParams = new HashMap<>();
//        finalPayParams.put("parentTask", "rechargeApp");
//        finalPayParams.put("childTask", "saveUserPaymentCheckout");
//        finalPayParams.put("userCode", myUserSessionManager.getSecurityCode());
//        finalPayParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        finalPayParams.put("payUsingIds", bundleData.getString("payUsingIds"));
//        finalPayParams.put("confirmPayUsings", bundleData.getString("confirmPayUsings"));
//        finalPayParams.put("confirmPaymentNotes", bundleData.getString("confirmPaymentNotes"));
//        finalPayParams.put("confirmPurchasedAmount", bundleData.getString("confirmPurchasedAmount"));
//        finalPayParams.put("confirmDiscountAmount", bundleData.getString("confirmDiscountAmount"));
//        finalPayParams.put("confirmDepositingAmount", bundleData.getString("confirmDepositingAmount"));
//        finalPayParams.put("confirmPayingAmount", bundleData.getString("confirmPayingAmount"));
//        finalPayParams.put("confirmTotalAmount", bundleData.getString("confirmPayingAmount"));
//        finalPayParams.put("totalAmt", bundleData.getString("totalAmt"));
//        finalPayParams.put("payAmt", bundleData.getString("payAmt"));
//        finalPayParams.put("addDiscountWallet", bundleData.getString("addDiscountWallet"));
//        finalPayParams.put("confirmPaymentProfileNames", bundleData.getString("confirmPaymentProfileNames"));
//        finalPayParams.put("payUsingIds", bundleData.getString("payUsingIds"));
//        finalPayParams.put("userIp", userDeviceDetails.getLocalIpAddress());
//
//
//        if (requestFrom.equals("CreditDebitCard")) {
//
//            finalPayParams.put("stripeToken", bundleData.getString("stripeToken"));
//        }
//
//        if (requestFrom.equals("Bank Deposit")) {
//
//            finalPayParams.put("chequeVoucherSlip", bundleData.getString("chequeVoucherSlip"));
//            finalPayParams.put("chequeVoucherSlipName", bundleData.getString("chequeVoucherSlipName"));
//            finalPayParams.put("confirmChequeVoucherNos", bundleData.getString("confirmChequeVoucherNos"));
//
//        }
//
//        // Make Http call
//        // Make Http call
////        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
////        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", finalPayParams,
////                new PboServerRequestListener() {
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////
////                        try {
////                            handleAddMoneyWalletServer(response);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
////                    }
////                });
//        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
//            @Override
//            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
//                try {
//                    handleAddMoneyWalletServer(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrofitFailure(String errorMessage, int apiCode) {
//                Log.d("ConfirmOrderFragmentNew", errorMessage);
//            }
//        });
//        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, finalPayParams);
//    }
//
//    public void handleAddMoneyWalletServer(JSONObject response) throws JSONException {
//
//        Log.d("BDreponse ----", "-=-=" + response.toString());
//
//        JSONObject jo = null;
//        try {
//
//            jo = response;
//
//            msgTitle = jo.getString("msgTitle");
//
//            alertDialog = new AlertDialog.Builder(getActivity());
//            alertDialog.setMessage(Html.fromHtml(jo.getString("msg")))
//                    .setTitle(jo.getString("msgTitle"))
//                    // .setNeutralButton("OK",null).create();
//                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//
//                            if (msgTitle.equals("Success")) {
//
//
//                                Intent intent = new Intent(getActivity(), DashBoardActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("defaultPage", "AddMoney");
//                                intent.putExtras(bundle);
//                                getActivity().finish();
//                                startActivity(intent);
//
//                                /*Intent mIntent = new Intent(getActivity(), DashBoardActivity.class);
//                                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Bundle mBundle = new Bundle();
//                                mBundle.putString("jumpToWallet", "onClickSave");
//                                mIntent.putExtras(mBundle);
//                                startActivity(mIntent);*/
//
//                            }
//
//
//                        }
//                    }).setCancelable(false).create().show();
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            //  userDeviceDetails.showToast(jo);
//        }
//
//
//    }
//
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//
//    }
//
//    public void createUserPaymentSct() {
//        Intent i = new Intent(getContext(), SCTPaymentActivity.class);
//        i.putExtra(StaticVariables.MERCHANT_AMOUNT, bundleData.getString("confirmTotalAmount"));
//        i.putExtra(StaticVariables.DESCRIPTION, bundleData.getString("confirmPaymentNotes"));
//        i.putExtra(StaticVariables.MERCHANT_TNX_ID,
//                bundleData.getString("transactionNo"));
//        startActivityForResult(i, SCT_REQUEST_CODE);
//
//    }
//
//    public void confirmSCTPayment(String status, String msg, String ref, String tnx) {
//        Log.i("bundlePaymentDetails", "" + bundleData);
//
//        //SharedPreferences paypalTempData = getSharedPreferences("PAYMENTDETAILSPAYPAL", 0);
//
////        RequestParams finalPayParams = new RequestParams();
////        finalPayParams.put("parentTask", "AndroidApp");
//        Map<String, String> finalPayParams = new HashMap<>();
//        finalPayParams.put("parentTask", "rechargeApp");
//        finalPayParams.put("childTask", "confirmSCTPayment");
//        finalPayParams.put("userCode", myUserSessionManager.getSecurityCode());
//        finalPayParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        finalPayParams.put("status", status);
//        finalPayParams.put("msg", msg);
//        finalPayParams.put("ref", ref);
//        finalPayParams.put("tnx", tnx);
//
//
//        // Make Http call
////        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getContext());
////        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", finalPayParams,
////                new PboServerRequestListener() {
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////
////                        try {
////                            handleconfirmSCTPayment(response);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//////                Toast.makeText(getActivity(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
////                    }
////                });
//
//        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
//            @Override
//            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
//                try {
//                    handleconfirmSCTPayment(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrofitFailure(String errorMessage, int apiCode) {
//                Log.d("ConfirmOrderFragmentNew", errorMessage);
//            }
//        });
//        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, finalPayParams, null, null);
//    }
//
//    public void handleconfirmSCTPayment(JSONObject response) throws JSONException {
//        Log.d("res=======", response + "");
//        if (response.getString("msg").equals("Success")) {
//            ((DashBoardActivity) getActivity()).updateUserBalance(response.getString("currency"), response.getString("balance"), Double.parseDouble(response.getString("0")));
//            Bundle bundle = new Bundle();
//            bundle.putString("userBalance", response.getString("currency") + " " + response.getString("balance"));
//            Fragment fragment = new DashboardMobaletFragment();
//            fragment.setArguments(bundle);
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.content_frame, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
//
//    }
//}
