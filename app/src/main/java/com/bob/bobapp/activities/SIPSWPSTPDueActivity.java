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
import com.bob.bobapp.adapters.SIPSWPSTPDueListAdapter;
import com.bob.bobapp.adapters.TransactionListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.SIPSWPSTPRequestBodyModel;
import com.bob.bobapp.api.request_object.SIPSWPSTPRequestModel;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
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

public class SIPSWPSTPDueActivity extends BaseFragment implements onSortItemListener {

    private TextView calender, tvSelectedDate, tvGo, tvClear, txtSort;
    private EditText etSearch;
    private RecyclerView rv;
    private APIInterface apiInterface;
    private Util util;
    private LinearLayout layoutDate;
    private int mYear, mMonth, mDay;
    private String strDateForRequest = "", searchKey = "", status = "1",sipDate="";
    private SIPSWPSTPDueListAdapter adapter;
    private ArrayList<SIPDueReportResponse> arrayList = new ArrayList<>();
    private ArrayList<SIPDueReportResponse> fiterDatearrayList = new ArrayList<>();
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_s_i_p_s_w_p_s_t_p_due, container, false);
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
        BOBActivity.title.setText("SIP SWP STP");
        BOBActivity.llMenu.setVisibility(View.GONE);
        apiInterface = BOBApp.getApi(context, Constants.ACTION_SIP_SWP_STP_DUE);
        util = new Util(context);
        getSIPSWRSTPApiCall("", "");
    }

    // api calling
    private void getSIPSWRSTPApiCall(String dateFrom, String dateTo) {
        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        SIPSWPSTPRequestBodyModel requestBodyModel = new SIPSWPSTPRequestBodyModel();

        requestBodyModel.setUserId("admin");
        requestBodyModel.setUcc(authenticateResponse.getClientUCC());
        requestBodyModel.setClientCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setClientType("H");
        requestBodyModel.setFamCode(0);
        requestBodyModel.setHeadCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setReportLevel("DETAIL");


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

        if (status.equalsIgnoreCase("1"))
        {
            requestBodyModel.setFromDate(finalDateTo);
            requestBodyModel.setToDate(newDate);
        } else {
            requestBodyModel.setFromDate(dateFrom);
            requestBodyModel.setToDate(dateTo);
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
            public void onFailure(Call<ArrayList<SIPDueReportResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter(ArrayList<SIPDueReportResponse> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            adapter = new SIPSWPSTPDueListAdapter(context, arrayList);
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
            String selectedDate = sipDate;

            if (selectedDate.equalsIgnoreCase("select Date") || sipDate.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "please select date", Toast.LENGTH_SHORT).show();
            } else {

                status = "2";
                String[] arrayString = selectedDate.split("To");

                String fromDate = arrayString[0];
                String toDate = arrayString[1];

                getSIPSWRSTPApiCall(fromDate, toDate);
            }
        } else if (id == R.id.tv_clear)
        {
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
        ArrayList<SIPDueReportResponse> filteredList = new ArrayList<>();
        for (SIPDueReportResponse item : arrayList) {
            if (item.getNextInstallmentDate() != null) {
                if (item.getNextInstallmentDate().contains(text)) {
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
            ArrayList<SIPDueReportResponse> filteredList = new ArrayList<>();

            for (SIPDueReportResponse item : arrayList) {

                if (item.getFund_Name() != null) {

                    if (item.getFund_Name().toLowerCase().startsWith(text.toLowerCase())) {

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
            ArrayList<SIPDueReportResponse> filteredList = new ArrayList<>();

            for (SIPDueReportResponse item : fiterDatearrayList) {

                if (item.getFund_Name() != null) {

                    if (item.getFund_Name().toLowerCase().startsWith(text.toLowerCase())) {

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
        SortByDialog allPlanDialog = new SortByDialog(getContext(), this, "Amount");
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
                            SIPDueReportResponse p1 = (SIPDueReportResponse) o1;
                            SIPDueReportResponse p2 = (SIPDueReportResponse) o2;
                            return p1.getFund_Name().compareToIgnoreCase(p2.getFund_Name());
                        }
                    });
                    // adapter.notifyDataSetChanged();
                    adapter.updateList(arrayList);
                }
            } else {
                if (fiterDatearrayList != null && fiterDatearrayList.size() > 0) {
                    Collections.sort(fiterDatearrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            SIPDueReportResponse p1 = (SIPDueReportResponse) o1;
                            SIPDueReportResponse p2 = (SIPDueReportResponse) o2;
                            return p1.getFund_Name().compareToIgnoreCase(p2.getFund_Name());
                        }
                    });
                    // adapter.notifyDataSetChanged();
                    adapter.updateList(fiterDatearrayList);
                }
            }
        } else {
            if (status.equalsIgnoreCase("1")) {
                if (arrayList != null && arrayList.size() > 0) {
                    Collections.sort(arrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            SIPDueReportResponse p1 = (SIPDueReportResponse) o1;
                            SIPDueReportResponse p2 = (SIPDueReportResponse) o2;
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
                            SIPDueReportResponse p1 = (SIPDueReportResponse) o1;
                            SIPDueReportResponse p2 = (SIPDueReportResponse) o2;
                            return Double.compare(Double.parseDouble("" + p1.getAmount()), Double.parseDouble("" + p2.getAmount()));
                        }
                    });
                    adapter.updateList(fiterDatearrayList);
                }
            }
        }
    }
}
