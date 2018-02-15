package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.MyOwnTags;
import com.paybyonline.ebiz.Adapter.ViewHolder.MyOwnTagsViewHolder;
import com.paybyonline.ebiz.R;

import java.util.Collections;
import java.util.List;

public class MyTagListAdapter  extends RecyclerView.Adapter<MyOwnTagsViewHolder>  {

	Context context;
	List<MyOwnTags> list = Collections.emptyList();
	LayoutInflater inflater;


	public MyTagListAdapter(Context context, List<MyOwnTags> list) {

			this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public MyOwnTagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = inflater.inflate(R.layout.my_tag_list, parent, false);
		return new MyOwnTagsViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyOwnTagsViewHolder holder, int position) {
		final MyOwnTags model = list.get(position);
		holder.bind(model,position,getItemCount());
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

}
