//package com.paybyonline.ebiz.Activity;
//
//
//import android.app.ProgressDialog;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.paybyonline.ebiz.R;
//
//import java.text.DecimalFormat;
//import java.util.HashMap;
//
//public class BankDepositActivity extends AppCompatActivity  {
//
//    ProgressDialog prgDialog;
//    Button saveButton;
//    ImageView bankLogo;
//   TextInputLayout chequeVoucherNumberWrapper ;
//   TextInputLayout paymentNoteWrapper ;
//    TextView bankNameText;
//    CheckBox agreePayAgrement;
//    String encodedString;
//    String imgPath, fileName;
//    Bitmap bitmap;
//    private static int RESULT_LOAD_IMG = 1;
//    String errorMessage ="";
//
//    private HashMap<String, String> userDetails;
//    private static String url;
//    DecimalFormat formatter = new DecimalFormat("0.00");
//    CoordinatorLayout coordinatorLayout;
//    Integer selectedUserPayTypeIndex = -1;
//    String payTypeIds[];
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bank_deposit_form);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle("Bank Deposit");
//
//        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        initializeComponents();
//
//    }
//
//
//
//    public void initializeComponents(){
//    }
//
//}
