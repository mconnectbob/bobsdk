package com.bob.bobapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.ConfirmOrderAdapter;
import com.bob.bobapp.adapters.ConfirmOrderStatusAdapter;
import com.bob.bobapp.api.response_object.BuyConfirmResponse;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.fragments.AddTransactionFragment;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.fragments.DashboardFragment;
import com.bob.bobapp.utility.Util;

import java.util.ArrayList;

public class ConfirmOrderStatusActivity extends BaseFragment {
    private Context context;
    private Util util;
    private RecyclerView recyclerConfirmOrder;
    private ConfirmOrderStatusAdapter confirmOrderStatusAdapter;
    private ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList = new ArrayList<>();
    ArrayList<BuyConfirmResponse> buyConfirmResponseArrayList = new ArrayList<>();
    private String clientName = "", status = "";
    private AppCompatTextView txtOverView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_confirm_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void getIds(View view) {
        recyclerConfirmOrder = view.findViewById(R.id.recyclerConfirmOrder);
        txtOverView = view.findViewById(R.id.txtOverView);
    }

    @Override
    protected void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        txtOverView.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Order Status");

        if (getArguments() != null) {
            requestBodyObjectArrayList = (ArrayList<InvestmentCartDetailsResponse>) getArguments().getSerializable("schemeResponseArrayList");
            buyConfirmResponseArrayList = (ArrayList<BuyConfirmResponse>) getArguments().getSerializable("buyConfirmResponseArrayList");
            clientName = getArguments().getString("clientName");
            status = getArguments().getString("status");

        }

        setAdapter();

    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgBack) {

            if(BOBActivity.mTabHost.getCurrentTab() == 0) {

                ((BaseContainerFragment) getParentFragment()).clearBackStackExceptFragment(new DashboardFragment());

            }else{

                if(BOBActivity.mTabHost.getCurrentTab() == 1) {

                    ((BaseContainerFragment) getParentFragment()).clearBackStackExceptFragment(new AddTransactionFragment());

                }else if(BOBActivity.mTabHost.getCurrentTab() == 2) {

                    ((BaseContainerFragment) getParentFragment()).clearBackStackExceptFragment(new PortfolioAnalytics());
                }

                BOBActivity.mTabHost.setCurrentTab(0);
            }
        }

        if (id == R.id.txtOverView)
        {
            BOBActivity.mTabHost.setCurrentTab(1);
        }

    }


    //  adapter
    private void setAdapter() {
        confirmOrderStatusAdapter = new ConfirmOrderStatusAdapter(getContext(), requestBodyObjectArrayList, buyConfirmResponseArrayList, clientName, status);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerConfirmOrder.setLayoutManager(linearLayoutManager);
        recyclerConfirmOrder.setAdapter(confirmOrderStatusAdapter);
    }
}
