package com.bob.bobapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.StopSIPListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.StopSipRequestObject;
import com.bob.bobapp.api.request_object.OrdersObject;
import com.bob.bobapp.api.request_object.SIPSWPSTPRequestBodyModel;
import com.bob.bobapp.api.request_object.SIPSWPSTPRequestModel;
import com.bob.bobapp.api.request_object.StopSipBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.SIPDueReportResponse;
import com.bob.bobapp.api.response_object.TransactionResponseModel;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.BMSPrefs;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopSIPActivity extends BaseFragment {
    private TextView calender, buy, swp, stp, tv_clear, tvSelectedDate, tv_go;
    private Button btnStop;
    private EditText etSearch;
    private LinearLayout layout_date;
    private RecyclerView rv;
    private StopSIPListAdapter adapter;
    private ArrayList<SIPDueReportResponse> sipArrayList = new ArrayList<>();
    private ArrayList<SIPDueReportResponse> swpArrayList = new ArrayList<>();
    private ArrayList<SIPDueReportResponse> stpArrayList = new ArrayList<>();
    private APIInterface apiInterface;
    private Util util;
    private LinearLayout llBuy, llSWP, viewBuy, viewSWP, llSTP, viewSTP;
    double count = 0;
    private Context context;
    ArrayList<OrdersObject> requestBodyObjectArrayList = new ArrayList<OrdersObject>();
    private String orderStatus = "1", goStatus = "1", fromDate = "", toDate = "";
    private String searchKey = "", strDateForRequest = "";
    private int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_stop_s_i_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void getIds(View view) {
        calender = view.findViewById(R.id.calender);

        rv = view.findViewById(R.id.rv);

        llBuy = view.findViewById(R.id.llBuy);
        llSWP = view.findViewById(R.id.llSWP);
        llSTP = view.findViewById(R.id.llSTP);
        buy = view.findViewById(R.id.buy);
        swp = view.findViewById(R.id.swp);
        stp = view.findViewById(R.id.stp);
        viewBuy = view.findViewById(R.id.viewBuy);
        viewSWP = view.findViewById(R.id.viewSWP);
        viewSTP = view.findViewById(R.id.viewSTP);
        btnStop = view.findViewById(R.id.btnStop);
        etSearch = view.findViewById(R.id.etSearch);
        tv_clear = view.findViewById(R.id.tv_clear);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        layout_date = view.findViewById(R.id.layout_date);
        tv_go = view.findViewById(R.id.tv_go);
    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        llBuy.setOnClickListener(this);
        llSWP.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        layout_date.setOnClickListener(this);
        tv_go.setOnClickListener(this);
        llSTP.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKey = etSearch.getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                filterByName(s.toString());
            }
        });

    }

    @Override
    public void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.title.setText("Stop SIP");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_SIP_SWP_STP_DUE);
        util = new Util(context);
        getApiCall("", "");
    }

    @Override
    public void setIcon(Util util) {
        FontManager.markAsIconContainer(calender, util.iconFont);
    }


    // filter by name
    private void filterByName(String text) {

        ArrayList<SIPDueReportResponse> filteredList = new ArrayList<>();

        if (orderStatus.equalsIgnoreCase("1")) {
            for (SIPDueReportResponse item : sipArrayList) {

                if (item.getFund_Name() != null) {

                    if (item.getFund_Name().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }
        } else {
            for (SIPDueReportResponse item : swpArrayList) {

                if (item.getFund_Name() != null) {

                    if (item.getFund_Name().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }

        }

        try {
            adapter.updateList(filteredList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void filter(String text) {

        ArrayList<SIPDueReportResponse> filteredList = new ArrayList<>();
        if (orderStatus.equalsIgnoreCase("1")) {
            for (SIPDueReportResponse item : sipArrayList) {

                if (item.getEndDate() != null) {

                    if (item.getEndDate().contains(text)) {

                        filteredList.add(item);
                    }
                }
            }

        } else {
            for (SIPDueReportResponse item : swpArrayList) {

                if (item.getEndDate() != null) {

                    if (item.getEndDate().contains(text)) {

                        filteredList.add(item);
                    }
                }
            }

        }

        if (filteredList.size() > 0) {
            adapter.updateList(filteredList);
        }
    }


    // api
    private void getApiCall(String dateFrom, String dateTo) {
        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        SIPSWPSTPRequestBodyModel requestBodyModel = new SIPSWPSTPRequestBodyModel();
        requestBodyModel.setUserId("admin");
        requestBodyModel.setClientCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setClientType("H");
        requestBodyModel.setFamCode(0);
        requestBodyModel.setHeadCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setReportType("Summary");
        String oldDate = authenticateResponse.getBusinessDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, 7);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());

        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + newDate);
        if (goStatus.equalsIgnoreCase("1")) {
            requestBodyModel.setToDate(newDate);
            requestBodyModel.setFromDate(authenticateResponse.getBusinessDate());
        } else {
            requestBodyModel.setToDate(dateTo);
            requestBodyModel.setFromDate(dateFrom);
        }


        SIPSWPSTPRequestModel model = new SIPSWPSTPRequestModel();
        model.setRequestBodyObject(requestBodyModel);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);
        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getSIPDueReportApiCall(model).enqueue(new Callback<ArrayList<SIPDueReportResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SIPDueReportResponse>> call, Response<ArrayList<SIPDueReportResponse>> response) {
                util.showProgressDialog(context, false);

                System.out.println("Stop Sip VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    swpArrayList.clear();
                    sipArrayList.clear();
                    stpArrayList.clear();
                    for (SIPDueReportResponse item : response.body()) {
                        if (item.getType().equalsIgnoreCase("swp")) {
                            swpArrayList.add(item);
                        } else if (item.getType().equalsIgnoreCase("SIP")) {
                            sipArrayList.add(item);
                        } else if (item.getType().equalsIgnoreCase("STP")) {
                            stpArrayList.add(item);
                        }

                    }

                    if (orderStatus.equalsIgnoreCase("1")) {
                        setAdapter(sipArrayList);
                    }

                    if (orderStatus.equalsIgnoreCase("2")) {
                        setAdapter(swpArrayList);
                    }

                    if (orderStatus.equalsIgnoreCase("3")) {
                        setAdapter(stpArrayList);
                    }


                    if (response.body() != null) {
                        btnStop.setVisibility(View.VISIBLE);
                    } else {
                        btnStop.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SIPDueReportResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setAdapter(ArrayList<SIPDueReportResponse> arrayList) {
        adapter = new StopSIPListAdapter(context, arrayList);
        rv.setAdapter(adapter);
//        if (arrayList != null && arrayList.size() > 0) {
//            adapter = new StopSIPListAdapter(context, arrayList);
//            rv.setAdapter(adapter);
//        } else {
//            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.llBuy) {
            try {
                orderStatus = "1";
                buy.setTextColor(getResources().getColor(R.color.black));
                swp.setTextColor(getResources().getColor(R.color.colorGray));
                viewBuy.setBackgroundColor(getResources().getColor(R.color.color_light_orange));
                viewSWP.setBackgroundColor(getResources().getColor(R.color.colorGray));
                stp.setTextColor(getResources().getColor(R.color.colorGray));
                viewSTP.setBackgroundColor(getResources().getColor(R.color.colorGray));
                adapter.updateList(sipArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (id == R.id.llSWP) {
            try {
                orderStatus = "2";
                buy.setTextColor(getResources().getColor(R.color.colorGray));
                swp.setTextColor(getResources().getColor(R.color.black));
                viewBuy.setBackgroundColor(getResources().getColor(R.color.colorGray));
                viewSWP.setBackgroundColor(getResources().getColor(R.color.color_light_orange));
                stp.setTextColor(getResources().getColor(R.color.colorGray));
                viewSTP.setBackgroundColor(getResources().getColor(R.color.colorGray));
                adapter.updateList(swpArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } else if (id == R.id.llSTP) {
            try {
                etSearch.setText("");
                orderStatus = "3";
                buy.setTextColor(getResources().getColor(R.color.colorGray));
                swp.setTextColor(getResources().getColor(R.color.colorGray));
                stp.setTextColor(getResources().getColor(R.color.black));
                viewBuy.setBackgroundColor(getResources().getColor(R.color.colorGray));
                viewSWP.setBackgroundColor(getResources().getColor(R.color.colorGray));
                viewSTP.setBackgroundColor(getResources().getColor(R.color.color_light_orange));
                adapter.updateList(stpArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (id == R.id.tv_go) {
            String selectedDate = tvSelectedDate.getText().toString();

            if (selectedDate.equalsIgnoreCase("select Date")) {
                Toast.makeText(getContext(), "please select date", Toast.LENGTH_SHORT).show();
            } else {
                goStatus = "2";
                String[] arrayString = selectedDate.split("To");

                fromDate = arrayString[0];
                toDate = arrayString[1];

                BMSPrefs.putString(getContext(), "filterFormDate", fromDate);
                BMSPrefs.putString(getContext(), "filterToDate", toDate);

                getApiCall(fromDate, toDate);
            }
        } else if (id == R.id.layout_date) {
            SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                    SmoothDateRangePickerFragment
                            .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                                @Override
                                public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                                           int yearStart, int monthStart,
                                                           int dayStart, int yearEnd,
                                                           int monthEnd, int dayEnd) {


                                    String date = yearStart + "-" + (++monthStart)
                                            + "-" + dayStart + " To " + yearEnd + "-"
                                            + (++monthEnd) + "-" + dayEnd;


                                    tvSelectedDate.setText(date);

                                }
                            });
            smoothDateRangePickerFragment.show(getActivity().getFragmentManager(), "Datepickerdialog");

        } else if (id == R.id.tv_clear) {
            strDateForRequest = "";
            tvSelectedDate.setText("Select Date");
            if (orderStatus.equalsIgnoreCase("1")) {
                if (sipArrayList.size() > 0) {
                    adapter.updateList(sipArrayList);
                }
            } else {
                if (swpArrayList.size() > 0) {
                    adapter.updateList(swpArrayList);
                }
            }

        } else if (id == R.id.btnStop) {
            if (orderStatus.equalsIgnoreCase("1")) {
                requestBodyObjectArrayList.clear();
                count = 0;
                for (SIPDueReportResponse model : sipArrayList) {
                    if (model.isSelected()) {
                        count = count + 1;
                        OrdersObject requestBodyObject = new OrdersObject();
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setRequestId(Integer.parseInt(model.getRequestId()));
                        requestBodyObject.setCode("" + model.getSchemeCode());
                        requestBodyObject.setOrderNumber(0);
                        requestBodyObject.setTransactionType(9);
                        requestBodyObject.setFundCode(0);
                        requestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (count > 0) {
                    stopSipAPIResponse();
                } else {
                    Toast.makeText(getContext(), "please select fund", Toast.LENGTH_SHORT).show();
                }
            } else if (orderStatus.equalsIgnoreCase("2")) {
                requestBodyObjectArrayList.clear();
                count = 0;
                for (SIPDueReportResponse model : swpArrayList) {
                    if (model.isSelected()) {
                        count = count + 1;
                        OrdersObject requestBodyObject = new OrdersObject();
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setRequestId(Integer.parseInt(model.getRequestId()));
                        requestBodyObject.setCode("" + model.getSchemeCode());
                        requestBodyObject.setOrderNumber(0);
                        requestBodyObject.setTransactionType(12);
                        requestBodyObject.setFundCode(0);
                        requestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (count > 0) {
                    stopSWPAPIResponse();
                } else {
                    Toast.makeText(getContext(), "please select fund", Toast.LENGTH_SHORT).show();
                }
            } else {
                requestBodyObjectArrayList.clear();
                count = 0;
                for (SIPDueReportResponse model : stpArrayList) {
                    if (model.isSelected()) {
                        count = count + 1;
                        OrdersObject requestBodyObject = new OrdersObject();
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setRequestId(Integer.parseInt(model.getRequestId()));
                        requestBodyObject.setCode("" + model.getSchemeCode());
                        requestBodyObject.setOrderNumber(0);
                        requestBodyObject.setTransactionType(11);
                        requestBodyObject.setFundCode(0);
                        requestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (count > 0) {
                    stopSTPAPIResponse();
                } else {
                    Toast.makeText(getContext(), "please select fund", Toast.LENGTH_SHORT).show();
                }
            }


        }

    }


    // api calling
    private void stopSipAPIResponse() {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_STOP_SIP);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        StopSipBodyObject stopSipBodyObject = new StopSipBodyObject();

        stopSipBodyObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        stopSipBodyObject.setFundCode("0");
        stopSipBodyObject.setClientCode(authenticateResponse.getUserCode());


        StopSipRequestObject globalRequestObject = new StopSipRequestObject();

        globalRequestObject.setRequestBodyObject(stopSipBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.stopSip(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                //  util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    requestBodyObjectArrayList.clear();
                    Toast.makeText(getContext(), "Systematic Transaction Cancellation is successful", Toast.LENGTH_SHORT).show();
                    if (goStatus.equalsIgnoreCase("1")) {
                        getApiCall("", "");
                    } else {
                        getApiCall(BMSPrefs.getString(getContext(), "filterFormDate"), BMSPrefs.getString(getContext(), "filterToDate"));
                    }

                } else {
                    requestBodyObjectArrayList.clear();
                    System.out.println("VALIDATION RESPONSEsss: " + response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSEsss: " + t.getLocalizedMessage());


                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // api calling
    private void stopSWPAPIResponse() {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_STOP_SIP);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        StopSipBodyObject stopSipBodyObject = new StopSipBodyObject();

        stopSipBodyObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        stopSipBodyObject.setFundCode("0");
        stopSipBodyObject.setClientCode(authenticateResponse.getUserCode());


        StopSipRequestObject globalRequestObject = new StopSipRequestObject();

        globalRequestObject.setRequestBodyObject(stopSipBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.stopSwp(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                //   util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    requestBodyObjectArrayList.clear();
                    Toast.makeText(getContext(), "Systematic Transaction Cancellation is successful", Toast.LENGTH_SHORT).show();
                    if (goStatus.equalsIgnoreCase("1")) {
                        getApiCall("", "");
                    } else {
                        getApiCall(BMSPrefs.getString(getContext(), "filterFormDate"), BMSPrefs.getString(getContext(), "filterToDate"));
                    }
                } else {
                    requestBodyObjectArrayList.clear();
                    System.out.println("VALIDATION RESPONSEsss: " + response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSEsss: " + t.getLocalizedMessage());


                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // stp
    private void stopSTPAPIResponse() {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_STOP_SIP);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        StopSipBodyObject stopSipBodyObject = new StopSipBodyObject();

        stopSipBodyObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        stopSipBodyObject.setFundCode("0");
        stopSipBodyObject.setClientCode(authenticateResponse.getUserCode());


        StopSipRequestObject globalRequestObject = new StopSipRequestObject();

        globalRequestObject.setRequestBodyObject(stopSipBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.stopStp(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                //util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    requestBodyObjectArrayList.clear();
                    Toast.makeText(getContext(), "Systematic Transaction Cancellation is successful", Toast.LENGTH_SHORT).show();

                    if (goStatus.equalsIgnoreCase("1")) {
                        getApiCall("", "");
                    } else {
                        getApiCall(BMSPrefs.getString(getContext(), "filterFormDate"), BMSPrefs.getString(getContext(), "filterToDate"));
                    }

                } else {
                    requestBodyObjectArrayList.clear();
                    System.out.println("VALIDATION RESPONSEsss: " + response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSEsss: " + t.getLocalizedMessage());


                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

