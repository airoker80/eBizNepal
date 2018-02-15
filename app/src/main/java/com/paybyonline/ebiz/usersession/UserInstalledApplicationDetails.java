package com.paybyonline.ebiz.usersession;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by mefriend24 on 9/16/15.
 */
public class UserInstalledApplicationDetails {

    String packageName;
    Context context;

    public UserInstalledApplicationDetails(Context context) {
        super();
        this.context = context;
    }

    public Boolean checkIfApplicationIsInstalled(String requestedAppName) {

        if(requestedAppName.equals("Facebook")){
            packageName = "com.facebook.katana" ;
        }else if(requestedAppName.equals("Email")){
            packageName = "com.android.email" ;
        }else if(requestedAppName.equals("Gmail")){
            packageName = "com.google.android.gm" ;
        }else if(requestedAppName.equals("Linkedin")){
            packageName = "com.linkedin.android" ;
        }else if(requestedAppName.equals("Twitter")){
            packageName = "com.twitter.android" ;
        }else if(requestedAppName.equals("Yahoo Mail")){
            packageName = "com.yahoo.mobile.client.android.mail" ;
        }else if(requestedAppName.equals("Facebook Messenger")){
            packageName = "com.facebook.orca facebook" ;
        }else if(requestedAppName.equals("Google+")){
            packageName = "com.google.android.apps.plus" ;
        }else if(requestedAppName.equals("Skype")){
            packageName = "com.skype.raider" ;
        }else if(requestedAppName.equals("Viber")){
            packageName = "com.viber.voip" ;
        }else if(requestedAppName.equals("Microsoft Outlook")){
            packageName = "com.microsoft.office.outlook" ;
        }else{
            packageName = "";
        }

        PackageManager pm = context.getPackageManager();
        boolean flag = false;
        try {
            pm.getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            flag = true;
        } catch (PackageManager.NameNotFoundException e) {
            flag = false;
        }
        return flag;
    }

    public String getPackageName() {
        return packageName;
    }
}
