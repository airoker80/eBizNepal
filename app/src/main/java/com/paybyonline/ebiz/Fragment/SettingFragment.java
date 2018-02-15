package com.paybyonline.ebiz.Fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;
import com.paybyonline.ebiz.util.PasswordValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
    //    PboServerRequestHandler handler;
    CardView changePassword;
    CardView resetPin;
    CardView updateAppDefaultPage;

    RadioGroup radioDefaultPage;
    PasswordValidator passwordValidator;
    private RetrofitHelper retrofitHelper;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Settings");
        ((DashBoardActivity) getActivity()).setFragmentName("Settings");
        // to reset menu item
        setHasOptionsMenu(true);
        passwordValidator = new PasswordValidator();
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        resetPin = (CardView) view.findViewById(R.id.resetPin);
        changePassword = (CardView) view.findViewById(R.id.changePassword);
        updateAppDefaultPage = (CardView) view.findViewById(R.id.updateAppDefaultPage);
        resetPin.setOnClickListener(view1 -> showResetPinCodeForm());
        changePassword.setOnClickListener(view12 -> showChangePasswordForm());

        updateAppDefaultPage.setOnClickListener(view13 -> updateAppDefaultPageForm());


        return view;
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).setFragmentName("Settings");
        ((DashBoardActivity) getActivity()).shareMenu.setVisible(true);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        super.onResume();
    }

    public void updateAppDefaultPageForm() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("UPDATE", null)
                .setNegativeButton("CANCEL", null)
                .create();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.setting_layout_default_page, null);
        RadioButton dashboardRadio = (RadioButton) dialogView.findViewById(R.id.dashboardRadio);
        RadioButton buyRechargeRadio = (RadioButton) dialogView.findViewById(R.id.buyRechargeRadio);
        RadioButton addMoneyRadio = (RadioButton) dialogView.findViewById(R.id.addMoneyRadio);
        RadioButton sendMoneyRadio = (RadioButton) dialogView.findViewById(R.id.sendMoneyRadio);
        RadioButton reportRadio = (RadioButton) dialogView.findViewById(R.id.reportRadio);
        radioDefaultPage = (RadioGroup) dialogView.findViewById(R.id.radioDefaultPage);
        if (!myUserSessionManager.getKeyDefaultPage().equals("")) {
            if (myUserSessionManager.getKeyDefaultPage().equals("Dashboard")) {
                dashboardRadio.setChecked(true);
            } else if (myUserSessionManager.getKeyDefaultPage().equals("Payments")) {
                buyRechargeRadio.setChecked(true);
            } else if (myUserSessionManager.getKeyDefaultPage().equals("Add Money")) {
                addMoneyRadio.setChecked(true);
            } else if (myUserSessionManager.getKeyDefaultPage().equals("Send Money")) {
                sendMoneyRadio.setChecked(true);
            } else if (myUserSessionManager.getKeyDefaultPage().equals("Report")) {
                reportRadio.setChecked(true);
            }

        } else {
            dashboardRadio.setChecked(true);
        }
        alertDialog.setView(dialogView);
        SpannableString spannableString = new SpannableString("Default Page");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        alertDialog.setTitle(spannableString);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = radioDefaultPage.getCheckedRadioButtonId();
                        RadioButton selectedRadio = (RadioButton) dialogView.findViewById(selectedId);
                        myUserSessionManager.setKeyDefaultPage(selectedRadio.getText().toString());
                        userDeviceDetails.showToast("Default Page Updated");
                        alertDialog.dismiss();
                    }
                });

                final Button btnDecline = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    public void showChangePasswordForm() {

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("UPDATE", null)
                .setNegativeButton("CANCEL", null)
                .create();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.setting_layout_change_password, null);
        final EditText userOldPassword = (EditText) dialogView.findViewById(R.id.userOldPassword);
        final EditText userNewPassword = (EditText) dialogView.findViewById(R.id.userNewPassword);
        final EditText userNewPasswordConfirm = (EditText) dialogView.findViewById(R.id.userNewPasswordConfirm);

        alertDialog.setView(dialogView);
        SpannableString spannableString = new SpannableString("Update Password");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);

        alertDialog.setTitle(spannableString);
        alertDialog.setOnShowListener(dialog -> {
            final Button btnAccept = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnAccept.setOnClickListener(v -> {
                if (validateChangePasswordForm(userOldPassword, userNewPassword, userNewPasswordConfirm)) {
                    updateUserPassword(userOldPassword.getText().toString(), userNewPassword.getText().toString());
                    alertDialog.dismiss();
                }
            });

            final Button btnDecline = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            btnDecline.setOnClickListener(v -> alertDialog.dismiss());
        });

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void updateUserPassword(String userOldPassword, String userNewPassword) {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "updatePassword");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("userOldPassword", userOldPassword);
        params.put("userNewPassword", userNewPassword);
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleUpdateUserPasswordResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleUpdateUserPasswordResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Setting", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleUpdateUserPasswordResponse(JSONObject response) throws JSONException {
        userDeviceDetails.showToast(response.getString("msg"));
        if (response.getString("msgTitle").equals("Success")) {
            myUserSessionManager.saveUserInformation(response.getString("username"),
                    response.getString("authenticationCode"), response.getString("userCode"));
        }
    }

    public Boolean validateChangePasswordForm(EditText userOldPassword, EditText userNewPassword, EditText userNewPasswordConfirm) {

        if (userOldPassword.getText().toString().isEmpty()) {
            userOldPassword.setError("Required");
            return false;
        } else {
            userOldPassword.setError(null);
        }

        if (userNewPassword.getText().toString().isEmpty()) {
            userNewPassword.setError("Required");
            return false;
        } else {
            userNewPassword.setError(null);
        }

        if (passwordValidator.validate(userNewPassword.getText().toString())) {
            userNewPassword.setError(null);
        } else {
            userNewPassword.setError("Password must contains at least 8 characters,one upper case letter,lower case letter,special character and numeric digit.");
            return false;
        }

        if (userNewPasswordConfirm.getText().toString().isEmpty()) {
            userNewPasswordConfirm.setError("Required");
            return false;
        } else {
            userNewPasswordConfirm.setError(null);
        }

        if (!userNewPassword.getText().toString().equals(userNewPasswordConfirm.getText().toString())) {
            userNewPasswordConfirm.setError("Password doesn't match");
            return false;
        } else {
            userNewPasswordConfirm.setError(null);
        }

        return true;
    }


    public void showResetPinCodeForm() {

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("RESET", null)
                .setNegativeButton("CANCEL", null)
                .create();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.setting_layout_reset_pincode, null);
        final EditText userPassword = (EditText) dialogView.findViewById(R.id.userPassword);
        final EditText userPinCode = (EditText) dialogView.findViewById(R.id.userPinCode);
        final EditText userPinCodeConfirm = (EditText) dialogView.findViewById(R.id.userPinCodeConfirm);
        SpannableString spannableString = new SpannableString("Reset Pin Code");
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        alertDialog.setView(dialogView);
        alertDialog.setTitle(spannableString);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateResetPinForm(userPassword, userPinCode, userPinCodeConfirm)) {
                            resetUserPinCode(userPassword.getText().toString(), userPinCode.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });

                final Button btnDecline = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public Boolean validateResetPinForm(EditText userPassword, EditText userPinCode, EditText userPinCodeConfirm) {

        if (userPassword.getText().toString().isEmpty()) {
            userPassword.setError("Required");
            return false;
        } else {
            userPassword.setError(null);
        }

        if (userPinCode.getText().toString().isEmpty()) {
            userPinCode.setError("Required");
            return false;
        } else {
            userPinCode.setError(null);
        }

        String minLength = "4";
        if (minLength.equals(userPinCode.getText().toString().length() + "")) {
            userPinCode.setError(null);
        } else {
            userPinCode.setError("4 Digit Transaction Pin Code Required");
            return false;
        }

        if (userPinCodeConfirm.getText().toString().isEmpty()) {
            userPinCodeConfirm.setError("Required");
            return false;
        } else {
            userPinCodeConfirm.setError(null);
        }

        if (!userPinCode.getText().toString().equals(userPinCodeConfirm.getText().toString())) {
            userPinCodeConfirm.setError("Pin Code doesn't match");
            return false;
        } else {
            userPinCodeConfirm.setError(null);
        }

        return true;
    }

    public void resetUserPinCode(String userPassword, String userPinCode) {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "resetUserPinCode");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("userPassword", userPassword);
        params.put("userPinCode", userPinCode);
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleResetUserPinCodeResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleResetUserPinCodeResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Setting", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleResetUserPinCodeResponse(JSONObject response) throws JSONException {
        userDeviceDetails.showToast(response.getString("msg"));
    }


}
