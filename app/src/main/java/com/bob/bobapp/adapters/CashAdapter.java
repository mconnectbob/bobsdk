package com.bob.bobapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.activities.BuyActivity;
import com.bob.bobapp.activities.FactSheetActivity;
import com.bob.bobapp.activities.SipActivity;
import com.bob.bobapp.api.response_object.lstRecommandationCash;
import com.bob.bobapp.utility.Util;


import java.util.ArrayList;

public abstract class CashAdapter extends RecyclerView.Adapter<CashAdapter.ViewHolder> {
    private Context context;
    private ArrayList<lstRecommandationCash> lstRecommandationCashArrayList;
    private Util util;
    public CashAdapter(Context context, ArrayList<lstRecommandationCash> lstRecommandationCashArrayList) {
        this.context = context;
        this.lstRecommandationCashArrayList = lstRecommandationCashArrayList;
        util = new Util(context);
    }

    public void updateList(ArrayList<lstRecommandationCash> list) {
        lstRecommandationCashArrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.discover_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtFundName.setText(lstRecommandationCashArrayList.get(position).getFundName());
        if(lstRecommandationCashArrayList.get(position).getMorningStarRating()!=null) {
            if (lstRecommandationCashArrayList.get(position).getMorningStarRating().equalsIgnoreCase("0")) {
                holder.linearRating.setVisibility(View.GONE);
            } else {
                holder.linearRating.setVisibility(View.VISIBLE);
                holder.txtRating.setText(lstRecommandationCashArrayList.get(position).getMorningStarRating());
            }
        }
        holder.txtClassification.setText(lstRecommandationCashArrayList.get(position).getClassification());
        if (lstRecommandationCashArrayList.get(position).getReturnIn3Month().equalsIgnoreCase("0.0")) {
            holder.txtThreeMonth.setText("-");
        } else {
            double threeMonth = Double.parseDouble(lstRecommandationCashArrayList.get(position).getReturnIn3Month());

            threeMonth = util.truncateDecimal(threeMonth).doubleValue();


            holder.txtThreeMonth.setText(""+threeMonth);
        }

        if (lstRecommandationCashArrayList.get(position).getReturnIn1Year().equalsIgnoreCase("0.0")) {
            holder.txtOneYear.setText("-");
        } else {
            double oneYear = Double.parseDouble(lstRecommandationCashArrayList.get(position).getReturnIn1Year());

            oneYear = util.truncateDecimal(oneYear).doubleValue();

            holder.txtOneYear.setText(""+oneYear);
        }

        if (lstRecommandationCashArrayList.get(position).getReturnIn6Month().equalsIgnoreCase("0.0")) {
            holder.txtSixMonth.setText("-");
        } else {
            double sixMonth = Double.parseDouble(lstRecommandationCashArrayList.get(position).getReturnIn6Month());

            sixMonth = util.truncateDecimal(sixMonth).doubleValue();


            holder.txtSixMonth.setText(""+sixMonth);
        }

        if (lstRecommandationCashArrayList.get(position).getReturnIn3Year().equalsIgnoreCase("0.0")) {
            holder.txtThreeYear.setText("-");
        }
        else {
            double threeYear = Double.parseDouble(lstRecommandationCashArrayList.get(position).getReturnIn3Year());

            threeYear = util.truncateDecimal(threeYear).doubleValue();

            holder.txtThreeYear.setText(""+threeYear);
        }
        if (lstRecommandationCashArrayList.get(position).getReturnIn5Year() != null) {
            if (lstRecommandationCashArrayList.get(position).getReturnIn5Year().equalsIgnoreCase("0.0")) {
                holder.txtFiveYear.setText("-");
            } else {
                double fiveYear = Double.parseDouble(lstRecommandationCashArrayList.get(position).getReturnIn5Year());

                fiveYear = util.truncateDecimal(fiveYear).doubleValue();

                holder.txtFiveYear.setText(""+fiveYear);
            }
        }


    }

    @Override
    public int getItemCount() {
        return lstRecommandationCashArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtFundName, txtSixMonth, txtThreeMonth, txtOneYear, txtBuy, txtSip,
                txtClassification, txtThreeYear, txtFiveYear,txtRating;
        private ImageView imgFactsheet;
        private LinearLayout linearRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFundName = itemView.findViewById(R.id.txtFundName);
            txtSixMonth = itemView.findViewById(R.id.txtSixMonth);
            txtThreeMonth = itemView.findViewById(R.id.txtThreeMonth);
            txtOneYear = itemView.findViewById(R.id.txtOneYear);
            txtBuy = itemView.findViewById(R.id.txtBuy);
            imgFactsheet = itemView.findViewById(R.id.imgFactsheet);
            txtSip = itemView.findViewById(R.id.txtSip);
            txtClassification = itemView.findViewById(R.id.txtClassification);
            txtThreeYear = itemView.findViewById(R.id.txtThreeYear);
            txtFiveYear = itemView.findViewById(R.id.txtFiveYear);
            txtRating = itemView.findViewById(R.id.txtRating);
            linearRating = itemView.findViewById(R.id.linearRating);

            txtBuy.setOnClickListener(this);
            imgFactsheet.setOnClickListener(this);
            txtSip.setOnClickListener(this);
            txtFundName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.txtBuy) {

                Bundle args = new Bundle();

                args.putString("commanScriptCode", lstRecommandationCashArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", lstRecommandationCashArrayList.get(getAdapterPosition()).getFundName());
                args.putString("status", "3");
                Fragment fragment = new BuyActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }
            if (view.getId() == R.id.txtFundName) {
                Bundle args = new Bundle();
                args.putString("factsheetStatus","2");
                args.putString("commanScriptCode", lstRecommandationCashArrayList.get(getAdapterPosition()).getSchemeCode());

                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            } if (view.getId() == R.id.imgFactsheet) {
                Bundle args = new Bundle();
                args.putString("factsheetStatus","2");
                args.putString("commanScriptCode", lstRecommandationCashArrayList.get(getAdapterPosition()).getSchemeCode());

                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }
            if (view.getId() == R.id.txtSip) {

                Bundle args = new Bundle();

                args.putString("commanScriptCode", lstRecommandationCashArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", lstRecommandationCashArrayList.get(getAdapterPosition()).getFundName());
                args.putString("status", "3");
                Fragment fragment = new SipActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }

        }
    }

    public abstract void getDetail(Fragment fragment);
}
