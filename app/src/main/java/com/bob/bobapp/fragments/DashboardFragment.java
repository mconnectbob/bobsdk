package com.bob.bobapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.activities.DematHoldingActivity;
import com.bob.bobapp.activities.DiscoverFundsActivity;
import com.bob.bobapp.activities.ExistingPortfolioActivity;
import com.bob.bobapp.activities.HoldingsActivity;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.activities.InvestmentCartActivity;
import com.bob.bobapp.activities.OrderStatusActivity;
import com.bob.bobapp.activities.RiskProfileActivity;
import com.bob.bobapp.activities.SIPSWPSTPDueActivity;
import com.bob.bobapp.activities.StopSIPActivity;
import com.bob.bobapp.activities.TransactionActivity;
import com.bob.bobapp.adapters.DashboardTransactionListAdapter;
import com.bob.bobapp.adapters.ExploreMoreListAdapter;
import com.bob.bobapp.adapters.ReportListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.bean.PortfolioPerformanceDetailCollection;
import com.bob.bobapp.api.request_object.ClientHoldingRequest;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.PortfolioPerformanceRequestObject;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.request_object.TransactionRequestBodyModel;
import com.bob.bobapp.api.request_object.TransactionRequestModel;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.PortfolioPerformanceResponseObject;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.api.response_object.TransactionResponseModel;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.BMSPrefs;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.PopUpClass;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.content.Context.WINDOW_SERVICE;

public class DashboardFragment extends BaseFragment implements onSortItemListener {
    private LinearLayout llMenu, linearTransaction, linearRiskProfile;

    private String[] arrayTitle = {"Holdings", "Transactions", "SIP SWP STP", "Investment Maturity", "Realised Gain/Loss", "Dividend"};

    private int[] arrayImage = {R.drawable.holdings, R.drawable.transaction, R.drawable.sip, R.drawable.investmentmaturity, R.drawable.realisedgainloss, R.drawable.corporataction};

    private RecyclerView rvTransaction, rvExploreMore, rvExploreReports;

    private CardView cardTransactions;

    private LinearLayout existingPortfolio, llAmount, cvNewFund;

    private TextView startNow, tvCurrentValue, tvInvestedAmount, tvGainLoss, tvDevidendInterest, tvNetGain, tvIRR,
            tvNetGainPercent, tvRiskProfileValue, btnReAccessRiskProfile, txtTransaction, btn_Details,
            txtViewMore, txtSipdetail, btn_orderStatus;

    private ImageView imageViewRightArrow, imgLoadTransaction, imgLoadRiskProfile;

    private Context context;

    private Util util;

    private ArrayList<TransactionResponseModel> transactionResponseModelArrayList = new ArrayList<>();

    private ArrayList<ClientHoldingObject> clientHoldingObjectArrayList = new ArrayList<>();

    private int currentIndex = 0;

    private PieChart riskProfilePieChart;

    private ImageView imgDashbaord, imgModerate, imgConservative, imgAggressive, imghighlyAggressive;

    private DecimalFormat formatter;

    private static DecimalFormat df2 = new DecimalFormat("###,###,##");

    private ImageView imgEye, imgEyeHide;

