package com.paybyonline.ebiz.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paybyonline.ebiz.App;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.usersession.FirstRunSession;

/**
 * Created by Sameer on 1/1/2018.
 */

public class TestSecondActivity extends AppCompatActivity {
    FirstRunSession firstRunSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        firstRunSession = new FirstRunSession(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Log.d("resume", "===>" + String.valueOf(firstRunSession.getFirstRunStatus()));
        super.onResume();
        if (!firstRunSession.getFirstRunStatus()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
//            apiSessionHandler.saveAppFirstState(false);
            setUpUrl();
        }
    }

    private void setUpUrl() {


        View view = getLayoutInflater().inflate(R.layout.dialog_setup_url, null);
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("Save Url", null)
                .setNegativeButton("CANCEL", null)
                .setView(view)
                .setTitle("Setting up the Application")
                .create();

        EditText baseUrl = (EditText) view.findViewById(R.id.baseUrl);

        builder.setOnShowListener(dialog -> {

            final Button btnAccept = builder.getButton(
                    AlertDialog.BUTTON_POSITIVE);

            btnAccept.setOnClickListener(v -> {
                builder.dismiss();
//                firstRunSession.saveAppFirstState(false);
                PayByOnlineConfig.BASE_URL = baseUrl.getText().toString();
                PayByOnlineConfig.SERVER_URL = PayByOnlineConfig.BASE_URL + "androidApp/";
                ((App) getApplicationContext()).setNewRetrofitInstance(PayByOnlineConfig.SERVER_URL);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });

            final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);

            btnDecline.setOnClickListener(v -> {
                        builder.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        finish();
                    }
            );
        });
        builder.show();
    }
}
