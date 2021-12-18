package com.bob.bobapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.bob.bobapp.adapters.FolioAdapter;
import com.bob.bobapp.adapters.FolioSpinnerAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.bean.SpinnerRowItem;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AddInvCartResponse;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.api.response_object.MFOrderValidationResponse;
import com.bob.bobapp.dialog.PopupDialog;
import com.bob.bobapp.dialog.SortByDialog;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.listener.OnGenericListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends BaseFragment implements OnGenericListener {
    private AppCompatTextView txtName, investmentAccountName, txtFolio;
    private AppCompatEditText edtAMount;
    private AppCompatButton btnAddToCart;
    private AppCompatSpinner spineerFolio;
    private ClientHoldingObject clientHoldingObject;
    private Context context;
    private Util util;
    private ArrayList<FolioWisePendingUnitsCollection> folioWisePendingUnitsCollectionArrayList = new ArrayList<>();
    private FolioAdapter folioAdapter;
    private String ValueResearchRating = "", FundOption = "", amountUnit = "", DividendOption = "", LatestNAV = "",
            IsDividend, folioNumber = "", l4ClientCode = "", commanScriptCode = "", schemeName = "",
            l4ClientCodes = "", status = "", existFolio = "", IsNFO = "", IsRestricted = "", CutOffTimeCrossed = "";
    private double MinInvAmount, PurchaseMultipleOf;
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.buy_activity, container, false);
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
        txtName = view.findViewById(R.id.txtName);
        spineerFolio = view.findViewById(R.id.spineerFolio);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        edtAMount = view.findViewById(R.id.edtAMount);
        txtFolio = view.findViewById(R.id.txtFolio);

        txtFolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllPlanDialog("1");
            }
        });
    }

    @Override
    protected void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.title.setText("Buy");
        BOBActivity.llMenu.setVisibility(View.GONE);

        if (getArguments() != null) {
            commanScriptCode = getArguments().getString("commanScriptCode");
            schemeName = getArguments().getString("schemeName");
            status = getArguments().getString("status");
            existFolio = getArguments().getString("existFolio");
        }

        //  Toast.makeText(getContext(), existFolio, Toast.LENGTH_SHORT).show();

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
                if (CutOffTimeCrossed.equalsIgnoreCase("Y")) {
                    showAddCartDialog("Order is initiated post cut off time, it will be submitted to AMC on next business day. Do you wish to proceed further?.");
                } else {
                    addInvCartAPIResponse();
                }
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            }
        }

        if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        }
    }

    // validation
    private String validatonForm() {
        String result = "";
        amountUnit = edtAMount.getText().toString().trim();
        folioNumber = txtFolio.getText().toString().trim();

        if (folioNumber.equalsIgnoreCase("Select")) {
            return "Please select folio no";
        }

        if (IsNFO.equalsIgnoreCase("Y")) {
            return "Purchase not allowed as Value date is greater than NFO End Date.";
        }

        if (IsRestricted.equalsIgnoreCase("Y")) {
            return "Nationality of the client falls within the restricted nationality defined in the master.";
        }


        if (amountUnit.isEmpty() || amountUnit.equalsIgnoreCase("")) {
            edtAMount.setFocusable(true);
            edtAMount.requestFocus();
            return "Please enter the Amount greater than 0";
        }


        if (MinInvAmount > Double.parseDouble(amountUnit)) {
            return "Minimum amount for transaction is" + " " + MinInvAmount + " " + " Please enter a value greater than or equal to the minimum amount";
        }

        if (Double.parseDouble(amountUnit) % PurchaseMultipleOf != 0) {
            return "The amount for" + " " + txtName.getText().toString() + " " + "needs to be in multiples of Rs " + PurchaseMultipleOf + "" + ".Please round it off to the nearest multiple";
        }

        return result = "success";

    }


    // adapter
