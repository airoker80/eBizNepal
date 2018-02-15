package com.paybyonline.ebiz.Fragment;

import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.ServiceCategoryDetailsGrid;
import com.paybyonline.ebiz.Adapter.ServiceCategoryViewAdapter;
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
 * A simple {@link Fragment} subclass.
 */
public class RechargeFragment extends Fragment {


    RecyclerView listView;
    CoordinatorLayout coordinatorLayout;
    private List<ApplicationInfo> mAppList;
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private List<ServiceCategoryDetailsGrid> serviceCategoryServiceTypeDetails;
    private HashMap<String, String> userDetails;
    //    PboServerRequestHandler handler;
    String[] serviceCatNameList = null;
    String[] imageIdList = null;
    String[] serviceCategoryId = null;
    private RetrofitHelper retrofitHelper;

    public RechargeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        mAppList = getActivity().getPackageManager().getInstalledApplications(0);
        listView = (RecyclerView) view.findViewById(R.id.listView);

//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
        setHasOptionsMenu(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Log.i("msgsss", "listView" + listView);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        obtainServiceCategoryTypeDetails();
        return view;
    }

    public void obtainServiceCategoryTypeDetails() {


        Log.i("obtainSerCatTypeDetails", "obtainServiceCategoryTypeDetails inside");
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "obtainAvailableProducts");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());

        // Make Http call
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait...", params, new PboServerRequestListener() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//
//                    handleObtainServiceCategoryTypeDetailsResponse(response);
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//
//                }
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainServiceCategoryTypeDetailsResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Recharge:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainServiceCategoryTypeDetailsResponse(JSONObject response) throws JSONException {


        System.out.println("DetailsResponse" + response);
        JSONArray jsonArray = response.getJSONArray("data");
        Log.i("jsonArray", jsonArray + "");
/////////////////////////////////////////////////////////////////////

        serviceCatNameList = new String[jsonArray.length()];
        serviceCategoryId = new String[jsonArray.length()];
        imageIdList = new String[jsonArray.length()];
        serviceCategoryServiceTypeDetails = new ArrayList<>();


        if (jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                Log.i("JSONObject", "" + obj);

                serviceCatNameList[i] = obj.getString("categoryName");
                serviceCategoryId[i] = obj.getString("categoryId");

                if (obj.getString("scLogo").equals("null")) {
                    // imageIdList[i] = "http://192.168.1.125:8080/Pbo-Android/CategoryTypeLogo/";
                    imageIdList[i] = PayByOnlineConfig.BASE_URL + "merchantLogo/merchantLogo/"
                            + Uri.encode(obj.getString("merchantLogo"));

                    serviceCategoryServiceTypeDetails.add(new ServiceCategoryDetailsGrid(
                            obj.getString("categoryId"), obj.getString("categoryName"),
                            imageIdList[i], obj.getString("isMerchant")));


                } else {

                    imageIdList[i] = PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" + Uri.encode(obj.getString("scLogo"));
                    serviceCategoryServiceTypeDetails.add(new ServiceCategoryDetailsGrid(
                            obj.get("categoryId").toString(),
                            obj.getString("categoryName"),
                            imageIdList[i], obj.getString("isMerchant")));

                    Log.i("JSONObject", "" + obj);

                }
            }

            Log.i("scLogo", "......");

            ServiceCategoryViewAdapter adapter = new ServiceCategoryViewAdapter(
                    getActivity(), serviceCategoryServiceTypeDetails);

            listView.setAdapter(adapter);
            Log.e("response:", "" + response);
        }
    }


}
