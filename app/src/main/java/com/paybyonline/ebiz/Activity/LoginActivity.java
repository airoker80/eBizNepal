package com.paybyonline.ebiz.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

//import com.loopj.android.http.RequestParams;
//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

//import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult>, GraphRequest.Callback {

    Button loginBtn;
    TextView forgetPwdBtn;
    Button rechargeCodeBtn;
    TextView createAccBtn;
    EditText emailTxt;
    EditText pwdTxt;
    String email;
    String pwd;
    CoordinatorLayout coordinatorLayout;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    private HashMap<String, String> userDetails;
    private RetrofitHelper retrofitHelper;

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
//    private CallbackManager mCallbackManager;
//    private Button fbLoginBtn;
//    private Button twitterLogin;
//    private TwitterAuthClient twitterAuthClient;
//    PboServerRequestHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getActionBar().setDisplayShowTitleEnabled(false);
        // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_login);

        retrofitHelper = new RetrofitHelper();
        getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

//        mCallbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(mCallbackManager, this);

        myUserSessionManager = new MyUserSessionManager(this);
        if (myUserSessionManager.isUserLoggedIn()) {

            if (myUserSessionManager.ifUserHasCountry()) {
//				userDeviceDetails.showToast("user has country");
                startActivity(new Intent(this,
                        DashBoardActivity.class));
                finish();

            } else {
//				userDeviceDetails.showToast("user has no country");
                HttpAsyncTaskCountryList();
                //  startActivity(new Intent(this, CountrySelectionActivity.class));
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle(R.string.title_activity_login);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(this);
        userDeviceDetails = new UserDeviceDetails(this);


        emailTxt = (EditText) findViewById(R.id.emailTxt);
        pwdTxt = (EditText) findViewById(R.id.pwdTxt);

        loginBtn = (Button) findViewById(R.id.loginBtn);

//        if (getPackageName().contains("ebiz")) {
//            fbLoginBtn = (Button) findViewById(R.id.fblogin);
//            fbLoginBtn.setOnClickListener(this);
//            twitterLogin = (Button) findViewById(R.id.twitterLogin);
//            twitterLogin.setOnClickListener(this);
//        }

        forgetPwdBtn = (TextView) findViewById(R.id.forgetPwdBtn);
        rechargeCodeBtn = (Button) findViewById(R.id.rechargeCodeBtn);
        createAccBtn = (TextView) findViewById(R.id.createAccBtn);

        emailTxt = (EditText) findViewById(R.id.emailTxt);
        pwdTxt = (EditText) findViewById(R.id.pwdTxt);

        loginBtn.setOnClickListener(this);
        forgetPwdBtn.setOnClickListener(this);
        rechargeCodeBtn.setOnClickListener(this);
        createAccBtn.setOnClickListener(this);
    }

    public boolean checkText() {

        if (emailTxt.getText().length() > 0) {
            email = emailTxt.getText().toString();
            emailTxt.setError(null);
        } else {
            emailTxt.setError("Enter Email");
            return false;
        }
        if (pwdTxt.getText().length() > 0) {

            pwd = pwdTxt.getText().toString();
            pwdTxt.setError(null);


        } else {

            pwdTxt.setError("Enter Password");
            return false;
        }
        return true;
    }

//    private void loginFacebook() {
//        showMyAlertProgressDialog.showProgressDialog("Logging In", "Please wait...");
//        if (AccessToken.getCurrentAccessToken() == null
//                || AccessToken.getCurrentAccessToken().isExpired()) {
//            LoginManager.getInstance()
//                    .logInWithReadPermissions(this, Arrays.asList(EMAIL, PUBLIC_PROFILE));
//        } else {
//            makeGraphCall();
//        }
//    }

//    private void twitterLogin() {
//        showMyAlertProgressDialog.showProgressDialog("Logging In", "Please Wait...");
//        twitterAuthClient = new TwitterAuthClient();
//        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                Log.d("Twitter Login", "Success");
//                getTwitterProfile();
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("Twitter Login", "Failed");
//            }
//        });
//    }


//    private void getTwitterProfile() {
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//        AccountService accountService = twitterApiClient.getAccountService();
//        Call<User> call = accountService.verifyCredentials(true, true, true);
//        call.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
//            @Override
//            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
//                User user = result.data;
//                String fullname = user.name;
//                String twitterID = String.valueOf(user.getId());
//                String userSocialProfile = user.profileImageUrl;
//                String userEmail = user.email;
//                String userFirstNmae = fullname.substring(0, fullname.lastIndexOf(" "));
//                String userLastNmae = fullname.substring(fullname.lastIndexOf(" "));
//                String userScreenName = user.screenName;
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
//            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
//        } else {
//            mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onClick(View v) {


        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bink);
        switch (v.getId()) {

            case R.id.loginBtn:
                loginBtn.startAnimation(animation1);
                if (checkText()) {
                    HttpAsyncTask();
                }

                // startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                break;

//            case R.id.fblogin:
//                loginFacebook();
//                break;
//
//            case R.id.twitterLogin:
//                twitterLogin();
//                break;

            case R.id.forgetPwdBtn:
                forgetPwdBtn.startAnimation(animation1);
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;


            case R.id.rechargeCodeBtn:
                rechargeCodeBtn.startAnimation(animation1);
                startActivity(new Intent(this, UseRechargeCodeActivity.class));
                break;


            case R.id.createAccBtn:
                //  creatAccBtn.startAnimation(animation1);
                startActivity(new Intent(this, CreateAccountActivity.class));
                break;

        }

    }

    public void HttpAsyncTask() {


//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        Log.i("userLogin", "Processing");
        params.put("password", pwd);
        params.put("username", email);
        Log.i("userLogin", "params " + params);
        // Make Http call
/*        handler = PboServerRequestHandler.getInstance(coordinatorLayout, this);

        handler.makeRequest("userLogin", "Please Wait !!!", params, new PboServerRequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.i("userLogin", "userLogin1");
                    HttpAsyncTaskResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
            }
        });*/
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("userLogin", "userLogin1");
                    HttpAsyncTaskResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("userLogin", "userLogin error");
                Toast.makeText(getBaseContext(), "Could not connect", Toast.LENGTH_SHORT).show();
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_USER_LOGIN, params, null, null);
    }

    public void HttpAsyncTaskResponse(JSONObject json) throws JSONException {


        String loginStatus = "";
        String username = "";
        String connectionStatus;

        try {
            //  loginBtn.setEnabled(true);
            Log.i("HttpAsyncTaskResponse", "" + json);


            loginStatus = json.getString("msgTitle");

            if (loginStatus.equals("Success")) {

                myUserSessionManager.saveUserInformation(username,
                        json.getString("authenticationCode"), json.getString("userCode"));

                if (json.getString("countryAdded")
                        .equals("present")) {
                    myUserSessionManager.addUserCountry();
                }

                if (myUserSessionManager.ifUserHasCountry()) {
                    boolean isNewUser = json.getBoolean("isNewUser");
                    boolean isPrimaryMailVerified = json.getBoolean("isPrimaryMailVerfied");
                    boolean isSecondaryMailVerified = json.getBoolean("isSecondaryMailVerified");
                    if (isNewUser && !isPrimaryMailVerified && !isSecondaryMailVerified) {
                        forceUserEmailVerify();
                    } else if (!isPrimaryMailVerified && !isSecondaryMailVerified) {
                        giveUserOptionToUpdateOrVerifyEmail(false);
                    } else {
                        continueToDashboard();
                    }
                } else {
//                    HttpAsyncTaskCountryList();
                    startActivity(new Intent(getApplicationContext(), AfterRegistration.class));
                }
            } else {
                Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_SHORT).show();
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

    private void forceUserEmailVerify() {
        giveUserOptionToUpdateOrVerifyEmail(true);
    }

    private void giveUserOptionToUpdateOrVerifyEmail(boolean isForced) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogview = inflater.inflate(R.layout.user_email_verify, null);
        View emailView = dialogview.findViewById(R.id.addEmailView);
        Button btnEmail = (Button) dialogview.findViewById(R.id.btnAddEmail);
        TextView tvEmailText = (TextView) dialogview.findViewById(R.id.notVerifiedText);
        if (isForced) {
            tvEmailText.setText(getString(R.string.email_verify_text_new_user));
            btnEmail.setVisibility(View.GONE);
        }
        btnEmail.setOnClickListener(v -> {
            emailView.setVisibility(View.VISIBLE);
            btnEmail.setVisibility(View.GONE);
        });
        TextInputEditText edtEmail = (TextInputEditText) dialogview.findViewById(R.id.edtAddEmail);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        AlertDialog emailDialog;
        if (isForced) {
            emailDialog = dialogBuilder.setTitle("Important")
                    .setView(dialogview)
                    .setPositiveButton("Ok", null)
                    .setCancelable(false)
                    .create();
        } else {
            emailDialog = dialogBuilder.setTitle("Important")
                    .setView(dialogview)
                    .setPositiveButton("Send Link", null)
                    .setNegativeButton("Later", null)
                    .setCancelable(false)
                    .create();
        }
        emailDialog.show();
        emailDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
            emailDialog.dismiss();
            continueToDashboard();
        });
        emailDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            String emailAddress = edtEmail.getText().toString().trim();
            if (isForced) {
                emailDialog.dismiss();
            } else {
                if (emailAddress.isEmpty()) {
                    sendMailToOldAddress(emailDialog);
                } else if (!validateEmail(emailAddress)) {
                    edtEmail.setError("Invalid email");
                } else {
                    sendNewEmail(emailDialog, emailAddress);
                }
            }
        });
    }

    private void sendMailToOldAddress(Dialog emailDialog) {
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "kyc");
        params.put("childTask", "sendMailExistingUser");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) throws JSONException {
                emailDialog.dismiss();
                handleSentMailResponse(jsonObject);
                continueToDashboard();
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                emailDialog.dismiss();
                Toast.makeText(getBaseContext(), "Could not connect", Toast.LENGTH_SHORT).show();
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    private void handleSentMailResponse(JSONObject response) throws JSONException {
        Log.e("responsesentMail", response.toString());
        if (response.getString("msgTitle").equals("Success")) {
            Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
        }

    }

    private void sendNewEmail(Dialog emailDialog, String emailAddress) {
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "kyc");
        params.put("childTask", "updateSecMail");
        params.put("getEmail", emailAddress);
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) throws JSONException {
                Log.i("PboServerRequestHandler", "dashboard");
                emailDialog.dismiss();
                handleSentMailResponse(jsonObject);
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                emailDialog.dismiss();
                Toast.makeText(getBaseContext(), "Could not connect", Toast.LENGTH_SHORT).show();
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    private boolean validateEmail(String email) {
        String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && !email.isEmpty();
    }

    private void continueToDashboard() {
        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void HttpAsyncTaskCountryList() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        // settings = getSharedPreferences(PREFS_NAME,
        // Context.MODE_PRIVATE);
        userDetails = myUserSessionManager.getUserDetails();
        Log.i("userDetails", userDetails + "");
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getCountryDetails");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("username", userDetails.get(MyUserSessionManager.KEY_USERNAME));

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, LoginActivity.this);
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait", params,
//                new PboServerRequestListener() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//                            HttpAsyncTaskCountryListResponse(response);
//
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
                    HttpAsyncTaskCountryListResponse(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Login", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void HttpAsyncTaskCountryListResponse(JSONObject response) throws JSONException {

        JSONObject json;
        String connectionStatus;
        json = new JSONObject();
        try {

            json = response;
            Log.i("Result", "" + response);
            //connectionStatus = json.getString("connectionStatus");
            if (json.getString("countryAdded").equals("present")) {

                myUserSessionManager.addUserCountry();

                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            } else if (json.getString("countryAdded").equals(
                    "NotPresent")) {

                JSONObject countryDetails = json
                        .getJSONObject("result");
                Log.i("Result", "" + json.getJSONObject("result"));

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

    private void makeGraphCall() {
        String ME_ENDPOINT = "/me";
        Bundle params = new Bundle();
        params.putString("fields", "picture.width(150).height(150),first_name,last_name,address,birthday,gender,id,email");


        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                ME_ENDPOINT,
                params,
                HttpMethod.GET,
                this
        );
        request.executeAsync();
    }

    private void sendFbDataToServer() {

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        makeGraphCall();
    }

    @Override
    public void onCancel() {
        showMyAlertProgressDialog.hideProgressDialog();
    }

    @Override
    public void onError(FacebookException error) {
        showMyAlertProgressDialog.hideProgressDialog();
    }

    @Override
    public void onCompleted(GraphResponse response) {
        Log.d("facebook", "got it");
        try {
            JSONObject jsonObject = new JSONObject(response.getRawResponse());
            String firstName = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String email = jsonObject.getString("email");
            String id = jsonObject.getString("id");
            String gender = "";
            if (jsonObject.has("gender")) {
                gender = jsonObject.getString("gender");
            }
            sendFbDataToServer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showMyAlertProgressDialog.hideProgressDialog();
    }
}
