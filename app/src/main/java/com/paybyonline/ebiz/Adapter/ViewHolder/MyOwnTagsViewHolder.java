package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.MyOwnTags;
import com.paybyonline.ebiz.Fragment.MyOwnServiceCategoryDetailsFragment;
import com.paybyonline.ebiz.R;

/**
 * Created by Anish on 8/15/2016.
 */
public class MyOwnTagsViewHolder  extends RecyclerView.ViewHolder {


    TextView availableServiceCount;
    TextView serviceName;
    TextView serviceType;

    View holderItems;

    public MyOwnTagsViewHolder(View itemView) {
        super(itemView);

        holderItems=itemView;

        serviceName = (TextView) itemView.findViewById(R.id.serviceName);
        serviceType = (TextView) itemView.findViewById(R.id.serviceType);
     availableServiceCount = (TextView) itemView.findViewById(R.id.availableServiceCount);


    }
    public void bind(final MyOwnTags myOwnTags,int position,int count){

        serviceName.setText(myOwnTags.getTagName());
        serviceType.setText(myOwnTags.getTagServices());
        availableServiceCount.setText(myOwnTags.getTagCount());
        

        holderItems.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context context = v.getContext();

                Bundle bundle = new Bundle();
                bundle.putString("tagName", myOwnTags.getTagName());
                bundle.putString("role", "ADMIN");
                Fragment fragment = new MyOwnServiceCategoryDetailsFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();


            }
        });
    }
}
