package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvestmentSwitchAdapter extends RecyclerView.Adapter<InvestmentSwitchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList;
    private DecimalFormat formatter;
    private OnItemDeleteListener OnItemDeleteListener;

    public InvestmentSwitchAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList, OnItemDeleteListener OnItemDeleteListener) {
        this.context = context;
        this.investmentCartDetailsResponseArrayList = investmentCartDetailsResponseArrayList;
        this.OnItemDeleteListener = OnItemDeleteListener;
        ;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_switch_adapter, parent, false);
        return new InvestmentSwitchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        InvestmentCartDetailsResponse model = investmentCartDetailsResponseArrayList.get(position);

        if (investmentCartDetailsResponseArrayList.get(position).getTransactionType().equalsIgnoreCase("switch")) {
            holder.linearData.setVisibility(View.VISIBLE);
            holder.txtSchemeName.setText(investmentCartDetailsResponseArrayList.get(position).getFundName());
            holder.txtFolio.setText(investmentCartDetailsResponseArrayList.get(position).getFolioNo());
            holder.txtAmount.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit())));
            holder.txtNoOfUnitsSwitch.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit())));
            holder.txtFrequency.setText(investmentCartDetailsResponseArrayList.get(position).getFrequency());
            holder.txtDay.setText(investmentCartDetailsResponseArrayList.get(position).getSIPStartDate());
            holder.txtCurrentFundValue.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getCurrentFundValue())));
            holder.txtCurrentunits.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getCurrentUnits())));

            if (investmentCartDetailsResponseArrayList.get(position).getAmountOrUnit().equalsIgnoreCase("A") && investmentCartDetailsResponseArrayList.get(position).getAllorPartial().equalsIgnoreCase("P")) {
                holder.txtOrderBasis.setText("Partial Amount");

            }
            if (investmentCartDetailsResponseArrayList.get(position).getAmountOrUnit().equalsIgnoreCase("U") && investmentCartDetailsResponseArrayList.get(position).getAllorPartial().equalsIgnoreCase("P")) {
                holder.txtOrderBasis.setText("Partial Units");

            }
            if (investmentCartDetailsResponseArrayList.get(position).getAmountOrUnit().equalsIgnoreCase("U") && investmentCartDetailsResponseArrayList.get(position).getAllorPartial().equalsIgnoreCase("A")) {
                holder.txtOrderBasis.setText("Full Switch");
            }
            holder.txtSwitchFund.setText(investmentCartDetailsResponseArrayList.get(position).getTargetFundName());
        } else {
            holder.linearData.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDeleteListener.onSwitchItemDeleteListener(investmentCartDetailsResponseArrayList.get(position).getICDID(), position, investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
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


    }

    @Override
    public int getItemCount() {
        return investmentCartDetailsResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView txtSchemeName, txtAmount, txtFolio, txtFrequency, txtDay, txtCurrentFundValue,
                txtCurrentunits, txtOrderBasis, txtNoOfUnitsSwitch, txtSwitchFund;
        private CardView linearData;
        private ImageView imgDelete;
        private CheckBox chkSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtFolio = itemView.findViewById(R.id.txtFolio);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtCurrentFundValue = itemView.findViewById(R.id.txtCurrentFundValue);
            txtCurrentunits = itemView.findViewById(R.id.txtCurrentunits);
            txtOrderBasis = itemView.findViewById(R.id.txtOrderBasis);
            txtNoOfUnitsSwitch = itemView.findViewById(R.id.txtNoOfUnitsSwitch);
            txtSwitchFund = itemView.findViewById(R.id.txtSwitchFund);
            linearData = itemView.findViewById(R.id.linearData);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            chkSelect = itemView.findViewById(R.id.chkSelect);
        }
    }
}
