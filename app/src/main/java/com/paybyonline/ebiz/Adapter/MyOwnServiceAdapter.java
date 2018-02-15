package com.paybyonline.ebiz.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.MyOwnTags;
import com.paybyonline.ebiz.Adapter.ViewHolder.MyOwnServiceViewHolder;
import com.paybyonline.ebiz.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anish on 9/17/2015.
 */

public class MyOwnServiceAdapter  extends RecyclerView.Adapter<MyOwnServiceViewHolder> {

    Context context;
    List<MyOwnTags> list = Collections.emptyList();
    LayoutInflater inflater;

    public MyOwnServiceAdapter(Context context, List<MyOwnTags> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyOwnServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.myownserviceadapterlayout , parent, false);
        return new MyOwnServiceViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyOwnServiceViewHolder holder, int position) {
        final MyOwnTags model = list.get(position);
        Log.i("MyOwnTags", "" + model);

        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void animateTo(List<MyOwnTags> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }
    private void applyAndAnimateRemovals(List<MyOwnTags> newModels) {
        for (int i = list.size() - 1; i >= 0; i--) {
            MyOwnTags model = list.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<MyOwnTags> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            MyOwnTags model = newModels.get(i);
            if (!list.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<MyOwnTags> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            MyOwnTags model = newModels.get(toPosition);
            int fromPosition = list.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public MyOwnTags removeItem(int position) {
        MyOwnTags model = list.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, MyOwnTags model) {
        list.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        MyOwnTags model = list.remove(fromPosition);
        list.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}

