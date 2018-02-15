package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.ContactsModel;
import com.paybyonline.ebiz.R;

import java.util.List;

/**
 * Created by Sameer on 2/14/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private int lastCheckedPosition = -1;
    List<ContactsModel> contactsModels;
    Context context;
    OnContactCallBack onContactCallBack;

    public ContactAdapter(List<ContactsModel> contactsModels, Context context, OnContactCallBack onContactCallBack) {
        this.contactsModels = contactsModels;
        this.context = context;
        this.onContactCallBack = onContactCallBack;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, int position) {

        ContactsModel contactsModel = contactsModels.get(position);

        holder.contactName.setText(contactsModel.getContactName());
        holder.contactNumber.setText(contactsModel.getContactPhone());
        holder.checkContactNumber.setChecked(position == lastCheckedPosition);

        holder.checkContactNumber.setOnClickListener(v -> {
            if (lastCheckedPosition==holder.getAdapterPosition()){
                onContactCallBack.onContactClicked(contactsModel);
                lastCheckedPosition= -1;
                holder.checkContactNumber.setChecked(false);
            }else {
                lastCheckedPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0, contactsModels.size());
                onContactCallBack.onContactClicked(contactsModel);
            }
        });


        holder.itemView.setOnClickListener(v -> {
            if (lastCheckedPosition==holder.getAdapterPosition()){
                onContactCallBack.onContactClicked(contactsModel);
                lastCheckedPosition= -1;
                holder.checkContactNumber.setChecked(false);
//                    lastCheckedPosition = holder.getAdapterPosition();
//                    notifyItemRangeChanged(0, availabilityList.size());
                // lastCheckedPosition = holder.getAdapterPosition();
            }else {
                lastCheckedPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0, contactsModels.size());
                onContactCallBack.onContactClicked(contactsModel);
            }

        });


    }

    @Override
    public int getItemCount() {
        return contactsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName,contactNumber;
        CheckBox checkContactNumber;
        public ViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.contactName);
            contactNumber = (TextView)itemView.findViewById(R.id.contactNumber);
            checkContactNumber = (CheckBox) itemView.findViewById(R.id.checkContactNumber);

            checkContactNumber.setChecked(false);
        }
    }

    public interface OnContactCallBack{
        void onContactClicked(ContactsModel contactsModel);
    }
}
