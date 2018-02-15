package com.paybyonline.ebiz.Fragment;


import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.FavouriteListAdapter;
import com.paybyonline.ebiz.Adapter.Model.FavouriteModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteServicesFragment extends Fragment implements FavouriteListAdapter.FavCallBack {
    StringBuilder stringBuilder;
    RecyclerView mRecyclerView;
    FloatingActionButton add_fav_services;
    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
    private RetrofitHelper retrofitHelper;
    List<FavouriteModel> wholeList = new ArrayList<>();
    List<FavouriteModel> unpreFerredList = new ArrayList<>();
    List<FavouriteModel> remainingList = new ArrayList<>();
    List<FavouriteModel> preferredList = new ArrayList<>();
    private List<ServiceCategoryServiceTypeDetails> serviceCategoryServiceTypeDetailsList = new ArrayList<>();
    private JSONObject originalJson;
    private FavouriteListAdapter favouriteListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_services, container, false);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        ((DashBoardActivity) getActivity()).setTitle("Favourite Services");
        stringBuilder = new StringBuilder();
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fabServiceRecycleView);
        add_fav_services = (FloatingActionButton) view.findViewById(R.id.add_fav_services);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        obtainMyFavouriteServices();

        add_fav_services.setOnClickListener(v -> {
            addServicesOnListModel(getContext());
        });

        return view;
    }

    @Override
    public void onResume() {
        ((DashBoardActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        ((DashBoardActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ((DashBoardActivity) getActivity()).setTitle("Favourite Services");
        ((DashBoardActivity) getActivity()).setFragmentName("Favourite Services");
        super.onResume();
    }

    public void obtainMyFavouriteServices() {
        Map<String, String> params = new HashMap<>();
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    clearAllLists();
                    originalJson = jsonObject;
                    handleObtainMyFavouriteServicesResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Favorite Services", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_FAVOURITE_SERVICE, params, null, null);

    }

    public void handleObtainMyFavouriteServicesResponse(JSONObject response) throws JSONException {

        try {
            JSONArray jsonArray = response.getJSONArray("preferredList");
            JSONArray unPreferredList = response.getJSONArray("unPreferredList");
            JSONArray remainingListArray = response.getJSONArray("remainingList");

            if (response.has("preferredList")) {

                // Loop through each array element, get JSON object
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    FavouriteModel favouriteModel = new FavouriteModel(obj.getString("sName"), obj.getString("id"),
                            PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" + Uri.encode(obj.getString("logoName")),
                            true, false, false);
                    favouriteModel.setSelected(true);
                    preferredList.add(favouriteModel);
                    serviceCategoryServiceTypeDetailsList.add(new ServiceCategoryServiceTypeDetails(
                            obj.get("sName").toString(),
                            obj.getString("serviceTypeName").toString(),
                            obj.get("serviceCategoryId").toString(),
                            obj.get("serviceTypeId").toString(),
                            obj.get("id").toString(), PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" + Uri.encode(obj.get("logoName").toString()),
                            obj.getString("isProductEnable").toString()));
                }
                wholeList.addAll(preferredList);

                ServiceCategoryServiceTypeAdapter adapter = new ServiceCategoryServiceTypeAdapter(getActivity(),
                        serviceCategoryServiceTypeDetailsList);

                mRecyclerView.setAdapter(adapter);

            }

            if (response.has("unPreferredList")) {
                for (int j = 0; j < unPreferredList.length(); j++) {
                    JSONObject obj = (JSONObject) unPreferredList.get(j);
                    unpreFerredList.add(new FavouriteModel(obj.getString("cName"), obj.getString("scstId"),
                            PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" + Uri.encode(obj.get("logoName").toString()),
                            false, true, false));
                }
                wholeList.addAll(unpreFerredList);
            }
            if (response.has("remainingList")) {
                JSONArray jarr = (JSONArray) remainingListArray.get(0);
                JSONArray jarr1 = (JSONArray) jarr.get(0);
                for (int i = 0; i < jarr1.length(); i++) {
                    JSONObject obj = (JSONObject) jarr1.get(i);
                    remainingList.add(new FavouriteModel(obj.getString("cName"), obj.getString("scstId"),
                            PayByOnlineConfig.BASE_URL + "CategoryTypeLogo/" + Uri.encode(obj.get("logoName").toString()),
                            false, false, true));
                }
            }
            wholeList.addAll(remainingList);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void addServicesOnListModel(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        Log.i("onclick1", "" + inflater);

        View dialogView = inflater.inflate(R.layout.dialog_list_fav_items, null);

        RecyclerView fav_item_list = (RecyclerView) dialogView.findViewById(R.id.fav_item_list);
        favouriteListAdapter = new FavouriteListAdapter(wholeList, this);
        fav_item_list.setHasFixedSize(true);
        fav_item_list.setLayoutManager(new LinearLayoutManager(getContext()));
        fav_item_list.setAdapter(favouriteListAdapter);

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final android.support.v7.app.AlertDialog dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Change Favourites")
                .setPositiveButton("Change", null)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create();
        Log.i("onclick2", "dialog call");
        dialogBuilder.setOnShowListener(dialog -> {
            final Button btnAccept = dialogBuilder.getButton(
                    android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
            btnAccept.setOnClickListener(v -> {
                remainingList.clear();
                preferredList.clear();
                for (FavouriteModel favouriteModel : wholeList) {
                    if (favouriteModel.isSelected() && !preferredList.contains(favouriteModel)) {
                        if (unpreFerredList.contains(favouriteModel)) {
                            unpreFerredList.remove(favouriteModel);
                        }
                        preferredList.add(favouriteModel);
                    }
                }
                changeFavourite(getSendingList(preferredList), getSendingList(unpreFerredList));
                dialogBuilder.dismiss();
            });
            final Button btnDecline = dialogBuilder.getButton(DialogInterface.BUTTON_NEGATIVE);
            btnDecline.setOnClickListener(v -> {
                resetLists();
                dialogBuilder.dismiss();
            });
        });

        dialogBuilder.show();
    }

    private void resetLists() {
        try {
            clearAllLists();
            handleObtainMyFavouriteServicesResponse(originalJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clearAllLists() {
        serviceCategoryServiceTypeDetailsList.clear();
        remainingList.clear();
        preferredList.clear();
        unpreFerredList.clear();
        wholeList.clear();
    }

    @Override
    public void onchecked(CheckBox checkBox, FavouriteModel favouriteModel) {
        if (checkBox.isChecked()) {
            if (!sizeExceeded()) {
                favouriteModel.setSelected(true);
                favouriteListAdapter.incrementCount();
            } else {
                Toast.makeText(getActivity(), "Cannot add more than 5 to favorites", Toast.LENGTH_SHORT).show();
                checkBox.setChecked(false);
                favouriteModel.setSelected(false);
            }
        } else {
            favouriteModel.setSelected(false);
            favouriteListAdapter.decrementCount();
        }
    }

    private boolean sizeExceeded() {
        return (favouriteListAdapter.getCount() + preferredList.size()) >= 5;
    }

    private String getSendingList(List<FavouriteModel> arr) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            if (i == arr.size() - 1) {
                string.append(arr.get(i).getId());
            } else {
                string.append(arr.get(i).getId() + ",");
            }
        }
        return string.toString();
    }

    private void changeFavourite(String preferedValue, String unpreferedValue) {
        //        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("preferredVal", preferedValue);
        params.put("unPreferredVal", unpreferedValue);
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());


        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    obtainMyFavouriteServices();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Favorite Services", "error");
                Toast.makeText(getActivity(), "Could not reset favorites", Toast.LENGTH_SHORT).show();
                resetLists();
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_FAVOURITE_SERVICE, params, null, null);
    }
}