    private double finalGain = 0, investedsAmount = 0.0, percentValue, currentAmount = 0;
    private String finalIRR = "", transactionStatus = "1", riskStatus = "1";
    private ArrayList<ClientHoldingObject> holdingArrayList = new ArrayList<>();
    private APIInterface apiInterface;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_HOLDING);
        String response = SettingPreferences.getHoldingResponse(getActivity());

        Type listType = new TypeToken<List<ClientHoldingObject>>() {
        }.getType();

        clientHoldingObjectArrayList = new Gson().fromJson(response, listType);

        if (clientHoldingObjectArrayList != null) {
            //        setData(currentIndex);
        }

    }

    @Override
    protected void setIcon(Util util) {
        //  FontManager.markAsIconContainer(tvUserHeader, util.iconFont);
    }

    @Override
    protected void getIds(View view) {
        transactionStatus = "1";
        riskStatus = "1";
        formatter = new DecimalFormat("###,###,###");

        txtSipdetail = view.findViewById(R.id.txtSipdetail);
        rvTransaction = view.findViewById(R.id.rvTransaction);
        txtTransaction = view.findViewById(R.id.txtTransaction);

        rvExploreMore = view.findViewById(R.id.rvExploreMore);

        rvExploreReports = view.findViewById(R.id.rvExploreReports);

        existingPortfolio = view.findViewById(R.id.existingPortfolio);

        llAmount = view.findViewById(R.id.llAmount);

        startNow = view.findViewById(R.id.startNow);

        cvNewFund = view.findViewById(R.id.cvNewFund);

        tvCurrentValue = view.findViewById(R.id.tv_current_value);

        tvInvestedAmount = view.findViewById(R.id.tv_invested_amount_value);

        tvGainLoss = view.findViewById(R.id.tv_utilized_gain_or_loss_value);

        tvDevidendInterest = view.findViewById(R.id.tv_utilized_devidend_or_interest_value);

        tvNetGain = view.findViewById(R.id.tv_utilized_net_gain_value);

        tvIRR = view.findViewById(R.id.tv_irr_value);

        tvNetGainPercent = view.findViewById(R.id.tv_utilized_net_gain_percent_value);

        tvRiskProfileValue = view.findViewById(R.id.tv_risk_profile_value);

        imageViewRightArrow = view.findViewById(R.id.img_right_arrow);

        btnReAccessRiskProfile = view.findViewById(R.id.btn_re_access_risk_profile);

        riskProfilePieChart = view.findViewById(R.id.risk_profile_view);

        btn_Details = view.findViewById(R.id.btn_Details);

        imgDashbaord = view.findViewById(R.id.imgDashbaord);

        cardTransactions = view.findViewById(R.id.cardTransactions);
        txtViewMore = view.findViewById(R.id.txtViewMore);

//        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//
//        drawerMenuView = (LinearLayout) view.findViewById(R.id.drawerMenuLLayout);

        imgEye = view.findViewById(R.id.imgEye);
        imgEyeHide = view.findViewById(R.id.imgEyeHide);
        imgModerate = view.findViewById(R.id.imgModerate);
        imgConservative = view.findViewById(R.id.imgConservative);
        imgAggressive = view.findViewById(R.id.imgAggressive);
        imghighlyAggressive = view.findViewById(R.id.imghighlyAggressive);
        btn_orderStatus = view.findViewById(R.id.btn_orderStatus);
        linearTransaction = view.findViewById(R.id.linearTransaction);
        linearRiskProfile = view.findViewById(R.id.linearRiskProfile);
        imgLoadTransaction = view.findViewById(R.id.imgLoadTransaction);
        imgLoadRiskProfile = view.findViewById(R.id.imgLoadRiskProfile);

        imgEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgEyeHide.setVisibility(View.VISIBLE);
                    imgEye.setVisibility(View.GONE);

                    String netGain = replaceString(tvGainLoss.getText().toString());

                    tvGainLoss.setText("₹X,XX,XXX");


                    // net gain value percent
                    String finaltvNetGainPercent = replaceString(tvNetGainPercent.getText().toString());
                   // tvNetGainPercent.setText(finaltvNetGainPercent);
                    tvNetGainPercent.setText("XX.XX");


                    String ivsAmount = replaceString(tvInvestedAmount.getText().toString());
                    //tvInvestedAmount.setText(ivsAmount);
                    tvInvestedAmount.setText("₹X,XX,XXX");

                    String currentValue = replaceString(tvCurrentValue.getText().toString());
                    //tvCurrentValue.setText(currentValue);
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
                    }
                    else
                        {
                        setData(holdingArrayList);
                    }

//                    if (finalIRR.equalsIgnoreCase("") || finalIRR.isEmpty()) {
//                        getPortfolioPerformanceAPIResponse();
//                    } else {
//                        setData(currentIndex);
//                    }


