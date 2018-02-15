package com.paybyonline.ebiz.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.util.BasicPageData;

public class OnlineBankingActivity extends AppCompatActivity {


    WebView webView;
    ProgressDialog progDailog;
    private BasicPageData basicPageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_banking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        basicPageData = new BasicPageData(OnlineBankingActivity.this,getWindow().getDecorView().getRootView());
        Bundle bundleData = getIntent().getExtras();
        Log.i("msgsss","bundleData -------------- "+bundleData);

        String shareUrl= PayByOnlineConfig.NIBL_CHECKOUT+"?"+
                "userCode="+basicPageData.getMyUserSessionManager().getSecurityCode()+
                "&authenticationCode="+basicPageData.getMyUserSessionManager().getAuthenticationCode()+
                "&payUsingIds="+bundleData.getString("payUsingIds")+
                "&confirmPayUsings="+bundleData.getString("confirmPayUsings")+
                "&confirmPaymentNotes="+bundleData.getString("confirmPaymentNotes")+
                "&confirmPurchasedAmount="+bundleData.getString("confirmPurchasedAmount")+
                "&confirmDiscountAmount="+bundleData.getString("confirmDiscountAmount")+
                "&confirmDepositingAmount="+bundleData.getString("confirmDepositingAmount")+
                "&payAmt="+bundleData.getString("payAmt")+
                "&payTypeValue="+bundleData.getString("payTypeValue")+
                "&confirmTotalAmount="+bundleData.getString("totalAmt")+
                "&addDiscountWallet="+bundleData.getString("addDiscountWallet")+
                "&confirmPaymentProfileNames="+bundleData.getString("confirmPaymentProfileNames")+
                "&userIp="+bundleData.getString("userIp");

        webView = (WebView)findViewById(R.id.webv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        progDailog = ProgressDialog.show(OnlineBankingActivity.this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);

        webView.setWebViewClient(new WebViewClient(){

            Boolean dataObtained = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();

                Log.i("msgss ","url ----- "+url);
                if (url.contains("/niblCheckoutFailed?") && dataObtained != true) {
                    Uri uri = Uri.parse(url);
                    String msg = uri.getQueryParameter("msg");
                    Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OnlineBankingActivity.this, DashBoardActivity.class));
                    finish();;
                }
                if (url.contains("/niblCheckoutResponse?") && dataObtained != true) {
                    Uri uri = Uri.parse(url);
                    String msg = uri.getQueryParameter("msg");
                    Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OnlineBankingActivity.this, DashBoardActivity.class));
                    finish();
                }
            }
        });
        webView.loadUrl(shareUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
