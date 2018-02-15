package com.paybyonline.ebiz.Adapter.ViewHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paybyonline.ebiz.Adapter.Model.SendReceiveMoney;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.loopj.android.http.RequestParams;
//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

//import cz.msebera.android.httpclient.Header;

/**
 * Created by mefriend24 on 11/25/16.
 */
public class SendMoneyDetailsViewHolder extends RecyclerView.ViewHolder {

    private final RetrofitHelper retrofitHelper;
    View holderItems;

    TextView acceptedDate;
    TextView transactionNo;
    TextView sentDate;
    TextView expDate;
    TextView purposeName;
    TextView showSentTo;
    TextView walletSentAmount;
    TextView deductedWalletSentAmount;
    TextView walletCharge;
    TextView sentRemarks;
    TextView status;
    TextView pspSentAmount;
    TextView deductedPspSentAmount;
    TextView pspCharge;

    LinearLayout walletDetails;
    LinearLayout pspDetails;
    LinearLayout walletChargeDetails;
    LinearLayout pspChargeDetails;

    Button cancelBtn;

    Context context;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
    AlertDialog dialog;

    public SendMoneyDetailsViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        retrofitHelper = new RetrofitHelper();
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        }
        acceptedDate = (TextView) itemView.findViewById(R.id.acceptedDate);
        transactionNo = (TextView) itemView.findViewById(R.id.transactionNo);
        sentDate = (TextView) itemView.findViewById(R.id.sentDate);
        expDate = (TextView) itemView.findViewById(R.id.expDate);
        purposeName = (TextView) itemView.findViewById(R.id.purposeName);
        showSentTo = (TextView) itemView.findViewById(R.id.showSentTo);
        walletSentAmount = (TextView) itemView.findViewById(R.id.walletSentAmount);
        deductedWalletSentAmount = (TextView) itemView.findViewById(R.id.deductedWalletSentAmount);
        walletCharge = (TextView) itemView.findViewById(R.id.walletCharge);
        sentRemarks = (TextView) itemView.findViewById(R.id.sentRemarks);
        status = (TextView) itemView.findViewById(R.id.status);
        pspSentAmount = (TextView) itemView.findViewById(R.id.pspSentAmount);
        deductedPspSentAmount = (TextView) itemView.findViewById(R.id.deductedPspSentAmount);
        pspCharge = (TextView) itemView.findViewById(R.id.pspCharge);
        walletDetails = (LinearLayout) itemView.findViewById(R.id.walletDetails);
        pspDetails = (LinearLayout) itemView.findViewById(R.id.pspDetails);
        walletChargeDetails = (LinearLayout) itemView.findViewById(R.id.walletChargeDetails);
        pspChargeDetails = (LinearLayout) itemView.findViewById(R.id.pspChargeDetails);
        cancelBtn = (Button) itemView.findViewById(R.id.cancelBtn);
        myUserSessionManager = new MyUserSessionManager(context);
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(context);

        holderItems = itemView;
    }

    public void bind(final SendReceiveMoney sendReceiveMoney, CoordinatorLayout coordinatorLayout) {

        this.coordinatorLayout = coordinatorLayout;

        if (sendReceiveMoney.getCompletionDate().equals("Delete")) {
            acceptedDate.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else {
            acceptedDate.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
            acceptedDate.setText(sendReceiveMoney.getCompletionDate());
        }

        transactionNo.setText(sendReceiveMoney.getTransactionNo());
        sentDate.setText(sendReceiveMoney.getSentDate());
        expDate.setText(sendReceiveMoney.getExpDate());
        purposeName.setText(sendReceiveMoney.getPurposeName());
        showSentTo.setText(sendReceiveMoney.getShowSentTo());
        if (sendReceiveMoney.getIsWalletSentAmount().equals("YES")) {
            walletDetails.setVisibility(View.VISIBLE);
            walletSentAmount.setText(sendReceiveMoney.getWalletSentAmount());
            deductedWalletSentAmount.setText(sendReceiveMoney.getDeductedWalletSentAmount());
            if (sendReceiveMoney.getWalletCharge().length() > 0) {
                walletChargeDetails.setVisibility(View.VISIBLE);
                walletCharge.setText(sendReceiveMoney.getWalletCharge());
            } else {
                walletChargeDetails.setVisibility(View.GONE);
            }
        } else {
            walletDetails.setVisibility(View.GONE);
        }
        if (sendReceiveMoney.getIsPspSentAmount().equals("YES")) {
            pspDetails.setVisibility(View.VISIBLE);
            pspSentAmount.setText(sendReceiveMoney.getPspSentAmount());
            deductedPspSentAmount.setText(sendReceiveMoney.getDeductedPspSentAmount());
            if (sendReceiveMoney.getPspCharge().length() > 0) {
                pspCharge.setText(sendReceiveMoney.getPspCharge());
                pspChargeDetails.setVisibility(View.VISIBLE);
            } else {
                pspChargeDetails.setVisibility(View.GONE);
            }
        } else {
            pspDetails.setVisibility(View.GONE);
        }

        sentRemarks.setText(sendReceiveMoney.getSentRemarks());
        status.setText(sendReceiveMoney.getStatus());

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateSendMoneyDetails(sendReceiveMoney.getTransactionNo());
                showDeleteConfirmationDailog(sendReceiveMoney.getTransactionNo());
            }
        });

    }

    public void showDeleteConfirmationDailog(final String transactionNo) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setPositiveButton("CONTINUE", null)
                .setNegativeButton("CANCEL", null)
                .create();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.layout_confirmation_remark, null);
        final EditText confirmRemarks = (EditText) dialogView.findViewById(R.id.confirmRemarks);
        alertDialog.setView(dialogView);
        alertDialog.setTitle("CONFIRMATION");

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        updateSendMoneyDetails(transactionNo, confirmRemarks.getText().toString());
                    }
                });

                final Button btnDecline = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();

    }

    public void updateSendMoneyDetails(String transactionNumber, String confirmRemarks) {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "updateSendMoneyDetails");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("transactionNo", transactionNumber);
        params.put("receiveRemarks", confirmRemarks);
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,context);
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    showMyAlertProgressDialog.showLongToast(response.getString("msg"));
//                    if(response.getString("msgTitle").equals("Success")){
//                        acceptedDate.setVisibility(View.VISIBLE);
//                        cancelBtn.setVisibility(View.GONE);
//                        acceptedDate.setText(response.getString("cancelDate"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    showMyAlertProgressDialog.showLongToast(jsonObject.getString("msg"));
                    if (jsonObject.getString("msgTitle").equals("Success")) {
                        acceptedDate.setVisibility(View.VISIBLE);
                        cancelBtn.setVisibility(View.GONE);
                        acceptedDate.setText(jsonObject.getString("cancelDate"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Send Money View Holder", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }


}
