package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.BalanceRequest;
import com.paybyonline.ebiz.Adapter.ViewHolder.BalanceRequestListViewHolder;
import com.paybyonline.ebiz.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anish on 8/15/2016.
 */
public class BalanceRequestListAdapter extends RecyclerView.Adapter<BalanceRequestListViewHolder> {



        Context context;
        List<BalanceRequest> list = Collections.emptyList();
        LayoutInflater inflater;

    public BalanceRequestListAdapter(Context context, List<BalanceRequest> list) {

        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


        @Override
        public BalanceRequestListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = inflater.inflate(R.layout.balance_request_list, parent, false);
            return new BalanceRequestListViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(BalanceRequestListViewHolder holder, int position) {
        final BalanceRequest model = list.get(position);
     /*   holder.holderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        holder.bind(model);
    }




        @Override
        public int getItemCount() {
            return list.size();
        }
        public void animateTo(List<BalanceRequest> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateAdditions(models);
            applyAndAnimateMovedItems(models);
        }
        private void applyAndAnimateRemovals(List<BalanceRequest> newModels) {
            for (int i = list.size() - 1; i >= 0; i--) {
                BalanceRequest model = list.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<BalanceRequest> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                BalanceRequest model = newModels.get(i);
                if (!list.contains(model)) {
                    addItem(i, model);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<BalanceRequest> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                BalanceRequest model = newModels.get(toPosition);
                int fromPosition = list.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }
        public BalanceRequest removeItem(int position) {
            BalanceRequest model = list.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addItem(int position, BalanceRequest model) {
            list.add(position, model);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition, int toPosition) {
            BalanceRequest model = list.remove(fromPosition);
            list.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }
 /*   @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }*/
    }
/*
    Context context;
    List<MyOwnTags> list = Collections.emptyList();
    LayoutInflater inflater;

    public MyOwnServiceAdapter(Context context, List<MyOwnTags> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public MyOwnTags getItem(int position) {
        // TODO Auto-generated method stub
        return (MyOwnTags)list.get(position);
    }


    @Override
    public long getItemId(int id) {
        // TODO Auto-generated method stub
        return id;
    }

    public class ViewHolder{
        TextView tagName;
        TextView tagCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = new ViewHolder();
        if(view == null){
            view = inflater.inflate(R.layout.myownserviceadapterlayout , parent, false);

            viewHolder.tagName = (TextView)view.findViewById(R.id.tagName);
            viewHolder.tagCount = (TextView)view.findViewById(R.id.tagCount);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)view.getTag();
            //view.getTag();
        }
        MyOwnTags myOwnTags = null;
        myOwnTags = list.get(position);
        if(myOwnTags != null)
        {
            viewHolder.tagName.setText(myOwnTags.getTagName());
            viewHolder.tagCount.setText(myOwnTags.getTagCount().substring(myOwnTags.getTagCount().indexOf("[")+1
            ,myOwnTags.getTagCount().indexOf("]")));
        }


        return view;
    }*/




