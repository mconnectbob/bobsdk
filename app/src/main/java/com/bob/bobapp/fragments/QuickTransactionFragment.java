
package com.bob.bobapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.activities.FactSheetActivity;
import com.bob.bobapp.activities.FilterDiscoverFund;
import com.bob.bobapp.adapters.AssetAdapter;
import com.bob.bobapp.adapters.FolioAdapter;
import com.bob.bobapp.adapters.FundHouseAdapter;
import com.bob.bobapp.adapters.FundTypeAdapter;
import com.bob.bobapp.adapters.SpineerAssetClassAdapter;
import com.bob.bobapp.adapters.SpineerFundOptionAdapter;
import com.bob.bobapp.adapters.SpinnerFundHouseAdapter;
import com.bob.bobapp.adapters.SpinnerFundTypeAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.request_object.AssetTypesRequest;
import com.bob.bobapp.api.request_object.AssetTypesRequestBody;
import com.bob.bobapp.api.request_object.FundTypesRequest;
import com.bob.bobapp.api.request_object.FundTypesRequestBody;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AssetTypesResponse;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.FundTypesResponse;
import com.bob.bobapp.api.response_object.IssuersResponse;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.api.response_object.SchemeResponse;
import com.bob.bobapp.listener.OnFilterItemListener;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuickTransactionFragment extends BaseFragment implements OnItemDeleteListener, OnFilterItemListener {
    private TextView txtApply, txtClear, tv_go;
    private EditText etSearch;
    private RadioButton radioGrowth, radioPayout, radioReinvest;
    private RecyclerView recyclerAsset, recyclerFundType, recyclerFundHouse;
    private AssetAdapter assetAdapter;
    private FundTypeAdapter fundTypeAdapter;
    private FundHouseAdapter fundHouseAdapter;
    private APIInterface apiInterface;
    private Util util;
    private ArrayList<AssetTypesResponse> assetTypesResponseArrayList = new ArrayList<>();
    private ArrayList<FundTypesResponse> fundTypesResponseArrayList = new ArrayList<>();
    private ArrayList<IssuersResponse> fundHouseResponseArrayList = new ArrayList<>();
    private String AssetTypes = "", FundTypes = "", FundHouses = "", fundOption = "", searchKey = "";
    private ArrayList<SchemeResponse> schemeResponseArrayList = new ArrayList<>();
    private AppCompatSpinner spineerFundHouse, spineerAssetClass, spineerFundType, spineerFundOption;
    private SpinnerFundHouseAdapter spinnerFundHouseAdapter;
    private SpineerAssetClassAdapter spineerAssetClassAdapter;
    private SpinnerFundTypeAdapter spinnerFundTypeAdapter;
    private SpineerFundOptionAdapter spineerFundOptionAdapter;
    private ArrayList<String> fundOptionArrayList = new ArrayList<>();

    public QuickTransactionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void getIds(View view) {
        fundOptionArrayList.clear();
        fundOptionArrayList.add("Select");
        fundOptionArrayList.add("Growth");
        fundOptionArrayList.add("Dividend Payout");
        fundOptionArrayList.add("Dividend Reinvest");

        recyclerAsset = view.findViewById(R.id.recyclerAsset);
        recyclerFundType = view.findViewById(R.id.recyclerFundType);
        recyclerFundHouse = view.findViewById(R.id.recyclerFundHouse);
        txtApply = view.findViewById(R.id.txtApply);
        radioGrowth = view.findViewById(R.id.radioGrowth);
        radioPayout = view.findViewById(R.id.radioPayout);
        radioReinvest = view.findViewById(R.id.radioReinvest);
        txtClear = view.findViewById(R.id.txtClear);
        etSearch = view.findViewById(R.id.etSearch);
        tv_go = view.findViewById(R.id.tv_go);

        spineerFundHouse = view.findViewById(R.id.spineerFundHouse);
        spineerAssetClass = view.findViewById(R.id.spineerAssetClass);
        spineerFundType = view.findViewById(R.id.spineerFundType);
        spineerFundOption = view.findViewById(R.id.spineerFundOption);

        setSpinnerFundOptionAdapter();
    }

    @Override
    protected void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);
        txtApply.setOnClickListener(this);
        txtClear.setOnClickListener(this);
        tv_go.setOnClickListener(this);

        radioGrowth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fundOption = "G";
                    radioGrowth.setChecked(true);
                    radioPayout.setChecked(false);
                    radioReinvest.setChecked(false);
                }
            }
        });

        radioPayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fundOption = "P";
                    radioGrowth.setChecked(false);
                    radioPayout.setChecked(true);
                    radioReinvest.setChecked(false);
                }
            }
        });

        radioReinvest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fundOption = "R";
                    radioGrowth.setChecked(false);
                    radioPayout.setChecked(false);
                    radioReinvest.setChecked(true);
                }
            }
        });

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

            }
        });


    }

    @Override
    protected void initializations() {
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);

        BOBActivity.title.setText("Search All Mutual Funds");
        apiInterface = BOBApp.getApi(getContext(), Constants.ASSEST_TYPE);
        util = new Util(getContext());
        AssetApiCall();
    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.imgBack) {

            getActivity().onBackPressed();
        }
        if (id == R.id.tv_go) {
            if (searchKey.length() > 2) {
                AssetTypes = "";
                fundOption = "";
                FundTypes = "";
                FundHouses = "";
                callSubmitAPI(searchKey);
            } else {
                Toast.makeText(getContext(), "please enter minimum 3 character", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.txtApply) {
            if (FundHouses.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "please select fund house", Toast.LENGTH_SHORT).show();
            } else {
                callSubmitAPI("");
            }

        }

        if (id == R.id.txtClear) {
            assetTypesResponseArrayList.clear();
            fundTypesResponseArrayList.clear();
            fundHouseResponseArrayList.clear();
            fundOptionArrayList.clear();
            AssetTypes = "";
            fundOption = "";
            FundTypes = "";
            FundHouses = "";
            radioGrowth.setChecked(false);
            radioPayout.setChecked(false);
            radioGrowth.setChecked(false);

            fundOptionArrayList.add("Select");
            fundOptionArrayList.add("Growth");
            fundOptionArrayList.add("Dividend Payout");
            fundOptionArrayList.add("Dividend Reinvest");

            setSpinnerFundOptionAdapter();
            AssetApiCall();
        }
    }

    // fund option
    private void setSpinnerFundOptionAdapter() {
        spineerFundOptionAdapter = new SpineerFundOptionAdapter(getContext(), fundOptionArrayList);
        spineerFundOption.setAdapter(spineerFundOptionAdapter);

        spineerFundOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String type = fundOptionArrayList.get(position);

                if (type.equalsIgnoreCase("Select")) {
                    fundOption = "";
                }
                if (type.equalsIgnoreCase("Growth")) {
                    fundOption = "G";
                }

                if (type.equalsIgnoreCase("Dividend Payout")) {
                    fundOption = "P";
                }

                if (type.equalsIgnoreCase("Dividend Reinvest")) {
                    fundOption = "R";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // fund house
    private void setSpinnerFundHouseAdapter() {
        IssuersResponse issuersResponse = new IssuersResponse();
        issuersResponse.setAMCName("Select");
        issuersResponse.setAMCCode("");
        issuersResponse.setClientCode("");
        fundHouseResponseArrayList.add(0, issuersResponse);

        spinnerFundHouseAdapter = new SpinnerFundHouseAdapter(getContext(), fundHouseResponseArrayList);
        spineerFundHouse.setAdapter(spinnerFundHouseAdapter);

        spineerFundHouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                FundHouses = fundHouseResponseArrayList.get(position).getAMCCode();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // Assest  adapter
    private void setAssetClassAdapter() {
        AssetTypesResponse assetTypesResponse = new AssetTypesResponse();
        assetTypesResponse.setAssetClassName("Select");
        assetTypesResponseArrayList.add(0, assetTypesResponse);

        spineerAssetClassAdapter = new SpineerAssetClassAdapter(getContext(), assetTypesResponseArrayList);
        spineerAssetClass.setAdapter(spineerAssetClassAdapter);

        spineerAssetClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String assetType = assetTypesResponseArrayList.get(position).getAssetClassName();

                if (assetType.equalsIgnoreCase("Select")) {
                    AssetTypes = "";
                } else {
                    AssetTypes = assetTypesResponseArrayList.get(position).getAssetClassName();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // fund type  adapter
    private void setSpineerFundTypeAdapter() {

        FundTypesResponse fundTypesResponse = new FundTypesResponse();
        fundTypesResponse.setFundTypeName("Select");
        fundTypesResponse.setFundTypeCode("");
        fundTypesResponse.setClientCode("");
        fundTypesResponseArrayList.add(0, fundTypesResponse);

        spinnerFundTypeAdapter = new SpinnerFundTypeAdapter(getContext(), fundTypesResponseArrayList);
        spineerFundType.setAdapter(spinnerFundTypeAdapter);

        spineerFundType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                FundTypes = fundTypesResponseArrayList.get(position).getFundTypeCode();

                //    Toast.makeText(getContext(), FundTypes, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    //  adapter
    private void setAssetAdapter() {
        assetAdapter = new AssetAdapter(getContext(), assetTypesResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAsset.setLayoutManager(linearLayoutManager);
        recyclerAsset.setAdapter(assetAdapter);
    }

    // fund type
    private void setFundTypeAdapter() {
        fundTypeAdapter = new FundTypeAdapter(getContext(), fundTypesResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFundType.setLayoutManager(linearLayoutManager);
        recyclerFundType.setAdapter(fundTypeAdapter);
    }

    // fund house adapter
    private void setFundHouseAdapter() {
        fundHouseAdapter = new FundHouseAdapter(getContext(), fundHouseResponseArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFundHouse.setLayoutManager(linearLayoutManager);
        recyclerFundHouse.setAdapter(fundHouseAdapter);
    }


    // api calling
    private void AssetApiCall() {
        util.showProgressDialog(getContext(), true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        AssetTypesRequestBody requestBody = new AssetTypesRequestBody();
        requestBody.setClientCode(authenticateResponse.getUserCode());
        requestBody.setUserId(authenticateResponse.getUserID());
        requestBody.setLastBusinessDate(authenticateResponse.getBusinessDate());
        requestBody.setAllocationType("2");
        requestBody.setCurrencyCode("1.0");
        requestBody.setAccountLevel("0");


        AssetTypesRequest model = new AssetTypesRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.AssetTypes(model).enqueue(new Callback<ArrayList<AssetTypesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AssetTypesResponse>> call, Response<ArrayList<AssetTypesResponse>> response) {
                //   util.showProgressDialog(getContext(), false);
                assetTypesResponseArrayList.clear();
                if (response.isSuccessful()) {
                    assetTypesResponseArrayList = response.body();
                    // setAssetAdapter();
                    setAssetClassAdapter();
                    FundTypeApiCall();

                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AssetTypesResponse>> call, Throwable t) {
                util.showProgressDialog(getContext(), false);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // api calling
    private void FundTypeApiCall() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        FundTypesRequestBody requestBody = new FundTypesRequestBody();
        requestBody.setClientCode(authenticateResponse.getUserCode());


        FundTypesRequest model = new FundTypesRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.FundTypes(model).enqueue(new Callback<ArrayList<FundTypesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<FundTypesResponse>> call, Response<ArrayList<FundTypesResponse>> response) {
                //   util.showProgressDialog(getContext(), false);
                fundTypesResponseArrayList.clear();
                if (response.isSuccessful()) {
                    fundTypesResponseArrayList = response.body();
                    //  setFundTypeAdapter();
                    setSpineerFundTypeAdapter();
                    FundHousesApiCall();
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FundTypesResponse>> call, Throwable t) {
                util.showProgressDialog(getContext(), false);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // api calling
    private void FundHousesApiCall() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        FundTypesRequestBody requestBody = new FundTypesRequestBody();
        requestBody.setClientCode(authenticateResponse.getUserCode());


        FundTypesRequest model = new FundTypesRequest();
        model.setRequestBodyObject(requestBody);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.Issuers(model).enqueue(new Callback<ArrayList<IssuersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<IssuersResponse>> call, Response<ArrayList<IssuersResponse>> response) {
                util.showProgressDialog(getContext(), false);
                fundHouseResponseArrayList.clear();
                if (response.isSuccessful()) {
                    fundHouseResponseArrayList = response.body();


                    //   setFundHouseAdapter();
                    setSpinnerFundHouseAdapter();
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<IssuersResponse>> call, Throwable t) {
                util.showProgressDialog(getContext(), false);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // adaptet
    @Override
    public void onItemDeleteListener(String id, int position, String name) {
        AssetTypes = name;
        //  Toast.makeText(getContext(),AssetTypes,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onFundHpuseItemListener(int position, String name) {
        FundHouses = name;
    }

    @Override
    public void onFundTypeItemListener(int position, String name) {
        FundTypes = name;
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
        requestBodyObject.setAssetTypes(AssetTypes);
        requestBodyObject.setFundRiskRating("");
        requestBodyObject.setFundHouses(FundHouses);
        requestBodyObject.setFundTypes(FundTypes);
        requestBodyObject.setFundOptionsCommaSeparated(fundOption);
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
                    fundOptionArrayList.clear();
                    etSearch.setText("");
                    schemeResponseArrayList = response.body();
                    if (schemeResponseArrayList.size() > 0) {
                        Bundle args = new Bundle();

                        args.putSerializable("schemeResponseArrayList", schemeResponseArrayList);

                        Fragment fragment = new FilterDiscoverFund();

                        fragment.setArguments(args);

                        getDetail(fragment);
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


    // replace fragment
    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    public void getDetail(Fragment fragment) {
        replaceFragment(fragment);
    }
}
