package com.paybyonline.ebiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.multidex.MultiDexApplication;

import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.twitter.sdk.android.core.Twitter;

import java.util.concurrent.TimeUnit;

import inficare.net.sctlib.MerchantCredentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by hp on 7/17/2017.
 */

public class App extends MultiDexApplication {

    public ApiIndex.API api;

    @Override
    public void onCreate() {
        super.onCreate();
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Twitter.initialize(this);
        new MerchantCredentials(App.this)
                .setMerchantId(PayByOnlineConfig.SCTMERCHANTID) // merchant ID
                .setIcon(largeIcon) // image for notification bar
                .setmerchantPassword(PayByOnlineConfig.SCTMERCHANTPASSWORD) // merchant Password
                .setmerchantSignaturePasscode(PayByOnlineConfig.SCTMERCHANTSIGNATUREPASSWORD) // merchantSignature Passcode
                .setmerchantUsername(PayByOnlineConfig.SCTMERCHANTUSERNAME) // merchant Username
                .setIsSandbox(false) // true if u want to test in sand box else false
                .storeMerchantCredentials(); // store the data for further use in the application,
// if this method (storeMerchantCredentials())is not called sdk won't work as expected.
        createRetrofitInstance(PayByOnlineConfig.SERVER_URL);

    }

    public void createRetrofitInstance(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        httpClient1.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiIndex.API.class);
    }

    public void setNewRetrofitInstance(String url) {
        createRetrofitInstance(url);
    }

    public ApiIndex.API getRetrofitApi() {
        return api;
    }
}
