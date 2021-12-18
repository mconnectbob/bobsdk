package com.bob.bobapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.ConfirmOrderAdapter;
import com.bob.bobapp.adapters.InvestmentBuyAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.BuyGlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.BuyConfirmResponse;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.api.response_object.SchemeResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmOrderActivity extends BaseFragment {
    private Util util;
    private Context context;
    private CheckBox chkAccept;
    private TextView txtInvestmentTabName;
    private RecyclerView recyclerConfirmOrder;
    private ConfirmOrderAdapter confirmOrderAdapter;
    private ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList = new ArrayList<>();
    private AppCompatTextView txtTranCount, txtTotalAmount, txtConfirm, txtCancel;
    private DecimalFormat formatter;
    List<InvestmentCartDetailsResponse> target = new LinkedList<InvestmentCartDetailsResponse>();
    private String json, bankCode = "", bankSource = "", accountNumber = "",
            clientName = "",clientNames=" ", status = "", ContactNo = "", cureentDate = "";
    private ArrayList<BuyConfirmResponse> buyConfirmResponseArrayList = new ArrayList<>();
    private ArrayList<RMDetailResponseObject> rmDetailResponseObjectArrayList = new ArrayList<>();
    private boolean isChkSelect = false;
    private TextView txtTermsCondition;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_confirm_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void getIds(View view) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        String s = dateTime;
        String[] arrayString = s.split("-");

        String currentYear = arrayString[0];
        String currentMonth = arrayString[1];
        String currentDay = arrayString[2];

        cureentDate = currentYear + "-" + currentMonth + "-" + currentDay;

        formatter = new DecimalFormat("###,###,##0.00");
        recyclerConfirmOrder = view.findViewById(R.id.recyclerConfirmOrder);
        txtTranCount = view.findViewById(R.id.txtTranCount);
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);
        txtConfirm = view.findViewById(R.id.txtConfirm);
        txtCancel = view.findViewById(R.id.txtCancel);
        txtInvestmentTabName = view.findViewById(R.id.txtInvestmentTabName);
        chkAccept = view.findViewById(R.id.chkAccept);
        txtTermsCondition = view.findViewById(R.id.txtTermsCondition);

        chkAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isChkSelect = true;
                } else {
                    isChkSelect = false;
                }
            }
        });
    }

    @Override
    protected void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtTermsCondition.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Confirm Your Order");
        requestBodyObjectArrayList.clear();

        if (getArguments() != null) {
            requestBodyObjectArrayList = (ArrayList<InvestmentCartDetailsResponse>) getArguments().getSerializable("schemeResponseArrayList");
            bankCode = getArguments().getString("bankCode");
            bankSource = getArguments().getString("bankSource");
            accountNumber = getArguments().getString("accountNumber");
            clientName = getArguments().getString("clientName");
            status = getArguments().getString("status");
        }

//        for(int i=0;i<requestBodyObjectArrayList.size();i++)
//        {
//            Toast.makeText(getContext(),requestBodyObjectArrayList.get(i).getDividendOption(),Toast.LENGTH_SHORT).show();
//        }

        if (status.equalsIgnoreCase("1")) {
            txtInvestmentTabName.setText("Buy -");
        } else if (status.equalsIgnoreCase("2")) {
            txtInvestmentTabName.setText("Sip -");
        } else if (status.equalsIgnoreCase("3")) {
            txtInvestmentTabName.setText("Redeem -");
        } else if (status.equalsIgnoreCase("4")) {
            txtInvestmentTabName.setText("Switch -");
        } else if (status.equalsIgnoreCase("5")) {
            txtInvestmentTabName.setText("SWP -");
        } else {
            txtInvestmentTabName.setText("STP -");
        }

        if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("2") || status.equalsIgnoreCase("3") || status.equalsIgnoreCase("4") || status.equalsIgnoreCase("5") || status.equalsIgnoreCase("6")) {
            callRMDetailAPI();
        } else {
            setAdapter(clientName);
        }

        txtTranCount.setText("" + requestBodyObjectArrayList.size() + " " + "Funds");

        double totalAmt = 0;

        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
            totalAmt = totalAmt + (Double.parseDouble(requestBodyObjectArrayList.get(i).getTxnAmountUnit()));
        }

        txtTotalAmount.setText(formatter.format(Double.parseDouble("" + totalAmt)));


