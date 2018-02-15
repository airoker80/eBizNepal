package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.MyCustomRecycleViewModel;
import com.paybyonline.ebiz.Fragment.BankDepositFormFragment;
import com.paybyonline.ebiz.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Anish on 8/17/2016.
 */
public class MyCustomRecycleViewHolder extends RecyclerView.ViewHolder {

    TextView id_name,bank_account_no,bank_address;
    ImageView imageView;
    View holderItem;
    Context holderContext;

    public MyCustomRecycleViewHolder(View itemView) {

        super(itemView);

        holderContext = itemView.getContext();
        id_name= (TextView) itemView.findViewById(R.id.id_name);
        imageView= (ImageView)itemView.findViewById(R.id.imageView);
        bank_account_no= (TextView)itemView.findViewById(R.id.bank_account_no);
        bank_address= (TextView)itemView.findViewById(R.id.bank_address);
        holderItem=itemView;

    }
    public void bind( final MyCustomRecycleViewModel myCustomRecycleView){

        id_name.setText(myCustomRecycleView.getUserPayName());

        if (myCustomRecycleView.getAccountNumber().equals("")){
//            bank_account_no.setText("Account no :" +myCustomRecycleView.getAccountNumber());
            bank_account_no.setVisibility(View.GONE);
        }else if (myCustomRecycleView.getUserPayName().equals("Cash Deposit")){
            bank_account_no.setText("Account no : ");
        }else {
//            bank_account_no.setVisibility(View.GONE);
            bank_account_no.setText("Account no :" +myCustomRecycleView.getAccountNumber());
        }
        if (myCustomRecycleView.getAddress().equals("")){
            bank_address.setVisibility(View.GONE);
//            bank_address.setText("");
        }
        else if (myCustomRecycleView.getUserPayName().equals("Cash Deposit")){
//            bank_address.setVisibility(View.GONE);
            bank_address.setText("Branch : ");

        }else {
//            bank_address.setVisibility(View.GONE);
            bank_address.setText("Branch :" +myCustomRecycleView.getAddress());
        }
//        bank_account_no.setText("Account no :" +myCustomRecycleView.getAccountNumber());
//        bank_address.setText("Bank :" +myCustomRecycleView.getAddress());
        Picasso.with(holderItem.getContext())
                .load(myCustomRecycleView.getUserPayLogo())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        holderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("MyCustomModel",""+"MyCustomModelClicked");
                Context context = v.getContext();
                Bundle bundle=new Bundle();

                bundle.putString("userPayName", myCustomRecycleView.getUserPayName());
                bundle.putString("userPayLogo", myCustomRecycleView.getUserPayLogo());
                bundle.putString("payTypeIds", myCustomRecycleView.getPayTypeIds());
                bundle.putBundle("amountDetails", myCustomRecycleView.getBundle());

                Fragment fragment = new BankDepositFormFragment();
                FragmentManager fragmentManager = ((FragmentActivity) holderContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


    }
}
