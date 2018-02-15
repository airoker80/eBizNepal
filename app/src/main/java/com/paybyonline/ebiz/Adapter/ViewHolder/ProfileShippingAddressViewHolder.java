package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.ProfileShippingAddress;
import com.paybyonline.ebiz.R;

/**
 * Created by Anish on 11/7/2016.
 */
public class ProfileShippingAddressViewHolder extends RecyclerView.ViewHolder  {

        TextView id;
        TextView preference;
        TextView status;
        TextView profileName;
        TextView address1;
        TextView address2;
        TextView city;
        TextView state;
        TextView postalCode;
        TextView country;
        TextView remark;
        TextView companyName;
        LinearLayout stateLayout;


         public ProfileShippingAddressViewHolder(View itemView) {

        super(itemView);

        stateLayout=(LinearLayout)itemView.findViewById(R.id.stateLayout);
        id=(TextView)itemView.findViewById(R.id.id);
        preference=(TextView)itemView.findViewById(R.id.pref);
        profileName=(TextView)itemView.findViewById(R.id.profileName);
        status=(TextView)itemView.findViewById(R.id.status);
        address1=(TextView)itemView.findViewById(R.id.address1);
        address2=(TextView)itemView.findViewById(R.id.address2);
        city=(TextView)itemView.findViewById(R.id.city);
        state=(TextView)itemView.findViewById(R.id.state);
        postalCode=(TextView)itemView.findViewById(R.id.postalCode);
        country=(TextView)itemView.findViewById(R.id.country);
        remark=(TextView)itemView.findViewById(R.id.remark);
        companyName=(TextView)itemView.findViewById(R.id.com_name);

        }

public void bind(final ProfileShippingAddress profileShippingAddress){

        id.setText(profileShippingAddress.getId());
        preference.setText(profileShippingAddress.getPreference());
        profileName.setText(profileShippingAddress.getName());
        status.setText(profileShippingAddress.getStatus());
        address1.setText(profileShippingAddress.getAddressLine1());
        address2.setText(profileShippingAddress.getAddressLine2());
        companyName.setText(profileShippingAddress.getCompanyName());
        city.setText(profileShippingAddress.getCity());
        if(profileShippingAddress.getState().equals("")){
        stateLayout.setVisibility(View.GONE);
        }else{
        state.setText(profileShippingAddress.getState());
        }

        postalCode.setText(profileShippingAddress.getZipPostalCode());
        country.setText(profileShippingAddress.getCountry());
        remark.setText(profileShippingAddress.getRemark());

        }
}
