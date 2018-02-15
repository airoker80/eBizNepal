package com.paybyonline.ebiz.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.BankingModel;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Swatin on 9/8/2016.
 */
public class NetBankingFragment extends Fragment {

    private Bundle bundle;
    private List<BankingModel> bankingModelList;
    private RecyclerView recycler;
    private MyUserSessionManager myUserSessionManager;
    private RetrofitHelper retrofitHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_banking, container, false);
        bundle = getArguments();
        Log.i("bundleData", "online banking " + bundle);
        myUserSessionManager = new MyUserSessionManager(getActivity());
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        TextView amtVal = (TextView) view.findViewById(R.id.amtVal);
        amtVal.setText(bundle.getString("amtPayingVal"));

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        obtainUserPaymentType();

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public class NetBankingRecyclerAdapter extends RecyclerView.Adapter<NetBankingRecyclerAdapter.NetBankingRecyclerViewHolder> {

        private OnItemClickListener onItemClickListener;

        private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public NetBankingRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_image_text_row, parent, false);
            return new NetBankingRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NetBankingRecyclerViewHolder holder, int position) {
            Picasso.with(getContext())
                    .load(bankingModelList.get(position).getUserPayLogo())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.hbl)
                    .into(holder.bankImage);
            holder.bankName.setText(bankingModelList.get(position).getUserPayName());
        }

        @Override
        public int getItemCount() {
            return bankingModelList.size();
        }

        class NetBankingRecyclerViewHolder extends RecyclerView.ViewHolder {

            private ImageView bankImage;
            private TextView bankName;

            NetBankingRecyclerViewHolder(View itemView) {
                super(itemView);
                bankImage = (ImageView) itemView.findViewById(R.id.bankLogo);
                bankName = (TextView) itemView.findViewById(R.id.bankName);
                itemView.setOnClickListener(view -> onItemClickListener.OnItemClick(view, getAdapterPosition()));
            }
        }
    }

    public void obtainUserPaymentType() {
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "obtainUserPaymentType");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("payMethod", "Online Banking");

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainUserPaymentType(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("response:", jsonObject.toString());
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {

            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainUserPaymentType(JSONObject response) throws JSONException {

        Log.i("response:", "" + response);
        try {
            Log.i("response:", "" + response);

            JSONArray jsonArray = response.getJSONArray("dataList");
            bankingModelList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = (JSONObject) jsonArray.get(i);
                int id = obj.getInt("id");
                String userPayName = obj.get("userPayName").toString();
                String userPayLogo = PayByOnlineConfig.BASE_URL + "documents/" +
                        Uri.encode(obj.get("userPayLogo").toString());

                bankingModelList.add(new BankingModel(id, userPayName, userPayLogo));

            }

            final NetBankingRecyclerAdapter adapter = new NetBankingRecyclerAdapter();
            adapter.setOnItemClickListener((view, position) -> {
                Fragment fragment = new NetBankingConfirmFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, Constants.NET_BANKING_CONFIRM_FRAGMENT);
                bundle.putString("bankId", String.valueOf(bankingModelList.get(position).getId()));
                bundle.putString("bankLogo", bankingModelList.get(position).getUserPayLogo());
                bundle.putString("bankName", bankingModelList.get(position).getUserPayName());
                bundle.putString("from", "nb");
                fragment.setArguments(bundle);
                fragmentTransaction.commitNow();
            });
            recycler.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
