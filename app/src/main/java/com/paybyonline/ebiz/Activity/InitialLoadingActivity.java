package com.paybyonline.ebiz.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.App;
import com.paybyonline.ebiz.BuildConfig;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.custom.VersionChecker;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;


public class InitialLoadingActivity extends AppCompatActivity {
    private ProgressDialog progress;
    ImageView ivPlayOverlay;
    ImageView refresh;
    TextView head, base;
    CoordinatorLayout coordinatorLayout;
    //    PboServerRequestHandler pboServerRequestHandler;
    PackageInfo pInfo = null;
    //    RequestParams requestParams = new RequestParams();
//    ProgressBar progressbar;
    private RetrofitHelper retrofitHelper;
    VersionChecker versionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getPackageName().contains("MobaletTest")) {
                startActivity(new Intent(this, TestSecondActivity.class));
                finish();
            } else if (BuildConfig.DEBUG) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                ((App) getApplicationContext()).setNewRetrofitInstance(PayByOnlineConfig.SERVER_URL);
                setContentView(R.layout.activity_initial_loading);
                showProgressDialog("Please Wait...");
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
//        progressbar = (ProgressBar) findViewById(R.id.progressbar);
                ivPlayOverlay = (ImageView) findViewById(R.id.ivPlayOverlay);
                head = (TextView) findViewById(R.id.textHead);
                base = (TextView) findViewById(R.id.textbase);
                refresh = (ImageView) findViewById(R.id.refresh);
                versionChecker = new VersionChecker(this);
                head.setVisibility(View.GONE);
                base.setVisibility(View.GONE);
                refresh.setVisibility(View.GONE);
                if (getPackageName().contains("ebiz")) {
                    ivPlayOverlay.setImageResource(R.mipmap.ic_new_logo);
                } else if (getPackageName().contains("tirnus")) {
                    ivPlayOverlay.setImageResource(R.drawable.tirnus);
                } else if (getPackageName().contains("aimspay")) {
                    ivPlayOverlay.setImageResource(R.drawable.aimspay);
                } else {
                    ivPlayOverlay.setImageResource(R.mipmap.ic_launcher);
                }
//        progress = new ProgressDialog(this);
                ViewCompat.setTranslationZ(ivPlayOverlay, 0);
                ivPlayOverlay.bringToFront();

                String packageName = getPackageName();
//        pboServerRequestHandler = PboServerRequestHandler.getInstance(coordinatorLayout,InitialLoadingActivity.this);
                refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                progressbar.setVisibility(View.VISIBLE);
                        refresh.setVisibility(View.GONE);
                        head.setVisibility(View.GONE);
                        base.setVisibility(View.GONE);
                        if (!packageName.contains("guheshwori")) {
                            Log.v("not", packageName + "!guheswori");
                            doJsoupVersionCheck();
                        } else if (packageName.contains("MobaletTest")) {
                            loadAppPages();
                        } else {
                            loadAppPages();
                        }
//                doVersionCheck();
//                loadAppPages();
                    }
                });

                retrofitHelper = new RetrofitHelper();
                getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
