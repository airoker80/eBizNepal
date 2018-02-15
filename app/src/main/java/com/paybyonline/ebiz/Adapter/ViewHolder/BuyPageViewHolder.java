package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.BuyPageModel;
import com.paybyonline.ebiz.Fragment.BuyProductFormFragment;
import com.paybyonline.ebiz.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Anish on 4/24/2016.
 */

public class BuyPageViewHolder extends RecyclerView.ViewHolder {


    TextView productCat;
    TextView productService;
    ImageView productImg;
    Context mContext;
    public View holdItemView;

    public BuyPageViewHolder(View itemView) {
        super(itemView);

        holdItemView=itemView;

        productImg = (ImageView) itemView.findViewById(R.id.productImg);
        productCat = (TextView) itemView.findViewById(R.id.productCat);
        productService = (TextView) itemView.findViewById(R.id.productService);


    }
    public void bind(final BuyPageModel model, Context context, RecyclerView.ViewHolder viewHolder) {

        if (model.getIsEnable().equals("Y")) {
            productCat.setText(model.getServiceCategory());
            productService.setText(model.getServiceType());
            this.mContext = context;

            Picasso.with(context)
                    .load(model.getLogoName())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(productImg);


            holdItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment = new BuyProductFormFragment();
                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    Bundle bundle = new Bundle();
                    bundle.putString("scstId", model.getScstId());
                    bundle.putString("serviceCategory", model.getServiceCategory());
                    fragmentTransaction.addToBackStack(null);
                    fragment.setArguments(bundle);
                    fragmentTransaction.commit();


                }
            });

     /*       holdItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Fragment fragment= new BuyProductFormFragment();
                    FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();

                }
            });*/


        }else {
            viewHolder.itemView.setVisibility(View.GONE);
        }
    }


}
