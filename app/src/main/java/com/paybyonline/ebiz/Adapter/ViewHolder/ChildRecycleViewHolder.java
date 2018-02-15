package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.AutoCompleteAdapter;
import com.paybyonline.ebiz.Adapter.ContactAdapter;
import com.paybyonline.ebiz.Adapter.Model.ContactsModel;
import com.paybyonline.ebiz.Adapter.Model.FavouriteModel;
import com.paybyonline.ebiz.Adapter.Model.ServiceType;
import com.paybyonline.ebiz.Adapter.Model.TextViewStyling;
import com.paybyonline.ebiz.Fragment.BuyProductFormFragment;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

//import cz.msebera.android.httpclient.Header;


/**
 * Created by mefriend24 on 9/20/16.
 */

public class ChildRecycleViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, DialogInterface.OnClickListener, ContactAdapter.OnContactCallBack {

    private final RetrofitHelper retrofitHelper;
    List<ContactsModel> ncellContact, ntcContact = new ArrayList<>();
    TextView childTextView;

    String dhRechargeValue = "";
    String isDH = "";
    //    TextView rechargeTargetNumberError;
    TextView rechargeServiceName;
    TextView prepmob;
    TextView pinRequest;
    Spinner amtSpin;
    EditText amtEdit;
    EditText dh_cas_id;
    LinearLayout childLayout;
    Context context;
    LayoutInflater inflater;
    static int previousViewColor = 0;
    AlertDialog dialog;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private EditText confirmationPin;
    private EditText pinEnterEditText;
    private EditText pinReenterEditText;
    private EditText enterPasswordEditText;
    private AutoCompleteTextView mobnoTxt;
    private EditText dealCode, dhDealCode;
    private Button btnPaynow;
    String isPinRecharge;
    String scstAmountType;
    InputMethodManager imm;
    String startsWith = "";
    String categoryLength = "";
    String maxVal = "";
    private String purchasedAmountType = "";
    String pinCodePresent = "NO";
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    private HashMap<String, String> userDetails;
    CoordinatorLayout coordinatorLayout;
    private String rechargeTargetNumberErrorMsg = "";
    private String rechargeTargetAmountErrorMsg = "";
    private String purchasedAmountValue = "";
    String serviceTypeId;
    String serviceCatId;
    String minVal = "";
    String iLabel = "";
    View customView;
    Boolean ifFormIsShown = true;
    Boolean isRechargeFromNotShownOnLoading = true;
    private static int RESULT_LOAD_IMG = 5;
    ImageView contactIcon;
    private Uri uriContact;
    View childItemView;
    private String contactID;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    Dialog pinRequestDialog;

    String dealFlag = "";
    String dealRemarks = "";
    Button checkDealBtn;
    RelativeLayout promoCodeLayout;
    Boolean promoCodeLayoutVisible = false;
    List<ContactsModel> contactsModelList = new ArrayList<>();


    public ChildRecycleViewHolder(View itemView) {
        super(itemView);

        retrofitHelper = new RetrofitHelper();
        context = itemView.getContext();
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        }

