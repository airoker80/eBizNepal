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

import com.paybyonline.ebiz.Adapter.Model.ServiceCatagoryDetails;
import com.paybyonline.ebiz.Adapter.Model.ServiceType;
import com.paybyonline.ebiz.Adapter.RechargePageAdapter;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anish on 9/28/2016.
 */
public class RechargeNowFragment extends Fragment {

    RecyclerView rechargeDetailsListView;
    CoordinatorLayout coordinatorLayout;
    MyUserSessionManager myUserSessionManager;
    RechargePageAdapter rechargePageAdapter;
    // URL to get JSON Array
    private List<ServiceType> serviceTypeList;
    private List<ServiceCatagoryDetails> serviceCategoryList;
    String[] id;
    String servCatName;
    String servCatId;
    String servTypeName;
    String servTypeId;

    public RechargeNowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge_now,
                container, false);

        rechargeDetailsListView = (RecyclerView) view.findViewById(R.id.rechargeDetailsListView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rechargeDetailsListView.setHasFixedSize(true);
        rechargeDetailsListView.setLayoutManager(mLayoutManager);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        myUserSessionManager = new MyUserSessionManager(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {

            servCatName = bundle.getString("servCatName");
            servCatId = bundle.getString("servCatId");
            servTypeName = bundle.getString("servTypeName");
            servTypeId = bundle.getString("servTypeId");

        }

        Log.i("values", servCatName + " " + servCatId);

        getServiceDetails();

        return view;
    }

    public void getServiceDetails() {

        String data = "";
//        String data= GenerateJsonData.getJsonData(getContext(), R.raw.service_details);
        Log.i("d" +
                "ata", "" + data);
        try {
            JSONArray jsonData = new JSONArray(data);

            serviceCategoryList = new ArrayList<ServiceCatagoryDetails>();

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject object = jsonData.getJSONObject(i);

                JSONArray jsonData2 = object.getJSONArray("serviceTypeList");

                serviceTypeList = new ArrayList<ServiceType>();
                if (servCatId.trim().equals(object.getString("serviceCategoryId").trim())) {

                    for (int j = 0; j < jsonData2.length(); j++) {
                        JSONObject object2 = jsonData2.getJSONObject(j);

                        serviceTypeList.add(new ServiceType(object2.getString("startsWith"), object2.getString("minVal"),
                                object2.getString("maxVal"),
                                object2.getString("iLabel"),
                                object2.getString("isPinRechargeService"),
                                object2.getString("categoryLength"),
                                object2.getString("serviceTypeName"),
                                object2.getString("serviceTypeId"),
                                object2.getString("pCountry"),PayByOnlineConfig.BASE_URL +
                                                                                "CategoryTypeLogo/" + Uri.encode(object2.getString("logoName")),
                                object2.getString("scstAmountType"),
                                object2.getString("currency"),
                                object2.getString("scstAmountValue"),
                                object2.getString("isProductEnable"),
                                object2.getString("enablePromoCode"),
                                object2.getString("scstId")));
                    }

                    serviceCategoryList.add(new ServiceCatagoryDetails(object.getString("serviceCategoryId")
                            , object.getString("serviceCategoryName"), object.getString("serviceCategoryLogo")
                            , serviceTypeList));
                }


            }

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rechargeDetailsListView.setHasFixedSize(true);
            rechargeDetailsListView.setLayoutManager(mLayoutManager);
            rechargePageAdapter = new RechargePageAdapter(getActivity(),
                    serviceCategoryList, R.layout.child_recycleview_layout, coordinatorLayout, servCatId, servTypeId);
            rechargeDetailsListView.setAdapter(rechargePageAdapter);


        } catch (Exception e) {
            Log.i("Exception", "" + e);
        }

        // rechargeDataArray = data.getJSONArray("data");
        // JSONObject jsonObject=data.getJSONObject("serviceTypeList");

    }
}