//        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
//            InvestmentCartDetailsResponse investmentCartDetailsResponse = new InvestmentCartDetailsResponse();
//            investmentCartDetailsResponse.setFundName(requestBodyObjectArrayList.get(i).getFundName());
//            investmentCartDetailsResponse.setTxnAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
//            target.add(investmentCartDetailsResponse);
//        }

        Type listType = new TypeToken<List<InvestmentCartDetailsResponse>>() {
        }.getType();


        Gson gson = new Gson();
        json = gson.toJson(target, listType);

        System.out.println("shri " + json);

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

        if (id == R.id.txtTermsCondition) {
            //  replaceFragment(new ConfirmTermsConditonActivity());

            Intent intent = new Intent(getContext(), TermsActivity.class);
            getActivity().startActivity(intent);

//            String URL ="https://wmsuat.bankofbaroda.co.in/MoneywarePortal/Portal/TnC/TnC.html";
//            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//            CustomTabsIntent customTabsIntent = builder.build();
//            customTabsIntent.launchUrl(getContext(), Uri.parse(URL));
        }

        if (id == R.id.txtConfirm) {
            if (isChkSelect) {
                if (status.equalsIgnoreCase("1")) {
                    addInvCartAPIResponse();
                }
                if (status.equalsIgnoreCase("2")) {
                    addSipInvCartAPIResponse();
                }
                if (status.equalsIgnoreCase("3")) {
                    addRedeemInvCartAPIResponse();
                }
                if (status.equalsIgnoreCase("4")) {
                    addSwitchInvCartAPIResponse();
                }

                if (status.equalsIgnoreCase("5")) {
                    addSwpInvCartAPIResponse();
                }

                if (status.equalsIgnoreCase("6")) {
                    addSwpInvCartAPIResponse();
                }
            } else {
                Toast.makeText(getContext(), "Please select terms and conditions", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.txtCancel) {
            getActivity().onBackPressed();
        }
    }


    //  adapter
    private void setAdapter(String clientName) {
        confirmOrderAdapter = new ConfirmOrderAdapter(context, requestBodyObjectArrayList, clientName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerConfirmOrder.setLayoutManager(linearLayoutManager);
        recyclerConfirmOrder.setAdapter(confirmOrderAdapter);
    }


    // confimr
    private void addInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CONFIRM_BUY);

        ArrayList<InvestmentCartDetailsResponse> finalRequestBodyObjectArrayList = new ArrayList<InvestmentCartDetailsResponse>();

        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
            InvestmentCartDetailsResponse investmentCartDetailsResponse = new InvestmentCartDetailsResponse();

            investmentCartDetailsResponse.setTxnAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setAMCName(requestBodyObjectArrayList.get(i).getAMCName());
            investmentCartDetailsResponse.setAddPurchaseMinAmt(requestBodyObjectArrayList.get(i).getAddPurchaseMinAmt());
            investmentCartDetailsResponse.setAssetClassCode(requestBodyObjectArrayList.get(i).getAssetClassCode());
            investmentCartDetailsResponse.setAwaitingHoldingFundValue(requestBodyObjectArrayList.get(i).getAwaitingHoldingFundValue());
            investmentCartDetailsResponse.setAwaitingHoldingUnit(requestBodyObjectArrayList.get(i).getAwaitingHoldingUnit());
            investmentCartDetailsResponse.setCostofInvestment(requestBodyObjectArrayList.get(i).getCostofInvestment());
            investmentCartDetailsResponse.setCurrentFundValue(requestBodyObjectArrayList.get(i).getCurrentFundValue());
            investmentCartDetailsResponse.setCurrentUnits(requestBodyObjectArrayList.get(i).getCurrentUnits());
            investmentCartDetailsResponse.setDebitDate(requestBodyObjectArrayList.get(i).getDebitDate());
            investmentCartDetailsResponse.setDividendOption(requestBodyObjectArrayList.get(i).getDividendOption());
            investmentCartDetailsResponse.setExistingAmount(requestBodyObjectArrayList.get(i).getExistingAmount());
            investmentCartDetailsResponse.setExistingUnits(requestBodyObjectArrayList.get(i).getExistingUnits());
            investmentCartDetailsResponse.setFC1CountryOfBirth(requestBodyObjectArrayList.get(i).getFC1CountryOfBirth());
            investmentCartDetailsResponse.setFC1NetWorth(requestBodyObjectArrayList.get(i).getFC1NetWorth());
            investmentCartDetailsResponse.setFC2CountryOfBirth(requestBodyObjectArrayList.get(i).getFC2CountryOfBirth());
            investmentCartDetailsResponse.setFC2NetWorth(requestBodyObjectArrayList.get(i).getFC2NetWorth());
            investmentCartDetailsResponse.setFC3CountryOfBirth(requestBodyObjectArrayList.get(i).getFC3CountryOfBirth());
            investmentCartDetailsResponse.setFC3NetWorth(requestBodyObjectArrayList.get(i).getFC3NetWorth());
            investmentCartDetailsResponse.setFolioNo(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolio(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFundCode(requestBodyObjectArrayList.get(i).getFundCode());
            investmentCartDetailsResponse.setFundName(requestBodyObjectArrayList.get(i).getFundName());
            investmentCartDetailsResponse.setICDID(requestBodyObjectArrayList.get(i).getICDID());
            investmentCartDetailsResponse.setICID(requestBodyObjectArrayList.get(i).getICID());
            investmentCartDetailsResponse.setIsApplyNominee(requestBodyObjectArrayList.get(i).getIsApplyNominee());
            investmentCartDetailsResponse.setIsDividend(requestBodyObjectArrayList.get(i).getIsDividend());
            investmentCartDetailsResponse.setL4ClientCode(requestBodyObjectArrayList.get(i).getL4ClientCode());
            investmentCartDetailsResponse.setLatestNAV(requestBodyObjectArrayList.get(i).getLatestNAV());
            investmentCartDetailsResponse.setLotSize(requestBodyObjectArrayList.get(i).getLotSize());
            investmentCartDetailsResponse.setMinInvAmount(requestBodyObjectArrayList.get(i).getMinInvAmount());
            investmentCartDetailsResponse.setMinRedeemUnit(requestBodyObjectArrayList.get(i).getMinRedeemUnit());
            investmentCartDetailsResponse.setNextInstallmentDate(requestBodyObjectArrayList.get(i).getNextInstallmentDate());
            investmentCartDetailsResponse.setNoOfMonth(requestBodyObjectArrayList.get(i).getNoOfMonth());
            investmentCartDetailsResponse.setNomineeIsMinor1(requestBodyObjectArrayList.get(i).getNomineeIsMinor1());
            investmentCartDetailsResponse.setNomineeIsMinor2(requestBodyObjectArrayList.get(i).getNomineeIsMinor2());
            investmentCartDetailsResponse.setNomineeIsMinor3(requestBodyObjectArrayList.get(i).getNomineeIsMinor3());
            if (requestBodyObjectArrayList.get(i).getNomineeShare1() == null) {
                investmentCartDetailsResponse.setNomineeShare1("0");

            } else {
                investmentCartDetailsResponse.setNomineeShare1(requestBodyObjectArrayList.get(i).getNomineeShare1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeShare2() == null) {
                investmentCartDetailsResponse.setNomineeShare2("0");

            } else {
                investmentCartDetailsResponse.setNomineeShare2(requestBodyObjectArrayList.get(i).getNomineeShare2());

            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare3() == null) {
                investmentCartDetailsResponse.setNomineeShare3("0");

            } else {
                investmentCartDetailsResponse.setNomineeShare3(requestBodyObjectArrayList.get(i).getNomineeShare3());
            }
            investmentCartDetailsResponse.setPeriod(requestBodyObjectArrayList.get(i).getPeriod());
            investmentCartDetailsResponse.setPurchaseAllowed(requestBodyObjectArrayList.get(i).getPurchaseAllowed());
            investmentCartDetailsResponse.setRecommendationStatus(requestBodyObjectArrayList.get(i).getRecommendationStatus());
            investmentCartDetailsResponse.setRedeemAllowed(requestBodyObjectArrayList.get(i).getRedeemAllowed());
            investmentCartDetailsResponse.setSIPAllowed(requestBodyObjectArrayList.get(i).getSIPAllowed());
            investmentCartDetailsResponse.setSIPStartDate(requestBodyObjectArrayList.get(i).getSIPStartDates());

            investmentCartDetailsResponse.setSettlementBankCode(requestBodyObjectArrayList.get(i).getSettlementBankCode());
            investmentCartDetailsResponse.setSipFrequency(requestBodyObjectArrayList.get(i).getSipFrequency());
            investmentCartDetailsResponse.setSwitchOutAllowed(requestBodyObjectArrayList.get(i).getSwitchOutAllowed());
            investmentCartDetailsResponse.setTargetFundCode(requestBodyObjectArrayList.get(i).getTargetFundCode());
            //investmentCartDetailsResponse.setTnCUrl(requestBodyObjectArrayList.get(i).getTnCUrl());
            investmentCartDetailsResponse.setTransactionType("PUR");
            investmentCartDetailsResponse.setValueResearchRating(requestBodyObjectArrayList.get(i).getValueResearchRating());


            if (requestBodyObjectArrayList.get(i).getDateOfBirth1() == null) {
                investmentCartDetailsResponse.setDateOfBirth1("");

            } else {
                investmentCartDetailsResponse.setDateOfBirth1(requestBodyObjectArrayList.get(i).getDateOfBirth1());

            }

            if (requestBodyObjectArrayList.get(i).getDateOfBirth2() == null) {
                investmentCartDetailsResponse.setDateOfBirth2("");

            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth2());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth3() == null) {
                investmentCartDetailsResponse.setDateOfBirth3("");

            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth3());
            }
            investmentCartDetailsResponse.setAmountOrUnit("Amount");
            investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());
            investmentCartDetailsResponse.setTranType("B");
            investmentCartDetailsResponse.setPaymentMode("7");
            investmentCartDetailsResponse.setPlatform_id("0");
            investmentCartDetailsResponse.setUMRN("");
            investmentCartDetailsResponse.setOrderChannelID("20");
            investmentCartDetailsResponse.setClientUCC(authenticateResponse.getClientUCC());
            investmentCartDetailsResponse.setChannel("BOBMPIN");
            investmentCartDetailsResponse.setMpinMode("OTPDisabled");
            investmentCartDetailsResponse.setRegistrationRefId("");
            investmentCartDetailsResponse.setPaymentType("Online");
            investmentCartDetailsResponse.setUTR("");
            investmentCartDetailsResponse.setReturnPaymentFlag("Y");
            investmentCartDetailsResponse.setClientCallbackUrl("https://barodawealth.com/MoneywarePortal/Portal/UI/NSELogin");
            investmentCartDetailsResponse.setDebitBankCode(bankCode);
            investmentCartDetailsResponse.setDebitBankSource(bankSource);
            investmentCartDetailsResponse.setFrequency("Daily");

//            if (requestBodyObjectArrayList.get(i).getFrequency() == null) {
//                investmentCartDetailsResponse.setFrequency("Daily");
//
//            } else {
//                investmentCartDetailsResponse.setFrequency(requestBodyObjectArrayList.get(i).getFrequency());
//
//            }

            // static
            investmentCartDetailsResponse.setClientCode(authenticateResponse.getUserCode());
            investmentCartDetailsResponse.setMpinEncValue("hMWQtHeORTUXdby5oecKfA==");
            investmentCartDetailsResponse.setAuthToken("");
            investmentCartDetailsResponse.setSessionID("");
            investmentCartDetailsResponse.setOTPPassword("");
            investmentCartDetailsResponse.setClientIp("");
            investmentCartDetailsResponse.setSwitchDividendOption("G");
            investmentCartDetailsResponse.setClientMobileNo(ContactNo);
            investmentCartDetailsResponse.setIsNRI("false");
            investmentCartDetailsResponse.setStatus("false");
            investmentCartDetailsResponse.setDeviationMessage("");
            investmentCartDetailsResponse.setReason("");
            investmentCartDetailsResponse.setMinSWPUnit("0.0");
            investmentCartDetailsResponse.setMinSipNoOfInst("");
            investmentCartDetailsResponse.setSIPAggrAmt("0.0");
            investmentCartDetailsResponse.setSTPAllowed("");
            investmentCartDetailsResponse.setSWPAllowed("");
            investmentCartDetailsResponse.setAllorPartial("2");
            investmentCartDetailsResponse.setAccountNumber(accountNumber);
            investmentCartDetailsResponse.setSchemeOfferLink(requestBodyObjectArrayList.get(i).getSchemeOfferLink());
            investmentCartDetailsResponse.setTransactionAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());

            //  String type = requestBodyObjectArrayList.get(i).getAmountOrUnit();

            investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setTranUnit("0");

