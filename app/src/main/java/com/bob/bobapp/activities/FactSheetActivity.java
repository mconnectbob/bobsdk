package com.bob.bobapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.FactsheetAdapter;
import com.bob.bobapp.adapters.SectorWeightsChartListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.FactsheetSchemePerformanceData;
import com.bob.bobapp.api.bean.SectorAllocationObject;
import com.bob.bobapp.api.bean.StockAllocationObject;
import com.bob.bobapp.api.request_object.FundDetailRequest;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.FundDetailResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.MyValueFormatter;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class FactSheetActivity extends BaseFragment {

    private TextView tvSchemeName, tvSchemeCategory, tvNAV, btnSIP, btnBuy, tvLaunchDate, tvSchemeType,
            tvInvestmentStrategyObjective, tvFundManagerName, txtYear, txtSchemeIndexName;

    private RecyclerView rvSchemePerformance, rvStockAllocation;

    private Util util;

    private Context context;

    private BarChart barChartYearOnYearPerformance;

    private HorizontalBarChart horizontalBarChartSectorAllocation;

    private String commanScriptCode = "", schemeName = "", factsheetStatus = "";
    private LinearLayout linearAllData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_fact_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        callFactsheetAPI();
    }

    @Override
    public void getIds(View view) {

        linearAllData = view.findViewById(R.id.linearAllData);

        tvSchemeName = view.findViewById(R.id.tv_scheme_name);

        tvSchemeCategory = view.findViewById(R.id.tv_category);

        tvNAV = view.findViewById(R.id.tv_nav);

        btnSIP = view.findViewById(R.id.btn_sip);

        btnBuy = view.findViewById(R.id.btn_buy);

        tvLaunchDate = view.findViewById(R.id.tv_launch_date);

        tvSchemeType = view.findViewById(R.id.tv_scheme_type);

        tvInvestmentStrategyObjective = view.findViewById(R.id.tv_investment_strategy_objective);

        tvFundManagerName = view.findViewById(R.id.tv_fund_manager_name);

        barChartYearOnYearPerformance = view.findViewById(R.id.bar_chart_year_on_year_performance);

        rvSchemePerformance = view.findViewById(R.id.rv_scheme_performance);

        horizontalBarChartSectorAllocation = view.findViewById(R.id.bar_chart_sector_allocation);

        rvStockAllocation = view.findViewById(R.id.rv_stock_allocation);
        txtSchemeIndexName = view.findViewById(R.id.txtSchemeIndexName);
        txtYear = view.findViewById(R.id.txtYear);
    }

    @Override
    public void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);

        btnSIP.setOnClickListener(this);

        btnBuy.setOnClickListener(this);
    }

    @Override
    public void initializations() {

        if (getArguments() != null) {

            commanScriptCode = getArguments().getString("commanScriptCode");
            factsheetStatus = getArguments().getString("factsheetStatus");


//            model = new Gson().fromJson(response, ClientHoldingObject.class);
        }
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);

        BOBActivity.llMenu.setVisibility(View.GONE);

        BOBActivity.title.setText("FactSheet");

        setSectorWeightAdapter();
    }

    private void setSectorWeightAdapter() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        SectorWeightsChartListAdapter adpter = new SectorWeightsChartListAdapter(context, display);
    }

    @Override
    public void setIcon(Util util) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.btn_sip) {
            Bundle args = new Bundle();

            args.putString("commanScriptCode", commanScriptCode);
            args.putString("schemeName", schemeName);
            if (factsheetStatus.equalsIgnoreCase("1")) {
                args.putString("status", "2");
            } else {
                args.putString("status", "3");
            }

            Fragment fragment = new SipActivity();

            fragment.setArguments(args);

            replaceFragment(fragment);

        } else if (id == R.id.btn_buy) {
            Bundle args = new Bundle();

            args.putString("commanScriptCode", commanScriptCode);
            args.putString("schemeName", schemeName);
            if (factsheetStatus.equalsIgnoreCase("1")) {
                args.putString("status", "2");
            } else {
                args.putString("status", "3");
            }

            Fragment fragment = new BuyActivity();

            fragment.setArguments(args);

            replaceFragment(fragment);

            // ((BaseContainerFragment) getParentFragment()).clearBackStack();
        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }


    private void callFactsheetAPI() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_FACTSHEET);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setSchemeCode(commanScriptCode);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        FundDetailRequest.createGlobalRequestObject(globalRequestObject);


        apiInterface.getFundDetailsResponse(FundDetailRequest.getGlobalRequestObject()).enqueue(new Callback<FundDetailResponse>() {
            @Override
            public void onResponse(Call<FundDetailResponse> call, Response<FundDetailResponse> response) {

                util.showProgressDialog(context, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));


                if (response.isSuccessful()) {
                    linearAllData.setVisibility(View.VISIBLE);
                    FundDetailResponse fundDetailResponse = response.body();
                    setData(fundDetailResponse);
                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FundDetailResponse> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(FundDetailResponse fundDetailResponse) {

        schemeName = fundDetailResponse.getSchemeName();

        tvSchemeName.setText(fundDetailResponse.getSchemeName());

        tvSchemeCategory.setText(fundDetailResponse.getCategory());

        String asOnDate = util.formatDateForFactsheet(fundDetailResponse.getNAVDate());

        double d = Double.parseDouble(String.valueOf(fundDetailResponse.getNAV()));

        d = util.truncateDecimal(d).doubleValue();

        tvNAV.setText(String.valueOf("₹ " + d) + " (as on " + asOnDate + ")");

        String launchDate = util.formatDateForFactsheet(fundDetailResponse.getLaunchDate());

        tvLaunchDate.setText(launchDate);

        tvSchemeType.setText(fundDetailResponse.getSchemeType());

        tvInvestmentStrategyObjective.setText(fundDetailResponse.getFundObjectives());

        tvFundManagerName.setText(fundDetailResponse.getFundManager());

        txtYear.setText("(Scheme Vs" + " " + fundDetailResponse.getBenchmarkName() + " )");
        txtSchemeIndexName.setText(fundDetailResponse.getBenchmarkName());

        createYearOnYearPerformanceBarChart(fundDetailResponse);
    }

    private void createYearOnYearPerformanceBarChart(FundDetailResponse fundDetailResponse) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        entries.add(new BarEntry(1, fundDetailResponse.getReturns1Year()));
        entries.add(new BarEntry(2, fundDetailResponse.getReturns3Year()));
        entries.add(new BarEntry(3, fundDetailResponse.getReturns5Year()));
        /*entries.add(new BarEntry(4, fundDetailResponse.getBenchmark1Year()));
        entries.add(new BarEntry(5, fundDetailResponse.getBenchmark3Year()));
        entries.add(new BarEntry(6, fundDetailResponse.getBenchmark5Year()));*/

        labels.add("");
        labels.add("1Y");
        labels.add("3Y");
        labels.add("5Y");
        /*labels.add("");
        labels.add("");
        labels.add("");*/
        colors.add(context.getResources().getColor(R.color.color_bob_style_0));

        colors.add(context.getResources().getColor(R.color.color_bob_style_1));

        colors.add(context.getResources().getColor(R.color.color_bob_style_2));

//        colors.add(context.getResources().getColor(R.color.progressTintEquity));
//        colors.add(context.getResources().getColor(R.color.progressTintEquity));
//        colors.add(context.getResources().getColor(R.color.progressTintEquity));
//        colors.add(context.getResources().getColor(R.color.progressTintEquity));
//        colors.add(context.getResources().getColor(R.color.progressTintEquity));
//        colors.add(context.getResources().getColor(R.color.progressTintEquity));


        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(colors);
        dataset.setValueFormatter(new MyValueFormatter());


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataset);

        BarData data = new BarData(dataset);

        data.setBarWidth(0.2f);

        barChartYearOnYearPerformance.setData(data);

        barChartYearOnYearPerformance.setDoubleTapToZoomEnabled(false);
        barChartYearOnYearPerformance.setTouchEnabled(false);

        barChartYearOnYearPerformance.getDescription().setEnabled(false);

        barChartYearOnYearPerformance.animateY(5000);

        barChartYearOnYearPerformance.getAxisRight().setDrawGridLines(false);

        barChartYearOnYearPerformance.getAxisLeft().setDrawGridLines(false);

        barChartYearOnYearPerformance.getXAxis().setDrawGridLines(false);

        barChartYearOnYearPerformance.getLegend().setEnabled(false);

        barChartYearOnYearPerformance.setDragEnabled(true);


        XAxis xAxis = barChartYearOnYearPerformance.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        //xAxis.setAxisMinimum(0);

        YAxis rightYAxis = barChartYearOnYearPerformance.getAxisRight();
        rightYAxis.setEnabled(false);

        createAndSetSchemePerformanceData(fundDetailResponse);
    }

    private void createAndSetSchemePerformanceData(FundDetailResponse fundDetailResponse) {

        ArrayList<FactsheetSchemePerformanceData> arrayList = new ArrayList<FactsheetSchemePerformanceData>();

        FactsheetSchemePerformanceData factsheetSchemePerformanceData = new FactsheetSchemePerformanceData();

        factsheetSchemePerformanceData.setColumnOne("1 Month %");
        factsheetSchemePerformanceData.setColumnTwo(String.valueOf(fundDetailResponse.getReturns1Month()));
        factsheetSchemePerformanceData.setColumnThree(String.valueOf(fundDetailResponse.getBenchmark1Month()));
        arrayList.add(factsheetSchemePerformanceData);

        factsheetSchemePerformanceData = new FactsheetSchemePerformanceData();
        factsheetSchemePerformanceData.setColumnOne("3 Month %");
        factsheetSchemePerformanceData.setColumnTwo(String.valueOf(fundDetailResponse.getReturns3Month()));
        factsheetSchemePerformanceData.setColumnThree(String.valueOf(fundDetailResponse.getBenchmark3Month()));
        arrayList.add(factsheetSchemePerformanceData);

        factsheetSchemePerformanceData = new FactsheetSchemePerformanceData();
        factsheetSchemePerformanceData.setColumnOne("6 Month %");
        factsheetSchemePerformanceData.setColumnTwo(String.valueOf(fundDetailResponse.getReturns6Month()));
        factsheetSchemePerformanceData.setColumnThree(String.valueOf(fundDetailResponse.getBenchmark6Month()));
        arrayList.add(factsheetSchemePerformanceData);

        factsheetSchemePerformanceData = new FactsheetSchemePerformanceData();
        factsheetSchemePerformanceData.setColumnOne("12 Month %");
        factsheetSchemePerformanceData.setColumnTwo(String.valueOf(fundDetailResponse.getReturns1Year()));
        factsheetSchemePerformanceData.setColumnThree(String.valueOf(fundDetailResponse.getBenchmark1Year()));
        arrayList.add(factsheetSchemePerformanceData);

        setAdapter(arrayList, fundDetailResponse);
    }

    private void setAdapter(ArrayList<FactsheetSchemePerformanceData> arrayList, FundDetailResponse fundDetailResponse) {

        FactsheetAdapter adapter = new FactsheetAdapter(context, arrayList);

        rvSchemePerformance.setAdapter(adapter);

        createSectorAllocationBarChart(fundDetailResponse);
    }

    private void createSectorAllocationBarChart(FundDetailResponse fundDetailResponse) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("");

        if (fundDetailResponse.getSectorAllocationResponseCollection().size() > 0) {

            for (int i = 0; i < fundDetailResponse.getSectorAllocationResponseCollection().size(); i++) {

                SectorAllocationObject sectorAllocationObject = fundDetailResponse.getSectorAllocationResponseCollection().get(i);

                entries.add(new BarEntry(i + 1, Float.parseFloat(sectorAllocationObject.getIndPercAllocation())));

                labels.add(sectorAllocationObject.getIndustry());
            }

            BarDataSet dataset = new BarDataSet(entries, "");

            dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);


            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(dataset);

            BarData data = new BarData(dataset);

            data.setBarWidth(0.15f);

            horizontalBarChartSectorAllocation.setData(data);

            horizontalBarChartSectorAllocation.getDescription().setEnabled(false);

            horizontalBarChartSectorAllocation.animateY(5000);

            horizontalBarChartSectorAllocation.getAxisLeft().setEnabled(false);
            horizontalBarChartSectorAllocation.getAxisRight().setEnabled(false);

            horizontalBarChartSectorAllocation.getAxisRight().setDrawGridLines(false);
            horizontalBarChartSectorAllocation.getAxisLeft().setDrawGridLines(false);

            horizontalBarChartSectorAllocation.getXAxis().setDrawGridLines(false);

            horizontalBarChartSectorAllocation.getLegend().setEnabled(false);

            XAxis xAxis = horizontalBarChartSectorAllocation.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setGranularity(1);
            xAxis.setGranularityEnabled(true);

            YAxis rightYAxis = horizontalBarChartSectorAllocation.getAxisRight();
            rightYAxis.setEnabled(false);

            horizontalBarChartSectorAllocation.getLayoutParams().height = entries.size() * 100;

        }

        createAndSetStockAllocationData(fundDetailResponse);
    }

    private void createAndSetStockAllocationData(FundDetailResponse fundDetailResponse) {

        ArrayList<FactsheetSchemePerformanceData> arrayList = new ArrayList<FactsheetSchemePerformanceData>();

        for (int i = 0; i < fundDetailResponse.getStockAllocationResponseCollection().size(); i++) {

            StockAllocationObject stockAllocationObject = fundDetailResponse.getStockAllocationResponseCollection().get(i);

            FactsheetSchemePerformanceData factsheetSchemePerformanceData = new FactsheetSchemePerformanceData();

            factsheetSchemePerformanceData.setColumnOne(stockAllocationObject.getCompany());
            factsheetSchemePerformanceData.setColumnTwo(stockAllocationObject.getType());
            factsheetSchemePerformanceData.setColumnThree(stockAllocationObject.getCompPercAllocation());
            arrayList.add(factsheetSchemePerformanceData);
        }

        setAdapterStockAllocation(arrayList, fundDetailResponse);
    }

    private void setAdapterStockAllocation(ArrayList<FactsheetSchemePerformanceData> arrayList, FundDetailResponse fundDetailResponse) {

        FactsheetAdapter adapter = new FactsheetAdapter(context, arrayList);

        rvStockAllocation.setAdapter(adapter);
    }

}