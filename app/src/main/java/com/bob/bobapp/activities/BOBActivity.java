package com.bob.bobapp.activities;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.MyInvestmentContainerFragment;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.Home.HomeContainerFragment;
import com.bob.bobapp.Home.InvestNowContainerFragment;
import com.bob.bobapp.Home.MyOrderContainerFragment;
import com.bob.bobapp.Home.ProfileContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.AccountListAdapter;
import com.bob.bobapp.adapters.NotificationAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.WebService;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.bean.InvestmentCartCountObject;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.AuthenticateRequest;
import com.bob.bobapp.api.request_object.ClientHoldingRequest;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.InvestmentCartCountRequest;
import com.bob.bobapp.api.request_object.MaturitiesReportModel;
import com.bob.bobapp.api.request_object.MaturityReportRequestModel;
import com.bob.bobapp.api.request_object.NotificationRequestObject;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.InvestmentMaturityModel;
import com.bob.bobapp.api.response_object.NotificationObject;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.fragments.ReportFragment;
import com.bob.bobapp.fragments.SetUpFragment;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.BMSPrefs;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import static android.graphics.Typeface.BOLD;

public class BOBActivity extends BaseActivity implements OnItemDeleteListener
{
    private ArrayList<InvestmentMaturityModel> investmentMaturityModelArrayList;

    private ArrayList<NotificationObject> notificationObjectArrayList;

    private ArrayList<AccountResponseObject> accountResponseObjectArrayList;

    private ArrayList<RMDetailResponseObject> rmDetailResponseObjectArrayList;

    private AccountResponseObject accountResponseObject;

    private int selectedPosition;

    private AuthenticateResponse authenticateResponse;

    public static AuthenticateResponse authResponse;

    private TextView tvUsername, tvPopupAccount, tvPopupRMDetails, btnPopupSubmit;

    private RecyclerView accountDetailsRecyclerView, rvNotification;

    private LinearLayout layoutAccountPopup, layoutRMDetailPopup, layoutHeaderPopup;

    private TextView imgMenu;

    public static LinearLayout llMenu;

    private TextView tvRMUsername, tvRMName, tvRMEmail, tvRMMobileNumber;

    public static TextView tvBellHeader, tvCartHeader, title, tvUserHeader, tvMenu;

    private Util util;

    private Context context;

    private ArrayList<ClientHoldingObject> holdingArrayList=new ArrayList<>();

    private View viewPopup;

    public static ImageView imgDashbaord, imgBack, imageViewLogo,imgInfo;

    public static FragmentTabHost mTabHost;

