package com.paybyonline.ebiz.util;



import com.paybyonline.ebiz.Adapter.Model.DashboarGridModel;
import com.paybyonline.ebiz.R;

import java.util.ArrayList;
import java.util.List;


public class GridData {
    public static List<DashboarGridModel> addGridData(){
        List<DashboarGridModel> dashboarGridModels = new ArrayList<DashboarGridModel>();


        dashboarGridModels.add(new DashboarGridModel((long) 1, R.drawable.ic_wallet,"Load Wallet"));
        dashboarGridModels.add(new DashboarGridModel((long) 2, R.drawable.ic_fundtransfer,"Fund Transfer"));
        dashboarGridModels.add(new DashboarGridModel((long) 3,  R.drawable.ic_merchant,"Merchant"));
        dashboarGridModels.add(new DashboarGridModel((long) 4,R.drawable.ic_recharge,"Recharge/Topup"));
        dashboarGridModels.add(new DashboarGridModel((long) 5, R.drawable.ic_flight_dashboard,"Flights"));
        dashboarGridModels.add(new DashboarGridModel((long) 6, R.drawable.ic_mytags_dashboard,"MyTags"));
        dashboarGridModels.add(new DashboarGridModel((long) 7, R.drawable.ic_profile_dashboard,"Profile"));
        dashboarGridModels.add(new DashboarGridModel((long) 8, R.drawable.ic_settings_dashboard,"Settings"));


        return dashboarGridModels;

    }
}
