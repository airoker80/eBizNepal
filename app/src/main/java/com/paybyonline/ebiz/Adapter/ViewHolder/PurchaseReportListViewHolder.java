package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.PurchaseReportList;
import com.paybyonline.ebiz.R;

/**
 * Created by Anish on 8/17/2016.
 */
public class PurchaseReportListViewHolder  extends RecyclerView.ViewHolder  {

    TextView id_type;
    TextView id_amt;
    TextView id_number;
    TextView id_tranDate;
    TextView id_tranNo;
    TextView id_pur_amt;
    TextView msDis;
    TextView id_netCostAmt;
    TextView id_reward;
    TextView dealPromo;
    TextView paidAmt;
    TextView id_remark;

    public PurchaseReportListViewHolder(View view) {
        super(view);

        id_type = (TextView)view.findViewById(R.id.id_type);
        id_amt = (TextView)view.findViewById(R.id.id_amt);
        id_tranDate = (TextView)view.findViewById(R.id.id_tranDate);
        id_tranNo = (TextView)view.findViewById(R.id.id_tranNo);
        id_number = (TextView)view.findViewById(R.id.id_number);
        id_pur_amt = (TextView)view.findViewById(R.id.id_pur_amt);
        msDis = (TextView)view.findViewById(R.id.msDis);
        id_netCostAmt = (TextView)view.findViewById(R.id.id_netCostAmt);
        id_reward = (TextView)view.findViewById(R.id.id_reward);
        id_remark = (TextView)view.findViewById(R.id.id_remark);
        dealPromo = (TextView)view.findViewById(R.id.id_dealPromo);

    }

    public void bind( final PurchaseReportList purchaseReportList){

        id_type.setText(Html.fromHtml(purchaseReportList.getServiceCat()+"<br>"+purchaseReportList.getType()));
        id_tranNo.setText(Html.fromHtml(purchaseReportList.getTransactionNo()));

        if(purchaseReportList.getReward().length()>0){

            id_reward.setText(Html.fromHtml(purchaseReportList.getReward()));

        }
        else{

            id_reward.setVisibility(View.GONE);

        }

        id_tranDate.setText(Html.fromHtml(purchaseReportList.getTransactionDate()));

        if(purchaseReportList.getDiscount().length()>0){

            msDis.setText(Html.fromHtml(purchaseReportList.getDiscount()));

        }else{
            msDis.setText(Html.fromHtml(purchaseReportList.getCommission()));
        }

        if(purchaseReportList.getDealAmount().length()>0){

            dealPromo.setText(Html.fromHtml(purchaseReportList.getDealAmount()));
        }
        else{

            dealPromo.setVisibility(View.GONE);
        }

        id_pur_amt.setText(Html.fromHtml(purchaseReportList.getPurchasedAmt()));
        id_amt.setText(Html.fromHtml(purchaseReportList.getToPay()));
//        id_number.setText(Html.fromHtml(purchaseReportList.getToPay()));
        id_netCostAmt.setText(Html.fromHtml(purchaseReportList.getPaidAmt()));
        id_remark.setText(Html.fromHtml(purchaseReportList.getRemark()));

        Log.i("purchaseReportList", "" + purchaseReportList);
//        type.setText("ServiceCat: ");

       /* id_type.setText(Html
                .fromHtml((purchaseReportList.getServiceCat() + ": " + purchaseReportList.getType())));

        id_amt.setText(Html
                .fromHtml(purchaseReportList.getPurchasedAmt()));

        id_number.setText(Html
                .fromHtml(purchaseReportList.getTransactionNo()));

        id_tranDate.setText(Html
                .fromHtml(purchaseReportList.getTransactionDate()));

        id_tranNo.setText(Html
                .fromHtml(purchaseReportList.getTransactionNo()));

        id_pur_amt.setText(Html
                .fromHtml(purchaseReportList.getPurchasedAmt()));

        msDis.setText(Html
                .fromHtml(purchaseReportList.getDiscount()));

        id_netCostAmt.setText(Html
                .fromHtml(purchaseReportList.getDealAmount()));*/
      /*  {
            viewHolder.type.setText(Html.fromHtml(purchaseReportList.getType()));
            viewHolder.transactionNo.setText(Html.fromHtml(purchaseReportList.getTransactionNo()));


            if(purchaseReportList.getReward().length()>0){
                viewHolder.reward.setText(Html.fromHtml("<b>Reward:</b>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+purchaseReportList.getReward()));}
            else{
                viewHolder.reward.setVisibility(view.GONE);
            }

            viewHolder.transactionDate.setText(Html.fromHtml("<b>Transaction Date:</b>"+"&nbsp;
            &nbsp;&nbsp;&nbsp;"+purchaseReportList.getTransactionDate()));

            viewHolder.seviceHolder.setText("SERVICE CATEGORY");
            viewHolder.serviceCat.setText(Html.fromHtml(" <b>Name:</b>"+"&nbsp;&nbsp;"+
            purchaseReportList.getServiceCat()));
            viewHolder.purchasedAmt.setText(Html.fromHtml("<b>Value: </b>"+"&nbsp;&nbsp;
            "+purchaseReportList.getPurchasedAmt()));
            viewHolder.toPay.setText(Html.fromHtml("<b>Purchase Amount: </b>"+"&nbsp;
            &nbsp;&nbsp;&nbsp;"+purchaseReportList.getToPay()));

            //viewHolder.MerchantHolder.setText("MERCHANT SERVICE");
            if(purchaseReportList.getDiscount().toString().length()>0){
                viewHolder.discount.setText(Html.fromHtml("<b>Merchant Service(MS) Discount: </b>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;"+purchaseReportList.getDiscount()));
            }else{
                viewHolder.discount.setText(Html.fromHtml("<b>Merchant Service(MS) S
                ervice Charge:</b>"+"&nbsp;&nbsp;&nbsp;&nbsp;" + purchaseReportList.getCommission()));}
            if(purchaseReportList.getDealAmount().toString().length()>0){
                viewHolder.dealPromo.setText(Html.fromHtml("<b>Deal/Promo Discount:</b>"+
                        "&nbsp;&nbsp;&nbsp;&nbsp;"+purchaseReportList.getDealAmount()));}
            else{viewHolder.dealPromo.setVisibility(View.GONE);}

            viewHolder.paidAmt.setText(Html.fromHtml("<b>Net Cost Amount:</b>"+
                    "&nbsp;&nbsp;&nbsp;&nbsp;"+purchaseReportList.getPaidAmt()));

            viewHolder.remark.setText(Html.fromHtml("<b>Remarks:</b>"+
                    "&nbsp;&nbsp;&nbsp;&nbsp;"+purchaseReportList.getRemark()));

        }*/

    }

}