//    private void setFolioAdapter(ArrayList<FolioWisePendingUnitsCollection> arrayList) {
//        folioAdapter = new FolioAdapter(getContext(), arrayList);
//        spineerFolio.setAdapter(folioAdapter);
//
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

        requestBodyObject.setTransactionType("BUY");

        String tillDate = util.getCurrentDate(false);

        // requestBodyObject.setTillDate(tillDate);

        requestBodyObject.setTillDate(authenticateResponse.getBusinessDate());

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

                System.out.println("Mf ORDER response " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    if (status.equalsIgnoreCase("2")) {
                        folioWisePendingUnitsCollectionArrayList = response.body().getFolioWisePendingUnitsCollection();
                        if (folioWisePendingUnitsCollectionArrayList.size() > 0) {

                        } else {
                            FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                            folioWisePendingUnitsCollection.setFolioNo("New Folio");
                            folioWisePendingUnitsCollectionArrayList.add(folioWisePendingUnitsCollection);
                            folioNumber = "New Folio";
                        }
                    }
                    else if (status.equalsIgnoreCase("3")) {
                        folioWisePendingUnitsCollectionArrayList = response.body().getFolioWisePendingUnitsCollection();
                        if (folioWisePendingUnitsCollectionArrayList.size() > 0) {
                            FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                            folioWisePendingUnitsCollection.setFolioNo("New Folio");
                            folioWisePendingUnitsCollectionArrayList.add(0, folioWisePendingUnitsCollection);
                        } else {
                            FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                            folioWisePendingUnitsCollection.setFolioNo("New Folio");
                            folioWisePendingUnitsCollectionArrayList.add(folioWisePendingUnitsCollection);
                            folioNumber = "New Folio";
                        }

                    }
                    else {
                        FolioWisePendingUnitsCollection folioWisePendingUnitsCollection = new FolioWisePendingUnitsCollection();
                        folioWisePendingUnitsCollection.setFolioNo(existFolio);
                        folioWisePendingUnitsCollectionArrayList.add(folioWisePendingUnitsCollection);
                    }

                    //setFolioAdapter(folioWisePendingUnitsCollectionArrayList);

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
                PurchaseMultipleOf = Double.parseDouble(response.body().getPurchaseMultipleOf());
                IsNFO = response.body().getIsNFO();
                IsRestricted = response.body().getIsRestricted();
                CutOffTimeCrossed = response.body().getCutOffTimeCrossed();
                ValueResearchRating = response.body().getValueResearchRating();
                FundOption = response.body().getFundOption();
                DividendOption = response.body().getDividendOption();
                //    Toast.makeText(getContext(),DividendOption,Toast.LENGTH_SHORT).show();
                LatestNAV = response.body().getLatestNAV();
                MinInvAmount = Double.parseDouble(response.body().getMinInvAmount());


                String IsDividends = response.body().getIsDividend();
                if (IsDividends.equalsIgnoreCase("0")) {
                    IsDividend = "false";
                } else {
                    IsDividend = "true";
                }
                callAccountDetailAPI();
            }

            @Override
            public void onFailure(Call<MFOrderValidationResponse> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // add cart api
    private void addInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            requestBodyObject.setValueResearchRating(ValueResearchRating);

            if (clientHoldingObject != null) {
                requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
                requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());
                requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
                requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            } else {
                requestBodyObject.setSchemeCode(commanScriptCode);
                requestBodyObject.setSchemeName(schemeName);
                requestBodyObject.setL4ClientCode(l4ClientCodes);
                requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());
            }

            requestBodyObject.setFundOption(FundOption);

            requestBodyObject.setTxnAmountUnit(amountUnit); //For INR

            requestBodyObject.setFundRiskRating(""); //For base

            requestBodyObject.setDividendOption(DividendOption);

            requestBodyObject.setTransactionType("Buy"); //For client

            requestBodyObject.setInputmode("2");

            requestBodyObject.setLatestNAV(LatestNAV);

            requestBodyObject.setIsDividend(IsDividend);

            requestBodyObject.setFolioNo(folioNumber);

            requestBodyObject.setParentChannelID("WMSPortal");

            requestBodyObject.setChannelID("Transaction");

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
                    showDialog();
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

            }

            @Override
            public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // show popup
    private void showDialog() {
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
                //BOBActivity.mTabHost.setCurrentTab(1);
            }
        });


        dialog.show();

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

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    // plan Dialog
    private void openAllPlanDialog(String status) {
        PopupDialog allPlanDialog = new PopupDialog(getContext(), folioWisePendingUnitsCollectionArrayList, this, status);
        allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        allPlanDialog.show();
    }

    @Override
    public void onItemDeleteListener(String name, String fundCode, String status, String bankSource, String accountNumber) {
        txtFolio.setText(name);
    }
}
