package com.paybyonline.ebiz.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.util.BasicPageData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PboWebViewActivity extends AppCompatActivity {

    WebView webView;
    ProgressDialog progDailog;
    private BasicPageData basicPageData;
    String webViewPage;
    Boolean dataObtained = false;
    private float m_downX;
    String bankCodeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbo_web_view);

        Bundle bundleData = getIntent().getExtras();

        String pageTitle = bundleData.getString("pageTitle");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(pageTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webv);
        basicPageData = new BasicPageData(PboWebViewActivity.this, getWindow().getDecorView().getRootView());
        webViewPage = bundleData.getString("webViewPage");
        if (webViewPage != null && (webViewPage.equals("Online Banking") || webViewPage.equals("Debit Credit Card"))) {
            createOnlineBankingWebView(bundleData);
        }
        if (pageTitle.equals("BSR Movies")) {
            launchWebView(bundleData.getString("url"));
        }
    }

    public void launchWebView(String launchUrl) {
        Log.d("LaunchURL", launchUrl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);

        progDailog = ProgressDialog.show(PboWebViewActivity.this, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        webView.loadUrl(launchUrl);
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
//                Log.i("msgss ","url ----- "+url);
//
//                if(webViewPage.equals("Online Banking")){
//                    handleOnlineBankingResponse(url);
//                }
//            }
//        });


        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progDailog.dismiss();
                Log.i("msgss ", "url ----- " + url);

                if (webViewPage != null && (webViewPage.equals("Online Banking") || webViewPage.equals("Debit Credit Card"))) {
                    handleOnlineBankingResponse(url);
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                invalidateOptionsMenu();
            }
        });
        webView.getSettings().setSupportZoom(true);
        //   webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setDisplayZoomControls(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

    }


    public void createOnlineBankingWebView(Bundle bundleData) {
        String shareUrl = null;
        String CHECKOUT = PayByOnlineConfig.CHECKOUT;
        if (webViewPage.equals("Debit Credit Card")) {
            CHECKOUT = PayByOnlineConfig.DEBIT_CREDIT_CARD_CHECKOUT;
        }
        try {
            shareUrl = CHECKOUT + "?" +
                    "userCode=" + URLEncoder.encode(basicPageData.getMyUserSessionManager().getSecurityCode(), "UTF-8") +
                    "&authenticationCode=" + URLEncoder.encode(basicPageData.getMyUserSessionManager().getAuthenticationCode(), "UTF-8") +
                    "&payUsingIds=" + bundleData.getString("payUsingIds") +
                    "&confirmPayUsings=" + bundleData.getString("confirmPayUsings") +
                    "&confirmPaymentNotes=" + bundleData.getString("confirmPaymentNotes") +
                    "&confirmPurchasedAmount=" + bundleData.getString("confirmPurchasedAmount") +
                    "&confirmDiscountAmount=" + bundleData.getString("confirmDiscountAmount") +
                    "&confirmDepositingAmount=" + bundleData.getString("confirmDepositingAmount") +
                    "&confirmPayingAmount=" + bundleData.getString("confirmPayingAmount") +
                    "&payAmt=" + bundleData.getString("payAmt") +
                    "&payTypeValue=" + bundleData.getString("payTypeValue") +
                    "&confirmTotalAmount=" + bundleData.getString("totalAmt") +
                    "&addDiscountWallet=" + bundleData.getString("addDiscountWallet") +
                    "&confirmPaymentProfileNames=" + bundleData.getString("confirmPaymentProfileNames") +
                    "&userIp=" + bundleData.getString("userIp");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        launchWebView(shareUrl);
    }

    public void handleOnlineBankingResponse(String url) {
        if (url.contains("/eBankingCheckoutFailed") && !dataObtained) {
            Uri uri = Uri.parse(url);
            String msg = uri.getQueryParameter("msg");
            dataObtained = true;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            startActivity(new Intent(PboWebViewActivity.this, DashBoardActivity.class));
            finish();
            ;
        }
        if (url.contains("/eBankingCheckOutResponse") && !dataObtained) {
            Uri uri = Uri.parse(url);
            String msg = uri.getQueryParameter("msg");
            dataObtained = true;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            startActivity(new Intent(PboWebViewActivity.this, DashBoardActivity.class));
            finish();
        }
        if(url.contains("/debitCreditCheckoutResponse") && !dataObtained) {
            Uri uri = Uri.parse(url);
            String msg = uri.getQueryParameter("msg");
            dataObtained = true;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            startActivity(new Intent(PboWebViewActivity.this, DashBoardActivity.class));
            finish();
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
