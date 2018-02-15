package com.paybyonline.ebiz.serverdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.paybyonline.ebiz.Activity.CreateAccountActivity;
import com.paybyonline.ebiz.Activity.ForgetPasswordActivity;
import com.paybyonline.ebiz.Activity.InitialLoadingActivity;
import com.paybyonline.ebiz.Activity.LoginActivity;
import com.paybyonline.ebiz.App;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Swatin on 3/16/2017.
 */

public class RetrofitHelper extends Fragment {

    private OnRetrofitResponse onResponse;
    private JSONObject responseJson = null;
    private ApiIndex.API api;
    private ProgressDialog progress;

    public interface OnRetrofitResponse {
        void onRetrofitSuccess(JSONObject jsonObject, int apiCode) throws JSONException;

        void onRetrofitFailure(String errorMessage, int apiCode);
    }

    public void setOnResponseListener(OnRetrofitResponse onResponse) {
        this.onResponse = onResponse;
    }

    public void sendRequest(int apiCode, Map<String, String> query, Map<String, String> header, Map<String, String> body) {
        Log.d("url", "===>" + PayByOnlineConfig.SERVER_URL);
        try {
            if (!(query == null)) {
                Log.d("RetrofitParams", "===>" + query.toString());
            } else if (!(body == null)) {
                Log.d("RetrofitParams", "===>" + body.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isConnected()) {
            if (body != null && !((getActivity() instanceof LoginActivity) || (getActivity() instanceof CreateAccountActivity))) {
                MyUserSessionManager myUserSessionManager = new MyUserSessionManager(getActivity());
                if (myUserSessionManager.getKeyAccessToken().equals(null)) {
                    body.put("accessToken", "");
                } else {
                    body.put("accessToken", myUserSessionManager.getKeyAccessToken());
                }

            }
            Call<ResponseBody> data = ApiIndex.getApi(api, apiCode, query, header, body);
            proceed(apiCode, data);
        } else {
            loadInitialErrorPage();
        }
    }

    private void proceed(final int apiCode, final Call<ResponseBody> data) {
        Log.e("RetrofitHelper", String.valueOf(apiCode));
        if (data != null) {
            showProgressDialog("Please Wait");
            data.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressDialog();
                    int responseCode = response.raw().code();
                    if (responseCode != 200) {
                        try {
                            Log.e("Error Body", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setFailResponseAppropriate(response.raw().headers().get("errorMessage"), apiCode);
                    } else {
                        try {
                            if (response.body() != null) {
                                String jsonString = response.body().string();
//                                Log.v("Body","==>"+String.valueOf(response.body().string()));
//                                if (checkJSONArray(response.body().string())){
//                                    JSONArray jsonArray = new JSONArray(response.body().string());
//                                    responseJson = new JSONObject(String.valueOf(jsonArray));
//                                }else {
//                                    responseJson = new JSONObject(response.body().string());
//                                }
                                Log.v("check", String.valueOf(checkJSONArray(jsonString)));
                                if (checkJSONArray(jsonString)) {
                                    JSONArray jsonArray = new JSONArray(jsonString);
                                    responseJson = new JSONObject();
                                    responseJson.put("array", jsonArray);
                                } else {
                                    responseJson = new JSONObject(jsonString);
                                }
//                                responseJson = new JSONObject(jsonString);
                                Log.e("RetrofitHelper", "Retrofit Json Fetched: " + responseJson.toString());
                                if (responseJson.has("msg") & !(getContext() instanceof ForgetPasswordActivity)) {
                                    if (responseJson.getString("msg").equals("User Authentication Failed")) {
                                        MyUserSessionManager userSessionManager = new MyUserSessionManager(getActivity());
//                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if (progress != null) {
                                            progress.dismiss();
//                                            getActivity().startActivity(intent);
                                        }
//                                        else {
//                                            getActivity().startActivity(intent);
//                                        }
                                        userSessionManager.logoutUser();

                                    } else {
                                        setSuccessResponseAppropriate(responseJson, apiCode);
                                        responseJson = null;
                                    }
                                } else {
                                    setSuccessResponseAppropriate(responseJson, apiCode);
                                    responseJson = null;
                                }
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    setFailResponseAppropriate(t.toString(), apiCode);
                    responseJson = null;
                }
            });
        } else {
            setFailResponseAppropriate("API NOT FOUND", apiCode);
        }
    }

    private void setFailResponseAppropriate(String errorMessage, int apiCode) {
        if (onResponse != null) {
            onResponse.onRetrofitFailure(errorMessage, apiCode);
        } else {
            Log.d("Retrofit::", "Listener not set");
        }
        hideProgressDialog();
    }

    private void setSuccessResponseAppropriate(JSONObject jsonObject, int apiCode) throws JSONException {
        if (onResponse != null) {
            onResponse.onRetrofitSuccess(jsonObject, apiCode);
        } else {
            Log.d("Retrofit::", "Listener not set");
        }
        hideProgressDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        api = ((App) context.getApplicationContext()).getRetrofitApi();
        progress = new ProgressDialog(context);
    }

    public void showProgressDialog(String message) {
        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "showProgressDialog");
        if (getActivity() != null && !progress.isShowing()) {
            SpannableString titleMsg = new SpannableString("Processing");
            titleMsg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleMsg.length(), 0);
            progress.setTitle(titleMsg);
            progress.setMessage(message);
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.show();
        }
    }

    public void hideProgressDialog() {
        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "hideProgressDialog");
        if (getActivity() != null && progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void loadInitialErrorPage() {
        Intent intent = new Intent(getContext(), InitialLoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    public boolean checkJSONArray(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        if (String.valueOf(stringBuilder.charAt(0)).equals("[")) {
            return true;
        } else {
            return false;
        }

    }
}
