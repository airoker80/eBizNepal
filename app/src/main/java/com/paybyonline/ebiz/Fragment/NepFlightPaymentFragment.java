package com.paybyonline.ebiz.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * Created by Sameer on 7/6/2017.
 */

public class NepFlightPaymentFragment extends Fragment implements View.OnClickListener {
    private static String FILE = Environment.getExternalStorageDirectory()
            + "/Pdfdownloads/";
    CoordinatorLayout coordinatorLayout;
    MyUserSessionManager myUserSessionManager;
    EditText confirmationPinEditTxt;
    TextView flightDiscount, descriptionTxt, acountDesc, flightAmmount;
    Button payFlight;
    String commValue, accessToken, bookingId, purchasedValue, discount, merchantTypeName, netAmount,
            serviceTypeId, sCategoryId;
    private RetrofitHelper retrofitHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myUserSessionManager = new MyUserSessionManager(getContext());
        // Inflate the layout for this fragment
        getActivity().setTitle("Buy Product");
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_buy_nepflight_form, container, false);
        flightDiscount = (TextView) view.findViewById(R.id.flightDiscount);
        descriptionTxt = (TextView) view.findViewById(R.id.descriptionTxt);
        acountDesc = (TextView) view.findViewById(R.id.acountDesc);
        flightAmmount = (TextView) view.findViewById(R.id.flightAmmount);
        sCategoryId = bundle.getString("sCategoryId");
        serviceTypeId = bundle.getString("serviceTypeId");
        merchantTypeName = bundle.getString("merchantTypeName");
        commValue = bundle.getString("commValue");
        accessToken = bundle.getString("accessToken");
        netAmount = bundle.getString("netAmount");
        accessToken = bundle.getString("accessToken");
        discount = bundle.getString("discount");
        bookingId = bundle.getString("bookingId");
