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
public class MyOwnServiceViewHolder extends RecyclerView.ViewHolder   {

    TextView service_name;
    TextView service_info;
    TextView initial_letter;
    public View holdItemView;

    public MyOwnServiceViewHolder(View itemView) {
        super(itemView);

        service_name = (TextView)itemView.findViewById(R.id.service_name);
        initial_letter = (TextView)itemView.findViewById(R.id.initial_letter);
        service_info = (TextView)itemView.findViewById(R.id.service_info);
        holdItemView=itemView;

    }
    public void bind(final MyOwnTags myOwnTags){

        service_name.setText(myOwnTags.getTagName());
        service_info.setText(myOwnTags.getTagCount());
        holdItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Bundle bundle=new Bundle();
                bundle.putString("tagName",myOwnTags.getTagName());
                bundle.putString("role", "USER");
                Fragment fragment = new MyOwnServiceCategoryDetailsFragment();
                FragmentManager fragmentManager =((FragmentActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();


            }
        });
        String firstLetter = String.valueOf(myOwnTags.getTagName().charAt(0));
        initial_letter.setText(firstLetter.toUpperCase());
        }


}