//                    tvGainLoss.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(finalGain))));
//                    tvNetGainPercent.setText(formatter.format(Double.parseDouble(String.valueOf(percentValue))));
//                    tvInvestedAmount.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(investedsAmount))));
//                    tvCurrentValue.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(currentAmount))));
//                    tvIRR.setText("IRR " + finalIRR + "%");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        setRiskProfile();
    }

    // replace stirng
    String replaceString(String string) {
        return string.replaceAll("[0-9]", "X"); // removing all special character.
    }


    // risk profile
    private void setRiskProfile() {
        float conservative = Float.parseFloat("1.0");
        float moderate = Float.parseFloat("1.0");
        float aggressive = Float.parseFloat("1.0");
        float highlyAgressive = Float.parseFloat("1.0");

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(conservative, 0));
        entries.add(new PieEntry(moderate, 1));
        entries.add(new PieEntry(aggressive, 2));
        entries.add(new PieEntry(highlyAgressive, 3));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Conservative");
        labels.add("Moderate");
        labels.add("Aggressive");
        labels.add("Highly Aggressive");

        final int[] MY_COLORS = {Color.rgb(102, 120, 154), Color.rgb(241, 162, 139), Color.rgb(123, 192, 233), Color.rgb(94, 27, 9)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : MY_COLORS) {
            colors.add(c);
        }

        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setColors(colors);
        dataset.setSliceSpace(1);
        dataset.setDrawValues(false);

        PieData data = new PieData(dataset);

        riskProfilePieChart.setDrawHoleEnabled(true);
        riskProfilePieChart.setHoleRadius(70);
        riskProfilePieChart.setData(data);
        riskProfilePieChart.getDescription().setEnabled(false);
        riskProfilePieChart.setDrawSliceText(false);
        riskProfilePieChart.setDrawEntryLabels(false);
        riskProfilePieChart.getLegend().setEnabled(false);
        riskProfilePieChart.animateY(5000);
        riskProfilePieChart.setMaxAngle(180);
        riskProfilePieChart.setRotationAngle(-180);
        riskProfilePieChart.setTouchEnabled(false);

        String riskValue = SettingPreferences.getRiskProfile(context);

        if (riskValue != null && !riskValue.equals("") && labels.contains(riskValue)) {

            int index = labels.indexOf(riskValue);

            Highlight highlight = new Highlight(index, 0, 0);

            riskProfilePieChart.highlightValue(highlight); //call onValueSelected()
        }
    }


    // set data
    private void setData(ArrayList<ClientHoldingObject> holdingArrayList)
    {
        ClientHoldingObject clientHoldingObject = holdingArrayList.get(currentIndex);

        DecimalFormat df = new DecimalFormat("#.##");

        String irrs = df.format(Double.parseDouble(finalIRR));

        tvIRR.setText("IRR " + irrs + "%");

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
        tvInvestedAmount.setText("₹" + InvestedAmount);


        // current value
        currentAmount = marketValue;
        String currentAmount = formatter.format(Double.parseDouble(String.valueOf(marketValue)));
        String finalCurrentAmount = replaceString(currentAmount);
        tvCurrentValue.setText("₹" + currentAmount);


        //   tvNetGain.setText(clientHoldingObject.getNetGain());
        tvNetGain.setText(formatter.format(Double.parseDouble("" + netGain)));


        percentValue = (netGain / valueOfCoast) * 100;

      //  DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(percentValue);

        String finaltvNetGainPercent = replaceString(formatted);
        tvNetGainPercent.setText(formatted);


        double gainLossValue = marketValue - valueOfCoast;

        finalGain = marketValue - valueOfCoast;


        //    gainLossValue = util.truncateDecimal(gainLossValue).doubleValue();

        String tvGainLossValue = formatter.format(Double.parseDouble(String.valueOf(gainLossValue)));
        String finalTvGainLossValue = replaceString(tvGainLossValue);
        tvGainLoss.setText("₹" + tvGainLossValue);


        //tvGainLoss.setText("₹" + formatter.format(Double.parseDouble(String.valueOf(gainLossValue))));

        tvDevidendInterest.setText(formatter.format(Double.parseDouble("" + sumDivendend)));


        double irrValue = util.truncateDecimal(Double.parseDouble(clientHoldingObject.getGainLossPercentage())).doubleValue();


        //  String riskValue = SettingPreferences.getRiskProfile(context);

//        tvRiskProfileValue.setText(riskValue);
//
//        if (riskValue.equalsIgnoreCase("Moderate"))
//        {
//            imgConservative.setVisibility(View.GONE);
//            imgAggressive.setVisibility(View.GONE);
//            imghighlyAggressive.setVisibility(View.GONE);
//            imgModerate.setVisibility(View.VISIBLE);
//        }
//        if (riskValue.equalsIgnoreCase("Conservative")) {
//
//            imgModerate.setVisibility(View.GONE);
//            imgAggressive.setVisibility(View.GONE);
//            imghighlyAggressive.setVisibility(View.GONE);
//            imgConservative.setVisibility(View.VISIBLE);
//        }
//        if (riskValue.equalsIgnoreCase("Aggressive")) {
//
//            imghighlyAggressive.setVisibility(View.GONE);
//            imgConservative.setVisibility(View.GONE);
//            imgModerate.setVisibility(View.GONE);
//            imgAggressive.setVisibility(View.VISIBLE);
//        }
//
//
//        if (riskValue.equalsIgnoreCase("Highly Aggressive")) {
//            imgConservative.setVisibility(View.GONE);
//            imgModerate.setVisibility(View.GONE);
//            imgAggressive.setVisibility(View.GONE);
//            imghighlyAggressive.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    protected void handleListener() {

        existingPortfolio.setOnClickListener(this);

        llAmount.setOnClickListener(this);

        startNow.setOnClickListener(this);

        cvNewFund.setOnClickListener(this);

        imageViewRightArrow.setOnClickListener(this);

        btnReAccessRiskProfile.setOnClickListener(this);

        btn_Details.setOnClickListener(this);
        txtViewMore.setOnClickListener(this);
        txtSipdetail.setOnClickListener(this);
        btn_orderStatus.setOnClickListener(this);
        imgLoadTransaction.setOnClickListener(this);
        imgLoadRiskProfile.setOnClickListener(this);

        BOBActivity.imgBack.setOnClickListener(this);

        BOBActivity.llMenu.setVisibility(View.VISIBLE);

        BOBActivity.llMenu.setOnClickListener(this);
        BOBActivity.tvCartHeader.setOnClickListener(this);

        //imgDashbaord.setOnClickListener(this);

        BOBActivity.imgBack.setVisibility(View.GONE);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        BOBActivity.imageViewLogo.setVisibility(View.VISIBLE);
        BOBActivity.imgInfo.setVisibility(View.GONE);

        BOBActivity.title.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);

        //  manageLeftSideDrawer();

        rvTransaction.setNestedScrollingEnabled(false);

        //   getPortfolioPerformanceAPIResponse();
        setExploreMoreAdapter();
        setReportAdapter();

    }


    private void setExploreMoreAdapter() {

        ArrayList<String> exploreMoreArrayList = new ArrayList<String>();

        exploreMoreArrayList.add("Equity Funds");

        exploreMoreArrayList.add("Debt Funds");

        exploreMoreArrayList.add("Hybrid Funds");

        exploreMoreArrayList.add("Liquid Funds");


        ExploreMoreListAdapter adapter = new ExploreMoreListAdapter(getActivity(), exploreMoreArrayList) {

            @Override
            public void getDetail(Fragment fragment) {

                replaceFragment(fragment);
            }
        };

        //   rvExploreMore.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvExploreMore.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        rvExploreMore.setAdapter(adapter);
    }

    // report adapter
    private void setReportAdapter() {

        ReportListAdapter adapter = new ReportListAdapter(getActivity(), arrayTitle, arrayImage) {

            @Override
            public void getDetail(Fragment fragment) {

                replaceFragment(fragment);
            }
        };

        //rvExploreReports.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvExploreReports.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvExploreReports.setAdapter(adapter);
    }

    private void setDashboardTransactionListAdapter(ArrayList<TransactionResponseModel> transactionResponseModelArrayList) {

        DashboardTransactionListAdapter adapter = new DashboardTransactionListAdapter(getActivity(), transactionResponseModelArrayList);

        rvTransaction.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.llMenu) {
            PopUpClass popUpClass = new PopUpClass();
            popUpClass.showPopupWindow(view, getContext(), this);
            //  menuButton();
        } else if (id == R.id.existingPortfolio) {

            //  ((BOBActivity) getActivity()).setTransactTab();

            replaceFragment(new ExistingPortfolioActivity());
        } else if (id == R.id.llAmount) {

            replaceFragment(new HoldingsActivity());

        } else if (id == R.id.imgLoadTransaction) {
            if (transactionStatus.equalsIgnoreCase("1")) {
                getTransactionApiCall();
            } else {

            }


        } else if (id == R.id.imgLoadRiskProfile) {

            if (riskStatus.equalsIgnoreCase("1")) {
                callRMDetailAPI();
            }

        } else if (id == R.id.startNow || id == R.id.cvNewFund) {

            Bundle args = new Bundle();

            args.putString("exploreName", "Debt Funds");

            Fragment fragment = new DiscoverFundsActivity();

            fragment.setArguments(args);


            replaceFragment(fragment);


        } else if (id == R.id.btn_orderStatus) {

            Bundle args = new Bundle();

            args.putString(Constants.COMING_FROM, "Dashboard");

            Fragment fragment = new OrderStatusActivity();

            fragment.setArguments(args);


            replaceFragment(fragment);


        } else if (id == R.id.btn_Details) {

            replaceFragment(new HoldingsActivity());

        } else if (id == R.id.img_right_arrow) {

            replaceFragment(new HoldingsActivity());

        } else if (id == R.id.btn_re_access_risk_profile) {

            //    callRMDetailAPI();

            replaceFragment(new RiskProfileActivity());

        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (id == R.id.tvCartHeader) {
            InvestmentCartActivity fragment = new InvestmentCartActivity();

            Bundle bundle = new Bundle();

            bundle.putString(Constants.COMING_FROMS, "Dashboard");

            fragment.setArguments(bundle);

            replaceFragment(fragment);
        } else if (id == R.id.txtViewMore) {
            Bundle args = new Bundle();

            args.putString("WhichActivity", "TransactionActivity");

            Fragment fragment = new TransactionActivity();

            fragment.setArguments(args);


            replaceFragment(fragment);
        } else if (id == R.id.txtSipdetail) {
            Bundle args = new Bundle();

            args.putString("WhichActivity", "TransactionActivity");

            Fragment fragment = new SIPSWPSTPDueActivity();

            fragment.setArguments(args);


            replaceFragment(fragment);
        }
    }

    private void getTransactionApiCall() {

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_TRANSACTION);

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        // Toast.makeText(getContext(), authenticateResponse.getBusinessDate(), Toast.LENGTH_SHORT).show();
        System.out.println("datesss:" + authenticateResponse.getBusinessDate());

        TransactionRequestBodyModel requestBodyModel = new TransactionRequestBodyModel();

        requestBodyModel.setUserId(authenticateResponse.getUserID());

        requestBodyModel.setOnlineAccountCode(authenticateResponse.getUserCode());

        requestBodyModel.setSchemeCode("0");

