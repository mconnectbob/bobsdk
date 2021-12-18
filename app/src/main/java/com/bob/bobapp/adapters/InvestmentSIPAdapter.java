package com.bob.bobapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.bob.bobapp.dialog.DividendPopup;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvestmentSIPAdapter extends RecyclerView.Adapter<InvestmentSIPAdapter.ViewHolder> implements onSortItemListener
{
    private Context context;
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList;
    private OnItemDeleteListener OnItemDeleteListener;
    private DecimalFormat formatter;
    private int clickedPosition = 0;

    public InvestmentSIPAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList, OnItemDeleteListener OnItemDeleteListener) {
        this.context = context;
        this.investmentCartDetailsResponseArrayList = investmentCartDetailsResponseArrayList;
        this.OnItemDeleteListener = OnItemDeleteListener;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_sip_adapter, parent, false);
        return new InvestmentSIPAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvestmentCartDetailsResponse model = investmentCartDetailsResponseArrayList.get(position);
        if (investmentCartDetailsResponseArrayList.get(position).getTransactionType().equalsIgnoreCase("sip")) {
            holder.linearData.setVisibility(View.VISIBLE);
            String date = investmentCartDetailsResponseArrayList.get(position).getSIPStartDate();

            System.out.println("date:"+ date);
            String year = "";
            String month = "";
            String day = "";

            if(date!=null) {
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
            }

            holder.txtSchemeName.setText(investmentCartDetailsResponseArrayList.get(position).getFundName());
            holder.txtAmount.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit())));
            holder.txtInstallment.setText(investmentCartDetailsResponseArrayList.get(position).getPeriod());
            holder.txtFrequency.setText(investmentCartDetailsResponseArrayList.get(position).getFrequency());
            holder.txtDay.setText(day);

            if (investmentCartDetailsResponseArrayList.get(position).getIsDividend().equalsIgnoreCase("true")) {
                holder.linearDividend.setVisibility(View.VISIBLE);
            } else {
                holder.linearDividend.setVisibility(View.GONE);
            }


            if (investmentCartDetailsResponseArrayList.get(position).getStatus().equalsIgnoreCase("1")) {
                holder.txtDividend.setText("Reinvest");
            } else if (investmentCartDetailsResponseArrayList.get(position).getStatus().equalsIgnoreCase("2")) {
                holder.txtDividend.setText("Payout");
            }
            else {
                holder.txtDividend.setText("Select Dividend Option");
            }


        } else {
            holder.linearData.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDeleteListener.onSipItemDeleteListener(investmentCartDetailsResponseArrayList.get(position).getICDID(), position, investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
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

        holder.linearDividend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = position;
                DividendPopup allPlanDialog = new DividendPopup(context,InvestmentSIPAdapter.this::onSortItemListener);
                allPlanDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                allPlanDialog.show();

            }
        });


    }

    @Override
    public void onSortItemListener(String dialogStatus) {
        investmentCartDetailsResponseArrayList.get(clickedPosition).setStatus(dialogStatus);
        if (dialogStatus.equalsIgnoreCase("1"))
        {
            investmentCartDetailsResponseArrayList.get(clickedPosition).setDividendOption("R");
        } else {
            investmentCartDetailsResponseArrayList.get(clickedPosition).setDividendOption("P");
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return investmentCartDetailsResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView txtSchemeName, txtAmount, txtInstallment, txtFrequency, txtDay;
        private CardView linearData;
        private ImageView imgDelete;
        private CheckBox chkSelect;
        private LinearLayoutCompat linearDividend;
        private TextView txtDividend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtInstallment = itemView.findViewById(R.id.txtInstallment);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtDay = itemView.findViewById(R.id.txtDay);
            linearData = itemView.findViewById(R.id.linearData);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            chkSelect = itemView.findViewById(R.id.chkSelect);
            linearDividend = itemView.findViewById(R.id.linearDividend);
            txtDividend = itemView.findViewById(R.id.txtDividend);

        }

    }
}