    private View homeView = null, myInvestment = null, investNow = null, profileView = null, myOrder = null;
    private String channelId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_container);

        EventBus.getDefault().register(this);

        context = this;
        setUpTabs();
        String ucc = getExtraData();

        //   Toast.makeText(getApplicationContext(), channelId, Toast.LENGTH_SHORT).show();

        BMSPrefs.putString(getApplicationContext(), "ucc", ucc);


        authenticateUser(ucc, channelId);

    }

    private String getExtraData() {

        BOBIntent intent = new BOBIntent(getIntent());

        Log.d("BOBActivity", "customer id: " + intent.getCustomerId());

        Log.d("BOBActivity", "login session id: " + intent.getLoginSessionId());

        Log.d("BOBActivity", "heartbeat token: " + intent.getHeartbeatToken());

        Log.d("BOBActivity", "channel id: " + intent.getChannelId());

        channelId = intent.getChannelId();

        return intent.getCustomerId();

    }

    @Override
    public void setIcon(Util util) {
        FontManager.markAsIconContainer(tvUserHeader, util.iconFont);

        FontManager.markAsIconContainer(tvBellHeader, util.iconFont);

        FontManager.markAsIconContainer(tvCartHeader, util.iconFont);

        FontManager.markAsIconContainer(tvMenu, util.iconFont);

        //    tvMenu.setBackgroundResource(R.mipmap.menu);

    }

    @Override
    public void getIds() {

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        title = (TextView) findViewById(R.id.title);

        tvUsername = (TextView) findViewById(R.id.txt_username);

        tvUserHeader = (TextView) findViewById(R.id.tvUserHeader);


        tvBellHeader = (TextView) findViewById(R.id.tvBellHeader);

        tvCartHeader = (TextView) findViewById(R.id.tvCartHeader);

        llMenu = (LinearLayout) findViewById(R.id.llMenu);

        tvMenu = (TextView) findViewById(R.id.menu);

        imgDashbaord = findViewById(R.id.imgDashbaord);

        imgBack = findViewById(R.id.imgBack);
        imgInfo = findViewById(R.id.imgInfo);

        imageViewLogo = findViewById(R.id.imageViewLogo);
    }

    @Override
    public void handleListener() {

        //llMenu.setOnClickListener(this);

        tvUsername.setOnClickListener(this);

        tvUserHeader.setOnClickListener(this);

        tvCartHeader.setOnClickListener(this);

        tvBellHeader.setOnClickListener(this);

        imgBack.setOnClickListener(this);

        imgDashbaord.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
    }

    @Override
    public void initializations() {
        BOBActivity.tvMenu.setText(getResources().getString(R.string.fa_icon_menu));
        BOBActivity. mTabHost.getTabWidget().setVisibility(View.VISIBLE);
    }

    public void setUpTabs()
    {
        mTabHost.getTabWidget().setDividerDrawable(null);

        homeView = LayoutInflater.from(context).inflate(R.layout.home, null);

        TextView homeIcon = (TextView) homeView.findViewById(R.id.txt_home_icon);

        FontManager.markAsIconContainer(homeIcon, util.iconFont);

        mTabHost.addTab(mTabHost.newTabSpec("Home").setIndicator(homeView), HomeContainerFragment.class, null);

        myInvestment = LayoutInflater.from(context).inflate(R.layout.my_investment, null);

        investNow = LayoutInflater.from(context).inflate(R.layout.invest_now, null);

        TextView investNowIcon = (TextView) investNow.findViewById(R.id.txt_invest_now_icon);

        FontManager.markAsIconContainer(investNowIcon, util.iconFont);

        mTabHost.addTab(mTabHost.newTabSpec("Invest Now").setIndicator(investNow), InvestNowContainerFragment.class, null);

        TextView myInvestmentIcon = (TextView) myInvestment.findViewById(R.id.txt_my_investment_icon);

        FontManager.markAsIconContainer(myInvestmentIcon, util.iconFont);

        mTabHost.addTab(mTabHost.newTabSpec("My Investment").setIndicator(myInvestment), MyInvestmentContainerFragment.class, null);


//        myOrder = LayoutInflater.from(context).inflate(R.layout.my_order, null);
//
//        TextView myOrderIcon = (TextView) myOrder.findViewById(R.id.txt_my_order_icon);
//
//        FontManager.markAsIconContainer(myOrderIcon, util.iconFont);
//
//        mTabHost.addTab(mTabHost.newTabSpec("My Orders").setIndicator(myOrder), MyOrderContainerFragment.class, null);
//
//        profileView = LayoutInflater.from(context).inflate(R.layout.profile, null);
//
//        TextView profileViewIcon = (TextView) profileView.findViewById(R.id.txt_profile_icon);
//
//        FontManager.markAsIconContainer(profileViewIcon, util.iconFont);
//
//        mTabHost.addTab(mTabHost.newTabSpec("Profile").setIndicator(profileView), ProfileContainerFragment.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId) {

                hideKeyboard();

                ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag("Home")).clearBackStack();

            }
        });
    }

    @Override
    public void onBackPressed() {

        hideKeyboard();

        onBackData();
    }


    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mTabHost.getApplicationWindowToken(), 0);

    }

    public void onBackData() {

        boolean isPopFragment = false;

        String currentTabTag = mTabHost.getCurrentTabTag();

        if (currentTabTag.equals("Home")) {

            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag("Home")).popFragment();

        }

        else if (currentTabTag.equals("My Investment"))
        {

            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag("My Investment")).popFragment();

        }

        else if (currentTabTag.equals("Invest Now")) {

            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag("Invest Now")).popFragment();

        }
        else if (currentTabTag.equals("My Orders")) {

            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag("My Orders")).popFragment();

        } else if (currentTabTag.equals("Profile")) {

            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag("Profile")).popFragment();
        }

        if (!isPopFragment) {

            alertboxExitFromApp("Alert!", "Are you sure? Do you want to exit from app?");
        }
    }

    public void alertboxExitFromApp(String title, String mymessage) {

        TextView textView = new TextView(context);

        textView.setText(title);

        textView.setPadding(70, 20, 10, 10);

        textView.setTypeface(null, BOLD);

        textView.setTextSize(18);

        new AlertDialog.Builder(context)

                .setMessage(mymessage)

                .setCustomTitle(textView)

                .setCancelable(false)

                .setPositiveButton("Yes",

                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                System.exit(0);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                    }

                }).show();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.txt_username || id == R.id.tvUserHeader) {

            viewPopup = view;

            callAccountDetailAPI();

        } else if (id == R.id.tvBellHeader) {

            viewPopup = view;

            callNotificationAPI();

        } else if (id == R.id.txt_popup_account) {

            tvPopupAccount.setTextColor(Color.parseColor(getString(R.color.color_rad)));

            tvPopupRMDetails.setTextColor(Color.parseColor(getString(R.color.black)));

            setPopupData(view);

        } else if (id == R.id.txt_popup_rm_details) {

            tvPopupAccount.setTextColor(Color.parseColor(getString(R.color.black)));

            tvPopupRMDetails.setTextColor(Color.parseColor(getString(R.color.color_light_orange)));

            setPopupData(view);

        } else if (id == R.id.btn_submit) {

        }
        else if (id == R.id.imgBack) {

            finish();

        }

        else if (id == R.id.imgDashbaord) {

            alertboxExitFromApp("Alert!", "Are you sure? Do you want to exit from app?");
        }
