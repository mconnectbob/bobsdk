package com.bob.bobapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.FolioAdapter;
import com.bob.bobapp.adapters.SipDateAdapter;
import com.bob.bobapp.adapters.SipFrequencyAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.api.response_object.MFOrderValidationResponse;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.api.response_object.SystematicSchemeDataResponse;
import com.bob.bobapp.dialog.PopupDialog;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.OnGenericListener;
import com.bob.bobapp.utility.BMSPrefs;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SipActivity extends BaseFragment implements OnGenericListener {
    private AppCompatTextView txtName, investmentAccountName, txtDate, txtFolio, txtDay, txtLatestFrequency;
    private AppCompatSpinner spineerFolio, spineerFrequency, spineerDay;
    private AppCompatEditText edtAMount, edtInstallments;
    private CheckBox checkSelect;
    private LinearLayoutCompat linearInstallment;
    private AppCompatButton btnAddToCart;
    private ClientHoldingObject clientHoldingObject;
    private Context context;
    private Util util;
    private ArrayList<FolioWisePendingUnitsCollection> folioWisePendingUnitsCollectionArrayList = new ArrayList<>();
    private FolioAdapter folioAdapter;
    private SipFrequencyAdapter SipFrequencyAdapter;
    private SipDateAdapter sipDateAdapter;
    private String frequency = "", sipStartDates = "", l4ClientCode = "";
    private ArrayList<String> frequencyList=new ArrayList<>();
    private ArrayList<String> sipStartDatesList;
    private ArrayList<String> finalFrequencyList = new ArrayList<>();
    private ArrayList<String> finalSipStartDatesList;
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();
    private String amount = "", ValueResearchRating = "", LatestNAV = "", FundOption = "", folioNumber,
            IsDividend = "", selectFrequency = "", DividendOption = "", finalSipStartDate, installmentNumber = "", IsPerpetual = "false",
            dateTime, year, currentMonth, SipDate, finalSipDate, nextInstallment, schemeName, commanScriptCode, l4ClientCodes = "",
            dateOfBirth = "", userYear = "", currentDay, CutOffTimeCrossed = "", SIPAllowed = "",status="";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Integer perpectualInstallment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.sip_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getExtraDataFromIntent();

        setData();

        getValidationResponse();

    }

    // set data here...
    private void setData() {
        if (clientHoldingObject != null) {
            txtName.setText(clientHoldingObject.getSchemeName());
            l4ClientCode = clientHoldingObject.getL4Client_Code();
        } else {
            txtName.setText(schemeName);
        }
    }

    private void getExtraDataFromIntent() {

        if (getArguments() != null) {

            String response = getArguments().getString(IntentKey.RESPONSE_KEY);

            clientHoldingObject = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }

    @Override
    protected void getIds(View view) {
//        try {
//            dateOfBirth = BMSPrefs.getString(getContext(), "userDob");
//
//            String[] arrayStringss = dateOfBirth.split("T");
//
//            String userDob = arrayStringss[0];
//
//            String[] arrayStrings = userDob.split("-");
//
//            userYear = arrayStrings[0];
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = simpleDateFormat.format(calendar.getTime());

        //   Toast.makeText(getContext(), dateTime, Toast.LENGTH_SHORT).show();
        String s = dateTime;
        String[] arrayString = s.split("-");

        year = arrayString[0];
        currentMonth = arrayString[1];
        currentDay = arrayString[2];


        txtName = view.findViewById(R.id.txtName);
        spineerFolio = view.findViewById(R.id.spineerFolio);
        spineerFrequency = view.findViewById(R.id.spineerFrequency);
        spineerDay = view.findViewById(R.id.spineerDay);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        edtAMount = view.findViewById(R.id.edtAMount);
        edtInstallments = view.findViewById(R.id.edtInstallments);
        checkSelect = view.findViewById(R.id.checkSelect);
        linearInstallment = view.findViewById(R.id.linearInstallment);
        txtDate = view.findViewById(R.id.txtDate);
        txtFolio = view.findViewById(R.id.txtFolio);
        txtDay = view.findViewById(R.id.txtDay);
        txtLatestFrequency = view.findViewById(R.id.txtLatestFrequency);

        checkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsPerpetual = "true";
                    linearInstallment.setVisibility(View.GONE);
                    edtAMount.setText("");
                    edtInstallments.setText("");
                    noOfInstallments();
                } else {
                    IsPerpetual = "false";
                    linearInstallment.setVisibility(View.VISIBLE);
                    edtAMount.setText("");
                }
            }
        });

        txtFolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllPlanDialog(folioWisePendingUnitsCollectionArrayList, "1");
            }
        });


    }

    @Override
    protected void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
        txtDay.setOnClickListener(this);
        txtLatestFrequency.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);

        BOBActivity.title.setText("Sip");
        BOBActivity.llMenu.setVisibility(View.GONE);
        if (getArguments() != null) {

            commanScriptCode = getArguments().getString("commanScriptCode");
            schemeName = getArguments().getString("schemeName");
            status = getArguments().getString("status");

//            model = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAddToCart) {
            String result = validatonForm();
            if (result.equalsIgnoreCase("success")) {
                if (CutOffTimeCrossed.equalsIgnoreCase("Y") && dateTime.equalsIgnoreCase(SipDate)) {
                    showAddCartDialog("Order placed is after Security Cut-off time or on a holiday. Start date should be next allowed date.Do you wish to proceed further?");
                }
                else {
                    callAmountValidationAPI();
                }
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            }

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

        if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        }
    }


    // validations
    private String validatonForm()
    {
        String result = "";

        amount = edtAMount.getText().toString().trim();

        installmentNumber = edtInstallments.getText().toString().trim();

        folioNumber = txtFolio.getText().toString().trim();



        if (txtLatestFrequency.getText().toString().isEmpty() || txtLatestFrequency.getText().toString().equalsIgnoreCase("Select")) {
            return "Please select frequency";
        }

        if (txtDay.getText().toString().isEmpty() || txtDay.getText().toString().equalsIgnoreCase("Select")) {
            return "Please select day";
        }

        if (IsPerpetual.equalsIgnoreCase("false")) {
            if (installmentNumber.isEmpty() || installmentNumber.equalsIgnoreCase("") || installmentNumber.equalsIgnoreCase("0")) {
                edtInstallments.setFocusable(true);
                edtInstallments.requestFocus();
                return "Please enter the no. of Installments";
            }
        }


        if (folioNumber.isEmpty() || folioNumber.equalsIgnoreCase("Select")) {
            return "Please select folio no";
        }

        if (amount.isEmpty() || amount.equalsIgnoreCase("")) {
            edtAMount.setFocusable(true);
            edtAMount.requestFocus();
            return "Please enter the Amount/Units greater than 0.";
        }


        if (SIPAllowed.equalsIgnoreCase("false")) {
            return txtName.getText().toString() + " " + "is not Allowed to transact";
        }


        return result = "success";

    }


    // no of installment
    private void noOfInstallments() {
        Integer CalculateDifferenceYears = Integer.parseInt(year) - Integer.parseInt(userYear);
        Integer subtractDifferencYears = 100 - CalculateDifferenceYears;
        perpectualInstallment = subtractDifferencYears * 12;
    }

    // adapter
