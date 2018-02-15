package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.paybyonline.ebiz.Adapter.Model.DashboarGridModel;
import com.paybyonline.ebiz.Fragment.SendMoneyFormFragment;
import com.paybyonline.ebiz.PlasmaTech.Fragment.PlasmatechFlightFragment;
import com.paybyonline.ebiz.R;

import java.util.List;


public class MobaletHorizontalAdapter extends RecyclerView.Adapter<MobaletHorizontalAdapter.ViewHolder> {
    Context context;
    private List<DashboarGridModel> dashboarGridModelList;


    public MobaletHorizontalAdapter(Context context, List<DashboarGridModel> dashboarGridModelList) {
        this.context = context;
        this.dashboarGridModelList = dashboarGridModelList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_mobaletlinear,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
          final DashboarGridModel dashboarGridModel= dashboarGridModelList.get(position);

        holder.icons.setImageResource(dashboarGridModel.getImageId());
        holder.icons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                AppCompatActivity activity=(AppCompatActivity) context;
                if(dashboarGridModel.getIconName().equals("fund")){
                    fragment = new SendMoneyFormFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
                else if(dashboarGridModel.getIconName().equals("flights")){
                    fragment = new PlasmatechFlightFragment();
//                    fragment = new NewBookFlightFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                    Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
                }
                else if(dashboarGridModel.getIconName().equals("bus")){
                   /* fragment = new NewBookFlightFragment();
                    FragmentManager fragmentManager =activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();*/
                    Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dashboarGridModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icons;


        public ViewHolder(View itemView) {

            super(itemView);
            icons = (ImageView) itemView.findViewById(R.id.circle_view_icons);

        }
    }

}
