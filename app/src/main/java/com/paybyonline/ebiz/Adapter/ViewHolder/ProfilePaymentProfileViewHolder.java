package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.PaymentProfile;
import com.paybyonline.ebiz.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Anish on 11/7/2016.
 */

public class ProfilePaymentProfileViewHolder  extends RecyclerView.ViewHolder  {

    TextView pref;
    TextView profName;
    TextView payment_method;
    TextView bank_name;
    ImageView profile_img;
    LinearLayout bankLayout;
    View holderItem;

    public ProfilePaymentProfileViewHolder(View itemView) {
        super(itemView);
       this.holderItem=itemView;

        bankLayout = (LinearLayout) holderItem.findViewById(R.id.bankLayout);

        profile_img = (ImageView) holderItem.findViewById(R.id.profile_img);
        pref = (TextView) holderItem.findViewById(R.id.pref);
        profName = (TextView) holderItem.findViewById(R.id.profName);
        payment_method = (TextView) holderItem.findViewById(R.id.pay_method);
        bank_name = (TextView) holderItem.findViewById(R.id.bank_name);

        Log.i("paymentProfile", "" + profile_img);
        Log.i("paymentProfile", "" + pref);
        Log.i("paymentProfile", "" + profName);
        Log.i("paymentProfile", "" + payment_method);
        Log.i("paymentProfile", "" + bank_name);

    }

    public void bind(final PaymentProfile paymentProfile) {

        Log.i("paymentProfile", "" + paymentProfile);
        Log.i("paymentProfile", "" + paymentProfile.getUsedBy());
        Log.i("paymentProfile", "" + paymentProfile.getBankName());
        Log.i("paymentProfile", "" + paymentProfile.getProfName());
        Log.i("paymentProfile", "" + paymentProfile.getProfImg());

        Picasso.with(holderItem.getContext())
                .load(paymentProfile.getProfImg())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(profile_img);

        pref.setText(paymentProfile.getUsedBy());
        profName.setText(paymentProfile.getProfName());
        payment_method.setText(paymentProfile.getPaymentMethod());

        if(paymentProfile.getBankName().equals("")){

            bankLayout.setVisibility(View.GONE);

        }else {

            bank_name.setText(paymentProfile.getBankName());

        }

    }


}
