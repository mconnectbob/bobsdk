package com.bob.bobapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.AssetAllocationAdpter;
import com.bob.bobapp.adapters.AssetAllocationLegendAdapter;
import com.bob.bobapp.adapters.ProductAllocationAdapter;
import com.bob.bobapp.adapters.ProductNameAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.bean.PortfolioPerformanceDetailCollection;
import com.bob.bobapp.api.bean.ProductValueBean;
import com.bob.bobapp.api.request_object.AssetAllocationPerformanceRequestObject;
import com.bob.bobapp.api.request_object.AssetAllocationRequestObject;
import com.bob.bobapp.api.request_object.ClientHoldingRequest;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.PortfolioPerformanceRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AssetAllocationPerformanceResponseObject;
import com.bob.bobapp.api.response_object.AssetAllocationResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.ClientHoldingResponse;
import com.bob.bobapp.api.response_object.InvestmentMaturityModel;
import com.bob.bobapp.api.response_object.PortfolioPerformanceResponseObject;
import com.bob.bobapp.fragments.AddTransactionFragment;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.fragments.DashboardFragment;
import com.bob.bobapp.fragments.ProfileFragment;
import com.bob.bobapp.fragments.ReportFragment;
import com.bob.bobapp.fragments.SetUpFragment;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.LabelFormatter;
import com.bob.bobapp.utility.MyValueFormatter;
import com.bob.bobapp.utility.PopUpClass;
import com.bob.bobapp.utility.ProductAllocationObject;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WINDOW_SERVICE;

public class PortfolioAnalytics extends BaseFragment implements onSortItemListener {
    private ArrayList<ProductAllocationObject> productAllocationObjectArrayList;

    private ArrayList<ClientHoldingObject> clientHoldingObjectArrayList = new ArrayList<>();
    private ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList = new ArrayList<>();
    private ArrayList<AssetAllocationPerformanceResponseObject> AssetAllocationPerformanceArrayList = new ArrayList<>();

    private TextView tvTitle, tvUserHeader, tvBellHeader, tvCartHeader, tvMenu, btn_view_holding,
            tvCurrentValue, tvInvestedAmount, tvGainLoss, tvDevidendInterest, tvNetGain, tvIRR, txtAssestAllocationPercentage, txtAssestAllocationMarketValue,
            tvNetGainPercent, txtInvestNow, txtAssestAllocationXIRR;

    private ImageView img_right_arrow, imgLoadAssetAllocation, imgLoadMutualFund, imgLoadMutualFundAMC;

    private DrawerLayout drawerLayout;

    private Util util;

    private Context context;

    private RecyclerView rvAssetAllocationLegend, rvAssetAllocation, rvProductName, rvMutulFundSchemeCategory, rvMutualFundAMCExposure;

    private PieChart pieChartProductAllocation, pieChartMutualFundSchemeCategory;

    private BarChart barChart, portfolioBarChart, barChartAssetAllocation;

    private HorizontalBarChart barChartMutualFundAMCExposure;

    private APIInterface apiInterface;

    private ArrayList<ClientHoldingObject> holdingArrayList = new ArrayList<>();

    private DecimalFormat formatter;

    private ImageView imgEye, imgEyeHide;

    private double finalGain = 0, percentValue = 0, investedsAmount = 0, currentAmount = 0;

    private String finalIRR = "", amcStatus = "";

    private int screenWidth = 0, screenHeight = 0;

    int DRAWER_ITEMS_OPEN_TIME = 200;

    private LinearLayout drawerMenuView, linearAssetAllocation, linearMutualFund, linearMutualFundAMC;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_portfolio_analytics, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_HOLDING);


    }

    @Override
    protected void getIds(View view) {
        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    BOBActivity.mTabHost.setCurrentTab(0);
                }

                return true;
            }
        });

        formatter = new DecimalFormat("###,###,###");

        btn_view_holding = view.findViewById(R.id.btn_view_holding);

        img_right_arrow = view.findViewById(R.id.img_right_arrow);

        tvCurrentValue = view.findViewById(R.id.tv_current_value);

        tvInvestedAmount = view.findViewById(R.id.tv_invested_amount_value);

        tvGainLoss = view.findViewById(R.id.tv_utilized_gain_or_loss_value);

        tvDevidendInterest = view.findViewById(R.id.tv_utilized_devidend_or_interest_value);

        tvNetGain = view.findViewById(R.id.tv_utilized_net_gain_value);

        tvIRR = view.findViewById(R.id.tv_irr_value);

        tvNetGainPercent = view.findViewById(R.id.tv_utilized_net_gain_percent_value);

        rvAssetAllocation = view.findViewById(R.id.rv_asset_allocation);

        rvProductName = view.findViewById(R.id.rv_product_allocation);

        rvMutulFundSchemeCategory = view.findViewById(R.id.rv_mutual_fund_scheme_category);

        pieChartProductAllocation = view.findViewById(R.id.pie_chart_product_allocation);

        barChartAssetAllocation = view.findViewById(R.id.bar_chart_asset_allocation);

        pieChartMutualFundSchemeCategory = view.findViewById(R.id.pie_chart_mutual_fund_scheme_category);

        barChart = view.findViewById(R.id.bar_chart);

        portfolioBarChart = view.findViewById(R.id.bar_chart_portfolio_performance);

        barChartMutualFundAMCExposure = view.findViewById(R.id.bar_chart_mutual_fund_amc_exposure);

        txtInvestNow = view.findViewById(R.id.txtInvestNow);

        rvMutualFundAMCExposure = view.findViewById(R.id.rv_mutual_fund_amc_exposure);

        rvAssetAllocationLegend = view.findViewById(R.id.rv_asset_allocation_legend);

        imgEye = view.findViewById(R.id.imgEye);
        imgEyeHide = view.findViewById(R.id.imgEyeHide);

        imgLoadAssetAllocation = view.findViewById(R.id.imgLoadAssetAllocation);
        linearAssetAllocation = view.findViewById(R.id.linearAssetAllocation);
        txtAssestAllocationPercentage = view.findViewById(R.id.txtAssestAllocationPercentage);
        txtAssestAllocationMarketValue = view.findViewById(R.id.txtAssestAllocationMarketValue);
        txtAssestAllocationXIRR = view.findViewById(R.id.txtAssestAllocationXIRR);

        imgLoadMutualFund = view.findViewById(R.id.imgLoadMutualFund);
        linearMutualFund = view.findViewById(R.id.linearMutualFund);

        linearMutualFundAMC = view.findViewById(R.id.linearMutualFundAMC);
        imgLoadMutualFundAMC = view.findViewById(R.id.imgLoadMutualFundAMC);
//
//        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//
//        drawerMenuView = (LinearLayout) view.findViewById(R.id.drawerMenuLLayout);

        imgEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgEyeHide.setVisibility(View.VISIBLE);
                    imgEye.setVisibility(View.GONE);
                    String netGain = replaceString(tvGainLoss.getText().toString());
                    //  tvGainLoss.setText(netGain);

                    tvGainLoss.setText("₹X,XX,XXX");