//    private void setFolioAdapter(ArrayList<FolioWisePendingUnitsCollection> arrayList) {
//        folioAdapter = new FolioAdapter(getContext(), arrayList);
//        spineerFolio.setAdapter(folioAdapter);
//        spineerFolio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                if (arrayList.size() > 0) {
//                    folioNumber = arrayList.get(position).getFolioNo();
//                } else {
//                    folioNumber = "New Folio";
//                }
//                //   Toast.makeText(getContext(), folioNumber, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//
//    }

    // sip frequency adapter
//    private void setFrequencyAdapter(ArrayList<String> arrayList) {
//        SipFrequencyAdapter = new SipFrequencyAdapter(getContext(), arrayList);
//        spineerFrequency.setAdapter(SipFrequencyAdapter);
//
//        spineerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                selectFrequency = arrayList.get(position);
//
//                //  Toast.makeText(getContext(), selectFrequency, Toast.LENGTH_SHORT).show();
//
//                String Day = sipStartDatesList.get(position);
//
//                finalSipStartDate = Day;
//
//                finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));
//
//             //   setDateAdapter(finalSipStartDatesList);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//    }


    // day frequency adapter
//    private void setDateAdapter(ArrayList<String> arrayList) {
//        sipDateAdapter = new SipDateAdapter(getContext(), arrayList);
//        spineerDay.setAdapter(sipDateAdapter);
//
//        spineerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectDate = arrayList.get(position);
//                String dskj = "";
//                String yearsss = "";
//
//                if (Integer.parseInt(selectDate) < Integer.parseInt(currentDay)) {
//                    if (Integer.parseInt(currentMonth) < 12) {
//                        String asd = String.valueOf(Integer.parseInt(currentMonth) + 1);
//                        dskj = asd;
//                        txtDate.setText(year + "-" + dskj + "-" + selectDate);
//                    } else {
//                        String setYear = String.valueOf(Integer.parseInt(year) + 1);
//                        String monthsss = "1";
//                        txtDate.setText(setYear + "-" + monthsss + "-" + selectDate);
//                    }
//
//                } else {
//                    txtDate.setText(year + "-" + currentMonth + "-" + selectDate);
//                }
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


    // api
    private void getValidationResponse() {

        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        if (clientHoldingObject != null) {
            requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
        } else {
            requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());
            requestBodyObject.setSchemeCode(commanScriptCode);
        }

        requestBodyObject.setTransactionType("SIP");

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

                if (response.isSuccessful())
                {
                    if (status.equalsIgnoreCase("2"))
                    {
                        folioWisePendingUnitsCollectionArrayList = response.body().getFolioWisePendingUnitsCollection();
                        if (folioWisePendingUnitsCollectionArrayList.size() > 0) {

                        } else {
                            FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                            folioWisePendingUnitsCollection.setFolioNo("New Folio");
                            folioWisePendingUnitsCollectionArrayList.add(folioWisePendingUnitsCollection);
                          //  folioNumber = "New Folio";
                        }
                    }
                     if (status.equalsIgnoreCase("3"))
                     {
                        folioWisePendingUnitsCollectionArrayList = response.body().getFolioWisePendingUnitsCollection();
                        if (folioWisePendingUnitsCollectionArrayList.size() > 0) {
                            FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                            folioWisePendingUnitsCollection.setFolioNo("New Folio");
                            folioWisePendingUnitsCollectionArrayList.add(0, folioWisePendingUnitsCollection);
                        } else {
                            FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                            folioWisePendingUnitsCollection.setFolioNo("New Folio");
                            folioWisePendingUnitsCollectionArrayList.add(folioWisePendingUnitsCollection);
                        //    folioNumber = "New Folio";
                        }

                    }


                    //     setFolioAdapter(folioWisePendingUnitsCollectionArrayList);

                    frequency = response.body().getSipFrequency();

                    frequencyList.clear();

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
                    // setFrequencyAdapter(finalFrequencyList);

                    sipStartDates = response.body().getSipStartDates();

                    sipStartDatesList = new ArrayList<>(Arrays.asList(sipStartDates.split("\\|,")));

                    String Day = sipStartDatesList.get(0);

                    finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));

                    //   setDateAdapter(finalFrequencyList);

                    SIPAllowed = response.body().getSIPAllowed();
                    CutOffTimeCrossed = response.body().getCutOffTimeCrossed();
                    ValueResearchRating = response.body().getValueResearchRating();
                    LatestNAV = response.body().getLatestNAV();
                    FundOption = response.body().getFundOption();
                    DividendOption = response.body().getDividendOption();

                    String IsDividends = response.body().getIsDividend();
                    if (IsDividends.equalsIgnoreCase("0")) {
                        IsDividend = "false";
                    } else {
                        IsDividend = "true";
                    }

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


    // for showing name
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

                if (response.isSuccessful()) {
                    accountResponseObjectArrayList = response.body();
                    l4ClientCodes = accountResponseObjectArrayList.get(1).getClientCode();
                    if (l4ClientCode.equalsIgnoreCase("")) {
                        investmentAccountName.setText(accountResponseObjectArrayList.get(1).getClientName());

                    } else {
                        for (int i = 0; i < accountResponseObjectArrayList.size(); i++) {
                            if (accountResponseObjectArrayList.get(i).getClientCode().equalsIgnoreCase(l4ClientCode)) {
                                investmentAccountName.setText(accountResponseObjectArrayList.get(i).getClientName());
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

                callRMDetailAPI();
            }

            @Override
            public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // add cart api
    private void addInvCartAPIResponse() {

        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            if (clientHoldingObject != null) {
                requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
                requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
                requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());

                requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

            } else {
                requestBodyObject.setL4ClientCode(l4ClientCodes);
                requestBodyObject.setSchemeCode(commanScriptCode);
                requestBodyObject.setSchemeName(schemeName);
                requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());

            }

            requestBodyObject.setValueResearchRating(ValueResearchRating);
            requestBodyObject.setLatestNAV(LatestNAV);
            requestBodyObject.setFundOption(FundOption);

            requestBodyObject.setParentChannelID("WMSPortal");
            requestBodyObject.setIsPerpetual(IsPerpetual);
            requestBodyObject.setTxnAmountUnit(amount);
            requestBodyObject.setFolioNo(folioNumber);
            requestBodyObject.setFolio(folioNumber);

            requestBodyObject.setFundRiskRating(""); //For base
            requestBodyObject.setIsDividend(IsDividend);
            requestBodyObject.setCostofInvestment("0");
            requestBodyObject.setFrequency(selectFrequency);
            requestBodyObject.setDividendOption(DividendOption);
            requestBodyObject.setInputmode("2");
            if (IsPerpetual.equalsIgnoreCase("true")) {
                requestBodyObject.setPeriod("" + perpectualInstallment);
                requestBodyObject.setNoofMonth("" + perpectualInstallment);
            } else {
                requestBodyObject.setPeriod(installmentNumber);
                requestBodyObject.setNoofMonth(installmentNumber);
            }

            requestBodyObject.setChannelID("Transaction");
            requestBodyObject.setSipStartDates(finalSipStartDate);
            requestBodyObject.setStartDate(finalSipDate);
            requestBodyObject.setNextInstallmentDate(nextInstallment);
            requestBodyObject.setTransactionType("SIP"); //For client
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


    // amount validatiom
    private void callAmountValidationAPI() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        if (clientHoldingObject != null) {
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
        } else {
            requestBodyObject.setSchemeCode(commanScriptCode);
        }

        if (IsPerpetual.equalsIgnoreCase("true")) {
            requestBodyObject.setInstallments("" + perpectualInstallment);
        } else {
            requestBodyObject.setInstallments(installmentNumber);
        }

        requestBodyObject.setRequestType("SIP");
        if (selectFrequency.equalsIgnoreCase("Daily")) {
            requestBodyObject.setFrequencyss("D");

        }
        if (selectFrequency.equalsIgnoreCase("Monthly")) {
            requestBodyObject.setFrequencyss("M");

        }
        if (selectFrequency.equalsIgnoreCase("Quarterly")) {
            requestBodyObject.setFrequencyss("Q");

        }
        if (selectFrequency.equalsIgnoreCase("Weekly")) {
            requestBodyObject.setFrequencyss("W");

        }
        if (selectFrequency.equalsIgnoreCase("Yearly")) {
            requestBodyObject.setFrequencyss("Y");

        }

        if (selectFrequency.equalsIgnoreCase("HalfYearly")) {
            requestBodyObject.setFrequencyss("H");

        }

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.GetSystematicSchemeData(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<SystematicSchemeDataResponse>() {

            @Override
            public void onResponse(Call<SystematicSchemeDataResponse> call, Response<SystematicSchemeDataResponse> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    try {
                        double amt = Double.parseDouble(response.body().getSIPAmount());
                        if (amt > Double.parseDouble(amount)) {
                            Toast.makeText(getContext(), "Minimum amount for transaction is " + amt + " " + " Please enter a value greater than or equal to the minimum amount.", Toast.LENGTH_LONG).show();
                        } else {
                            addInvCartAPIResponse();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SystematicSchemeDataResponse> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void callRMDetailAPI() {

       // util.showProgressDialog(context, true);

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

                    ArrayList<RMDetailResponseObject> rmDetailResponseObjectArrayList = response.body();

                    RMDetailResponseObject rmDetailResponseObject = rmDetailResponseObjectArrayList.get(0);

                    String dob = rmDetailResponseObjectArrayList.get(0).getDateOfBirth();

                  //  BMSPrefs.putString(getContext(), "userDob", dob);

                    try {
                        dateOfBirth = dob;

                        String[] arrayStringss = dateOfBirth.split("T");

                        String userDob = arrayStringss[0];

                        String[] arrayStrings = userDob.split("-");

                        userYear = arrayStrings[0];
                    } catch (Exception ex) {
                        ex.printStackTrace();
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


    // plan Dialog
    private void openAllPlanDialog(ArrayList<? extends Object> arrayList, String status) {
        PopupDialog allPlanDialog = new PopupDialog(getContext(), arrayList, this, status);
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onItemDeleteListener(String name, String fundCode, String status,String bankSource,String accountNumber) {
        if (status.equalsIgnoreCase("1")) {
            txtFolio.setText(name);
        }

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
            String yearsss = "";

            if (Integer.parseInt(selectDate) < Integer.parseInt(currentDay)) {
                if (Integer.parseInt(currentMonth) < 12) {
                    String asd = String.valueOf(Integer.parseInt(currentMonth) + 1);
                    dskj = asd;
                    txtDate.setText(year + "-" + dskj + "-" + selectDate);
                } else {
                    String setYear = String.valueOf(Integer.parseInt(year) + 1);
                    String monthsss = "1";
                    txtDate.setText(setYear + "-" + monthsss + "-" + selectDate);
                }

            } else {
                txtDate.setText(year + "-" + currentMonth + "-" + selectDate);
            }

            SipDate = txtDate.getText().toString();

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

            nextInstallment = (year + 1) + month + date;

            Integer installMonth = Integer.parseInt(year) + 1;

            nextInstallment = String.valueOf(installMonth) + month + date;

        }

    }


    private void showAddCartDialog(String message) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_cart_validation_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        AppCompatButton btnMoveToCart = dialog.findViewById(R.id.btnMoveToCart);
        AppCompatButton btnAddMore = dialog.findViewById(R.id.btnAddMore);
        AppCompatTextView txtMessage = dialog.findViewById(R.id.txtMessage);

        txtMessage.setText(message);

        btnMoveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callAmountValidationAPI();
            }
        });


        dialog.show();

    }

}
