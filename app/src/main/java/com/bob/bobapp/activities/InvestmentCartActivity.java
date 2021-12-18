package com.bob.bobapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.BankAccountAdapter;
import com.bob.bobapp.adapters.InvestmentAccountAdapter;
import com.bob.bobapp.adapters.InvestmentBuyAdapter;
import com.bob.bobapp.adapters.InvestmentRedeemAdapter;
import com.bob.bobapp.adapters.InvestmentSIPAdapter;
import com.bob.bobapp.adapters.InvestmentSTPAdapter;
import com.bob.bobapp.adapters.InvestmentSWPAdapter;
import com.bob.bobapp.adapters.InvestmentSwitchAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.AccountRequestBody;
import com.bob.bobapp.api.request_object.AccountsRequest;
import com.bob.bobapp.api.request_object.AddInvCartRequest;
import com.bob.bobapp.api.request_object.BankAccountBody;
import com.bob.bobapp.api.request_object.BankAccountRequest;
import com.bob.bobapp.api.request_object.DeleterCartRequest;
import com.bob.bobapp.api.request_object.DeleterCartRequestBody;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.InvestmentcartCountsRequest;
import com.bob.bobapp.api.request_object.InvestmentcartCountsRequestBody;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.OrdersObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountsResponse;
import com.bob.bobapp.api.response_object.AddInvCartResponse;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.BankAccountResponse;
import com.bob.bobapp.api.response_object.GetAccountDetailResponse;
import com.bob.bobapp.api.response_object.InvestmentCartCountResponse;
import com.bob.bobapp.api.request_object.InvestmentCartDetailsRequest;
import com.bob.bobapp.api.request_object.InvestmentCartDetailsRequestBody;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.api.response_object.OrderStatusResponse;
import com.bob.bobapp.api.response_object.SIPDueReportResponse;
import com.bob.bobapp.api.response_object.SystematicSchemeDataResponse;
import com.bob.bobapp.dialog.PopupDialog;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.OnGenericListener;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestmentCartActivity extends BaseFragment implements OnItemDeleteListener, OnGenericListener {
    private TextView txtBuyImage, txtMyOrders, txtInvestmentCart;
    private AppCompatButton btnTransaction;
    private AppCompatTextView txtInvestmentTab, txtTranCount, txtTotalAmount, txtInvestmentAcoount, txtBankAccount,txtAvailableBalance;
    private LinearLayoutCompat linearBankDetail, linearBuy, linearSIP, linearRedeem, linearSwitch, linearSWP, linearSTP,linearBalance;
    private RecyclerView recyclerBuy, recyclerSIP, recyclerRedeem, recyclerSwitch, recyclerSWP, recyclerSTP;
    private AppCompatSpinner spineerInvestmentAccount, spineerBankAccount;
    private InvestmentBuyAdapter investmentBuyAdapter;
    private InvestmentSIPAdapter investmentSIPAdapter;
    private InvestmentRedeemAdapter investmentRedeemAdapter;
    private InvestmentSwitchAdapter investmentSwitchAdapter;
    private InvestmentSWPAdapter investmentSWPAdapter;
    private InvestmentSTPAdapter investmentSTPAdapter;
    private InvestmentAccountAdapter investmentAccountAdapter;
    private BankAccountAdapter bankAccountAdapter;
    private View viewSip, viewBuy, viewRedeem, viewSwitch, viewSWP, viewSTP;
    private double totalAmt = 0;

    private APIInterface apiInterface;
    private ArrayList<InvestmentCartCountResponse> investmentCartCountResponseArrayList = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayLists = new ArrayList<>();
    private ArrayList<AccountsResponse> accountsResponseArrayList = new ArrayList<>();
    private ArrayList<BankAccountResponse> bankAccountResponseArrayList = new ArrayList<>();
    private Util util;
    private Context context;
    private DecimalFormat formatter;
    private String cartName = "Buy";

    private ArrayList<InvestmentCartDetailsResponse> buyArraylist = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> sipArraylist = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> redeemArraylist = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> switchArraylist = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> swpArraylist = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> stpArraylist = new ArrayList<>();

    private ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> sipRequestBodyObjectArrayList = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> redeemRequestBodyObjectArrayList = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> switchRequestBodyObjectArrayList = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> swpRequestBodyObjectArrayList = new ArrayList<>();
    private ArrayList<InvestmentCartDetailsResponse> stpRequestBodyObjectArrayList = new ArrayList<>();

    private String bankCode = "", bankSource = "", accountNumber = "", clientName = "", status = "1",dateTime="";

    private TextView tvBuy, tvSIP, tvRedeem, tvSwitch, tvSWP, tvSTP;

    private String fromFragment = "";

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private double availableBalnce=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        getArgumentFromBundle();

        return inflater.inflate(R.layout.activity_investment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void getIds(View view) {
        bankCode="";
        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    if (!fromFragment.equals("")) {
//                        getActivity().onBackPressed();
//                    } else {
//                        BOBActivity.mTabHost.setCurrentTab(0);
//                    }

                    if (!fromFragment.equals("")) {

                        getActivity().onBackPressed();

                    } else {
                        getActivity().onBackPressed();
                        //  BOBActivity.mTabHost.setCurrentTab(0);
                    }
                }
                return true;
            }
        });


        status = "1";
        formatter = new DecimalFormat("###,###,##0.00");
        txtMyOrders = view.findViewById(R.id.txtMyOrders);

        txtBuyImage = view.findViewById(R.id.txtBuyImage);

        linearBankDetail = view.findViewById(R.id.linearBankDetail);
        recyclerBuy = view.findViewById(R.id.recyclerBuy);
        recyclerSIP = view.findViewById(R.id.recyclerSIP);
        recyclerRedeem = view.findViewById(R.id.recyclerRedeem);
        recyclerSwitch = view.findViewById(R.id.recyclerSwitch);
        recyclerSWP = view.findViewById(R.id.recyclerSWP);
        recyclerSTP = view.findViewById(R.id.recyclerSTP);

        linearBuy = view.findViewById(R.id.linearBuy);
        linearSIP = view.findViewById(R.id.linearSIP);
        linearRedeem = view.findViewById(R.id.linearRedeem);
        linearSwitch = view.findViewById(R.id.linearSwitch);
        linearSWP = view.findViewById(R.id.linearSWP);
        linearSTP = view.findViewById(R.id.linearSTP);
        viewSip = view.findViewById(R.id.viewSip);
        viewBuy = view.findViewById(R.id.viewBuy);
        viewRedeem = view.findViewById(R.id.viewRedeem);
        viewSwitch = view.findViewById(R.id.viewSwitch);
        viewSWP = view.findViewById(R.id.viewSWP);
        viewSTP = view.findViewById(R.id.viewSTP);

        tvBuy = view.findViewById(R.id.tv_buy);
        tvSIP = view.findViewById(R.id.tv_sip);
        tvRedeem = view.findViewById(R.id.tv_redeem);
        tvSwitch = view.findViewById(R.id.tv_switch);
        tvSWP = view.findViewById(R.id.tv_swp);
        tvSTP = view.findViewById(R.id.tv_stp);


        txtInvestmentTab = view.findViewById(R.id.txtInvestmentTab);
        txtTranCount = view.findViewById(R.id.txtTranCount);
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);

        spineerInvestmentAccount = view.findViewById(R.id.spineerInvestmentAccount);

        spineerBankAccount = view.findViewById(R.id.spineerBankAccount);
        txtInvestmentCart = view.findViewById(R.id.txtInvestmentCart);
        btnTransaction = view.findViewById(R.id.btnTransaction);
        txtInvestmentAcoount = view.findViewById(R.id.txtInvestmentAcoount);
        txtBankAccount = view.findViewById(R.id.txtBankAccount);
        linearBalance = view.findViewById(R.id.linearBalance);
        txtAvailableBalance = view.findViewById(R.id.txtAvailableBalance);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = simpleDateFormat.format(calendar.getTime());

      //  Toast.makeText(getContext(),dateTime,Toast.LENGTH_SHORT).show();
    }

    private void getArgumentFromBundle() {

        if (getArguments() != null && getArguments().containsKey(Constants.COMING_FROMS)) {

            fromFragment = getArguments().getString(Constants.COMING_FROMS);
        }
    }


    // investment account adapter
    private void setInvestmentAccountAdapter() {
//        AccountsResponse accountsResponse = new AccountsResponse();
//        accountsResponse.setClientName("Select");
//        accountsResponse.setClientCode("0");
//        accountsResponseArrayList.set(0, accountsResponse);


        investmentAccountAdapter = new InvestmentAccountAdapter(context, accountsResponseArrayList);
        spineerInvestmentAccount.setAdapter(investmentAccountAdapter);

        spineerInvestmentAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                clientName = accountsResponseArrayList.get(position).getClientName();
                if (clientName.equalsIgnoreCase("select")) {

                } else {
                    getBankAccountsApiCall(accountsResponseArrayList.get(position).getClientCode());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    // bank account adapter
    private void setBankAccountAdapter() {
        bankAccountAdapter = new BankAccountAdapter(context, bankAccountResponseArrayList);
        spineerBankAccount.setAdapter(bankAccountAdapter);

        spineerBankAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                bankCode = bankAccountResponseArrayList.get(position).getBankCode();
                bankSource = bankAccountResponseArrayList.get(position).getBankSource();
                accountNumber = bankAccountResponseArrayList.get(position).getBankAccountNo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    @Override
    public void handleListener() {
        linearSIP.setOnClickListener(this);
        linearBuy.setOnClickListener(this);
        linearRedeem.setOnClickListener(this);
        linearSwitch.setOnClickListener(this);
        linearSWP.setOnClickListener(this);
        linearSTP.setOnClickListener(this);
        txtMyOrders.setOnClickListener(this);
        btnTransaction.setOnClickListener(this);
        txtInvestmentAcoount.setOnClickListener(this);
        txtBankAccount.setOnClickListener(this);
        BOBActivity.imgBack.setOnClickListener(this);
    }


    @Override
    protected void initializations() {
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.GONE);
        BOBActivity.title.setText(getString(R.string.investment_cart));
        apiInterface = BOBApp.getApi(context, Constants.ACTION_INVESTMENT_CART_COUNT);
        util = new Util(context);

        getInvestmentCartCountApiCall(cartName);

    }

    @Override
    protected void setIcon(Util util) {
        FontManager.markAsIconContainer(txtBuyImage, util.iconFont);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.linearSIP) {
            status = "2";
            cartName = "SIP";
            if (sipArraylist.size() > 0) {
                txtTranCount.setText("" + sipArraylist.size() + " " + "Funds");
            } else {
                txtTranCount.setText("0 Funds");
            }

            double totalAmt = 0;

            for (int i = 0; i < sipArraylist.size(); i++) {
                totalAmt = totalAmt + (Double.parseDouble(sipArraylist.get(i).getTxnAmountUnit()));
            }

            txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

            txtInvestmentTab.setText(R.string.sip_);
            linearSIP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            linearBuy.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearRedeem.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSWP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSwitch.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSTP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));

            recyclerBuy.setVisibility(View.GONE);
            recyclerRedeem.setVisibility(View.GONE);
            recyclerSwitch.setVisibility(View.GONE);
            recyclerSIP.setVisibility(View.VISIBLE);
            linearBankDetail.setVisibility(View.VISIBLE);
            recyclerSWP.setVisibility(View.GONE);
            recyclerSTP.setVisibility(View.GONE);

            tvBuy.setTextColor(getResources().getColor(R.color.dark_black));
            tvSIP.setTextColor(getResources().getColor(R.color.white));
            tvRedeem.setTextColor(getResources().getColor(R.color.dark_black));
            tvSwitch.setTextColor(getResources().getColor(R.color.dark_black));
            tvSWP.setTextColor(getResources().getColor(R.color.dark_black));
            tvSTP.setTextColor(getResources().getColor(R.color.dark_black));


        } else if (id == R.id.linearBuy) {
            status = "1";
            cartName = "Buy";
            if (buyArraylist.size() > 0) {
                txtTranCount.setText("" + buyArraylist.size() + " " + "Funds");
            } else {
                txtTranCount.setText("0 Funds");
            }

            double totalAmt = 0;

            for (int i = 0; i < buyArraylist.size(); i++) {
                totalAmt = totalAmt + (Double.parseDouble(buyArraylist.get(i).getTxnAmountUnit()));
            }

            txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

            txtInvestmentTab.setText(R.string.buy_);
            linearSIP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearBuy.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            linearRedeem.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSWP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSwitch.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSTP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));


            recyclerBuy.setVisibility(View.VISIBLE);
            recyclerSIP.setVisibility(View.GONE);
            recyclerRedeem.setVisibility(View.GONE);
            recyclerSwitch.setVisibility(View.GONE);
            linearBankDetail.setVisibility(View.VISIBLE);
            recyclerSWP.setVisibility(View.GONE);
            recyclerSTP.setVisibility(View.GONE);

            tvBuy.setTextColor(getResources().getColor(R.color.white));
            tvSIP.setTextColor(getResources().getColor(R.color.dark_black));
            tvRedeem.setTextColor(getResources().getColor(R.color.dark_black));
            tvSwitch.setTextColor(getResources().getColor(R.color.dark_black));
            tvSWP.setTextColor(getResources().getColor(R.color.dark_black));
            tvSTP.setTextColor(getResources().getColor(R.color.dark_black));

        } else if (id == R.id.linearRedeem) {
            status = "3";
            cartName = "REDEEM";
            if (redeemArraylist.size() > 0) {
                txtTranCount.setText("" + redeemArraylist.size() + " " + "Funds");
            } else {
                txtTranCount.setText("0 Funds");
            }

            double totalAmt = 0;

            for (int i = 0; i < redeemArraylist.size(); i++) {
                totalAmt = totalAmt + (Double.parseDouble(redeemArraylist.get(i).getTxnAmountUnit()));
            }

            txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

            txtInvestmentTab.setText(R.string.redeem_);
            linearSIP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearBuy.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearRedeem.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            linearSWP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSwitch.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSTP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));

            tvBuy.setTextColor(getResources().getColor(R.color.dark_black));
            tvSIP.setTextColor(getResources().getColor(R.color.dark_black));
            tvRedeem.setTextColor(getResources().getColor(R.color.white));
            tvSwitch.setTextColor(getResources().getColor(R.color.dark_black));
            tvSWP.setTextColor(getResources().getColor(R.color.dark_black));
            tvSTP.setTextColor(getResources().getColor(R.color.dark_black));

            linearBankDetail.setVisibility(View.GONE);
            recyclerRedeem.setVisibility(View.VISIBLE);
            recyclerBuy.setVisibility(View.GONE);
            recyclerSwitch.setVisibility(View.GONE);
            recyclerSIP.setVisibility(View.GONE);
            recyclerSWP.setVisibility(View.GONE);
            recyclerSTP.setVisibility(View.GONE);
        } else if (id == R.id.linearSwitch) {
            status = "4";
            cartName = "SWITCH";
            if (switchArraylist.size() > 0) {
                txtTranCount.setText("" + switchArraylist.size() + " " + "Funds");
            } else {
                txtTranCount.setText("0 Funds");
            }

            double totalAmt = 0;

            for (int i = 0; i < switchArraylist.size(); i++) {
                totalAmt = totalAmt + (Double.parseDouble(switchArraylist.get(i).getTxnAmountUnit()));
            }
            txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

            txtInvestmentTab.setText(R.string.switchs);
            linearSIP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearBuy.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearRedeem.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSWP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSwitch.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            linearSTP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));

            tvBuy.setTextColor(getResources().getColor(R.color.dark_black));
            tvSIP.setTextColor(getResources().getColor(R.color.dark_black));
            tvRedeem.setTextColor(getResources().getColor(R.color.dark_black));
            tvSwitch.setTextColor(getResources().getColor(R.color.white));
            tvSWP.setTextColor(getResources().getColor(R.color.dark_black));
            tvSTP.setTextColor(getResources().getColor(R.color.dark_black));

            linearBankDetail.setVisibility(View.GONE);
            recyclerRedeem.setVisibility(View.GONE);
            recyclerBuy.setVisibility(View.GONE);
            recyclerSIP.setVisibility(View.GONE);
            recyclerSwitch.setVisibility(View.VISIBLE);
            recyclerSWP.setVisibility(View.GONE);
            recyclerSTP.setVisibility(View.GONE);
        } else if (id == R.id.linearSWP) {
            status = "5";
            cartName = "SWP";
            if (swpArraylist.size() > 0) {
                txtTranCount.setText("" + swpArraylist.size() + " " + "Funds");
            } else {
                txtTranCount.setText("0 Funds");
            }

            double totalAmt = 0;

            for (int i = 0; i < swpArraylist.size(); i++) {
                totalAmt = totalAmt + (Double.parseDouble(swpArraylist.get(i).getTxnAmountUnit()));
            }

            txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

            txtInvestmentTab.setText(R.string.swp_);
            linearSIP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearBuy.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearRedeem.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSWP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));
            linearSwitch.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSTP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));

            tvBuy.setTextColor(getResources().getColor(R.color.dark_black));
            tvSIP.setTextColor(getResources().getColor(R.color.dark_black));
            tvRedeem.setTextColor(getResources().getColor(R.color.dark_black));
            tvSwitch.setTextColor(getResources().getColor(R.color.dark_black));
            tvSWP.setTextColor(getResources().getColor(R.color.white));
            tvSTP.setTextColor(getResources().getColor(R.color.dark_black));

            linearBankDetail.setVisibility(View.GONE);
            recyclerRedeem.setVisibility(View.GONE);
            recyclerBuy.setVisibility(View.GONE);
            recyclerSIP.setVisibility(View.GONE);
            recyclerSwitch.setVisibility(View.GONE);
            recyclerSWP.setVisibility(View.VISIBLE);
            recyclerSTP.setVisibility(View.GONE);
        } else if (id == R.id.linearSTP) {
            status = "6";
            cartName = "STP";
            if (stpArraylist.size() > 0) {
                txtTranCount.setText("" + stpArraylist.size() + " " + "Funds");
            } else {
                txtTranCount.setText("0 Funds");
            }

            double totalAmt = 0;

            for (int i = 0; i < stpArraylist.size(); i++) {
                totalAmt = totalAmt + (Double.parseDouble(stpArraylist.get(i).getTxnAmountUnit()));
            }

            txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

            txtInvestmentTab.setText(R.string.stp_);
            linearSIP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearBuy.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearRedeem.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSWP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSwitch.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange_unselect));
            linearSTP.setBackground(getResources().getDrawable(R.drawable.rounded_inner_orange));

            tvBuy.setTextColor(getResources().getColor(R.color.dark_black));
            tvSIP.setTextColor(getResources().getColor(R.color.dark_black));
            tvRedeem.setTextColor(getResources().getColor(R.color.dark_black));
            tvSwitch.setTextColor(getResources().getColor(R.color.dark_black));
            tvSWP.setTextColor(getResources().getColor(R.color.dark_black));
            tvSTP.setTextColor(getResources().getColor(R.color.white));

            linearBankDetail.setVisibility(View.GONE);
            recyclerRedeem.setVisibility(View.GONE);
            recyclerBuy.setVisibility(View.GONE);
            recyclerSIP.setVisibility(View.GONE);
            recyclerSwitch.setVisibility(View.GONE);
            recyclerSWP.setVisibility(View.GONE);
            recyclerSTP.setVisibility(View.VISIBLE);

        } else if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.txtInvestmentAcoount) {
            openAllPlanDialog(accountsResponseArrayList, "1");
        } else if (id == R.id.txtBankAccount) {
            if (txtInvestmentAcoount.getText().toString().equalsIgnoreCase("select")) {
                Toast.makeText(getContext(), "please select investment account", Toast.LENGTH_LONG).show();
            } else {
                openAllPlanDialog(bankAccountResponseArrayList, "2");
            }
        } else if (id == R.id.imgBack) {
            try {
                if (!fromFragment.equals("")) {

                    getActivity().onBackPressed();

                } else {
                    getActivity().onBackPressed();
                    //  BOBActivity.mTabHost.setCurrentTab(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //  BOBActivity.mTabHost.setCurrentTab(3);
        } else if (id == R.id.txtMyOrders) {
            replaceFragment(new OrderStatusActivity());
        } else if (id == R.id.btnTransaction) {
            if (status.equalsIgnoreCase("1"))
            {
                requestBodyObjectArrayList.clear();
                for (InvestmentCartDetailsResponse model : buyArraylist) {
                    if (model.isSelected()) {
                        InvestmentCartDetailsResponse requestBodyObject = new InvestmentCartDetailsResponse();

                        requestBodyObject.setTxnAmountUnit(model.getTxnAmountUnit());

                        requestBodyObject.setAMCName(model.getAMCName());
                        requestBodyObject.setAddPurchaseMinAmt(model.getAddPurchaseMinAmt());
                        requestBodyObject.setAssetClassCode(model.getAssetClassCode());
                        requestBodyObject.setAwaitingHoldingFundValue(model.getAwaitingHoldingFundValue());
                        requestBodyObject.setAwaitingHoldingUnit(model.getAwaitingHoldingUnit());
                        requestBodyObject.setCostofInvestment(model.getCostofInvestment());
                        requestBodyObject.setCurrentFundValue(model.getCurrentFundValue());
                        requestBodyObject.setCurrentUnits(model.getCurrentUnits());
                        requestBodyObject.setDebitDate(model.getDebitDate());

                        if (model.getIsDividend().equalsIgnoreCase("false")) {
                            requestBodyObject.setDividendOption("G");
                        } else {
                            requestBodyObject.setDividendOption(model.getDividendOption());
                        }

                        requestBodyObject.setExistingAmount(model.getExistingAmount());
                        requestBodyObject.setExistingUnits(model.getExistingUnits());
                        requestBodyObject.setFC1CountryOfBirth(model.getFC1CountryOfBirth());
                        requestBodyObject.setFC1NetWorth(model.getFC1NetWorth());
                        requestBodyObject.setFC2CountryOfBirth(model.getFC2CountryOfBirth());
                        requestBodyObject.setFC2NetWorth(model.getFC2NetWorth());
                        requestBodyObject.setFC3CountryOfBirth(model.getFC3CountryOfBirth());
                        requestBodyObject.setFC3NetWorth(model.getFC3NetWorth());
                        requestBodyObject.setFolioNo(model.getFolioNo());
                        requestBodyObject.setFundCode(model.getFundCode());
                        requestBodyObject.setFundName(model.getFundName());
                        requestBodyObject.setICDID(model.getICDID());
                        requestBodyObject.setICID(model.getICID());
                        requestBodyObject.setIsApplyNominee(model.getIsApplyNominee());
                        requestBodyObject.setIsDividend(model.getIsDividend());
                        requestBodyObject.setL4ClientCode(model.getL4ClientCode());
                        requestBodyObject.setLatestNAV(model.getLatestNAV());
                        requestBodyObject.setLotSize(model.getLotSize());
                        requestBodyObject.setMinInvAmount(model.getMinInvAmount());
                        requestBodyObject.setMinRedeemUnit(model.getMinRedeemUnit());
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setNoOfMonth(model.getNoOfMonth());
                        requestBodyObject.setNomineeIsMinor1(model.getNomineeIsMinor1());
                        requestBodyObject.setNomineeIsMinor2(model.getNomineeIsMinor2());
                        requestBodyObject.setNomineeIsMinor3(model.getNomineeIsMinor3());
                        requestBodyObject.setNomineeShare1(model.getNomineeShare1());
                        requestBodyObject.setNomineeShare2(model.getNomineeShare2());
                        requestBodyObject.setNomineeShare3(model.getNomineeShare3());
                        requestBodyObject.setPeriod(model.getPeriod());
                        requestBodyObject.setPurchaseAllowed(model.getPurchaseAllowed());
                        requestBodyObject.setRecommendationStatus(model.getRecommendationStatus());
                        requestBodyObject.setRedeemAllowed(model.getRedeemAllowed());
                        requestBodyObject.setSIPAllowed(model.getSIPAllowed());
                        requestBodyObject.setSIPStartDate(model.getSIPStartDate());
                        requestBodyObject.setSIPStartDates(model.getSIPStartDates());
                        requestBodyObject.setSettlementBankCode(model.getSettlementBankCode());
                        requestBodyObject.setSipFrequency(model.getSipFrequency());
                        requestBodyObject.setSwitchOutAllowed(model.getSwitchOutAllowed());
                        requestBodyObject.setTargetFundCode(model.getTargetFundCode());
                        requestBodyObject.setTargetFundName(model.getTargetFundName());
                        requestBodyObject.setTnCUrl(model.getTnCUrl());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setValueResearchRating(model.getValueResearchRating());

                        requestBodyObject.setDateOfBirth1(model.getDateOfBirth1());
                        requestBodyObject.setDateOfBirth2(model.getDateOfBirth2());
                        requestBodyObject.setDateOfBirth3(model.getDateOfBirth3());


                        requestBodyObject.setAllorPartial(model.getAllorPartial());
                        requestBodyObject.setAmountOrUnit(model.getAmountOrUnit());
                        requestBodyObject.setFrequency(model.getFrequency());
                        requestBodyObject.setSchemeOfferLink(model.getSchemeOfferLink());
                        requestBodyObject.setNomineeRelationship1(model.getNomineeRelationship1());
                        requestBodyObject.setNomineeRelationship2(model.getNomineeRelationship2());
                        requestBodyObject.setNomineeRelationship3(model.getNomineeRelationship3());

                        requestBodyObject.setNomineeAddress1(model.getNomineeAddress1());
                        requestBodyObject.setNomineeAddress2(model.getNomineeAddress2());
                        requestBodyObject.setNomineeAddress3(model.getNomineeAddress3());


                        requestBodyObject.setNomineeAddress3(model.getAssetClassName());
                        //   requestBodyObject.setClientCode(model.getDateOfBirth3());


                        requestBodyObjectArrayList.add(requestBodyObject);
                    }
                }
                    double amt=0;

                for(int j=0;j<requestBodyObjectArrayList.size();j++)
                {
                    amt=amt+Double.parseDouble(requestBodyObjectArrayList.get(j).getTxnAmountUnit());
                }

                if (requestBodyObjectArrayList.size() > 0)
                {
                    if (clientName.equalsIgnoreCase("Select") || bankCode.equalsIgnoreCase("0") || bankCode.equalsIgnoreCase(""))
                    {
                        Toast.makeText(getContext(), "please select account details", Toast.LENGTH_SHORT).show();
                    }

                    else if(amt>availableBalnce)
                    {
                        Toast.makeText(getContext(), "insufficient fund in selected account.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        Bundle args = new Bundle();
                        args.putSerializable("schemeResponseArrayList", requestBodyObjectArrayList);
                        args.putString("bankCode", bankCode);
                        args.putString("bankSource", bankSource);
                        args.putString("accountNumber", accountNumber);
                        args.putString("clientName", clientName);
                        args.putString("status", status);

                        //  Fragment fragment = new HoldingDetailActivity();
                        Fragment fragment = new ConfirmOrderActivity();

                        fragment.setArguments(args);

                        getDetail(fragment);
                    }


                } else {
                    Toast.makeText(getContext(), "please select funds", Toast.LENGTH_SHORT).show();
                }

            }
            if (status.equalsIgnoreCase("2")) {
                sipRequestBodyObjectArrayList.clear();
                for (InvestmentCartDetailsResponse model : sipArraylist) {
                    if (model.isSelected()) {
                        InvestmentCartDetailsResponse requestBodyObject = new InvestmentCartDetailsResponse();

                        requestBodyObject.setTxnAmountUnit(model.getTxnAmountUnit());

                        requestBodyObject.setAMCName(model.getAMCName());
                        requestBodyObject.setAddPurchaseMinAmt(model.getAddPurchaseMinAmt());
                        requestBodyObject.setAssetClassCode(model.getAssetClassCode());
                        requestBodyObject.setAwaitingHoldingFundValue(model.getAwaitingHoldingFundValue());
                        requestBodyObject.setAwaitingHoldingUnit(model.getAwaitingHoldingUnit());
                        requestBodyObject.setCostofInvestment(model.getCostofInvestment());
                        requestBodyObject.setCurrentFundValue(model.getCurrentFundValue());
                        requestBodyObject.setCurrentUnits(model.getCurrentUnits());
                        requestBodyObject.setDebitDate(model.getDebitDate());
                        if (model.getIsDividend().equalsIgnoreCase("false"))
                        {
                            requestBodyObject.setDividendOption("G");
                        }
                        else {
                            requestBodyObject.setDividendOption(model.getDividendOption());
                        }

                        requestBodyObject.setExistingAmount(model.getExistingAmount());
                        requestBodyObject.setExistingUnits(model.getExistingUnits());
                        requestBodyObject.setFC1CountryOfBirth(model.getFC1CountryOfBirth());
                        requestBodyObject.setFC1NetWorth(model.getFC1NetWorth());
                        requestBodyObject.setFC2CountryOfBirth(model.getFC2CountryOfBirth());
                        requestBodyObject.setFC2NetWorth(model.getFC2NetWorth());
                        requestBodyObject.setFC3CountryOfBirth(model.getFC3CountryOfBirth());
                        requestBodyObject.setFC3NetWorth(model.getFC3NetWorth());
                        requestBodyObject.setFolioNo(model.getFolioNo());
                        requestBodyObject.setFundCode(model.getFundCode());
                        requestBodyObject.setFundName(model.getFundName());
                        requestBodyObject.setICDID(model.getICDID());
                        requestBodyObject.setICID(model.getICID());
                        requestBodyObject.setIsApplyNominee(model.getIsApplyNominee());
                        requestBodyObject.setIsDividend(model.getIsDividend());
                        requestBodyObject.setL4ClientCode(model.getL4ClientCode());
                        requestBodyObject.setLatestNAV(model.getLatestNAV());
                        requestBodyObject.setLotSize(model.getLotSize());
                        requestBodyObject.setMinInvAmount(model.getMinInvAmount());
                        requestBodyObject.setMinRedeemUnit(model.getMinRedeemUnit());
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setNoOfMonth(model.getNoOfMonth());
                        requestBodyObject.setNomineeIsMinor1(model.getNomineeIsMinor1());
                        requestBodyObject.setNomineeIsMinor2(model.getNomineeIsMinor2());
                        requestBodyObject.setNomineeIsMinor3(model.getNomineeIsMinor3());
                        requestBodyObject.setNomineeShare1(model.getNomineeShare1());
                        requestBodyObject.setNomineeShare2(model.getNomineeShare2());
                        requestBodyObject.setNomineeShare3(model.getNomineeShare3());
                        requestBodyObject.setPeriod(model.getPeriod());
                        requestBodyObject.setPurchaseAllowed(model.getPurchaseAllowed());
                        requestBodyObject.setRecommendationStatus(model.getRecommendationStatus());
                        requestBodyObject.setRedeemAllowed(model.getRedeemAllowed());
                        requestBodyObject.setSIPAllowed(model.getSIPAllowed());
                        requestBodyObject.setSIPStartDate(model.getSIPStartDate());
                        requestBodyObject.setSIPStartDates(model.getSIPStartDates());
                        requestBodyObject.setSettlementBankCode(model.getSettlementBankCode());
                        requestBodyObject.setSipFrequency(model.getSipFrequency());
                        requestBodyObject.setSwitchOutAllowed(model.getSwitchOutAllowed());
                        requestBodyObject.setTargetFundCode(model.getTargetFundCode());
                        requestBodyObject.setTnCUrl(model.getTnCUrl());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setValueResearchRating(model.getValueResearchRating());

                        requestBodyObject.setDateOfBirth1(model.getDateOfBirth1());
                        requestBodyObject.setDateOfBirth2(model.getDateOfBirth2());
                        requestBodyObject.setDateOfBirth3(model.getDateOfBirth3());
                        requestBodyObject.setNomineeName1(model.getNomineeName1());
                        requestBodyObject.setNomineeName2(model.getNomineeName2());
                        requestBodyObject.setNomineeName3(model.getNomineeName3());


                        requestBodyObject.setGuardianName1(model.getGuardianName1());
                        requestBodyObject.setGuardianName2(model.getGuardianName2());
                        requestBodyObject.setGuardianName3(model.getGuardianName3());


                        requestBodyObject.setAllorPartial(model.getAllorPartial());
                        requestBodyObject.setAmountOrUnit(model.getAmountOrUnit());
                        requestBodyObject.setFrequency(model.getFrequency());
                        requestBodyObject.setSchemeOfferLink(model.getSchemeOfferLink());
                        requestBodyObject.setNomineeRelationship1(model.getNomineeRelationship1());
                        requestBodyObject.setNomineeRelationship2(model.getNomineeRelationship2());
                        requestBodyObject.setNomineeRelationship3(model.getNomineeRelationship3());


                        //   requestBodyObject.setClientCode(model.getDateOfBirth3());

                        sipRequestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                double amt=0;

                for(int k=0;k<sipRequestBodyObjectArrayList.size();k++)
                {
                    amt=amt+Double.parseDouble(sipRequestBodyObjectArrayList.get(k).getTxnAmountUnit());
                }

                if (sipRequestBodyObjectArrayList.size() > 0) {
                    if (clientName.equalsIgnoreCase("Select") || bankCode.equalsIgnoreCase("0") || bankCode.equalsIgnoreCase("")) {
                        Toast.makeText(getContext(), "please select account details", Toast.LENGTH_SHORT).show();
                    }
                    else if(amt>availableBalnce)
                    {
                        Toast.makeText(getContext(), "insufficient fund in selected account.", Toast.LENGTH_LONG).show();
                    }

                    else {
                        Bundle args = new Bundle();
                        args.putSerializable("schemeResponseArrayList", sipRequestBodyObjectArrayList);
                        args.putString("bankCode", bankCode);
                        args.putString("bankSource", bankSource);
                        args.putString("accountNumber", accountNumber);
                        args.putString("clientName", clientName);
                        args.putString("status", status);

                        //  Fragment fragment = new HoldingDetailActivity();
                        Fragment fragment = new ConfirmOrderActivity();

                        fragment.setArguments(args);

                        getDetail(fragment);
                    }

                } else {
                    Toast.makeText(getContext(), "please select funds", Toast.LENGTH_SHORT).show();
                }

            }
            if (status.equalsIgnoreCase("3"))
            {
                redeemRequestBodyObjectArrayList.clear();
                for (InvestmentCartDetailsResponse model : redeemArraylist) {
                    if (model.isSelected()) {
                        InvestmentCartDetailsResponse requestBodyObject = new InvestmentCartDetailsResponse();

                        requestBodyObject.setTxnAmountUnit(model.getTxnAmountUnit());

                        requestBodyObject.setAMCName(model.getAMCName());
                        requestBodyObject.setAddPurchaseMinAmt(model.getAddPurchaseMinAmt());
                        requestBodyObject.setAssetClassCode(model.getAssetClassCode());
                        requestBodyObject.setAwaitingHoldingFundValue(model.getAwaitingHoldingFundValue());
                        requestBodyObject.setAwaitingHoldingUnit(model.getAwaitingHoldingUnit());
                        requestBodyObject.setCostofInvestment(model.getCostofInvestment());
                        requestBodyObject.setCurrentFundValue(model.getCurrentFundValue());
                        requestBodyObject.setCurrentUnits(model.getCurrentUnits());
                        requestBodyObject.setDebitDate(model.getDebitDate());
                        requestBodyObject.setDividendOption(model.getDividendOption());
                        requestBodyObject.setExistingAmount(model.getExistingAmount());
                        requestBodyObject.setExistingUnits(model.getExistingUnits());
                        requestBodyObject.setFC1CountryOfBirth(model.getFC1CountryOfBirth());
                        requestBodyObject.setFC1NetWorth(model.getFC1NetWorth());
                        requestBodyObject.setFC2CountryOfBirth(model.getFC2CountryOfBirth());
                        requestBodyObject.setFC2NetWorth(model.getFC2NetWorth());
                        requestBodyObject.setFC3CountryOfBirth(model.getFC3CountryOfBirth());
                        requestBodyObject.setFC3NetWorth(model.getFC3NetWorth());
                        requestBodyObject.setFolioNo(model.getFolioNo());
                        requestBodyObject.setFundCode(model.getFundCode());
                        requestBodyObject.setFundName(model.getFundName());
                        requestBodyObject.setICDID(model.getICDID());
                        requestBodyObject.setICID(model.getICID());
                        requestBodyObject.setIsApplyNominee(model.getIsApplyNominee());
                        requestBodyObject.setIsDividend(model.getIsDividend());
                        requestBodyObject.setL4ClientCode(model.getL4ClientCode());
                        requestBodyObject.setLatestNAV(model.getLatestNAV());
                        requestBodyObject.setLotSize(model.getLotSize());
                        requestBodyObject.setMinInvAmount(model.getMinInvAmount());
                        requestBodyObject.setMinRedeemUnit(model.getMinRedeemUnit());
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setNoOfMonth(model.getNoOfMonth());
                        requestBodyObject.setNomineeIsMinor1(model.getNomineeIsMinor1());
                        requestBodyObject.setNomineeIsMinor2(model.getNomineeIsMinor2());
                        requestBodyObject.setNomineeIsMinor3(model.getNomineeIsMinor3());
                        requestBodyObject.setNomineeShare1(model.getNomineeShare1());
                        requestBodyObject.setNomineeShare2(model.getNomineeShare2());
                        requestBodyObject.setNomineeShare3(model.getNomineeShare3());
                        requestBodyObject.setPeriod(model.getPeriod());
                        requestBodyObject.setPurchaseAllowed(model.getPurchaseAllowed());
                        requestBodyObject.setRecommendationStatus(model.getRecommendationStatus());
                        requestBodyObject.setRedeemAllowed(model.getRedeemAllowed());
                        requestBodyObject.setSIPAllowed(model.getSIPAllowed());
                        requestBodyObject.setSIPStartDate(model.getSIPStartDate());
                        requestBodyObject.setSIPStartDates(model.getSIPStartDates());
                        requestBodyObject.setSettlementBankCode(model.getSettlementBankCode());
                        requestBodyObject.setSipFrequency(model.getSipFrequency());
                        requestBodyObject.setSwitchOutAllowed(model.getSwitchOutAllowed());
                        requestBodyObject.setTargetFundCode(model.getTargetFundCode());
                        requestBodyObject.setTnCUrl(model.getTnCUrl());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setValueResearchRating(model.getValueResearchRating());

                        requestBodyObject.setDateOfBirth1(model.getDateOfBirth1());
                        requestBodyObject.setDateOfBirth2(model.getDateOfBirth2());
                        requestBodyObject.setDateOfBirth3(model.getDateOfBirth3());
                        requestBodyObject.setNomineeName1(model.getNomineeName1());
                        requestBodyObject.setNomineeName2(model.getNomineeName2());
                        requestBodyObject.setNomineeName3(model.getNomineeName3());


                        requestBodyObject.setAllorPartial(model.getAllorPartial());
                        requestBodyObject.setAmountOrUnit(model.getAmountOrUnit());
                        requestBodyObject.setFrequency(model.getFrequency());
                        requestBodyObject.setSchemeOfferLink(model.getSchemeOfferLink());
                        requestBodyObject.setNomineeRelationship1(model.getNomineeRelationship1());
                        requestBodyObject.setNomineeRelationship2(model.getNomineeRelationship2());
                        requestBodyObject.setNomineeRelationship3(model.getNomineeRelationship3());


                        //   requestBodyObject.setClientCode(model.getDateOfBirth3());

                        redeemRequestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (redeemRequestBodyObjectArrayList.size() > 0) {
                    Bundle args = new Bundle();
                    args.putSerializable("schemeResponseArrayList", redeemRequestBodyObjectArrayList);
                    args.putString("bankCode", "0");
                    args.putString("bankSource", "0");
                    args.putString("accountNumber", "0");
                    args.putString("clientName", clientName);
                    args.putString("status", status);

                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new ConfirmOrderActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                } else {
                    Toast.makeText(getContext(), "please select funds", Toast.LENGTH_SHORT).show();
                }

            }

            if (status.equalsIgnoreCase("4")) {
                switchRequestBodyObjectArrayList.clear();
                for (InvestmentCartDetailsResponse model : switchArraylist) {
                    if (model.isSelected()) {
                        InvestmentCartDetailsResponse requestBodyObject = new InvestmentCartDetailsResponse();

                        requestBodyObject.setTxnAmountUnit(model.getTxnAmountUnit());

                        requestBodyObject.setAMCName(model.getAMCName());
                        requestBodyObject.setAddPurchaseMinAmt(model.getAddPurchaseMinAmt());
                        requestBodyObject.setAssetClassCode(model.getAssetClassCode());
                        requestBodyObject.setAwaitingHoldingFundValue(model.getAwaitingHoldingFundValue());
                        requestBodyObject.setAwaitingHoldingUnit(model.getAwaitingHoldingUnit());
                        requestBodyObject.setCostofInvestment(model.getCostofInvestment());
                        requestBodyObject.setCurrentFundValue(model.getCurrentFundValue());
                        requestBodyObject.setCurrentUnits(model.getCurrentUnits());
                        requestBodyObject.setDebitDate(model.getDebitDate());
                        requestBodyObject.setDividendOption(model.getDividendOption());
                        requestBodyObject.setExistingAmount(model.getExistingAmount());
                        requestBodyObject.setExistingUnits(model.getExistingUnits());
                        requestBodyObject.setFC1CountryOfBirth(model.getFC1CountryOfBirth());
                        requestBodyObject.setFC1NetWorth(model.getFC1NetWorth());
                        requestBodyObject.setFC2CountryOfBirth(model.getFC2CountryOfBirth());
                        requestBodyObject.setFC2NetWorth(model.getFC2NetWorth());
                        requestBodyObject.setFC3CountryOfBirth(model.getFC3CountryOfBirth());
                        requestBodyObject.setFC3NetWorth(model.getFC3NetWorth());
                        requestBodyObject.setFolioNo(model.getFolioNo());
                        requestBodyObject.setFundCode(model.getFundCode());
                        requestBodyObject.setFundName(model.getFundName());
                        requestBodyObject.setICDID(model.getICDID());
                        requestBodyObject.setICID(model.getICID());
                        requestBodyObject.setIsApplyNominee(model.getIsApplyNominee());
                        requestBodyObject.setIsDividend(model.getIsDividend());
                        requestBodyObject.setL4ClientCode(model.getL4ClientCode());
                        requestBodyObject.setLatestNAV(model.getLatestNAV());
                        requestBodyObject.setLotSize(model.getLotSize());
                        requestBodyObject.setMinInvAmount(model.getMinInvAmount());
                        requestBodyObject.setMinRedeemUnit(model.getMinRedeemUnit());
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setNoOfMonth(model.getNoOfMonth());
                        requestBodyObject.setNomineeIsMinor1(model.getNomineeIsMinor1());
                        requestBodyObject.setNomineeIsMinor2(model.getNomineeIsMinor2());
                        requestBodyObject.setNomineeIsMinor3(model.getNomineeIsMinor3());
                        requestBodyObject.setNomineeShare1(model.getNomineeShare1());
                        requestBodyObject.setNomineeShare2(model.getNomineeShare2());
                        requestBodyObject.setNomineeShare3(model.getNomineeShare3());
                        requestBodyObject.setPeriod(model.getPeriod());
                        requestBodyObject.setPurchaseAllowed(model.getPurchaseAllowed());
                        requestBodyObject.setRecommendationStatus(model.getRecommendationStatus());
                        requestBodyObject.setRedeemAllowed(model.getRedeemAllowed());
                        requestBodyObject.setSIPAllowed(model.getSIPAllowed());
                        requestBodyObject.setSIPStartDate(model.getSIPStartDate());
                        requestBodyObject.setSIPStartDates(model.getSIPStartDates());
                        requestBodyObject.setSettlementBankCode(model.getSettlementBankCode());
                        requestBodyObject.setSipFrequency(model.getSipFrequency());
                        requestBodyObject.setSwitchOutAllowed(model.getSwitchOutAllowed());
                        requestBodyObject.setTargetFundCode(model.getTargetFundCode());
                        requestBodyObject.setTnCUrl(model.getTnCUrl());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setValueResearchRating(model.getValueResearchRating());

                        requestBodyObject.setDateOfBirth1(model.getDateOfBirth1());
                        requestBodyObject.setDateOfBirth2(model.getDateOfBirth2());
                        requestBodyObject.setDateOfBirth3(model.getDateOfBirth3());
                        requestBodyObject.setNomineeName1(model.getNomineeName1());
                        requestBodyObject.setNomineeName2(model.getNomineeName2());
                        requestBodyObject.setNomineeName3(model.getNomineeName3());


                        requestBodyObject.setAllorPartial(model.getAllorPartial());
                        requestBodyObject.setAmountOrUnit(model.getAmountOrUnit());
                        requestBodyObject.setFrequency(model.getFrequency());
                        requestBodyObject.setSchemeOfferLink(model.getSchemeOfferLink());
                        requestBodyObject.setNomineeRelationship1(model.getNomineeRelationship1());
                        requestBodyObject.setNomineeRelationship2(model.getNomineeRelationship2());
                        requestBodyObject.setNomineeRelationship3(model.getNomineeRelationship3());


                        //   requestBodyObject.setClientCode(model.getDateOfBirth3());

                        switchRequestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (switchRequestBodyObjectArrayList.size() > 0) {
                    Bundle args = new Bundle();
                    args.putSerializable("schemeResponseArrayList", switchRequestBodyObjectArrayList);
                    args.putString("bankCode", "0");
                    args.putString("bankSource", "0");
                    args.putString("accountNumber", "0");
                    args.putString("clientName", clientName);
                    args.putString("status", status);

                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new ConfirmOrderActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                } else {
                    Toast.makeText(getContext(), "please select funds", Toast.LENGTH_SHORT).show();
                }

            }

            if (status.equalsIgnoreCase("5")) {
                swpRequestBodyObjectArrayList.clear();
                for (InvestmentCartDetailsResponse model : swpArraylist) {
                    if (model.isSelected()) {
                        InvestmentCartDetailsResponse requestBodyObject = new InvestmentCartDetailsResponse();

                        requestBodyObject.setTxnAmountUnit(model.getTxnAmountUnit());

                        requestBodyObject.setAMCName(model.getAMCName());
                        requestBodyObject.setAddPurchaseMinAmt(model.getAddPurchaseMinAmt());
                        requestBodyObject.setAssetClassCode(model.getAssetClassCode());
                        requestBodyObject.setAwaitingHoldingFundValue(model.getAwaitingHoldingFundValue());
                        requestBodyObject.setAwaitingHoldingUnit(model.getAwaitingHoldingUnit());
                        requestBodyObject.setCostofInvestment(model.getCostofInvestment());
                        requestBodyObject.setCurrentFundValue(model.getCurrentFundValue());
                        requestBodyObject.setCurrentUnits(model.getCurrentUnits());
                        requestBodyObject.setDebitDate(model.getDebitDate());
                        requestBodyObject.setDividendOption(model.getDividendOption());
                        requestBodyObject.setExistingAmount(model.getExistingAmount());
                        requestBodyObject.setExistingUnits(model.getExistingUnits());
                        requestBodyObject.setFC1CountryOfBirth(model.getFC1CountryOfBirth());
                        requestBodyObject.setFC1NetWorth(model.getFC1NetWorth());
                        requestBodyObject.setFC2CountryOfBirth(model.getFC2CountryOfBirth());
                        requestBodyObject.setFC2NetWorth(model.getFC2NetWorth());
                        requestBodyObject.setFC3CountryOfBirth(model.getFC3CountryOfBirth());
                        requestBodyObject.setFC3NetWorth(model.getFC3NetWorth());
                        requestBodyObject.setFolioNo(model.getFolioNo());
                        requestBodyObject.setFundCode(model.getFundCode());
                        requestBodyObject.setFundName(model.getFundName());
                        requestBodyObject.setICDID(model.getICDID());
                        requestBodyObject.setICID(model.getICID());
                        requestBodyObject.setIsApplyNominee(model.getIsApplyNominee());
                        requestBodyObject.setIsDividend(model.getIsDividend());
                        requestBodyObject.setL4ClientCode(model.getL4ClientCode());
                        requestBodyObject.setLatestNAV(model.getLatestNAV());
                        requestBodyObject.setLotSize(model.getLotSize());
                        requestBodyObject.setMinInvAmount(model.getMinInvAmount());
                        requestBodyObject.setMinRedeemUnit(model.getMinRedeemUnit());
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setNoOfMonth(model.getNoOfMonth());
                        requestBodyObject.setNomineeIsMinor1(model.getNomineeIsMinor1());
                        requestBodyObject.setNomineeIsMinor2(model.getNomineeIsMinor2());
                        requestBodyObject.setNomineeIsMinor3(model.getNomineeIsMinor3());
                        requestBodyObject.setNomineeShare1(model.getNomineeShare1());
                        requestBodyObject.setNomineeShare2(model.getNomineeShare2());
                        requestBodyObject.setNomineeShare3(model.getNomineeShare3());
                        requestBodyObject.setPeriod(model.getPeriod());
                        requestBodyObject.setPurchaseAllowed(model.getPurchaseAllowed());
                        requestBodyObject.setRecommendationStatus(model.getRecommendationStatus());
                        requestBodyObject.setRedeemAllowed(model.getRedeemAllowed());
                        requestBodyObject.setSIPAllowed(model.getSIPAllowed());
                        requestBodyObject.setSIPStartDate(model.getSIPStartDate());
                        requestBodyObject.setSIPStartDates(model.getSIPStartDates());
                        requestBodyObject.setSettlementBankCode(model.getSettlementBankCode());
                        requestBodyObject.setSipFrequency(model.getSipFrequency());
                        requestBodyObject.setSwitchOutAllowed(model.getSwitchOutAllowed());
                        requestBodyObject.setTargetFundCode(model.getTargetFundCode());
                        requestBodyObject.setTnCUrl(model.getTnCUrl());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setValueResearchRating(model.getValueResearchRating());

                        requestBodyObject.setDateOfBirth1(model.getDateOfBirth1());
                        requestBodyObject.setDateOfBirth2(model.getDateOfBirth2());
                        requestBodyObject.setDateOfBirth3(model.getDateOfBirth3());
                        requestBodyObject.setNomineeName1(model.getNomineeName1());
                        requestBodyObject.setNomineeName2(model.getNomineeName2());
                        requestBodyObject.setNomineeName3(model.getNomineeName3());


                        requestBodyObject.setAllorPartial(model.getAllorPartial());
                        requestBodyObject.setAmountOrUnit(model.getAmountOrUnit());
                        requestBodyObject.setFrequency(model.getFrequency());
                        requestBodyObject.setSchemeOfferLink(model.getSchemeOfferLink());
                        requestBodyObject.setNomineeRelationship1(model.getNomineeRelationship1());
                        requestBodyObject.setNomineeRelationship2(model.getNomineeRelationship2());
                        requestBodyObject.setNomineeRelationship3(model.getNomineeRelationship3());


                        //   requestBodyObject.setClientCode(model.getDateOfBirth3());

                        swpRequestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (swpRequestBodyObjectArrayList.size() > 0) {
                    Bundle args = new Bundle();
                    args.putSerializable("schemeResponseArrayList", swpRequestBodyObjectArrayList);
                    args.putString("bankCode", "0");
                    args.putString("bankSource", "0");
                    args.putString("accountNumber", "0");
                    args.putString("clientName", clientName);
                    args.putString("status", status);

                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new ConfirmOrderActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                } else {
                    Toast.makeText(getContext(), "please select funds", Toast.LENGTH_SHORT).show();
                }

            }
            if (status.equalsIgnoreCase("6")) {
                stpRequestBodyObjectArrayList.clear();
                for (InvestmentCartDetailsResponse model : stpArraylist) {
                    if (model.isSelected()) {
                        InvestmentCartDetailsResponse requestBodyObject = new InvestmentCartDetailsResponse();

                        requestBodyObject.setTxnAmountUnit(model.getTxnAmountUnit());

                        requestBodyObject.setAMCName(model.getAMCName());
                        requestBodyObject.setAddPurchaseMinAmt(model.getAddPurchaseMinAmt());
                        requestBodyObject.setAssetClassCode(model.getAssetClassCode());
                        requestBodyObject.setAwaitingHoldingFundValue(model.getAwaitingHoldingFundValue());
                        requestBodyObject.setAwaitingHoldingUnit(model.getAwaitingHoldingUnit());
                        requestBodyObject.setCostofInvestment(model.getCostofInvestment());
                        requestBodyObject.setCurrentFundValue(model.getCurrentFundValue());
                        requestBodyObject.setCurrentUnits(model.getCurrentUnits());
                        requestBodyObject.setDebitDate(model.getDebitDate());
                        requestBodyObject.setDividendOption(model.getDividendOption());
                        requestBodyObject.setExistingAmount(model.getExistingAmount());
                        requestBodyObject.setExistingUnits(model.getExistingUnits());
                        requestBodyObject.setFC1CountryOfBirth(model.getFC1CountryOfBirth());
                        requestBodyObject.setFC1NetWorth(model.getFC1NetWorth());
                        requestBodyObject.setFC2CountryOfBirth(model.getFC2CountryOfBirth());
                        requestBodyObject.setFC2NetWorth(model.getFC2NetWorth());
                        requestBodyObject.setFC3CountryOfBirth(model.getFC3CountryOfBirth());
                        requestBodyObject.setFC3NetWorth(model.getFC3NetWorth());
                        requestBodyObject.setFolioNo(model.getFolioNo());
                        requestBodyObject.setFundCode(model.getFundCode());
                        requestBodyObject.setFundName(model.getFundName());
                        requestBodyObject.setICDID(model.getICDID());
                        requestBodyObject.setICID(model.getICID());
                        requestBodyObject.setIsApplyNominee(model.getIsApplyNominee());
                        requestBodyObject.setIsDividend(model.getIsDividend());
                        requestBodyObject.setL4ClientCode(model.getL4ClientCode());
                        requestBodyObject.setLatestNAV(model.getLatestNAV());
                        requestBodyObject.setLotSize(model.getLotSize());
                        requestBodyObject.setMinInvAmount(model.getMinInvAmount());
                        requestBodyObject.setMinRedeemUnit(model.getMinRedeemUnit());
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setNoOfMonth(model.getNoOfMonth());
                        requestBodyObject.setNomineeIsMinor1(model.getNomineeIsMinor1());
                        requestBodyObject.setNomineeIsMinor2(model.getNomineeIsMinor2());
                        requestBodyObject.setNomineeIsMinor3(model.getNomineeIsMinor3());
                        requestBodyObject.setNomineeShare1(model.getNomineeShare1());
                        requestBodyObject.setNomineeShare2(model.getNomineeShare2());
                        requestBodyObject.setNomineeShare3(model.getNomineeShare3());
                        requestBodyObject.setPeriod(model.getPeriod());
                        requestBodyObject.setPurchaseAllowed(model.getPurchaseAllowed());
                        requestBodyObject.setRecommendationStatus(model.getRecommendationStatus());
                        requestBodyObject.setRedeemAllowed(model.getRedeemAllowed());
                        requestBodyObject.setSIPAllowed(model.getSIPAllowed());
                        requestBodyObject.setSIPStartDate(model.getSIPStartDate());
                        requestBodyObject.setSIPStartDates(model.getSIPStartDates());
                        requestBodyObject.setSettlementBankCode(model.getSettlementBankCode());
                        requestBodyObject.setSipFrequency(model.getSipFrequency());
                        requestBodyObject.setSwitchOutAllowed(model.getSwitchOutAllowed());
                        requestBodyObject.setTargetFundCode(model.getTargetFundCode());
                        requestBodyObject.setTnCUrl(model.getTnCUrl());
                        requestBodyObject.setTransactionType(model.getTransactionType());
                        requestBodyObject.setValueResearchRating(model.getValueResearchRating());

                        requestBodyObject.setDateOfBirth1(model.getDateOfBirth1());
                        requestBodyObject.setDateOfBirth2(model.getDateOfBirth2());
                        requestBodyObject.setDateOfBirth3(model.getDateOfBirth3());
                        requestBodyObject.setNomineeName1(model.getNomineeName1());
                        requestBodyObject.setNomineeName2(model.getNomineeName2());
                        requestBodyObject.setNomineeName3(model.getNomineeName3());


                        requestBodyObject.setAllorPartial(model.getAllorPartial());
                        requestBodyObject.setAmountOrUnit(model.getAmountOrUnit());
                        requestBodyObject.setFrequency(model.getFrequency());
                        requestBodyObject.setSchemeOfferLink(model.getSchemeOfferLink());
                        requestBodyObject.setNomineeRelationship1(model.getNomineeRelationship1());
                        requestBodyObject.setNomineeRelationship2(model.getNomineeRelationship2());
                        requestBodyObject.setNomineeRelationship3(model.getNomineeRelationship3());


                        //   requestBodyObject.setClientCode(model.getDateOfBirth3());

                        stpRequestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (stpRequestBodyObjectArrayList.size() > 0) {
                    Bundle args = new Bundle();
                    args.putSerializable("schemeResponseArrayList", stpRequestBodyObjectArrayList);
                    args.putString("bankCode", "0");
                    args.putString("bankSource", "0");
                    args.putString("accountNumber", "0");
                    args.putString("clientName", clientName);
                    args.putString("status", status);

                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new ConfirmOrderActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                } else {
                    Toast.makeText(getContext(), "please select funds", Toast.LENGTH_SHORT).show();
                }

            }


        }


    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, false);
    }

    public void replaceFragments(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    // buy adapter
    private void setInvestmentBuyAdapter(ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        //    Toast.makeText(getContext(),investmentCartDetailsResponseArrayList.get(0).getDividendOption(),Toast.LENGTH_SHORT).show();
        investmentBuyAdapter = new InvestmentBuyAdapter(context, investmentCartDetailsResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerBuy.setLayoutManager(linearLayoutManager);
        recyclerBuy.setAdapter(investmentBuyAdapter);
    }


    // sip adapter
    private void setInvestmentSIPAdapter(ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        investmentSIPAdapter = new InvestmentSIPAdapter(context, investmentCartDetailsResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerSIP.setLayoutManager(linearLayoutManager);
        recyclerSIP.setAdapter(investmentSIPAdapter);
    }

    // redeem adapter
    private void setInvestmentRedeemAdapter(ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        investmentRedeemAdapter = new InvestmentRedeemAdapter(context, investmentCartDetailsResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerRedeem.setLayoutManager(linearLayoutManager);
        recyclerRedeem.setAdapter(investmentRedeemAdapter);
    }

    // switch adapter
    private void setInvestmentSwitchAdapter(ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        investmentSwitchAdapter = new InvestmentSwitchAdapter(context, investmentCartDetailsResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerSwitch.setLayoutManager(linearLayoutManager);
        recyclerSwitch.setAdapter(investmentSwitchAdapter);
    }

    // swp adapter
    private void setInvestmentSWPAdapter(ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        investmentSWPAdapter = new InvestmentSWPAdapter(context, investmentCartDetailsResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerSWP.setLayoutManager(linearLayoutManager);
        recyclerSWP.setAdapter(investmentSWPAdapter);
    }

    // STP adapter
    private void setInvestmentSTPAdapter(ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        investmentSTPAdapter = new InvestmentSTPAdapter(context, investmentCartDetailsResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerSTP.setLayoutManager(linearLayoutManager);
        recyclerSTP.setAdapter(investmentSTPAdapter);
    }

    // adapter buy delete listener
    @Override
    public void onItemDeleteListener(String ICDID, int position, String Amount) {
        //  Toast.makeText(getContext(), ICDID, Toast.LENGTH_SHORT).show();

        deleteToCartResponse(ICDID);

        double totalAmt = 0;

        //   buyArraylist.remove(position);

        for (int i = 0; i < buyArraylist.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(buyArraylist.get(i).getTxnAmountUnit()));
        }

        double amount = totalAmt - Double.parseDouble(Amount);

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + amount)));
    }

    // sip
    @Override
    public void onSipItemDeleteListener(String id, int position, String Amount) {
        deleteToCartResponse(id);

        double totalAmt = 0;

        //   buyArraylist.remove(position);

        for (int i = 0; i < sipArraylist.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(sipArraylist.get(i).getTxnAmountUnit()));
        }

        double amount = totalAmt - Double.parseDouble(Amount);

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + amount)));

    }

    @Override
    public void onRedeemItemDeleteListener(String id, int position, String Amount) {

        deleteToCartResponse(id);

        double totalAmt = 0;

        //   buyArraylist.remove(position);

        for (int i = 0; i < redeemArraylist.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(redeemArraylist.get(i).getTxnAmountUnit()));
        }

        double amount = totalAmt - Double.parseDouble(Amount);

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + amount)));
    }

    @Override
    public void onSwitchItemDeleteListener(String id, int position, String Amount) {
        deleteToCartResponse(id);

        double totalAmt = 0;

        //   buyArraylist.remove(position);

        for (int i = 0; i < switchArraylist.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(switchArraylist.get(i).getTxnAmountUnit()));
        }

        double amount = totalAmt - Double.parseDouble(Amount);

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + amount)));
    }

    @Override
    public void onSwPItemDeleteListener(String id, int position, String Amount) {
        deleteToCartResponse(id);

        double totalAmt = 0;

        //   buyArraylist.remove(position);

        for (int i = 0; i < swpArraylist.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(swpArraylist.get(i).getTxnAmountUnit()));
        }

        double amount = totalAmt - Double.parseDouble(Amount);

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + amount)));
    }

    @Override
    public void onSTpItemDeleteListener(String id, int position, String Amount) {
        deleteToCartResponse(id);

        double totalAmt = 0;

        //   buyArraylist.remove(position);

        for (int i = 0; i < stpArraylist.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(stpArraylist.get(i).getTxnAmountUnit()));
        }

        double amount = totalAmt - Double.parseDouble(Amount);

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + amount)));
    }


    // api call
    private void getInvestmentCartCountApiCall(String cartName) {
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

                //    util.showProgressDialog(InvestmentCartActivity.this, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    int count = 0;
                    investmentCartCountResponseArrayList = response.body();
                    for (int i = 0; i < investmentCartCountResponseArrayList.size(); i++) {
                        if (investmentCartCountResponseArrayList.get(i).getTransactionType().equalsIgnoreCase(cartName)) {
                            txtTranCount.setText(investmentCartCountResponseArrayList.get(i).getTranCount() + " " + "Funds");
                            break;
                        } else {
                            txtTranCount.setText("0 Funds");

                        }
                    }

                    for (int i = 0; i < investmentCartCountResponseArrayList.size(); i++) {

                        count = count + Integer.parseInt(investmentCartCountResponseArrayList.get(i).getTranCount());
                    }

                    txtInvestmentCart.setText("Investment Cart" + " " + "(" + count + ")");

                    getInvestmentCartDetailsApiCall();


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


    // api call
    private void getInvestmentCartDetailsApiCall() {

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        InvestmentCartDetailsRequestBody requestBody = new InvestmentCartDetailsRequestBody();


        requestBody.setClientCode(authenticateResponse.getUserCode());
        requestBody.setParentChannelID("WMSPortal");

        InvestmentCartDetailsRequest model = new InvestmentCartDetailsRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getInvestmentCartDetails(model).enqueue(new Callback<ArrayList<InvestmentCartDetailsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<InvestmentCartDetailsResponse>> call, Response<ArrayList<InvestmentCartDetailsResponse>> response) {
                System.out.println("Cart DetailVALIDATION RESPONSEsssss: " + new Gson().toJson(response.body()));

                //    util.showProgressDialog(InvestmentCartActivity.this, false);
                if (response.isSuccessful()) {
                    buyArraylist.clear();
                    sipArraylist.clear();
                    redeemArraylist.clear();
                    switchArraylist.clear();
                    swpArraylist.clear();
                    stpArraylist.clear();

                    investmentCartDetailsResponseArrayLists = response.body();

                    for (InvestmentCartDetailsResponse item : response.body()) {
                        if (item.getTransactionType().equalsIgnoreCase("buy")) {
                            buyArraylist.add(item);
                        } else if (item.getTransactionType().equalsIgnoreCase("sip")) {
                            sipArraylist.add(item);
                        } else if (item.getTransactionType().equalsIgnoreCase("redeem")) {
                            redeemArraylist.add(item);
                        } else if (item.getTransactionType().equalsIgnoreCase("switch")) {
                            switchArraylist.add(item);
                        } else if (item.getTransactionType().equalsIgnoreCase("swp")) {
                            swpArraylist.add(item);
                        } else if (item.getTransactionType().equalsIgnoreCase("stp")) {
                            stpArraylist.add(item);
                        }
                    }

                    double totalAmt = 0;

                    for (int i = 0; i < buyArraylist.size(); i++) {
                        totalAmt = totalAmt + (Double.parseDouble(buyArraylist.get(i).getTxnAmountUnit()));
                    }

                    txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));

                    if (buyArraylist.size() > 0) {
                        txtTranCount.setText("" + buyArraylist.size() + " " + "Funds");
                    } else {
                        txtTranCount.setText("0 Funds");
                    }

                    setInvestmentBuyAdapter(buyArraylist);

                    setInvestmentSIPAdapter(sipArraylist);

                    setInvestmentRedeemAdapter(redeemArraylist);
                    setInvestmentSwitchAdapter(switchArraylist);
                    setInvestmentSWPAdapter(swpArraylist);
                    setInvestmentSTPAdapter(stpArraylist);
                    getAccountsApiCall();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InvestmentCartDetailsResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // api call
    private void getAccountsApiCall() {
        AccountRequestBody requestBody = new AccountRequestBody();

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        requestBody.setClientCode(authenticateResponse.getUserCode());

        AccountsRequest model = new AccountsRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);

        apiInterface.getAccounts(model).enqueue(new Callback<ArrayList<AccountsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AccountsResponse>> call, Response<ArrayList<AccountsResponse>> response) {
                System.out.println("VALIDATION RESPONSEsssss: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);
                if (response.isSuccessful()) {
                    accountsResponseArrayList = response.body();
                    accountsResponseArrayList.remove(0);
                    setInvestmentAccountAdapter();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccountsResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // api call
    private void getBankAccountsApiCall(String L4ClientCode) {
        util.showProgressDialog(context, true);
        BankAccountBody requestBody = new BankAccountBody();

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;


        requestBody.setClientCode(authenticateResponse.getUserCode());
        requestBody.setL4ClientCode(L4ClientCode);
        requestBody.setIsPortal("1");
        requestBody.setBankAccountTranNo("2");

        BankAccountRequest model = new BankAccountRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);

        apiInterface.getBankAccount(model).enqueue(new Callback<ArrayList<BankAccountResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<BankAccountResponse>> call, Response<ArrayList<BankAccountResponse>> response) {
                System.out.println("Bank Accoiut Response: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);
                if (response.isSuccessful()) {
                    bankAccountResponseArrayList = response.body();
                    bankAccountResponseArrayList.remove(0);
                    setBankAccountAdapter();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BankAccountResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // delete cart api
    private void deleteToCartResponse(String ICDID) {
        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_DELETE_TO_CART);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();
            requestBodyObject.setInputmode("1");
            requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());
            requestBodyObject.setParentChannelID("WMSPortal");
            requestBodyObject.setICDID(ICDID);
            // requestBodyObject.setChannelID("Transaction");
            requestBodyObjectArrayList.add(requestBodyObject);
        }

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        GlobalRequestObjectArray globalRequestObject = new GlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addInvestmentCart(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    getInvestmentCartCountRefreshApiCall(cartName);
                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // api call
    private void getInvestmentCartCountRefreshApiCall(String cartName) {
        // util.showProgressDialog(context, true);

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
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    int count = 0;
                    investmentCartCountResponseArrayList = response.body();
                    for (int i = 0; i < investmentCartCountResponseArrayList.size(); i++) {
                        if (investmentCartCountResponseArrayList.get(i).getTransactionType().equalsIgnoreCase(cartName)) {
                            txtTranCount.setText(investmentCartCountResponseArrayList.get(i).getTranCount() + " " + "Funds");
                            break;
                        } else {
                            txtTranCount.setText("0 Funds");
                        }
                    }

                    for (int i = 0; i < investmentCartCountResponseArrayList.size(); i++) {

                        count = count + Integer.parseInt(investmentCartCountResponseArrayList.get(i).getTranCount());
                    }

                    txtInvestmentCart.setText("Investment Cart" + " " + "(" + count + ")");

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



    //check availabler acount api call
    private void getAvailableBalanceApiCall(String accountNumber) {
        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse=BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setBankAccountNumber(accountNumber);
        requestBodyObject.setClientCode(authenticateResponse.getUserCode());
        requestBodyObject.setValueDate(dateTime);
        requestBodyObject.setAmount("0");
        requestBodyObject.setSessionNo("Z_jC2WyEZXQEvq48B0d-b8PYd-rrZJrXlh0p3_Q-JOkP1goHvzCY!-406898623!1639651568772");
        requestBodyObject.setAuthToken("TRACENUMBER");



        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.GetAccountDetails(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<GetAccountDetailResponse>() {

            @Override
            public void onResponse(Call<GetAccountDetailResponse> call, Response<GetAccountDetailResponse> response) {

                System.out.println("Available balance VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    try {
                        availableBalnce=Double.parseDouble(response.body().getCustomerBankAccounts().get(0).getAvailableBalance());
                        linearBalance.setVisibility(View.VISIBLE);
                        txtAvailableBalance.setText("₹"+response.body().getCustomerBankAccounts().get(0).getAvailableBalance());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetAccountDetailResponse> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void getDetail(Fragment fragment) {
        replaceFragments(fragment);
    }


    // plan Dialog
    private void openAllPlanDialog(ArrayList<? extends Object> arrayList, String status) {
        PopupDialog allPlanDialog = new PopupDialog(getContext(), arrayList, this, status);
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onItemDeleteListener(String name, String fundCode, String status, String bankSources, String accountNumbers) {
        if (status.equalsIgnoreCase("1")) {
            txtInvestmentAcoount.setText(name);
            clientName = name;
            getBankAccountsApiCall(fundCode);
        }

        if (status.equalsIgnoreCase("2"))
        {
            txtBankAccount.setText(name);
            bankCode = fundCode;
            bankSource = bankSources;
            accountNumber = accountNumbers;
            getAvailableBalanceApiCall(accountNumber);
        }

    }
}
