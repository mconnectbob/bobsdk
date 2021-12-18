package com.bob.bobapp.activities;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.response_object.LifeInsuranceResponse;
import com.bob.bobapp.fragments.AddTransactionFragment;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HoldingDetailActivity extends BaseFragment {

    private TextView name, amount, gain, gainPercent, xirr, cost, marketValue, folioNo, unit,txtTransact;

    private ClientHoldingObject model;

    private String holdingDetailName,holdingDetailAmount,holdingDetailGain,holdingDetailQuantity,
            holdingDetailValueOfCost,holdingDetailGainLossPercentage,holdingDetailFolio,commanScriptCode;

    Double finalGianPercent;

    private Context context;

    private Util util;
    private DecimalFormat formatter;
    private ImageView imgFactSheet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);
        formatter = new DecimalFormat("###,###,##0.00");

        return inflater.inflate(R.layout.holding_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getIds(View view) {

        name = view.findViewById(R.id.name);
        amount = view.findViewById(R.id.amount);
        gain = view.findViewById(R.id.gain);
        gainPercent = view.findViewById(R.id.gainPercent);
        xirr = view.findViewById(R.id.xirr);
        cost = view.findViewById(R.id.cost);
        marketValue = view.findViewById(R.id.marketValue);
        folioNo = view.findViewById(R.id.folioNo);
        unit = view.findViewById(R.id.unit);
        txtTransact = view.findViewById(R.id.txtTransact);
        imgFactSheet = view.findViewById(R.id.imgFactSheet);


    }

    @Override
    public void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);
        txtTransact.setOnClickListener(this);
        imgFactSheet.setOnClickListener(this);
        name.setOnClickListener(this);

    }

    @Override
    public void initializations() {
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Holdings Detail");

        if (getArguments() != null) {
            //  String response = getArguments().getString("item");
            commanScriptCode = getArguments().getString("commanScriptCode");
            holdingDetailName = getArguments().getString("holdingDetailName");
            holdingDetailAmount = getArguments().getString("holdingDetailAmount");
            holdingDetailGain = getArguments().getString("holdingDetailGain");
            holdingDetailValueOfCost = getArguments().getString("holdingDetailValueOfCost");
            holdingDetailGainLossPercentage = getArguments().getString("holdingDetailGainLossPercentage");
            holdingDetailFolio = getArguments().getString("holdingDetailFolio");
            holdingDetailQuantity = getArguments().getString("holdingDetailQuantity");


            Float netGain= Float.parseFloat(holdingDetailGain);
            Float valueOfcCosr= Float.parseFloat(holdingDetailValueOfCost);

          //  Float finalGianPercent=netGain/(valueOfcCosr*100);
            Float finalGianPercent=(netGain/valueOfcCosr)*100;

            DecimalFormat df = new DecimalFormat("#.##");
            String formatted = df.format(finalGianPercent);
            System.out.println("asfsf"+formatted);

            gainPercent.setText(formatted+"%");


        }

        setData();


//        amount.setText(model.getValueOfCost());
//        gain.setText(model.getNetGain());
//
//        if (model.getXirrAsset() != null) {
//            xirr.setText(model.getXirrAsset());
//        } else {
//            xirr.setText("0.0");
//        }
//        gainPercent.setText(new DecimalFormat("##.##").format(Double.valueOf(model.getGainLossPercentage())) + "%");
//
//        cost.setText(model.getCostofInvestment() + "");
//        marketValue.setText(model.getMarketValue() + "");
//        folioNo.setText(model.getFolioNumber() + "");
//        unit.setText(model.getCurrentUnits() + "");


    }

    // set data
    private void setData() {
        name.setText(holdingDetailName);
        amount.setText(formatter.format(Double.parseDouble(holdingDetailAmount)));
        gain.setText(formatter.format(Double.parseDouble(holdingDetailGain)));

     //   gainPercent.setText(""+finalGianPercent+"%");



        folioNo.setText(holdingDetailFolio);
        unit.setText(holdingDetailQuantity);
        marketValue.setText(formatter.format(Double.parseDouble(holdingDetailAmount)));
        cost.setText(holdingDetailValueOfCost);

        if (holdingDetailGainLossPercentage != null) {
            double netGainPercent = util.truncateDecimal(Double.parseDouble(holdingDetailGainLossPercentage)).doubleValue();
         xirr.setText(String.valueOf(netGainPercent));

        } else {
           xirr.setText("0.0");
        }
    }


    @Override
    public void setIcon(Util util) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.menu) {
            getActivity().onBackPressed();
        }
        else if(view.getId()==R.id.txtTransact)
        {
            BOBActivity.mTabHost.setCurrentTab(1);
           // replaceFragment(new AddTransactionFragment());
        }
        else if (view.getId() == R.id.imgBack) {
            getActivity().onBackPressed();
        }

        else if (view.getId() == R.id.imgFactSheet) {
            Bundle args = new Bundle();

            args.putString("commanScriptCode",commanScriptCode);
            args.putString("factsheetStatus","1");

            //  Fragment fragment = new HoldingDetailActivity();
            Fragment fragment = new FactSheetActivity();

            fragment.setArguments(args);

            replaceFragment(fragment);
        }
        else if (view.getId() == R.id.name) {
            Bundle args = new Bundle();
            args.putString("factsheetStatus","1");
            args.putString("commanScriptCode",commanScriptCode);

            //  Fragment fragment = new HoldingDetailActivity();
            Fragment fragment = new FactSheetActivity();

            fragment.setArguments(args);

            replaceFragment(fragment);
        }

    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

}
