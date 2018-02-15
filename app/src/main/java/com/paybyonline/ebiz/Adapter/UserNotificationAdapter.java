package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.UserNotification;
import com.paybyonline.ebiz.Adapter.ViewHolder.UserNotificationViewHolder;
import com.paybyonline.ebiz.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mefriend24 on 10/20/16.
 */
public class UserNotificationAdapter extends RecyclerView.Adapter<UserNotificationViewHolder> {

    private LayoutInflater mInflater;
    private List<UserNotification> userNotificationList;
    Context context;


    public UserNotificationAdapter(Context context, List<UserNotification> userNotificationList) {

        this.context = context;

        this.mInflater = LayoutInflater.from(context);
        this.userNotificationList = new ArrayList<>(userNotificationList);
        Log.i("userNotificationList",""+userNotificationList.size());

    }

    @Override
    public UserNotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.user_notification_item, parent, false);
        return new UserNotificationViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(UserNotificationViewHolder holder, final int position) {


        final UserNotification model = userNotificationList.get(position);
        holder.bind(model, context);

    }

    @Override
    public int getItemCount() {

        return userNotificationList.size();
    }


}
