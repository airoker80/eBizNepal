package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.TransactionReportList;
import com.paybyonline.ebiz.R;


/**
 * Created by Anish on 8/17/2016.
 */
public class TransactionReportViewHolder extends RecyclerView.ViewHolder {

    TextView transactionDate;
    TextView transactionNo;
    TextView discription;
    TextView payment;
    TextView paymentDeposit;
    TextView balance;
    LinearLayout payDepositLayout;
    LinearLayout paymentLayout;

    public TransactionReportViewHolder(View view) {
        super(view);

        balance = (TextView) view.findViewById(R.id.balance);
        transactionDate = (TextView) view.findViewById(R.id.transactionDate);
        transactionNo = (TextView) view.findViewById(R.id.transactionNo);
        payment = (TextView) view.findViewById(R.id.pay_amt);
        paymentDeposit = (TextView) view.findViewById(R.id.paymentDeposit);
        discription = (TextView) view.findViewById(R.id.msDis);
        payDepositLayout = (LinearLayout) view.findViewById(R.id.payDepositLayout);
        paymentLayout = (LinearLayout) view.findViewById(R.id.paymentLayout);

    }
    public void bind( final TransactionReportList transactionReportList){

        String deposit = Html.fromHtml(transactionReportList.getDeposit()).toString();
        if(deposit.length()>0){
            paymentLayout.setVisibility(View.GONE);
            paymentDeposit.setText(Html.fromHtml(transactionReportList.getCurrency()+" "+transactionReportList.getDeposit()));
        }else{
            payDepositLayout.setVisibility(View.GONE);
            payment.setText(Html.fromHtml(transactionReportList.getCurrency()+" "+ transactionReportList.getPayment()));
        }

        balance.setText(Html.fromHtml("Balance : "+transactionReportList.getCurrency()+" "+transactionReportList.getBalance()));
        transactionDate.setText(Html.fromHtml(transactionReportList.getTransactionDate()));
        transactionNo.setText(Html.fromHtml(transactionReportList.getTransactionNumber()));
        discription.setText(Html.fromHtml(transactionReportList.getDescription()));

    }
}