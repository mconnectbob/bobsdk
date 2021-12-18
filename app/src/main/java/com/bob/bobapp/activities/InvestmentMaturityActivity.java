package com.bob.bobapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.InvestmentMaturityListAdapter;
import com.bob.bobapp.adapters.SIPSWPSTPDueListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.MaturitiesReportModel;
import com.bob.bobapp.api.request_object.MaturityReportRequestModel;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.InvestmentMaturityModel;
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

public class InvestmentMaturityActivity extends BaseFragment implements onSortItemListener {
    private TextView calender, tvSelectedDate, tvGo, tvClear, txtSort;
    private EditText etSearch;
    private InvestmentMaturityListAdapter adapter;
    private RecyclerView rv;
    private Util util;
    private APIInterface apiInterface;
    private Context context;
    private String searchKey = "", strDateForRequest = "", status = "1", sipDate="";
    private ArrayList<InvestmentMaturityModel> arrayList = new ArrayList<>();
    private ArrayList<InvestmentMaturityModel> fiterDatearrayList = new ArrayList<>();
    private LinearLayout layoutDate;
    private int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_investment_maturity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void getIds(View view) {
        calender = view.findViewById(R.id.calender);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        layoutDate = view.findViewById(R.id.layout_date);
        tvGo = view.findViewById(R.id.tv_go);
        tvClear = view.findViewById(R.id.tv_clear);
        rv = view.findViewById(R.id.rv);
        etSearch = view.findViewById(R.id.etSearch);
        txtSort = view.findViewById(R.id.txtSort);
    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        layoutDate.setOnClickListener(this);
        tvGo.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        txtSort.setOnClickListener(this);
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
        BOBActivity.title.setText("Investment Maturities");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_INVESTMENT_MATURITY);
        util = new Util(context);
        getApiCall("", "");
    }

    private void filterByName(String text) {
        if (status.equalsIgnoreCase("1")) {
            ArrayList<InvestmentMaturityModel> filteredList = new ArrayList<>();

            for (InvestmentMaturityModel item : arrayList) {

                if (item.getSchName() != null) {

                    if (item.getSchName().toLowerCase().startsWith(text.toLowerCase())) {

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
            ArrayList<InvestmentMaturityModel> filteredList = new ArrayList<>();

            for (InvestmentMaturityModel item : fiterDatearrayList) {

                if (item.getSchName() != null) {

                    if (item.getSchName().toLowerCase().startsWith(text.toLowerCase())) {

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


    // api calling
    private void getApiCall(String dateFrom, String dateTo) {
        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        MaturitiesReportModel model = new MaturitiesReportModel();

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
        c.add(Calendar.DAY_OF_MONTH, 7);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());

        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + newDate);


        if (status.equalsIgnoreCase("1")) {
            model.setFromDate(finalDateTo);
            model.setTillDate(newDate);
        } else {
            model.setFromDate(dateFrom);
            model.setTillDate(dateTo);
        }

        model.setHeadCode(authenticateResponse.getUserCode());

        MaturityReportRequestModel requestModel = new MaturityReportRequestModel();
        requestModel.setRequestBodyObject(model);
        requestModel.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);
        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        requestModel.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getInvestmentMaturityReportApiCall(requestModel).enqueue(new Callback<ArrayList<InvestmentMaturityModel>>() {
            @Override
            public void onResponse(Call<ArrayList<InvestmentMaturityModel>> call, Response<ArrayList<InvestmentMaturityModel>> response) {
                util.showProgressDialog(context, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (status.equalsIgnoreCase("1")) {
                        arrayList = response.body();
                        setAdapter(arrayList);
                    } else {

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
            public void onFailure(Call<ArrayList<InvestmentMaturityModel>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(ArrayList<InvestmentMaturityModel> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            adapter = new InvestmentMaturityListAdapter(context, arrayList);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setIcon(Util util) {
        FontManager.markAsIconContainer(calender, util.iconFont);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.menu) {
            getActivity().onBackPressed();
        } else if (view.getId() == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (view.getId() == R.id.layout_date) {
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

                                        tvSelectedDate.setText(dt1.format(filterFromDate) + " " + "To" + " " + dt1.format(filterToDate));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                }
                            });
            smoothDateRangePickerFragment.show(getActivity().getFragmentManager(), "Datepickerdialog");

        } else if (view.getId() == R.id.tv_clear) {
            status = "1";
            tvSelectedDate.setText("Select Date");
            try {
                adapter.updateList(arrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            strDateForRequest = "";
//
//            tvSelectedDate.setText("Select Date");
//
//            if (arrayList != null && arrayList.size() > 0) {
//                adapter.updateList(arrayList);
//            }

        } else if (view.getId() == R.id.tv_go) {
            String selectedDate = sipDate;

            if (selectedDate.equalsIgnoreCase("select Date") || selectedDate.isEmpty())
            {
                Toast.makeText(getContext(), "please select date", Toast.LENGTH_SHORT).show();
            } else {
                sipDate="";
                status = "2";
                String[] arrayString = selectedDate.split("To");

                String fromDate = arrayString[0];
                String toDate = arrayString[1];

                getApiCall(fromDate, toDate);
            }
        } else if (view.getId() == R.id.txtSort) {

            openAllPlanDialog();


        }

    }


    // plan Dialog
    private void openAllPlanDialog() {
        SortByDialog allPlanDialog = new SortByDialog(getContext(), this, "Current Value");
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
                            InvestmentMaturityModel p1 = (InvestmentMaturityModel) o1;
                            InvestmentMaturityModel p2 = (InvestmentMaturityModel) o2;
                            return p1.getSchName().compareToIgnoreCase(p2.getSchName());
                        }
                    });
                    adapter.updateList(arrayList);
                } else {
                    if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                        Collections.sort(fiterDatearrayList, new Comparator() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                InvestmentMaturityModel p1 = (InvestmentMaturityModel) o1;
                                InvestmentMaturityModel p2 = (InvestmentMaturityModel) o2;
                                return p1.getSchName().compareToIgnoreCase(p2.getSchName());
                            }
                        });
                        adapter.updateList(fiterDatearrayList);
                    }
                }

            }
        } else {
            if (status.equalsIgnoreCase("1")) {
                if (arrayList != null && arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            InvestmentMaturityModel p1 = (InvestmentMaturityModel) o1;
                            InvestmentMaturityModel p2 = (InvestmentMaturityModel) o2;
                            return Double.compare(Double.parseDouble(p1.getCostOnInvestment()), Double.parseDouble(p2.getCostOnInvestment()));
                        }
                    });
                    adapter.updateList(arrayList);
                }
            } else {
                if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                    Collections.sort(fiterDatearrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            InvestmentMaturityModel p1 = (InvestmentMaturityModel) o1;
                            InvestmentMaturityModel p2 = (InvestmentMaturityModel) o2;
                            return Double.compare(Double.parseDouble(p1.getCostOnInvestment()), Double.parseDouble(p2.getCostOnInvestment()));
                        }
                    });
                    adapter.updateList(fiterDatearrayList);
                }

            }
        }
    }

}

