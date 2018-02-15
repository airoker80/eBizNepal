package com.paybyonline.ebiz.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paybyonline.ebiz.Activity.DashBoardActivity;
import com.paybyonline.ebiz.Offline.GenerateJsonData;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileUserInfoFragment extends Fragment {

    UserDeviceDetails userDeviceDetails;
    MyUserSessionManager myUserSessionManager;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    CoordinatorLayout coordinatorLayout;
//    PboServerRequestHandler handler;

    ImageView profileImage;
    TextView name;
    TextView emailAddress;
    TextView address;
    TextView mobileNo;
    TextView gender;
    TextView userTimeZone;
    TextView dateOfBirth;
    TextView editUserProfile;

    ImageView updateProfileImage;
    private static int RESULT_LOAD_IMG = 12;
    ProgressDialog prgDialog;
    Bitmap bitmap;
    String encodedString;
    Bitmap updatedImageBitmap;
    String imgPath = "";
    String fileName = "";

    Spinner timezoneSpinner;
    String userCurrentTimezone;
    Button dobBtn;
    Button setSelectedDateBtn;

    private int year;
    private int month;
    private int day;

    EditText phoneNoUpdate;
    private RadioGroup radioSexGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private View updateFormView;
    private RetrofitHelper retrofitHelper;

    public ProfileUserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user_info, container, false);
        //(DashBoardActivity).setT
        ((DashBoardActivity) getActivity()).setTitle("User Profile");
        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        name = (TextView) view.findViewById(R.id.name);
        emailAddress = (TextView) view.findViewById(R.id.emailAddress);
        address = (TextView) view.findViewById(R.id.address);
        editUserProfile = (TextView) view.findViewById(R.id.editUserProfile);
        mobileNo = (TextView) view.findViewById(R.id.mobileNo);
        gender = (TextView) view.findViewById(R.id.gender);
        userTimeZone = (TextView) view.findViewById(R.id.userTimeZone);
        dateOfBirth = (TextView) view.findViewById(R.id.dateOfBirth);
        updateProfileImage = (ImageView) view.findViewById(R.id.updateProfileImage);

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        updateProfileImage.setOnClickListener(view12 -> showProfileImageChooseOption());

        obtainUserProfileInfo();
        return view;
    }

    public void updateUserProfileForm() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("UPDATE", null)
                .setNegativeButton("CANCEL", null)
                .create();

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.layout_profile_user_info_update, null);
        timezoneSpinner = (Spinner) dialogView.findViewById(R.id.timezoneSpinner);
        phoneNoUpdate = (EditText) dialogView.findViewById(R.id.phoneNoUpdate);
        radioFemale = (RadioButton) dialogView.findViewById(R.id.radioFemale);
        radioMale = (RadioButton) dialogView.findViewById(R.id.radioMale);
        dobBtn = (Button) dialogView.findViewById(R.id.dobBtn);
        radioSexGroup = (RadioGroup) dialogView.findViewById(R.id.radioSex);
        phoneNoUpdate.setText(mobileNo.getText().toString());

        if (gender.getText().toString().equals("Male")) {
            radioMale.setChecked(true);
        }
        if (gender.getText().toString().equals("Female")) {
            radioFemale.setChecked(true);
        }

        setCurrentDateOnView(dobBtn);

        dobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(dobBtn);
            }
        });

        getUserTimezone();

        alertDialog.setView(dialogView);
        alertDialog.setTitle("Update User Profile");

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        updateUserProfileInfo();
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


        updateFormView = dialogView;
        alertDialog.show();
    }

    public void updateUserProfileInfo() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "updateUserProfileInfo");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("mobileNumbers", phoneNoUpdate.getText().toString());


        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) updateFormView.findViewById(selectedId);

        params.put("genderVals", radioSexButton.getText().toString());
        params.put("userDobs", dobBtn.getText().toString());
        params.put("timeZ", timezoneSpinner.getSelectedItem().toString());
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout,getActivity());
//
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    userDeviceDetails.showToast(response.getString("msg"));
//                    if (response.getString("msgTitle").equals("Success")){
//                        obtainUserProfileInfo();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    userDeviceDetails.showToast(jsonObject.getString("msg"));
                    if (jsonObject.getString("msgTitle").equals("Success")) {
                        obtainUserProfileInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile User:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }


    private void showDatePicker(Button fromToBtn) {
        DatePickerAllFragment date = new DatePickerAllFragment();
        setSelectedDateBtn = fromToBtn;
        String[] selDate = fromToBtn.getText().toString().split("-");
        Bundle args = new Bundle();
        args.putInt("year", Integer.parseInt(selDate[0]));
        args.putInt("month", Integer.parseInt(selDate[1]) - 1);
        args.putInt("day", Integer.parseInt(selDate[2]));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            setSelectedDateBtn.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };

    public void setCurrentDateOnView(Button fromToBtn) {

        final Calendar c = Calendar.getInstance();
        String dob = dateOfBirth.getText().toString();
        if (dob.length() > 0) {
            String[] dobArray = dob.split("-");
            year = Integer.parseInt(dobArray[0].trim());
            month = Integer.parseInt(dobArray[1].trim()) - 1;
            day = Integer.parseInt(dobArray[2].trim());
        } else {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }


        fromToBtn.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("-").append(month + 1).append("-")
                .append(day));


    }


    public void getUserTimezone() {

        String data = GenerateJsonData.getJsonData(getActivity(), R.raw.user_timezone_list);

        try {
            JSONArray timezoneArray = new JSONArray(data);

            if (timezoneArray.length() > 0) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < timezoneArray.length(); i++) {
                    String timezone = timezoneArray.getString(i);
                    list.add(timezone);
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                timezoneSpinner.setAdapter(dataAdapter);

                if (!userCurrentTimezone.equals(null)) {
                    if (userCurrentTimezone.equals("Asia/Katmandu")) {
                        userCurrentTimezone = "Asia/Kathmandu";
                    }
                    int spinnerPosition = dataAdapter.getPosition(userCurrentTimezone);
                    timezoneSpinner.setSelection(spinnerPosition);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showProfileImageChooseOption() {
        loadImageFromGallery();
    }

    public void loadImageFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ProfileUserInfoFragment.this.startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
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
                updateUserProfileImage();
            }
        }.execute(null, null, null);
    }


    public void updateUserProfileImage() {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "updateUserProfileImage");
        params.put("profileImageEncodedData", encodedString);
        params.put("profileImageName", fileName);
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());

        Log.i("msgsss", "params " + params);

