//package com.paybyonline.ebiz.Fragment;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.support.annotation.IdRes;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.paybyonline.ebiz.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//
//public class BookFlightFragment extends Fragment implements View.OnClickListener {
//
//    ImageView updown_dwl;
//    boolean flag = true;
//    TextView passenger_number, childPassengerNumber;
//    Button setSelectedDateBtn;
//    String date;
//    Button flight_from, flight_to;
//    RadioButton radio_round_trip, radio_one_way;
//    RadioGroup radio_flight_group;
//    int day, year, month;
//    int presentDay, presentYear, presentMonth;
//    TextView passenger_number_edtxt;
//    LinearLayout showBookingOption;
//    RelativeLayout showPassenger;
//
//    int passengers = 0;
//    int childPassenger = 0, adultPassenger = 0;
//    ImageView child_passenger_plus, child_passenger_minus, adult_passenger_plus, adult_passenger_minus;
//
//    public BookFlightFragment() {
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_flight, container, false);
//        flight_from = (Button) view.findViewById(R.id.flight_from);
//        flight_to = (Button) view.findViewById(R.id.flight_to);
//        radio_round_trip = (RadioButton) view.findViewById(R.id.radio_round_trip);
//        radio_one_way = (RadioButton) view.findViewById(R.id.radio_one_way);
//        radio_flight_group = (RadioGroup) view.findViewById(R.id.radio_flight_group);
//        passenger_number_edtxt = (TextView) view.findViewById(R.id.passenger_number_edtxt);
//        showBookingOption = (LinearLayout) view.findViewById(R.id.showBookingOption);
//        showPassenger = (RelativeLayout) view.findViewById(R.id.showPassenger);
//        passenger_number = (TextView) view.findViewById(R.id.passenger_number);
//        childPassengerNumber = (TextView) view.findViewById(R.id.childPassengerNumber);
//        child_passenger_plus = (ImageView) view.findViewById(R.id.child_passenger_plus);
//        child_passenger_minus = (ImageView) view.findViewById(R.id.child_passenger_minus);
//        adult_passenger_plus = (ImageView) view.findViewById(R.id.adult_passenger_plus);
//        adult_passenger_minus = (ImageView) view.findViewById(R.id.adult_passenger_minus);
//        updown_dwl = (ImageView) view.findViewById(R.id.updown_dwl);
//        updown_dwl.setOnClickListener(this);
//        radio_round_trip.setChecked(true);
//        flight_to.setVisibility(View.VISIBLE);
//        flight_to.setOnClickListener(this);
//        showPassenger.setOnClickListener(this);
//        passenger_number_edtxt.setOnClickListener(this);
//        showBookingOption.setVisibility(View.GONE);
//
//        child_passenger_plus.setOnClickListener(this);
//        child_passenger_minus.setOnClickListener(this);
//        adult_passenger_plus.setOnClickListener(this);
//        adult_passenger_minus.setOnClickListener(this);
//
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String[] dateString = date.split("-");
//        presentYear = Integer.parseInt(dateString[0]);
//        presentMonth = Integer.parseInt(dateString[1]);
//        presentDay = Integer.parseInt(dateString[2]);
//        flight_from.setText(date);
//        flight_to.setText(date);
///*        if (adultPassenger>0)
//            adult_passenger_minus.setEnabled(true);
//        else
//            adult_passenger_minus.setEnabled(false);
//        if (childPassenger>0)
//            child_passenger_minus.setEnabled(true);
//        else
//            child_passenger_minus.setEnabled(false);*/
//
//        flight_from.setOnClickListener(this);
//        radio_flight_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                switch (checkedId) {
//                    case R.id.radio_one_way:
//                        flight_to.setVisibility(View.GONE);
//                        radio_round_trip.setChecked(false);
//
//                        break;
//
//                    case R.id.radio_round_trip:
//                        flight_to.setVisibility(View.VISIBLE);
//                        radio_one_way.setChecked(false);
//                        break;
//                }
//            }
//        });
//        return view;
//    }
//
//    public void setCurrentDateOnView(Button fromToBtn, String calDate) {
//
//        if (calDate.length() > 0) {
//
////           String[] lastPostDateVal = lastPostDate.split("-");
//            fromToBtn.setText(calDate);
//
//        } else {
//
//            final Calendar c = Calendar.getInstance();
//            year = c.get(Calendar.YEAR);
//            month = c.get(Calendar.MONTH);
//            day = c.get(Calendar.DAY_OF_MONTH);
//
//            // set current date into textview
//            fromToBtn.setText(new StringBuilder()
//                    // Month is 0 based, just add 1
//                    .append(year).append("-").append(month + 1).append("-")
//                    .append(day));
//
//        }
//
//
//    }
//
//    void showDatePicker(Button fromToBtn) {
//
//        setSelectedDateBtn = fromToBtn;
//        DatePickerFragment date = new DatePickerFragment();
//
//        Calendar calender = Calendar.getInstance();
//        Bundle args = new Bundle();
//
//        String[] selDate = fromToBtn.getText().toString().split("-");
//
//        args.putInt("year", Integer.parseInt(selDate[0]));
//        args.putInt("month", Integer.parseInt(selDate[1]) - 1);
//        args.putInt("day", Integer.parseInt(selDate[2]));
//        date.setArguments(args);
//
//        date.setCallBack(ondate);
//
//        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
//
//
//    }
//
//    DatePickerDialog.OnDateSetListener ondate =
//            new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                      int dayOfMonth) {
//
//                    date = String.valueOf(year) + "-"
//                            + String.valueOf(monthOfYear + 1) + "-"
//                            + String.valueOf(dayOfMonth);
//
//                    setSelectedDateBtn.setText(date);
//
//                }
//            };
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.flight_from:
//                showDatePicker(flight_from);
//                break;
//            case R.id.showPassenger:
//                if (flag) {
//                    showBookingOption.setVisibility(View.VISIBLE);
//                    flag = false;
//                    updown_dwl.setImageResource(R.drawable.ic_expand_less);
//                } else {
//                    showBookingOption.setVisibility(View.GONE);
//                    flag = true;
//                    updown_dwl.setImageResource(R.drawable.ic_expand_more);
//                }
//
//                break;
//            case R.id.updown_dwl:
//                if (flag) {
//                    showBookingOption.setVisibility(View.VISIBLE);
//                    flag = false;
//                    updown_dwl.setImageResource(R.drawable.ic_expand_less);
//                } else {
//                    showBookingOption.setVisibility(View.GONE);
//                    flag = true;
//                    updown_dwl.setImageResource(R.drawable.ic_expand_more);
//                }
//
//                break;
//            case R.id.flight_to:
//                showDatePicker(flight_to);
//                break;
//            case R.id.passenger_number_edtxt:
//                if (flag) {
//                    showBookingOption.setVisibility(View.VISIBLE);
//                    flag = false;
//                    updown_dwl.setImageResource(R.drawable.ic_expand_less);
//                } else {
//                    showBookingOption.setVisibility(View.GONE);
//                    flag = true;
//                    updown_dwl.setImageResource(R.drawable.ic_expand_more);
//                }
//
//                break;
//            case R.id.adult_passenger_plus:
//                adultPassenger++;
//                passenger_number.setText(String.valueOf(adultPassenger));
//                passenger_number_edtxt.setText(String.valueOf(adultPassenger + childPassenger) + " Passengers");
//
//                break;
//            case R.id.child_passenger_plus:
//                childPassenger++;
//                childPassengerNumber.setText(String.valueOf(childPassenger));
//                passenger_number_edtxt.setText(String.valueOf(adultPassenger + childPassenger) + " Passengers");
//                break;
//            case R.id.adult_passenger_minus:
//
//                if (adultPassenger > 0) {
//                    adultPassenger--;
//
//                    passenger_number.setText(String.valueOf(adultPassenger));
//                    passenger_number_edtxt.setText(String.valueOf(adultPassenger + childPassenger) + " Passengers");
//                }
//                break;
//            case R.id.child_passenger_minus:
//
//                if (childPassenger > 0) {
//                    childPassenger--;
//
//                    childPassengerNumber.setText(String.valueOf(childPassenger));
//                    passenger_number_edtxt.setText(String.valueOf(adultPassenger + childPassenger) + " Passengers");
//                }
//
//                break;
//        }
//    }
//
//
//}
