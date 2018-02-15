package com.paybyonline.ebiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import java.util.HashMap;

public class ContactUs extends AppCompatActivity {

    EditText name_edit_text;
    EditText name_message;
    Button btnSendMail;
    String name_text;
    String msg_text;
    TextView error_txt;
    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    CoordinatorLayout coordinatorLayout;
    private HashMap<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_us_page);

        error_txt=(TextView)findViewById(R.id.error_txt);
        name_edit_text=(EditText)findViewById(R.id.name_edit_text);
        name_message=(EditText)findViewById(R.id.name_message);
        btnSendMail=(Button)findViewById(R.id.btnSendMail);
        myUserSessionManager = new MyUserSessionManager(this);

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = name_edit_text.getText().toString();
                String message = name_message.getText().toString();

                if (subject.length() == 0 || message.length() == 0) {
                    if (subject.length() == 0) {
                        error_txt.setText("Email Subject Required!");
                    }
                    if (message.length() == 0) {
                        error_txt.setText("Email Body Required!");
                    }

                } else {
                    error_txt.setText("");
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL,
                            new String[] { "support@paybyonline.aimspay.com" });
                    // email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);

                    // need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email,
                            "Choose an Email client"));
                }
            }
        });
    }

}