//        requestBodyModel.setDateFrom(util.getCurrentDate(true));
//
//        requestBodyModel.setDateTo(util.getCurrentDate(false));


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
        c.add(Calendar.DAY_OF_MONTH, -90);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());

        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + newDate);


        requestBodyModel.setDateTo(finalDateTo);
        requestBodyModel.setDateFrom(newDate);


        requestBodyModel.setOrderType("1");

        requestBodyModel.setPageIndex("0");

        requestBodyModel.setPageSize("0");

        requestBodyModel.setCurrencyCode("1");

        requestBodyModel.setAmountDenomination("0");

        requestBodyModel.setAccountLevel("0");

        requestBodyModel.setIsFundware("false");

        requestBodyModel.setClientType("H");

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
                System.out.println("VALIDATION RESPONSEsssss: " + new Gson().toJson(response.body()));
                transactionResponseModelArrayList.clear();

                if (response.isSuccessful()) {
                    transactionStatus = "2";

                    transactionResponseModelArrayList = response.body();

                    linearTransaction.setVisibility(View.VISIBLE);

                    if (transactionResponseModelArrayList.size() > 0) {
                        txtTransaction.setVisibility(View.GONE);
                        rvTransaction.setVisibility(View.VISIBLE);
                    } else {
                        txtTransaction.setVisibility(View.VISIBLE);
                        rvTransaction.setVisibility(View.GONE);
                    }
                    setDashboardTransactionListAdapter(transactionResponseModelArrayList);

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

    private void callRMDetailAPI() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_RM_DETAIL);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(BOBActivity.authResponse.getUserID());

        requestBodyObject.setUserType(BOBActivity.authResponse.getUserType());

        requestBodyObject.setUserCode(BOBActivity.authResponse.getUserCode());

        requestBodyObject.setLastBusinessDate(BOBActivity.authResponse.getBusinessDate());

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        RMDetailRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getRMDetailResponse(RMDetailRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<RMDetailResponseObject>>() {
            @Override
            public void onResponse(Call<ArrayList<RMDetailResponseObject>> call, Response<ArrayList<RMDetailResponseObject>> response) {

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    System.out.println("call rm RESPONSEsssss: " + new Gson().toJson(response.body()));

                    riskStatus = "2";
                    linearRiskProfile.setVisibility(View.VISIBLE);
                    ArrayList<RMDetailResponseObject> rmDetailResponseObjectArrayList = response.body();

                    RMDetailResponseObject rmDetailResponseObject = rmDetailResponseObjectArrayList.get(0);

                    String dob = rmDetailResponseObjectArrayList.get(0).getDateOfBirth();

                    BMSPrefs.putString(getContext(), "userDob", dob);

                    SettingPreferences.setRiskProfile(context, rmDetailResponseObject.getRiskProfile());

                    String riskValue = rmDetailResponseObject.getRiskProfile();

                    tvRiskProfileValue.setText(riskValue);

                    if (riskValue.equalsIgnoreCase("Moderate")) {
                        imgConservative.setVisibility(View.GONE);
                        imgAggressive.setVisibility(View.GONE);
                        imghighlyAggressive.setVisibility(View.GONE);
                        imgModerate.setVisibility(View.VISIBLE);
                    }
                    if (riskValue.equalsIgnoreCase("Conservative")) {

                        imgModerate.setVisibility(View.GONE);
                        imgAggressive.setVisibility(View.GONE);
                        imghighlyAggressive.setVisibility(View.GONE);
                        imgConservative.setVisibility(View.VISIBLE);
                    }
                    if (riskValue.equalsIgnoreCase("Aggressive")) {

                        imghighlyAggressive.setVisibility(View.GONE);
                        imgConservative.setVisibility(View.GONE);
                        imgModerate.setVisibility(View.GONE);
                        imgAggressive.setVisibility(View.VISIBLE);
                    }


                    if (riskValue.equalsIgnoreCase("Highly Aggressive")) {
                        imgConservative.setVisibility(View.GONE);
                        imgModerate.setVisibility(View.GONE);
                        imgAggressive.setVisibility(View.GONE);
                        imghighlyAggressive.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<RMDetailResponseObject>> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getPortfolioPerformanceAPIResponse() {
        util.showProgressDialog(context, true);

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

            @Override
            public void onResponse(@NonNull Call<PortfolioPerformanceResponseObject> call, @NonNull Response<PortfolioPerformanceResponseObject> response) {

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    ArrayList<PortfolioPerformanceDetailCollection> assetArrayList = response.body().getPortfolioPerformanceDetailCollection();
                    finalIRR = assetArrayList.get(0).getReturnSinceInception();

                    String finalIRRs = replaceString(assetArrayList.get(0).getReturnSinceInception());

                    setData(holdingArrayList);

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


    private DrawerLayout drawerLayout;

    private LinearLayout drawerMenuView;

    private int screenWidth = 0, screenHeight = 0;

    int DRAWER_ITEMS_OPEN_TIME = 200;

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

    public void menuButton() {

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {

            drawerLayout.closeDrawer(Gravity.RIGHT);

        } else {

            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

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

    private void createShowAccountDetailPopup(View view) {

        Balloon balloon = new Balloon.Builder(context)

                .setLayout(R.layout.layout_account_details)

                .setArrowSize(10)

                .setArrowOrientation(ArrowOrientation.TOP)

                .setArrowPosition(0.7f)

                .setWidthRatio(1.0f)

                .setMargin(10)

                .setCornerRadius(10f)


                .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray))

                .setBalloonAnimation(BalloonAnimation.CIRCULAR)

                .build();

        balloon.showAlignBottom(view);
    }


    @Override
    public void onSortItemListener(String name) {
        if (name.equalsIgnoreCase("home")) {
            Toast.makeText(getContext(),"This functionality will be available post-integration",Toast.LENGTH_LONG).show();
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

                //   util.showProgressDialog(context, false);

                if (response.isSuccessful())
                {
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


}