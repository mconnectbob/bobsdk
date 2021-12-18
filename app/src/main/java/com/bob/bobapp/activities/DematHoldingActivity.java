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
import com.bob.bobapp.adapters.DematHoldingListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.ClientHoldingRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.OrderStatusResponse;
import com.bob.bobapp.api.response_object.TransactionResponseModel;
import com.bob.bobapp.dialog.SortByDialog;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

public class DematHoldingActivity extends BaseFragment implements onSortItemListener {
    private TextView calender, tvSelectedDate, tvGo, tvClear, txtSort;
    private LinearLayout layoutDate;
    private RecyclerView rv;
    private APIInterface apiInterface;
    private Util util;
    private ArrayList<ClientHoldingObject> holdingArrayList=new ArrayList<>();
    private DematHoldingListAdapter adapter;
    private int mYear, mMonth, mDay;
    private String strDateForRequest = "", searchKey = "";
    private Context context;
    private EditText etSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_demat_holding, container, false);
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
        txtSort = view.findViewById(R.id.txtSort);
        etSearch = view.findViewById(R.id.etSearch);

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
            }
        });


    }

    @Override
    public void initializations() {
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.title.setText("Demat Holdings");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_HOLDING);
        util = new Util(context);
        getHoldingApiCall();
    }


    // holding api
    private void getHoldingApiCall() {
        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        RequestBodyObject requestBodyObject = new RequestBodyObject();
        requestBodyObject.setUserId(authenticateResponse.getUserID());
        requestBodyObject.setUserType(authenticateResponse.getUserType());
        requestBodyObject.setUserCode(authenticateResponse.getUserCode());
        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());
        requestBodyObject.setCurrencyCode("1"); //For INR
        requestBodyObject.setAmountDenomination("0"); //For base
        requestBodyObject.setAccountLevel("0"); //For client
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);
        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        ClientHoldingRequest.createClientHoldingRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);

        apiInterface.getHoldingResponse(ClientHoldingRequest.getClientHoldingRequestObject()).enqueue(new Callback<ArrayList<ClientHoldingObject>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientHoldingObject>> call, Response<ArrayList<ClientHoldingObject>> response) {

                util.showProgressDialog(context, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    for (ClientHoldingObject item : response.body()) {
                        if (item.getSource().equalsIgnoreCase("Direct Equity")) {
                            holdingArrayList.add(item);
                        }
                    }
                    setAdapter();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ClientHoldingObject>> call, Throwable t) {

                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setAdapter() {
        adapter = new DematHoldingListAdapter(context, holdingArrayList);
        rv.setAdapter(adapter);

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
            openCalender(tvSelectedDate);
        } else if (id == R.id.tv_go) {
            String selectedDate = strDateForRequest;
            if (!selectedDate.equals("")) {
                filter(selectedDate);
            }
        } else if (id == R.id.tv_clear) {
            strDateForRequest = "";
            tvSelectedDate.setText("Select Date");
            adapter.updateList(holdingArrayList);
        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (id == R.id.txtSort) {

            openAllPlanDialog();


        }
    }

    private void filter(String text) {

        ArrayList<ClientHoldingObject> filteredList = new ArrayList<>();
        for (ClientHoldingObject item : holdingArrayList) {
            if (item.getMaturityDate() != null) {
                if (item.getMaturityDate().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        adapter.updateList(filteredList);
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
        SortByDialog allPlanDialog = new SortByDialog(getContext(), this, "Quantity");
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onSortItemListener(String name) {
        etSearch.setText("");
        if (name.equalsIgnoreCase("1")) {
            try {
                adapter.updateList(holdingArrayList);
                Collections.sort(holdingArrayList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ClientHoldingObject p1 = (ClientHoldingObject) o1;
                        ClientHoldingObject p2 = (ClientHoldingObject) o2;
                        return p1.getSchemeName().compareToIgnoreCase(p2.getSchemeName());
                    }
                });
                adapter.notifyDataSetChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                adapter.updateList(holdingArrayList);
                Collections.sort(holdingArrayList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ClientHoldingObject p1 = (ClientHoldingObject) o1;
                        ClientHoldingObject p2 = (ClientHoldingObject) o2;
                        return Double.compare(Double.parseDouble(p1.getQuantity()), Double.parseDouble(p2.getQuantity()));
                    }
                });
                adapter.notifyDataSetChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // filter
    private void filterByName(String text) {
        ArrayList<ClientHoldingObject> filteredList = new ArrayList<>();

        for (ClientHoldingObject item : holdingArrayList) {

            if (item.getSchemeName() != null) {

                if (item.getSchemeName().toLowerCase().startsWith(text.toLowerCase())) {

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