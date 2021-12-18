package com.bob.bobapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.OrderStatusListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.InvestmentcartCountsRequest;
import com.bob.bobapp.api.request_object.InvestmentcartCountsRequestBody;
import com.bob.bobapp.api.request_object.OrderStatusRequest;
import com.bob.bobapp.api.request_object.OrderStatusRequestBody;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.InvestmentCartCountResponse;
import com.bob.bobapp.api.response_object.OrderStatusResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.fragments.ReportFragment;
import com.bob.bobapp.fragments.SetUpFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WINDOW_SERVICE;

public class OrderStatusActivity extends BaseFragment {

    private TextView buyText, stpText, swpText, redeemText, sipText, switchText, txtInvestmentCart, txtInvestmentCount, txtFilter;
    private EditText etSearch;
    private ImageView imgClose;
    private RecyclerView rv;
    private LinearLayout linearSearch;
    private LinearLayout llBuy, llSip, llSwitch, buyView, sipView, switchView, llRedeem, llSwps, llStps;
    private APIInterface apiInterface;
    private Util util;
    private ArrayList<OrderStatusResponse> buyArrayList = new ArrayList<>();
    private ArrayList<OrderStatusResponse> sipArrayList = new ArrayList<>();
    private ArrayList<OrderStatusResponse> switchArrayList = new ArrayList<>();
    private ArrayList<OrderStatusResponse> redeemArrayList = new ArrayList<>();
    private ArrayList<OrderStatusResponse> swpArrayList = new ArrayList<>();
    private ArrayList<OrderStatusResponse> stpArrayList = new ArrayList<>();
    private OrderStatusListAdapter adapter;
    private Context context;
    private int count;
    private String status = "1", searchKey = "";
    private ArrayList<InvestmentCartCountResponse> investmentCartCountResponseArrayList = new ArrayList<>();

    private String fromFragment = "";

    private int screenWidth = 0, screenHeight = 0;

    int DRAWER_ITEMS_OPEN_TIME = 200;

    private LinearLayout drawerMenuView;

    private DrawerLayout drawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        getArgumentFromBundle();

        return inflater.inflate(R.layout.activity_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {
        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    if (!fromFragment.equals("")) {
                        getActivity().onBackPressed();
                    } else {
                        BOBActivity.mTabHost.setCurrentTab(0);
                    }
                }
                return true;
            }
        });

        rv = view.findViewById(R.id.rv);

        llBuy = view.findViewById(R.id.llBuy);
        llSip = view.findViewById(R.id.llSip);
        llSwitch = view.findViewById(R.id.llSwitch);
        llRedeem = view.findViewById(R.id.llRedeem);
        llSwps = view.findViewById(R.id.llSwps);
        llStps = view.findViewById(R.id.llStps);

        buyText = view.findViewById(R.id.buyText);
        sipText = view.findViewById(R.id.sipText);
        switchText = view.findViewById(R.id.switchText);
        redeemText = view.findViewById(R.id.redeemText);
        swpText = view.findViewById(R.id.swpText);
        stpText = view.findViewById(R.id.stpText);

        buyView = view.findViewById(R.id.buyView);
        sipView = view.findViewById(R.id.sipView);
        switchView = view.findViewById(R.id.switchView);
        txtInvestmentCart = view.findViewById(R.id.txtInvestmentCart);
        txtInvestmentCount = view.findViewById(R.id.txtInvestmentCount);
        linearSearch = view.findViewById(R.id.linearSearch);
        imgClose = view.findViewById(R.id.imgClose);
        txtFilter = view.findViewById(R.id.txtFilter);
        etSearch = view.findViewById(R.id.etSearch);


