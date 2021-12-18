package com.bob.bobapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.SipDateAdapter;
import com.bob.bobapp.adapters.SipFrequencyAdapter;
import com.bob.bobapp.adapters.SwitchFundAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.MFOrderValidationResponse;
import com.bob.bobapp.api.response_object.TranferSchemeResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemActivity extends BaseFragment {
    private AppCompatTextView txtName, txtFolioNumber, investmentAccountName, txtAmount, txtUnits,
            txtFullSwitch, txtCurrentFundValue, txtSealableUnits, txtSellAmount;
    private AppCompatButton btnAddToCart;
    private AppCompatEditText edtAMount;
    private Context context;
    private Util util;
    private ClientHoldingObject clientHoldingObject;
    private AppCompatSpinner spineerSwitchFund;
    private SwitchFundAdapter switchFundAdapter;
    private ArrayList<TranferSchemeResponse> switchFundArrayList = new ArrayList<>();
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();
    private String l4ClientCode, amount, targetFundName, targetFundCode, DividendOption, LatestNAV,
            IsDividend, FundOption, AmountOrUnit = "A", AllorPartial = "Partial", MarketValue, selectSeable = "1";
    private double quantity, AwaitingHoldingUnit, SalableUnit, Awatingholdingamt, existingAmount;
    private DecimalFormat formatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.redeem_activity, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getExtraDataFromIntent();
        setData();
        //getTransferFundResponse();

        getValidationResponse();
    }

    // set data here...
    private void setData() {
        txtName.setText(clientHoldingObject.getSchemeName());
        txtFolioNumber.setText(clientHoldingObject.getFolio());
        txtCurrentFundValue.setText(formatter.format(Double.parseDouble(clientHoldingObject.getMarketValue())));
        l4ClientCode = clientHoldingObject.getL4Client_Code();
        quantity = Double.parseDouble(clientHoldingObject.getQuantity());
        MarketValue = clientHoldingObject.getMarketValue();

    }


    private void getExtraDataFromIntent() {

        if (getArguments() != null) {

            String response = getArguments().getString(IntentKey.RESPONSE_KEY);

            clientHoldingObject = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }


    @Override
    protected void getIds(View view) {
        formatter = new DecimalFormat("###,###,##0.00");
        txtName = view.findViewById(R.id.txtName);
        txtFolioNumber = view.findViewById(R.id.txtFolioNumber);
        spineerSwitchFund = view.findViewById(R.id.spineerSwitchFund);
        txtAmount = view.findViewById(R.id.txtAmount);
        txtUnits = view.findViewById(R.id.txtUnits);
        txtFullSwitch = view.findViewById(R.id.txtFullSwitch);
        edtAMount = view.findViewById(R.id.edtAMount);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        txtCurrentFundValue = view.findViewById(R.id.txtCurrentFundValue);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        txtSealableUnits = view.findViewById(R.id.txtSealableUnits);
        txtSellAmount = view.findViewById(R.id.txtSellAmount);

        edtAMount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });

    }

    @Override
    protected void handleListener() {
        txtAmount.setOnClickListener(this);
        txtUnits.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
        txtFullSwitch.setOnClickListener(this);
        BOBActivity.imgBack.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Reedem");

    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        }


        if (id == R.id.txtAmount) {
            selectSeable = "1";
            edtAMount.setText("");
            edtAMount.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            AmountOrUnit = "A";
            AllorPartial = "Partial";
            // txtAmount.setBackgroundColor(Color.parseColor("#f57222"));
            txtAmount.setTextColor(Color.parseColor("#1F3C66"));

            //     txtUnits.setBackgroundColor(Color.parseColor("#ffffff"));
            txtUnits.setTextColor(Color.parseColor("#817D7D"));

            //  txtFullSwitch.setBackgroundColor(Color.parseColor("#ffffff"));
            txtFullSwitch.setTextColor(Color.parseColor("#817D7D"));
            txtSellAmount.setText("Sell Amount");
        }

        if (id == R.id.txtUnits) {
            selectSeable = "2";
            edtAMount.setText("");
            edtAMount.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            txtSellAmount.setText("Sell Units");
            AmountOrUnit = "U";
            AllorPartial = "Partial";
            //    txtUnits.setBackgroundColor(Color.parseColor("#f57222"));
            txtUnits.setTextColor(Color.parseColor("#1F3C66"));

            //    txtAmount.setBackgroundColor(Color.parseColor("#ffffff"));
            txtAmount.setTextColor(Color.parseColor("#817D7D"));

            // txtFullSwitch.setBackgroundColor(Color.parseColor("#ffffff"));
            txtFullSwitch.setTextColor(Color.parseColor("#817D7D"));
        }
        if (id == R.id.txtFullSwitch) {
            selectSeable = "3";
            edtAMount.setText("");
            edtAMount.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            if (SalableUnit > 0) {
                edtAMount.setText("" + SalableUnit);
            } else {
                edtAMount.setText("0");
            }
            txtSellAmount.setText("Saleable Units");
            AmountOrUnit = "U";
            AllorPartial = "All";
            //   txtUnits.setBackgroundColor(Color.parseColor("#ffffff"));
            txtUnits.setTextColor(Color.parseColor("#817D7D"));


            //  txtAmount.setBackgroundColor(Color.parseColor("#ffffff"));
            txtAmount.setTextColor(Color.parseColor("#817D7D"));

            // txtFullSwitch.setBackgroundColor(Color.parseColor("#f57222"));
            txtFullSwitch.setTextColor(Color.parseColor("#1F3C66"));
        }

        if (id == R.id.btnAddToCart) {
            amount = edtAMount.getText().toString().trim();
            if (amount.isEmpty() || amount.equalsIgnoreCase("")) {
                edtAMount.setFocusable(true);
                edtAMount.requestFocus();
                Toast.makeText(getContext(), "please enter saleable amount", Toast.LENGTH_SHORT).show();
            } else if (selectSeable.equalsIgnoreCase("2") || selectSeable.equalsIgnoreCase("3")) {
                if (Double.parseDouble(amount) > SalableUnit) {
                    Toast.makeText(getContext(), "Saleable Units should not greater than Actual Saleable Units  ", Toast.LENGTH_SHORT).show();
                } else {
                    addInvCartAPIResponse();
                }
            } else if (selectSeable.equalsIgnoreCase("1")) {
                double amt = Double.parseDouble(MarketValue) - (Awatingholdingamt + existingAmount);
                if (Double.parseDouble(amount) > amt) {
                    Toast.makeText(getContext(), "The amount entered for" + " " + clientHoldingObject.getSchemeName() + " " + "is greater than the amount you hold(Considering pending STP orders).Please enter the correct amount for" + " " + clientHoldingObject.getFolio(), Toast.LENGTH_LONG).show();
                } else {
                    addInvCartAPIResponse();
                }
            } else {
                addInvCartAPIResponse();
            }
        }

    }

    // sealagble units
    private void getSealableUnits(double AwaitingHoldingUnit) {
        SalableUnit = quantity - AwaitingHoldingUnit;
        if (SalableUnit > 0) {
            txtSealableUnits.setText(formatter.format(Double.parseDouble("" + SalableUnit)));
        } else {
            txtSealableUnits.setText("0");

        }
    }

    //  adapter
    private void setAdapter(ArrayList<TranferSchemeResponse> arrayList) {
        switchFundAdapter = new SwitchFundAdapter(getContext(), arrayList);
        spineerSwitchFund.setAdapter(switchFundAdapter);
        spineerSwitchFund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                targetFundName = arrayList.get(position).getName();
                targetFundCode = arrayList.get(position).getCode();
                // Toast.makeText(getContext(),targetFundCode,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }


    // fund transfer api
    private void getValidationResponse() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_ORDER);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());


        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.getTransferSchemes(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<ArrayList<TranferSchemeResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<TranferSchemeResponse>> call, Response<ArrayList<TranferSchemeResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                //    util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    switchFundArrayList = response.body();
                    setAdapter(switchFundArrayList);
                }

                callAccountDetailAPI();
            }

            @Override
            public void onFailure(Call<ArrayList<TranferSchemeResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                System.out.println("error: " + t.getLocalizedMessage());

                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // name
    private void callAccountDetailAPI() {
        try {
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

                    //  util.showProgressDialog(getContext(), false);

                    if (response.isSuccessful()) {
                        accountResponseObjectArrayList = response.body();
                        for (int i = 0; i <= accountResponseObjectArrayList.size(); i++) {
                            if (accountResponseObjectArrayList.get(i).getClientCode().equalsIgnoreCase(l4ClientCode)) {
                                investmentAccountName.setText(accountResponseObjectArrayList.get(i).getClientName());
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                    getMFValidationResponse();

                }

                @Override
                public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                    util.showProgressDialog(getContext(), false);

                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void getMFValidationResponse() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());

        requestBodyObject.setTransactionType("SWITCH");

        String tillDate = util.getCurrentDate(false);

        requestBodyObject.setTillDate(tillDate);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.getOrderValidationData(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<MFOrderValidationResponse>() {

            @Override
            public void onResponse(Call<MFOrderValidationResponse> call, Response<MFOrderValidationResponse> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    Awatingholdingamt = Double.parseDouble(response.body().getAwaitingHoldingFundValue());
                    existingAmount = Double.parseDouble(response.body().getExistingAmount());
                    AwaitingHoldingUnit = Double.parseDouble(response.body().getAwaitingHoldingUnit());
                    DividendOption = response.body().getDividendOption();
                    FundOption = response.body().getFundOption();
                    LatestNAV = response.body().getLatestNAV();
                    String IsDividends = response.body().getIsDividend();
                    if (IsDividends.equalsIgnoreCase("0")) {
                        IsDividend = "false";
                    } else {
                        IsDividend = "true";
                    }
                    //Toast.makeText(getContext(), " " + AwaitingHoldingUnit, Toast.LENGTH_SHORT).show();
                    getSealableUnits(AwaitingHoldingUnit);
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MFOrderValidationResponse> call, Throwable t) {
                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // add to cart api
    private void addInvCartAPIResponse() {
        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();
            requestBodyObject.setAllorPartial(AllorPartial);
            requestBodyObject.setAmountOrUnit(AmountOrUnit);
            requestBodyObject.setChannelID("Transaction");
            requestBodyObject.setCostofInvestment("0");
            requestBodyObject.setCurrentFundValue(MarketValue);
            requestBodyObject.setCurrentUnits("" + quantity);
            requestBodyObject.setDividendOption(DividendOption);
            requestBodyObject.setFolioNo(txtFolioNumber.getText().toString());
            requestBodyObject.setFundOption(FundOption);
            requestBodyObject.setIsDividend(IsDividend);
            requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
            requestBodyObject.setLatestNAV(LatestNAV);
            requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
            requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());
            requestBodyObject.setTransactionType("REDEEM");
            requestBodyObject.setTxnAmountUnit(amount);
            requestBodyObject.setInputmode("2");


            requestBodyObject.setParentChannelID("WMSPortal");

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
                    edtAMount.setText("");
                    showPopup();
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

    // show popup
    private void showPopup() {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.pop_dialog_transact);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        AppCompatButton btnMoveToCart = dialog.findViewById(R.id.btnMoveToCart);
        AppCompatButton btnAddMore = dialog.findViewById(R.id.btnAddMore);

        btnMoveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                InvestmentCartActivity fragment = new InvestmentCartActivity();

                Bundle bundle = new Bundle();

                bundle.putString(Constants.COMING_FROM, "BuyActivity");

                fragment.setArguments(bundle);

                replaceFragment(fragment);
            }
        });
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().onBackPressed();
            }
        });


        dialog.show();


    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }


}
