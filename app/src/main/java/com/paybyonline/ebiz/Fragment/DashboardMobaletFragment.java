package com.paybyonline.ebiz.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.paybyonline.ebiz.Adapter.MobaletHorizontalAdapter;
import com.paybyonline.ebiz.Adapter.Model.DashboarGridModel;
import com.paybyonline.ebiz.Adapter.Model.ServiceCatagoryDetails;
import com.paybyonline.ebiz.Adapter.Model.ServiceType;
import com.paybyonline.ebiz.Adapter.Model.TextViewStyling;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * Created by hp on 7/10/2017.
 */

public class DashboardMobaletFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {
    LinearLayout coordinatorLayout;
    RecyclerView rechargeDetailsListView;
    MyUserSessionManager myUserSessionManager;
    EditText dh_cas_id,dhDealCode;
    List<ServiceType> serviceTypeListSelected;
    private List<ServiceType> serviceTypeList;
    private List<ServiceCatagoryDetails> serviceCategoryList;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    List<DashboarGridModel> dashboarGridModelList;
    ImageView loadWallet, go_to;
    String dhRechargeValue = "";
    TextView userBalance;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Spinner serviceTypeListSpinner;
    //  ViewPager viewPager;
    String getServiceType = "";
    LinearLayout rechargeLayout;
    String servCatId = "";
    //    PboServerRequestHandler handler;
    ImageView ntc, ncell, smartCell, broadlink, dishhome;


    TextView rechargeServiceName, amountTxt;
    TextView prepmob;
    TextView pinRequest;
    Spinner amtSpin;
    List<ServiceType> serviceTypeListSelectedPager;
    EditText amtEdit;
    LayoutInflater inflater;
    AlertDialog dialog;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private EditText confirmationPin;
    private EditText pinEnterEditText;
    private EditText pinReenterEditText;
    private EditText enterPasswordEditText;
    private AutoCompleteTextView mobnoTxt;
    private EditText dealCode;
    private Button btnPaynow;
    String isPinRecharge;
    String startsWith = "";
    String categoryLength = "";
    String maxVal = "";
    private String purchasedAmountType = "";
    String pinCodePresent = "NO";
    UserDeviceDetails userDeviceDetails;
    private HashMap<String, String> userDetails;
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
    ImageView contactIcon;
    private String contactID;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    Dialog pinRequestDialog;
    String categoryType;
    String dealFlag = "";
    String dealRemarks = "";
    Button checkDealBtn;
    ServiceType serviceType;
    RelativeLayout promoCodeLayout;
    Boolean promoCodeLayoutVisible = false;
    private RetrofitHelper retrofitHelper;
//    private PboServerRequestHandler pboServerRequestHandler;


    public DashboardMobaletFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_mobalet, container, false);