/*            requestParams.put("installedAppVersionName", pInfo.versionName+"");
            requestParams.put("installedAppVersionCode", pInfo.versionCode+"");
            requestParams.put("applicationName", getPackageName()+"");*/

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//        progressbar.setVisibility(View.VISIBLE);
//        loadAppPages();
                if (packageName.contains("guheshwori")) {
                    loadAppPages();
                } else {
                    if (retrofitHelper.isConnected()) {
                        Log.v("not", packageName + "!guheswori");
                        doJsoupVersionCheck();
//            doVersionCheck();
                    } else {
//                progressbar.setVisibility(View.GONE);
                        refresh.setVisibility(View.VISIBLE);
                        head.setVisibility(View.VISIBLE);
                        base.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    public void doVersionCheck() {
////        pboServerRequestHandler.makeRequestService("verifyInstalledApp", "Please Wait !!!", requestParams, new PboServerRequestListener() {
////            @Override
////            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////              //  pboServerRequestHandler.showProgressDialog("Please Wait !!!");
////                try {
////                    Log.i("params",""+requestParams);
////                    handleVersionCheckResponse(response);
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                    pboServerRequestHandler.hideProgressDialog();
////                }
////            }
////        });
//        Map<String, String> requestParams = new HashMap<>();
//        requestParams.put("installedAppVersionName", "1.0.10");
//        requestParams.put("installedAppVersionCode", "10");
//        requestParams.put("applicationName", "com.gulfsewa.paybyonline");
//        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
//            @Override
//            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
//                try {
//                    Log.i("params", jsonObject.toString());
//                    handleVersionCheckResponse(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrofitFailure(String errorMessage, int apiCode) {
//                Log.i("Initial Loading", "error");
//                progressbar.setVisibility(View.GONE);
//            }
//        });
//        retrofitHelper.sendRequest(ApiIndex.GET_ON_VERIFY_INSTALLED_APP_ENDPOINT, requestParams, null, null);
//    }

//    public void handleVersionCheckResponse(JSONObject response) throws JSONException {
//
//        JSONObject jsonObject;
//        try {
//            jsonObject = response;
//            Log.i("VersionCheck", "" + response);
//            // pboServerRequestHandler.hideProgressDialog();
//            if (jsonObject.getString("isLatest").equals("NO")) {
//                appUpdateModel();
//            } else {
//                loadAppPages();
//            }
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            // pboServerRequestHandler.hideProgressDialog();
//        }
//
//
//    }

    public void appUpdateModel() {

        final AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("UPDATE", null)
//                .setNegativeButton("CANCEL", null)
                .setMessage("New version of application is available in play store. " +
                        "Please update.")
                .setTitle("Update Information")
                .setCancelable(false)
                .create();
        this.setFinishOnTouchOutside(false);
        builder.setCanceledOnTouchOutside(false);
        builder.setOnShowListener(dialog -> {
            final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            btnAccept.setOnClickListener(v -> {
                builder.dismiss();

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    finish();
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    finish();
                }

            });
           /* final Button btnCancel = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();

                }
            });*/

        });

        builder.show();

    }

    public void loadAppPages() {
//        progressbar.setVisibility(View.GONE);
        hideProgressDialog();
        if (retrofitHelper.isConnected()) {
            startActivity(new Intent(getApplicationContext(),
                    DashBoardActivity.class));
            InitialLoadingActivity.this.finish();
        } else {
//            progressbar.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
            head.setVisibility(View.VISIBLE);
            base.setVisibility(View.VISIBLE);
        }
    }

    void doJsoupVersionCheck() {
//        showProgressDialog("Please wait ..");
//        try {
//            ProgressDialog dialog = new ProgressDialog(this);
//            dialog.setMessage("Checkin app version");
//            String latestVersion = versionChecker.execute().get();
        versionChecker.execute(pInfo.versionName);
//            if (latestVersion.equals(pInfo.versionName)) {
//                Log.e("Ok", "App is upto date");
////                hideProgressDialog();
//                loadAppPages();
//
//            } else {
////                hideProgressDialog();
//                appUpdateModel();
//            }

//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//        } catch (Exception e) {
////            hideProgressDialog();
//            e.printStackTrace();
//        }
    }

    public void versionCheckReturn() {
        loadAppPages();
        hideProgressDialog();
    }

    public void hideProgressDialog() {
        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "hideProgressDialog");
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    public void showProgressDialog(String message) {
//        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "showProgressDialog");

        if (isConnected()) {
            if (progress == null) {
                progress = new ProgressDialog(this);
            }
            if (!progress.isShowing()) {
                SpannableString titleMsg = new SpannableString("Processing");
                titleMsg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleMsg.length(), 0);
                progress.setTitle(titleMsg);
                progress.setMessage(message);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }
        } else {
            hideProgressDialog();
        }

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
