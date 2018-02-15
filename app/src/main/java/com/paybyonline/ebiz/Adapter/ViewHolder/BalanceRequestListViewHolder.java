package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.BalanceRequest;
import com.paybyonline.ebiz.R;

/**
 * Created by Anish on 8/15/2016.
 */
public class BalanceRequestListViewHolder extends RecyclerView.ViewHolder  {



    TextView payStatus;
    TextView transactionNo;
    TextView paidUsing;
    TextView requestedDate;
    TextView purchaseAmt;
    TextView flatPercent;
    TextView wdDisCom;
    TextView totalAmt;
    TextView disPercent;
    TextView ptDisCom;
    TextView paidAmount;
    public  View holderItem;

    public BalanceRequestListViewHolder(View view) {
        super(view);


        holderItem=view;

          payStatus = (TextView) view.findViewById(R.id.payStatus);
          transactionNo = (TextView) view
                    .findViewById(R.id.transactionNo);
           paidUsing = (TextView) view.findViewById(R.id.paidUsing);

            requestedDate = (TextView) view
                    .findViewById(R.id.transactionDate);
          purchaseAmt = (TextView) view
                    .findViewById(R.id.purchaseAmt);

           flatPercent = (TextView) view.findViewById(R.id.flatPercent);

         wdDisCom = (TextView) view.findViewById(R.id.wdDisCom);

          totalAmt = (TextView) view.findViewById(R.id.netWDVal);

           disPercent = (TextView) view.findViewById(R.id.disPercent);
            ptDisCom = (TextView) view.findViewById(R.id.ptDisCom);
           paidAmount = (TextView) view.findViewById(R.id.paidAmount);


    }
    public void bind(final BalanceRequest balanceRequest){

        Log.i("BalanceRequest","BalanceRequest"+balanceRequest);
      payStatus.setText(balanceRequest.getPayStatus());
        transactionNo.setText(balanceRequest.getTransactionNo());
         paidUsing.setText(balanceRequest
                .getPaidUsing());

        requestedDate.setText(balanceRequest.getRequestedDate());
      purchaseAmt.setText( balanceRequest.getPurchasedAmount());


        if(balanceRequest.getflatPercent().length()>0){
          flatPercent
                    .setText( balanceRequest.getflatPercent());}
        if(balanceRequest.getflatDiscount().length()>0){
            wdDisCom
                    .setText( balanceRequest.getflatDiscount());
        }else{

          wdDisCom
                    .setText(balanceRequest.getflatAddition());
        }



        totalAmt.setText(balanceRequest.gettotalAmt());


        if(balanceRequest.getDiscountAmount().length()>0){
           ptDisCom
                    .setText( balanceRequest.getDiscountAmount());
        }
        else{
           ptDisCom
                    .setText( balanceRequest.getAdditionAmount());
        }
       disPercent
                .setText(balanceRequest.getDiscountPercent());
       paidAmount.setText( balanceRequest.getPaidAmount());
    }
}
