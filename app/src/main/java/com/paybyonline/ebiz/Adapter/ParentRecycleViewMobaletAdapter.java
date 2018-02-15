package com.paybyonline.ebiz.Adapter;

/**
 * Created by mefriend24 on 9/20/16.
 */

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.ServiceCatagoryDetails;
import com.paybyonline.ebiz.Adapter.ViewHolder.ParentRecycleViewDashboardHolder;
import com.paybyonline.ebiz.Adapter.ViewHolder.ParentRecycleViewHolder;
import com.paybyonline.ebiz.R;

import java.util.ArrayList;
import java.util.List;


public class ParentRecycleViewMobaletAdapter extends RecyclerView.Adapter<ParentRecycleViewDashboardHolder> {

    Context context;
    List<ServiceCatagoryDetails> serviceCategoryList =
            new ArrayList<ServiceCatagoryDetails>();
    LayoutInflater inflater;
    int childLayout;
    CoordinatorLayout coordinatorLayout;
    String servTypeId ="";


    public ParentRecycleViewMobaletAdapter(Context context, List<ServiceCatagoryDetails> serviceCategoryList,
                                           int childLayout, CoordinatorLayout coordinatorLayout, String servTypeId) {

        this.context = context;
        this.servTypeId = servTypeId;
        this.childLayout = childLayout;
        this.serviceCategoryList = serviceCategoryList;
        this.coordinatorLayout = coordinatorLayout;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ParentRecycleViewDashboardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.parent_recycleview_mobalet_layout, parent, false);
        return new ParentRecycleViewDashboardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ParentRecycleViewDashboardHolder holder, int position) {

        holder.bind(serviceCategoryList.get(position),childLayout,coordinatorLayout,servTypeId,position);
    }

    @Override
    public int getItemCount() {
        return serviceCategoryList.size();
    }

    // Resolved view changing issue after adding this
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

}
