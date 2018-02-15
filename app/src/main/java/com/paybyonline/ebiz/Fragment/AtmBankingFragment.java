package com.paybyonline.ebiz.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paybyonline.ebiz.Adapter.Model.AtmbankingModel;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;


/**
 * Created by Anish on 9/8/2016.
 */
public class AtmBankingFragment extends Fragment {
    CoordinatorLayout coordinateLayout;
    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    List<AtmbankingModel> atmbankingModelList;
    private HashMap<String, String> userDetails;
    /*private RadioButton radio_sct;
    private RadioButton radio_bank;*/
    DecimalFormat formatter = new DecimalFormat("0.00");
    RadioButton radioButton;
    RadioGroup radioGroup;
    private EditText payment_note;
    private TextView amt_val;
    private EditText profile_name;
    private CheckBox chkSave;
    private CheckBox chkAgree;
    String payId;
    private Button payBtn;
    private int SCT_REQUEST_CODE = 0;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    Bundle bundle;
    TextView amtVal;
    TextView profile_name_title;
    private RetrofitHelper retrofitHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atm_banking, container, false);
        myUserSessionManager = new MyUserSessionManager(getActivity());
        userDeviceDetails = new UserDeviceDetails(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        bundle = getArguments();
        coordinateLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        initializeComponents(view);
        obtainUserPaymentType();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                payId = String.valueOf(checkedId);
                //Toast.makeText(getContext(), checkedId+"---" + radioButton.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (payId != null) {
                    if (validateFormData()) {
                        showConfirmOrderForm(payId);
                    }

                } else {
                    Toast.makeText(getContext(), "Please select the payment method!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    public void initializeComponents(View view) {
       /* radio_sct=(RadioButton)view.findViewById(R.id.radio_sct);
        radio_bank=(RadioButton)view.findViewById(R.id.radio_bank);*/
        radioGroup = (RadioGroup) view.findViewById(R.id.atmlist);
        payment_note = (EditText) view.findViewById(R.id.payment_note);
        amt_val = (TextView) view.findViewById(R.id.amt_val);
        profile_name = (EditText) view.findViewById(R.id.profile_name);
        chkSave = (CheckBox) view.findViewById(R.id.chkSave);
        chkAgree = (CheckBox) view.findViewById(R.id.chkAgree);
        payBtn = (Button) view.findViewById(R.id.loginBtn);
        profile_name_title = (TextView) view.findViewById(R.id.profile_name_title);
        amtVal = (TextView) view.findViewById(R.id.amtVal);
        amtVal.setText(bundle.getString("amtPayingVal"));
        profile_name_title.setVisibility(View.GONE);
        profile_name.setVisibility(View.GONE);

    }

    public void obtainUserPaymentType() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "obtainUserPaymentType");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("payMethod", "Atm Banking");

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinateLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
//                params, new PboServerRequestListener() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//                            handleObtainUserPaymentType(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Log.i("response:", "" + response);
//                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                    }
//                });
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
                Log.i("Atm Banking:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainUserPaymentType(JSONObject response) throws JSONException {

        Log.i("response:", "" + response);
        try {
            Log.i("response:", "" + response);

            JSONArray jsonArray = response.getJSONArray("dataList");
            atmbankingModelList = new ArrayList<AtmbankingModel>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = (JSONObject) jsonArray.get(i);
                int id = obj.getInt("id");
                String userPayName = obj.get("userPayName").toString();
                String userPayLogo = PayByOnlineConfig.BASE_URL + "documents/" +
                        Uri.encode(obj.get("userPayLogo").toString());

                atmbankingModelList.add(new AtmbankingModel(id, userPayName, userPayLogo));

            }


            for (int i = 0; i < atmbankingModelList.size(); i++) {
                AtmbankingModel atmbankingModel = atmbankingModelList.get(i);
                radioButton = new RadioButton(getContext());
                radioButton.setId(atmbankingModel.getId());
                radioButton.setTag(atmbankingModel.getUserPayName());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 5, 0);
                radioButton.setLayoutParams(params);
                radioButton.setGravity(Gravity.CENTER_VERTICAL);
                ImageView imageView = new ImageView(getContext());
                imageView.setId(atmbankingModel.getId());
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(200, 200);
                params2.setMargins(5, 0, 0, 0);
                imageView.setLayoutParams(params2);
                Picasso.with(getContext())
                        .load(atmbankingModel.getUserPayLogo())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);
                radioGroup.addView(radioButton);
                radioGroup.addView(imageView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showConfirmOrderForm(String payId) {

        showMyAlertProgressDialog.showProgressDialog("Processing...",
                "Please wait.");


//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "showConfirmOrderSCT");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("payUsingIds", payId);
        params.put("amtPayingVal", bundle.getString("amtPayingVal"));
        params.put("sct", "true");

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinateLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Obtaining Confirm Order",
//                params, new PboServerRequestListener() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//                            handleShowConfirmOrderForm(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                    }
//                });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleShowConfirmOrderForm(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Atm Banking", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleShowConfirmOrderForm(JSONObject response) throws JSONException {
        Log.i("response----", response + "");
        showMyAlertProgressDialog.hideProgressDialog();
        // userDeviceDetails.showToast(response);
        showMyAlertProgressDialog.hideProgressDialog();
        if (response.getString("msgTitle").equals("Success")) {
            try {
                JSONObject jo = response;

                Log.i("data:", "" + jo);


                Bundle infoBundle = new Bundle();
                infoBundle.putString("username", userDetails.get(MyUserSessionManager.KEY_USERNAME));
                infoBundle.putString("payUsingIds", jo.get("userPayTypesIds").toString());
                infoBundle.putString("confirmPayUsings", jo.get("confirmPayUsings").toString());
                infoBundle.putString("payTypeValue", jo.get("payTypeValue").toString());
                infoBundle.putString("confirmPaymentNotes", payment_note.getText().toString());
                infoBundle.putString("confirmPurchasedAmount", bundle.getString("purchasedAmt"));
                infoBundle.putString("confirmDiscountAmount", bundle.getString("disAmt"));
                infoBundle.putString("confirmDepositingAmount", bundle.getString("walletDepositingVal"));
                infoBundle.putString("confirmPayingAmount", bundle.getString("amtPayingVal"));
                infoBundle.putString("totalAmt", bundle.getString("totalAmt"));
                infoBundle.putString("payAmt", bundle.getString("amtPayingVal"));
                infoBundle.putString("addDiscountWallet", bundle.getString("addDiscountWallet"));

                if (profile_name.getText().toString().length() > 0) {
                    infoBundle.putString("confirmPaymentProfileNames", profile_name.getText().toString());
                } else {
                    infoBundle.putString("confirmPaymentProfileNames", "");
                }

                String profileName = null;
                if (profile_name.getText().toString().length() > 0) {

                    profileName = profile_name.getText().toString();

                } else {

                    profileName = "-";
                }
                Double confirmTotalAmount = Double.parseDouble(bundle.getString("amtPayingVal", ""))
                        - Double.parseDouble(jo.get("payTypeDisCom").toString());
                Double amtPayingVal = Double.parseDouble(bundle.getString("amtPayingVal"));
                String confirmOrderText = "<br>A) Payment Amount : " + jo.get("paymentCurrency").toString() + " " + formatter.format(amtPayingVal) +
                        "<br>B)  " + jo.get("payTypeHeading").toString() + ":" + jo.get("payTypeValue").toString() +
                        "<br>C) Service Charge Amount :\n" + jo.get("paymentCurrency").toString() + " " + formatter.format(Double.parseDouble(jo.get("payTypeDisCom").toString())) +
                        "<br>D) Fund added to wallet: " + jo.get("paymentCurrency").toString() + " " + formatter.format(confirmTotalAmount) + "<br>" +
                        "**(Fund added to wallet = A-C)";

      /*                  "<br>Payment Gateway : " + jo.get("paymentGateway").toString() +
                        "<br>Payment Profile Name:" + "&nbsp;" + profileName +
                        "<br>Payment Currency : " + jo.get("paymentCurrency").toString() +
                        "<br>Payment Method : " + jo.get("payTypeMethod").toString();*/


/*                String confirmOrderText = "<br>Payment Amount : " + formatter.format(amtPayingVal) +
                        "<br>Payment Type " + jo.get("payTypeHeading").toString() +
                        "<br>Value : " + jo.get("payTypeValue").toString() +
                        "<br>Amount :\n" + formatter.format(Double.parseDouble(jo.get("payTypeDisCom").toString())) + "<br>" +
                        "<br>Net Payment Amount: " + formatter.format(confirmTotalAmount) +
                        "<br>Payment Gateway : " + jo.get("paymentGateway").toString() +
                        "<br>Payment Profile Name:" + "&nbsp;" + profileName +
                        "<br>Payment Currency : " + jo.get("paymentCurrency").toString() +
                        "<br>Payment Method : " + jo.get("payTypeMethod").toString();*/


                Fragment fragment = new ConfirmOrderFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                infoBundle.putString("requestFrom", "AtmBanking");
                infoBundle.putString("confirmOrderText", confirmOrderText);
                infoBundle.putString("confirmTotalAmount", amtPayingVal.toString());
                infoBundle.putString("transactionNo", jo.get("transactionNo").toString());
                fragment.setArguments(infoBundle);
                fragmentTransaction.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
        }
    }


    public Boolean validateFormData() {
        if (chkSave.isChecked() && !(profile_name.getText().toString().length() > 0)) {
            userDeviceDetails.showToast("Please Enter Profile Name");
            return false;
        }
        if (!chkAgree.isChecked()) {
            userDeviceDetails.showToast("Please Agree Payment Terms");
            return false;
        }
        return true;
    }
}
