package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Fragment.BuyProductFormFragment;
import com.paybyonline.ebiz.Fragment.BuyProductFragment;
import com.paybyonline.ebiz.Fragment.RechargeBuyFragment;
import com.paybyonline.ebiz.Fragment.RechargeGridFragment;
import com.paybyonline.ebiz.Fragment.SendMoneyFormFragment;
import com.paybyonline.ebiz.Fragment.WalletFragment;
import com.paybyonline.ebiz.PlasmaTech.Fragment.PlasmatechFlightFragment;
import com.paybyonline.ebiz.R;

/**
 * Created by Sameer on 1/2/2018.
 */

public class DashboardGridAdapter extends RecyclerView.Adapter<DashboardGridAdapter.ViewHolder> {
    Context context;
    private final String[] web;
    private final int[] Imageid;

    public DashboardGridAdapter(Context context, String[] web, int[] imageid) {
        this.context = context;
        this.web = web;
        Imageid = imageid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_grid,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.circle_view_icons.setImageResource(Imageid[position]);
        holder.dashboard_text.setText(web[position]);
        holder.circle_view_icons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                AppCompatActivity activity=(AppCompatActivity) context;
                if (position==0){
                    fragment = new WalletFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putBoolean("openAddForm", true);
                    fragment.setArguments(bundle1);
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
                if (position==1){
                    fragment = new SendMoneyFormFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
                if (position==2){
                    fragment = new BuyProductFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
                if (position==3){
                    fragment = new RechargeBuyFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
                if (position==4){
                    fragment = new BuyProductFormFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("scstId","71");
                    bundle.putString("serviceCategory","Nepal Electricity Authority");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
                if (position==5){
                    fragment = new RechargeGridFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("servCatId","4");
                    bundle.putString("servCatName","DishHome");
                    bundle.putString("scstId","71");
                    bundle.putString("servTypeName","DishHome Topup");
                    bundle.putString("servTypeId","64");
//                    bundle.putString("serviceCategory","Nepal Electricity Authority");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }if (position==6){
                    fragment = new PlasmatechFlightFragment();
//                    fragment = new NewBookFlightFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return web.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView circle_view_icons;
        TextView dashboard_text;
        public ViewHolder(View itemView) {

            super(itemView);
            circle_view_icons=(ImageView) itemView.findViewById(R.id.circle_view_icons);
            dashboard_text=(TextView)itemView.findViewById(R.id.dashboard_text);
        }
    }
}
