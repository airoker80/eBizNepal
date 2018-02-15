package com.paybyonline.ebiz.usersession;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sameer on 1/1/2018.
 */

public class FirstRunSession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final String PREFS_NAME = "ApiPrefs";

    public static  final  String FIRST_RUN ="firstRun";

    public FirstRunSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void  saveAppFirstState(Boolean firstRun){editor.putBoolean(FIRST_RUN,firstRun);editor.commit();}

    public Boolean getFirstRunStatus(){return  pref.getBoolean(FIRST_RUN,true);}
}
