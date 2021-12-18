package com.bob.bobapp.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.activities.BuyActivity;
import com.bob.bobapp.activities.RedeemActivity;
import com.bob.bobapp.activities.STPActivity;
import com.bob.bobapp.activities.SWPActivity;
import com.bob.bobapp.activities.SipActivity;
import com.bob.bobapp.activities.SwitchActivity;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.dialog.DividendPopup;
import com.bob.bobapp.dialog.PopupDialog;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvestmentBuyAdapter extends RecyclerView.Adapter<InvestmentBuyAdapter.ViewHolder> implements onSortItemListener {
    private Context context;
    private OnItemDeleteListener OnItemDeleteListener;
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList;
    private DecimalFormat formatter;
    private String status = "";
    private int clickedPosition = 0;

    public InvestmentBuyAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList, OnItemDeleteListener OnItemDeleteListener) {
        this.context = context;
        this.investmentCartDetailsResponseArrayList = investmentCartDetailsResponseArrayList;
        this.OnItemDeleteListener = OnItemDeleteListener;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_cart_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InvestmentCartDetailsResponse model = investmentCartDetailsResponseArrayList.get(position);
        if (investmentCartDetailsResponseArrayList.get(position).getTransactionType().equalsIgnoreCase("buy")) {
            holder.linearData.setVisibility(View.VISIBLE);
            holder.txtSchemeName.setText(investmentCartDetailsResponseArrayList.get(position).getFundName());
            holder.txtAmount.setText(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
            holder.txtAmount.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit())));
            if (investmentCartDetailsResponseArrayList.get(position).getIsDividend().equalsIgnoreCase("true")) {
                holder.linearDividend.setVisibility(View.VISIBLE);
            } else {
                holder.linearDividend.setVisibility(View.GONE);
            }

        } else {
            holder.linearData.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDeleteListener.onItemDeleteListener(investmentCartDetailsResponseArrayList.get(position).getICDID(), position, investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
                investmentCartDetailsResponseArrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model.setSelected(!model.isSelected());
                } else {
                    model.setSelected(false);
                }
            }
        });


        if (investmentCartDetailsResponseArrayList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.txtDividend.setText("Reinvest");
        } else if (investmentCartDetailsResponseArrayList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.txtDividend.setText("Payout");
        }
        else {
            holder.txtDividend.setText("Select Dividend Option");
        }

        holder.linearDividend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = position;
                DividendPopup allPlanDialog = new DividendPopup(context, InvestmentBuyAdapter.this::onSortItemListener);
                allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                allPlanDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return investmentCartDetailsResponseArrayList.size();
    }

    @Override
    public void onSortItemListener(String dialogStatus) {
        investmentCartDetailsResponseArrayList.get(clickedPosition).setStatus(dialogStatus);
        if (dialogStatus.equalsIgnoreCase("1")) {
            investmentCartDetailsResponseArrayList.get(clickedPosition).setDividendOption("R");
        } else {
            investmentCartDetailsResponseArrayList.get(clickedPosition).setDividendOption("P");
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView txtSchemeName, txtAmount;
        private CardView linearData;
        private ImageView imgDelete;
        private CheckBox chkSelect;
        private LinearLayoutCompat linearDividend;
        private TextView txtDividend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            linearData = itemView.findViewById(R.id.linearData);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            chkSelect = itemView.findViewById(R.id.chkSelect);
            linearDividend = itemView.findViewById(R.id.linearDividend);
            txtDividend = itemView.findViewById(R.id.txtDividend);
        }
    }


}
