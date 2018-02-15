package com.paybyonline.ebiz.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.Model.CheckListModel;
import com.paybyonline.ebiz.Adapter.Model.MyOwnTags;
import com.paybyonline.ebiz.Adapter.Model.ServiceCatagoryDetails;
import com.paybyonline.ebiz.Adapter.Model.ServiceCategoryServiceTypeDetails;
import com.paybyonline.ebiz.Adapter.MyOwnServiceAdapter;
import com.paybyonline.ebiz.Adapter.ServiceTypeListAdapter;
import com.paybyonline.ebiz.R;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOwnServicesFragment extends Fragment implements DialogInterface.OnClickListener {

    RecyclerView mRecyclerView;
    private MyUserSessionManager myUserSessionManager;
    private UserDeviceDetails userDeviceDetails;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private HashMap<String, String> userDetails;
    private List<MyOwnTags> myOwnTagslist;
    private MyOwnServiceAdapter listCall;
    LinearLayout showHideServiceType;

    EditText tagNameEditText;
    AlertDialog pindialog;
    ArrayList<CheckListModel> listOfServiceCategory;
    CoordinatorLayout coordinatorLayout;
    //    private static String url;
    //    PboServerRequestHandler handler;
    ListView serviceTypeListView;
    private String serviceTypeId = null;
    AppCompatActivity mActivity;
    LinkedHashMap<Integer, ServiceCategoryServiceTypeDetails> serviceCategoryTestListMap;
    ArrayAdapter<CheckListModel> adapter;
    List<ServiceCategoryServiceTypeDetails> serviceCategoryServiceTypeDetails;
    ArrayList<ServiceCategoryServiceTypeDetails> serviceCategoryTestList = new ArrayList<>();
    LinkedHashMap<String, ServiceCategoryServiceTypeDetails> listServiceCategoryTest;
    Context context;
    //    private boolean _hasLoadedOnce= false; // your boolean field
    RelativeLayout data_not_available;

    FloatingActionButton addMyServicesFab;
    private RetrofitHelper retrofitHelper;

    // public MyOwnServices Ins
    public static MyOwnServicesFragment newInstance() {
        MyOwnServicesFragment myFragment = new MyOwnServicesFragment();
        return myFragment;
    }

    public MyOwnServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_own_services, container, false);
        // Inflate the layout for this fragment
        //DashBoardActivity.addServices.se
        context = getContext();
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        //((DashBoardActivity) getActivity()).setTitle("Buy/Recharge");
        Log.i("context", "" + context);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        addMyServicesFab = (FloatingActionButton) view.findViewById(R.id.addMyServicesFab);
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
        setHasOptionsMenu(true);
        data_not_available = (RelativeLayout) view.findViewById(R.id.data_not_available);

        LinkedHashMap<String, ServiceCatagoryDetails> listServiceCategoryTest = new LinkedHashMap<String, ServiceCatagoryDetails>();

        // addList.setOnClickListener(this);

//        url = PayByOnlineConfig.SERVER_URL;

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        obtainMyServices();

        addMyServicesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServicesOnListModel(context);
            }
        });

        return view;
    }


    /* @Override
     public void setUserVisibleHint(boolean isFragmentVisible_) {
         super.setUserVisibleHint(true);

         if (this.isVisible()) {
             // we check that the fragment is becoming visible
             if (isFragmentVisible_ && !_hasLoadedOnce) {
                 obtainMyServices();
                 _hasLoadedOnce = true;
             }
         }
     }
 */
    public void obtainMyServices() {

        Log.i("MyServiceCatResponse", "obtainMyServices");

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "myServiceCategory");
        params.put("isNew", "YES");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("role", "USER");

