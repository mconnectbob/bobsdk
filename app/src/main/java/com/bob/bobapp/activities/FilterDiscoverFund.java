package com.bob.bobapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.DiscoverFundListAdapter;
import com.bob.bobapp.adapters.FilterDiscoverFundAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.LstRecommandationDebt;
import com.bob.bobapp.api.response_object.SchemeResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterDiscoverFund extends BaseFragment {
    private Util util;
    private Context context;
    private RecyclerView recyclerFilterData;
    private ArrayList<SchemeResponse> schemeResponseArrayList = new ArrayList<>();
    private ArrayList<SchemeResponse> duplicateSchemeResponseArrayList = new ArrayList<>();
    private FilterDiscoverFundAdapter filterDiscoverFundAdapter;
    private EditText etSearch;
    private String searchKey = "";
    private APIInterface apiInterface;
    private TextView txtDisclaimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_filter_discover_funds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {
        recyclerFilterData = view.findViewById(R.id.recyclerFilterData);
        etSearch = view.findViewById(R.id.etSearch);
        txtDisclaimer = view.findViewById(R.id.txtDisclaimer);


    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);

        txtDisclaimer.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKey = etSearch.getText().toString();

                if (searchKey.length() > 2) {
                    callSubmitAPI(searchKey);
                }

                if (searchKey.length() == 0) {
                    setAdapter(schemeResponseArrayList);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchKey.length() == 0) {
                    setAdapter(schemeResponseArrayList);
                }
            }
        });

    }

    @Override
    public void initializations() {
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.imgInfo.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            schemeResponseArrayList = (ArrayList<SchemeResponse>) getArguments().getSerializable("schemeResponseArrayList");
        }
        apiInterface = BOBApp.getApi(getContext(), Constants.ASSEST_TYPE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Baroda Select Schemes");
        util = new Util(context);
        setAdapter(schemeResponseArrayList);
    }

    // replace fragment
    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    @Override
    public void setIcon(Util util) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        }
        if (id == R.id.txtDisclaimer)
        {
            showDisclaimerDialog();
        }

    }

    // set adapter
    private void setAdapter(ArrayList<SchemeResponse> schemeResponseArrayList) {
        recyclerFilterData.setNestedScrollingEnabled(false);
        filterDiscoverFundAdapter = new FilterDiscoverFundAdapter(context, schemeResponseArrayList) {
            @Override
            public void getDetail(Fragment fragment) {
                replaceFragment(fragment);
            }
        };
        recyclerFilterData.setAdapter(filterDiscoverFundAdapter);
    }


    private void filter(String text) {

        ArrayList<SchemeResponse> filteredList = new ArrayList<>();

        for (SchemeResponse item : schemeResponseArrayList) {

            if (item.getSchemeName() != null) {

                if (item.getSchemeName().toLowerCase().startsWith(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }

        filterDiscoverFundAdapter.updateList(filteredList);
    }

    private void callSubmitAPI(String search) {
        util.showProgressDialog(getContext(), true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());
        requestBodyObject.setUserId("admin");
        requestBodyObject.setTransactionType("2");
        requestBodyObject.setSearchString(search);
        requestBodyObject.setAssetTypes("");
        requestBodyObject.setFundRiskRating("");
        requestBodyObject.setFundHouses("");
        requestBodyObject.setFundTypes("");
        requestBodyObject.setFundOptionsCommaSeparated("");
        requestBodyObject.setFundOptions("1");
        requestBodyObject.setFundRiskRating("1");
        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        RMDetailRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getSchemes(RMDetailRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<SchemeResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<SchemeResponse>> call, Response<ArrayList<SchemeResponse>> response) {
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(getContext(), false);

                if (response.isSuccessful()) {
                    duplicateSchemeResponseArrayList.clear();
                    duplicateSchemeResponseArrayList = response.body();

                    if (schemeResponseArrayList.size() > 0) {
                        setAdapter(duplicateSchemeResponseArrayList);
                    } else {
                        Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SchemeResponse>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // disclaimer popup
    private void showDisclaimerDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.webview_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        AppCompatButton btnMoveToCart = dialog.findViewById(R.id.btnMoveToCart);
        AppCompatButton btnAddMore = dialog.findViewById(R.id.btnAddMore);
        WebView webview = dialog.findViewById(R.id.webview);

        webview.setWebViewClient(new MyWebViewClient());

        String ENROLLMENT_URL = "https://barodawealth.com/MoneywarePortal/Portal/TnC/Disclaimer1.html";
        webview.loadUrl(ENROLLMENT_URL);

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
            }
        });


        dialog.show();

    }

    public final class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Intent i = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                i = new Intent(Intent.ACTION_VIEW, request.getUrl());
            }
            startActivity(i);
            return true;
        }
    }


}