//        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//
//        drawerMenuView = (LinearLayout) view.findViewById(R.id.drawerMenuLLayout);

    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        llBuy.setOnClickListener(this);
        llSip.setOnClickListener(this);
        llSwps.setOnClickListener(this);
        llRedeem.setOnClickListener(this);
        llSwitch.setOnClickListener(this);
        llStps.setOnClickListener(this);
        txtInvestmentCart.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        txtFilter.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKey = etSearch.getText().toString().trim();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });


    }

    private void getArgumentFromBundle() {

        if (getArguments() != null && getArguments().containsKey(Constants.COMING_FROM)) {

            fromFragment = getArguments().getString(Constants.COMING_FROM);
        }
    }

    // filter
    private void filter(String text) {
        ArrayList<OrderStatusResponse> filteredList = new ArrayList<>();
        if (status.equalsIgnoreCase("1")) {
            for (OrderStatusResponse item : buyArrayList) {

                if (item.getFundName() != null) {

                    if (item.getFundName().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }
        }

        if (status.equalsIgnoreCase("2")) {
            for (OrderStatusResponse item : sipArrayList) {

                if (item.getFundName() != null) {

                    if (item.getFundName().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }
        }

        if (status.equalsIgnoreCase("3")) {
            for (OrderStatusResponse item : switchArrayList) {

                if (item.getFundName() != null) {

                    if (item.getFundName().toLowerCase().startsWith(text.toLowerCase())) {

                        filteredList.add(item);
                    }
                }
            }
        }


        adapter.updateList(filteredList);
    }


    @Override
    public void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
//        BOBActivity.imageViewLogo.setVisibility(View.VISIBLE);
//        BOBActivity.llMenu.setVisibility(View.VISIBLE);

//        BOBActivity.title.setVisibility(View.GONE);
//        BOBActivity.imgBack.setVisibility(View.GONE);

        BOBActivity.tvCartHeader.setOnClickListener(this);

        BOBActivity.title.setText("Order Status");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_SIP_SWP_STP_DUE);
        util = new Util(context);

        BOBActivity.llMenu.setOnClickListener(this);

      //  manageLeftSideDrawer();


        getApiCall();
    }

    // api
    private void getApiCall() {

        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        OrderStatusRequestBody requestBodyModel = new OrderStatusRequestBody();
        String tillDate = util.getCurrentDate(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(tillDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, 7);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());

        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + newDate);

        requestBodyModel.setFamCode(0);
        requestBodyModel.setHeadCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setClientCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setFromDate(tillDate);
        requestBodyModel.setToDate(newDate);// current date -7
        requestBodyModel.setClientType("H");

        OrderStatusRequest model = new OrderStatusRequest();
        model.setRequestBodyObject(requestBodyModel);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);
        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);

        apiInterface.getOrderStatusApiCall(model).enqueue(new Callback<ArrayList<OrderStatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderStatusResponse>> call, Response<ArrayList<OrderStatusResponse>> response) {
                //  util.showProgressDialog(context, true);

                if (response.isSuccessful()) {
                    System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                    for (OrderStatusResponse item : response.body()) {
                        if (item.getOrderType().equalsIgnoreCase("Purchase")) {
                            buyArrayList.add(item);
                        } else if (item.getOrderType().equalsIgnoreCase("SIP")) {
                            sipArrayList.add(item);
                        } else if (item.getOrderType().equalsIgnoreCase("Switch")) {
                            switchArrayList.add(item);
                        } else if (item.getOrderType().equalsIgnoreCase("Redemption")) {
                            redeemArrayList.add(item);
                        } else if (item.getOrderType().equalsIgnoreCase("SWP")) {
                            swpArrayList.add(item);
                        } else if (item.getOrderType().equalsIgnoreCase("STP")) {
                            stpArrayList.add(item);
                        }
                    }

                    try {
                        setAdapter(buyArrayList);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    getInvestmentCartCountApiCall();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<OrderStatusResponse>> call, Throwable t) {
                util.showProgressDialog(context, true);
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setAdapter(ArrayList<OrderStatusResponse> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            adapter = new OrderStatusListAdapter(context, arrayList);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setIcon(Util util) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.llMenu) {

          //  menuButton();
        }
        if (id == R.id.tvCartHeader) {
            InvestmentCartActivity fragment = new InvestmentCartActivity();

            Bundle bundle = new Bundle();

            bundle.putString(Constants.COMING_FROMS, "");

            fragment.setArguments(bundle);

            replaceFragment(fragment);
        } else if (id == R.id.llBuy) {
            status = "1";
            buyText.setTextColor(getResources().getColor(R.color.white));
            sipText.setTextColor(getResources().getColor(R.color.colorGray));
            switchText.setTextColor(getResources().getColor(R.color.colorGray));
            redeemText.setTextColor(getResources().getColor(R.color.colorGray));
            swpText.setTextColor(getResources().getColor(R.color.colorGray));
            stpText.setTextColor(getResources().getColor(R.color.colorGray));

            buyText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            sipText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            switchText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            redeemText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            swpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            stpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            try {
                adapter.updateList(buyArrayList);
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        } else if (id == R.id.llSip) {
            status = "2";
            buyText.setTextColor(getResources().getColor(R.color.colorGray));
            sipText.setTextColor(getResources().getColor(R.color.white));
            switchText.setTextColor(getResources().getColor(R.color.colorGray));
            redeemText.setTextColor(getResources().getColor(R.color.colorGray));
            swpText.setTextColor(getResources().getColor(R.color.colorGray));
            stpText.setTextColor(getResources().getColor(R.color.colorGray));

            buyText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            sipText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            switchText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            redeemText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            swpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            stpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            try {
                adapter.updateList(sipArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (id == R.id.llSwitch) {
            status = "3";
            buyText.setTextColor(getResources().getColor(R.color.colorGray));
            sipText.setTextColor(getResources().getColor(R.color.colorGray));
            redeemText.setTextColor(getResources().getColor(R.color.colorGray));
            swpText.setTextColor(getResources().getColor(R.color.colorGray));
            stpText.setTextColor(getResources().getColor(R.color.colorGray));
            switchText.setTextColor(getResources().getColor(R.color.white));

            buyText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            sipText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            redeemText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            swpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            stpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            switchText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            try {
                adapter.updateList(switchArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (id == R.id.llRedeem) {
            status = "4";
            buyText.setTextColor(getResources().getColor(R.color.colorGray));
            sipText.setTextColor(getResources().getColor(R.color.colorGray));
            switchText.setTextColor(getResources().getColor(R.color.colorGray));
            swpText.setTextColor(getResources().getColor(R.color.colorGray));
            stpText.setTextColor(getResources().getColor(R.color.colorGray));
            redeemText.setTextColor(getResources().getColor(R.color.white));

            buyText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            sipText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            switchText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            swpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            stpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            redeemText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            try {
                adapter.updateList(redeemArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (id == R.id.llSwps) {
            status = "5";
            buyText.setTextColor(getResources().getColor(R.color.colorGray));
            sipText.setTextColor(getResources().getColor(R.color.colorGray));
            switchText.setTextColor(getResources().getColor(R.color.colorGray));
            redeemText.setTextColor(getResources().getColor(R.color.colorGray));
            stpText.setTextColor(getResources().getColor(R.color.colorGray));
            swpText.setTextColor(getResources().getColor(R.color.white));

            buyText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            sipText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            switchText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            redeemText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            stpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            swpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            try {
                adapter.updateList(swpArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (id == R.id.llStps) {
            status = "6";
            buyText.setTextColor(getResources().getColor(R.color.colorGray));
            sipText.setTextColor(getResources().getColor(R.color.colorGray));
            switchText.setTextColor(getResources().getColor(R.color.colorGray));
            redeemText.setTextColor(getResources().getColor(R.color.colorGray));
            swpText.setTextColor(getResources().getColor(R.color.colorGray));
            stpText.setTextColor(getResources().getColor(R.color.white));

            buyText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            sipText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            switchText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            redeemText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            swpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            stpText.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            try {
                adapter.updateList(stpArrayList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (id == R.id.txtInvestmentCart) {

            replaceFragment(new InvestmentCartActivity());

        }
        else if (id == R.id.imgBack) {

            if (!fromFragment.equals("")) {

                getActivity().onBackPressed();

            } else {
                BOBActivity.mTabHost.setCurrentTab(0);
            }

        }
        else if (id == R.id.imgClose) {
            linearSearch.setVisibility(View.GONE);
        } else if (id == R.id.txtFilter) {
            linearSearch.setVisibility(View.VISIBLE);
        }
    }

    public void replaceFragment(Fragment fragment) {
        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }


    private void getInvestmentCartCountApiCall() {
        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        InvestmentcartCountsRequestBody requestBody = new InvestmentcartCountsRequestBody();
        requestBody.setClientCode(authenticateResponse.getUserCode());
        requestBody.setParentChannelID("WMSPortal");

        InvestmentcartCountsRequest model = new InvestmentcartCountsRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getInvestmentCartCount(model).enqueue(new Callback<ArrayList<InvestmentCartCountResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<InvestmentCartCountResponse>> call, Response<ArrayList<InvestmentCartCountResponse>> response) {

                util.showProgressDialog(getContext(), false);

                if (response.isSuccessful()) {
                    investmentCartCountResponseArrayList.clear();
                    investmentCartCountResponseArrayList = response.body();

                    for (int i = 0; i < investmentCartCountResponseArrayList.size(); i++) {

                        count = count + Integer.parseInt(investmentCartCountResponseArrayList.get(i).getTranCount());
                    }

//                    txtInvestmentCount.setVisibility(View.VISIBLE);
//                    txtInvestmentCount.setText("" + count);


                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InvestmentCartCountResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {

        super.onStop();

        BOBActivity.imageViewLogo.setVisibility(View.GONE);

        BOBActivity.title.setVisibility(View.VISIBLE);

        BOBActivity.imgBack.setVisibility(View.VISIBLE);
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

}