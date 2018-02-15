package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.TransactionReportList;
import com.paybyonline.ebiz.Adapter.ViewHolder.TransactionReportViewHolder;
import com.paybyonline.ebiz.R;


import java.util.Collections;
import java.util.List;

/**
 * Created by Anish on 8/17/2016.
 */
public class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportViewHolder>  {

    Context context;
    List<TransactionReportList> list = Collections.emptyList();
    LayoutInflater inflater;

    public TransactionReportAdapter(Context context,
                                      List<TransactionReportList> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public TransactionReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.transaction_report_list, parent, false);
        return new TransactionReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionReportViewHolder holder, int position) {
        final TransactionReportList model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