//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params,
//                new PboServerRequestListener() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//
//                            handleMyServiceCategoryResponse(response);
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//
//                        }
//                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                    }
//                });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("My Own", "" + jsonObject);
                    handleMyServiceCategoryResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("My Own", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleMyServiceCategoryResponse(JSONObject response) throws JSONException {

        System.out.println("MyServiceCatResponse" + response.toString());

        try {

            System.out.println("MyServiceCatResponse" + response.toString());
            Log.i("MyServiceCatResponse", "" + response);

            JSONArray jsonArray = response.getJSONArray("data");
            myOwnTagslist = new ArrayList<MyOwnTags>();

            if (jsonArray.length() != 0) {
                // Loop through each array element, get JSON object
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    myOwnTagslist.add(new MyOwnTags(obj.get("tagName")
                            .toString(), obj.get("tagCount").toString()));

                    Log.i("myOwnTagslist", "" + obj.get("tagName")
                            .toString());

                }
                listCall = new MyOwnServiceAdapter(getActivity(), myOwnTagslist);

                if (listCall != null) {

                    mRecyclerView.setAdapter(listCall);

                } else {

                    data_not_available.setVisibility(View.VISIBLE);
                }
            } else {
                data_not_available.setVisibility(View.VISIBLE);
            }


            /////////////////////////////////////Listing available services///////////////////////////////////////////////////
            try {

                JSONArray allAvailableServices = response.getJSONArray("allAvailableServices");
                serviceCategoryTestListMap = new LinkedHashMap<>();
                //JSONObject categoryDetails = new JSONObject();
                if (allAvailableServices.length() != 0) {

                    for (int i = 0; i < allAvailableServices.length(); i++) {

                        JSONObject obj = allAvailableServices.getJSONObject(i);

                        serviceCategoryTestListMap.put(i,
                                new ServiceCategoryServiceTypeDetails(obj.getString("serviceCategoryName")
                                        , obj.getString("serviceTypeName"), obj.getString("serviceCategoryId")
                                        , obj.getString("serviceTypeId"), obj.getString("scstId")));

                    }
                }
            } catch (JSONException e1) {

                e1.printStackTrace();
            }

            listOfServiceCategory = new ArrayList<>();

            for (ServiceCategoryServiceTypeDetails foo : serviceCategoryTestListMap.values()) {
                listOfServiceCategory.add(new CheckListModel(foo.getServiceTitleName()));
            }

            ////////////////////////////////////////////////////////////////////////////////////////


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    public void addServicesOnListModel(Context context) {

        //	dialog.dismiss();
        Log.i("onclick1", "function called");
        Log.i("onclick1", "getActivity() called");
        Log.i("onclick1", "getActivity() called" + context);

        LayoutInflater inflater = LayoutInflater.from(context);
        Log.i("onclick1", "" + inflater);

        View dialogView = inflater.inflate(R.layout.my_own_service_add_list_request, null);

        tagNameEditText = (EditText) dialogView.findViewById(R.id.tagName);
        serviceTypeListView = (ListView) dialogView.findViewById(R.id.serviceCategoryServiceTypeList);
        showHideServiceType = (LinearLayout) dialogView.findViewById(R.id.showHideServiceType);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        Log.i("onclick2", "dialog call");
        pindialog = dialogBuilder
                .setView(dialogView)
                .setTitle("Add Own Services")
                .setPositiveButton("Save", this)
                .setNegativeButton("Cancel", this)
                .setCancelable(false)
                .create();
        pindialog.show();

        pindialog.setCanceledOnTouchOutside(true);
        //serviceCategoryListSpinner = (Spinner)dialogview.findViewById(R.id.serviceCategorySpinner);

        adapter = new ServiceTypeListAdapter(getActivity(), listOfServiceCategory);

        System.out.println("list :" + serviceCategoryServiceTypeDetails);
        Log.i("list", "" + serviceCategoryServiceTypeDetails);

        if (listOfServiceCategory != null) {
            serviceTypeListView.setAdapter(adapter);
        } else {
            userDeviceDetails.showToast("No data available");
        }


//        obtainCcategoryListDetails();


        Button saveBtn = pindialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //saveBtn.setOnClickListener(new CustomListener2(pindialog));
        saveBtn.setOnClickListener(v -> {
            //	userDeviceDetails.showToast("clicked");
            ServiceCategoryServiceTypeDetails scstDetails;
            String scStId = "";
            ArrayList<String> scList = new ArrayList<String>();

            for (int i = 0; i < listOfServiceCategory.size(); i++) {

                //CheckListModel.toggleChecked();
                CheckListModel selectedList = adapter.getItem(i);
                //CheckListModel.toggleChecked();
                if (selectedList.isSelected()) {

                    scstDetails = serviceCategoryTestListMap.get(i);
                    scList.add(scstDetails.getServiceCategoryName() + scstDetails.getServiceCategoryName());

                    if (scStId.length() > 0) {

                        scStId += "," + scstDetails.getServiceCategoryId() + "-" + scstDetails.getServiceTypeId();

                    } else {

                        scStId += scstDetails.getServiceCategoryId() + "-" + scstDetails.getServiceTypeId();

                    }
                }
            }

            if (tagNameEditText.getText().toString().length() > 0) {
                if (scList.size() > 0) {

                    submitDataList(scStId, tagNameEditText.getText().toString());

                } else {

                    userDeviceDetails.showToast("No Item Selected");
                }

            } else {

                tagNameEditText.setError("Tag name required");
            }
        });
    }

    public void obtainCcategoryListDetails() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getServiceDetailList");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        /*params.put("username",
                userDetails.get(MyUserSessionManager.KEY_USERNAME).toString());*/
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
//                params, new PboServerRequestListener() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//
//                            handleObtainCategoryListDetailsResponse(response);
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//
//                        }
//                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",
//                        // Toast.LENGTH_LONG).show();
//                    }
//                });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainCategoryListDetailsResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("My Own", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainCategoryListDetailsResponse(JSONObject response) throws JSONException {

        Log.i("nCategoryList", "" + response);

        System.out.println("handleObtainCategoryListDetailsResponse:" + response);

        try {

            JSONArray jsonArray = response.getJSONArray("data");
            serviceCategoryTestListMap = new LinkedHashMap<>();
            //JSONObject categoryDetails = new JSONObject();
            if (jsonArray.length() != 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    serviceCategoryTestListMap.put(i,
                            new ServiceCategoryServiceTypeDetails(obj.getString("serviceCategoryName")
                                    , obj.getString("serviceTypeName"), obj.getString("serviceCategoryId")
                                    , obj.getString("serviceTypeId"), obj.getString("scstId")));

                }
            }
        } catch (JSONException e1) {

            e1.printStackTrace();
        }

        listOfServiceCategory = new ArrayList<>();

        for (ServiceCategoryServiceTypeDetails foo : serviceCategoryTestListMap.values()) {

            listOfServiceCategory.add(new CheckListModel(foo.getServiceTitleName()));
            //userDeviceDetails.showToast(""+serviceCategoryTestListMap.values());
        }

        adapter = new ServiceTypeListAdapter(getActivity(), listOfServiceCategory);

        System.out.println("listCategory :" + serviceCategoryServiceTypeDetails);
        Log.i("list", "===>" + listOfServiceCategory);
