package com.paybyonline.ebiz.Fragment;

import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.RequestParams;

//import com.paybyonline.ebiz.serverdata.PboServerRequestHandler;
//import com.paybyonline.ebiz.serverdata.PboServerRequestListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ReportLineChartFragment extends Fragment implements OnChartValueSelectedListener, OnClickListener {

    private LineChart mChart;
    private MyUserSessionManager myUserSessionManager;
    private UserDeviceDetails userDeviceDetails;
    private ShowMyAlertProgressDialog showMyAlertProgressDialog;
    private HashMap<String, String> userDetails;

    //    private static String url;
    //	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
//	int[] mColors = ColorTemplate.JOYFUL_COLORS;
//	int[] mColors = ColorTemplate.LIBERTY_COLORS;
    int[] mColors = ColorTemplate.COLORFUL_COLORS;
//	int[] mColors = ColorTemplate.PASTEL_COLORS;


    int dataCount;
    String categoryName;
    String[] categoryAmount;
    Spinner searchSpinner;
    //	DatePicker myDatePicker;
    Button dateFrom;
    Button dateTo;
    Button searchDataBetweenDate;
    //	TextView dateFromText;
//	TextView dateToText;
    LinearLayout dateSearchForm;
    String searchParameter = "";

    private int year;
    private int month;
    private int day;
    //	TextView setSelectedDate;
    Button setSelectedDateBtn;

    Boolean donotCallSpinner = true;
    CoordinatorLayout coordinatorLayout;
    private RetrofitHelper retrofitHelper;

    public ReportLineChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_line_chart, container,
                false);

//		getActivity().setTitle("Report");
        donotCallSpinner = true;

        retrofitHelper = new RetrofitHelper();
        getChildFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        userDeviceDetails = new UserDeviceDetails(getActivity());
        myUserSessionManager = new MyUserSessionManager(getActivity());
        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getActivity());
        userDetails = myUserSessionManager.getUserDetails();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
//        url = PayByOnlineConfig.SERVER_URL;

        mChart = (LineChart) view.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        //  mChart.setDrawYValues(false);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");


        dateSearchForm = (LinearLayout) view.findViewById(R.id.dateSearchForm);
        dateFrom = (Button) view.findViewById(R.id.dateFrom);
        dateTo = (Button) view.findViewById(R.id.dateTo);
        dateSearchForm.setVisibility(View.GONE);
        searchDataBetweenDate = (Button) view.findViewById(R.id.searchDataBetweenDate);
        dateFrom.setOnClickListener(this);
        dateTo.setOnClickListener(this);
        searchDataBetweenDate.setOnClickListener(this);
//		dateFromText = (TextView) view.findViewById(R.id.dateFromText);
//		dateToText = (TextView) view.findViewById(R.id.dateToText);

        setCurrentDateOnView(dateFrom);
        setCurrentDateOnView(dateTo);

        //setCurrentDateOnView();
        searchSpinner = (Spinner) view.findViewById(R.id.pieChartSpinner);
        searchSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int pos, long id) {
                // TODO Auto-generated method stub
//				userDeviceDetails.showToast(parent.getItemAtPosition(pos).toString());
//				String selectedItem = parent.getItemAtPosition(pos).toString();

                if (donotCallSpinner) {
                    donotCallSpinner = false;

                } else {
                    switch (pos) {
                        case 0:
                            searchParameter = "last7";
                            dateSearchForm.setVisibility(View.GONE);
                            connectWithServer();
                            break;
                        case 1:
                            searchParameter = "last30";
                            dateSearchForm.setVisibility(View.GONE);
                            connectWithServer();
                            break;
                        default:
                            searchParameter = "dateBetween";
                            dateSearchForm.setVisibility(View.VISIBLE);
                            break;
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //default 7 days
        searchParameter = "last7";
        connectWithServer();

        return view;
    }

    public void setCurrentDateOnView(Button fromToBtn) {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

		/*// set current date into textview
        textView.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(year).append("-").append(month+1).append("-")
				.append(day));*/

        fromToBtn.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("-").append(month + 1).append("-")
                .append(day));


    }

    private void connectWithServer() {
        if (!userDeviceDetails.isConnected()) {
            userDeviceDetails
                    .showToast("Please Check your Internet Connection");
            mChart.clear();
        } else {

            //new HttpAsyncTaskGetLineChartData().execute(url + "/getLineChartData");
            HttpAsyncTaskGetLineChartData();

        }
    }

    private void showDatePicker(Button fromToBtn) {
        //setSelectedDate = fromTo;
        setSelectedDateBtn = fromToBtn;
        DatePickerAllFragment date = new DatePickerAllFragment();
        /**
         * Set Up Current Date Into dialog
         */
        String[] selDate = fromToBtn.getText().toString().split("-");
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
         /* args.putInt("year", calender.get(Calendar.YEAR));
          args.putInt("month", calender.get(Calendar.MONTH));
		  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));*/
        args.putInt("year", Integer.parseInt(selDate[0]));
        args.putInt("month", Integer.parseInt(selDate[1]) - 1);
        args.putInt("day", Integer.parseInt(selDate[2]));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
    }

    OnDateSetListener ondate = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

		   /*setSelectedDate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                   + "-" + String.valueOf(dayOfMonth));*/
            setSelectedDateBtn.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };


    // ///////////////////////////////////////////////////////////////////////////
