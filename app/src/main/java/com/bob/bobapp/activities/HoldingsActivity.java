package com.bob.bobapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.HoldingListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.ClientHoldingRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.OrderStatusResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoldingsActivity extends BaseFragment {
    private TextView btn_transaction;
    private RecyclerView rvHoldings;
    private ArrayList<ClientHoldingObject> holdingArrayList=new ArrayList<>() ;
    private APIInterface apiInterface;
    private Util util;
    private EditText etSearch;
    private HoldingListAdapter adapter;
    private String searchKey = "";
    private Context context;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view==null) {
            context = getActivity();

            util = new Util(context);

            getHoldingApiCall();

            view=inflater.inflate(R.layout.activity_holdings, container, false);

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {
        rvHoldings = view.findViewById(R.id.rvHoldings);
        etSearch = view.findViewById(R.id.etSearch);
        btn_transaction = view.findViewById(R.id.btn_transaction);

    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        btn_transaction.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKey = etSearch.getText().toString();
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

    @Override
    public void initializations() {
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.llMenu.setVisibility(View.GONE);

        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);

        //  BOBActivity.mTabHost.setVisibility(View.GONE);
        BOBActivity.title.setText("Holdings");

      //  util = new Util(context);

    }


    private void getHoldingApiCall()
    {
        util.showProgressDialog(context, true);
        apiInterface = BOBApp.getApi(context, Constants.ACTION_CLIENT_HOLDING);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
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
        ClientHoldingRequest.createClientHoldingRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);

        apiInterface.getHoldingResponse(ClientHoldingRequest.getClientHoldingRequestObject()).enqueue(new Callback<ArrayList<ClientHoldingObject>>() {
            @Override
            public void onResponse(Call<ArrayList<ClientHoldingObject>> call, Response<ArrayList<ClientHoldingObject>> response) {

                util.showProgressDialog(context, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    for (ClientHoldingObject item : response.body()) {
                        if (!item.getSource().equalsIgnoreCase("Direct Equity")) {
                            holdingArrayList.add(item);
                        }
                    }

                    setAdapter();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ClientHoldingObject>> call, Throwable t) {

                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setAdapter() {
        adapter = new HoldingListAdapter(context, holdingArrayList) {

            @Override
            public void getDetail(Fragment fragment) {

                replaceFragment(fragment);
            }
        };
        rvHoldings.setAdapter(adapter);
    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }


    @Override
    public void setIcon(Util util) {
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.menu) {
            getActivity().onBackPressed();
        } else if (view.getId() == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (view.getId() == R.id.btn_transaction) {
            Bundle args = new Bundle();

            args.putString("WhichActivity", "TransactionActivity");

            Fragment fragment = new TransactionActivity();

            fragment.setArguments(args);

            replaceFragment(fragment);
        }

    }

    private void filter(String text) {

        ArrayList<ClientHoldingObject> filteredList = new ArrayList<>();

        for (ClientHoldingObject item : holdingArrayList) {

            if (item.getIssuer() != null) {

                if (item.getIssuer().toLowerCase().startsWith(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }

        adapter.updateList(filteredList);
    }


}
