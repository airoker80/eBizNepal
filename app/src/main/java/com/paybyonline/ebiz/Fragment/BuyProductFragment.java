package com.paybyonline.ebiz.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.BuyPageAdapter;
import com.paybyonline.ebiz.Adapter.Model.BuyPageModel;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
import com.paybyonline.ebiz.serverdata.PboServerRequestListener;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyProductFragment extends Fragment {

    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    CoordinatorLayout coordinateLayout;
    RecyclerView productList;
    ArrayList<BuyPageModel> listOfProduct = new ArrayList<>();
    BuyPageAdapter buyPageAdapter;
    private GridLayoutManager lLayout;
    FloatingActionButton rechargeDetailReport;
    private RetrofitHelper retrofitHelper;

    public BuyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_product, container, false);
        myUserSessionManager = new MyUserSessionManager(getActivity());
        userDeviceDetails = new UserDeviceDetails(getActivity());
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
       /* Spannable text = new SpannableString("Buy/Recharge");
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/
        //((DashBoardActivity) getActivity()).setTitle("Buy/Recharge");
        coordinateLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        productList = (RecyclerView) view.findViewById(R.id.ServiceProviderListView);
        // int mNoOfColumns = calculateNoOfColumns();
        Log.i("calculateNoOfColumns", "" + calculateNoOfColumns());
        lLayout = new GridLayoutManager(getContext(), 2);

        //  RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        productList.setHasFixedSize(true);
        productList.setLayoutManager(new GridLayoutManager(getContext(),calculateNoOfColumns()));

        rechargeDetailReport = (FloatingActionButton) view.findViewById(R.id.rechargeDetailReport);

        rechargeDetailReport.setOnClickListener(view1 -> {
            DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
            dashBoardActivity.selectPageToDisplay(R.id.purchaseReport, true, "1");
        });

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        getProductListContent();
        return view;
    }

    public void getProductListContent() {

//        final RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        // Make Http call
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "listMerchantProduct");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());


/*        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinateLayout, getActivity());
        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "", params,
                (statusCode, headers, response) -> {

                    try {
                        Log.i("Listresponse", params.toString());
                        handleProductListContentResponse(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });*/


        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("Listresponse", jsonObject.toString());
                    handleProductListContentResponse(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Buy Product", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, params);
    }

    public void handleProductListContentResponse(JSONObject response) throws JSONException {

        Log.i("Listresponse", "response");
        Log.i("Listresponse", response + "");
        listOfProduct = new ArrayList<BuyPageModel>();

        JSONArray productData = response.getJSONArray("advanceList");
        if (productData.length() > 0) {

            for (int i = 0; i < productData.length(); i++) {

                JSONObject obs = productData.getJSONObject(i);

                Log.i("obs", obs + "");
                // Boolean isParticipated = obs.getString("isParticipated").equals("YES") ? true : false;
                if (obs.getString("isEnable").equals("Y")){
                    listOfProduct.add(new BuyPageModel(obs.getString("serviceCategory"),
                            obs.getString("serviceType"), obs.getString("serviceClassification"),
                            obs.getString("tag"), obs.getString("productType"),
                            PayByOnlineConfig.BASE_URL + "serviceCategoryServiceTypeLogo/" +
                                    Uri.encode(obs.getString("logoName")), obs.getString("scstId"),
                            obs.getString("merchantType"), obs.getString("countryName"), obs.getString("isEnable"), obs.getString("isSelected")));

                }

            }
            buyPageAdapter = new BuyPageAdapter(getContext(), listOfProduct);
            productList.setAdapter(buyPageAdapter);
        } else {
            // emptyMessage.setVisibility(View.VISIBLE);
        }


    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    @Override
    public void onResume() {

        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
       /* Spannable text = new SpannableString("Buy/Recharge");
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/
        //((DashBoardActivity) getActivity()).setTitle("Buy/Recharge");
        super.onResume();
    }
}