//
//                    // net gain value percent
                    String finaltvNetGainPercent = replaceString(tvNetGainPercent.getText().toString());
                    //tvNetGainPercent.setText(finaltvNetGainPercent);
                    tvNetGainPercent.setText("XX.XX");

                    String ivsAmount = replaceString(tvInvestedAmount.getText().toString());
                    // tvInvestedAmount.setText(ivsAmount);
                    tvInvestedAmount.setText("₹X,XX,XXX");

                    String currentValue = replaceString(tvCurrentValue.getText().toString());
                    // tvCurrentValue.setText(currentValue);
                    tvCurrentValue.setText("₹X,XX,XXX");

                    String irrr = replaceString(tvIRR.getText().toString());
                    // tvIRR.setText(irrr);
                    tvIRR.setText("IRR XX.XX");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        imgEyeHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgEyeHide.setVisibility(View.GONE);
                    imgEye.setVisibility(View.VISIBLE);

                    if (holdingArrayList == null || holdingArrayList.size() == 0) {
                        getHoldingApiCall();
                    } else {
                        setData(holdingArrayList);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

//                tvGainLoss.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(finalGain))));
//                tvNetGainPercent.setText(formatter.format(Double.parseDouble(String.valueOf(percentValue))));
//                tvInvestedAmount.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(investedsAmount))));
//                tvCurrentValue.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(currentAmount))));
//                tvIRR.setText("IRR " + finalIRR + "%");
            }
        });

    }

    @Override
    public void handleListener() {

        btn_view_holding.setOnClickListener(this);

        img_right_arrow.setOnClickListener(this);
        txtInvestNow.setOnClickListener(this);
        imgLoadAssetAllocation.setOnClickListener(this);
        imgLoadMutualFund.setOnClickListener(this);
        imgLoadMutualFundAMC.setOnClickListener(this);


        BOBActivity.imgBack.setOnClickListener(this);
        BOBActivity.tvCartHeader.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.imageViewLogo.setVisibility(View.VISIBLE);
        BOBActivity.llMenu.setVisibility(View.VISIBLE);

        BOBActivity.title.setVisibility(View.GONE);
        BOBActivity.imgBack.setVisibility(View.GONE);

        BOBActivity.llMenu.setOnClickListener(this);

        // manageLeftSideDrawer();

        // BOBActivity.title.setText("My Investments");

        //    BOBActivity.llMenu.setVisibility(View.GONE);
    }

    @Override
    protected void setIcon(Util util) {

        this.util = util;

        FontManager.markAsIconContainer(BOBActivity.tvUserHeader, util.iconFont);

        FontManager.markAsIconContainer(BOBActivity.tvBellHeader, util.iconFont);

        FontManager.markAsIconContainer(BOBActivity.tvCartHeader, util.iconFont);
    }


    // replace stirng
    String replaceString(String string) {
        return string.replaceAll("[0-9]", "X"); // removing all special character.
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.imgLoadMutualFundAMC) {
            amcStatus = "2";
            if (clientHoldingObjectArrayList.size() == 0) {
                getProductAllocationAPIResponse();
                linearMutualFundAMC.setVisibility(View.VISIBLE);
            } else {
                linearMutualFundAMC.setVisibility(View.VISIBLE);
                getMutualFundAMCExposureData();
            }

        } else if (id == R.id.imgLoadMutualFund) {
            amcStatus = "1";
            if (clientHoldingObjectArrayList.size() == 0) {

                getProductAllocationAPIResponse();
            } else {
                linearMutualFund.setVisibility(View.VISIBLE);
                getMutualFundSchemeCategoryData();
            }

        } else if (id == R.id.imgLoadAssetAllocation) {
            if (assetAllocationResponseObjectArrayList.size() == 0) {
                callAssetAllocationAPI();
            } else {
                linearAssetAllocation.setVisibility(View.VISIBLE);
                setAssetAllocationGraph(assetAllocationResponseObjectArrayList, AssetAllocationPerformanceArrayList);
            }

        } else if (id == R.id.llMenu) {

            PopUpClass popUpClass = new PopUpClass();
            popUpClass.showPopupWindow(view, getContext(), this);

        } else if (id == R.id.tvCartHeader) {
            InvestmentCartActivity fragment = new InvestmentCartActivity();

            Bundle bundle = new Bundle();

            bundle.putString(Constants.COMING_FROMS, "Dashboard");

            fragment.setArguments(bundle);

            replaceFragment(fragment);
        } else if (id == R.id.btn_view_holding) {

            replaceFragment(new HoldingsActivity());

        } else if (id == R.id.img_right_arrow) {

            replaceFragment(new HoldingsActivity());

        } else if (id == R.id.imgBack) {
            BOBActivity.mTabHost.setCurrentTab(0);
        } else if (id == R.id.txtInvestNow) {

            replaceFragment(new AddTransactionFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    // api calling
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

                //   util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    holdingArrayList = response.body();
                    getPortfolioPerformanceAPIResponse();

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

    // set data method
    private void setData(ArrayList<ClientHoldingObject> holdingArrayList) {
        ClientHoldingObject clientHoldingObject = holdingArrayList.get(0);

        double netGain = 0;
        double valueOfCoast = 0;
        double marketValue = 0;
        double sumDivendend = 0;

        for (int i = 0; i < holdingArrayList.size(); i++) {
            netGain = netGain + Double.parseDouble(holdingArrayList.get(i).getNetGain());
            valueOfCoast = valueOfCoast + Double.parseDouble(holdingArrayList.get(i).getValueOfCost());
            marketValue = marketValue + Double.parseDouble(holdingArrayList.get(i).getMarketValue());
            sumDivendend = sumDivendend + Double.parseDouble(holdingArrayList.get(i).getDividend());
        }

        investedsAmount = valueOfCoast;
        String InvestedAmount = formatter.format(Double.parseDouble(String.valueOf(valueOfCoast)));
        String finalInvestedAmount = replaceString(InvestedAmount);
        //   tvInvestedAmount.setText("₹" + finalInvestedAmount);
        tvInvestedAmount.setText("₹" + InvestedAmount);

        // current value
        currentAmount = marketValue;
        String currentAmount = formatter.format(Double.parseDouble(String.valueOf(marketValue)));
        String finalCurrentAmount = replaceString(currentAmount);
        tvCurrentValue.setText("₹" + currentAmount);


        tvNetGain.setText(formatter.format(Double.parseDouble("" + netGain)));

        //double percentValue = util.truncateDecimal(Double.parseDouble(clientHoldingObject.getGainLossPercentage())).doubleValue();

        percentValue = (netGain / valueOfCoast) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(percentValue);
        String finaltvNetGainPercent = replaceString(formatted);
        tvNetGainPercent.setText(formatted);


        double gainLossValue = marketValue - valueOfCoast;
        finalGain = marketValue - valueOfCoast;


        String tvGainLossValue = formatter.format(Double.parseDouble(String.valueOf(gainLossValue)));
        String finalTvGainLossValue = replaceString(tvGainLossValue);
        tvGainLoss.setText("₹" + tvGainLossValue);


        //     tvGainLoss.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(gainLossValue))));

        tvDevidendInterest.setText(formatter.format(Double.parseDouble("" + sumDivendend)));


        double irrValue = util.truncateDecimal(Double.parseDouble(clientHoldingObject.getGainLossPercentage())).doubleValue();

        String irrs = df.format(Double.parseDouble(finalIRR));

        tvIRR.setText("IRR " + irrs + "%");


    }


    // assest allocation
    private void callAssetAllocationAPI() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_ASSET_ALLOCATION);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(authenticateResponse.getUserID());

        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setAllocationType("2");

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAccountLevel("0"); //For client

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        AssetAllocationRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getAssetAllocationAPIResponse(AssetAllocationRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<AssetAllocationResponseObject>>() {
            @Override
            public void onResponse(Call<ArrayList<AssetAllocationResponseObject>> call, Response<ArrayList<AssetAllocationResponseObject>> response) {
                // util.showProgressDialog(context, false);
                if (response.isSuccessful()) {
                    assetAllocationResponseObjectArrayList = response.body();

                    getAssetAllocationPerformanceAPIResponse();
                    //   getProductAllocationAPIResponse();

                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<AssetAllocationResponseObject>> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // variable for our bar data set.
    BarDataSet barDataSet1, barDataSet2;

    // array list for storing entries.
    ArrayList barEntries, label;

    public void setAssetAllocationGraph(ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList, ArrayList<AssetAllocationPerformanceResponseObject> AssetAllocationPerformanceArrayList) {

        barDataSet1 = new BarDataSet(getBarEntriesOne(assetAllocationResponseObjectArrayList), "Actual");
        barDataSet1.setColor(context.getResources().getColor(R.color.color_bob_style_0));
        barDataSet1.setValueFormatter(new MyValueFormatter());

        barDataSet2 = new BarDataSet(getBarEntriesTwo(assetAllocationResponseObjectArrayList), "Suggested");
        barDataSet2.setColor(context.getResources().getColor(R.color.color_bob_style_1));
        barDataSet2.setValueFormatter(new MyValueFormatter());

        // below line is to add bar data set to our bar data.
        BarData data = new BarData(barDataSet1, barDataSet2);

        // after adding data to our bar data we
        // are setting that data to our bar chart.
        barChartAssetAllocation.setData(data);


        // below line is to remove description
        // label of our bar chart.
        barChartAssetAllocation.getDescription().setEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = barChartAssetAllocation.getXAxis();

        // below line is to set value formatter to our x-axis and
        // we are adding our days to our x axis.
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getBarLabel(assetAllocationResponseObjectArrayList, AssetAllocationPerformanceArrayList)));

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true);

        barChart.setTouchEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

        // below line is to set position
        // to our x-axis to bottom.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // below line is to set granularity
        // to our x axis labels.
        xAxis.setGranularity(1);

        // below line is to enable
        // granularity to our x axis.
        xAxis.setGranularityEnabled(true);

        // below line is to make our
        // bar chart as draggable.

        // barChartAssetAllocation.setMaxVisibleValueCount(3);

        barChartAssetAllocation.setTouchEnabled(true);
        barChartAssetAllocation.setDoubleTapToZoomEnabled(false);

        barChartAssetAllocation.setDragEnabled(true);

        // below line is to make visible
        // range for our bar chart.
        barChartAssetAllocation.setVisibleXRangeMaximum(3);

        //  barChartAssetAllocation.setActivated(false);
        barChartAssetAllocation.setHighlightPerTapEnabled(false);


        barChartAssetAllocation.setPinchZoom(false);

        barChartAssetAllocation.setScaleEnabled(false);


        // below line is to add bar
        // space to our chart.
        float barSpace = 0.1f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.5f;

        // we are setting width of
        // bar in below line.
        data.setBarWidth(0.15f);

        // below line is to set minimum
        // axis to our chart.
        barChartAssetAllocation.getXAxis().setAxisMinimum(0);

        // below line is to
        // animate our chart.
        barChartAssetAllocation.animateY(5000);

        // below line is to group bars
        // and add spacing to it.
        barChartAssetAllocation.groupBars(0, groupSpace, barSpace);

        barChartAssetAllocation.setDrawGridBackground(false);

        barChartAssetAllocation.getAxisRight().setDrawGridLines(false);
        barChartAssetAllocation.getAxisLeft().setDrawGridLines(false);
        barChartAssetAllocation.getXAxis().setDrawGridLines(false);
        barChartAssetAllocation.getAxisRight().setEnabled(false);
        barChartAssetAllocation.getAxisLeft().setValueFormatter(new MyValueFormatter());
        barChartAssetAllocation.setExtraOffsets(0, 5f, 0, 0);

        double percentValue = 0;
        double marketValue = 0;
        double xirr = 0;
        for (int i = 0; i < assetAllocationResponseObjectArrayList.size(); i++) {
            percentValue = percentValue + Double.parseDouble(assetAllocationResponseObjectArrayList.get(i).getValuePercentage());
            marketValue = marketValue + Double.parseDouble(assetAllocationResponseObjectArrayList.get(i).getValueAmount());
        }

        for (int j = 0; j < AssetAllocationPerformanceArrayList.size(); j++) {
            xirr = xirr + Double.parseDouble(AssetAllocationPerformanceArrayList.get(j).getXIRRPercentage());
        }


        double netGainPercentss = util.truncateDecimal(percentValue).doubleValue();


        txtAssestAllocationPercentage.setText("" + netGainPercentss + "%");

        txtAssestAllocationMarketValue.setText(formatter.format(Double.parseDouble(String.valueOf(marketValue))));

        double d = xirr;

        d = util.truncateDecimal(d).doubleValue();


        txtAssestAllocationXIRR.setText(d + "%");

        // below line is to invalidate
        // our bar chart.
        barChartAssetAllocation.invalidate();
    }

    private ArrayList getBarLabel(ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList, ArrayList<AssetAllocationPerformanceResponseObject> AssetAllocationPerformanceArrayList) {

        label = new ArrayList();

        for (int i = 0; i < assetAllocationResponseObjectArrayList.size(); i++) {

            AssetAllocationResponseObject assetAllocationResponseObject = assetAllocationResponseObjectArrayList.get(i);

            label.add(assetAllocationResponseObject.getAssetClassName());

            System.out.println("ASSET NAME: " + assetAllocationResponseObject.getAssetClassName());
        }

        setAssetAllocationLegendAdapter(assetAllocationResponseObjectArrayList, AssetAllocationPerformanceArrayList);

        return label;
    }

    // array list for first set
    private ArrayList<BarEntry> getBarEntriesOne(ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList) {

        barEntries = new ArrayList<>();

        for (int i = 0; i < assetAllocationResponseObjectArrayList.size(); i++) {

            AssetAllocationResponseObject assetAllocationResponseObject = assetAllocationResponseObjectArrayList.get(i);

            if (assetAllocationResponseObject.getValuePercentage() != null && !assetAllocationResponseObject.getValuePercentage().equals("")) {

                double doubleValue = Double.parseDouble(assetAllocationResponseObject.getValuePercentage());

                barEntries.add(new BarEntry(i + 1, Float.parseFloat(String.format("%.02f", doubleValue))));

                System.out.println("DOUBLE VALUE getValuePercentage: " + String.format("%.02f", doubleValue));
            }
        }

        return barEntries;
    }

    // array list for second set.
    private ArrayList<BarEntry> getBarEntriesTwo(ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList) {

        barEntries = new ArrayList<>();

        for (int i = 0; i < assetAllocationResponseObjectArrayList.size(); i++) {

            AssetAllocationResponseObject assetAllocationResponseObject = assetAllocationResponseObjectArrayList.get(i);

            if (assetAllocationResponseObject.getTargetValuePercentage() != null && !assetAllocationResponseObject.getTargetValuePercentage().equals("")) {

                double doubleValue = Double.parseDouble(assetAllocationResponseObject.getTargetValuePercentage());

                barEntries.add(new BarEntry(i + 1, Float.parseFloat(String.format("%.02f", doubleValue))));

                System.out.println("DOUBLE VALUE getTargetValuePercentage: " + String.format("%.02f", doubleValue));
            }
        }
        return barEntries;
    }

    private void setAssetAllocationAdapter(ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList) {

        AssetAllocationAdpter assetAllocationAdpter = new AssetAllocationAdpter(context, assetAllocationResponseObjectArrayList);

        rvMutualFundAMCExposure.setAdapter(assetAllocationAdpter);
    }

    private void setAssetAllocationLegendAdapter(ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList, ArrayList<AssetAllocationPerformanceResponseObject> AssetAllocationPerformanceArrayList) {

        Collections.sort(assetAllocationResponseObjectArrayList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                AssetAllocationResponseObject p1 = (AssetAllocationResponseObject) o1;

                AssetAllocationResponseObject p2 = (AssetAllocationResponseObject) o2;

                return Double.compare(Double.parseDouble(p2.getValueAmount()), Double.parseDouble(p1.getValueAmount()));
            }
        });



        AssetAllocationLegendAdapter assetAllocationAdpter = new AssetAllocationLegendAdapter(context, assetAllocationResponseObjectArrayList, AssetAllocationPerformanceArrayList);

        rvAssetAllocationLegend.setAdapter(assetAllocationAdpter);
    }

    private void getProductAllocationAPIResponse() {
        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_HOLDING);

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

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<ArrayList<ClientHoldingObject>> call, @NonNull Response<ArrayList<ClientHoldingObject>> response) {
                util.showProgressDialog(context, false);
                if (response.isSuccessful()) {

                    clientHoldingObjectArrayList = response.body();

                    LinkedHashMap<String, String> productAllocationMap = new LinkedHashMap<String, String>();

                    productAllocationMap = calculateProductAllocationSourceAndCost(clientHoldingObjectArrayList);

                    setProductAllocationGraph(productAllocationMap, productAllocationObjectArrayList);

                    getMutualFundSchemeCategoryData();

                    getMutualFundAMCExposureData();

                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ClientHoldingObject>> call, @NonNull Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProductAllocationGraph(LinkedHashMap<String, String> productAllocationMap, ArrayList<ProductAllocationObject> productAllocationObjectArrayList) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<Integer> colors = new ArrayList<Integer>();

        ArrayList<ProductValueBean> productValueBeanArrayList = new ArrayList<ProductValueBean>();

        int index = 0;

        for (Map.Entry<String, String> entry : productAllocationMap.entrySet()) {

            entries.add(new PieEntry(Float.parseFloat(entry.getValue()), index));

            labels.add(entry.getKey());

            ProductValueBean productValueBean = new ProductValueBean();

            productValueBean.setLabel(entry.getKey());

            productValueBean.setPercentage(entry.getValue());

            productValueBeanArrayList.add(productValueBean);

            if (entry.getKey().contains("Cash")) {

                colors.add(context.getResources().getColor(R.color.progressTintCash));

            } else if (entry.getKey().contains("Debt")) {

                colors.add(context.getResources().getColor(R.color.progressTintDebt));

            } else if (entry.getKey().contains("Equity")) {

                colors.add(context.getResources().getColor(R.color.progressTintEquity));

            } else {

                colors.add(context.getResources().getColor(R.color.progressTint));
            }

            index = index + 1;
        }


        PieDataSet dataset = new PieDataSet(entries, "");

        dataset.setColors(colors);

        dataset.setSliceSpace(5);

        dataset.setDrawValues(false);

        PieData data = new PieData(dataset);


        pieChartProductAllocation.setDrawHoleEnabled(true);

        pieChartProductAllocation.setHoleRadius(70);

        pieChartProductAllocation.setData(data);

        pieChartProductAllocation.setDescription(new Description());

        pieChartProductAllocation.setDrawSliceText(false);

        pieChartProductAllocation.getLegend().setEnabled(false);

        pieChartProductAllocation.animateY(5000);

        setProductAdapter(productAllocationObjectArrayList);
    }

    private void setProductAdapter(ArrayList<ProductAllocationObject> productAllocationObjectArrayList) {

        ProductAllocationAdapter productAllocationAdapter = new ProductAllocationAdapter(context, productAllocationObjectArrayList);

        rvProductName.setAdapter(productAllocationAdapter);
    }

    private void getAssetAllocationPerformanceAPIResponse() {

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_ASSET_PERFORMANCE);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(authenticateResponse.getUserID());

        requestBodyObject.setUserType(authenticateResponse.getUserType());

        requestBodyObject.setUserCode(authenticateResponse.getUserCode());

        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setIndexType("/0/,/999/,/1015/,/1016/,/1090/,/1091/");

        requestBodyObject.setAccountLevel("0"); //For client

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        AssetAllocationPerformanceRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getAssetAllocationPerformanceAPIResponse(AssetAllocationPerformanceRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<AssetAllocationPerformanceResponseObject>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<AssetAllocationPerformanceResponseObject>> call, @NonNull Response<ArrayList<AssetAllocationPerformanceResponseObject>> response) {
                System.out.println("AssetAllocationPerformance VALIDATION RESPONSE: " + new Gson().toJson(response.body()));
                util.showProgressDialog(context, false);
                if (response.isSuccessful()) {

                    AssetAllocationPerformanceArrayList = response.body();
                    linearAssetAllocation.setVisibility(View.VISIBLE);
                    setAssetAllocationGraph(assetAllocationResponseObjectArrayList, AssetAllocationPerformanceArrayList);

                    createAssetAllocationBarChart(response.body());


                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<AssetAllocationPerformanceResponseObject>> call, @NonNull Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAssetAllocationBarChart(ArrayList<AssetAllocationPerformanceResponseObject> assetArrayList) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        Collections.reverse(assetArrayList);

        for (int i = 0; i < assetArrayList.size(); i++) {

            AssetAllocationPerformanceResponseObject model = assetArrayList.get(i);

            entries.add(new BarEntry(i + 1, Float.parseFloat(model.getXIRRPercentage())));

            labels.add(model.getAssetClass());

            if (model.getAssetClass().contains("Cash")) {

                colors.add(context.getResources().getColor(R.color.color_bob_style_2));

            } else if (model.getAssetClass().contains("Debt")) {

                colors.add(context.getResources().getColor(R.color.color_bob_style_1));

            } else if (model.getAssetClass().contains("Equity")) {

                colors.add(context.getResources().getColor(R.color.color_bob_style_0));

            } else {

                colors.add(context.getResources().getColor(R.color.color_bob_style_3));
            }
        }

        barDataSet1 = new BarDataSet(entries, "Actual");
        barDataSet1.setColors(colors);
        barDataSet1.setValueFormatter(new MyValueFormatter());

        BarData data = new BarData(barDataSet1);
        data.setBarWidth(0.15f);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        barChart.animateY(5000);
        barChart.setDrawGridBackground(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setValueFormatter(new MyValueFormatter());
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
    }

    private void getPortfolioPerformanceAPIResponse() {

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_PORTFOLIO_PERFORMANCE);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        /*requestBodyObject.setUserId(authenticateResponse.getUserID());

        requestBodyObject.setUserId("admin");

        requestBodyObject.setClientCode("1");

        requestBodyObject.setIndexType("/0/,/1015/,/1016/,/1059/,/4/");

        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        requestBodyObject.setIsFundware("false");*/

        requestBodyObject.setUserId(authenticateResponse.getUserID());

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setIndexType("0");

        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        requestBodyObject.setIsFundware("false");


        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        PortfolioPerformanceRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getPortfolioPerformanceAPIResponse(PortfolioPerformanceRequestObject.getGlobalRequestObject()).enqueue(new Callback<PortfolioPerformanceResponseObject>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<PortfolioPerformanceResponseObject> call, @NonNull Response<PortfolioPerformanceResponseObject> response) {

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                    ArrayList<PortfolioPerformanceDetailCollection> assetArrayList = response.body().getPortfolioPerformanceDetailCollection();

                    // tvIRR.setText("IRR " + assetArrayList.get(0).getReturnSinceInception() + "%");

                    finalIRR = assetArrayList.get(0).getReturnSinceInception();

                    setData(holdingArrayList);

                    //createPortfolioAssestData(assetArrayList);

                    //   createPortfolioPerformanceBarChart(assetArrayList);


                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PortfolioPerformanceResponseObject> call, @NonNull Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPortfolioAssestData(ArrayList<PortfolioPerformanceDetailCollection> assetArrayList) {

        for (int i = 0; i < assetArrayList.size(); i++) {

            PortfolioPerformanceDetailCollection portfolioPerformanceDetailCollection = assetArrayList.get(i);

            portfolioPerformanceDetailCollection.setReturnSinceInception(assetArrayList.get(0).getReturnSinceInception());

            portfolioPerformanceDetailCollection.setYTDReturn(assetArrayList.get(0).getYTDReturn());

            portfolioPerformanceDetailCollection.setQTDReturn(assetArrayList.get(0).getQTDReturn());

            assetArrayList.add(portfolioPerformanceDetailCollection);
        }
    }

    private void createPortfolioPerformanceBarChart(ArrayList<PortfolioPerformanceDetailCollection> assetArrayList) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int i = 0; i < assetArrayList.size(); i++) {

            PortfolioPerformanceDetailCollection model = assetArrayList.get(i);

            if (model.getType() != null && model.getType().equalsIgnoreCase("Portfolio")) {

                entries.add(new BarEntry(1, Float.parseFloat(model.getReturnSinceInception())));

                entries.add(new BarEntry(2, Float.parseFloat(model.getYTDReturn())));

                entries.add(new BarEntry(3, Float.parseFloat(model.getQTDReturn())));

                labels.add("Inception");

                labels.add("Year to Date");

                labels.add("Last Qtr");


                colors.add(context.getResources().getColor(R.color.color_bob_style_0));

                colors.add(context.getResources().getColor(R.color.color_bob_style_1));

                colors.add(context.getResources().getColor(R.color.color_bob_style_2));
            }
        }

        barDataSet1 = new BarDataSet(entries, "Actual");
        barDataSet1.setColors(colors);
        barDataSet1.setValueFormatter(new MyValueFormatter());

        BarData data = new BarData(barDataSet1);
        data.setBarWidth(0.15f);

        portfolioBarChart.setData(data);
        portfolioBarChart.getDescription().setEnabled(false);

        portfolioBarChart.setTouchEnabled(false);
        portfolioBarChart.setDoubleTapToZoomEnabled(false);


        XAxis xAxis = portfolioBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        portfolioBarChart.setDragEnabled(true);
        portfolioBarChart.setVisibleXRangeMaximum(3);

        //portfolioBarChart.getXAxis().setAxisMinimum(0);
        portfolioBarChart.animateY(5000);
        portfolioBarChart.setDrawGridBackground(false);

        portfolioBarChart.getAxisRight().setDrawGridLines(false);
        portfolioBarChart.getAxisLeft().setDrawGridLines(false);
        portfolioBarChart.getXAxis().setDrawGridLines(false);
        portfolioBarChart.getAxisRight().setEnabled(false);
        portfolioBarChart.getAxisLeft().setValueFormatter(new MyValueFormatter());
        portfolioBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        portfolioBarChart.getLegend().setEnabled(false);
        portfolioBarChart.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getMutualFundSchemeCategoryData() {
        if (amcStatus.equalsIgnoreCase("1"))
        {
            linearMutualFund.setVisibility(View.VISIBLE);
        } else {
            linearMutualFund.setVisibility(View.GONE);
        }
        LinkedHashMap<String, String> mutualFundSchemeCategoryMap = calculateMutualFundSchemeData(clientHoldingObjectArrayList);

        System.out.println("MUTUAL FUND SCHEME CATEGORY ALLOCATION MAP: " + mutualFundSchemeCategoryMap);

        setMutulFundSchemeCategoryGraph(mutualFundSchemeCategoryMap);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setMutulFundSchemeCategoryGraph(LinkedHashMap<String, String> productAllocationMap) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<ProductValueBean> productValueBeanArrayList = new ArrayList<ProductValueBean>();

        int index = 0;

        for (Map.Entry<String, String> entry : productAllocationMap.entrySet()) {

            entries.add(new PieEntry(Float.parseFloat(entry.getValue()), index));

            labels.add(entry.getKey());

            ProductValueBean productValueBean = new ProductValueBean();

            productValueBean.setLabel(entry.getKey());

            productValueBean.setPercentage(entry.getValue());

            productValueBeanArrayList.add(productValueBean);

            index = index + 1;
        }

        PieDataSet dataset = new PieDataSet(entries, "");

        PieData data = new PieData(dataset);

        int colorArray[] = util.getBobStyleColors(productValueBeanArrayList);

        dataset.setColors(colorArray);

        //dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

        dataset.setSliceSpace(0);

        dataset.setDrawValues(false);

        pieChartMutualFundSchemeCategory.setDrawHoleEnabled(true);

        pieChartMutualFundSchemeCategory.setHoleRadius(70);

        pieChartMutualFundSchemeCategory.setData(data);

        pieChartMutualFundSchemeCategory.getDescription().setEnabled(false);

        pieChartMutualFundSchemeCategory.setDrawSliceText(false);

        pieChartMutualFundSchemeCategory.getLegend().setEnabled(false);

        pieChartMutualFundSchemeCategory.animateY(5000);

        setMutualFundSchemeCategoryAdapter(productValueBeanArrayList, colorArray);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setMutualFundSchemeCategoryAdapter(ArrayList<ProductValueBean> productValueBeanArrayList, int[] VORDIPLOM_COLORS) {

        ProductNameAdapter productNameAdapter = new ProductNameAdapter(context, productValueBeanArrayList, VORDIPLOM_COLORS);

        rvMutulFundSchemeCategory.setAdapter(productNameAdapter);

        //  getMutualFundAMCExposureData();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getMutualFundAMCExposureData() {

        ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList = calculateMutualFundAMCExposureData(clientHoldingObjectArrayList);

        ArrayList<AssetAllocationResponseObject> arrayList = new ArrayList<>();


        for (int i = 0; i < assetAllocationResponseObjectArrayList.size(); i++) {

            String fileName = "custom_progress_bar_" + (i + 1);

            String str[] = fileName.split("_");

            int lastValue = Integer.parseInt(str[3]);

            if (lastValue <= 8) {

                fileName = "custom_progress_bar_" + (i + 1);

            } else {

                fileName = "custom_progress_bar_8";
            }

            int resId = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());

            Drawable drawable = context.getResources().getDrawable(resId);

            assetAllocationResponseObjectArrayList.get(i).setColorDrawable(drawable);

            arrayList.add(assetAllocationResponseObjectArrayList.get(i));
        }

        setAssetAllocationAdapter(arrayList);
    }

    private void createBarChartMutualFundExposure(LinkedHashMap<String, String> mutualFundMap) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<Integer> colors = new ArrayList<Integer>();

        ArrayList<ProductValueBean> productValueBeanArrayList = new ArrayList<ProductValueBean>();

        int index = 0;

        for (Map.Entry<String, String> entry : mutualFundMap.entrySet()) {

            entries.add(new BarEntry(Float.parseFloat(entry.getValue()), index));

            labels.add(entry.getKey());

            if (entry.getKey().contains("Large")) {

                colors.add(context.getResources().getColor(R.color.progressTintCash));

            } else if (entry.getKey().contains("Value")) {

                colors.add(context.getResources().getColor(R.color.progressTintDebt));

            } else if (entry.getKey().contains("Multi")) {

                colors.add(context.getResources().getColor(R.color.progressTintEquity));

            } else {

                colors.add(context.getResources().getColor(R.color.progressTint));
            }


            ProductValueBean productValueBean = new ProductValueBean();

            productValueBean.setLabel(entry.getKey());

            productValueBean.setPercentage(entry.getValue());

            productValueBeanArrayList.add(productValueBean);


            index = index + 1;
        }

        BarDataSet dataset = new BarDataSet(entries, "");

        //dataset.setColors(colors);
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataset);

        BarData data = new BarData(dataSets);

        barChartMutualFundAMCExposure.setData(data);


        barChartMutualFundAMCExposure.setDescription(new Description());

        barChartMutualFundAMCExposure.animateY(5000);

        barChartMutualFundAMCExposure.setTouchEnabled(false);
        barChartMutualFundAMCExposure.setDoubleTapToZoomEnabled(false);

        barChartMutualFundAMCExposure.getAxisLeft().setEnabled(false);
        barChartMutualFundAMCExposure.getAxisRight().setEnabled(false);

        barChartMutualFundAMCExposure.getAxisRight().setDrawGridLines(false);
        barChartMutualFundAMCExposure.getAxisLeft().setDrawGridLines(false);

        barChartMutualFundAMCExposure.getXAxis().setDrawGridLines(false);


        barChartMutualFundAMCExposure.getLegend().setEnabled(false);

        XAxis xAxis = barChartMutualFundAMCExposure.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis rightYAxis = barChartMutualFundAMCExposure.getAxisRight();
        rightYAxis.setEnabled(false);

        barChartMutualFundAMCExposure.getLayoutParams().height = mutualFundMap.size() * 100;
    }

    private LinkedHashMap<String, String> calculateProductAllocationSourceAndCost(ArrayList<ClientHoldingObject> clientHoldingObjectArrayList) {

        /*String response = "[{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"111\",\"CurrentPrice\":\"2070.1\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"20701.0\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.01\",\"Quantity\":\"10.0\",\"SchemeName\":\"ASIAN PAINTS LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"20701.0\"},{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"4306\",\"CurrentPrice\":\"492.95\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"4929.5\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"10.0\",\"SchemeName\":\"AXIS BANK LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"4929.5\"},{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"143\",\"CurrentPrice\":\"4.75\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"80.75\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"17.0\",\"SchemeName\":\"BAJAJ HINDUSTHAN SUGAR LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"80.75\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Fund of Funds\",\"ClientCode\":\"31\",\"CommonScripCode\":\"71085\",\"CurrentPrice\":\"43.82\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"2182313245/04\",\"GainLossPercentage\":\"-68.7638321746897\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"ICICI Prudential Asset Management Company Limited\",\"L4Client_Code\":\"27\",\"MarketValue\":\"1067588.66\",\"MarketValue1\":\"0.0\",\"NetGain\":\"767588.66\",\"PortfolioWeight\":\"0.32\",\"Quantity\":\"24362.7121\",\"SchemeName\":\"ICICI Prudential Income Optimizer Fund(FOF) Growth\",\"Source\":\"Balanced Mutual Fund\",\"Title\":\"Balanced\",\"ValueOfCost\":\"300000.0\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Conservative Allocation\",\"ClientCode\":\"31\",\"CommonScripCode\":\"72653\",\"CurrentPrice\":\"26.18\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"21823393/07\",\"GainLossPercentage\":\"-53.6396369763997\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"IDFC Asset Management Company Limited\",\"L4Client_Code\":\"27\",\"MarketValue\":\"615.24\",\"MarketValue1\":\"0.0\",\"NetGain\":\"-24384.77\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"23.5042\",\"SchemeName\":\"IDFC Regular Savings Fund Regular Plan Growth\",\"Source\":\"Balanced Mutual Fund\",\"Title\":\"Balanced\",\"ValueOfCost\":\"25000.01\"}]";

        Type listType = new TypeToken<List<ClientHoldingObject>>(){}.getType();

        clientHoldingObjectArrayList = new Gson().fromJson(response, listType);*/

        double sourceMarketValue = 0, totalMarketValue = 0;

        String sourceName = "";

        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

        for (int i = 0; i < clientHoldingObjectArrayList.size(); i++) {

            ClientHoldingObject clientHoldingObject = clientHoldingObjectArrayList.get(i);

            totalMarketValue = totalMarketValue + Double.parseDouble(clientHoldingObject.getMarketValue());

            sourceName = clientHoldingObject.getSource();

            if (linkedHashMap.isEmpty()) {

                sourceMarketValue = Double.parseDouble(clientHoldingObject.getMarketValue());

                linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));

            } else {

                if (linkedHashMap.containsKey(sourceName)) {

                    sourceMarketValue = sourceMarketValue + Double.parseDouble(clientHoldingObject.getMarketValue());

                    linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));

                } else {

                    sourceName = clientHoldingObject.getSource();

                    sourceMarketValue = Double.parseDouble(clientHoldingObject.getMarketValue());

                    linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));
                }
            }
        }

        ArrayList<ProductAllocationObject> productAllocationObjectArrayList = createProductAllocationArray(linkedHashMap);

        calculateSourceAndCostPercentage(linkedHashMap, productAllocationObjectArrayList, totalMarketValue);

        return linkedHashMap;
    }

    private void calculateSourceAndCostPercentage(LinkedHashMap<String, String> linkedHashMap, ArrayList<ProductAllocationObject> productAllocationObjectArrayList, double totalMarketValue) {

        this.productAllocationObjectArrayList = new ArrayList<>();

        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {

            String value = String.valueOf((Double.parseDouble(entry.getValue()) / totalMarketValue) * 100);

            linkedHashMap.put(entry.getKey(), value);
        }

        for (int i = 0; i < productAllocationObjectArrayList.size(); i++) {

            ProductAllocationObject productAllocationObject = productAllocationObjectArrayList.get(i);

            productAllocationObject.setProductPercentage(linkedHashMap.get(productAllocationObject.getProductName()));

            this.productAllocationObjectArrayList.add(productAllocationObject);
        }

        System.out.println("PERCENTAGE CALCULATION: " + linkedHashMap);

        System.out.println("TOTAL MARKET VALUE CALCULATION: " + totalMarketValue);
    }

    private ArrayList<ProductAllocationObject> createProductAllocationArray(LinkedHashMap<String, String> linkedHashMap) {

        ArrayList<ProductAllocationObject> productAllocationObjectArrayList = new ArrayList<>();

        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {

            ProductAllocationObject productAllocationObject = new ProductAllocationObject();

            productAllocationObject.setProductName(entry.getKey());

            productAllocationObject.setProductCost(entry.getValue());

            productAllocationObjectArrayList.add(productAllocationObject);
        }

        return productAllocationObjectArrayList;
    }

    private LinkedHashMap<String, String> calculateMutualFundSchemeData(ArrayList<ClientHoldingObject> clientHoldingObjectArrayList) {

        /*String response = "[{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"111\",\"CurrentPrice\":\"2070.1\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"20701.0\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.01\",\"Quantity\":\"10.0\",\"SchemeName\":\"ASIAN PAINTS LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"20701.0\"},{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"4306\",\"CurrentPrice\":\"492.95\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"4929.5\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"10.0\",\"SchemeName\":\"AXIS BANK LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"4929.5\"},{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"143\",\"CurrentPrice\":\"4.75\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"80.75\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"17.0\",\"SchemeName\":\"BAJAJ HINDUSTHAN SUGAR LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"80.75\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Fund of Funds\",\"ClientCode\":\"31\",\"CommonScripCode\":\"71085\",\"CurrentPrice\":\"43.82\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"2182313245/04\",\"GainLossPercentage\":\"-68.7638321746897\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"ICICI Prudential Asset Management Company Limited\",\"L4Client_Code\":\"27\",\"MarketValue\":\"1067588.66\",\"MarketValue1\":\"0.0\",\"NetGain\":\"767588.66\",\"PortfolioWeight\":\"0.32\",\"Quantity\":\"24362.7121\",\"SchemeName\":\"ICICI Prudential Income Optimizer Fund(FOF) Growth\",\"Source\":\"Balanced Mutual Fund\",\"Title\":\"Balanced\",\"ValueOfCost\":\"300000.0\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Conservative Allocation\",\"ClientCode\":\"31\",\"CommonScripCode\":\"72653\",\"CurrentPrice\":\"26.18\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"21823393/07\",\"GainLossPercentage\":\"-53.6396369763997\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"IDFC Asset Management Company Limited\",\"L4Client_Code\":\"27\",\"MarketValue\":\"615.24\",\"MarketValue1\":\"0.0\",\"NetGain\":\"-24384.77\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"23.5042\",\"SchemeName\":\"IDFC Regular Savings Fund Regular Plan Growth\",\"Source\":\"Balanced Mutual Fund\",\"Title\":\"Balanced\",\"ValueOfCost\":\"25000.01\"}]";

        Type listType = new TypeToken<List<ClientHoldingObject>>(){}.getType();

        clientHoldingObjectArrayList = new Gson().fromJson(response, listType);*/

        double sourceMarketValue = 0, totalMarketValue = 0;

        String sourceName = "";

        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

        for (int i = 0; i < clientHoldingObjectArrayList.size(); i++) {

            String DataSource = clientHoldingObjectArrayList.get(i).getDataSource().trim();

            if (DataSource.equalsIgnoreCase("Mutual Fund")) {

                ClientHoldingObject clientHoldingObject = clientHoldingObjectArrayList.get(i);

                totalMarketValue = totalMarketValue + Double.parseDouble(clientHoldingObject.getMarketValue());

                sourceName = clientHoldingObject.getClassification();

                if (linkedHashMap.isEmpty()) {

                    sourceMarketValue = Double.parseDouble(clientHoldingObject.getMarketValue());

                    linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));

                } else {

                    if (linkedHashMap.containsKey(sourceName)) {

                        sourceMarketValue = Double.parseDouble(linkedHashMap.get(sourceName)) + Double.parseDouble(clientHoldingObject.getMarketValue());

                        linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));

                    } else {

                        sourceName = clientHoldingObject.getClassification();

                        sourceMarketValue = Double.parseDouble(clientHoldingObject.getMarketValue());

                        linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));
                    }
                }
            }
        }

        LinkedHashMap<String, String> linkedHashMapFinal = new LinkedHashMap<>();

        double finalValue = 0;

        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {

            double value = (Double.parseDouble(entry.getValue()) / totalMarketValue) * 100;

            if (value < 5) {

                finalValue = finalValue + value;

            } else {

                linkedHashMapFinal.put(entry.getKey(), String.valueOf(value));
            }
        }

        linkedHashMapFinal.put("Others", String.valueOf(finalValue));

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : linkedHashMapFinal.entrySet()) {

            sortedMap.put(entry.getKey(), Double.parseDouble(entry.getValue()));
        }

        linkedHashMapFinal = util.sortByValue(sortedMap);

        return linkedHashMapFinal;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<AssetAllocationResponseObject> calculateMutualFundAMCExposureData(ArrayList<ClientHoldingObject> clientHoldingObjectArrayList) {

        //String response = "[{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"111\",\"CurrentPrice\":\"2070.1\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"20701.0\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.01\",\"Quantity\":\"10.0\",\"SchemeName\":\"ASIAN PAINTS LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"20701.0\"},{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"4306\",\"CurrentPrice\":\"492.95\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"4929.5\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"10.0\",\"SchemeName\":\"AXIS BANK LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"4929.5\"},{\"AverageCost\":\"0.0\",\"ClientCode\":\"31\",\"CommonScripCode\":\"143\",\"CurrentPrice\":\"4.75\",\"DataSource\":\"Direct Equity                                     \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"GainLossPercentage\":\"0.0\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"Direct\",\"L4Client_Code\":\"27\",\"MarketValue\":\"80.75\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"17.0\",\"SchemeName\":\"BAJAJ HINDUSTHAN SUGAR LTD\",\"Source\":\"Direct Equity\",\"Title\":\"Equity\",\"ValueOfCost\":\"80.75\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Fund of Funds\",\"ClientCode\":\"31\",\"CommonScripCode\":\"71085\",\"CurrentPrice\":\"43.82\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"2182313245/04\",\"GainLossPercentage\":\"-68.7638321746897\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"ICICI Prudential Asset Management Company Limited\",\"L4Client_Code\":\"27\",\"MarketValue\":\"1067588.66\",\"MarketValue1\":\"0.0\",\"NetGain\":\"767588.66\",\"PortfolioWeight\":\"0.32\",\"Quantity\":\"24362.7121\",\"SchemeName\":\"ICICI Prudential Income Optimizer Fund(FOF) Growth\",\"Source\":\"Balanced Mutual Fund\",\"Title\":\"Balanced\",\"ValueOfCost\":\"300000.0\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Conservative Allocation\",\"ClientCode\":\"31\",\"CommonScripCode\":\"72653\",\"CurrentPrice\":\"26.18\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"21823393/07\",\"GainLossPercentage\":\"-53.6396369763997\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"IDFC Asset Management Company Limited\",\"L4Client_Code\":\"27\",\"MarketValue\":\"615.24\",\"MarketValue1\":\"0.0\",\"NetGain\":\"-24384.77\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"23.5042\",\"SchemeName\":\"IDFC Regular Savings Fund Regular Plan Growth\",\"Source\":\"Balanced Mutual Fund\",\"Title\":\"Balanced\",\"ValueOfCost\":\"25000.01\"}]";

        /*String response = "[{\"AverageCost\":\"0.0\",\"Classification\":\"Long Term\",\"ClientCode\":\"410\",\"CommonScripCode\":\"49104\",\"CurrentPrice\":\"2047.6\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"8529638\",\"GainLossPercentage\":\"1.02167163618105E-09\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"SBI Funds Management Private Limited\",\"L4Client_Code\":\"10931\",\"MarketValue\":\"179075255.42\",\"MarketValue1\":\"0.0\",\"NetGain\":\"0.0\",\"PortfolioWeight\":\"32.14\",\"Quantity\":\"87456.0\",\"SchemeName\":\"SBI Banking and PSU Fund Direct Plan Growth\",\"Source\":\"Debt Mutual Fund\",\"Title\":\"Debt\",\"ValueOfCost\":\"179075255.42\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Credit Risk\",\"ClientCode\":\"410\",\"CommonScripCode\":\"57431\",\"CurrentPrice\":\"35.56\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"7412365\",\"GainLossPercentage\":\"15.4774864533227\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"SBI Funds Management Private Limited\",\"L4Client_Code\":\"10931\",\"MarketValue\":\"342763.9\",\"MarketValue1\":\"0.0\",\"NetGain\":\"53678.84\",\"PortfolioWeight\":\"0.06\",\"Quantity\":\"9638.0\",\"SchemeName\":\"SBI Credit Risk Fund Regular Plan Growth\",\"Source\":\"Debt Mutual Fund\",\"Title\":\"Debt\",\"ValueOfCost\":\"289085.06\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Arbitrage Fund\",\"ClientCode\":\"410\",\"CommonScripCode\":\"44775\",\"CurrentPrice\":\"26.87\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"963215\",\"GainLossPercentage\":\"7.96852966393438\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"SBI Funds Management Private Limited\",\"L4Client_Code\":\"10931\",\"MarketValue\":\"199150.8\",\"MarketValue1\":\"0.0\",\"NetGain\":\"17275.89\",\"PortfolioWeight\":\"0.04\",\"Quantity\":\"7412.0\",\"SchemeName\":\"SBI Arbitrage Opportunities Fund Regular Growth\",\"Source\":\"Equity Mutual Fund\",\"Title\":\"Equity\",\"ValueOfCost\":\"181874.91\"},{\"AverageCost\":\"0.0\",\"Classification\":\"Equity - Other\",\"ClientCode\":\"410\",\"CommonScripCode\":\"45014\",\"CurrentPrice\":\"13.44\",\"DataSource\":\"Mutual Fund                                       \",\"Dividend\":\"0.0\",\"ELSSLocking\":\"0\",\"Folio\":\"845632\",\"GainLossPercentage\":\"12.4032859638476\",\"IncludeAmount\":\"0.0\",\"Issuer\":\"SBI Funds Management Private Limited\",\"L4Client_Code\":\"10931\",\"MarketValue\":\"1343.57\",\"MarketValue1\":\"0.0\",\"NetGain\":\"303.07\",\"PortfolioWeight\":\"0.0\",\"Quantity\":\"100.0\",\"SchemeName\":\"SBI PSU Regular Growth\",\"Source\":\"Equity Mutual Fund\",\"Title\":\"Equity\",\"ValueOfCost\":\"1040.5\"}]";

        Type listType = new TypeToken<List<ClientHoldingObject>>(){}.getType();

        clientHoldingObjectArrayList = new Gson().fromJson(response, listType);*/

        double sourceMarketValue = 0, totalMarketValue = 0;

        String sourceName = "";

        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

        for (int i = 0; i < clientHoldingObjectArrayList.size(); i++) {

            ClientHoldingObject clientHoldingObject = clientHoldingObjectArrayList.get(i);

            String DataSource = clientHoldingObject.getDataSource().trim();

            if (DataSource.equalsIgnoreCase("mutual fund")) {

                totalMarketValue = totalMarketValue + Double.parseDouble(clientHoldingObject.getMarketValue());

                sourceName = clientHoldingObject.getIssuer();

                if (linkedHashMap.isEmpty()) {

                    sourceMarketValue = Double.parseDouble(clientHoldingObject.getMarketValue());

                    linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));

                } else {

                    if (linkedHashMap.containsKey(sourceName)) {

                        sourceMarketValue = Double.parseDouble(linkedHashMap.get(sourceName)) + Double.parseDouble(clientHoldingObject.getMarketValue());

                        linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));

                    } else {

                        sourceName = clientHoldingObject.getIssuer();

                        sourceMarketValue = Double.parseDouble(clientHoldingObject.getMarketValue());

                        linkedHashMap.put(sourceName, String.valueOf(sourceMarketValue));
                    }
                }
            }
        }

        System.out.println("MAP WITH TOTAL VALUE: " + linkedHashMap);

        LinkedHashMap<String, String> linkedHashMapFinal = new LinkedHashMap<>();

        double finalValue = 0;

        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {

            double value = (Double.parseDouble(entry.getValue()) / totalMarketValue) * 100;

            value = util.truncateDecimal(value).doubleValue();
            System.out.println("TOTAL MARKET VALUE: " + totalMarketValue);
            System.out.println("KEY: " + entry.getKey());
            System.out.println("VALUE: " + entry.getValue());

            if (value < 5) {

                finalValue = finalValue + value;

            } else {

                linkedHashMapFinal.put(entry.getKey(), String.valueOf(value));
            }
        }

        linkedHashMapFinal.put("Others", String.valueOf(finalValue));

        ArrayList<AssetAllocationResponseObject> assetAllocationResponseObjectArrayList = new ArrayList<>();

        for (Map.Entry<String, String> entry : linkedHashMapFinal.entrySet()) {

            //String value = String.valueOf((Double.parseDouble(entry.getValue()) / totalMarketValue) * 100);
            String value = entry.getValue();


            AssetAllocationResponseObject assetAllocationResponseObject = new AssetAllocationResponseObject();

            assetAllocationResponseObject.setAssetClassName(entry.getKey());

            assetAllocationResponseObject.setValuePercentage(value);

            assetAllocationResponseObject.setValueAmount("");

            assetAllocationResponseObjectArrayList.add(assetAllocationResponseObject);

            linkedHashMap.put(entry.getKey(), value);
        }

        Collections.sort(assetAllocationResponseObjectArrayList, percentageComparator);

        Collections.reverse(assetAllocationResponseObjectArrayList);

        return assetAllocationResponseObjectArrayList;
    }

    Comparator<AssetAllocationResponseObject> percentageComparator = new Comparator<AssetAllocationResponseObject>() {

        @Override
        public int compare(AssetAllocationResponseObject t1, AssetAllocationResponseObject t2) {

            return (int) ((Float.parseFloat(t1.getValuePercentage()) < Float.parseFloat(t2.getValuePercentage())) ? -1 : (Float.parseFloat(t1.getValuePercentage()) < Float.parseFloat(t2.getValuePercentage())) ? 1 : 0);
        }
    };

    Comparator<AssetAllocationResponseObject> percentageComparatorMutualFund = new Comparator<AssetAllocationResponseObject>() {

        @Override
        public int compare(AssetAllocationResponseObject t1, AssetAllocationResponseObject t2) {

            return (int) ((Float.parseFloat(t1.getValuePercentage()) < Float.parseFloat(t2.getValuePercentage())) ? -1 : (Float.parseFloat(t1.getValuePercentage()) < Float.parseFloat(t2.getValuePercentage())) ? 1 : 0);
        }
    };

    @Override
    public void onStop() {

        super.onStop();

        BOBActivity.imageViewLogo.setVisibility(View.GONE);

        BOBActivity.title.setVisibility(View.VISIBLE);

        BOBActivity.imgBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        BOBActivity.imageViewLogo.setVisibility(View.VISIBLE);

        BOBActivity.title.setVisibility(View.GONE);

        BOBActivity.imgBack.setVisibility(View.GONE);
    }


    public void menuButton() {

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {

            drawerLayout.closeDrawer(Gravity.RIGHT);

        } else {

            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }


    public void manageLeftSideDrawer() {

        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();

        manager.getDefaultDisplay().getMetrics(metrics);

        screenWidth = metrics.widthPixels;

        screenHeight = metrics.heightPixels;

        View leftSideDrawerView = LayoutInflater.from(context).inflate(R.layout.left_side_drawer_layout, null);

        leftSideDrawerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        ImageView close = (ImageView) leftSideDrawerView.findViewById(R.id.close);

        ImageView imgIcon = (ImageView) leftSideDrawerView.findViewById(R.id.imgIcon);

        TextView home = leftSideDrawerView.findViewById(R.id.home);

        TextView dashboard = leftSideDrawerView.findViewById(R.id.dashboard);

        TextView portFolio = leftSideDrawerView.findViewById(R.id.portFolio);

        TextView report = leftSideDrawerView.findViewById(R.id.report);

        TextView transact = leftSideDrawerView.findViewById(R.id.transact);

        TextView orderStatus = leftSideDrawerView.findViewById(R.id.orderStatus);

        TextView dematHolding = leftSideDrawerView.findViewById(R.id.dematHolding);

        TextView stopSIP = leftSideDrawerView.findViewById(R.id.stopSIP);

        TextView setup = leftSideDrawerView.findViewById(R.id.setup);

        imgIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(0);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(0);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });


        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        util.alertboxExitFromApp("Alert!", "Are you sure? Do you want to exit from app?");
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(0);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        portFolio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(1);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new ReportFragment());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        transact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(2);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        orderStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new OrderStatusActivity());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        dematHolding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new DematHoldingActivity());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        stopSIP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new StopSIPActivity());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        setup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new SetUpFragment());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        drawerMenuView.addView(leftSideDrawerView);
    }

    @Override
    public void onSortItemListener(String name) {
        if (name.equalsIgnoreCase("home")) {
            //  BOBActivity.mTabHost.setCurrentTab(0);
            Toast.makeText(getContext(), "This functionality will be available post-integration", Toast.LENGTH_LONG).show();
        }

        if (name.equalsIgnoreCase("profile")) {
            replaceFragment(new ProfileFragment());
        }

        if (name.equalsIgnoreCase("stop sip")) {
            replaceFragment(new StopSIPActivity());
        }

        if (name.equalsIgnoreCase("demat holding")) {
            replaceFragment(new DematHoldingActivity());
        }
    }
}
