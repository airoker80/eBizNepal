package com.paybyonline.ebiz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paybyonline.ebiz.Adapter.Model.BuyPageModel;
import com.paybyonline.ebiz.Adapter.ViewHolder.BuyPageViewHolder;
import com.paybyonline.ebiz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anish on 4/24/2016.
 */
public class BuyPageAdapter extends RecyclerView.Adapter<BuyPageViewHolder> {

    private LayoutInflater mInflater;
    private List<BuyPageModel> productList;
    Context context;


    public BuyPageAdapter(Context context, List<BuyPageModel> productList) {

        this.context = context;

        this.mInflater = LayoutInflater.from(context);
        this.productList = new ArrayList<>(productList);
        Log.i("productList",""+productList.size());

    }

    @Override
    public BuyPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.product_list, parent, false);
        return new BuyPageViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(BuyPageViewHolder holder, final int position) {


        final BuyPageModel model = productList.get(position);
        holder.bind(model, context,holder);

      /*  final BuyPageModel model = productList.get(position);
        holder.bind(model);*/
/*      holder.holdItemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

             // Log.i("BuyProductForm", "" + holder);
              Fragment fragment = new BuyProductFormFragment();
              FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              fragmentTransaction.replace(R.id.content_frame, fragment);
              Bundle bundle = new Bundle();
              bundle.putString("scstId", model.getScstId());
              fragmentTransaction.addToBackStack(null);
              fragment.setArguments(bundle);
              fragmentTransaction.commit();

          }
      });*/

    }

    @Override
    public int getItemCount() {

        return productList.size();
    }



    public BuyPageModel removeItem(int position) {
        BuyPageModel model = productList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, BuyPageModel model) {
        productList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        BuyPageModel model = productList.remove(fromPosition);
        productList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
