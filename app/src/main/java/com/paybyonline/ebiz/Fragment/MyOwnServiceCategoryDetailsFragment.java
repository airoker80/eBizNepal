package com.paybyonline.ebiz.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.Model.MyOwnServicesDetails;
import com.paybyonline.ebiz.Adapter.Model.ServiceCategoryServiceTypeDetails;
import com.paybyonline.ebiz.Adapter.ServiceCategoryServiceTypeAdapter;
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

public class MyOwnServiceCategoryDetailsFragment extends Fragment {

    private MyUserSessionManager myUserSessionManager;
    private UserDeviceDetails userDeviceDetails;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private HashMap<String, String> userDetails;
    CoordinatorLayout coordinatorLayout;
    String myTagName = "";
    String role = "";

    RecyclerView mySeviceCatTypeList;

    private List<ServiceCategoryServiceTypeDetails> serviceCategoryServiceTypeDetails;

    TextView tagName;
    private RetrofitHelper retrofitHelper;

    public MyOwnServiceCategoryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_own_service_category_details, container, false);
        //initializeComponents(view);

        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();

        ((DashBoardActivity) getActivity())
                .setTitle("My Own Service Category Details");

        tagName = (TextView) view.findViewById(R.id.tagName);
        mySeviceCatTypeList = (RecyclerView) view.findViewById(R.id.myServiceCatTypeGrid);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            myTagName = bundle.getString("tagName");
            tagName.setText(Html.fromHtml("<b>Tag Name:</b>" + myTagName));
            role = bundle.getString("role");
        }

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mySeviceCatTypeList.setHasFixedSize(true);
        mySeviceCatTypeList.setLayoutManager(mLayoutManager);
        mySeviceCatTypeList.setItemAnimator(new DefaultItemAnimator());


        obtainMyServicesDetails();


        return view;

    }

    public void obtainMyServicesDetails() {


//		RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "myServiceCategoryDetails");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("tagName", myTagName);
        params.put("role", role);
//		PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
//		handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!",
//				params, new PboServerRequestListener() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//				try {
//                   Log.i("response",""+response);
//					handleMyServiceCategoryDetailsResponse(response);
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				//Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//			}
//		});
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("response", "" + jsonObject);
                    handleMyServiceCategoryDetailsResponse(jsonObject);

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

    public void handleMyServiceCategoryDetailsResponse(JSONObject response) throws JSONException {

        JSONArray jsonArray;
        try {

            jsonArray = response.getJSONArray("data");
            Log.i("jsonArray ", "" + jsonArray);
            serviceCategoryServiceTypeDetails = new ArrayList<ServiceCategoryServiceTypeDetails>();
            ArrayList<MyOwnServicesDetails> serviceCategoryServiceTypeDetailsList = new ArrayList<MyOwnServicesDetails>();

            if (jsonArray.length() != 0) {

                // Loop through each array element, get JSON object
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);

                    serviceCategoryServiceTypeDetails.add(new ServiceCategoryServiceTypeDetails(
                            obj.get("serviceCategoryName")
                                    .toString(), obj.get("serviceTypeName").toString()
                            , obj.get("serviceCategoryId")
                            .toString(), obj.get("serviceTypeId").toString()
                            , obj.get("scstId").toString(), PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" +
                            Uri.encode(obj.get("serviceLogo").toString()), obj.getString("isProductEnable")));


					/*selectedFavorites.add(new
                            MyOwnServicesDetails(obj.get("serviceCategoryName").toString(),
							obj.get("serviceTypeName").toString(),obj.get("serviceLogo").toString()));*/

                }

                ServiceCategoryServiceTypeAdapter adapter = new ServiceCategoryServiceTypeAdapter(getActivity(),
                        serviceCategoryServiceTypeDetails);

                mySeviceCatTypeList.setAdapter(adapter);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