//        generatePdf("asdad \n awafdca");
        purchasedValue = bundle.getString("purchasedValue");
        flightAmmount.setText(netAmount);
        flightDiscount.setText(discount);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();
        payFlight = (Button) view.findViewById(R.id.payFlight);
        payFlight.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.dialog_pin_enter, null);
                confirmationPinEditTxt = (EditText) view.findViewById(R.id.confirmationPinEditTxt);
                final AlertDialog builder = new AlertDialog.Builder(getContext())
                        .setPositiveButton("OK", null)
                        .setNegativeButton("CANCEL", null)
                        .setView(view)
                        .setTitle("Enter Pin Code")
                        .create();
                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                paymentRequest();
                                //textView.setText(passengerModule.getPassengerName());
                                builder.dismiss();
                            }
                        });

                        final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                        btnDecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                            }
                        });
                    }
                });
                builder.show();
            }
        }


    }

    public void paymentRequest() {
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(getContext());
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,
//                getActivity());
//        final RequestParams sendParams = new RequestParams();
        Map<String, String> sendParams = new HashMap<>();
        sendParams.put("responseType", "JSON");
        sendParams.put("platformName", "Embedded by PBO");
        sendParams.put("purchasedValue", purchasedValue);
        sendParams.put("paidAmount", netAmount);
        sendParams.put("sCategory", sCategoryId);
        sendParams.put("serviceType", serviceTypeId);
        sendParams.put("webFrontFormNameId", "0");
        sendParams.put("dealName", "");
        sendParams.put("dynamicMerchantSave", "false");
        sendParams.put("parentTask", "rechargeApp");
        sendParams.put("bookingId", bookingId);
        sendParams.put("commValue", commValue);
        sendParams.put("accessToken", accessToken);
        // sendParams.put("childTask", "saveRecharge");
        sendParams.put("childTask", "performRecharge");
//        sendParams.put("childTask", "saveRecharge");
        sendParams.put("userCode", myUserSessionManager.getSecurityCode());
        sendParams.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        sendParams.put("confirmationPin", confirmationPinEditTxt.getText().toString());

/*        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "Processing...", sendParams,
                new PboServerRequestListener() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {

                            Log.i("ListresponseT", "response");
                            Log.i("params", ""+sendParams.toString());

                            handleSendFieldContentResponse(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("exp",e+"");

                        }

                    }
                });*/

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("ListresponseT", "response");
                    handleSendFieldContentResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exp", e + "");
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Nep Flight", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, sendParams);
    }

    public void handleSendFieldContentResponse(JSONObject response) throws JSONException {
        Log.v("responsePayment", response.toString());
        if (response.getString("msgStatus").equals("Success")) {
            makePDF();
        } else {
            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
        }
    }

    public void makePDF() {
//        RequestParams jsonObjectPost = new RequestParams();
        Map<String, String> jsonObjectPost = new HashMap<>();
        Log.d("bookingId", bookingId);
        jsonObjectPost.put("parentTask", "nepFlight");
        jsonObjectPost.put("userCode", myUserSessionManager.getSecurityCode());
        jsonObjectPost.put("childTask", "issueFinalTicket");
        jsonObjectPost.put("bookingId", bookingId);
        jsonObjectPost.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        jsonObjectPost.put("accessToken", accessToken);

       /* PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", jsonObjectPost, new PboServerRequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("authenticateUser", "" + response);
                try {
                    handleObtainPaymentDetails(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
            }
        });*/

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainPaymentDetails(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Favorite Services", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, jsonObjectPost, null, null);
    }

    private void handleObtainPaymentDetails(JSONObject response) throws JSONException {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.dialog_success, null);

        try {
            String size = response.getString("size");
            String imageOut = response.getString("imageOut");
            String imageIn = response.getString("imageIn");
            JSONObject serverResponse = response.getJSONObject("serverResponse");
            String bookingId = response.getString("bookingId");
            String coBrandImage = response.getString("coBrandImage");


            String IssueDate = serverResponse.getString("IssueDate");
            String TaxCurrency = serverResponse.getString("TaxCurrency");
            String Airline = serverResponse.getString("Airline");
            String Departure = serverResponse.getString("Departure");
            String Sector = serverResponse.getString("Sector");
            String Surcharge = serverResponse.getString("Surcharge");
            String PaxType = serverResponse.getString("PaxType");
            String FlightDate = serverResponse.getString("FlightDate");
            String TicketNo = serverResponse.getString("TicketNo");
            String FreeBaggage = serverResponse.getString("FreeBaggage");
            String FirstName = serverResponse.getString("FirstName");
            String BarcodeImage = serverResponse.getString("BarcodeImage");
            String Arrival = serverResponse.getString("Arrival");
            String CommissionAmount = serverResponse.getString("CommissionAmount");
            String FlightNo = serverResponse.getString("FlightNo");
            String ArrivalTime = serverResponse.getString("ArrivalTime");
            String Currency = serverResponse.getString("Currency");
            String Fare = serverResponse.getString("Fare");
            String Refundable = serverResponse.getString("Refundable");
            String FlightTime = serverResponse.getString("FlightTime");
            String Tax = serverResponse.getString("Tax");
            String Nationality = serverResponse.getString("Nationality");
            String ClassCode = serverResponse.getString("ClassCode");
            String PnrNo = serverResponse.getString("PnrNo");
            String Gender = serverResponse.getString("Gender");
            String BarCodeValue = serverResponse.getString("BarCodeValue");
            String ReportingTime = serverResponse.getString("ReportingTime");
            JSONObject PaxNo = serverResponse.getJSONObject("PaxNo");
            JSONObject LastName = serverResponse.getJSONObject("LastName");
            JSONObject PaxId = serverResponse.getJSONObject("PaxId");
            JSONObject Title = serverResponse.getJSONObject("Title");

            final int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (checkIfAlreadyhavePermission()) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Log.v("called1", "==>" + "called");
/*                    generatePdf("bookingId : " + bookingId + "\n"
                            + "IssueDate : " + IssueDate + "\n"
                            + "IssueDate : " + IssueDate + "\n"

                            + "TaxCurrency : " + TaxCurrency + "\n"
                            + "Airline : " + Airline + "\n"
                            + "Departure : " + Departure + "\n"
                            + "Sector : " + Sector + "\n"
                            + "Surcharge : " + Surcharge + "\n"
                            + "PaxType : " + PaxType + "\n"
                            + "FlightDate : " + FlightDate + "\n"
                            + "TicketNo : " + TicketNo + "\n"
                            + "FreeBaggage : " + FreeBaggage + "\n"
                            + "FirstName : " + FirstName + "\n"
                            + "BarcodeImage : " + BarcodeImage + "\n"
                            + "Arrival : " + Arrival + "\n"

                            + "CommissionAmount : " + CommissionAmount + "\n"
                            + "FlightNo : " + FlightNo + "\n"
                            + "ArrivalTime : " + ArrivalTime + "\n"
                            + "Currency : " + Currency + "\n"
                            + "Fare : " + Fare + "\n"
                            + "Refundable : " + Refundable + "\n"
                            + "FlightTime : " + FlightTime + "\n"
                            + "Tax : " + Tax + "\n"
                            + "Nationality : " + Nationality + "\n"
                            + "ClassCode : " + ClassCode + "\n"
                            + "PnrNo : " + PnrNo + "\n"
                            + "Gender : " + Gender + "\n"
                            + "BarCodeValue : " + BarCodeValue + "\n"
                            + "ReportingTime : " + ReportingTime + "\n"
                    );*/
                    Log.v("called2", "==>" + "called");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

/*        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setPositiveButton("OK", null)
//                .setNegativeButton("CANCEL", null)
                .setView(view)
                .setTitle("Success")
                .create();

        builder.show();*/
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), DashBoardActivity.class));
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        return result == PackageManager.PERMISSION_GRANTED;
        if (result == PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void stringtopdf(String data) {
        Log.e("start", "start");
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Pdfdownloads");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
//        String name = new Date().toString() + ".jpg";

        try {
            final File file = new File(myDir, "sample.pdf");
            file.setReadable(true);
            Log.d("fil9676867868e", file.toString());
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(10000, 200, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            canvas.drawText(data, 8, 8, paint);


            document.finishPage(page);
            document.writeTo(fOut);
            document.close();
            Log.d("success", "success" + "==0-0-");
            Toast.makeText(getContext(), "successfully saved in Pdfdownloads folder", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.i("EEEEEERRRRRRRRRRRR", e.getLocalizedMessage());
        }
    }

    /*public void generatePdf(String text) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Pdfdownloads");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        Log.v("dateNow", "---->" + formattedDate);
        String concate = FILE + formattedDate + "flight_ticket.pdf";
        Log.v("called", "==>" + "called");
        Document objDocument = new Document();
        objDocument.setCreator("DynamicPDFHelloWorld.java");
        objDocument.setAuthor("Your Name");
        objDocument.setTitle("Hello World");

        // Create a page to add to the document
        Page objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT,
                54.0f);

        // Create a Label to add to the page
//        String strText = "Hello World...\nFrom DynamicPDF Generator " + "for Java\nDynamicPDF.com";
        String strText = text;
        Label objLabel = new Label(strText, 0, 0, 5040, 1000,
                Font.getHelvetica(), 18, TextAlign.LEFT);

        // Add label to page
        objPage.getElements().add(objLabel);

        // Add page to document
        objDocument.getPages().add(objPage);

        Log.v("tesxxt", "==" + text);
        try {
            Log.v("pdfsuccess", concate);
            // Outputs the document to file
            objDocument.draw(concate);
            Toast.makeText(getContext(), "File has been written to :" + concate,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error, unable to write to file\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }*/
}

