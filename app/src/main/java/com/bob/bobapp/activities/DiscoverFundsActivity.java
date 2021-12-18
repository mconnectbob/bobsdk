package com.bob.bobapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;

import com.bob.bobapp.adapters.CashAdapter;
import com.bob.bobapp.adapters.DiscoverFundListAdapter;

import com.bob.bobapp.adapters.EquityAdapter;
import com.bob.bobapp.adapters.HybridAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.DiscoverFundRequest;
import com.bob.bobapp.api.request_object.DiscoverFundRequestBody;

import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.DiscoverFundResponse;
import com.bob.bobapp.api.response_object.LstRecommandationDebt;
import com.bob.bobapp.api.response_object.TransactionResponseModel;
import com.bob.bobapp.api.response_object.lstRecommandationCash;
import com.bob.bobapp.api.response_object.lstRecommandationEquity;
import com.bob.bobapp.api.response_object.lstRecommandationHybrid;
import com.bob.bobapp.dialog.FilterDialog;
import com.bob.bobapp.dialog.PopupDialog;
import com.bob.bobapp.dialog.SortByDialog;
import com.bob.bobapp.fragments.AddTransactionFragment;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.fragments.QuickTransactionFragment;
import com.bob.bobapp.listener.OnGenericListener;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFundsActivity extends BaseFragment implements onSortItemListener, OnGenericListener {
    private TextView txtDebt, txtEquity, txtCash, txtHybrid, txtFilter, txtSearch, txtSort, txtDisclaimer;
    private View viewDebt, viewEquity, viewCash, viewHybrid;
    private RecyclerView rv, recyclerEquity, recyclerCash, recyclerHybrid;
    private EditText etSearch;
    private APIInterface apiInterface;
    private Util util;
    private ArrayList<LstRecommandationDebt> lstRecommandationDebtArrayList = new ArrayList<>();
    private ArrayList<lstRecommandationEquity> lstRecommandationEquityArrayList = new ArrayList<>();
    private ArrayList<lstRecommandationCash> lstRecommandationCashArrayList = new ArrayList<>();
    private ArrayList<lstRecommandationHybrid> lstRecommandationHybridArrayList = new ArrayList<>();
    private String searchKey = "", status = "1", exploreName;
    private DiscoverFundListAdapter discoverFundListAdapter;
    private EquityAdapter equityAdapter;
    private CashAdapter cashAdapter;
    private HybridAdapter hybridAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_discover_funds, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {
        etSearch = view.findViewById(R.id.etSearch);

        rv = view.findViewById(R.id.rv);
        recyclerEquity = view.findViewById(R.id.recyclerEquity);
        recyclerCash = view.findViewById(R.id.recyclerCash);
        recyclerHybrid = view.findViewById(R.id.recyclerHybrid);

        txtDebt = view.findViewById(R.id.txtDebt);
        viewDebt = view.findViewById(R.id.viewDebt);

        txtEquity = view.findViewById(R.id.txtEquity);
        viewEquity = view.findViewById(R.id.viewEquity);

        txtHybrid = view.findViewById(R.id.txtHybrid);
        viewHybrid = view.findViewById(R.id.viewHybrid);


        txtCash = view.findViewById(R.id.txtCash);
        viewCash = view.findViewById(R.id.viewCash);
        txtFilter = view.findViewById(R.id.txtFilter);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtSort = view.findViewById(R.id.txtSort);
        txtDisclaimer = view.findViewById(R.id.txtDisclaimer);
    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        BOBActivity.imgInfo.setOnClickListener(this);
        txtEquity.setOnClickListener(this);
        txtDebt.setOnClickListener(this);
        txtCash.setOnClickListener(this);
        txtFilter.setOnClickListener(this);
        txtSearch.setOnClickListener(this);
        txtSort.setOnClickListener(this);
        txtHybrid.setOnClickListener(this);
        txtDisclaimer.setOnClickListener(this);

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
                if (status.equalsIgnoreCase("1")) {
                    filter(s.toString());
                } else if (status.equalsIgnoreCase("2")) {
                    filterEquity(s.toString());
                } else if (status.equalsIgnoreCase("3")) {
                    filterCash(s.toString());
                } else {
                    filterCash(s.toString());
                }
            }
        });
    }

    @Override
    public void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.imgInfo.setVisibility(View.VISIBLE);


        if (getArguments() != null) {
            exploreName = getArguments().getString("exploreName");
        }