//        Log.i("list", "" + serviceCategoryServiceTypeDetails);

        if (listOfServiceCategory != null) {
            //userDeviceDetails.showToast(listOfServiceCategory.size()+"list");
            serviceTypeListView.setAdapter(adapter);
        } else {

            userDeviceDetails.showToast("No data available");
        }

    }

    public void submitDataList(String scStId, String tagName) {


//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "saveMyOwnServiceTags");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("tagName", tagName);
        params.put("scstId", scStId);

        Log.d("MyOwnServiceSaveParams", params.toString());

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
//                params, new PboServerRequestListener() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                        try {
//
//                            submitDataListResponse(response);
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//
//                        }
//                        //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//                    }
//                });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    submitDataListResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Favorite Services", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void submitDataListResponse(JSONObject response) throws JSONException {

        pindialog.dismiss();

        try {
            JSONObject json = response;
            Log.i("json", json + "");

            userDeviceDetails.showToast(json.getString("msg"));

//            showMyAlertProgressDialog.showUserAlertDialog(
//                    json.getString("msg"), json.getString("msgTitle"));

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.addServices).setVisible(true);
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (AppCompatActivity) context;
        }
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        //((DashBoardActivity) getActivity()).setTitle("Buy/Recharge");
        super.onResume();
    }

    public void myOwnServicesFiltering(String query) {
        Log.i("msgsss dash ", myOwnTagslist + "");
        Log.i("msgsss dash ", query + "");

        List<MyOwnTags> filteredModelList = new ArrayList<>();
        filteredModelList = filter(myOwnTagslist, query);
        listCall.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);

    }

    private List<MyOwnTags> filter(List<MyOwnTags> models, String query) {

        query = query.toLowerCase();

        List<MyOwnTags> filteredModelList = new ArrayList<>();
        for (MyOwnTags model : models) {

            if (model != null) {

                String tagName = model.getTagName().toLowerCase();
                if (tagName.contains(query)) {

                    filteredModelList.add(model);
                }
            }
        }

        return filteredModelList;
    }

}