/*        handler.makePostRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                try {
                    Log.i("msgssss", "response " + response);
                    userDeviceDetails.showToast(response.getString("msg"));
                    if (response.getString("msgTitle").equals("Success")) {
                        profileImage.setImageBitmap(updatedImageBitmap);
                        DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
                        dashBoardActivity.updateUserPhoto(response.getString("userPhoto"));
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
                    Log.i("msgssss", "response " + jsonObject.toString());
                    userDeviceDetails.showToast(jsonObject.getString("msg"));
                    if (jsonObject.getString("msgTitle").equals("Success")) {
                        profileImage.setImageBitmap(updatedImageBitmap);
                        DashBoardActivity dashBoardActivity = (DashBoardActivity) getActivity();
                        dashBoardActivity.updateUserPhoto(jsonObject.getString("userPhoto"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile User:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.POST_ON_AUTH_APP_USER_ENDPOINT, null, null, params);
    }

    public void obtainUserProfileInfo() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getProfileUserInfo");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
//        handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
//
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleObtainUserProfileInfoResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleObtainUserProfileInfoResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile User:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    //    public void handleObtainUserProfileInfoResponse() {
    public void handleObtainUserProfileInfoResponse(JSONObject response) throws JSONException {

//        String jsonData= GenerateJsonData.getJsonData(getContext(), R.raw.user_info);

        try {

            JSONObject dataSource = response;
            Log.i("data", "" + dataSource);
            JSONObject data = dataSource.getJSONObject("data");
            if (dataSource.getString("msgTitle").equals("Success")) {
                // JSONObject data = response.getJSONObject("data");

                Picasso.with(getActivity())
                        .load(data.getString("userPhoto"))
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.noimg)
                        .into(profileImage);

                name.setText(data.getString("name"));
                emailAddress.setText(data.getString("emailAddress"));
                address.setText(data.getString("address"));
                mobileNo.setText(data.getString("mobileNo"));
                gender.setText(data.getString("gender"));
                userTimeZone.setText(data.getString("userTimeZone"));
                userCurrentTimezone = data.getString("userTimeZone");

                dateOfBirth.setText(data.getString("dateOfBirth"));

                editUserProfile.setOnClickListener(view1 -> updateUserProfileForm());

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
