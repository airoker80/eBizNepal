package com.paybyonline.ebiz.custom;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.paybyonline.ebiz.Activity.InitialLoadingActivity;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Sameer on 11/6/2017.
 */
public class VersionChecker extends AsyncTask<String, String, String> {

    String newVersion;
    Context context;

    //    ProgressDialog dialog = null;
    public VersionChecker(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
//        dialog=new ProgressDialog(context);
//        dialog.setMessage("Checking app version");
//        Log.v("backgg","backgg");
//        dialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        Log.v("check", "checking");
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        if (newVersion.equals(params[0])) {
            return newVersion;
        } else {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String newVersion) {
//        if (dialog.isShowing()) {
//            Log.v("dismish","dismish");
//            dialog.dismiss();
//        }
        if (newVersion.isEmpty()) {
            ((InitialLoadingActivity) context).hideProgressDialog();
            ((InitialLoadingActivity) context).appUpdateModel();
        } else {
            ((InitialLoadingActivity) context).versionCheckReturn();
        }
    }
}