//        else if (id == R.id.tvCartHeader) {
//            replaceFragment(new InvestmentCartActivity());
//        }
    }

    private void callNotificationAPI() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserType("1");

        requestBodyObject.setUserCode(authenticateResponse.getUserCode());

        requestBodyObject.setUserId(authenticateResponse.getUserID());

        requestBodyObject.setClientType("H");

        //requestBodyObject.setReminderPeriod("0");

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        globalRequestObject.setSource(Constants.SOURCE);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        NotificationRequestObject.createGlobalRequestObject(globalRequestObject);

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(this, Constants.ACTION_NOTIFICATION);

        apiInterface.getNotificationResponse(NotificationRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<NotificationObject>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationObject>> call, Response<ArrayList<NotificationObject>> response) {

                if (response.isSuccessful()) {

                    notificationObjectArrayList = response.body();

                    getInvestmentMaturityApiCall();

                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<NotificationObject>> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInvestmentMaturityApiCall() {

        MaturitiesReportModel model = new MaturitiesReportModel();

        model.setFromDate("20200101");

        model.setHeadCode("32");

        model.setTillDate("20210301");

        MaturityReportRequestModel requestModel = new MaturityReportRequestModel();

        requestModel.setRequestBodyObject(model);

        requestModel.setSource(Constants.SOURCE);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(this, uniqueIdentifier);

        requestModel.setUniqueIdentifier(uniqueIdentifier);

        APIInterface apiInterface = BOBApp.getApi(this, Constants.ACTION_INVESTMENT_MATURITY);

        apiInterface.getInvestmentMaturityReportApiCall(requestModel).enqueue(new Callback<ArrayList<InvestmentMaturityModel>>() {

            @Override
            public void onResponse(Call<ArrayList<InvestmentMaturityModel>> call, Response<ArrayList<InvestmentMaturityModel>> response) {

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    investmentMaturityModelArrayList = response.body();

                    createNotificationPopup(viewPopup);

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

    private void createNotificationPopup(View view) {

        Balloon balloon = new Balloon.Builder(context)

                .setLayout(R.layout.notification_details)

                .setArrowSize(10)

                .setArrowOrientation(ArrowOrientation.TOP)

                .setArrowPosition(0.8f)

                .setWidthRatio(1.0f)

                .setMargin(10)

                .setCornerRadius(10f)

                .setPadding(10)

                .setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))

                .setBalloonAnimation(BalloonAnimation.CIRCULAR)

                .build();

        balloon.showAlignBottom(view);

        rvNotification = balloon.getContentView().findViewById(R.id.rvNotification);

        setNotificationDate();
    }

    private void setNotificationDate() {

        NotificationAdapter adapter = new NotificationAdapter(this, notificationObjectArrayList, investmentMaturityModelArrayList);

        rvNotification.setAdapter(adapter);
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

                .setPadding(10)

                .setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))

                .setBalloonAnimation(BalloonAnimation.CIRCULAR)

                .build();

        balloon.showAlignBottom(view);

        tvPopupAccount = balloon.getContentView().findViewById(R.id.txt_popup_account);

        tvPopupRMDetails = balloon.getContentView().findViewById(R.id.txt_popup_rm_details);

        btnPopupSubmit = balloon.getContentView().findViewById(R.id.btn_submit);

        accountDetailsRecyclerView = balloon.getContentView().findViewById(R.id.rvAccounts);

        layoutAccountPopup = balloon.getContentView().findViewById(R.id.account_layout);

        tvRMUsername = balloon.getContentView().findViewById(R.id.tv_rm_username_name);

        tvRMName = balloon.getContentView().findViewById(R.id.tv_rm_name);

        tvRMEmail = balloon.getContentView().findViewById(R.id.tv_rm_email);

        tvRMMobileNumber = balloon.getContentView().findViewById(R.id.tv_rm_mobile_number);

        layoutRMDetailPopup = balloon.getContentView().findViewById(R.id.layout_rm_details);

        layoutHeaderPopup = balloon.getContentView().findViewById(R.id.layout_header_popup);


        tvPopupAccount.setOnClickListener(this);

        tvPopupRMDetails.setOnClickListener(this);

        btnPopupSubmit.setOnClickListener(this);

        setPopupData(view);
    }

    private void setPopupData(View view) {

        AccountListAdapter adapter = new AccountListAdapter(this, accountResponseObjectArrayList, this) {

            @Override
            protected void onAccountSelect(int position) {

                accountResponseObject = accountResponseObjectArrayList.get(position);

                selectedPosition = position;
            }
        };

        accountDetailsRecyclerView.setAdapter(adapter);

        if (view.getId() == R.id.txt_popup_rm_details) {

            RMDetailResponseObject rmDetailResponseObject = rmDetailResponseObjectArrayList.get(0);

            tvRMUsername.setText(rmDetailResponseObject.getClientName());

            tvRMName.setText(rmDetailResponseObject.getPrimaryRMName());

            tvRMEmail.setText(rmDetailResponseObject.getPrimaryRMEmail());

            tvRMMobileNumber.setText(rmDetailResponseObject.getPrimaryRMContactNo());

            layoutAccountPopup.setVisibility(View.GONE);

            layoutRMDetailPopup.setVisibility(View.VISIBLE);

        } else if (view.getId() == R.id.txt_popup_account) {

            layoutAccountPopup.setVisibility(View.VISIBLE);

            layoutRMDetailPopup.setVisibility(View.GONE);
        }
    }

    public void setTransactTab() {

        mTabHost.setCurrentTab(2);
    }

    private void callAccountDetailAPI() {

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setClientType("H"); //H for client

        requestBodyObject.setIsFundware("false");

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        AccountRequestObject.createAccountRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(this, Constants.ACTION_ACCOUNT);

        apiInterface.getAccountResponse(AccountRequestObject.getAccountRequestObject()).enqueue(new Callback<ArrayList<AccountResponseObject>>() {
            @Override
            public void onResponse(Call<ArrayList<AccountResponseObject>> call, Response<ArrayList<AccountResponseObject>> response) {

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    accountResponseObjectArrayList = response.body();

                    callRMDetailAPI();

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callRMDetailAPI() {

        APIInterface apiInterface = BOBApp.getApi(this, Constants.ACTION_RM_DETAIL);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

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

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        RMDetailRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getRMDetailResponse(RMDetailRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<RMDetailResponseObject>>() {
            @Override
            public void onResponse(Call<ArrayList<RMDetailResponseObject>> call, Response<ArrayList<RMDetailResponseObject>> response) {

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    rmDetailResponseObjectArrayList = response.body();

                    createShowAccountDetailPopup(viewPopup);

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


    // authenticate api
    private void authenticateUser(String ucc, String channelId)
    {

    //  AuthenticateRequest.createAuthenticateRequestObject("false", "057741897", "true", "14", "0", "0");

   //   AuthenticateRequest.createAuthenticateRequestObject("false", "067224743", "true", "14", "0", "0");

       //   AuthenticateRequest.createAuthenticateRequestObject("false", "002532492", "true", "14", "0", "0");

         //  AuthenticateRequest.createAuthenticateRequestObject("false", "062949624", "true", "14", "0", "0");

    //    AuthenticateRequest.createAuthenticateRequestObject("false", "069409856", "true", "14", "0", "0");

    AuthenticateRequest.createAuthenticateRequestObject("false", ucc, "true", channelId, "0", "0");

     //   util.showProgressDialog(context, true);

        WebService.action(context, Constants.ACTION_AUTHENTICATE);
    }


    @Subscribe
    public void getAuthenticateResponse(AuthenticateResponse authenticateResponse)
    {
        try
        {
            if (authenticateResponse != null) {
                this.authenticateResponse = authenticateResponse;

                authResponse = authenticateResponse;

                String response = new Gson().toJson(authResponse);

                Log.d("authdddadadd", response);

                tvUsername.setText(authenticateResponse.getUserName());

                RequestBodyObject requestBodyObject = new RequestBodyObject();

                requestBodyObject.setUserId(authenticateResponse.getUserID());

                requestBodyObject.setUserType(authenticateResponse.getUserType());

                requestBodyObject.setUserCode(authenticateResponse.getUserCode());

                requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

                requestBodyObject.setCurrencyCode("1"); //For INR

                requestBodyObject.setAmountDenomination("0"); //For base

                requestBodyObject.setAccountLevel("0"); //For client

                if ((authenticateResponse.getClientUCC() != null))
                {
                    UUID uuid = UUID.randomUUID();

                    String uniqueIdentifier = String.valueOf(uuid);

                    SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

                    ClientHoldingRequest.createClientHoldingRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);

                    WebService.action(context, Constants.ACTION_CLIENT_HOLDING);
                } else {
                    util.showProgressDialog(context, false);
                    showDialog("Become a Wealth Customer by opening Investment Service Account with Bank of Baroda. Please contact your nearest Branch.");
//                    Intent intent = new Intent(getApplicationContext(), NewSetupActivity.class);
//                    startActivity(intent);
//                    finish();
                }
            } else {
                util.showProgressDialog(context, false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Subscribe
    public void getHoldingResponse(ArrayList<ClientHoldingObject> clientHoldingObjectArrayList) {

        String response = new Gson().toJson(clientHoldingObjectArrayList);

        SettingPreferences.setHoldingResponse(context, response);

        holdingArrayList.clear();

        holdingArrayList = clientHoldingObjectArrayList;

        System.out.println("holding VALIDATION RESPONSEsssss: " + new Gson().toJson(holdingArrayList));

        if (clientHoldingObjectArrayList != null && !clientHoldingObjectArrayList.isEmpty()) {

            ClientHoldingObject clientHoldingObject = clientHoldingObjectArrayList.get(0);

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            requestBodyObject.setClientCode(clientHoldingObject.getClientCode());

            requestBodyObject.setParentChannelID(authenticateResponse.getChannelID());

            UUID uuid = UUID.randomUUID();

            String uniqueIdentifier = String.valueOf(uuid);

            SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

            InvestmentCartCountRequest.createInvestmentCartCountRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);

            //WebService.action(context, Constants.ACTION_CART_COUNT);

          //  setUpTabs();

        } else {
            util.showProgressDialog(context, false);
        }
    }


    @Subscribe
    public void getInvestmentCartCountResponse(ArrayList<InvestmentCartCountObject> investmentCartCountObjectArrayList) {
        int cartCount = 0;

        if (investmentCartCountObjectArrayList != null && !investmentCartCountObjectArrayList.isEmpty()) {

            cartCount = investmentCartCountObjectArrayList.size();

        } else {

            util.showProgressDialog(context, false);
        }

        //setAdapter();
    }

    public ArrayList<ClientHoldingObject> getHoldingList() {

        if (holdingArrayList != null && holdingArrayList.size() > 0) {

            return holdingArrayList;
        }

        return null;
    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) fragment.getParentFragment()).replaceFragment(fragment, true);
    }

    @Override
    public void onItemDeleteListener(String id, int position, String clientType) {

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

    // show dialog if client ucc is null
    private void showDialog(String message) {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.client_ucc_popup);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        AppCompatButton btnOk = dialog.findViewById(R.id.btnOk);
        AppCompatTextView txtMessage = dialog.findViewById(R.id.txtMessage);

        txtMessage.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
              //  finish();
             //   onBackPressed();
                System.exit(0);
            }
        });

        dialog.show();

    }

}