//        ((DashBoardActivity) getActivity()).toolbar.setBackgroundColor(Color.parseColor("202632"));
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202632")));
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);
        //((DashBoardActivity) getActivity()).getSupportActionBar().setIcon(R.mipmap.ic_new_logo);
        ((DashBoardActivity) getActivity()).setFragmentName("dashboard");

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        Spannable text = new SpannableString(getString(R.string.title_activity_login));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setTitle(text);
        myUserSessionManager = new MyUserSessionManager(getContext());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getContext());
        //  viewPager= (ViewPager)view.findViewById(R.id.viewpager);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        serviceTypeListSpinner = (Spinner) view.findViewById(R.id.spinner);
        rechargeLayout = (LinearLayout) view.findViewById(R.id.rechargeLayout);
        ntc = (ImageView) view.findViewById(R.id.ntc);
        ncell = (ImageView) view.findViewById(R.id.ncell);
        smartCell = (ImageView) view.findViewById(R.id.smartCell);
        broadlink = (ImageView) view.findViewById(R.id.broadlink);
        dishhome = (ImageView) view.findViewById(R.id.dishhome);
        //rechargeDetailsListView = (RecyclerView) view.findViewById(R.id.rechargeDetailsListView);
        coordinatorLayout = (LinearLayout) view.findViewById(R.id.coordinatorLayout);
        loadWallet = (ImageView) view.findViewById(R.id.loadWallet);
        go_to = (ImageView) view.findViewById(R.id.go_to);
        addData();
        setupRecyclerView();

        userBalance = (TextView) view.findViewById(R.id.userBalance);
        loadUserMenuDetails();
        getAvailableServices("1", "1");
        ntc.setImageResource(R.drawable.ic_ntc_circle);
        ncell.setImageResource(R.drawable.ic_ncell);
        dishhome.setImageResource(R.drawable.ic_dishhome);
        smartCell.setImageResource(R.drawable.ic_smartcell);
        broadlink.setImageResource(R.drawable.ic_braodlink);
        ntc.setEnabled(false);
        ncell.setEnabled(true);
        dishhome.setEnabled(true);
        smartCell.setEnabled(true);
        broadlink.setEnabled(true);
       /* String userBal= getArguments().getString("userBalance");
        if (userBal!=null) {

        }*/

        getUserBalance();
        ntc.setOnClickListener(this);
        ncell.setOnClickListener(this);
        smartCell.setOnClickListener(this);
        broadlink.setOnClickListener(this);
        dishhome.setOnClickListener(this);
        loadWallet.setOnClickListener(this);
        go_to.setOnClickListener(this);
        serviceTypeListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getItem = (TextView) parent.getChildAt(0);
                if (getItem != null) {
                    getItem.setTextColor(Color.BLACK);
                }
                getServiceType = getItem.getText().toString();
                showForm(serviceTypeList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public void addData() {
        dashboarGridModelList = new ArrayList<DashboarGridModel>();
        dashboarGridModelList.add(new DashboarGridModel((long) 1, R.drawable.fund_transfer_background, "fund"));
//        dashboarGridModelList.add(new DashboarGridModel((long) 2, R.drawable.filghts_background, "flights"));
//        dashboarGridModelList.add(new DashboarGridModel((long) 3, R.drawable.bus_background, "bus"));
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202632")));
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);
        Spannable text = new SpannableString(getString(R.string.title_activity_login));
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setTitle(text);

        getUserBalance();
        super.onResume();
    }


    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);


        //setup the adapter with empty list
        MobaletHorizontalAdapter mobaletHorizontalAdapter = new MobaletHorizontalAdapter(getContext(), dashboarGridModelList);

        recyclerView.setAdapter(mobaletHorizontalAdapter);

    }

    public void loadUserMenuDetails() {
//        RequestParams params = new RequestParams();
        RetrofitHelper retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
//        params.put("childTask", "getAccountSummary");
        params.put("childTask", "getUserMenuDetails");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("role", "USER");
//        pboServerRequestHandler = PboServerRequestHandler.getInstance(coordinatorLayout,getContext());
//        pboServerRequestHandler.makeRequestService(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleLoadUserMenuDetailsResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleLoadUserMenuDetailsResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Dashboard Mobalet", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleLoadUserMenuDetailsResponse(JSONObject response) throws JSONException {
        JSONObject jsonObject = response;
        Log.i("res ", "" + jsonObject);

        if (jsonObject.getString("msg").equals("Success")) {

            if (response.has("userDetails")) {
                JSONObject userDetails = response.getJSONObject("userDetails");

                String userBalanceData = userDetails.getString("userBalance");
                String currencyCode = userDetails.getString("currencyCode");
                userBalance.setText(currencyCode + " " + userBalanceData);
//                DashBoardActivity.userBalance.setText(currencyCode+ " "+ userBalanceData);
                ((DashBoardActivity) getActivity()).setUserBalance(currencyCode, userBalanceData);
//                DashBoardActivity.userBalance.setText(currencyCode+ " "+ userBalanceData);
            }


        } else {
            userDeviceDetails.showToast("Error Occurred. Please try again !!!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ntc:
//                 createList("NTC", "1");
                getAvailableServices("1", "1");
                ntc.setImageResource(R.drawable.ic_ntc_circle);
                ncell.setImageResource(R.drawable.ic_ncell);
                dishhome.setImageResource(R.drawable.ic_dishhome);
                smartCell.setImageResource(R.drawable.ic_smartcell);
                broadlink.setImageResource(R.drawable.ic_braodlink);
                ntc.setEnabled(false);
                ncell.setEnabled(true);
                dishhome.setEnabled(true);
                smartCell.setEnabled(true);
                broadlink.setEnabled(true);
                break;
            case R.id.ncell:
                //createList("NCell", "1");
                getAvailableServices("2", "1");
                ntc.setImageResource(R.drawable.ic_ntc);
                ncell.setImageResource(R.drawable.ic_ncell_circle);
                dishhome.setImageResource(R.drawable.ic_dishhome);
                smartCell.setImageResource(R.drawable.ic_smartcell);
                broadlink.setImageResource(R.drawable.ic_braodlink);
                ntc.setEnabled(true);
                ncell.setEnabled(false);
                dishhome.setEnabled(true);
                smartCell.setEnabled(true);
                broadlink.setEnabled(true);
                break;
            case R.id.smartCell:
                //createList("SMART CELL", "17");
                getAvailableServices("15", "17");
                ntc.setImageResource(R.drawable.ic_ntc);
                ncell.setImageResource(R.drawable.ic_ncell);
                dishhome.setImageResource(R.drawable.ic_dishhome);
                smartCell.setImageResource(R.drawable.ic_smartcell_circle);
                broadlink.setImageResource(R.drawable.ic_braodlink);
                ntc.setEnabled(true);
                ncell.setEnabled(true);
                dishhome.setEnabled(true);
                smartCell.setEnabled(false);
                broadlink.setEnabled(true);
                break;
            case R.id.broadlink:
                //createList("BroadLink", "9");
                getAvailableServices("3", "9");
                ntc.setImageResource(R.drawable.ic_ntc);
                ncell.setImageResource(R.drawable.ic_ncell);
                dishhome.setImageResource(R.drawable.ic_dishhome);
                smartCell.setImageResource(R.drawable.ic_smartcell);
                broadlink.setImageResource(R.drawable.ic_broadlink_circle);
                ntc.setEnabled(true);
                ncell.setEnabled(true);
                dishhome.setEnabled(true);
                smartCell.setEnabled(true);
                broadlink.setEnabled(false);
                break;
            case R.id.dishhome:
                //createList("DishHome", "5");
                getAvailableServices("4", "5");
                ntc.setImageResource(R.drawable.ic_ntc);
                ncell.setImageResource(R.drawable.ic_ncell);
                dishhome.setImageResource(R.drawable.ic_dishhome_circle);
                smartCell.setImageResource(R.drawable.ic_smartcell);
                broadlink.setImageResource(R.drawable.ic_braodlink);
                ntc.setEnabled(true);
                ncell.setEnabled(true);
                dishhome.setEnabled(false);
                smartCell.setEnabled(true);
                broadlink.setEnabled(true);
                break;
            case R.id.loadWallet:
                Bundle bundle = new Bundle();
                bundle.putBoolean("openAddForm", true);
                fragment = new WalletFragment();
                fragment.setArguments(bundle);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.go_to:
                fragment = new RechargeBuyFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.pinCodeRequest:
                pinRequest();
                break;

        }

    }

    /* public void createList(String categoryName, String serviceTypeId){
         serviceTypeListSelected = new ArrayList<>();
         //int index= serviceCategoryList.indexOf(categoryName);
         //Log.d("index",  index +"");
         if(serviceCategoryList!=null) {
             for(int i =0;i<serviceCategoryList.size();i++){
                 ServiceCatagoryDetails serviceCatagoryDetails = serviceCategoryList.get(i);
                 if (serviceCatagoryDetails.getServiceCategoryName().equals(categoryName)) {
                     serviceTypeListSelected = serviceCatagoryDetails.getServiceTypeArrayList();
                    String serviceCatId= serviceCatagoryDetails.getServiceCategoryId();

                     servCatId = serviceCatId;

 //                    MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(categoryName, servCatId);
 //
 //                    viewPager.setAdapter(myViewPagerAdapter);

                     RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                     rechargeDetailsListView.setHasFixedSize(true);
                     rechargeDetailsListView.setLayoutManager(mLayoutManager);
                     ParentRecycleViewAdapter parentRecycleViewAdapter = new ParentRecycleViewAdapter(getActivity(),
                             serviceCategoryList,R.layout.child_recycleview_layout,coordinatorLayout,serviceTypeId);

                     rechargeDetailsListView.setAdapter(parentRecycleViewAdapter);

                 }
             }

         }
         else {
             Toast.makeText(getContext(), "Service not available", Toast.LENGTH_SHORT).show();
         }
     }*/
    public void getAvailableServices(String servCatId, final String serviceTypeId) {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getRechargeServiceDetails");
        params.put("servCatId", servCatId);
        Log.d("serviceID===", servCatId);
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());

//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//        handler.makeRequestService(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleGetAvailableServicesResponse(response, serviceTypeId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleGetAvailableServicesResponse(jsonObject, serviceTypeId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Dashboard Mobalet", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }


    public void handleGetAvailableServicesResponse(JSONObject response, String serviceTypeId) throws JSONException {

        try {
//            handler.showProgressDialog("Please Wait !!!");
//            retrofitHelper.showProgressDialog("Please Wait !!!");
            Log.i("responseMobalet", "" + response);


            JSONArray jsonData = response.getJSONArray("data");
            Log.i("data", "" + jsonData);

            serviceCategoryList = new ArrayList<ServiceCatagoryDetails>();

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject object = jsonData.getJSONObject(i);

                JSONArray jsonData2 = object.getJSONArray("serviceTypeList");

                serviceTypeList = new ArrayList<ServiceType>();
                for (int j = 0; j < jsonData2.length(); j++) {
                    JSONObject object2 = jsonData2.getJSONObject(j);
                    Log.i("object2 ", "object2 " + object2);

                    serviceTypeList.add(new ServiceType(object2.getString("startsWith"), object2.getString("minVal"),
                            object2.getString("maxVal"), object2.getString("iLabel"), object2.getString("isPinRechargeService"),
                            object2.getString("categoryLength"), object2.getString("serviceTypeName"),
                            object2.getString("serviceTypeId"), object2.getString("pCountry"),
                            PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" +
                                    Uri.encode((object2.has("logoName") ? object2.getString("logoName") : "")),
                            object2.getString("scstAmountType"),
                            object2.getString("currency"), object2.getString("scstAmountValue"), object2.getString("isProductEnable"), object2.getString("enablePromoCode"),object2.getString("scstId")));
                }

                serviceCategoryList.add(new ServiceCatagoryDetails(object.getString("serviceCategoryId")
                        , object.getString("serviceCategoryName"), PayByOnlineConfig.BASE_URL + "ServiceLogo/categoryTypeLogo/" +
//                        ,object.getString("serviceCategoryName"), PayByOnlineConfig.BASE_URL+"CategoryTypeLogo/"+
                        Uri.encode(object.getString("serviceCategoryLogo"))
                        , serviceTypeList));

            }
           /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rechargeDetailsListView.setHasFixedSize(true);
            rechargeDetailsListView.setLayoutManager(mLayoutManager);
            ParentRecycleViewMobaletAdapter parentRecycleViewAdapter = new ParentRecycleViewMobaletAdapter(getActivity(),
                    serviceCategoryList, R.layout.child_recycleview_layout, coordinatorLayout, serviceTypeId);

            rechargeDetailsListView.setAdapter(parentRecycleViewAdapter);*/

            createForm();
//            handler.hideProgressDialog();
//            retrofitHelper.hideProgressDialog();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Exception", "" + e);
//            retrofitHelper.hideProgressDialog();
        }
    }

    private void createForm() {
        ArrayList<String> serviceTypeName = new ArrayList<String>();
        List<ServiceType> serviceTypeListSelected = new ArrayList<ServiceType>();
        if (serviceCategoryList.size() > 0) {
            for (int i = 0; i < serviceCategoryList.size(); i++) {
                ServiceCatagoryDetails serviceCatagoryDetails = serviceCategoryList.get(i);
                serviceCatId = serviceCatagoryDetails.getServiceCategoryId();
                serviceTypeListSelected = serviceCatagoryDetails.getServiceTypeArrayList();
                for (int j = 0; i < serviceTypeListSelected.size(); i++) {
                    ServiceType serviceType = serviceTypeListSelected.get(i);
                    String sType = serviceType.getService_type_name();
                    serviceTypeName.add(sType);


                }
            }

            ArrayAdapter adapter1 = new ArrayAdapter(getContext(),
                    android.R.layout.simple_spinner_item, serviceTypeName);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            serviceTypeListSpinner.setAdapter(adapter1);
            getServiceType = serviceTypeListSpinner.getSelectedItem().toString();
            showForm(serviceTypeListSelected);
        }
    }

    public void showForm(List<ServiceType> serviceTypeListSelected) {
        if (!getServiceType.equals("")) {
            for (int i = 0; i < serviceTypeListSelected.size(); i++) {
                ServiceType serviceType = serviceTypeListSelected.get(i);

                if (serviceType.getService_type_name().equals(getServiceType)) {
                    if (serviceType.getService_type_name().equals("DishHome Topup")){
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
                        if (rechargeLayout.getChildCount() > 0) {
                            rechargeLayout.removeAllViews();
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
                        ArrayAdapter adapter1 = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, output);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        amtSpin.setAdapter(adapter1);

                        amtSpin.setOnItemSelectedListener(onServiceAmountValueSelectedListener);


                        dhBtnPaynow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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
                            }
                        });
                        rechargeLayout.addView(customView);
                        rechargeLayout.setVisibility(View.VISIBLE);

                    }else {
                        serviceTypeId = serviceType.getService_type_id();
                        startsWith = serviceType.getStartsWith();
                        categoryLength = serviceType.getCategoryLength();
                        minVal = serviceType.getMinVal();
                        purchasedAmountType = serviceType.getScstAmountType();
                        iLabel = serviceType.getiLabel();
                        maxVal = serviceType.getMaxVal();
                        isPinRecharge = serviceType.getIsPinRechargeService();
                        showRechargeForm(rechargeLayout, serviceType);
                    }
                    Log.v("service","==>"+serviceType.getService_type_name());

                }
            }

        }
    }

    public void showRechargeForm(LinearLayout rechargeFormLayout, ServiceType serviceType) {

        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "serviceType.getIsProductEnable() " + serviceType.getIsProductEnable());

        if (rechargeFormLayout.getChildCount() > 0) {
            rechargeFormLayout.removeAllViews();
        }

        if (serviceType.getIsProductEnable().equals("Y")) {
            inflater = LayoutInflater.from(getContext());
            customView = inflater.inflate(R.layout.recharge_form_mobalet, null);
            amtEdit = (EditText) customView.findViewById(R.id.amtEdit);
            amountTxt = (TextView) customView.findViewById(R.id.amountTxt);
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
            if (((DashBoardActivity)getActivity()).contactList.size()>0){
                AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(getContext(),((DashBoardActivity)getActivity()).contactList);
                mobnoTxt.setAdapter(autoCompleteAdapter);
            }
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
            }

            if (serviceType.getScstAmountType().equals("SELECT")) {

                amtSpin.setVisibility(View.VISIBLE);
                amountTxt.setVisibility(View.VISIBLE);
                amtEdit.setVisibility(View.GONE);
                contactIcon.setVisibility(View.GONE);
                String[] output = serviceType.getScst_amount_value().split(",");
                ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_spinner_item, output);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                amtSpin.setAdapter(adapter1);

                amtSpin.setOnItemSelectedListener(onServiceAmountValueSelectedListener);

            } else {
                amountTxt.setVisibility(View.GONE);
                amtEdit.setVisibility(View.VISIBLE);
                amtSpin.setVisibility(View.GONE);
            }

            setTargetNumberValidationMessage();

            btnPaynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleRechargeBtnClick();
                }
            });

            checkDealBtn = (Button) customView.findViewById(R.id.checkDealBtn);
            checkDealBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealCode.setError(null);
                    if (dealCode.getText().toString().length() > 0) {
                        verifyUserDeal("o");
                    } else {
                        dealCode.setError("Please enter code");
                    }
                }
            });

            contactIcon.setVisibility(View.GONE);
            contactIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent contactIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

                    getActivity().startActivityForResult(contactIntent, REQUEST_CODE_PICK_CONTACTS);

                }

            });
            rechargeFormLayout.addView(customView);

        } else {
            TextView textView = new TextView(getActivity());
            textView.setPadding(10, 10, 10, 10);
            textView.setText("Service not available now");
            rechargeFormLayout.addView(textView);
        }

        rechargeFormLayout.setVisibility(View.VISIBLE);

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
                    Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
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
        //Toast.makeText(getContext(), targetNumberEditTextVal, Toast.LENGTH_SHORT).show();

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
            Log.e("rechrged", "rechrged");

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
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.request_pin_layout, null);

        pinEnterEditText = (EditText) dialogview.findViewById(R.id.enterPin);
        pinReenterEditText = (EditText) dialogview.findViewById(R.id.reenterPin);

        enterPasswordEditText = (EditText) dialogview
                .findViewById(R.id.confirmpassword);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
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

    public void showUserAlertDialogWithView(String message, String title) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.redirect_website_message,
                null);
        TextView redirectMsg = (TextView) dialogview
                .findViewById(R.id.redirectMsg);

        redirectMsg.setText(message);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog pindialog = dialogBuilder.setTitle(title)
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
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.confirmation_pin_code_form,
                null);
        confirmationPin = (EditText) dialogview
                .findViewById(R.id.confirmationPin);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
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

        pinCodePresent = ((DashBoardActivity) getActivity()).getPinCodeStatus();

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
            e.printStackTrace();
            Log.i("Exception", "" + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

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
//                            PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
//                            handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
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
                                    Log.d("Dashboard Mobalet", "error");
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
                TextView getItem = (TextView) parent.getChildAt(0);
                if (getItem != null) {
                    getItem.setTextColor(Color.BLACK);
                }
                purchasedAmountValue = getItem.getText().toString();
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

//        RequestParams rechargeParams=new RequestParams();
        Map<String, String> rechargeParams = new HashMap<>();
        rechargeParams.put("parentTask", "rechargeApp");
        rechargeParams.put("childTask", "performRecharge");
//        rechargeParams.put("childTask", "saveRecharge");
        rechargeParams.put("userCode", myUserSessionManager.getSecurityCode());
        rechargeParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        rechargeParams.put("rechargeNumber", mobnoTxt.getText()
                .toString());
        rechargeParams.put("confirmationPin", confirmationPin.getText()
                .toString());
        rechargeParams.put("responseType", "JSON");
        rechargeParams.put("sCategory", serviceCatId);
        rechargeParams.put("serviceType", serviceTypeId);
        rechargeParams.put("purchasedValue", purchasedAmountValue);
        rechargeParams.put("dealFlag", dealFlag);
        rechargeParams.put("dealRemarks", dealRemarks);
        rechargeParams.put("dealName", dealCode.getText().toString());

        Log.i("rechargeParams", rechargeParams + "");
        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", rechargeParams,
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
                Log.d("Dashboard Mobalet", "error");
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

            if (json.getString("msgTitle").equals("FAILED")) {

                if (json.getString("reason").equals("No Code")) {

                    showUserAlertDialogWithView(json.getString("msg"),
                            "msgTitle");
                } else {
                    showMyAlertProgressDialog.showUserAlertDialog(
                            json.getString("msg"), json.getString("msgTitle"));
                }

            } else {
                amtEdit.setText("");
                mobnoTxt.setText("");
                dealCode.setText("");
                dealFlag = "";
                dealRemarks = "";

                if (response.has("accountBalance")) {
                    DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
                    dashBoardActivity.updateUserBalance(response.getString("currency"), response.getString("accountBalance"), Double.parseDouble(response.getString("holdMoneyAmount")));
                    userBalance.setText(response.getString("currency") + " " + response.getString("accountBalance"));
                }
                showMyAlertProgressDialog.showUserAlertDialog(
                        json.getString("msg"), json.getString("msgTitle"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUserBalance(String balance) {
        userBalance.setText(balance);
    }

    public void getUserBalance() {
        userBalance.setText(myUserSessionManager.getKeyUserAmt());
    }
}

