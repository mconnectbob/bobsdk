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
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.CorporateAdapter;
import com.bob.bobapp.adapters.TransactionListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.TransactionRequestBodyModel;
import com.bob.bobapp.api.request_object.TransactionRequestModel;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
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

public class CorporateActivity extends BaseFragment implements onSortItemListener {
    private ArrayList<TransactionResponseModel> arrayList = new ArrayList<>();
    private ArrayList<TransactionResponseModel> fiterDatearrayList = new ArrayList<>();
    private CorporateAdapter adapter;
    private TextView calender, tvSelectedDate, tvGo, tvClear, txtSort;
    private EditText etSearch;
    private RecyclerView rv;
    private APIInterface apiInterface;
    private Util util;
    private TransactionResponseModel responseStr;
    private String whichActivity = "";
    private LinearLayout layoutDate;
    private int mYear, mMonth, mDay;

    private String strDateForRequest = "", searchKey = "", status = "1",sipDate="";

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {


        rv = view.findViewById(R.id.rv);
        calender = view.findViewById(R.id.calender);
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
        BOBActivity.title.setText("Dividend");

        apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_TRANSACTION);
        util = new Util(context);
        getTransactionApiCall("", "");

    }

    private void getTransactionApiCall(String dateFrom, String dateTo) {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        TransactionRequestBodyModel requestBodyModel = new TransactionRequestBodyModel();
        requestBodyModel.setUserId(authenticateResponse.getUserID());
        requestBodyModel.setOnlineAccountCode(authenticateResponse.getUserCode());
        requestBodyModel.setSchemeCode("0");


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
            requestBodyModel.setDateTo(finalDateTo);
            requestBodyModel.setDateFrom(newDate);
        } else {
            requestBodyModel.setDateTo(dateTo);
            requestBodyModel.setDateFrom(dateFrom);
        }

        requestBodyModel.setOrderType("1");
        requestBodyModel.setPageIndex("6");
        requestBodyModel.setPageSize("5");
        requestBodyModel.setCurrencyCode("1");
        requestBodyModel.setAmountDenomination("0");
        requestBodyModel.setAccountLevel("0");
        requestBodyModel.setIsFundware("false");
        requestBodyModel.setClientType("H");
        requestBodyModel.setForCorporateAction("Y");

        TransactionRequestModel model = new TransactionRequestModel();
        model.setRequestBodyObject(requestBodyModel);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);
        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);

        apiInterface.getTransactionApiCall(model).enqueue(new Callback<ArrayList<TransactionResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TransactionResponseModel>> call, Response<ArrayList<TransactionResponseModel>> response) {

                util.showProgressDialog(context, false);
                System.out.println("Corporate Activity VALIDATION RESPONSE: " + new Gson().toJson(response.body()));
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
            public void onFailure(Call<ArrayList<TransactionResponseModel>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setAdapter(ArrayList<TransactionResponseModel> arrayList) {

        if (arrayList != null && arrayList.size() > 0) {

            adapter = new CorporateAdapter(context, arrayList, whichActivity);

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
          //  String selectedDate = tvSelectedDate.getText().toString();
            String selectedDate = sipDate;
            if (selectedDate.equalsIgnoreCase("select Date") || sipDate.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "please select date", Toast.LENGTH_SHORT).show();
            } else {
                status = "2";
                String[] arrayString = selectedDate.split("To");

                String fromDate = arrayString[0];
                String toDate = arrayString[1];

                getTransactionApiCall(fromDate, toDate);

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

        ArrayList<TransactionResponseModel> filteredList = new ArrayList<>();

        for (TransactionResponseModel item : arrayList) {

            if (item.getDate() != null) {

                if (item.getDate().contains(text)) {

                    filteredList.add(item);
                }
            }
        }
        if (filteredList != null && filteredList.size() > 0) {
            adapter.updateList(filteredList);
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    // filter by name
    private void filterByName(String text) {
        if (status.equalsIgnoreCase("1")) {
            ArrayList<TransactionResponseModel> filteredList = new ArrayList<>();
            for (TransactionResponseModel item : arrayList) {

                if (item.getSecurity() != null) {

                    if (item.getSecurity().toLowerCase().startsWith(text.toLowerCase())) {

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
            ArrayList<TransactionResponseModel> filteredList = new ArrayList<>();
            for (TransactionResponseModel item : fiterDatearrayList) {
                if (item.getSecurity() != null) {
                    if (item.getSecurity().toLowerCase().startsWith(text.toLowerCase())) {

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


    private void openCalender(final TextView textView) {

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);

        mMonth = c.get(Calendar.MONTH);

        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                try {

                    String dateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                    Date date = format.parse(dateStr);


                    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                    String strDate = dateFormat.format(date);


                    DateFormat dateFormatForRequest = new SimpleDateFormat("yyyy-MM-dd");

                    strDateForRequest = dateFormatForRequest.format(date);

                    textView.setText(strDate);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    // plan Dialog
    private void openAllPlanDialog() {
        SortByDialog allPlanDialog = new SortByDialog(getContext(), this, "Amount");
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onSortItemListener(String name) {
        etSearch.setText("");
        try {
            adapter.updateList(arrayList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (name.equalsIgnoreCase("1")) {
            if (status.equalsIgnoreCase("1")) {
                if (arrayList != null && arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            TransactionResponseModel p1 = (TransactionResponseModel) o1;
                            TransactionResponseModel p2 = (TransactionResponseModel) o2;
                            return p1.getSecurity().compareToIgnoreCase(p2.getSecurity());
                        }
                    });
                    adapter.updateList(arrayList);
                }
            } else {
                if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                    Collections.sort(fiterDatearrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            TransactionResponseModel p1 = (TransactionResponseModel) o1;
                            TransactionResponseModel p2 = (TransactionResponseModel) o2;
                            return p1.getSecurity().compareToIgnoreCase(p2.getSecurity());
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
                            TransactionResponseModel p1 = (TransactionResponseModel) o1;
                            TransactionResponseModel p2 = (TransactionResponseModel) o2;
                            return Double.compare(Double.parseDouble("" + p1.getAmount()), Double.parseDouble("" + p2.getAmount()));
                        }
                    });
                    adapter.updateList(arrayList);
                }
            } else {
                if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                    Collections.sort(fiterDatearrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            TransactionResponseModel p1 = (TransactionResponseModel) o1;
                            TransactionResponseModel p2 = (TransactionResponseModel) o2;
                            return Double.compare(Double.parseDouble("" + p1.getAmount()), Double.parseDouble("" + p2.getAmount()));
                        }
                    });
                    adapter.updateList(fiterDatearrayList);
                }
            }
        }
    }
}
