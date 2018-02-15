package com.paybyonline.ebiz.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Swatin on 11/24/2017.
 */

public class NetBankingConfirmFragment extends Fragment {

    private EditText edtPaymentNote;
    private TextView txtProfileName;
    private EditText edtProfileName;
    private CheckBox chkSave;
    private CheckBox chkAgree;
    private String payId;
    private UserDeviceDetails userDeviceDetails;
    private RetrofitHelper retrofitHelper;
    private MyUserSessionManager myUserSessionManager;
    private DecimalFormat formatter = new DecimalFormat("0.00");
    private HashMap<String, String> userDetails;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private Bundle bundle;
    private View btnPayNow;
    private TextView txtAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.net_banking_bank_selected_view, container, false);
        btnPayNow = view.findViewById(R.id.payNowBtn);
        txtProfileName = (TextView) view.findViewById(R.id.profile_name_title);
        edtPaymentNote = (EditText) view.findViewById(R.id.payment_note);
        edtProfileName = (EditText) view.findViewById(R.id.profile_name);
        chkSave = (CheckBox) view.findViewById(R.id.chkSave);
        chkAgree = (CheckBox) view.findViewById(R.id.chkAgree);
        txtAmount = (TextView) view.findViewById(R.id.amtVal);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        userDeviceDetails = new UserDeviceDetails(getActivity());
        txtProfileName.setVisibility(View.GONE);
        edtProfileName.setVisibility(View.GONE);
        bundle = getArguments();
        if (bundle != null) {
            txtAmount.setText(bundle.getString("amtPayingVal"));
            payId = bundle.getString("bankId");
        }
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payId != null) {
                    if (validateFormData()) {
                        showConfirmOrderOnline(payId);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select the payment method!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        chkSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    txtProfileName.setVisibility(View.VISIBLE);
                    edtProfileName.setVisibility(View.VISIBLE);
                } else {
                    txtProfileName.setVisibility(View.GONE);
                    edtProfileName.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showConfirmOrderOnline(String payId) {

        showMyAlertProgressDialog.showProgressDialog("Processing...",
                "Please wait.");

        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        if (bundle.getString("from").equals("nb")){
            params.put("childTask", "showConfirmOrderFormOnlineBanking");
        }else if (bundle.getString("from").equals("dc")){
            params.put("childTask", "showConfirmOrderFormDebitCreditCard");
        }else {
            params.put("childTask", "showConfirmOrderFormOnlineBanking");
        }

        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("payUsingIds", payId);
        params.put("amtPayingVal", bundle.getString("amtPayingVal"));

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleShowConfirmOrderFormNibl(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Net Banking", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public Boolean validateFormData() {
        if (chkSave.isChecked() && !(edtProfileName.getText().toString().length() > 0)) {
            userDeviceDetails.showToast("Please Enter Profile Name");
            return false;
        }
        if (!chkAgree.isChecked()) {
            userDeviceDetails.showToast("Please Agree Payment Terms");
            return false;
        }
        return true;
    }

    public void handleShowConfirmOrderFormNibl(JSONObject response) throws JSONException {
        Log.i("response----", response + "");
        showMyAlertProgressDialog.hideProgressDialog();
        // userDeviceDetails.showToast(response);
        showMyAlertProgressDialog.hideProgressDialog();
        try {
            Log.i("data:", "" + response);

            Bundle infoBundle = new Bundle();
            infoBundle.putString("username", userDetails.get(MyUserSessionManager.KEY_USERNAME));
            infoBundle.putString("payUsingIds", response.get("userPayTypesIds").toString());
            infoBundle.putString("confirmPayUsings", response.get("confirmPayUsings").toString());
            infoBundle.putString("payTypeValue", response.get("payTypeValue").toString());
            infoBundle.putString("confirmPaymentNotes", edtPaymentNote.getText().toString());
            infoBundle.putString("confirmPurchasedAmount", bundle.getString("purchasedAmt"));
            infoBundle.putString("confirmDiscountAmount", bundle.getString("disAmt"));
            infoBundle.putString("confirmDepositingAmount", bundle.getString("walletDepositingVal"));
            infoBundle.putString("confirmPayingAmount", bundle.getString("amtPayingVal"));
            infoBundle.putString("totalAmt", bundle.getString("totalAmt"));
            infoBundle.putString("payAmt", bundle.getString("amtPayingVal"));
            infoBundle.putString("addDiscountWallet", bundle.getString("addDiscountWallet"));

            String profileName = null;
            if (edtProfileName.getText().toString().length() > 0) {
                profileName = edtProfileName.getText().toString();
                infoBundle.putString("confirmPaymentProfileNames", edtProfileName.getText().toString());
            } else {
                profileName = "-";
                infoBundle.putString("confirmPaymentProfileNames", "");
            }
            Double confirmTotalAmount = Double.parseDouble(bundle.getString("amtPayingVal", ""))
                    + Double.parseDouble(response.get("payTypeDisCom").toString());
            Double amtPayingVal = Double.parseDouble(bundle.getString("amtPayingVal"));
            String confirmOrderText = "<br>Payment Amount : " + formatter.format(amtPayingVal) +
                    "<br>Payment Type Discount/Commission" + response.get("payTypeHeading").toString() +
                    "<br>Value : " + response.get("payTypeValue").toString() +
                    "<br>Amount :\n" + formatter.format(Double.parseDouble(response.get("payTypeDisCom").toString())) + "<br>" +
                    "<br>Net Payment Amount: " + formatter.format(confirmTotalAmount) +
                    "<br>Payment Gateway : " + response.get("paymentGateway").toString() +
                    "<br>Payment Profile Name:" + "&nbsp;" + profileName +
                    "<br>Payment Currency : " + response.get("paymentCurrency").toString() +
                    "<br>Payment Method : " + response.get("payTypeMethod").toString();

            Fragment fragment = new ConfirmOrderFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            infoBundle.putString("requestFrom", response.getString("confirmPayUsings"));
            infoBundle.putString("confirmOrderText", confirmOrderText);
            infoBundle.putString("bankName", response.get("paymentGateway").toString());
            fragment.setArguments(infoBundle);
            fragmentTransaction.commitNow();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
