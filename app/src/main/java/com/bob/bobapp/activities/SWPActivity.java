package com.bob.bobapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bob.bobapp.dialog.PopupDialog;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.OnGenericListener;
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

public class SWPActivity extends BaseFragment implements OnGenericListener {
    private AppCompatTextView txtName, txtFolioNumber, investmentAccountName, txtCurrentFundValue,
            txtSealableUnits, txtAmount, txtUnits, txtSellAmount, txtLatestFrequency, txtDay;
    private AppCompatEditText edtAMount, edtInstallments;
    private AppCompatSpinner spineerSwitchFund, spineerFrequency, spineerDay;
    private AppCompatButton btnAddToCart;
    private String frequency = "", sipStartDates = "", l4ClientCode, AmountOrUnit = "A", AllorPartial = "Partial",
            amount, noOfInstallments, finalSipStartDate, IsDividend, LatestNAV, DividendOption, targetFundName,
            targetFundCode, ValueResearchRating, FundCode, selectFrequency = "", finalSipDate, SipDate, year, currentDay, currentMonth,
            dateTime, MarketValue, selectSeable = "1";
    private Context context;
    private Util util;
    private ClientHoldingObject clientHoldingObject;
    private SwitchFundAdapter switchFundAdapter;
    private SipFrequencyAdapter sipFrequencyAdapter;
    private SipDateAdapter sipDateAdapter;
    private ArrayList<TranferSchemeResponse> switchFundArrayList = new ArrayList<>();
    private ArrayList<String> frequencyList;
    private ArrayList<String> sipStartDatesList;
    private ArrayList<String> finalFrequencyList = new ArrayList<>();
    private ArrayList<String> finalSipStartDatesList;
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();
    private double quantity, AwaitingHoldingUnit, SalableUnit, Awatingholdingamt, existingAmount;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat formatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.swp_activity, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getExtraDataFromIntent();
        setData();

