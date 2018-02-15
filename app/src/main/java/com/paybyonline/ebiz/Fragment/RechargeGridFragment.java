package com.paybyonline.ebiz.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.Model.ParentRecycleViewSampleModel;
import com.paybyonline.ebiz.Adapter.Model.ServiceCatagoryDetails;
import com.paybyonline.ebiz.Adapter.Model.ServiceType;
import com.paybyonline.ebiz.Adapter.ParentRecycleViewAdapter;
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
 * Created by Anish on 9/26/2016.
 */
public class RechargeGridFragment extends Fragment {

    CoordinatorLayout coordinatorLayout;
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    RecyclerView rechargeDetailsListView;
    private HashMap<String, String> userDetails;
    ParentRecycleViewAdapter parentRecycleViewAdapter;
    // URL to get JSON Array
    private List<ServiceType> serviceTypeList;
    private List<ServiceCatagoryDetails> serviceCategoryList;
    private ArrayList<ParentRecycleViewSampleModel> serviceCatServiceTypeList;
//    private static String url;

    String servCatName = "";
    String servCatId = "";
    String servTypeName = "";
    String servTypeId = "";
    String scstId = "";
    //    PboServerRequestHandler handler;
    FloatingActionButton rechargeDetailReport;
    private RetrofitHelper retrofitHelper;


    public RechargeGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge_grid,
                container, false);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        rechargeDetailsListView = (RecyclerView) view
                .findViewById(R.id.rechargeDetailsListView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rechargeDetailsListView.setHasFixedSize(true);
        rechargeDetailsListView.setLayoutManager(mLayoutManager);

//        url = PayByOnlineConfig.SERVER_URL;

        Bundle bundle = getArguments();
        if (bundle != null) {

            servCatName = bundle.getString("servCatName");
            servCatId = bundle.getString("servCatId");
            servTypeName = bundle.getString("servTypeName");
            servTypeId = bundle.getString("servTypeId");
            scstId = bundle.getString("serviceCategory");

            Log.e("bundle grid ", "==>" + bundle.toString());
        }

        rechargeDetailReport = (FloatingActionButton) view.findViewById(R.id.rechargeDetailReport);

        rechargeDetailReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
                dashBoardActivity.selectPageToDisplay(R.id.purchaseReport, true, "0");
//                dashBoardActivity.viewRechargeReportFragment(R.id.purchaseReport,true,1);
            }
        });

        getAvailableServices();

        return view;
    }

    public void getAvailableServices() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getRechargeServiceDetails");
        params.put("servCatId", servCatId);
        Log.d("serviceID===", servCatId);
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());

//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleGetAvailableServicesResponse(response);
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
                    handleGetAvailableServicesResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Recharge Grid", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }


    public void handleGetAvailableServicesResponse(JSONObject response) throws JSONException {

        try {
//            handler.showProgressDialog("Please Wait !!!");

            Log.i("response", "" + response);

           /* DashBoardActivity dashBoardActivity = (DashBoardActivity) getContext();
            String userBalance = response.getString("userBalance");
            String currencyCode = response.getString("currencyCode");
            String hasPinCode = response.has("hasPinCode") ? response.getString("hasPinCode") : "NO";
            String userPhoto = response.getString("userPhoto");
            String notViewNotification = response.getString("notViewNotification");
            String favouriteServicesCount = response.getString("favouriteServicesCount");


            if(response.has("gnData")){
                JSONObject obj = response.getJSONObject("gnData");
                dashBoardActivity.setUserDetails((currencyCode+" "+userBalance),userPhoto,notViewNotification,favouriteServicesCount,hasPinCode,
                        obj.getString("Viber").toString(),
                        obj.getString("Facebook").toString(),
                        obj.getString("Twitter").toString(),
                        obj.getString("Google_plus").toString(),
                        obj.getString("Gmail").toString(),
                        obj.getString("Yahoo").toString(),
                        obj.getString("Skype").toString(),
                        obj.getString("Linkedin").toString(),
                        obj.getString("Microsoft_Outlook").toString());
            }else{
                dashBoardActivity.setUserDetails((currencyCode+" "+userBalance),userPhoto,notViewNotification,favouriteServicesCount,hasPinCode,"", "", "", "", "", "", "", "", "");
            }*/

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

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rechargeDetailsListView.setHasFixedSize(true);
            rechargeDetailsListView.setLayoutManager(mLayoutManager);

            parentRecycleViewAdapter = new ParentRecycleViewAdapter(getActivity(),
                    serviceCategoryList, R.layout.child_recycleview_layout, coordinatorLayout, servTypeId);

            rechargeDetailsListView.setAdapter(parentRecycleViewAdapter);

//            handler.hideProgressDialog();

        } catch (Exception e) {
            Log.i("Exception", "" + e);
//            handler.hideProgressDialog();
        }
    }

    @Override
    public void onResume() {


        super.onResume();
    }
}
