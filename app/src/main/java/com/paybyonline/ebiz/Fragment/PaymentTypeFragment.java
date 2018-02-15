//package com.paybyonline.ebiz.Fragment;
//
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTabHost;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.paybyonline.ebiz.Activity.DashBoardActivity;
//import com.paybyonline.ebiz.R;
//import com.paybyonline.ebiz.serverdata.ApiIndex;
//import com.paybyonline.ebiz.serverdata.RetrofitHelper;
//import com.paybyonline.ebiz.usersession.MyUserSessionManager;
//import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
//import com.paybyonline.ebiz.usersession.UserDeviceDetails;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//
////import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
////import com.paybyonline.ebiz.serverdata.PboServerRequestListener;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class PaymentTypeFragment extends Fragment {
//
//    private FragmentTabHost mTabHost;
//    UserDeviceDetails userDeviceDetails;
//    MyUserSessionManager myUserSessionManager;
//    ShowMyAlertProgressDialog showMyAlertProgressDialog;
//    private HashMap<String, String> userDetails;
//    CoordinatorLayout coordinatorLayout;
//    String disPerVal;
//    String amount;
//    String disAmt;
//    String disType;
//    String amtType;
//    String totalAmt;
//    String walletDepositingVal;
//    String amtPayingVal;
//    String purchasedAmt;
//    Bundle infoBundle;
//    private RetrofitHelper retrofitHelper;
//
//    public PaymentTypeFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        ((DashBoardActivity) getActivity())
//                .setTitle("Recharge");
//
//        userDeviceDetails = new UserDeviceDetails(getActivity());
//        myUserSessionManager = new MyUserSessionManager(getActivity());
//        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
//        userDetails = myUserSessionManager.getUserDetails();
//        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);
//
//        infoBundle = new Bundle();
//
//
//        Bundle bundle = getArguments();
//        amount = bundle.getString("amount");
//        Log.i("amount  ", amount);
//
//        retrofitHelper = new RetrofitHelper();
//        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
//
//        obtainPaymentDetails();
//        Log.i("obtainPaymentDetails  ", "call");
//        mTabHost = new FragmentTabHost(getActivity());
//        // Locate fragment1.xml to create FragmentTabHost
//        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_payment_type);
//        // Create Tab 1
//        Log.i("infoBundle", infoBundle + "");
//        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Payment Option"),
//                PaymentOptionFragment.class, infoBundle);
//        // Create Tab 2
//        // mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Load Profile"), LoadProfileFragment.class, null);
//        // Create Tab 3
//
//        return mTabHost;
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            Field childFragmentManager = Fragment.class
//                    .getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//
//            throw new RuntimeException(e);
//
//        } catch (IllegalAccessException e) {
//
//            throw new RuntimeException(e);
//
//        }
//    }
//
//    // Remove FragmentTabHost
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mTabHost = null;
//    }
//
//    public void obtainPaymentDetails() {
//
//        /*Bundle bundle = this.getArguments();
//       // String  amt_txt = bundle.getString("amount");*/
////        RequestParams params = new RequestParams();
//        Map<String, String> params = new HashMap<>();
//        params.put("parentTask", "rechargeApp");
//        params.put("childTask", "viewPaymentDetails");
//        params.put("userCode", myUserSessionManager.getSecurityCode());
//        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        params.put("purchasedAmtAdds", amount);
//
//        // Make Http call
////        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
////        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Accessing Payment Details",
////                params, new PboServerRequestListener() {
////                    @Override
////                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////
////                        try {
////                            Log.i("authenticateUser", "" + response);
////
////                            handleObtainPaymentDetails(response);
////
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
//                    Log.i("Payment Type", "" + jsonObject);
//                    handleObtainPaymentDetails(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrofitFailure(String errorMessage, int apiCode) {
//                Log.i("Atm Banking:", "error");
//            }
//        });
//        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
//
//    }
//
//    public void handleObtainPaymentDetails(JSONObject response) throws JSONException {
//
//
//        Log.i("PaymentDetails", "" + response);
//
//        try {
//
//            // Loop through each array element, get JSON object
//            JSONObject obj = response;
//            String msgTitle = obj.getString("msgTitle");
//
//            if (msgTitle.equals("Failed")) {
//                // userDeviceDetails
//                //      .showToast(obj.getString("msg").toString());
//                showMyAlertProgressDialog.showUserAlertDialog(
//                        obj.getString("msg"), "FAILED");
//
//            } else {
//
//                String showDetails = obj.getString("showDetails");
//                disPerVal = obj.getString("disPerVal");
//                disAmt = obj.getString("disAmt");
//                disType = obj.getString("disType");
//                amtType = obj.getString("amtType");
//                totalAmt = obj.getString("totalAmt");
//                walletDepositingVal = obj.getString("walletDepositingVal");
//                amtPayingVal = obj.getString("amtPayingVal");
//                purchasedAmt = obj.getString("purchasedAmt");
//
//                // userDeviceDetails.showToast("amtPayingVal "+amtPayingVal);
//
//                SharedPreferences pref = getActivity().getSharedPreferences("PAYMENTDETAILS", 0);
//                SharedPreferences.Editor editor1 = pref.edit();
//                SharedPreferences.Editor editor = pref.edit();
//                editor1.clear();
//
//                // editor.remove("logged");
//                // editor.remove("appUserName");
//                // editor.remove("country");
//                editor1.apply();
//
//                SharedPreferences pref2 = getActivity().getSharedPreferences("PAYMENTDETAILS", 0);
//                editor = pref2.edit();
//
//                // userDeviceDetails.showToast(amtPayingVal);
//                editor.putString("disPerVal", disPerVal);
//                editor.putString("disAmt", disAmt);
//                editor.putString("disType", disType);
//                editor.putString("amtType", amtType);
//                editor.putString("totalAmt", totalAmt);
//                editor.putString("disPerVal", disPerVal);
//                editor.putString("walletDepositingVal", walletDepositingVal);
//                editor.putString("amtPayingVal", amtPayingVal);
//                editor.putString("purchasedAmt", purchasedAmt);
//                editor.putString("name", "flatDiscount");
//
//                editor.apply();
//                //  Bundle bundle =new  Bundle();
//                infoBundle.putString("disPerVal", disPerVal);
//                infoBundle.putString("disAmt", disAmt);
//                infoBundle.putString("disType", disType);
//                infoBundle.putString("amtType", amtType);
//                infoBundle.putString("totalAmt", totalAmt);
//                infoBundle.putString("disPerVal", disPerVal);
//                infoBundle.putString("walletDepositingVal", walletDepositingVal);
//                infoBundle.putString("amtPayingVal", amtPayingVal);
//                infoBundle.putString("purchasedAmt", purchasedAmt);
//                infoBundle.putString("name", "flatDiscount");
//
//
//            }
//
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//}
//
