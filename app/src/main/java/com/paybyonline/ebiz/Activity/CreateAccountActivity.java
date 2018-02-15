package com.paybyonline.ebiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

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

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailTxt;
    EditText pwdTxt;
    EditText partnerCodeTxt;
    CheckBox showPasswordCheck;
    //    EditText mobno;
    String email = "";
    String pwd = "";
    String partnerCode = "";
    String mobNoTxt = "";
    CoordinatorLayout coordinatorLayout;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    private HashMap<String, String> userDetails;
    Button regBtn;

    PasswordValidator passwordValidator;
    private RetrofitHelper retrofitHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordValidator = new PasswordValidator();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(CreateAccountActivity.this);
        userDeviceDetails = new UserDeviceDetails(this);
        myUserSessionManager = new MyUserSessionManager(this);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        pwdTxt = (EditText) findViewById(R.id.pwdTxt);
//        mobno=(EditText)findViewById(R.id.mobno);
        partnerCodeTxt = (EditText) findViewById(R.id.partnerCode);
        showPasswordCheck = (CheckBox) findViewById(R.id.showPasswordCheck);
        showPasswordCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    pwdTxt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pwdTxt.setSelection(pwdTxt.getText().length());
                } else {
                    pwdTxt.setInputType(129);
                    pwdTxt.setSelection(pwdTxt.getText().length());

                }
            }
        });
        regBtn = (Button) findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTextView()) {
                    HttpAsyncTaskUserRegister();
                }
            }
        });
        retrofitHelper = new RetrofitHelper();
        getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
    }

    public boolean checkTextView() {

        if (emailTxt.getText().length() > 0) {

            email = emailTxt.getText().toString();
            emailTxt.setError(null);

        } else {
            emailTxt.setError("Required valid email");
            return false;
        }

        if (pwdTxt.getText().length() > 0) {
            pwdTxt.setError(null);
            pwd = pwdTxt.getText().toString();

        } else {
            pwdTxt.setError("Required");
            return false;
        }

        if (passwordValidator.validate(pwdTxt.getText().toString())) {
            pwdTxt.setError(null);
        } else {
            pwdTxt.setError("Password must contains at least 8 characters,one upper case letter,lower case letter,special character and numeric digit.");
            return false;
        }

        /*if(mobno.getText().length()>0){
            mobno.setError(null);
            mobNoTxt=mobno.getText().toString();
        }else{
            mobno.setError("Required");
            return false;
        }*/

        if (partnerCodeTxt.getText().length() > 0) {
            partnerCode = getString(R.string.referralId);
        }

        return true;
    }

    public void HttpAsyncTaskUserRegister() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("userEmail", email);
        params.put("userPassword", pwd);
        params.put("partnerCode", getString(R.string.referralId));
        params.put("socialRequestSent", "Self");
        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,CreateAccountActivity.this);
//        handler.makeRequest("userRegister", "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    HttpAsyncTaskUserRegisterResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    HttpAsyncTaskUserRegisterResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Create Account", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_REGISTER_USER_ENDPOINT, params, null, null);
    }

    public void HttpAsyncTaskUserRegisterResponse(JSONObject response) throws JSONException {

        JSONObject json;
        String userMsgServer = "";
        try {


            Log.i("response ", response + "");

            json = response;
            String regStat = json.getString("msgTitle");
            userMsgServer = json.getString("msg");

            if (regStat.equals("Success")) {

                myUserSessionManager.saveUserInformation(emailTxt.getText().toString(),
                        response.getString("authenticationCode"), response.getString("userCode"));

                // ************ passing countryDetails to Country Selection Activity **********//*
                Intent intent = new Intent(getApplicationContext(), AfterRegistration.class);
//                intent.putExtra("mobileNo", mobNoTxt);
                startActivity(intent);

            } else {

                showMyAlertProgressDialog.showUserAlertDialog(
                        userMsgServer, "Registration Failed");

            }


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
