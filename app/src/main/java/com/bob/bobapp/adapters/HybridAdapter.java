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
import com.bob.bobapp.api.response_object.lstRecommandationEquity;
import com.bob.bobapp.api.response_object.lstRecommandationHybrid;
import com.bob.bobapp.utility.Util;

import java.util.ArrayList;

public abstract class HybridAdapter extends RecyclerView.Adapter<HybridAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<lstRecommandationHybrid> lstRecommandationHybridArrayList;
    private Util util;

    public HybridAdapter(Context context, ArrayList<lstRecommandationHybrid> lstRecommandationHybridArrayList) {
        this.context = context;
        this.lstRecommandationHybridArrayList = lstRecommandationHybridArrayList;
        util = new Util(context);
    }

    public void updateList(ArrayList<lstRecommandationHybrid> list) {
        lstRecommandationHybridArrayList = list;
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
        if(lstRecommandationHybridArrayList.get(position).getMorningStarRating()!=null) {
            if (lstRecommandationHybridArrayList.get(position).getMorningStarRating().equalsIgnoreCase("0")) {
                holder.linearRating.setVisibility(View.GONE);
            } else {
                holder.linearRating.setVisibility(View.VISIBLE);
                holder.txtRating.setText(lstRecommandationHybridArrayList.get(position).getMorningStarRating());
            }
        }
        holder.txtFundName.setText(lstRecommandationHybridArrayList.get(position).getFundName());
        holder.txtClassification.setText(lstRecommandationHybridArrayList.get(position).getClassification());
        if (lstRecommandationHybridArrayList.get(position).getReturnIn3Month().equalsIgnoreCase("0.0")) {
            holder.txtThreeMonth.setText("-");
        } else {
            double threeMonth = Double.parseDouble(lstRecommandationHybridArrayList.get(position).getReturnIn3Month());

            threeMonth = util.truncateDecimal(threeMonth).doubleValue();


            holder.txtThreeMonth.setText(""+threeMonth);
        }

        if (lstRecommandationHybridArrayList.get(position).getReturnIn1Year().equalsIgnoreCase("0.0")) {
            holder.txtOneYear.setText("-");
        } else {
            double oneYear = Double.parseDouble(lstRecommandationHybridArrayList.get(position).getReturnIn1Year());

            oneYear = util.truncateDecimal(oneYear).doubleValue();

            holder.txtOneYear.setText(""+oneYear);
        }

        if (lstRecommandationHybridArrayList.get(position).getReturnIn6Month().equalsIgnoreCase("0.0")) {
            holder.txtSixMonth.setText("-");
        } else {
            double sixMonth = Double.parseDouble(lstRecommandationHybridArrayList.get(position).getReturnIn6Month());

            sixMonth = util.truncateDecimal(sixMonth).doubleValue();


            holder.txtSixMonth.setText(""+sixMonth);
        }

        if (lstRecommandationHybridArrayList.get(position).getReturnIn3Year().equalsIgnoreCase("0.0")) {
            holder.txtThreeYear.setText("-");
        }
        else {
            double threeYear = Double.parseDouble(lstRecommandationHybridArrayList.get(position).getReturnIn3Year());

            threeYear = util.truncateDecimal(threeYear).doubleValue();

            holder.txtThreeYear.setText(""+threeYear);
        }
        if (lstRecommandationHybridArrayList.get(position).getReturnIn5Year() != null) {
            if (lstRecommandationHybridArrayList.get(position).getReturnIn5Year().equalsIgnoreCase("0.0")) {
                holder.txtFiveYear.setText("-");
            } else {
                double fiveYear = Double.parseDouble(lstRecommandationHybridArrayList.get(position).getReturnIn5Year());

                fiveYear = util.truncateDecimal(fiveYear).doubleValue();

                holder.txtFiveYear.setText(""+fiveYear);
            }
        }
    }

    @Override
    public int getItemCount() {
        return lstRecommandationHybridArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtFundName, txtSixMonth,txtRating, txtThreeMonth, txtOneYear,txtBuy,txtSip,txtClassification,txtThreeYear,txtFiveYear;
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
            txtClassification = itemView.findViewById(R.id.txtClassification);
            txtSip = itemView.findViewById(R.id.txtSip);
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

                args.putString("commanScriptCode", lstRecommandationHybridArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", lstRecommandationHybridArrayList.get(getAdapterPosition()).getFundName());
                args.putString("status", "3");
                Fragment fragment = new BuyActivity();
                fragment.setArguments(args);
                getDetail(fragment);
            }
            if (view.getId() == R.id.txtFundName) {
                Bundle args = new Bundle();
                args.putString("factsheetStatus","2");
                args.putString("commanScriptCode", lstRecommandationHybridArrayList.get(getAdapterPosition()).getSchemeCode());

                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            } if (view.getId() == R.id.imgFactsheet) {
                Bundle args = new Bundle();
                args.putString("factsheetStatus","2");
                args.putString("commanScriptCode", lstRecommandationHybridArrayList.get(getAdapterPosition()).getSchemeCode());

                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }

            if (view.getId() == R.id.txtSip)
            {
                Bundle args = new Bundle();
                args.putString("commanScriptCode", lstRecommandationHybridArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", lstRecommandationHybridArrayList.get(getAdapterPosition()).getFundName());
                args.putString("status", "3");

                Fragment fragment = new SipActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }
        }
    }

    public abstract void getDetail(Fragment fragment);
}
