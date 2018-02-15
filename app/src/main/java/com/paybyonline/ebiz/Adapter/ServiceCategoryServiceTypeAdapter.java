package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.ServiceCategoryServiceTypeDetails;
import com.paybyonline.ebiz.Adapter.ViewHolder.MyOwnServiceDetailsViewHolder;
import com.paybyonline.ebiz.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anish on 9/9/2016.
 */

public class ServiceCategoryServiceTypeAdapter extends RecyclerView.Adapter<MyOwnServiceDetailsViewHolder> {

    Context context;
    //    List<MyOwnServicesDetails> list = Collections.emptyList();
    LayoutInflater inflater;
    private List<ServiceCategoryServiceTypeDetails> serviceCategoryServiceTypeDetails = Collections.emptyList();
    ;

    public ServiceCategoryServiceTypeAdapter(Context context, List<ServiceCategoryServiceTypeDetails>
            serviceCategoryServiceTypeDetails) {

        this.context = context;
        this.serviceCategoryServiceTypeDetails = serviceCategoryServiceTypeDetails;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MyOwnServiceDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.service_category_type_grid_list, parent, false);
        return new MyOwnServiceDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOwnServiceDetailsViewHolder holder, int position) {

        final ServiceCategoryServiceTypeDetails model = serviceCategoryServiceTypeDetails.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return serviceCategoryServiceTypeDetails.size();
    }

}