/*	private class HttpAsyncTaskGetLineChartData extends
            AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			*//*showMyAlertProgressDialog.showProgressDialogCancelable("Processing...",
					"Please wait.");*//*
			showMyAlertProgressDialog.setRefreshActionButtonState(true, ((MainActivity)getActivity()).optionsMenuRefresh);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {
			MyServerConnector myServerConnector = new MyServerConnector();
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("username", userDetails
					.get(MyUserSessionManager.KEY_USERNAME).toString()));
			urlParameters.add(new BasicNameValuePair("criteria", searchParameter));

			try {
				return myServerConnector.postToRechargeServer(urls[0],
						urlParameters);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSONObject res = new JSONObject();
				try {
					res.put("connectionStatus", "failed");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return res.toString();
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			JSONObject json;
			String connectionStatus;
			json = new JSONObject();
			try {

				json = new JSONObject(result);
				Log.i("json ",json+"");
				connectionStatus = json.getString("connectionStatus");
				//showMyAlertProgressDialog.hideProgressDialog();
				showMyAlertProgressDialog.setRefreshActionButtonState(false, ((MainActivity)getActivity()).optionsMenuRefresh);
				if (connectionStatus.equals("failed")
						|| (!connectionStatus.equals("200"))) {
					Log.i("Conn", connectionStatus);
					userDeviceDetails
							.showToast("Some Error Occurred. Please try again later !!!");

				} else {
					JSONArray dataList = json.getJSONArray("dataList");
					 Boolean dataPresent = false;
					 Log.i("dataList ",dataList+"");
					 Log.i("dataList.length() ",dataList.length()+"");

					 if(dataList.length()>=0){
						 JSONObject obs = dataList.getJSONObject(0);
								dataCount = obs.get("data").toString().split(",").length;
								addEmptyData();
							    mChart.invalidate();
					 }

					for (int i = 0; i < dataList.length(); i++) {

						JSONObject obs = dataList.getJSONObject(i);
						*//*if(i==0){
							Log.i("aaaaaaaaaaaa", "aaaaaaaaaaaaaa "+i);
							dataCount = obs.get("data").toString().split(",").length;
							addEmptyData();
						    mChart.invalidate();
						}else{*//*
							dataPresent = true;
							categoryAmount = new String[dataCount];
							categoryName = obs.get("name").toString();
							String catAmt = obs.get("data").toString();
							catAmt = catAmt.substring(1, catAmt.length()-1);
							String[] list = new String[dataCount];
							list = catAmt.split(",");

							for (int j = 0; j < list.length; j++) {
								categoryAmount[j] = list[j].toString();
							}
						     addDataSet(categoryName,categoryAmount);

						//}

					}
					Log.i("dataPresent", dataPresent+"");
					if(!dataPresent){
						mChart.clear();
					}


				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}*/
    public void HttpAsyncTaskGetLineChartData() {

//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getLineChartData");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("criteria", searchParameter);

        // Make Http call
//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout, getActivity());
////		handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    HttpAsyncTaskGetLineChartDataResponse(response);
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
                    HttpAsyncTaskGetLineChartDataResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Report Line Chart", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void HttpAsyncTaskGetLineChartDataResponse(JSONObject response) throws JSONException {

        JSONObject json;
        String connectionStatus;
        json = new JSONObject();
        try {

            json = response;
            Log.i("json ", json + "");
            //	connectionStatus = json.getString("connectionStatus");
            //showMyAlertProgressDialog.hideProgressDialog();

            JSONArray dataList = json.getJSONArray("dataList");
            Boolean dataPresent = false;
            Log.i("dataList ", dataList + "");
            Log.i("dataList.length() ", dataList.length() + "");

            if (dataList.length() > 0) {
                JSONObject obs = dataList.getJSONObject(0);
                dataCount = obs.get("data").toString().split(",").length;
                addEmptyData();
                mChart.invalidate();
            }

            for (int i = 0; i < dataList.length(); i++) {

                JSONObject obs = dataList.getJSONObject(i);
						/*if(i==0){
							Log.i("aaaaaaaaaaaa", "aaaaaaaaaaaaaa "+i);
							dataCount = obs.get("data").toString().split(",").length;
							addEmptyData();
						    mChart.invalidate();
						}else{*/
                dataPresent = true;
                categoryAmount = new String[dataCount];
                categoryName = obs.get("name").toString();
                String catAmt = obs.get("data").toString();
                catAmt = catAmt.substring(1, catAmt.length() - 1);
                String[] list = new String[dataCount];
                list = catAmt.split(",");

                for (int j = 0; j < list.length; j++) {
                    categoryAmount[j] = list[j];
                }
                addDataSet(categoryName, categoryAmount);

                //}

            }
            Log.i("dataPresent", dataPresent + "");
            if (!dataPresent) {
                mChart.clear();
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JSONObject res = new JSONObject();
            try {
                res.put("connectionStatus", "failed");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private void addEmptyData() {
        String[] xVals = new String[dataCount];
        for (int i = 0; i < dataCount; i++) {
            xVals[i] = "D" + (i + 1);
        }

        // create a chartdata object that contains only the x-axis labels (no entries or datasets)
        LineData data = new LineData(xVals);
        mChart.setData(data);
        mChart.invalidate();
    }

    private void addDataSet(String categoryName, String[] amtList) {

        LineData data = mChart.getData();

        if (data != null) {

            int count = (data.getDataSetCount() + 1);
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            for (int i = 0; i < data.getXValCount(); i++) {
                float amt = Float.parseFloat(amtList[i]);
                yVals.add(new Entry(amt, i));
            }

            LineDataSet set = new LineDataSet(yVals, categoryName);
            set.setLineWidth(2.5f);
            set.setCircleSize(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);

            data.addDataSet(set);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		/*userDeviceDetails
				.showToast(e.getVal()+"");*/
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.dateFrom:
                showDatePicker(dateFrom);
                break;
            case R.id.dateTo:
                showDatePicker(dateTo);
                break;
            case R.id.searchDataBetweenDate:
//			userDeviceDetails.showToast("Date Search");
                try {
                    Boolean result = verifyDateSearch(dateFrom.getText().toString(), dateTo.getText().toString());
                    if (result) {
                        //userDeviceDetails.showToast("rrrrrr");
                        searchParameter = dateFrom.getText().toString() + "," + dateTo.getText().toString();
                        connectWithServer();
                    } else {
                        userDeviceDetails.showToast("Start date must be lesser or equal than to end date");
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    private Boolean verifyDateSearch(String dateFromText, String dateToText) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(dateFromText);
        Date toDate = sdf.parse(dateToText);
        Boolean returnValue = false;
        if (fromDate.compareTo(toDate) > 0) {
//    		userDeviceDetails.showToast("Date1 is after Date2");
            returnValue = false;
        } else if (fromDate.compareTo(toDate) < 0) {
            returnValue = true;
//    		userDeviceDetails.showToast("Date1 is before Date2");
        } else if (fromDate.compareTo(toDate) == 0) {
            returnValue = true;
//    		userDeviceDetails.showToast("Date1 is equal to Date2");
        } else {
            returnValue = false;
//    		userDeviceDetails.showToast("How to get here?");
        }

        return returnValue;
    }

}
