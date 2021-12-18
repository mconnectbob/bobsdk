package com.bob.bobapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.InvestmentMaturityListAdapter;
import com.bob.bobapp.adapters.RealizaedGainLossListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.MaturitiesReportModel;
import com.bob.bobapp.api.request_object.MaturityReportRequestModel;
import com.bob.bobapp.api.request_object.RealisedGainLossRequestModel;
import com.bob.bobapp.api.request_object.RealizedGainLossRequestBodyModel;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.InvestmentMaturityModel;
import com.bob.bobapp.api.response_object.RealizedGainLoss;
import com.bob.bobapp.api.response_object.SIPDueReportResponse;
import com.bob.bobapp.api.response_object.TransactionResponseModel;
import com.bob.bobapp.dialog.SortByDialog;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.onSortItemListener;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealizedGainLossActivity extends BaseFragment implements onSortItemListener {
    private TextView calender, tvSelectedDate, tvGo, tvClear, txtSort;
    private EditText etSearch;
    private RecyclerView rv;
    private APIInterface apiInterface;
    private Util util;
    private LinearLayout layoutDate;
    private int mYear, mMonth, mDay;
    private String strDateForRequest = "", searchKey = "", status = "1",sipDate="";
    private ArrayList<RealizedGainLoss> arrayList = new ArrayList<>();
    private ArrayList<RealizedGainLoss> fiterDatearrayList = new ArrayList<>();
    private RealizaedGainLossListAdapter adapter;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_realized_gain_loss, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {
        calender = view.findViewById(R.id.calender);

        rv = view.findViewById(R.id.rv);

        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        layoutDate = view.findViewById(R.id.layout_date);
        tvGo = view.findViewById(R.id.tv_go);
        tvClear = view.findViewById(R.id.tv_clear);
        etSearch = view.findViewById(R.id.etSearch);
        txtSort = view.findViewById(R.id.txtSort);

    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        layoutDate.setOnClickListener(this);
        tvGo.setOnClickListener(this);
        txtSort.setOnClickListener(this);
        tvClear.setOnClickListener(this);
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
                if (s.length() == 0) {
                    try {
                        if (status.equalsIgnoreCase("1")) {
                            adapter.updateList(arrayList);
                        } else {
                            adapter.updateList(fiterDatearrayList);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });


    }


    @Override
    public void initializations() {
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Realized Gain/Loss");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_REALISED_GAIN_LOSS);
        util = new Util(context);
        getApiCall("", "");

    }

    @Override
    public void setIcon(Util util) {

        FontManager.markAsIconContainer(calender, util.iconFont);

    }


    private void getApiCall(String dateFrom, String dateTo) {

        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        RealizedGainLossRequestBodyModel model = new RealizedGainLossRequestBodyModel();
        model.setFamClient("H");
        model.setBOCode(authenticateResponse.getUserCode());
        model.setScripCode("0");

        String oldDate = authenticateResponse.getBusinessDate();

        String[] arrayString = oldDate.split("T");

        String fromDate = arrayString[0];

        String[] arrayStrings = fromDate.split("-");

        String year = arrayStrings[0];
        String month = arrayStrings[1];
        String day = arrayStrings[2];

        String finalDateTo = year + "/" + month + "/" + day;

        System.out.println("fromDate: " + finalDateTo);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(finalDateTo));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, -7);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());

        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + newDate);

        if (status.equalsIgnoreCase("1")) {
            model.setFromDate(newDate);
            model.setToDate(finalDateTo);
        } else {
            model.setFromDate(dateFrom);
            model.setToDate(dateTo);
        }

        model.setUserId("admin");
        model.setSchemeCode(0);
        model.setFamCode(0);
        model.setInvestType("A");
        model.setCurrencyCode(1);
        model.setAmountIn(0);
        model.setIsFundware(false);

        RealisedGainLossRequestModel requestModel = new RealisedGainLossRequestModel();
        requestModel.setRequestBodyObject(model);
        requestModel.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        requestModel.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getRealisedGainLossReportApiCall(requestModel).enqueue(new Callback<ArrayList<RealizedGainLoss>>() {
            @Override
            public void onResponse(Call<ArrayList<RealizedGainLoss>> call, Response<ArrayList<RealizedGainLoss>> response) {
                util.showProgressDialog(context, false);
                System.out.println("Mf VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (status.equalsIgnoreCase("1")) {
                        arrayList = response.body();
                        setAdapter(arrayList);
                    }
                    else {
                        fiterDatearrayList = response.body();
                        if (fiterDatearrayList.size() > 0) {
                            status = "2";
                        } else {
                            status = "1";
                        }
                        setAdapter(fiterDatearrayList);
                    }

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RealizedGainLoss>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter(ArrayList<RealizedGainLoss> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            adapter = new RealizaedGainLossListAdapter(context, arrayList);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.layout_date) {
            SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                    SmoothDateRangePickerFragment
                            .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                                @Override
                                public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                                           int yearStart, int monthStart,
                                                           int dayStart, int yearEnd,
                                                           int monthEnd, int dayEnd) {


                                    sipDate = yearStart + "/" + (++monthStart)
                                            + "/" + dayStart + " To " + yearEnd + "/"
                                            + (++monthEnd) + "/" + dayEnd;


                                    String[] arrayString = sipDate.split("To");

                                    String sipFromDate = arrayString[0];
                                    String sipToDate = arrayString[1];

                                    try {
                                        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
                                        Date filterFromDate = null;
                                        Date filterToDate = null;
                                        filterFromDate = dt.parse(sipFromDate);
                                        filterToDate = dt.parse(sipToDate);
                                        // *** same for the format String below
                                        // SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
                                        System.out.println("fdfszfs" + dt1.format(filterFromDate));

                                        tvSelectedDate.setText(dt1.format(filterFromDate)+" "+"To"+" "+dt1.format(filterToDate));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            });
            smoothDateRangePickerFragment.show(getActivity().getFragmentManager(), "Datepickerdialog");

        } else if (id == R.id.tv_go) {
            String selectedDate = tvSelectedDate.getText().toString();

            if (selectedDate.equalsIgnoreCase("select Date")|| sipDate.isEmpty()) {
                Toast.makeText(getContext(), "please select date", Toast.LENGTH_SHORT).show();
            } else {
                status = "2";
                String[] arrayString = selectedDate.split("To");

                String fromDate = arrayString[0];
                String toDate = arrayString[1];

                getApiCall(fromDate, toDate);
            }
        } else if (id == R.id.tv_clear) {
            sipDate="";
            status = "1";
            tvSelectedDate.setText("Select Date");
            try {
                adapter.updateList(arrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (id == R.id.txtSort) {

            openAllPlanDialog();


        }
    }

    private void filter(String text) {

        ArrayList<RealizedGainLoss> filteredList = new ArrayList<>();
        for (RealizedGainLoss item : arrayList) {
            if (item.getOpenDate() != null) {
                if (item.getOpenDate().contains(text)) {
                    filteredList.add(item);
                }
            }
        }

        adapter.updateList(filteredList);
    }

    // filter by name
    private void filterByName(String text) {
        if (status.equalsIgnoreCase("1")) {
            ArrayList<RealizedGainLoss> filteredList = new ArrayList<>();

            for (RealizedGainLoss item : arrayList) {

                if (item.getShortName() != null) {

                    if (item.getShortName().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }

            try {
                adapter.updateList(filteredList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ArrayList<RealizedGainLoss> filteredList = new ArrayList<>();

            for (RealizedGainLoss item : fiterDatearrayList) {

                if (item.getShortName() != null) {

                    if (item.getShortName().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }

            try {
                adapter.updateList(filteredList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // plan Dialog
    private void openAllPlanDialog() {
        SortByDialog allPlanDialog = new SortByDialog(getContext(), this, "Quantity");
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onSortItemListener(String name) {
        etSearch.setText("");
        if (name.equalsIgnoreCase("1")) {
            if (status.equalsIgnoreCase("1")) {
                if (arrayList != null && arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            RealizedGainLoss p1 = (RealizedGainLoss) o1;
                            RealizedGainLoss p2 = (RealizedGainLoss) o2;
                            return p1.getShortName().compareToIgnoreCase(p2.getShortName());
                        }
                    });
                    adapter.updateList(arrayList);
                }
            } else {
                if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                    Collections.sort(fiterDatearrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            RealizedGainLoss p1 = (RealizedGainLoss) o1;
                            RealizedGainLoss p2 = (RealizedGainLoss) o2;
                            return p1.getShortName().compareToIgnoreCase(p2.getShortName());
                        }
                    });
                    adapter.updateList(fiterDatearrayList);
                }
            }
        } else {
            if (status.equalsIgnoreCase("1")) {
                if (arrayList != null && arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            RealizedGainLoss p1 = (RealizedGainLoss) o1;
                            RealizedGainLoss p2 = (RealizedGainLoss) o2;
                            return Double.compare(p1.getQuantity(), p2.getQuantity());
                        }
                    });
                    adapter.updateList(arrayList);
                }
            } else {
                if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                    Collections.sort(fiterDatearrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            RealizedGainLoss p1 = (RealizedGainLoss) o1;
                            RealizedGainLoss p2 = (RealizedGainLoss) o2;
                            return Double.compare(p1.getQuantity(), p2.getQuantity());
                        }
                    });
                    adapter.updateList(fiterDatearrayList);
                }

            }

        }
    }
}