        getTransferFundResponse();
    }

    // set data here...
    private void setData() {
        txtName.setText(clientHoldingObject.getSchemeName());
        txtFolioNumber.setText(clientHoldingObject.getFolio());
        l4ClientCode = clientHoldingObject.getL4Client_Code();
        txtCurrentFundValue.setText(formatter.format(Double.parseDouble(clientHoldingObject.getMarketValue())));
        quantity = Double.parseDouble(clientHoldingObject.getQuantity());
        MarketValue = clientHoldingObject.getMarketValue();
    }

    // get data
    private void getExtraDataFromIntent() {
        if (getArguments() != null) {
            String response = getArguments().getString(IntentKey.RESPONSE_KEY);
            clientHoldingObject = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }


    @Override
    protected void getIds(View view) {
        formatter = new DecimalFormat("###,###,##0.00");
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = simpleDateFormat.format(calendar.getTime());
        String s = dateTime;
        String[] arrayString = s.split("-");

        year = arrayString[0];
        currentMonth = arrayString[1];
        currentDay = arrayString[2];


        txtName = view.findViewById(R.id.txtName);
        txtFolioNumber = view.findViewById(R.id.txtFolioNumber);
        spineerSwitchFund = view.findViewById(R.id.spineerSwitchFund);
        spineerFrequency = view.findViewById(R.id.spineerFrequency);
        spineerDay = view.findViewById(R.id.spineerDay);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        txtCurrentFundValue = view.findViewById(R.id.txtCurrentFundValue);
        txtSealableUnits = view.findViewById(R.id.txtSealableUnits);
        txtAmount = view.findViewById(R.id.txtAmount);
        txtUnits = view.findViewById(R.id.txtUnits);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        edtAMount = view.findViewById(R.id.edtAMount);
        edtInstallments = view.findViewById(R.id.edtInstallments);
        txtSellAmount = view.findViewById(R.id.txtSellAmount);
        txtLatestFrequency = view.findViewById(R.id.txtLatestFrequency);
        txtDay = view.findViewById(R.id.txtDay);

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
        txtLatestFrequency.setOnClickListener(this);
        txtDay.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.imgBack.setOnClickListener(this);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.title.setText("SWP");
        BOBActivity.llMenu.setVisibility(View.GONE);
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
            txtSellAmount.setText("Sell Amount");
            AmountOrUnit = "A";
            AllorPartial = "Partial";
            // AllorPartial = "Amount";
            //     txtAmount.setBackgroundColor(Color.parseColor("#f57222"));
            txtAmount.setTextColor(Color.parseColor("#1F3C66"));

            //  txtUnits.setBackgroundColor(Color.parseColor("#ffffff"));
            txtUnits.setTextColor(Color.parseColor("#817D7D"));
        }

        if (id == R.id.txtUnits) {
            selectSeable = "2";
            txtSellAmount.setText("Sell Unit");
            AmountOrUnit = "U";
            AllorPartial = "Partial";
            //  amt = "Units";
            //  txtUnits.setBackgroundColor(Color.parseColor("#f57222"));
            txtUnits.setTextColor(Color.parseColor("#1F3C66"));

            //    txtAmount.setBackgroundColor(Color.parseColor("#ffffff"));
            txtAmount.setTextColor(Color.parseColor("#817D7D"));
        }

        if (id == R.id.txtDay) {
            if (txtLatestFrequency.getText().toString().equalsIgnoreCase("select")) {
                Toast.makeText(getContext(), "Please select frequency", Toast.LENGTH_SHORT).show();
            } else {
                openAllPlanDialog(finalSipStartDatesList, "2");
            }
        }


        if (id == R.id.txtLatestFrequency) {
            openAllPlanDialog(finalFrequencyList, "3");
        }


        if (id == R.id.btnAddToCart) {
            amount = edtAMount.getText().toString().trim();
            noOfInstallments = edtInstallments.getText().toString().trim();


            if (noOfInstallments.isEmpty() || noOfInstallments.equalsIgnoreCase("")) {
                edtInstallments.setFocusable(true);
                edtInstallments.requestFocus();
                Toast.makeText(getContext(), "please enter no of installments", Toast.LENGTH_SHORT).show();
            } else if (txtLatestFrequency.getText().toString().isEmpty() || txtLatestFrequency.getText().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(getContext(), "Please select frequency", Toast.LENGTH_SHORT).show();
            } else if (txtDay.getText().toString().isEmpty() || txtDay.getText().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(getContext(), "Please select day", Toast.LENGTH_SHORT).show();
            } else if (amount.isEmpty() || amount.equalsIgnoreCase("")) {
                edtAMount.setFocusable(true);
                edtAMount.requestFocus();
                Toast.makeText(getContext(), "please enter saleable amount", Toast.LENGTH_SHORT).show();
            } else if (selectSeable.equalsIgnoreCase("2")) {
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

    // sip frequency adapter
//    private void setFrequencyAdapter(ArrayList<String> arrayList) {
//        sipFrequencyAdapter = new SipFrequencyAdapter(getContext(), arrayList);
//        spineerFrequency.setAdapter(sipFrequencyAdapter);
//
//        spineerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//                selectFrequency = arrayList.get(position);
//                String Day = sipStartDatesList.get(position);
//                finalSipStartDate = Day;
//
//                finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));
//
//                setDateAdapter(finalSipStartDatesList);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//    }

    // get SealableUnits
    private void getSealableUnits(double AwaitingHoldingUnit) {
        SalableUnit = quantity - AwaitingHoldingUnit;
        if (SalableUnit > 0) {
            txtSealableUnits.setText(formatter.format(Double.parseDouble("" + SalableUnit)));
        } else {
            txtSealableUnits.setText("0");

        }

    }


    // day frequency adapter
//    private void setDateAdapter(ArrayList<String> arrayList) {
//        sipDateAdapter = new SipDateAdapter(getContext(), arrayList);
//        spineerDay.setAdapter(sipDateAdapter);
//
//        spineerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectDate = arrayList.get(position);
//
//                String dskj = "";
//
//                if (Integer.parseInt(selectDate) < Integer.parseInt(currentDay)) {
//                    if (Integer.parseInt(currentMonth) < 12) {
//                        String asd = String.valueOf(Integer.parseInt(currentMonth) + 1);
//                        dskj = asd;
//                        SipDate = (year + "-" + dskj + "-" + selectDate);
//                    } else {
//                        String setYear = String.valueOf(Integer.parseInt(year) + 1);
//                        String monthsss = "1";
//                        SipDate = (setYear + "-" + monthsss + "-" + selectDate);
//                    }
//
//                } else {
//                    SipDate = (year + "-" + currentMonth + "-" + selectDate);
//
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//
//
//    }

    //  adapter
    private void setAdapter(ArrayList<TranferSchemeResponse> arrayList) {
        switchFundAdapter = new SwitchFundAdapter(getContext(), arrayList);
        spineerSwitchFund.setAdapter(switchFundAdapter);
        spineerSwitchFund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                targetFundName = arrayList.get(position).getName();
                targetFundCode = arrayList.get(position).getCode();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    // fund transfer api
    private void getTransferFundResponse() {
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

                    getValidationResponse();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TranferSchemeResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                System.out.println("error: " + t.getLocalizedMessage());

                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // frequencty and day
    private void getValidationResponse() {

        // util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());

        requestBodyObject.setTransactionType("STP");

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

                //   util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    frequency = response.body().getSipFrequency();

                    frequencyList = new ArrayList<>(Arrays.asList(frequency.split(",")));

                    finalFrequencyList.clear();

                    for (String s : frequencyList) {
                        if (s.equalsIgnoreCase("0")) {
                            finalFrequencyList.add("Daily");
                        }
                        if (s.equalsIgnoreCase("1")) {
                            finalFrequencyList.add("Weekly");
                        }
                        if (s.equalsIgnoreCase("4")) {
                            finalFrequencyList.add("Monthly");
                        }
                        if (s.equalsIgnoreCase("5")) {
                            finalFrequencyList.add("Quarterly");
                        }

                        if (s.equalsIgnoreCase("6")) {
                            finalFrequencyList.add("HalfYearly");
                        }
                        if (s.equalsIgnoreCase("7")) {
                            finalFrequencyList.add("Yearly");
                        }
                    }
                    //        setFrequencyAdapter(finalFrequencyList);

                    sipStartDates = response.body().getSipStartDates();

                    sipStartDatesList = new ArrayList<>(Arrays.asList(sipStartDates.split("\\|,")));

                    String Day = sipStartDatesList.get(0);

                    finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));

                    //      setDateAdapter(finalFrequencyList);

                    callAccountDetailAPI();

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

    // account name
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

                // util.showProgressDialog(getContext(), false);

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
    }

    private void getMFValidationResponse() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());

        requestBodyObject.setTransactionType("SWP");

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
                    FundCode = response.body().getFundOption();
                    LatestNAV = response.body().getLatestNAV();
                    ValueResearchRating = response.body().getValueResearchRating();
                    String IsDividends = response.body().getIsDividend();
                    if (IsDividends.equalsIgnoreCase("0")) {
                        IsDividend = "false";
                    } else {
                        IsDividend = "true";
                    }
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

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            requestBodyObject.setParentChannelID("WMSPortal");
            //   requestBodyObject.setICDID("768");//FundCode
            requestBodyObject.setLatestNAV(LatestNAV);
            requestBodyObject.setFundCode(clientHoldingObject.getCommonScripCode());
            requestBodyObject.setFundName(clientHoldingObject.getSchemeName());
            requestBodyObject.setValueResearchRating(ValueResearchRating);
            requestBodyObject.setFolio(txtFolioNumber.getText().toString().trim());
            requestBodyObject.setFolioNo(txtFolioNumber.getText().toString().trim());
            requestBodyObject.setCostofInvestment("0");
            requestBodyObject.setCurrentUnits("" + quantity);
            requestBodyObject.setCurrentFundValue(MarketValue);
            requestBodyObject.setAllorPartial(AllorPartial);
            //    requestBodyObject.setAmountOrUnit(amt);
            requestBodyObject.setAmountOrUnit(AmountOrUnit);
            requestBodyObject.setAmtOrUnit(AmountOrUnit);
            requestBodyObject.setTxnAmountUnit(amount);
            requestBodyObject.setTransactionAmountUnit(amount);
            requestBodyObject.setTranAmt("0");
            requestBodyObject.setTranUnit("0");
            requestBodyObject.setTranType("s");
            requestBodyObject.setTransactionType("SWP");
            requestBodyObject.setAssetClassCode("0");
            requestBodyObject.setAssetClassName("");
            requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
            requestBodyObject.setDebitBankCode("0");
            requestBodyObject.setDebitBankSource("0");
            requestBodyObject.setBankAccountNo("0");
            requestBodyObject.setIsDividend(IsDividend);
            requestBodyObject.setSwitchDividendOption(DividendOption);
            requestBodyObject.setSettlementBankCode("0");
            requestBodyObject.setPaymentMode("7");
            requestBodyObject.setTargetFundName("");
            requestBodyObject.setTargetFundCode("");
            requestBodyObject.setStartDate(finalSipDate);
            requestBodyObject.setSipStartDates(finalSipStartDate);
            requestBodyObject.setSipStartDate(finalSipDate);
            requestBodyObject.setNoofMonth(noOfInstallments);
            requestBodyObject.setFrequency(selectFrequency);
            requestBodyObject.setPlatform_id("0");
            requestBodyObject.setOrderChannelID("14");
            requestBodyObject.setClientIP("106.66.247.165");
            requestBodyObject.setClientMobileNo("918208624908");
            requestBodyObject.setOTPPassword("");
            requestBodyObject.setExternalCode("");
            requestBodyObject.setChannel("");
            requestBodyObject.setClientUCC(authenticateResponse.getClientUCC());
            requestBodyObject.setSessionID("ydAHnylmwxk55EXC6gwz13-zp7lOt7kQYY2fxqMJ142CuU5RTjOg!544971065!1632215443814");
            requestBodyObject.setMpinMode("OTPDisabled");
            requestBodyObject.setRegistrationRefId("");
            requestBodyObject.setMpinEncValue("hMWQtHeORTUXdby5oecKfA==");


            // extra params
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
            requestBodyObject.setPeriod(noOfInstallments);
            requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());
            requestBodyObject.setInputmode("2");
            requestBodyObject.setSettlementBankCode("0");
            requestBodyObject.setFundRiskRating("");


//            requestBodyObject.setChannelID("Transaction");


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
                    edtInstallments.setText("");
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


    // plan Dialog
    private void openAllPlanDialog(ArrayList<? extends Object> arrayList, String status) {
        PopupDialog allPlanDialog = new PopupDialog(getContext(), arrayList, this, status);
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onItemDeleteListener(String name, String fundCode, String status, String bankSource, String accountNumber) {

        if (status.equalsIgnoreCase("3")) {
            finalSipStartDatesList.clear();
            txtLatestFrequency.setText(name);
            selectFrequency = name;

            String Day = sipStartDatesList.get(Integer.parseInt(fundCode));

            finalSipStartDate = Day;

            finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));
        }

        if (status.equalsIgnoreCase("2")) {
            txtDay.setText(name);
            String selectDate = name;

            String dskj = "";

            if (Integer.parseInt(selectDate) < Integer.parseInt(currentDay)) {
                if (Integer.parseInt(currentMonth) < 12) {
                    String asd = String.valueOf(Integer.parseInt(currentMonth) + 1);
                    dskj = asd;
                    SipDate = (year + "-" + dskj + "-" + selectDate);
                } else {
                    String setYear = String.valueOf(Integer.parseInt(year) + 1);
                    String monthsss = "1";
                    SipDate = (setYear + "-" + monthsss + "-" + selectDate);
                }

            } else {
                SipDate = (year + "-" + currentMonth + "-" + selectDate);

            }
            String[] arrayString = SipDate.split("-");

            String year = arrayString[0];
            String month = arrayString[1];
            String date = arrayString[2];

            if (month.length() == 1) {
                month = "0" + month;
            }

            if (date.length() == 1) {
                date = "0" + date;
            }

            finalSipDate = year + month + date;


        }


    }

}
