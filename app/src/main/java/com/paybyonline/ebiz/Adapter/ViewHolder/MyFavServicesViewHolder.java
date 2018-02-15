package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.ServiceType;
import com.paybyonline.ebiz.R;

/**
 * Created by Anish on 9/29/2016.
 */
public class MyFavServicesViewHolder extends RecyclerView.ViewHolder{

    TextView servicesType;

    public MyFavServicesViewHolder(View itemView) {
        super(itemView);
        servicesType=(TextView)itemView.findViewById(R.id.servicesType);
    }

    public void bind(final Context context,final ServiceType childArrayList) {

        servicesType.setText(childArrayList.getService_type_name());
        Log.i("serviceCatDebind", "" + childArrayList.getService_type_name());
        servicesType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Fragment fragment = null;
                FragmentTransaction ft =context.getSupportFragmentManager()
                        .beginTransaction();
                ft.addToBackStack(null);
                fragment = new RechargeNowQuick();
                ft.replace(R.id.content_frame, fragment);

                Bundle bundle = new Bundle();
                //bundle.putString("servCatName", childArrayList.getServiceCategoryName());
               // bundle.putString("servCatId", model.getServiceCategoryId());

                fragment.setArguments(bundle);
                ft.commit();*/
            }
        });
    }

    }
