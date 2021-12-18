package com.bob.bobapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.activities.BuySIPRedeemSwitchActivity;
import com.bob.bobapp.activities.FactSheetActivity;
import com.bob.bobapp.activities.HoldingDetailActivity;
import com.bob.bobapp.activities.InsuranceDetailActivity;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class HoldingListAdapter extends RecyclerView.Adapter<HoldingListAdapter.ViewHolder> {
    private Context context;
    private List<ClientHoldingObject> arrayList;
    private DecimalFormat formatter;
    private Util util;

    public HoldingListAdapter(Context context, List<ClientHoldingObject> holdingArrayList) {
        this.context = context;
        this.arrayList = holdingArrayList;
        formatter = new DecimalFormat("###,###,##0.00");
        util = new Util(context);
    }

    public void updateList(List<ClientHoldingObject> list) {
        arrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.holdins_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ClientHoldingObject model = arrayList.get(position);
        holder.name.setText(model.getSchemeName());

        holder.amount.setText(formatter.format(Double.parseDouble(model.getMarketValue())));
        holder.gain.setText(formatter.format(Double.parseDouble(model.getNetGain())));

        if (model.getGainLossPercentage() != null) {
            double netGainPercent = util.truncateDecimal(Double.parseDouble(model.getGainLossPercentage())).doubleValue();
            holder.xirr.setText(String.valueOf(netGainPercent));
            String xiirrValue = String.valueOf(netGainPercent);
            if (xiirrValue.contains("-")) {
                holder.xirr.setTextColor(Color.parseColor("#ff3100"));
            } else {
                holder.xirr.setTextColor(Color.parseColor("#1F3C66"));

            }

            // holder.xirr.setText(model.getGainLossPercentage());
        } else {
            holder.xirr.setText("0.0");
        }

        Double netGain = Double.parseDouble(model.getNetGain());
        Double valueOfcCosr = Double.parseDouble(model.getValueOfCost());
        //   Double finalGianPercent=netGain/(valueOfcCosr*100);
        Double finalGianPercent = (netGain / valueOfcCosr) * 100;

        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(finalGianPercent);
        System.out.println("asfsf" + formatted);

        holder.gainPercent.setText(formatted + " " + "%");


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    TextView transact;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, amount, gain, gainPercent, xirr;
        private ImageView imgFactSheet, imgArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            gain = itemView.findViewById(R.id.gain);
            gainPercent = itemView.findViewById(R.id.gainPercent);
            xirr = itemView.findViewById(R.id.xirr);
            imgFactSheet = itemView.findViewById(R.id.imgFactSheet);
            imgArrow = itemView.findViewById(R.id.imgArrow);

            imgArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();

                    args.putString("commanScriptCode", arrayList.get(getAdapterPosition()).getCommonScripCode());
                    args.putString("holdingDetailName", arrayList.get(getAdapterPosition()).getSchemeName());
                    args.putString("holdingDetailAmount", arrayList.get(getAdapterPosition()).getMarketValue());
                    args.putString("holdingDetailGain", arrayList.get(getAdapterPosition()).getNetGain());
                    args.putString("holdingDetailValueOfCost", arrayList.get(getAdapterPosition()).getValueOfCost());
                    args.putString("holdingDetailGainLossPercentage", arrayList.get(getAdapterPosition()).getGainLossPercentage());
                    args.putString("holdingDetailFolio", arrayList.get(getAdapterPosition()).getFolio());
                    args.putString("holdingDetailQuantity", arrayList.get(getAdapterPosition()).getQuantity());


                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new HoldingDetailActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                }
            });

            imgFactSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle args = new Bundle();

                    args.putString("commanScriptCode", arrayList.get(getAdapterPosition()).getCommonScripCode());
                    args.putString("factsheetStatus", "1");

                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new FactSheetActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle args = new Bundle();

                    args.putString("commanScriptCode", arrayList.get(getAdapterPosition()).getCommonScripCode());
                    args.putString("factsheetStatus", "1");

                    //  Fragment fragment = new HoldingDetailActivity();
                    Fragment fragment = new FactSheetActivity();

                    fragment.setArguments(args);

                    getDetail(fragment);
                }
            });
        }

    }

    public abstract void getDetail(Fragment fragment);
}