//            if (type.equalsIgnoreCase("U")) {
//                // investmentCartDetailsResponse.setTranUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
//                investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
//                investmentCartDetailsResponse.setTranUnit("0");
//            }
//
//            if (type.equalsIgnoreCase("A")) {
//                investmentCartDetailsResponse.setTranUnit("0");
//                investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
//
//            }

            investmentCartDetailsResponse.setAccountNumber(accountNumber);

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship1() == null) {
                investmentCartDetailsResponse.setNomineeRelationship1("");

            } else {
                investmentCartDetailsResponse.setNomineeRelationship1(requestBodyObjectArrayList.get(i).getNomineeRelationship1());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship2() == null) {
                investmentCartDetailsResponse.setNomineeRelationship2("");

            } else {
                investmentCartDetailsResponse.setNomineeRelationship2(requestBodyObjectArrayList.get(i).getNomineeRelationship2());

            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship3() == null) {
                investmentCartDetailsResponse.setNomineeRelationship3("");

            } else {
                investmentCartDetailsResponse.setNomineeRelationship3(requestBodyObjectArrayList.get(i).getNomineeRelationship3());

            }

            investmentCartDetailsResponse.setNomineeAddress1("");
            investmentCartDetailsResponse.setNomineeAddress2("");
            investmentCartDetailsResponse.setNomineeAddress3("");

            investmentCartDetailsResponse.setGuardianName1("");
            investmentCartDetailsResponse.setGuardianName2("");
            investmentCartDetailsResponse.setGuardianName3("");


            investmentCartDetailsResponse.setGuardianRelationship1("");
            investmentCartDetailsResponse.setGuardianRelationship2("");
            investmentCartDetailsResponse.setGuardianRelationship3("");

            investmentCartDetailsResponse.setGuardianPan1("");
            investmentCartDetailsResponse.setGuardianPan2("");
            investmentCartDetailsResponse.setGuardianPan3("");

            investmentCartDetailsResponse.setGuardianAddress1("");
            investmentCartDetailsResponse.setGuardianAddress2("");
            investmentCartDetailsResponse.setGuardianAddress3("");
            investmentCartDetailsResponse.setStartDay("");
            investmentCartDetailsResponse.setStartDate(cureentDate);
            investmentCartDetailsResponse.setSIPStartDates(cureentDate);


            investmentCartDetailsResponse.setParentChannelID("");

            //   investmentCartDetailsResponse.setAllorPartial(requestBodyObjectArrayList.get(i).getAllorPartial());
            investmentCartDetailsResponse.setAllorPartial("Partial");
            //investmentCartDetailsResponse.setSIPStartDates(requestBodyObjectArrayList.get(i).getSIPStartDates());


            // investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());

            finalRequestBodyObjectArrayList.add(investmentCartDetailsResponse);

        }

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        BuyGlobalRequestObjectArray globalRequestObject = new BuyGlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(finalRequestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addPlaceOrder(globalRequestObject).enqueue(new Callback<ArrayList<BuyConfirmResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<BuyConfirmResponse>> call, Response<ArrayList<BuyConfirmResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    buyConfirmResponseArrayList = response.body();

                    Bundle args = new Bundle();

                    args.putSerializable("schemeResponseArrayList", requestBodyObjectArrayList);
                    args.putSerializable("buyConfirmResponseArrayList", buyConfirmResponseArrayList);
                    args.putString("clientName", clientName);
                    args.putString("status", status);

                    Fragment fragment = new ConfirmOrderStatusActivity();

                      fragment.setArguments(args);

                    getDetail(fragment);

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuyConfirmResponse>> call, Throwable t) {

                util.showProgressDialog(context, false);


                System.out.println("error mshg" + t.getLocalizedMessage());


                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // confirm
    private void addSipInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CONFIRM_BUY);

        ArrayList<InvestmentCartDetailsResponse> finalRequestBodyObjectArrayList = new ArrayList<InvestmentCartDetailsResponse>();

        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
            InvestmentCartDetailsResponse investmentCartDetailsResponse = new InvestmentCartDetailsResponse();


            investmentCartDetailsResponse.setClientCode(authenticateResponse.getUserCode());
            investmentCartDetailsResponse.setParentChannelID("WMSPortal");
            investmentCartDetailsResponse.setICDID(requestBodyObjectArrayList.get(i).getICDID());
            investmentCartDetailsResponse.setLatestNAV(requestBodyObjectArrayList.get(i).getLatestNAV());
            investmentCartDetailsResponse.setFundCode(requestBodyObjectArrayList.get(i).getFundCode());
            investmentCartDetailsResponse.setFundName(requestBodyObjectArrayList.get(i).getFundName());
            investmentCartDetailsResponse.setValueResearchRating(requestBodyObjectArrayList.get(i).getValueResearchRating());

            investmentCartDetailsResponse.setFolio(requestBodyObjectArrayList.get(i).getFolioNo());
            //   investmentCartDetailsResponse.setFolioNo(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setCostofInvestment(requestBodyObjectArrayList.get(i).getCostofInvestment());
            investmentCartDetailsResponse.setCurrentUnits(requestBodyObjectArrayList.get(i).getCurrentUnits());
            investmentCartDetailsResponse.setCurrentFundValue(requestBodyObjectArrayList.get(i).getCurrentFundValue());
            investmentCartDetailsResponse.setAllorPartial("Partial");
            investmentCartDetailsResponse.setTxnAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            //da
            investmentCartDetailsResponse.setTransactionAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setTranUnit("0");
            investmentCartDetailsResponse.setTranType("B");
            investmentCartDetailsResponse.setTransactionType("SIP");
            investmentCartDetailsResponse.setAssetClassCode(requestBodyObjectArrayList.get(i).getAssetClassCode());
            investmentCartDetailsResponse.setL4ClientCode(requestBodyObjectArrayList.get(i).getL4ClientCode());
            investmentCartDetailsResponse.setDebitBankCode(bankCode);
            investmentCartDetailsResponse.setDebitBankSource(bankSource);
            investmentCartDetailsResponse.setIsDividend(requestBodyObjectArrayList.get(i).getIsDividend());
            investmentCartDetailsResponse.setDividendOption(requestBodyObjectArrayList.get(i).getDividendOption());
            investmentCartDetailsResponse.setSwitchDividendOption("G");
            investmentCartDetailsResponse.setSettlementBankCode(requestBodyObjectArrayList.get(i).getSettlementBankCode());
            investmentCartDetailsResponse.setPaymentMode("7");
            investmentCartDetailsResponse.setTargetFundCode(requestBodyObjectArrayList.get(i).getTargetFundCode());

            //investmentCartDetailsResponse.setStartDate(requestBodyObjectArrayList.get(i).getSIPStartDate());

            String date = requestBodyObjectArrayList.get(i).getSIPStartDate();

            String year = "";
            String month = "";
            String day = "";
            if(date!=null) {
                for (int j = 0; j < 4; j++) {
                    if (date.charAt(j) != '/') {
                        year = year + date.charAt(j);
                    } else {
                        break;
                    }
                }

                for (int k = 4; k < 6; k++) {
                    if (date.charAt(k) != '/') {
                        month = month + date.charAt(k);
                    } else {
                        break;
                    }
                }

                for (int l = 6; l < 8; l++) {
                    if (date.charAt(l) != '/') {
                        day = day + date.charAt(l);
                    } else {
                        break;
                    }
                }

                System.out.println("year" + year);
                System.out.println("month" + month);
                System.out.println("day" + date);


                String finalDate = year + "-" + month + "-" + day;
                investmentCartDetailsResponse.setStartDate(finalDate);
                investmentCartDetailsResponse.setSIPStartDates(finalDate);
            }



            // investmentCartDetailsResponse.setSIPStartDates(requestBodyObjectArrayList.get(i).getSIPStartDate());


            investmentCartDetailsResponse.setSIPStartDate(requestBodyObjectArrayList.get(i).getSIPStartDates());

            investmentCartDetailsResponse.setStartDay(day);

            investmentCartDetailsResponse.setNoOfMonth(requestBodyObjectArrayList.get(i).getNoOfMonth());
            investmentCartDetailsResponse.setIsApplyNominee(requestBodyObjectArrayList.get(i).getIsApplyNominee());

            if (requestBodyObjectArrayList.get(i).getDateOfBirth1() == null) {
                investmentCartDetailsResponse.setDateOfBirth1("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth1(requestBodyObjectArrayList.get(i).getDateOfBirth1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship1() == null) {
                investmentCartDetailsResponse.setNomineeRelationship1("");

            } else {
                investmentCartDetailsResponse.setNomineeRelationship1(requestBodyObjectArrayList.get(i).getNomineeRelationship1());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeShare1() == null) {
                investmentCartDetailsResponse.setNomineeShare1("0");

            } else {
                investmentCartDetailsResponse.setNomineeShare1(requestBodyObjectArrayList.get(i).getNomineeShare1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeShare2() == null) {
                investmentCartDetailsResponse.setNomineeShare2("0");

            } else {
                investmentCartDetailsResponse.setNomineeShare2(requestBodyObjectArrayList.get(i).getNomineeShare2());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress1() == null) {
                investmentCartDetailsResponse.setNomineeAddress1("");

            } else {
                investmentCartDetailsResponse.setNomineeAddress1(requestBodyObjectArrayList.get(i).getNomineeAddress1());
            }

            if (requestBodyObjectArrayList.get(i).getGuardianName1() == null) {
                investmentCartDetailsResponse.setGuardianName1("");

            } else {
                investmentCartDetailsResponse.setGuardianName1(requestBodyObjectArrayList.get(i).getGuardianName1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeName1() == null) {
                investmentCartDetailsResponse.setNomineeName1("");

            } else {
                investmentCartDetailsResponse.setNomineeName1(requestBodyObjectArrayList.get(i).getNomineeName1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName2() == null) {
                investmentCartDetailsResponse.setNomineeName2("");

            } else {
                investmentCartDetailsResponse.setNomineeName2(requestBodyObjectArrayList.get(i).getNomineeName2());
            }

            investmentCartDetailsResponse.setNomineeIsMinor1(requestBodyObjectArrayList.get(i).getNomineeIsMinor1());

            investmentCartDetailsResponse.setGuardianRelationship1("");
            investmentCartDetailsResponse.setGuardianRelationship2("");
            investmentCartDetailsResponse.setGuardianRelationship3("");


            investmentCartDetailsResponse.setGuardianPan1("");
            investmentCartDetailsResponse.setGuardianPan2("");
            investmentCartDetailsResponse.setGuardianPan3("");

            investmentCartDetailsResponse.setGuardianAddress1("");
            investmentCartDetailsResponse.setGuardianAddress2("");
            investmentCartDetailsResponse.setGuardianAddress3("");


            if (requestBodyObjectArrayList.get(i).getDateOfBirth2() == null) {
                investmentCartDetailsResponse.setDateOfBirth2("");

            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth2());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship2() == null) {
                investmentCartDetailsResponse.setNomineeRelationship2("");

            } else {
                investmentCartDetailsResponse.setNomineeRelationship2(requestBodyObjectArrayList.get(i).getNomineeRelationship2());

            }


            if (requestBodyObjectArrayList.get(i).getNomineeShare3() == null) {
                investmentCartDetailsResponse.setNomineeShare3("0");

            } else {
                investmentCartDetailsResponse.setNomineeShare3(requestBodyObjectArrayList.get(i).getNomineeShare3());
            }

            investmentCartDetailsResponse.setNomineeAddress2("");
            investmentCartDetailsResponse.setNomineeAddress3("");
            investmentCartDetailsResponse.setNomineeIsMinor2(requestBodyObjectArrayList.get(i).getNomineeIsMinor2());
            investmentCartDetailsResponse.setGuardianName2("");
            investmentCartDetailsResponse.setGuardianName3("");

            investmentCartDetailsResponse.setPlatform_id("0");
            investmentCartDetailsResponse.setFrequency(requestBodyObjectArrayList.get(i).getFrequency());
            investmentCartDetailsResponse.setUMRN("");
            investmentCartDetailsResponse.setOrderChannelID("20");
            investmentCartDetailsResponse.setClientIp("");
            investmentCartDetailsResponse.setClientMobileNo(ContactNo);
            investmentCartDetailsResponse.setOTPPassword("");
            investmentCartDetailsResponse.setChannel("BOBMPIN");
            investmentCartDetailsResponse.setClientUCC(authenticateResponse.getClientUCC());
            investmentCartDetailsResponse.setSessionID("");
            investmentCartDetailsResponse.setAuthToken("");
            investmentCartDetailsResponse.setMpinMode("OTPDisabled");
            investmentCartDetailsResponse.setRegistrationRefId("");
            investmentCartDetailsResponse.setMpinEncValue("hMWQtHeORTUXdby5oecKfA==");
            investmentCartDetailsResponse.setPaymentType("Online");
            investmentCartDetailsResponse.setUTR("");
            investmentCartDetailsResponse.setReturnPaymentFlag("Y");
            investmentCartDetailsResponse.setClientCallbackUrl("https://barodawealth.com/MoneywarePortal/Portal/UI/NSELogin");


            // not required
            investmentCartDetailsResponse.setAMCName(requestBodyObjectArrayList.get(i).getAMCName());
            investmentCartDetailsResponse.setAddPurchaseMinAmt(requestBodyObjectArrayList.get(i).getAddPurchaseMinAmt());
            investmentCartDetailsResponse.setAwaitingHoldingFundValue(requestBodyObjectArrayList.get(i).getAwaitingHoldingFundValue());
            investmentCartDetailsResponse.setAwaitingHoldingUnit(requestBodyObjectArrayList.get(i).getAwaitingHoldingUnit());
            investmentCartDetailsResponse.setDebitDate(requestBodyObjectArrayList.get(i).getDebitDate());
            investmentCartDetailsResponse.setExistingAmount(requestBodyObjectArrayList.get(i).getExistingAmount());
            investmentCartDetailsResponse.setExistingUnits(requestBodyObjectArrayList.get(i).getExistingUnits());
            investmentCartDetailsResponse.setFC1CountryOfBirth(requestBodyObjectArrayList.get(i).getFC1CountryOfBirth());
            investmentCartDetailsResponse.setFC1NetWorth(requestBodyObjectArrayList.get(i).getFC1NetWorth());
            investmentCartDetailsResponse.setFC2CountryOfBirth(requestBodyObjectArrayList.get(i).getFC2CountryOfBirth());
            investmentCartDetailsResponse.setFC2NetWorth(requestBodyObjectArrayList.get(i).getFC2NetWorth());
            investmentCartDetailsResponse.setFC3CountryOfBirth(requestBodyObjectArrayList.get(i).getFC3CountryOfBirth());
            investmentCartDetailsResponse.setFC3NetWorth(requestBodyObjectArrayList.get(i).getFC3NetWorth());

            if (requestBodyObjectArrayList.get(i).getFolioNo().equalsIgnoreCase("new folio")) {
                investmentCartDetailsResponse.setFolioNo("");

            } else {
                investmentCartDetailsResponse.setFolioNo(requestBodyObjectArrayList.get(i).getFolioNo());

            }

            investmentCartDetailsResponse.setICID(requestBodyObjectArrayList.get(i).getICID());
            investmentCartDetailsResponse.setLotSize(requestBodyObjectArrayList.get(i).getLotSize());
            investmentCartDetailsResponse.setMinInvAmount(requestBodyObjectArrayList.get(i).getMinInvAmount());
            investmentCartDetailsResponse.setMinRedeemUnit(requestBodyObjectArrayList.get(i).getMinRedeemUnit());
            //    investmentCartDetailsResponse.setNextInstallmentDate(requestBodyObjectArrayList.get(i).getNextInstallmentDate());

            investmentCartDetailsResponse.setNextInstallmentDate("01/01/1900");

            investmentCartDetailsResponse.setNomineeIsMinor3(requestBodyObjectArrayList.get(i).getNomineeIsMinor3());
            investmentCartDetailsResponse.setPeriod(requestBodyObjectArrayList.get(i).getPeriod());
            investmentCartDetailsResponse.setPurchaseAllowed(requestBodyObjectArrayList.get(i).getPurchaseAllowed());
            investmentCartDetailsResponse.setRecommendationStatus(requestBodyObjectArrayList.get(i).getRecommendationStatus());
            investmentCartDetailsResponse.setRedeemAllowed(requestBodyObjectArrayList.get(i).getRedeemAllowed());
            investmentCartDetailsResponse.setSIPAllowed(requestBodyObjectArrayList.get(i).getSIPAllowed());

            investmentCartDetailsResponse.setSipFrequency(requestBodyObjectArrayList.get(i).getSipFrequency());
            investmentCartDetailsResponse.setSwitchOutAllowed(requestBodyObjectArrayList.get(i).getSwitchOutAllowed());
            //investmentCartDetailsResponse.setTnCUrl(requestBodyObjectArrayList.get(i).getTnCUrl());


            if (requestBodyObjectArrayList.get(i).getDateOfBirth3() == null) {
                investmentCartDetailsResponse.setDateOfBirth3("");

            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth3());
            }

            investmentCartDetailsResponse.setAmountOrUnit("Amount");
            // investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());
            investmentCartDetailsResponse.setAmtOrUnit("U");

            investmentCartDetailsResponse.setClientCode(authenticateResponse.getUserCode());

            investmentCartDetailsResponse.setIsNRI("false");
            investmentCartDetailsResponse.setStatus("");
            investmentCartDetailsResponse.setDeviationMessage("");
            investmentCartDetailsResponse.setReason("");
            investmentCartDetailsResponse.setMinSWPUnit("0.0");
            investmentCartDetailsResponse.setMinSipNoOfInst("");
            investmentCartDetailsResponse.setSIPAggrAmt("0.0");
            investmentCartDetailsResponse.setSTPAllowed("");
            investmentCartDetailsResponse.setSWPAllowed("");

            investmentCartDetailsResponse.setAccountNumber(accountNumber);

            investmentCartDetailsResponse.setSchemeOfferLink(requestBodyObjectArrayList.get(i).getSchemeOfferLink());
            investmentCartDetailsResponse.setAccountNumber(accountNumber);


            if (requestBodyObjectArrayList.get(i).getNomineeRelationship3() == null) {
                investmentCartDetailsResponse.setNomineeRelationship3("");

            } else {
                investmentCartDetailsResponse.setNomineeRelationship3(requestBodyObjectArrayList.get(i).getNomineeRelationship3());

            }


            //   investmentCartDetailsResponse.setAllorPartial(requestBodyObjectArrayList.get(i).getAllorPartial());


            // investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());

            finalRequestBodyObjectArrayList.add(investmentCartDetailsResponse);

        }

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        BuyGlobalRequestObjectArray globalRequestObject = new BuyGlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(finalRequestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addPlaceOrder(globalRequestObject).enqueue(new Callback<ArrayList<BuyConfirmResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<BuyConfirmResponse>> call, Response<ArrayList<BuyConfirmResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    buyConfirmResponseArrayList = response.body();
                    Bundle args = new Bundle();

                    args.putSerializable("schemeResponseArrayList", requestBodyObjectArrayList);
                    args.putSerializable("buyConfirmResponseArrayList", buyConfirmResponseArrayList);
                    args.putString("clientName", clientName);
                    args.putString("status", status);

                    Fragment fragment = new ConfirmOrderStatusActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);

                //    Toast.makeText(getContext(), "Order Placed Successfull with Order No:-" + buyConfirmResponseArrayList.get(0).getRequestID(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuyConfirmResponse>> call, Throwable t) {

                util.showProgressDialog(context, false);


                System.out.println("error mshg" + t.getLocalizedMessage());


                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // place order for redeem
    private void addRedeemInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CONFIRM_BUY);

        ArrayList<InvestmentCartDetailsResponse> finalRequestBodyObjectArrayList = new ArrayList<InvestmentCartDetailsResponse>();

        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
            InvestmentCartDetailsResponse investmentCartDetailsResponse = new InvestmentCartDetailsResponse();
            investmentCartDetailsResponse.setClientCode(authenticateResponse.getUserCode());
            investmentCartDetailsResponse.setParentChannelID("WMSPortal");
            investmentCartDetailsResponse.setICDID(requestBodyObjectArrayList.get(i).getICDID());
            investmentCartDetailsResponse.setLatestNAV(requestBodyObjectArrayList.get(i).getLatestNAV());
            investmentCartDetailsResponse.setFundCode(requestBodyObjectArrayList.get(i).getFundCode());
            investmentCartDetailsResponse.setFundName(requestBodyObjectArrayList.get(i).getFundName());
            investmentCartDetailsResponse.setValueResearchRating(requestBodyObjectArrayList.get(i).getValueResearchRating());
            investmentCartDetailsResponse.setFolio(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolioNo(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolioNumber(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setCostofInvestment(requestBodyObjectArrayList.get(i).getCostofInvestment());
            investmentCartDetailsResponse.setCurrentUnits(requestBodyObjectArrayList.get(i).getCurrentUnits());
            investmentCartDetailsResponse.setCurrentFundValue(requestBodyObjectArrayList.get(i).getCurrentFundValue());

            //   investmentCartDetailsResponse.setAllorPartial(requestBodyObjectArrayList.get(i).getAllorPartial());

            if (requestBodyObjectArrayList.get(i).getAllorPartial().equalsIgnoreCase("P")) {
                investmentCartDetailsResponse.setAllorPartial("Partial");
            } else {
                investmentCartDetailsResponse.setAllorPartial("All");
            }

            investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());

            //  investmentCartDetailsResponse.setAmtOrUnit("A");


            investmentCartDetailsResponse.setTxnAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setTransactionAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());

            if (requestBodyObjectArrayList.get(i).getAmountOrUnit() != null) {
                if (requestBodyObjectArrayList.get(i).getAmountOrUnit().equalsIgnoreCase("A")) {
                    investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
                    investmentCartDetailsResponse.setTranUnit("0");
                }
                if (requestBodyObjectArrayList.get(i).getAmountOrUnit().equalsIgnoreCase("U")) {
                    investmentCartDetailsResponse.setTranAmt("0");
                    investmentCartDetailsResponse.setTranUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
                }
            }

            investmentCartDetailsResponse.setTranType("S");
            investmentCartDetailsResponse.setTransactionType("RED");
            investmentCartDetailsResponse.setAssetClassCode(requestBodyObjectArrayList.get(i).getAssetClassCode());
            investmentCartDetailsResponse.setAssetClassName(requestBodyObjectArrayList.get(i).getAssetClassName());
            investmentCartDetailsResponse.setL4ClientCode(requestBodyObjectArrayList.get(i).getL4ClientCode());
            investmentCartDetailsResponse.setDebitBankCode("0");
            investmentCartDetailsResponse.setDebitBankSource("0");
            investmentCartDetailsResponse.setIsDividend(requestBodyObjectArrayList.get(i).getIsDividend());
            investmentCartDetailsResponse.setDividendOption(requestBodyObjectArrayList.get(i).getDividendOption());
            // investmentCartDetailsResponse.setSwitchDividendOption(requestBodyObjectArrayList.get(i).getSwitchDividendOption());
            investmentCartDetailsResponse.setSwitchDividendOption("G");
            investmentCartDetailsResponse.setSettlementBankCode(requestBodyObjectArrayList.get(i).getSettlementBankCode());
            investmentCartDetailsResponse.setPaymentMode("7");
            investmentCartDetailsResponse.setTargetFundCode(requestBodyObjectArrayList.get(i).getTargetFundCode());
            investmentCartDetailsResponse.setStartDate(cureentDate);
            investmentCartDetailsResponse.setSIPStartDates(cureentDate);
            investmentCartDetailsResponse.setSIPStartDate(requestBodyObjectArrayList.get(i).getSIPStartDates());
            investmentCartDetailsResponse.setStartDay("01");
            investmentCartDetailsResponse.setNoOfMonth(requestBodyObjectArrayList.get(i).getNoOfMonth());
            investmentCartDetailsResponse.setIsApplyNominee(requestBodyObjectArrayList.get(i).getIsApplyNominee());

            if (requestBodyObjectArrayList.get(i).getNomineeName1() != null) {
                investmentCartDetailsResponse.setNomineeName1(requestBodyObjectArrayList.get(i).getNomineeName1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName2() != null) {
                investmentCartDetailsResponse.setNomineeName2(requestBodyObjectArrayList.get(i).getNomineeName2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName3() != null) {
                investmentCartDetailsResponse.setNomineeName3(requestBodyObjectArrayList.get(i).getNomineeName3());
            }

            if (requestBodyObjectArrayList.get(i).getDateOfBirth1() == null) {
                investmentCartDetailsResponse.setDateOfBirth1("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth1(requestBodyObjectArrayList.get(i).getDateOfBirth1());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth2() == null) {
                investmentCartDetailsResponse.setDateOfBirth2("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth2());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth3() == null) {
                investmentCartDetailsResponse.setDateOfBirth3("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth3(requestBodyObjectArrayList.get(i).getDateOfBirth3());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship1() != null) {
                investmentCartDetailsResponse.setNomineeRelationship1(requestBodyObjectArrayList.get(i).getNomineeRelationship1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship2() != null) {
                investmentCartDetailsResponse.setNomineeRelationship2(requestBodyObjectArrayList.get(i).getNomineeRelationship2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship3() != null) {
                investmentCartDetailsResponse.setNomineeRelationship3(requestBodyObjectArrayList.get(i).getNomineeRelationship3());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeShare1() == null) {
                investmentCartDetailsResponse.setNomineeShare1("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare1(requestBodyObjectArrayList.get(i).getNomineeShare1());

            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare2() == null) {
                investmentCartDetailsResponse.setNomineeShare2("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare2(requestBodyObjectArrayList.get(i).getNomineeShare2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare3() == null) {
                investmentCartDetailsResponse.setNomineeShare3("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare3(requestBodyObjectArrayList.get(i).getNomineeShare3());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress1() != null) {
                investmentCartDetailsResponse.setNomineeAddress1(requestBodyObjectArrayList.get(i).getNomineeAddress1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress2() != null) {
                investmentCartDetailsResponse.setNomineeAddress2(requestBodyObjectArrayList.get(i).getNomineeAddress2());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress3() != null) {
                investmentCartDetailsResponse.setNomineeAddress3(requestBodyObjectArrayList.get(i).getNomineeAddress3());
            }

            investmentCartDetailsResponse.setNomineeIsMinor1("false");
            investmentCartDetailsResponse.setNomineeIsMinor2("false");
            investmentCartDetailsResponse.setNomineeIsMinor3("false");

            if (requestBodyObjectArrayList.get(i).getGuardianName1() != null) {
                investmentCartDetailsResponse.setGuardianName1(requestBodyObjectArrayList.get(i).getGuardianName1());
            }
            if (requestBodyObjectArrayList.get(i).getGuardianName2() != null) {
                investmentCartDetailsResponse.setGuardianName2(requestBodyObjectArrayList.get(i).getGuardianName2());
            }
            if (requestBodyObjectArrayList.get(i).getGuardianName3() != null) {
                investmentCartDetailsResponse.setGuardianName3(requestBodyObjectArrayList.get(i).getGuardianName3());
            }

            investmentCartDetailsResponse.setPlatform_id("0");
            investmentCartDetailsResponse.setFrequency("Daily");

            investmentCartDetailsResponse.setUMRN("");
            investmentCartDetailsResponse.setOrderChannelID("20");

            investmentCartDetailsResponse.setClientIP("172.17.125.94");
            investmentCartDetailsResponse.setClientMobileNo(ContactNo);

            investmentCartDetailsResponse.setOTPPassword("");
            investmentCartDetailsResponse.setChannel("BOBMPIN");

            investmentCartDetailsResponse.setClientUCC(authenticateResponse.getClientUCC());
            investmentCartDetailsResponse.setSessionID("");

            investmentCartDetailsResponse.setAuthToken("");
            investmentCartDetailsResponse.setMpinMode("OTPDisabled");

            investmentCartDetailsResponse.setRegistrationRefId("");
            investmentCartDetailsResponse.setMpinEncValue("z8g2axnP8+aVb8MJqX7CafLLQLsNyQ0jtXJpMk1AuZw=");
            investmentCartDetailsResponse.setPaymentType("");
            investmentCartDetailsResponse.setUTR("");
            investmentCartDetailsResponse.setReturnPaymentFlag("N");
            investmentCartDetailsResponse.setClientCallbackUrl("");
            finalRequestBodyObjectArrayList.add(investmentCartDetailsResponse);

        }

        UUID uuid = UUID.randomUUID();


        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        BuyGlobalRequestObjectArray globalRequestObject = new BuyGlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(finalRequestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addPlaceOrder(globalRequestObject).enqueue(new Callback<ArrayList<BuyConfirmResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<BuyConfirmResponse>> call, Response<ArrayList<BuyConfirmResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    buyConfirmResponseArrayList = response.body();

                    Bundle args = new Bundle();

                    args.putSerializable("schemeResponseArrayList", requestBodyObjectArrayList);
                    args.putSerializable("buyConfirmResponseArrayList", buyConfirmResponseArrayList);
                    args.putString("clientName", clientNames);
                    args.putString("status", status);

                    Fragment fragment = new ConfirmOrderStatusActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);


//                    if (buyConfirmResponseArrayList.get(0).getSuccessFlag().equalsIgnoreCase("false")) {
//                        Toast.makeText(getContext(), buyConfirmResponseArrayList.get(0).getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(), "Order Placed Successfull with Request Id:-" + buyConfirmResponseArrayList.get(0).getOrderNo(), Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuyConfirmResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);

                System.out.println("error mshg" + t.getLocalizedMessage());

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //switch
    private void addSwitchInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CONFIRM_BUY);

        ArrayList<InvestmentCartDetailsResponse> finalRequestBodyObjectArrayList = new ArrayList<InvestmentCartDetailsResponse>();

        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
            InvestmentCartDetailsResponse investmentCartDetailsResponse = new InvestmentCartDetailsResponse();
            investmentCartDetailsResponse.setClientCode(authenticateResponse.getUserCode());
            investmentCartDetailsResponse.setParentChannelID("WMSPortal");
            investmentCartDetailsResponse.setICDID(requestBodyObjectArrayList.get(i).getICDID());
            investmentCartDetailsResponse.setLatestNAV(requestBodyObjectArrayList.get(i).getLatestNAV());
            investmentCartDetailsResponse.setFundCode(requestBodyObjectArrayList.get(i).getFundCode());
            investmentCartDetailsResponse.setFundName(requestBodyObjectArrayList.get(i).getFundName());
            investmentCartDetailsResponse.setValueResearchRating(requestBodyObjectArrayList.get(i).getValueResearchRating());
            investmentCartDetailsResponse.setFolio(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolioNo(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolioNumber(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setCostofInvestment(requestBodyObjectArrayList.get(i).getCostofInvestment());
            investmentCartDetailsResponse.setCurrentUnits(requestBodyObjectArrayList.get(i).getCurrentUnits());
            investmentCartDetailsResponse.setCurrentFundValue(requestBodyObjectArrayList.get(i).getCurrentFundValue());

            //   investmentCartDetailsResponse.setAllorPartial(requestBodyObjectArrayList.get(i).getAllorPartial());

            if (requestBodyObjectArrayList.get(i).getAllorPartial().equalsIgnoreCase("P")) {
                investmentCartDetailsResponse.setAllorPartial("Partial");
            } else {
                investmentCartDetailsResponse.setAllorPartial("All");
            }

            investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());

            //  investmentCartDetailsResponse.setAmtOrUnit("A");


            investmentCartDetailsResponse.setTxnAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setTransactionAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());

            if (requestBodyObjectArrayList.get(i).getAmountOrUnit() != null) {
                if (requestBodyObjectArrayList.get(i).getAmountOrUnit().equalsIgnoreCase("A")) {
                    investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
                    investmentCartDetailsResponse.setTranUnit("0");
                }
                if (requestBodyObjectArrayList.get(i).getAmountOrUnit().equalsIgnoreCase("U")) {
                    investmentCartDetailsResponse.setTranAmt("0");
                    investmentCartDetailsResponse.setTranUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
                }
            }
            investmentCartDetailsResponse.setTranType("B");
            investmentCartDetailsResponse.setTransactionType("SWI");
            investmentCartDetailsResponse.setAssetClassCode(requestBodyObjectArrayList.get(i).getAssetClassCode());
            investmentCartDetailsResponse.setAssetClassName(requestBodyObjectArrayList.get(i).getAssetClassName());
            investmentCartDetailsResponse.setL4ClientCode(requestBodyObjectArrayList.get(i).getL4ClientCode());
            investmentCartDetailsResponse.setDebitBankCode("0");
            investmentCartDetailsResponse.setDebitBankSource("0");
            investmentCartDetailsResponse.setIsDividend(requestBodyObjectArrayList.get(i).getIsDividend());
            investmentCartDetailsResponse.setDividendOption(requestBodyObjectArrayList.get(i).getDividendOption());
            // investmentCartDetailsResponse.setSwitchDividendOption(requestBodyObjectArrayList.get(i).getSwitchDividendOption());
            investmentCartDetailsResponse.setSwitchDividendOption("G");
            investmentCartDetailsResponse.setSettlementBankCode(requestBodyObjectArrayList.get(i).getSettlementBankCode());
            investmentCartDetailsResponse.setPaymentMode("7");

            investmentCartDetailsResponse.setTargetFundCode(requestBodyObjectArrayList.get(i).getTargetFundCode());
            investmentCartDetailsResponse.setTargetFundName(requestBodyObjectArrayList.get(i).getTargetFundName());

            investmentCartDetailsResponse.setStartDate(cureentDate);
            investmentCartDetailsResponse.setSIPStartDates(cureentDate);
            investmentCartDetailsResponse.setSIPStartDate(requestBodyObjectArrayList.get(i).getSIPStartDates());

            // investmentCartDetailsResponse.setStartDay("01");

            if (requestBodyObjectArrayList.get(i).getNoOfMonth() == null) {
                investmentCartDetailsResponse.setNoOfMonth("0");

            } else {
                investmentCartDetailsResponse.setNoOfMonth(requestBodyObjectArrayList.get(i).getNoOfMonth());
            }

            investmentCartDetailsResponse.setIsApplyNominee(requestBodyObjectArrayList.get(i).getIsApplyNominee());

            if (requestBodyObjectArrayList.get(i).getNomineeName1() != null) {
                investmentCartDetailsResponse.setNomineeName1(requestBodyObjectArrayList.get(i).getNomineeName1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName2() != null) {
                investmentCartDetailsResponse.setNomineeName2(requestBodyObjectArrayList.get(i).getNomineeName2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName3() != null) {
                investmentCartDetailsResponse.setNomineeName3(requestBodyObjectArrayList.get(i).getNomineeName3());
            }

            if (requestBodyObjectArrayList.get(i).getDateOfBirth1() == null) {
                investmentCartDetailsResponse.setDateOfBirth1("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth1(requestBodyObjectArrayList.get(i).getDateOfBirth1());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth2() == null) {
                investmentCartDetailsResponse.setDateOfBirth2("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth2());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth3() == null) {
                investmentCartDetailsResponse.setDateOfBirth3("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth3(requestBodyObjectArrayList.get(i).getDateOfBirth3());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship1() != null) {
                investmentCartDetailsResponse.setNomineeRelationship1(requestBodyObjectArrayList.get(i).getNomineeRelationship1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship2() != null) {
                investmentCartDetailsResponse.setNomineeRelationship2(requestBodyObjectArrayList.get(i).getNomineeRelationship2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship3() != null) {
                investmentCartDetailsResponse.setNomineeRelationship3(requestBodyObjectArrayList.get(i).getNomineeRelationship3());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeShare1() == null) {
                investmentCartDetailsResponse.setNomineeShare1("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare1(requestBodyObjectArrayList.get(i).getNomineeShare1());

            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare2() == null) {
                investmentCartDetailsResponse.setNomineeShare2("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare2(requestBodyObjectArrayList.get(i).getNomineeShare2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare3() == null) {
                investmentCartDetailsResponse.setNomineeShare3("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare3(requestBodyObjectArrayList.get(i).getNomineeShare3());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress1() != null) {
                investmentCartDetailsResponse.setNomineeAddress1(requestBodyObjectArrayList.get(i).getNomineeAddress1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress2() != null) {
                investmentCartDetailsResponse.setNomineeAddress2(requestBodyObjectArrayList.get(i).getNomineeAddress2());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress3() != null) {
                investmentCartDetailsResponse.setNomineeAddress3(requestBodyObjectArrayList.get(i).getNomineeAddress3());
            }

            investmentCartDetailsResponse.setNomineeIsMinor1("false");
            investmentCartDetailsResponse.setNomineeIsMinor2("false");
            investmentCartDetailsResponse.setNomineeIsMinor3("false");

            if (requestBodyObjectArrayList.get(i).getGuardianName1() != null) {
                investmentCartDetailsResponse.setGuardianName1(requestBodyObjectArrayList.get(i).getGuardianName1());
            }
            if (requestBodyObjectArrayList.get(i).getGuardianName2() != null) {
                investmentCartDetailsResponse.setGuardianName2(requestBodyObjectArrayList.get(i).getGuardianName2());
            }
            if (requestBodyObjectArrayList.get(i).getGuardianName3() != null) {
                investmentCartDetailsResponse.setGuardianName3(requestBodyObjectArrayList.get(i).getGuardianName3());
            }

            investmentCartDetailsResponse.setPlatform_id("0");
            investmentCartDetailsResponse.setFrequency("Daily");

            investmentCartDetailsResponse.setUMRN("");
            investmentCartDetailsResponse.setOrderChannelID("20");

            investmentCartDetailsResponse.setClientIP("172.17.125.94");
            investmentCartDetailsResponse.setClientMobileNo(ContactNo);

            investmentCartDetailsResponse.setOTPPassword("");
            investmentCartDetailsResponse.setChannel("Integra");

            investmentCartDetailsResponse.setClientUCC(authenticateResponse.getClientUCC());
            investmentCartDetailsResponse.setSessionID("");

            investmentCartDetailsResponse.setAuthToken("TRACENUMBER");
            investmentCartDetailsResponse.setMpinMode("OTPDisabled");

            investmentCartDetailsResponse.setRegistrationRefId("");
            investmentCartDetailsResponse.setMpinEncValue("z8g2axnP8+aVb8MJqX7CafLLQLsNyQ0jtXJpMk1AuZw=");
            investmentCartDetailsResponse.setPaymentType("");
            investmentCartDetailsResponse.setUTR("");
            investmentCartDetailsResponse.setReturnPaymentFlag("N");
            investmentCartDetailsResponse.setClientCallbackUrl("");
            finalRequestBodyObjectArrayList.add(investmentCartDetailsResponse);

        }

        UUID uuid = UUID.randomUUID();


        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        BuyGlobalRequestObjectArray globalRequestObject = new BuyGlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(finalRequestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addPlaceOrder(globalRequestObject).enqueue(new Callback<ArrayList<BuyConfirmResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<BuyConfirmResponse>> call, Response<ArrayList<BuyConfirmResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    buyConfirmResponseArrayList = response.body();
                    Bundle args = new Bundle();

                    args.putSerializable("schemeResponseArrayList", requestBodyObjectArrayList);
                    args.putSerializable("buyConfirmResponseArrayList", buyConfirmResponseArrayList);
                    args.putString("clientName", clientNames);
                    args.putString("status", status);

                    Fragment fragment = new ConfirmOrderStatusActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);

//                    if (buyConfirmResponseArrayList.get(0).getSuccessFlag().equalsIgnoreCase("false")) {
//                        Toast.makeText(getContext(), buyConfirmResponseArrayList.get(0).getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(), "Order Placed Successfull with Request Id:-" + buyConfirmResponseArrayList.get(0).getOrderNo(), Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuyConfirmResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);

                System.out.println("error mshg" + t.getLocalizedMessage());

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // swp
    private void addSwpInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_CONFIRM_BUY);

        ArrayList<InvestmentCartDetailsResponse> finalRequestBodyObjectArrayList = new ArrayList<InvestmentCartDetailsResponse>();

        for (int i = 0; i < requestBodyObjectArrayList.size(); i++) {
            InvestmentCartDetailsResponse investmentCartDetailsResponse = new InvestmentCartDetailsResponse();
            investmentCartDetailsResponse.setClientCode(authenticateResponse.getUserCode());
            investmentCartDetailsResponse.setParentChannelID("WMSPortal");
            investmentCartDetailsResponse.setICDID(requestBodyObjectArrayList.get(i).getICDID());
            investmentCartDetailsResponse.setLatestNAV(requestBodyObjectArrayList.get(i).getLatestNAV());
            investmentCartDetailsResponse.setFundCode(requestBodyObjectArrayList.get(i).getFundCode());
            investmentCartDetailsResponse.setFundName(requestBodyObjectArrayList.get(i).getFundName());
            investmentCartDetailsResponse.setValueResearchRating(requestBodyObjectArrayList.get(i).getValueResearchRating());
            investmentCartDetailsResponse.setFolio(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolioNo(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setFolioNumber(requestBodyObjectArrayList.get(i).getFolioNo());
            investmentCartDetailsResponse.setCostofInvestment(requestBodyObjectArrayList.get(i).getCostofInvestment());
            investmentCartDetailsResponse.setCurrentUnits(requestBodyObjectArrayList.get(i).getCurrentUnits());
            investmentCartDetailsResponse.setCurrentFundValue(requestBodyObjectArrayList.get(i).getCurrentFundValue());

            //   investmentCartDetailsResponse.setAllorPartial(requestBodyObjectArrayList.get(i).getAllorPartial());

            if (requestBodyObjectArrayList.get(i).getAllorPartial().equalsIgnoreCase("P")) {
                investmentCartDetailsResponse.setAllorPartial("Partial");
            } else {
                investmentCartDetailsResponse.setAllorPartial("All");
            }

            investmentCartDetailsResponse.setAmtOrUnit(requestBodyObjectArrayList.get(i).getAmountOrUnit());

            //  investmentCartDetailsResponse.setAmtOrUnit("A");


            investmentCartDetailsResponse.setTxnAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
            investmentCartDetailsResponse.setTransactionAmountUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());

            if (requestBodyObjectArrayList.get(i).getAmountOrUnit() != null) {
                if (requestBodyObjectArrayList.get(i).getAmountOrUnit().equalsIgnoreCase("A")) {
                    investmentCartDetailsResponse.setTranAmt(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
                    investmentCartDetailsResponse.setTranUnit("0");
                }
                if (requestBodyObjectArrayList.get(i).getAmountOrUnit().equalsIgnoreCase("U")) {
                    investmentCartDetailsResponse.setTranAmt("0");
                    investmentCartDetailsResponse.setTranUnit(requestBodyObjectArrayList.get(i).getTxnAmountUnit());
                }
            }
            investmentCartDetailsResponse.setTranType("S");

            if (status.equalsIgnoreCase("5")) {
                investmentCartDetailsResponse.setTransactionType("SWP");
            } else {
                investmentCartDetailsResponse.setTransactionType("STP");
            }
            investmentCartDetailsResponse.setAssetClassCode(requestBodyObjectArrayList.get(i).getAssetClassCode());
            investmentCartDetailsResponse.setAssetClassName(requestBodyObjectArrayList.get(i).getAssetClassName());
            investmentCartDetailsResponse.setL4ClientCode(requestBodyObjectArrayList.get(i).getL4ClientCode());
            investmentCartDetailsResponse.setDebitBankCode("0");
            investmentCartDetailsResponse.setDebitBankSource("0");
            investmentCartDetailsResponse.setIsDividend(requestBodyObjectArrayList.get(i).getIsDividend());
            investmentCartDetailsResponse.setDividendOption(requestBodyObjectArrayList.get(i).getDividendOption());
            // investmentCartDetailsResponse.setSwitchDividendOption(requestBodyObjectArrayList.get(i).getSwitchDividendOption());
            investmentCartDetailsResponse.setSwitchDividendOption("G");
            investmentCartDetailsResponse.setSettlementBankCode(requestBodyObjectArrayList.get(i).getSettlementBankCode());
            investmentCartDetailsResponse.setPaymentMode("7");

            investmentCartDetailsResponse.setTargetFundCode(requestBodyObjectArrayList.get(i).getTargetFundCode());
            investmentCartDetailsResponse.setTargetFundName(requestBodyObjectArrayList.get(i).getTargetFundName());


            String date = requestBodyObjectArrayList.get(i).getSIPStartDate();

            String year = "";
            String month = "";
            String day = "";

            for (int j = 0; j < 4; j++) {
                if (date.charAt(j) != '/') {
                    year = year + date.charAt(j);
                } else {
                    break;
                }
            }

            for (int k = 4; k < 6; k++) {
                if (date.charAt(k) != '/') {
                    month = month + date.charAt(k);
                } else {
                    break;
                }
            }

            for (int l = 6; l < 8; l++) {
                if (date.charAt(l) != '/') {
                    day = day + date.charAt(l);
                } else {
                    break;
                }
            }

            System.out.println("year" + year);
            System.out.println("month" + month);
            System.out.println("day" + date);


            String finalDate = year + "-" + month + "-" + day;


            investmentCartDetailsResponse.setStartDate(finalDate);
            investmentCartDetailsResponse.setSIPStartDates(finalDate);

            investmentCartDetailsResponse.setSIPStartDate(requestBodyObjectArrayList.get(i).getSIPStartDates());

            // investmentCartDetailsResponse.setStartDay("01");

            if (requestBodyObjectArrayList.get(i).getNoOfMonth() == null) {
                investmentCartDetailsResponse.setNoOfMonth("0");

            } else {
                investmentCartDetailsResponse.setNoOfMonth(requestBodyObjectArrayList.get(i).getNoOfMonth());
            }

            investmentCartDetailsResponse.setIsApplyNominee(requestBodyObjectArrayList.get(i).getIsApplyNominee());

            if (requestBodyObjectArrayList.get(i).getNomineeName1() != null) {
                investmentCartDetailsResponse.setNomineeName1(requestBodyObjectArrayList.get(i).getNomineeName1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName2() != null) {
                investmentCartDetailsResponse.setNomineeName2(requestBodyObjectArrayList.get(i).getNomineeName2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeName3() != null) {
                investmentCartDetailsResponse.setNomineeName3(requestBodyObjectArrayList.get(i).getNomineeName3());
            }

            if (requestBodyObjectArrayList.get(i).getDateOfBirth1() == null) {
                investmentCartDetailsResponse.setDateOfBirth1("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth1(requestBodyObjectArrayList.get(i).getDateOfBirth1());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth2() == null) {
                investmentCartDetailsResponse.setDateOfBirth2("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth2(requestBodyObjectArrayList.get(i).getDateOfBirth2());

            }
            if (requestBodyObjectArrayList.get(i).getDateOfBirth3() == null) {
                investmentCartDetailsResponse.setDateOfBirth3("");
            } else {
                investmentCartDetailsResponse.setDateOfBirth3(requestBodyObjectArrayList.get(i).getDateOfBirth3());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeRelationship1() != null) {
                investmentCartDetailsResponse.setNomineeRelationship1(requestBodyObjectArrayList.get(i).getNomineeRelationship1());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship2() != null) {
                investmentCartDetailsResponse.setNomineeRelationship2(requestBodyObjectArrayList.get(i).getNomineeRelationship2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeRelationship3() != null) {
                investmentCartDetailsResponse.setNomineeRelationship3(requestBodyObjectArrayList.get(i).getNomineeRelationship3());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeShare1() == null) {
                investmentCartDetailsResponse.setNomineeShare1("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare1(requestBodyObjectArrayList.get(i).getNomineeShare1());

            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare2() == null) {
                investmentCartDetailsResponse.setNomineeShare2("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare2(requestBodyObjectArrayList.get(i).getNomineeShare2());
            }
            if (requestBodyObjectArrayList.get(i).getNomineeShare3() == null) {
                investmentCartDetailsResponse.setNomineeShare3("0");
            } else {
                investmentCartDetailsResponse.setNomineeShare3(requestBodyObjectArrayList.get(i).getNomineeShare3());

            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress1() != null) {
                investmentCartDetailsResponse.setNomineeAddress1(requestBodyObjectArrayList.get(i).getNomineeAddress1());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress2() != null) {
                investmentCartDetailsResponse.setNomineeAddress2(requestBodyObjectArrayList.get(i).getNomineeAddress2());
            }

            if (requestBodyObjectArrayList.get(i).getNomineeAddress3() != null) {
                investmentCartDetailsResponse.setNomineeAddress3(requestBodyObjectArrayList.get(i).getNomineeAddress3());
            }

            investmentCartDetailsResponse.setNomineeIsMinor1("false");
            investmentCartDetailsResponse.setNomineeIsMinor2("false");
            investmentCartDetailsResponse.setNomineeIsMinor3("false");

            if (requestBodyObjectArrayList.get(i).getGuardianName1() != null) {
                investmentCartDetailsResponse.setGuardianName1(requestBodyObjectArrayList.get(i).getGuardianName1());
            }
            if (requestBodyObjectArrayList.get(i).getGuardianName2() != null) {
                investmentCartDetailsResponse.setGuardianName2(requestBodyObjectArrayList.get(i).getGuardianName2());
            }
            if (requestBodyObjectArrayList.get(i).getGuardianName3() != null) {
                investmentCartDetailsResponse.setGuardianName3(requestBodyObjectArrayList.get(i).getGuardianName3());
            }

            investmentCartDetailsResponse.setPlatform_id("0");

            if (requestBodyObjectArrayList.get(i).getFrequency() == null) {
                investmentCartDetailsResponse.setFrequency("");
            } else {
                investmentCartDetailsResponse.setFrequency(requestBodyObjectArrayList.get(i).getFrequency());
            }


            investmentCartDetailsResponse.setUMRN("");
            investmentCartDetailsResponse.setOrderChannelID("20");

            investmentCartDetailsResponse.setClientIP("172.17.125.94");
            investmentCartDetailsResponse.setClientMobileNo(ContactNo);

            investmentCartDetailsResponse.setOTPPassword("");
            investmentCartDetailsResponse.setChannel("BOBMPIN");

            investmentCartDetailsResponse.setClientUCC(authenticateResponse.getClientUCC());
            investmentCartDetailsResponse.setSessionID("");

            investmentCartDetailsResponse.setAuthToken("");
            investmentCartDetailsResponse.setMpinMode("OTPDisabled");

            investmentCartDetailsResponse.setRegistrationRefId("");
            investmentCartDetailsResponse.setMpinEncValue("z8g2axnP8+aVb8MJqX7CafLLQLsNyQ0jtXJpMk1AuZw=");
            investmentCartDetailsResponse.setPaymentType("");
            investmentCartDetailsResponse.setUTR("");
            investmentCartDetailsResponse.setReturnPaymentFlag("N");
            investmentCartDetailsResponse.setClientCallbackUrl("");
            finalRequestBodyObjectArrayList.add(investmentCartDetailsResponse);

        }

        UUID uuid = UUID.randomUUID();


        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        BuyGlobalRequestObjectArray globalRequestObject = new BuyGlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(finalRequestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addPlaceOrder(globalRequestObject).enqueue(new Callback<ArrayList<BuyConfirmResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<BuyConfirmResponse>> call, Response<ArrayList<BuyConfirmResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    buyConfirmResponseArrayList = response.body();
                    Bundle args = new Bundle();

                    args.putSerializable("schemeResponseArrayList", requestBodyObjectArrayList);
                    args.putSerializable("buyConfirmResponseArrayList", buyConfirmResponseArrayList);
                    args.putString("clientName", clientNames);
                    args.putString("status", status);

                    Fragment fragment = new ConfirmOrderStatusActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
//                    if (buyConfirmResponseArrayList.get(0).getSuccessFlag().equalsIgnoreCase("false")) {
//                        Toast.makeText(getContext(), buyConfirmResponseArrayList.get(0).getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(), "Order Placed Successfull with Request Id:-" + buyConfirmResponseArrayList.get(0).getRequestID(), Toast.LENGTH_LONG).show();
//                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BuyConfirmResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);

                System.out.println("error mshg" + t.getLocalizedMessage());

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // details
    private void callRMDetailAPI() {

        util.showProgressDialog(getContext(), true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_RM_DETAIL);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

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

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        RMDetailRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getRMDetailResponse(RMDetailRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<RMDetailResponseObject>>() {

            @Override
            public void onResponse(Call<ArrayList<RMDetailResponseObject>> call, Response<ArrayList<RMDetailResponseObject>> response) {

                util.showProgressDialog(getContext(), false);

                System.out.println("Call Rm VALIDATION RESPONSE: " + new Gson().toJson(response.body()));


                if (response.isSuccessful()) {

                    rmDetailResponseObjectArrayList = response.body();

                    clientNames = rmDetailResponseObjectArrayList.get(0).getClientName();
                    ContactNo = rmDetailResponseObjectArrayList.get(0).getContactNo();
                    if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("2")) {
                        setAdapter(clientName);
                    }
                    else
                    {
                        setAdapter(clientNames);
                    }

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

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    public void getDetail(Fragment fragment) {
        replaceFragment(fragment);
    }
}
