package com.bob.bobapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.activities.DematHoldingActivity;
import com.bob.bobapp.activities.InvestmentCartActivity;
import com.bob.bobapp.activities.OrderStatusActivity;
import com.bob.bobapp.activities.StopSIPActivity;
import com.bob.bobapp.adapters.AccountListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.PortfolioPerformanceDetailCollection;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.PortfolioPerformanceRequestObject;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.PortfolioPerformanceResponseObject;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WINDOW_SERVICE;

public class ProfileFragment extends BaseFragment implements OnItemDeleteListener {
    private TextView tv_rm_username_name, tv_rm_name, tv_rm_email, tv_rm_mobile_number,
            txtAccountEmail, txtAccountPhone, btn_submit;

    private RecyclerView accountDetailsRecyclerView;

    private APIInterface apiInterface;

    private Util util;

    private ArrayList<RMDetailResponseObject> rmDetailResponseObjectArrayList = new ArrayList<>();

    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();

    private AccountResponseObject accountResponseObject;

    private int selectedPosition;

    private Context context;
    private String clientCodes = "", clinetTypes = "";

    private int screenWidth = 0, screenHeight = 0;

    int DRAWER_ITEMS_OPEN_TIME = 200;

    private LinearLayout drawerMenuView;

    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        apiInterface = BOBApp.getApi(getContext(), Constants.ACTION_RM_DETAIL);

        util = new Util(context);

        callRMDetailAPI();
    }

    @Override
    protected void getIds(View view) {
        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    getActivity().onBackPressed();
                }

                return true;
            }
        });

        tv_rm_username_name = view.findViewById(R.id.tv_rm_username_name);

        tv_rm_name = view.findViewById(R.id.tv_rm_name);

        tv_rm_email = view.findViewById(R.id.tv_rm_email);

        tv_rm_mobile_number = view.findViewById(R.id.tv_rm_mobile_number);

        tv_rm_username_name = view.findViewById(R.id.tv_rm_username_name);

        accountDetailsRecyclerView = view.findViewById(R.id.rvAccounts);

        txtAccountEmail = view.findViewById(R.id.txtAccountEmail);
        txtAccountPhone = view.findViewById(R.id.txtAccountPhone);
        btn_submit = view.findViewById(R.id.btn_submit);

//        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//
//        drawerMenuView = (LinearLayout) view.findViewById(R.id.drawerMenuLLayout);
    }

    @Override
    protected void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.imageViewLogo.setVisibility(View.GONE);

        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);

        BOBActivity.title.setVisibility(View.VISIBLE);

        BOBActivity.imgBack.setVisibility(View.VISIBLE);

        BOBActivity.title.setText("Profile");

        BOBActivity.llMenu.setOnClickListener(this);

        BOBActivity.tvCartHeader.setOnClickListener(this);

      //  manageLeftSideDrawer();
    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.imgBack) {
            getActivity().onBackPressed();
            // BOBActivity.mTabHost.setCurrentTab(0);
        }
        if (id == R.id.llMenu) {
          //  menuButton();
        }
        if (id == R.id.tvCartHeader) {
            InvestmentCartActivity fragment = new InvestmentCartActivity();

            Bundle bundle = new Bundle();

            bundle.putString(Constants.COMING_FROMS, "Dashboard");

            fragment.setArguments(bundle);

            replaceFragment(fragment);
        }

        if (id == R.id.btn_submit) {
            if (clientCodes.isEmpty() || clientCodes.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Please select account", Toast.LENGTH_SHORT).show();
            } else {
                getPortfolioPerformanceAPIResponse(clientCodes, clinetTypes);
            }
        }
    }

    private void callRMDetailAPI() {

        util.showProgressDialog(getContext(), true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(BOBActivity.authResponse.getUserID());

        requestBodyObject.setUserType(authenticateResponse.getUserType());

        requestBodyObject.setUserCode(authenticateResponse.getUserCode());

        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        RMDetailRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getRMDetailResponse(RMDetailRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<RMDetailResponseObject>>() {

            @Override
            public void onResponse(Call<ArrayList<RMDetailResponseObject>> call, Response<ArrayList<RMDetailResponseObject>> response) {

                System.out.println("VALIDATION RESPONSEssss: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {

                    rmDetailResponseObjectArrayList = response.body();

                    tv_rm_username_name.setText(rmDetailResponseObjectArrayList.get(0).getClientName());

                    tv_rm_name.setText(rmDetailResponseObjectArrayList.get(0).getPrimaryRMName());

                    tv_rm_email.setText(rmDetailResponseObjectArrayList.get(0).getPrimaryRMEmail());

                    tv_rm_mobile_number.setText(rmDetailResponseObjectArrayList.get(0).getPrimaryRMContactNo());

                    txtAccountEmail.setText(rmDetailResponseObjectArrayList.get(0).getEmailAddress());

                    txtAccountPhone.setText(rmDetailResponseObjectArrayList.get(0).getContactNo());

                    callAccountDetailAPI();

                } else {

                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RMDetailResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callAccountDetailAPI() {

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setClientType("H"); //H for client

        requestBodyObject.setIsFundware("false");

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);

        AccountRequestObject.createAccountRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);


        APIInterface apiInterface = BOBApp.getApi(getContext(), Constants.ACTION_ACCOUNT);

        apiInterface.getAccountResponse(AccountRequestObject.getAccountRequestObject()).enqueue(new Callback<ArrayList<AccountResponseObject>>() {
            @Override
            public void onResponse(Call<ArrayList<AccountResponseObject>> call, Response<ArrayList<AccountResponseObject>> response) {

                util.showProgressDialog(getContext(), false);

                System.out.println("Account RESPONSE: " + new Gson().toJson(response.body()));


                if (response.isSuccessful()) {

                    accountResponseObjectArrayList = response.body();

                    setPopupData(accountResponseObjectArrayList);

                } else {

                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setPopupData(ArrayList<AccountResponseObject> accountResponseObjectArrayList) {

        AccountListAdapter adapter = new AccountListAdapter(getContext(), accountResponseObjectArrayList, this) {

            @Override
            protected void onAccountSelect(int position) {

                accountResponseObject = accountResponseObjectArrayList.get(position);

                selectedPosition = position;
            }
        };

        accountDetailsRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemDeleteListener(String clientCode, int position, String ClientType) {
        clientCodes = clientCode;
        clinetTypes = ClientType;

    }

    @Override
    public void onSipItemDeleteListener(String id, int position, String name) {

    }

    @Override
    public void onRedeemItemDeleteListener(String id, int position, String Amount) {

    }

    @Override
    public void onSwitchItemDeleteListener(String id, int position, String Amount) {

    }

    @Override
    public void onSwPItemDeleteListener(String id, int position, String Amount) {

    }

    @Override
    public void onSTpItemDeleteListener(String id, int position, String Amount) {

    }


    private void getPortfolioPerformanceAPIResponse(String clientCode, String ClientTypes) {
        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_PORTFOLIO_PERFORMANCE);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(authenticateResponse.getUserID());
        requestBodyObject.setUserType("1");
        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());
        requestBodyObject.setClientCode(clientCode);
        requestBodyObject.setCurrencyCode("1"); //For INR
        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        requestBodyObject.setIsFundware("false");

        requestBodyObject.setIndexType("0");
        requestBodyObject.setClientType(ClientTypes);


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
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
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

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }
}