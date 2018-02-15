package com.paybyonline.ebiz.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileAccountInfoFragment extends Fragment {

    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
//    PboServerRequestHandler handler;

    ImageView profileImage;
    TextView name;
    TextView accountId;
    TextView accountName;
    TextView accountRole;
    TextView category;
    TextView tier;
    TextView registeredLocation;
    TextView registeredCountry;
    TextView accountType;
    TextView accountDescription;

    ImageView updateProfileImage;
    private static int RESULT_LOAD_IMG = 13;
    ProgressDialog prgDialog;
    Bitmap bitmap;
    String encodedString;
    Bitmap updatedImageBitmap;
    String imgPath = "";
    String fileName = "";
    private RetrofitHelper retrofitHelper;


    public ProfileAccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_account_info, container, false);

        ((DashBoardActivity) getActivity()).setTitle("Account Profile");

        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        name = (TextView) view.findViewById(R.id.name);
        accountId = (TextView) view.findViewById(R.id.accountId);
        accountName = (TextView) view.findViewById(R.id.accountName);
        accountRole = (TextView) view.findViewById(R.id.accountRole);
        category = (TextView) view.findViewById(R.id.category);
        tier = (TextView) view.findViewById(R.id.tier);
        registeredLocation = (TextView) view.findViewById(R.id.registeredLocation);
        registeredCountry = (TextView) view.findViewById(R.id.registeredCountry);
        accountType = (TextView) view.findViewById(R.id.accountType);
        accountDescription = (TextView) view.findViewById(R.id.accountDescription);

        updateProfileImage = (ImageView) view.findViewById(R.id.updateProfileImage);

        updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileImageChooseOption();
            }
        });

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        obtainUserAccountInfo();

        return view;
    }

    public void showProfileImageChooseOption() {
        loadImageFromGallery();
    }

    public void loadImageFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ProfileAccountInfoFragment.this.startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK
                && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgPath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imgView = profileImage;
            imgView.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            int height = bitmap.getHeight(), width = bitmap.getWidth();

            if (height > 1280 && width > 960) {
                Bitmap imgbitmap = BitmapFactory.decodeFile(imgPath, options);
                updatedImageBitmap = bitmap;
//                imgView.setImageBitmap(imgbitmap);
                System.out.println("Need to resize");
            } else {
//                imgView.setImageBitmap(bitmap);
                updatedImageBitmap = bitmap;
                System.out.println("WORKS");
            }

            String fileNameSegments[] = imgPath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];

            Log.i("msg ", "fileName : " + fileName + " imgPath : " + imgPath);

            encodeImagetoString();


        } else {
            Toast.makeText(getContext(), "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }


    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
                prgDialog = new ProgressDialog(getActivity());
                prgDialog.setMessage("Processing Profile Picture. Please Wait...");
                prgDialog.show();
            }

            ;

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
//				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.hide();
                updateAccountProfileImage();
            }
        }.execute(null, null, null);
    }


    public void updateAccountProfileImage() {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "updateAccountProfileImage");
        params.put("profileImageEncodedData", encodedString);
        params.put("profileImageName", fileName);
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());

/*        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    Log.i("msgssss","response "+response);
                    userDeviceDetails.showToast(response.getString("msg"));
                    if(response.getString("msgTitle").equals("Success")){
                        profileImage.setImageBitmap(updatedImageBitmap);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("msgssss", "response " + jsonObject);
                    userDeviceDetails.showToast(jsonObject.getString("msg"));
                    if (jsonObject.getString("msgTitle").equals("Success")) {
                        profileImage.setImageBitmap(updatedImageBitmap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile Account:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, params);
    }

    public void obtainUserAccountInfo() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getProfileAccountInfo");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());

     /*   handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    handleObtainUserAccountInfoResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainUserAccountInfoResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile Account:", "error" + errorMessage);
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void handleObtainUserAccountInfoResponse(JSONObject response) throws JSONException {

        try {

            if (response.getString("msgTitle").equals("Success")) {
                JSONObject data = response.getJSONObject("data");

                Picasso.with(getActivity())
                        .load(data.getString("accountProfileImage"))
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.noimg)
                        .into(profileImage);

                name.setText(data.getString("accountName"));
                accountId.setText(data.getString("accountId"));
                accountName.setText(data.getString("accountName"));
                accountRole.setText(data.getString("accountRole"));
                category.setText(data.getString("category"));
                registeredLocation.setText(data.getString("registeredLocation"));
                tier.setText(data.getString("tier"));
                registeredCountry.setText(data.getString("registeredCountry"));
                accountType.setText(data.getString("accountType"));
                accountDescription.setText(data.getString("accountDescription"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