//        Toast.makeText(getContext(), exploreName, Toast.LENGTH_LONG).show();

        rv.setNestedScrollingEnabled(false);
        recyclerEquity.setNestedScrollingEnabled(false);
        recyclerCash.setNestedScrollingEnabled(false);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.title.setText("Baroda Select Schemes");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_SIP_SWP_STP_DUE);
        util = new Util(context);
        getApiCall();


    }

    // api calling
    private void getApiCall() {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        DiscoverFundRequestBody discoverFundRequestBody = new DiscoverFundRequestBody();
        discoverFundRequestBody.setClientcode(Integer.parseInt(authenticateResponse.getUserCode()));

        DiscoverFundRequest model = new DiscoverFundRequest();
        model.setRequestBodyObject(discoverFundRequestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);

        apiInterface.getDiscoverFundApiCall(model).enqueue(new Callback<DiscoverFundResponse>() {
            @Override
            public void onResponse(Call<DiscoverFundResponse> call, Response<DiscoverFundResponse> response) {

                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    lstRecommandationDebtArrayList = response.body().getLstRecommandationDebt();
                    lstRecommandationEquityArrayList = response.body().getLstRecommandationEquity();
                    lstRecommandationCashArrayList = response.body().getLstRecommandationCash();
                    lstRecommandationHybridArrayList = response.body().getLstRecommandationHybrid();


                    if (exploreName.equalsIgnoreCase("Equity Funds")) {
                        status = "1";
                        viewEquity.setBackgroundColor(Color.parseColor("#f57222"));
                        viewDebt.setBackgroundColor(Color.parseColor("#696969"));
                        viewCash.setBackgroundColor(Color.parseColor("#696969"));
                        viewHybrid.setBackgroundColor(Color.parseColor("#696969"));

                        txtEquity.setTextColor(Color.parseColor("#211E0E"));
                        txtDebt.setTextColor(Color.parseColor("#696969"));
                        txtCash.setTextColor(Color.parseColor("#696969"));
                        txtHybrid.setTextColor(Color.parseColor("#696969"));

                        recyclerEquity.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        recyclerCash.setVisibility(View.GONE);
                        recyclerHybrid.setVisibility(View.GONE);

                        setEquityAdapter();

                    } else if (exploreName.equalsIgnoreCase("Debt Funds")) {
                        status = "2";
                        viewDebt.setBackgroundColor(Color.parseColor("#f57222"));
                        viewEquity.setBackgroundColor(Color.parseColor("#696969"));
                        viewCash.setBackgroundColor(Color.parseColor("#696969"));
                        viewHybrid.setBackgroundColor(Color.parseColor("#696969"));

                        txtDebt.setTextColor(Color.parseColor("#211E0E"));
                        txtEquity.setTextColor(Color.parseColor("#696969"));
                        txtCash.setTextColor(Color.parseColor("#696969"));
                        txtHybrid.setTextColor(Color.parseColor("#696969"));

                        recyclerEquity.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        recyclerCash.setVisibility(View.GONE);
                        recyclerHybrid.setVisibility(View.GONE);
                        setAdapter();

                    } else if (exploreName.equalsIgnoreCase("Liquid Funds")) {
                        status = "3";
                        viewCash.setBackgroundColor(Color.parseColor("#f57222"));
                        viewEquity.setBackgroundColor(Color.parseColor("#696969"));
                        viewDebt.setBackgroundColor(Color.parseColor("#696969"));
                        viewHybrid.setBackgroundColor(Color.parseColor("#696969"));

                        txtCash.setTextColor(Color.parseColor("#211E0E"));
                        txtDebt.setTextColor(Color.parseColor("#696969"));
                        txtEquity.setTextColor(Color.parseColor("#696969"));
                        txtHybrid.setTextColor(Color.parseColor("#696969"));

                        recyclerEquity.setVisibility(View.GONE);
                        rv.setVisibility(View.GONE);
                        recyclerCash.setVisibility(View.VISIBLE);
                        recyclerHybrid.setVisibility(View.GONE);

                        setCashAdapter();
                    } else {
                        status = "4";
                        viewCash.setBackgroundColor(Color.parseColor("#696969"));
                        viewEquity.setBackgroundColor(Color.parseColor("#696969"));
                        viewDebt.setBackgroundColor(Color.parseColor("#696969"));
                        viewHybrid.setBackgroundColor(Color.parseColor("#f57222"));

                        txtCash.setTextColor(Color.parseColor("#696969"));
                        txtDebt.setTextColor(Color.parseColor("#696969"));
                        txtEquity.setTextColor(Color.parseColor("#696969"));
                        txtHybrid.setTextColor(Color.parseColor("#211E0E"));

                        recyclerEquity.setVisibility(View.GONE);
                        rv.setVisibility(View.GONE);
                        recyclerCash.setVisibility(View.GONE);
                        recyclerHybrid.setVisibility(View.VISIBLE);

                        setHybridAdapter();
                    }
                }

            }

            @Override
            public void onFailure(Call<DiscoverFundResponse> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }


    // set debt adapter
    private void setAdapter() {
        discoverFundListAdapter = new DiscoverFundListAdapter(context, lstRecommandationDebtArrayList) {
            @Override
            public void getDetail(Fragment fragment) {
                replaceFragment(fragment);
            }
        };
        rv.setAdapter(discoverFundListAdapter);

    }

    // replace fragment
    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }


    // set equity adapter
    private void setEquityAdapter() {
        equityAdapter = new EquityAdapter(context, lstRecommandationEquityArrayList) {
            @Override
            public void getDetail(Fragment fragment) {
                replaceFragment(fragment);
            }
        };
        recyclerEquity.setAdapter(equityAdapter);
    }

    // set cash adapter
    private void setCashAdapter() {
        cashAdapter = new CashAdapter(context, lstRecommandationCashArrayList) {
            @Override
            public void getDetail(Fragment fragment) {
                replaceFragment(fragment);
            }
        };
        recyclerCash.setAdapter(cashAdapter);
    }

    // set cash adapter
    private void setHybridAdapter() {
        hybridAdapter = new HybridAdapter(context, lstRecommandationHybridArrayList) {
            @Override
            public void getDetail(Fragment fragment) {
                replaceFragment(fragment);
            }
        };
        recyclerHybrid.setAdapter(hybridAdapter);
    }

    @Override
    public void setIcon(Util util) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.txtDisclaimer)
        {
            showDisclaimerDialog();
        } else if (id == R.id.imgInfo) {
            replaceFragment(new InformationActivity());
        } else if (id == R.id.txtDebt) {
            status = "2";
            viewDebt.setBackgroundColor(Color.parseColor("#FF5C34"));
            viewEquity.setBackgroundColor(Color.parseColor("#696969"));
            viewCash.setBackgroundColor(Color.parseColor("#696969"));
            viewHybrid.setBackgroundColor(Color.parseColor("#696969"));

            txtDebt.setTextColor(Color.parseColor("#211E0E"));
            txtEquity.setTextColor(Color.parseColor("#696969"));
            txtCash.setTextColor(Color.parseColor("#696969"));
            txtHybrid.setTextColor(Color.parseColor("#696969"));

            recyclerEquity.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            recyclerCash.setVisibility(View.GONE);
            recyclerHybrid.setVisibility(View.GONE);

            setAdapter();
        } else if (id == R.id.txtEquity) {
            status = "1";
            viewEquity.setBackgroundColor(Color.parseColor("#FF5C34"));
            viewDebt.setBackgroundColor(Color.parseColor("#696969"));
            viewCash.setBackgroundColor(Color.parseColor("#696969"));
            viewHybrid.setBackgroundColor(Color.parseColor("#696969"));

            txtEquity.setTextColor(Color.parseColor("#211E0E"));
            txtDebt.setTextColor(Color.parseColor("#696969"));
            txtCash.setTextColor(Color.parseColor("#696969"));
            txtHybrid.setTextColor(Color.parseColor("#696969"));

            recyclerEquity.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            recyclerCash.setVisibility(View.GONE);
            recyclerHybrid.setVisibility(View.GONE);

            setEquityAdapter();

        } else if (id == R.id.txtCash) {
            status = "3";
            viewCash.setBackgroundColor(Color.parseColor("#FF5C34"));
            viewEquity.setBackgroundColor(Color.parseColor("#696969"));
            viewDebt.setBackgroundColor(Color.parseColor("#696969"));
            viewHybrid.setBackgroundColor(Color.parseColor("#696969"));

            txtCash.setTextColor(Color.parseColor("#211E0E"));
            txtDebt.setTextColor(Color.parseColor("#696969"));
            txtEquity.setTextColor(Color.parseColor("#696969"));
            txtHybrid.setTextColor(Color.parseColor("#696969"));

            recyclerEquity.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            recyclerCash.setVisibility(View.VISIBLE);
            recyclerHybrid.setVisibility(View.GONE);

            setCashAdapter();
        } else if (id == R.id.txtHybrid) {
            status = "4";
            viewCash.setBackgroundColor(Color.parseColor("#696969"));
            viewEquity.setBackgroundColor(Color.parseColor("#696969"));
            viewDebt.setBackgroundColor(Color.parseColor("#696969"));
            viewHybrid.setBackgroundColor(Color.parseColor("#FF5C34"));

            txtCash.setTextColor(Color.parseColor("#696969"));
            txtDebt.setTextColor(Color.parseColor("#696969"));
            txtEquity.setTextColor(Color.parseColor("#696969"));
            txtHybrid.setTextColor(Color.parseColor("#211E0E"));

            recyclerEquity.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            recyclerCash.setVisibility(View.GONE);
            recyclerHybrid.setVisibility(View.VISIBLE);

            setHybridAdapter();
        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (id == R.id.txtFilter) {
            if (status.equalsIgnoreCase("1")) {
                openFilterDialog(status, lstRecommandationEquityArrayList);
            }

            if (status.equalsIgnoreCase("2")) {
                openFilterDialog(status, lstRecommandationDebtArrayList);
            }

            if (status.equalsIgnoreCase("3")) {
                openFilterDialog(status, lstRecommandationCashArrayList);
            }
            if (status.equalsIgnoreCase("4")) {
                openFilterDialog(status, lstRecommandationHybridArrayList);
            }


        } else if (id == R.id.txtSearch) {
            replaceFragment(new QuickTransactionFragment());
        } else if (id == R.id.txtSort) {
            openAllPlanDialog();
        }

    }


    // filter
    private void filter(String text) {

        ArrayList<LstRecommandationDebt> filteredList = new ArrayList<>();

        for (LstRecommandationDebt item : lstRecommandationDebtArrayList) {

            if (item.getFundName() != null) {

                if (item.getFundName().toLowerCase().startsWith(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }
        try {
            discoverFundListAdapter.updateList(filteredList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // equity filter
    private void filterEquity(String text) {

        ArrayList<lstRecommandationEquity> filteredList = new ArrayList<>();

        for (lstRecommandationEquity item : lstRecommandationEquityArrayList) {

            if (item.getFundName() != null) {

                if (item.getFundName().toLowerCase().startsWith(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }

        try {
            equityAdapter.updateList(filteredList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // cash filter
    private void filterCash(String text) {

        ArrayList<lstRecommandationCash> filteredList = new ArrayList<>();

        for (lstRecommandationCash item : lstRecommandationCashArrayList) {

            if (item.getFundName() != null) {

                if (item.getFundName().toLowerCase().startsWith(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }
        try {
            cashAdapter.updateList(filteredList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // plan Dialog
    private void openAllPlanDialog() {
        SortByDialog allPlanDialog = new SortByDialog(getContext(), this, "3M Value");
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onSortItemListener(String name) {
        if (name.equalsIgnoreCase("1")) {
            if (status.equalsIgnoreCase("1")) {
                if (lstRecommandationEquityArrayList != null && lstRecommandationEquityArrayList.size() > 0) {
                    Collections.sort(lstRecommandationEquityArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            LstRecommandationDebt p1 = (LstRecommandationDebt) o1;
                            LstRecommandationDebt p2 = (LstRecommandationDebt) o2;
                            return p1.getFundName().compareToIgnoreCase(p2.getFundName());
                        }
                    });
                    equityAdapter.notifyDataSetChanged();
                }
            }
            if (status.equalsIgnoreCase("2")) {
                if (lstRecommandationDebtArrayList != null && lstRecommandationDebtArrayList.size() > 0) {
                    Collections.sort(lstRecommandationDebtArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            lstRecommandationEquity p1 = (lstRecommandationEquity) o1;
                            lstRecommandationEquity p2 = (lstRecommandationEquity) o2;
                            return p1.getFundName().compareToIgnoreCase(p2.getFundName());
                        }
                    });
                    discoverFundListAdapter.notifyDataSetChanged();
                }
            }
            if (status.equalsIgnoreCase("3")) {
                if (lstRecommandationCashArrayList != null && lstRecommandationCashArrayList.size() > 0) {
                    Collections.sort(lstRecommandationCashArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            lstRecommandationCash p1 = (lstRecommandationCash) o1;
                            lstRecommandationCash p2 = (lstRecommandationCash) o2;
                            return p1.getFundName().compareToIgnoreCase(p2.getFundName());
                        }
                    });
                    cashAdapter.notifyDataSetChanged();
                }
            }
            if (status.equalsIgnoreCase("4")) {
                if (lstRecommandationHybridArrayList != null && lstRecommandationHybridArrayList.size() > 0) {
                    Collections.sort(lstRecommandationHybridArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            lstRecommandationCash p1 = (lstRecommandationCash) o1;
                            lstRecommandationCash p2 = (lstRecommandationCash) o2;
                            return p1.getFundName().compareToIgnoreCase(p2.getFundName());
                        }
                    });
                    hybridAdapter.notifyDataSetChanged();
                }
            }

        } else {
            if (status.equalsIgnoreCase("1")) {
                if (lstRecommandationDebtArrayList != null && lstRecommandationDebtArrayList.size() > 0) {
                    Collections.sort(lstRecommandationDebtArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            LstRecommandationDebt p1 = (LstRecommandationDebt) o1;
                            LstRecommandationDebt p2 = (LstRecommandationDebt) o2;
                            return Double.compare(Double.parseDouble(p1.getReturnIn3Month()), Double.parseDouble(p2.getReturnIn3Month()));
                        }
                    });
                    discoverFundListAdapter.notifyDataSetChanged();
                }
            }
            if (status.equalsIgnoreCase("2")) {
                if (lstRecommandationEquityArrayList != null && lstRecommandationEquityArrayList.size() > 0) {
                    Collections.sort(lstRecommandationEquityArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            lstRecommandationEquity p1 = (lstRecommandationEquity) o1;
                            lstRecommandationEquity p2 = (lstRecommandationEquity) o2;
                            return Double.compare(Double.parseDouble(p1.getReturnIn3Month()), Double.parseDouble(p2.getReturnIn3Month()));
                        }
                    });
                    equityAdapter.notifyDataSetChanged();
                }
            }
            if (status.equalsIgnoreCase("3")) {
                if (lstRecommandationCashArrayList != null && lstRecommandationCashArrayList.size() > 0) {
                    Collections.sort(lstRecommandationCashArrayList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            lstRecommandationCash p1 = (lstRecommandationCash) o1;
                            lstRecommandationCash p2 = (lstRecommandationCash) o2;
                            return Double.compare(Double.parseDouble(p1.getReturnIn3Month()), Double.parseDouble(p2.getReturnIn3Month()));
                        }
                    });
                    cashAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    // plan Dialog
    private void openFilterDialog(String status, ArrayList<? extends Object> arrayList) {
        FilterDialog allPlanDialog = new FilterDialog(getContext(), arrayList, status, this);
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }


    @Override
    public void onItemDeleteListener(String name, String fundCode, String status, String bankSource, String accountNumber) {
        if (status.equalsIgnoreCase("2")) {
            ArrayList<LstRecommandationDebt> filteredList = new ArrayList<>();

            for (LstRecommandationDebt item : lstRecommandationDebtArrayList) {

                if (item.getClassification() != null) {

                    if (item.getClassification().equalsIgnoreCase(name)) {

                        filteredList.add(item);
                    }
                }
            }
            try {
                discoverFundListAdapter.updateList(filteredList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (status.equalsIgnoreCase("1")) {
            ArrayList<lstRecommandationEquity> filteredList = new ArrayList<>();

            for (lstRecommandationEquity item : lstRecommandationEquityArrayList) {

                if (item.getClassification() != null) {

                    if (item.getClassification().equalsIgnoreCase(name)) {

                        filteredList.add(item);
                    }
                }
            }
            try {
                equityAdapter.updateList(filteredList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (status.equalsIgnoreCase("3")) {
            ArrayList<lstRecommandationCash> filteredList = new ArrayList<>();

            for (lstRecommandationCash item : lstRecommandationCashArrayList) {

                if (item.getClassification() != null) {

                    if (item.getClassification().equalsIgnoreCase(name)) {

                        filteredList.add(item);
                    }
                }
            }
            try {
                cashAdapter.updateList(filteredList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (status.equalsIgnoreCase("4")) {
            ArrayList<lstRecommandationHybrid> filteredList = new ArrayList<>();

            for (lstRecommandationHybrid item : lstRecommandationHybridArrayList) {

                if (item.getClassification() != null) {

                    if (item.getClassification().equalsIgnoreCase(name)) {

                        filteredList.add(item);
                    }
                }
            }
            try {
                hybridAdapter.updateList(filteredList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // dialog
    private void showDisclaimerDialog() {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.webview_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        AppCompatButton btnMoveToCart = dialog.findViewById(R.id.btnMoveToCart);
        AppCompatButton btnAddMore = dialog.findViewById(R.id.btnAddMore);
        WebView webview=dialog.findViewById(R.id.webview);

        webview.setWebViewClient(new MyWebViewClient());

        String ENROLLMENT_URL = "https://barodawealth.com/MoneywarePortal/Portal/TnC/Disclaimer1.html";

        webview.loadUrl(ENROLLMENT_URL);

        btnMoveToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public final class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Intent i = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                i = new Intent(Intent.ACTION_VIEW, request.getUrl());
            }
            startActivity(i);
            return true;
        }
    }


}
