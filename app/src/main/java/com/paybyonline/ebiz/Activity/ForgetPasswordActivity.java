package com.paybyonline.ebiz.Activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText emailTxt;
    Button rqstPwd;


    CoordinatorLayout coordinatorLayout;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    private HashMap<String, String> userDetails;
    private RetrofitHelper retrofitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FORGET PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        retrofitHelper = new RetrofitHelper();
        getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(this);
        userDeviceDetails = new UserDeviceDetails(this);

        emailTxt = (EditText) findViewById(R.id.emailTxt);
        rqstPwd = (Button) findViewById(R.id.rqstPwd);
        rqstPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String emailAddress = emailTxt.getText().toString();

                if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    emailTxt.setError("Please enter a valid email address");
                } else {
                    forgetPasswordRequest();
                }

            }
        });
    }

    public void forgetPasswordRequest() {
//        RequestParams finalPayParams = new RequestParams();
        Map<String, String> finalPayParams = new HashMap<>();
        finalPayParams.put("username", emailTxt.getText().toString());
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, ForgetPasswordActivity.this);
//        handler.makeRequest("forgotPassword", "Requesting Password Reset", finalPayParams, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleForgetPasswordRequest(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleForgetPasswordRequest(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Forget password", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_FORGOTPASSWORD, finalPayParams, null, null);
    }

    public void handleForgetPasswordRequest(JSONObject response) throws JSONException {
        showMyAlertProgressDialog.showUserAlertDialog(response.getString("msg"), response.getString("msgTitle"));
        if (response.getString("msgTitle").equals("Success")) {
            emailTxt.setText("");
        }

    }
}
