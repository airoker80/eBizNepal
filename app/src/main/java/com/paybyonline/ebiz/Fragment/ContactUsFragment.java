package com.paybyonline.ebiz.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.R;


/**
 * Created by Anish on 9/8/2016.
 */
public class ContactUsFragment extends Fragment {

    EditText subject;
    EditText message;
    Button btnSendMail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us_page, container, false);

        try {
            if (!getActivity().getPackageName().contains("ebiz")) {
                view.findViewById(R.id.feedbackLayout).setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        //((DashBoardActivity) getActivity()).setTitle("Contact Us");
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Contact Us");
        ((DashBoardActivity) getActivity()).setFragmentName("Contact Us");
        message = (EditText) view.findViewById(R.id.message);
        subject = (EditText) view.findViewById(R.id.subject);
        btnSendMail = (Button) view.findViewById(R.id.btnSendMail);
        btnSendMail.setOnClickListener(view1 -> {
            if (validateForm()) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"info@mobalet.com"});
                // email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                email.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                email.putExtra(Intent.EXTRA_TEXT, message.getText().toString());

                // need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email,
                        "Choose an Email client"));
            }
        });
        return view;
    }

    public Boolean validateForm() {

        if (subject.getText().toString().isEmpty()) {
            subject.setError("Required");
            return false;
        }
        if (message.getText().toString().isEmpty()) {
            message.setError("Required");
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Contact Us");
        ((DashBoardActivity) getActivity()).setFragmentName("Contact Us");
        super.onResume();
    }
}
