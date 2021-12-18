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

public class InvestmentSWPAdapter extends RecyclerView.Adapter<InvestmentSWPAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList;
    private OnItemDeleteListener OnItemDeleteListener;
    private DecimalFormat formatter;

    public InvestmentSWPAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList, OnItemDeleteListener OnItemDeleteListener) {
        this.context = context;
        this.investmentCartDetailsResponseArrayList = investmentCartDetailsResponseArrayList;
        this.OnItemDeleteListener = OnItemDeleteListener;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_swp_adapter, parent, false);
        return new InvestmentSWPAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvestmentCartDetailsResponse model = investmentCartDetailsResponseArrayList.get(position);

        if (investmentCartDetailsResponseArrayList.get(position).getTransactionType().equalsIgnoreCase("swp")) {
            holder.linearData.setVisibility(View.VISIBLE);

            try {
                String date = investmentCartDetailsResponseArrayList.get(position).getSIPStartDate();

                String year = "";
                String month = "";
                String day = "";

                for (int j = 0; j < 4; j++) {
                    if (date.charAt(j) != '/') {
                        year = year + date.charAt(j);
                    } else {
                        break;
                    }
                }

                for (int k = 4; k < 6; k++) {
                    if (date.charAt(k) != '/') {
                        month = month + date.charAt(k);
                    } else {
                        break;
                    }
                }

                for (int l = 6; l < 8; l++) {
                    if (date.charAt(l) != '/') {
                        day = day + date.charAt(l);
                    } else {
                        break;
                    }
                }

                System.out.println("year" + year);
                System.out.println("month" + month);
                System.out.println("day" + date);

                String finalDate = day + "-" + month + "-" + year;
                holder.txtDay.setText(day);

            }catch (Exception exception)
            {
                exception.printStackTrace();
            }


            holder.txtFolio.setText(investmentCartDetailsResponseArrayList.get(position).getFolioNo());
            holder.txtSchemeName.setText(investmentCartDetailsResponseArrayList.get(position).getFundName());
            holder.txtAmount.setText(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
            holder.txtWithdrawAmount.setText(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
            holder.txtFrequency.setText(investmentCartDetailsResponseArrayList.get(position).getFrequency());
            holder.txtCurrentFundValue.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getCurrentFundValue())));
            holder.txtCurrentunits.setText(investmentCartDetailsResponseArrayList.get(position).getCurrentUnits());
            holder.txtInstallment.setText(investmentCartDetailsResponseArrayList.get(position).getPeriod());

            if (investmentCartDetailsResponseArrayList.get(position).getAmountOrUnit().equalsIgnoreCase("A")) {
                holder.txtWithdrawalType.setText("Amount");

            } else {
                holder.txtWithdrawalType.setText("Units");
            }

        } else {
            holder.linearData.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDeleteListener.onSwPItemDeleteListener(investmentCartDetailsResponseArrayList.get(position).getICDID(), position, investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
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
        private TextView txtDelete;
        private AppCompatTextView txtSchemeName, txtAmount, txtFrequency, txtDay, txtCurrentFundValue,
                txtCurrentunits, txtWithdrawalType, txtWithdrawAmount, txtInstallment, txtFolio;
        private CardView linearData;
        private ImageView imgDelete;
        private CheckBox chkSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtCurrentFundValue = itemView.findViewById(R.id.txtCurrentFundValue);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtCurrentunits = itemView.findViewById(R.id.txtCurrentunits);
            txtWithdrawalType = itemView.findViewById(R.id.txtWithdrawalType);
            txtWithdrawAmount = itemView.findViewById(R.id.txtWithdrawAmount);
            txtInstallment = itemView.findViewById(R.id.txtInstallment);
            linearData = itemView.findViewById(R.id.linearData);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            txtFolio = itemView.findViewById(R.id.txtFolio);
            chkSelect = itemView.findViewById(R.id.chkSelect);
        }
    }
}
