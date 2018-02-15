package com.paybyonline.ebiz.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Adapter.DashboardGridAdapter;
import com.paybyonline.ebiz.R;

/**
 * Created by Sameer on 1/2/2018.
 */

public class GridDashboardFragment extends Fragment {
    FloatingActionButton more_content;
    String[] dashboardMenu = {
            "Load Wallet",
            "Fund Transfer",
            "Merchant",
            "Recharge/Topup",
            "NEA",
            "DishHome TopUp",
            "Plasmatech",
    };
    int[] imageId = {
            R.drawable.ic_wallet,
            R.drawable.ic_fundtransfer,
            R.drawable.ic_merchant,
            R.drawable.ic_recharge,
            R.drawable.nea_blue,
            R.drawable.ic_dishhome_cobrand,
            R.drawable.ic_dishhome_cobrand,
    };

    RecyclerView dashboard_grid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_dashboard, container, false);
        ((DashBoardActivity) getActivity()).setTitle("Dashboard");
        dashboard_grid = (RecyclerView) view.findViewById(R.id.dashboard_grid);
        more_content=(FloatingActionButton)view.findViewById(R.id.more_content);
        dashboard_grid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        com.paybyonline.ebiz.Adapter.DashboardGridAdapter dashboardGridAdapter = new DashboardGridAdapter(getContext(), dashboardMenu, imageId);
        dashboard_grid.setAdapter(dashboardGridAdapter);

        more_content.setOnClickListener(v -> {
            Fragment fragment = new BuyProductFragment();
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        });
        return view;

    }

}