package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.FavouriteModel;
import com.paybyonline.ebiz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sameer on 12/12/2017.
 */

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder> {
    private List<FavouriteModel> favouriteModelList = new ArrayList<>();
    private Context context;
    public FavCallBack favCallBack;
    private boolean isSetupTime = false;
    private int count = 0;

    public FavouriteListAdapter(List<FavouriteModel> favouriteModelList, FavCallBack favCallBack) {
        this.favouriteModelList = favouriteModelList;
        this.favCallBack = favCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fav_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavouriteModel favouriteModel = favouriteModelList.get(position);
        holder.label.setText(favouriteModel.getName());
        isSetupTime = true;
        holder.check.setChecked(favouriteModel.isSelected());
        isSetupTime = false;
        Picasso.with(context)
                .load(favouriteModel.getLogo())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.fav_logo);
    }

    @Override
    public int getItemCount() {
        return favouriteModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        CheckBox check;
        ImageView fav_logo;

        public ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
            check = (CheckBox) itemView.findViewById(R.id.check);
            fav_logo = (ImageView) itemView.findViewById(R.id.fav_logo);

            check.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isSetupTime) {
                    FavouriteModel favouriteModel = favouriteModelList.get(getAdapterPosition());
                    Log.d("checked", String.valueOf(favouriteModelList.get(getAdapterPosition()).isSelected()));
                    favCallBack.onchecked(check, favouriteModel);
                }
            });
        }
    }

    public void incrementCount() {
        count++;
    }

    public void decrementCount() {
        count--;
    }

    public int getCount() {
        return count;
    }

    public interface FavCallBack {
        void onchecked(CheckBox checkBox, FavouriteModel favouriteModel);
    }
}
