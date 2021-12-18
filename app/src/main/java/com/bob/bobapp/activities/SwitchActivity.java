package com.bob.bobapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.bob.bobapp.dialog.SwitchFundPopup;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.OnGenericListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwitchActivity extends BaseFragment implements OnGenericListener {
    private AppCompatTextView txtName, txtFolioNumber, investmentAccountName, txtAmount, txtUnits,
            txtFullSwitch, txtCurrentFundValue, txtSealableUnits, txtAmounts, txtSwitchFund;
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
            IsDividend, FundOption, AmountOrUnit = "A", AllorPartial = "Partial", MarketValue,
            selectSeable = "1", IsNFO = "",IsRestricted="",cutOffTimeCrossed="";
    private double quantity, AwaitingHoldingUnit, SalableUnit, Awatingholdingamt, existingAmount;
    private DecimalFormat formatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.switch_activity, container, false);
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
        txtAmounts = view.findViewById(R.id.txtAmounts);
        txtSwitchFund = view.findViewById(R.id.txtSwitchFund);
    }

    @Override
    protected void handleListener() {
        txtAmount.setOnClickListener(this);
        txtUnits.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
        txtFullSwitch.setOnClickListener(this);
        txtSwitchFund.setOnClickListener(this);
        BOBActivity.imgBack.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.title.setText("Switch");
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
            edtAMount.setText("");
            selectSeable = "1";
            txtFullSwitch.setText("ENTIRE UNITS");
            txtAmounts.setText("Switch Amount");
            AmountOrUnit = "A";
            AllorPartial = "Partial";
            //   txtAmount.setBackgroundColor(Color.parseColor("#1F3C66"));
            txtAmount.setTextColor(Color.parseColor("#1F3C66"));

            //     txtUnits.setBackgroundColor(Color.parseColor("#ffffff"));
            txtUnits.setTextColor(Color.parseColor("#817D7D"));

            //  txtFullSwitch.setBackgroundColor(Color.parseColor("#ffffff"));
            txtFullSwitch.setTextColor(Color.parseColor("#817D7D"));
        }

        if (id == R.id.txtUnits) {
            edtAMount.setText("");
            selectSeable = "2";
            txtFullSwitch.setText("ENTIRE UNITS");
            txtAmounts.setText("Switch Units");
            AmountOrUnit = "U";
            AllorPartial = "Partial";
            //       txtUnits.setBackgroundColor(Color.parseColor("#1F3C66"));
            txtUnits.setTextColor(Color.parseColor("#1F3C66"));

            //    txtAmount.setBackgroundColor(Color.parseColor("#ffffff"));
            txtAmount.setTextColor(Color.parseColor("#817D7D"));

            //   txtFullSwitch.setBackgroundColor(Color.parseColor("#ffffff"));
            txtFullSwitch.setTextColor(Color.parseColor("#817D7D"));
        }
        if (id == R.id.txtFullSwitch) {
            if (SalableUnit > 0) {
                edtAMount.setText("" + SalableUnit);
            } else {
                edtAMount.setText("0");
            }

            selectSeable = "3";
            txtFullSwitch.setText("FULL SWITCH");
            txtAmounts.setText("Saleable Units");
            AmountOrUnit = "U";
            AllorPartial = "All";
            //  txtUnits.setBackgroundColor(Color.parseColor("#ffffff"));
            txtUnits.setTextColor(Color.parseColor("#817D7D"));

            //    txtAmount.setBackgroundColor(Color.parseColor("#ffffff"));
            txtAmount.setTextColor(Color.parseColor("#817D7D"));

            //   txtFullSwitch.setBackgroundColor(Color.parseColor("#1F3C66"));
            txtFullSwitch.setTextColor(Color.parseColor("#1F3C66"));
        }

        if (id == R.id.btnAddToCart) {
            String result = validatonForm();
            if (result.equalsIgnoreCase("success"))
            {
                if (cutOffTimeCrossed.equalsIgnoreCase("Y")) {
                    showAddCartDialog("Order is initiated post cut off time, it willbe submitted to AMC on next business day. Do you wish to proceed further?.");
                } else {
                    addInvCartAPIResponse();
                }
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();

            }
        }

        if (id == R.id.txtSwitchFund) {
            openAllPlanDialog();
        }


    }

    // validation popup
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
                addInvCartAPIResponse();
            }
        });


        dialog.show();

    }


    // validation
    private String validatonForm() {
        String result = "";

        amount = edtAMount.getText().toString().trim();
        targetFundName = txtSwitchFund.getText().toString().trim();

        if (targetFundName.isEmpty() || targetFundName.equalsIgnoreCase("select")) {
            return "please select switch to fund";
        }
        if (amount.isEmpty() || amount.equalsIgnoreCase("")) {
            edtAMount.setFocusable(true);
            edtAMount.requestFocus();
            return "please enter saleable amount";
        }

        if (selectSeable.equalsIgnoreCase("2") || amount.equalsIgnoreCase("0")) {
            if (Double.parseDouble(amount) > SalableUnit) {
                return "Saleable Units should not greater than Actual Saleable Units";
            }
        }

        if (selectSeable.equalsIgnoreCase("3") || amount.equalsIgnoreCase("0")) {
            if (Double.parseDouble(amount) > SalableUnit) {
                return "Saleable Units should not greater than Actual Saleable Units";
            }
        }

        if (selectSeable.equalsIgnoreCase("1")) {
            double amt = Double.parseDouble(MarketValue) - (Awatingholdingamt + existingAmount);
            if (Double.parseDouble(amount) > amt) {
                return "The amount entered for" + " " + clientHoldingObject.getSchemeName() + " " + "is greater than the amount you hold(Considering pending STP orders).Please enter the correct amount for" + " " + clientHoldingObject.getFolio();
                //     Toast.makeText(getContext(), "The amount entered for" + " " + clientHoldingObject.getSchemeName() + " " + "is greater than the amount you hold(Considering pending STP orders).Please enter the correct amount for" + " " + clientHoldingObject.getFolio(), Toast.LENGTH_LONG).show();
            }
        }

        if (IsNFO.equalsIgnoreCase("Y")) {
            return "Purchase not allowed as Value date is greater than NFO End Date.";
        }

        if (IsRestricted.equalsIgnoreCase("Y")) {
            return "Nationality of the client falls within the restricted nationality defined in the master.";
        }

        return result = "success";
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
//    private void setAdapter(ArrayList<TranferSchemeResponse> arrayList) {
//        switchFundAdapter = new SwitchFundAdapter(getContext(), arrayList);
//        spineerSwitchFund.setAdapter(switchFundAdapter);
//        spineerSwitchFund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                targetFundName = arrayList.get(position).getName();
//                targetFundCode = arrayList.get(position).getCode();
//                // Toast.makeText(getContext(),targetFundCode,Toast.LENGTH_SHORT).show();
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
                    //setAdapter(switchFundArrayList);
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

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setClientType("H"); //H for client

        requestBodyObject.setIsFundware("false");

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);
        try {
            SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
                    IsNFO = response.body().getIsNFO();
                    IsRestricted = response.body().getIsRestricted();
                    cutOffTimeCrossed = response.body().getCutOffTimeCrossed();
                  //  Toast.makeText(getContext(),cutOffTimeCrossed,Toast.LENGTH_SHORT).show();

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

                }
                else {
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
            requestBodyObject.setTxnAmountUnit(amount);
            requestBodyObject.setTargetFundName(targetFundName);
            requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
            requestBodyObject.setTargetFundCode(targetFundCode);
            requestBodyObject.setDividendOption(DividendOption);
            requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());
            requestBodyObject.setLatestNAV(LatestNAV);
            requestBodyObject.setInputmode("2");
            requestBodyObject.setAllorPartial(AllorPartial);
            requestBodyObject.setIsDividend(IsDividend);
            requestBodyObject.setTransactionType("SWITCH");
            requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
            requestBodyObject.setCurrentUnits("" + quantity);
            requestBodyObject.setAmountOrUnit(AmountOrUnit);
            requestBodyObject.setCurrentFundValue(MarketValue);
            requestBodyObject.setCostofInvestment("0");

            requestBodyObject.setParentChannelID("WMSPortal");
            requestBodyObject.setFolioNo(txtFolioNumber.getText().toString());
            requestBodyObject.setChannelID("Transaction");
            requestBodyObject.setFundOption(FundOption);

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
                    if (selectSeable.equalsIgnoreCase("3")) {

                    } else {
                        edtAMount.setText("");
                    }
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
    private void openAllPlanDialog() {
        SwitchFundPopup allPlanDialog = new SwitchFundPopup(getContext(), switchFundArrayList, this);
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onItemDeleteListener(String name, String fundCode, String status,String bankSource,String accountNumber) {
        txtSwitchFund.setText(name);
        targetFundCode = fundCode;
    }


}
