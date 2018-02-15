package com.paybyonline.ebiz.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by Anish on 12/11/15.
 */
public class NotificationProvider {

    static CoordinatorLayout coordinatorLayout;
    static Context context;
    ProgressDialog progress;

    public NotificationProvider(CoordinatorLayout coordinatorLayout) {
        super();
        NotificationProvider.coordinatorLayout = coordinatorLayout;
    }

    public NotificationProvider(Context context) {
        super();
        NotificationProvider.context = context;
    }


    public void showMessage(String msg){
       Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showToast(String msg,Context context){
       Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }


    public static void notifyUser(String msg,CoordinatorLayout coordinatorLayout){
       Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    public void showProgressDialog(String message) {
        progress = new ProgressDialog(context);
        progress.setTitle("Processing");
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.show();
    }

    public void hideProgressDialog() {
        if(progress.isShowing()){ progress.dismiss();}
    }




}