        childTextView = (TextView) itemView.findViewById(R.id.childTextView);
        childLayout = (LinearLayout) itemView.findViewById(R.id.childLayout);
        context = itemView.getContext();

        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(context);
        userDeviceDetails = new UserDeviceDetails(context);
        myUserSessionManager = new MyUserSessionManager(context);
        childItemView = itemView;

    }

    public void showRechargeForm(LinearLayout rechargeFormLayout, ServiceType serviceType) {
        inflater = LayoutInflater.from(context);
        Log.d("Servictype", "--->" + serviceType);
        if (serviceType.getService_type_name().equals("DishHome Topup")) {
            isDH = "dh";
            Log.d("scIddd", serviceType.getService_type_name());
            customView = inflater.inflate(R.layout.dishhome_form, null);
            RadioGroup dh_rg = (RadioGroup) customView.findViewById(R.id.dh_rg);
            dh_cas_id = (EditText) customView.findViewById(R.id.dh_cas_id);
            dhDealCode = (EditText) customView.findViewById(R.id.dhDealCode);
            Button dhCheckDealBtn = (Button) customView.findViewById(R.id.dhCheckDealBtn);
            final LinearLayout gone_dh_ll = (LinearLayout) customView.findViewById(R.id.gone_dh_ll);
            Button dhBtnPaynow = (Button) customView.findViewById(R.id.dhBtnPaynow);
            amtSpin = (Spinner) customView.findViewById(R.id.amtSpin);
            final TextView dishomeServiceName = (TextView) customView.findViewById(R.id.dishomeServiceName);
            dh_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.dh_cas_rb:
                            gone_dh_ll.setVisibility(View.VISIBLE);
                            dishomeServiceName.setText("CAS ID");
                            break;
                        case R.id.dh_chip_rb:
                            gone_dh_ll.setVisibility(View.VISIBLE);
                            dishomeServiceName.setText("CHIP-ID");
                            break;
                    }

                }
            });
            if (rechargeFormLayout.getChildCount() > 0) {
                rechargeFormLayout.removeAllViews();
            }

            dhCheckDealBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dhDealCode.setError(null);
                    if (dhDealCode.getText().toString().length() > 0) {
                        verifyUserDeal("dh");
                    } else {
                        dhDealCode.setError("Please enter code");
                    }
                }
            });


            String[] output = serviceType.getScst_amount_value().split(",");
            ArrayAdapter adapter1 = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, output);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            amtSpin.setAdapter(adapter1);

            amtSpin.setOnItemSelectedListener(onServiceAmountValueSelectedListener);


            dhBtnPaynow.setOnClickListener(v -> {
//                    handleRechargeBtnClick();
                dhRechargeValue = dh_cas_id.getText().toString();
                purchasedAmountValue = amtSpin.getSelectedItem().toString();
                if (dishomeServiceName.getText().toString().equals("CAS ID")) {
                    try {
                        if (dh_cas_id.getText().toString().substring(0, Math.min(dh_cas_id.getText().toString().length(), 4)).equals("7190") & dh_cas_id.getText().toString().length() == 11) {
                            Log.d("first4", "===>" + "ok");
                            transactionConfirmModel();
                        } else if (!dh_cas_id.getText().toString().substring(0, Math.min(dh_cas_id.getText().toString().length(), 4)).equals("7190")) {
                            dh_cas_id.setError("First 4 characters must start with 7190");
                        } else if (dh_cas_id.getText().toString().length() != 11) {
                            dh_cas_id.setError("Numnber of characters must be exactly 11");
                        }
                    } catch (Exception e) {
                        dh_cas_id.setError("Invalid characters");
                        e.printStackTrace();
                    }
                    Log.d("first4", "===>" + dh_cas_id.getText().toString().substring(0, Math.min(dh_cas_id.getText().toString().length(), 4)) + "=== " + String.valueOf(dh_cas_id.getText().toString().length()));
                } else if (dishomeServiceName.getText().toString().equals("CHIP-ID")) {
                    try {
                        if (dh_cas_id.getText().toString().substring(0, Math.min(dh_cas_id.getText().toString().length(), 1)).equals("0") & dh_cas_id.getText().toString().length() == 10) {
                            Log.d("first4", "===>" + "ok");
                            transactionConfirmModel();
                        } else if (!dh_cas_id.getText().toString().substring(0, Math.min(dh_cas_id.getText().toString().length(), 1)).equals("0")) {
                            dh_cas_id.setError("First character must start with 0");
                        } else if (dh_cas_id.getText().toString().length() != 10) {
                            dh_cas_id.setError("Numnber of characters must be exactly 10");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            rechargeFormLayout.addView(customView);
            rechargeFormLayout.setVisibility(View.VISIBLE);

        } else if (serviceType.getService_type_name().equals("Worldlink")) {
            Log.i("msgss", "Worldlink clicked");

            Fragment fragment = new BuyProductFormFragment();
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            Bundle bundle = new Bundle();
            bundle.putString("scstId", serviceType.getScstId());
            fragmentTransaction.addToBackStack(null);
            fragment.setArguments(bundle);
            fragmentTransaction.commit();
        } else {
            Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "serviceType.getIsProductEnable() " + serviceType.getIsProductEnable());

            if (rechargeFormLayout.getChildCount() > 0) {
                rechargeFormLayout.removeAllViews();
            }

            if (serviceType.getIsProductEnable().equals("Y")) {

                customView = inflater.inflate(R.layout.recharge_form, null);
                amtEdit = (EditText) customView.findViewById(R.id.amtEdit);
                promoCodeLayout = (RelativeLayout) customView.findViewById(R.id.promoCodeLayout);
                dealCode = (EditText) customView.findViewById(R.id.dealCode);
                if (serviceType.getEnablePromoCode().equals("Y")) {
                    promoCodeLayout.setVisibility(View.VISIBLE);
                    promoCodeLayoutVisible = true;
                } else {
                    promoCodeLayout.setVisibility(View.GONE);
                    promoCodeLayoutVisible = false;
                }
                dealCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        dealFlag = "";
                        dealRemarks = "";
                    }
                });
                mobnoTxt = (AutoCompleteTextView) customView.findViewById(R.id.mobnoTxt);
                ImageView contactIcon = (ImageView) customView.findViewById(R.id.contactIcon);
                if (getContacts().size() > 0) {
                    contactsModelList = getContacts();
                    getBothContacts(ncellContact, ntcContact);
                    if (serviceType.getService_type_name().equals("NTC")) {
                        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(context, ntcContact);
//                        mobnoTxt.setAdapter(autoCompleteAdapter);
                    } else if (serviceType.getService_type_name().equals("NCELL")) {
                        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(context, ncellContact);
//                        mobnoTxt.setAdapter(autoCompleteAdapter);
                    } else {
                        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(context, contactsModelList);
//                        mobnoTxt.setAdapter(autoCompleteAdapter);
                    }

                }
//                contactIcon.setVisibility(View.VISIBLE);

                contactIcon.setOnClickListener(v -> {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.dialog_list_fav_items, null);
                    RecyclerView fav_item_list = (RecyclerView) dialogView.findViewById(R.id.fav_item_list);
                    ContactAdapter contactAdapter = new ContactAdapter(contactsModelList, context, this);
                    fav_item_list.setLayoutManager(new LinearLayoutManager(context));
                    fav_item_list.setAdapter(contactAdapter);

                    final android.support.v7.app.AlertDialog dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context)
                            .setView(dialogView)
                            .setTitle("Select Contacts")
                            .setPositiveButton("Change", null)
                            .setNegativeButton("Cancel", null)
                            .setCancelable(false)
                            .create();

                    dialogBuilder.setOnShowListener(dialog -> {
                        final Button btnAccept = dialogBuilder.getButton(
                                android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                        btnAccept.setOnClickListener(v2 -> dialogBuilder.dismiss());
                        final Button btnDecline = dialogBuilder.getButton(DialogInterface.BUTTON_NEGATIVE);
                        btnDecline.setOnClickListener(v1 -> {
                            dialogBuilder.dismiss();
                        });
                    });
                    Log.i("onclick2", "dialog call");

                    dialogBuilder.show();


                });


                amtSpin = (Spinner) customView.findViewById(R.id.amtSpin);
                btnPaynow = (Button) customView.findViewById(R.id.btnPaynow);
                contactIcon = (ImageView) customView.findViewById(R.id.contactIcon);
                rechargeServiceName = (TextView) customView.findViewById(R.id.rechargeServiceName);
                prepmob = (TextView) customView.findViewById(R.id.prepmob);
                rechargeServiceName.setText(serviceType.getService_type_name());
                prepmob.setText(serviceType.getiLabel());
                mobnoTxt.setHint(serviceType.getiLabel());

                dealFlag = "";
                dealRemarks = "";

                if (isPinRecharge.equals("true")) {
                    prepmob.setVisibility(View.GONE);
                    mobnoTxt.setVisibility(View.GONE);
                    contactIcon.setVisibility(View.GONE);
                }

                if (serviceType.getScstAmountType().equals("SELECT")) {

                    amtSpin.setVisibility(View.VISIBLE);
                    amtEdit.setVisibility(View.GONE);
//                    contactIcon.setVisibility(View.GONE);
                    String[] output = serviceType.getScst_amount_value().split(",");
                    ArrayAdapter adapter1 = new ArrayAdapter(context,
                            android.R.layout.simple_spinner_item, output);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    amtSpin.setAdapter(adapter1);

                    amtSpin.setOnItemSelectedListener(onServiceAmountValueSelectedListener);

                } else {
                    amtEdit.setVisibility(View.VISIBLE);
                    amtSpin.setVisibility(View.GONE);
                }

                setTargetNumberValidationMessage();

                btnPaynow.setOnClickListener(v -> handleRechargeBtnClick());

                checkDealBtn = (Button) customView.findViewById(R.id.checkDealBtn);
                checkDealBtn.setOnClickListener(view -> {
                    dealCode.setError(null);
                    if (dealCode.getText().toString().length() > 0) {
                        verifyUserDeal("o");
                    } else {
                        dealCode.setError("Please enter code");
                    }
                });

//                contactIcon.setVisibility(View.GONE);
//                contactIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent contactIntent = new Intent(Intent.ACTION_PICK,
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//
//                        ((Activity) context).startActivityForResult(contactIntent, REQUEST_CODE_PICK_CONTACTS);
//
//                    }
//
//                });
                rechargeFormLayout.addView(customView);

            } else {
                TextView textView = new TextView(context);
                textView.setPadding(10, 10, 10, 10);
                textView.setText("Service not available now");
                rechargeFormLayout.addView(textView);
            }

            rechargeFormLayout.setVisibility(View.VISIBLE);

        }


    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == Activity.RESULT_OK
                && null != data) {
            Log.d("Contact Number:", "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactNumber();


        }
    }*/
    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = context.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d("Contact Id", "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();
        mobnoTxt.setText(contactNumber);
        prepmob.setText(contactNumber);
        Log.d("Contact Number", "Contact Phone Number: " + contactNumber);
    }

    public void bind(final ServiceType serviceType, final LinearLayout rechargeFormLayout,
                     CoordinatorLayout coordinatorLayout, String serviceCategoryId, String servTypeId, int parentPosition) {
//                     CoordinatorLayout coordinatorLayout,String serviceCategoryId,String servTypeId,Boolean ifClick) {


        Log.i("msgss", "3child view  binding");
        Log.i("serv---=-----", servTypeId);
        if (serviceType.getIsProductEnable().equals("Y")) {
            int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

            // to stop color from repeating in adjacent layout
            while (previousViewColor == randomAndroidColor) {
                randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            }

            previousViewColor = randomAndroidColor;

            childItemView.setBackgroundColor(randomAndroidColor);


            this.coordinatorLayout = coordinatorLayout;
            serviceTypeId = serviceType.getService_type_id();
            serviceCatId = serviceCategoryId;
            childTextView.setText(serviceType.getService_type_name());
            startsWith = serviceType.getStartsWith();
            categoryLength = serviceType.getCategoryLength();
            minVal = serviceType.getMinVal();
            scstAmountType = serviceType.getScstAmountType();
            purchasedAmountType = serviceType.getScstAmountType();
            iLabel = serviceType.getiLabel();
            maxVal = serviceType.getMaxVal();
            isPinRecharge = serviceType.getIsPinRechargeService();

            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


/*                <objectAnimator
                android:duration="@android:integer/config_shortAnimTime"
                android:propertyName="translationZ"
                android:valueTo="8dp"
                android:valueType="floatType"/>*/
/*
                ObjectAnimator animation = ObjectAnimator.ofFloat(childLayout, "rotationY", 0.0f, 360f);
                animation.setDuration(3600);
                animation.setPropertyName("translationX");
                animation.setRepeatCount(ObjectAnimator.INFINITE);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();*/

                /*childLayout
                        .startAnimation(
                                AnimationUtils.loadAnimation(context, R.anim.linear_interpolator) );*/

                /*if(rechargeFormLayout.isShown()){
                    rechargeFormLayout.setVisibility(View.GONE);
                }else{
                    if(serviceCatId.equals("19")){
                        Log.i("msgss","sim tv clicked");
                        Fragment fragment = new BuyProductFormFragment();
                        FragmentManager fragmentManager = ((FragmentActivity) context).
                                getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        Bundle bundle = new Bundle();
                        bundle.putString("scstId", "24");
                        fragmentTransaction.addToBackStack(null);
                        fragment.setArguments(bundle);
                        fragmentTransaction.commit();

                    }else{
                        showRechargeForm( rechargeFormLayout, serviceType);
                    }

                }*/


                    switch (serviceType.getService_type_name()) {
                        case "SIM TV": {
                            Fragment fragment = new BuyProductFormFragment();
                            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            Bundle bundle = new Bundle();
                            bundle.putString("scstId", serviceType.getScstId());
                            fragmentTransaction.addToBackStack(null);
                            fragment.setArguments(bundle);
                            fragmentTransaction.commit();

                            break;
                        }
                        case "SUBISU": {
                            Fragment fragment = new BuyProductFormFragment();
                            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            Bundle bundle = new Bundle();
                            bundle.putString("scstId", serviceType.getScstId());
                            fragmentTransaction.addToBackStack(null);
                            fragment.setArguments(bundle);
                            fragmentTransaction.commit();
                            break;
                        }
                        case "Subisu Direct": {

                            Fragment fragment = new BuyProductFormFragment();
                            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            Bundle bundle = new Bundle();
                            bundle.putString("scstId", serviceType.getScstId());
                            fragmentTransaction.addToBackStack(null);
                            fragment.setArguments(bundle);
                            fragmentTransaction.commit();
                            break;
                        }

//                    case "50": {
//                        Log.i("msgss", "Worldlink clicked");
//                        Fragment fragment = new BuyProductFormFragment();
//                        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.content_frame, fragment);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("scstId", "191");
//                        fragmentTransaction.addToBackStack(null);
//                        fragment.setArguments(bundle);
//                        fragmentTransaction.commit();
//
//                        break;
//                    }
                        default:
                            Log.d("cat", serviceCatId);
                            showRechargeForm(rechargeFormLayout, serviceType);
                            break;
                    }


                }
            });

            if (!servTypeId.isEmpty()) {
                if ((servTypeId.equals(serviceType.getService_type_id())) && (isRechargeFromNotShownOnLoading)) {
                    showRechargeForm(rechargeFormLayout, serviceType);
                    isRechargeFromNotShownOnLoading = false;
                }

            }

        }

    }

    public void dhHandleRechargeBtnClick() {

        try {
            Boolean continueRecharge = false;
            if (promoCodeLayoutVisible) {
                dealCode.setError(null);
                if (dhDealCode.getText().toString().length() > 0) {
                    if (dealFlag.equals("")) {
                        dhDealCode.setError("Please check deal first");
                    } else {
                        continueRecharge = true;
                    }
                } else {
                    continueRecharge = true;
                }
            } else {
                continueRecharge = true;
            }

            if (continueRecharge) {
                if (isPinRecharge.equals("false")) {

                    if (verifyRechargeNumber()) {
                        verifyPaymentAmount();
                    }
                } else {
                    verifyPaymentAmount();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("exception ", ex + "");
        }

    }

    public void handleRechargeBtnClick() {

        try {
            Boolean continueRecharge = false;
            if (promoCodeLayoutVisible) {
                dealCode.setError(null);
                if (dealCode.getText().toString().length() > 0) {
                    if (dealFlag.equals("")) {
                        dealCode.setError("Please check deal first");
                    } else {
                        continueRecharge = true;
                    }
                } else {
                    continueRecharge = true;
                }
            } else {
                continueRecharge = true;
            }

            if (continueRecharge) {
                if (isPinRecharge.equals("false")) {

                    if (verifyRechargeNumber()) {
                        verifyPaymentAmount();
                    }
                } else {
                    verifyPaymentAmount();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("exception ", ex + "");
        }

    }

    public void verifyUserDeal(String s) {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "checkUserDeals");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        if (!s.equals("dh")) {
            params.put("dealName", dealCode.getText().toString());
        } else {
            params.put("dealName", dhDealCode.getText().toString());
        }
        params.put("sCategory", serviceCatId);
        params.put("serviceType", serviceTypeId);
        params.put("purchasedValue", purchasedAmountValue);

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, context);
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
//                    dealFlag = response.getString("dealFlag");
//                    dealRemarks = response.getString("dealRemarks");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                    dealFlag = jsonObject.getString("dealFlag");
                    dealRemarks = jsonObject.getString("dealRemarks");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Child Recycler", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }


    public Boolean verifyRechargeNumber() {

        Boolean continueRecharge = true;
        String targetNumberEditTextVal = mobnoTxt.getText().toString();

        if (targetNumberEditTextVal.length() > 0) {

            if ((!categoryLength.equals("null")) && (categoryLength.length() > 0)) {
//            if (!categoryLength.equals("null")) {

                if (!categoryLength.equals(targetNumberEditTextVal
                        .length() + "")) {
                    continueRecharge = false;
                }
            }

//            if (!startsWith.equals("null")) {
            if ((!startsWith.equals("null")) && (startsWith.length() > 0)) {

                int startsWithLength = startsWith.length();
                String checkRechargeNumber = targetNumberEditTextVal
                        .substring(0, startsWithLength);
                if (!startsWith.equals(checkRechargeNumber)) {
                    continueRecharge = false;
                }
            }

            if (!continueRecharge) {
                mobnoTxt.setError(Html.fromHtml(rechargeTargetNumberErrorMsg));
            }

        } else {

            mobnoTxt.setError("Required");
            continueRecharge = false;
        }

        return continueRecharge;
    }

    public void verifyPaymentAmount() {

        Boolean continueRecharge = true;
        String purchasedValue = "";

        if (purchasedAmountType.equals("SELECT")) {

            purchasedAmountValue = purchasedValue = amtSpin
                    .getSelectedItem().toString();
            transactionConfirmModel();

        } else {

            purchasedAmountValue = purchasedValue = amtEdit.getText().toString();

            if (purchasedValue.length() > 0) {
                if ((!minVal.equals("null")) && (minVal.length() > 0)) {
//                if (!minVal.equals("null")) {

                    if ((Double.parseDouble(purchasedValue) < Double
                            .parseDouble(minVal))) {
                        continueRecharge = false;
                    }
                }

                if ((!maxVal.equals("null")) && (maxVal.length() > 0)) {
//                if (!maxVal.equals("null")) {

                    if ((Double.parseDouble(purchasedValue) > Double
                            .parseDouble(maxVal))) {
                        continueRecharge = false;
                    }
                }

                if (continueRecharge) {
                    transactionConfirmModel();
                } else {
                    amtEdit.setError(Html.fromHtml(rechargeTargetAmountErrorMsg));
                }

            } else {
                amtEdit.setError("Required");
            }


        }
    }


    public void pinRequest() {

        dialog.dismiss();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogview = inflater.inflate(R.layout.request_pin_layout, null);

        pinEnterEditText = (EditText) dialogview.findViewById(R.id.enterPin);
        pinReenterEditText = (EditText) dialogview.findViewById(R.id.reenterPin);

        enterPasswordEditText = (EditText) dialogview
                .findViewById(R.id.confirmpassword);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                context);
        AlertDialog pindialog = dialogBuilder
                // .setMessage(ss)
                // .setIcon(R.drawable.ic_launcher)
                .setView(dialogview).setTitle("Pin Code Request")
                .setPositiveButton("Save", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();
        pindialog.show();
        pindialog.setCanceledOnTouchOutside(true);

        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
        saveBtn.setOnClickListener(new CustomListener2(pindialog));

    }

    public void showUserAlertDialogWithView(String message) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogview = inflater.inflate(R.layout.redirect_website_message,
                null);
        TextView redirectMsg = (TextView) dialogview
                .findViewById(R.id.redirectMsg);

        redirectMsg.setText(message);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog pindialog = dialogBuilder.setTitle("msgTitle")
                .setView(dialogview).setPositiveButton("Request Pin", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();

        pindialog.show();
        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pindialog.dismiss();

                pinRequest();
            }
        });

    }

    public void transactionConfirmModel() {

        rechargeTargetNumberErrorMsg = "";
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogview = inflater.inflate(R.layout.confirmation_pin_code_form,
                null);
        confirmationPin = (EditText) dialogview
                .findViewById(R.id.confirmationPin);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                context);
        dialog = dialogBuilder
                .setView(dialogview).setTitle("Transaction Confirmation")
                .setPositiveButton("Yes", this)
                .setNegativeButton("Cancel", this).setCancelable(false)
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        pinRequest = (TextView) dialogview
                .findViewById(R.id.pinCodeRequest);
        TextView pinCodeHeading = (TextView) dialogview
                .findViewById(R.id.pinCodeHeading);

        pinCodePresent = ((DashBoardActivity) context).getPinCodeStatus();

        if (pinCodePresent.equals("YES")) {
            pinRequest.setVisibility(View.GONE);
            pinCodeHeading.setVisibility(View.GONE);

        } else {

            pinRequest.setVisibility(View.VISIBLE);
            pinCodeHeading.setVisibility(View.VISIBLE);
            pinRequest.setText(new TextViewStyling()
                    .textViewLink("Create Pin Code"));
            //   pinRequest.setPadding(R.dimen.margin_left,0,0,0);
            pinRequest.setTextColor(Color.parseColor("#5F86C4"));
            pinRequest.setOnClickListener(this);

        }


        Button yesBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        yesBtn.setOnClickListener(new CustomListener(dialog));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    public void setTargetNumberValidationMessage() {

        try {

            rechargeTargetNumberErrorMsg = iLabel
                    + " should have";

            rechargeTargetAmountErrorMsg = "Amount should have";


            if (purchasedAmountType.equals("TEXT")) {
                if (minVal.equals("null")) {

                    rechargeTargetAmountErrorMsg = rechargeTargetAmountErrorMsg
                            + "<br/>Min Val: ";
                } else {

                    rechargeTargetAmountErrorMsg = rechargeTargetAmountErrorMsg
                            + "<br/>Min Val: " + minVal;
                }

                if (maxVal.equals("null")) {

                    rechargeTargetAmountErrorMsg = rechargeTargetAmountErrorMsg
                            + "<br/>Max Val: ";
                } else {

                    rechargeTargetAmountErrorMsg = rechargeTargetAmountErrorMsg
                            + "<br/>Max Val: " + maxVal;
                }
            }


            if (startsWith.equals("null")) {

                rechargeTargetNumberErrorMsg = rechargeTargetNumberErrorMsg
                        + "<br/>Starts With: ";
            } else {
                rechargeTargetNumberErrorMsg = rechargeTargetNumberErrorMsg
                        + "<br/>Starts With: " + startsWith;
            }

            if (categoryLength.equals("null")) {
                rechargeTargetNumberErrorMsg = rechargeTargetNumberErrorMsg
                        + "<br/>" + iLabel + " Length: ";
            } else {
                rechargeTargetNumberErrorMsg = rechargeTargetNumberErrorMsg
                        + "<br/>" + iLabel + " Length: "
                        + categoryLength;
            }


//            rechargeTargetNumberError.setText(Html
//                    .fromHtml(rechargeTargetNumberErrorMsg));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", "" + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.pinCodeRequest:
                pinRequest();
                break;

        }

    }

    @Override
    public void onContactClicked(ContactsModel contactsModel) {
        mobnoTxt.setText(contactsModel.getContactPhone());

    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            // Do whatever you want here
            // If you want to close the dialog, uncomment the line below
            String minLength = "4";
            if (minLength.equals(confirmationPin.getText().toString().length()
                    + "")) {
                dialog.dismiss();
                // for hiding keyboard on button click
                // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                performUserRecharge();
            } else {
                confirmationPin
                        .setError("4 Digit Transaction Pin Code Required");
            }

        }
    }

    class CustomListener2 implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener2(Dialog dialog) {
            this.dialog = dialog;
            pinRequestDialog = dialog;
        }

        @Override
        public void onClick(View v) {

            // Do whatever you want here
            // If you want to close the dialog, uncomment the line below
            String minLength = "4";

            if (!minLength.equals(pinEnterEditText.getText().toString()
                    .length()
                    + ""))
                pinEnterEditText
                        .setError("4 Digit Transaction Pin Code Required");

            if (!minLength.equals(pinReenterEditText.getText().toString()
                    .length()
                    + ""))
                pinReenterEditText
                        .setError("4 Digit Transaction Pin Code Required");

            if (enterPasswordEditText.getText().toString().isEmpty())
                enterPasswordEditText.setError("password required");

            if (pinEnterEditText.getText().toString().length() > 0
                    && pinReenterEditText.getText().toString().length() > 0
                    && enterPasswordEditText.getText().toString().length() > 0) {

                if (minLength.equals(pinEnterEditText.getText().toString()
                        .length()
                        + "")
                        && minLength.equals(pinReenterEditText.getText()
                        .toString().length()
                        + "")) {

                    if (pinEnterEditText.getText().toString()
                            .equals(pinReenterEditText.getText().toString())) {

                        try {

//                            RequestParams params = new RequestParams();
                            Map<String, String> params = new HashMap<>();
                            params.put("parentTask", "rechargeApp");
                            params.put("childTask", "saveUserPinCode");
                            params.put("userCode", myUserSessionManager.getSecurityCode());
                            params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
                            params.put("pin", pinEnterEditText.getText()
                                    .toString());
                            // Make Http call
//                            PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, context);
//                            handler.makeRequest("authenticateAppUser", "Please Wait !!!", params, new PboServerRequestListener() {
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                                    try {
//                                        handlePerformUserRechargeResponse(response);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                                }
//                            });
                            retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
                                @Override
                                public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                                    try {
                                        handlePerformUserRechargeResponse(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onRetrofitFailure(String errorMessage, int apiCode) {
                                    Log.d("Child Recycler", "error");
                                }
                            });
                            retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else {
                        pinEnterEditText.setError(" Pin not matched");
                    }

                } else {
                    pinEnterEditText
                            .setError(" 4 Digit Transaction Pin Code Required");
                }

            } else {
                enterPasswordEditText.setError("enter the data");
            }
        }
    }

    AdapterView.OnItemSelectedListener onServiceAmountValueSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (purchasedAmountType.equals("SELECT")) {
                purchasedAmountValue = amtSpin
                        .getSelectedItem().toString();
                // viewRechargePayDetails();
            } else {
                purchasedAmountValue = amtEdit.getText()
                        .toString();
                if ((purchasedAmountValue.length() > 0)) {
                    amtEdit
                            .setError("Purchase Value Required");
                } else {

                    //   viewRechargePayDetails();
                }

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void performUserRecharge() {

//        RequestParams rechargeParams = new RequestParams();
        Map<String, String> rechargeParams = new HashMap<>();
        rechargeParams.put("parentTask", "rechargeApp");
        rechargeParams.put("childTask", "performRecharge");
        rechargeParams.put("purchasedValue", purchasedAmountValue);
        rechargeParams.put("responseType", "JSON");
        rechargeParams.put("confirmationPin", confirmationPin.getText().toString());
        rechargeParams.put("sCategory", serviceCatId);
        rechargeParams.put("serviceType", serviceTypeId);
//        rechargeParams.put("childTask", "saveRecharge");
        rechargeParams.put("userCode", myUserSessionManager.getSecurityCode());
        rechargeParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());

        if (isDH.equals("dh")) {
            rechargeParams.put("rechargeNumber", dhRechargeValue);
            rechargeParams.put("dealFlag", "");
            rechargeParams.put("dealRemarks", "");
            rechargeParams.put("dealName", "");
        } else {
            rechargeParams.put("rechargeNumber", mobnoTxt.getText().toString());
            rechargeParams.put("dealFlag", dealFlag);
            rechargeParams.put("dealRemarks", dealRemarks);
            rechargeParams.put("dealName", dealCode.getText().toString());
        }


        Log.i("rechargeParams", rechargeParams + "");
        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, context);
//        handler.makeRequest("authenticateAppUser", "Please Wait !!!", rechargeParams,
//                new PboServerRequestListener() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//                            handlePerformUserRechargeResponse(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                    }
//                });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handlePerformUserRechargeResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Child Recycler", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, rechargeParams, null, null);
    }

    public void handlePerformUserRechargeResponse(JSONObject response) throws JSONException {

        dialog.dismiss();
        if (pinRequestDialog != null) {
            pinRequestDialog.dismiss();
        }
        try {

            JSONObject json = response;
            Log.i("jsonttttttttttttt", json + "");

            if (json.getString("msgTitle").equals("Failed")) {
                if (json.has("reson")) {
                    if (json.getString("reason").equals("No Code")) {
                        showUserAlertDialogWithView(json.getString("msg"));
                    }
                } else {

                    showMyAlertProgressDialog.showUserAlertDialog(
                            json.getString("msg"), json.getString("msgTitle"));
                }

            } else {
                if (isDH.equals("dh")) {
                    dh_cas_id.setText("");
                } else {
                    amtEdit.setText("");
                    mobnoTxt.setText("");
                    dealCode.setText("");
                }

                dealFlag = "";
                dealRemarks = "";

                if (response.has("accountBalance")) {
                    DashBoardActivity dashBoardActivity = (DashBoardActivity) context;
                    dashBoardActivity.updateUserBalance(response.getString("currency"), response.getString("accountBalance"), Double.parseDouble(response.getString("holdMoneyAmount")));
                }
                showMyAlertProgressDialog.showUserAlertDialog(
                        json.getString("msg"), json.getString("msgTitle"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQS_PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor = context.managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number =       cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                //contactName.setText(name);
                mobnoTxt.setText(number);
                //contactEmail.setText(email);
            }
        }
    }*/

    public List<ContactsModel> getContacts() {
        List<ContactsModel> contactsModels = new ArrayList<>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.v("contact" + name, "==>" + phoneNumber);
            contactsModels.add(new ContactsModel(name, phoneNumber));
        }
        phones.close();
        return contactsModels;
    }

    public void getBothContacts(List<ContactsModel> ncell, List<ContactsModel> ntc) {
        List<ContactsModel> allContact = getContacts();
        ncell = new ArrayList<>();
        ntc = new ArrayList<>();
        for (ContactsModel contactsModel : allContact) {
            if (contactsModel.getContactPhone().startsWith("981") | contactsModel.getContactPhone().startsWith("980")) {
                ncell.add(contactsModel);
                Log.v("ncell", contactsModel.getContactPhone());
            } else if (contactsModel.getContactPhone().startsWith("984") | contactsModel.getContactPhone().startsWith("985")) {
                ntc.add(contactsModel);
                Log.v("ntc", contactsModel.getContactPhone());
            }
        }
    }
}
