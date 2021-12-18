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
import com.bob.bobapp.api.response_object.LstRecommandationDebt;
import com.bob.bobapp.api.response_object.SchemeResponse;
import com.bob.bobapp.utility.Util;

import java.util.ArrayList;

public abstract class FilterDiscoverFundAdapter extends RecyclerView.Adapter<FilterDiscoverFundAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SchemeResponse> schemeResponseArrayList;
    private Util util;

    public FilterDiscoverFundAdapter(Context context, ArrayList<SchemeResponse> schemeResponseArrayList) {
        this.context = context;
        this.schemeResponseArrayList = schemeResponseArrayList;
        util = new Util(context);
    }

    public void updateList(ArrayList<SchemeResponse> list) {
        schemeResponseArrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.filter_discover_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        SchemeResponse schemeResponse=schemeResponseArrayList.get(position);
        holder.txtFundName.setText(schemeResponse.getSchemeName());
        if(schemeResponse.getMorningStarRating()!=null) {
            if (schemeResponse.getMorningStarRating().equalsIgnoreCase("0")) {
                holder.linearRating.setVisibility(View.GONE);
            } else {
                holder.linearRating.setVisibility(View.VISIBLE);
                holder.txtRating.setText(schemeResponse.getMorningStarRating());
            }
        }

        double sixMonth = Double.parseDouble(schemeResponse.getReturns6Month());

        sixMonth = util.truncateDecimal(sixMonth).doubleValue();

        holder.txtSixMonth.setText(""+sixMonth);


        double threeMonth = Double.parseDouble(schemeResponse.getReturns3Month());

        threeMonth = util.truncateDecimal(threeMonth).doubleValue();

        holder.txtThreeMonth.setText(""+threeMonth);


        double oneYear = Double.parseDouble(schemeResponse.getReturns1Year());

        oneYear = util.truncateDecimal(oneYear).doubleValue();

        holder.txtOneYear.setText(""+oneYear);


        double threeYear = Double.parseDouble(schemeResponse.getReturns3Year());

        threeYear = util.truncateDecimal(threeYear).doubleValue();

        holder.txtThreeYear.setText(""+threeYear);


        double fiveYear = Double.parseDouble(schemeResponse.getReturns5Year());

        fiveYear = util.truncateDecimal(fiveYear).doubleValue();

        holder.txtFiveYear.setText(""+fiveYear);


    }

    @Override
    public int getItemCount() {
        return schemeResponseArrayList.size();
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
        // click listener
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.txtBuy) {
                Bundle args = new Bundle();

                args.putString("commanScriptCode", schemeResponseArrayList.get(getAdapterPosition()).getSchemeCode());

                args.putString("schemeName", schemeResponseArrayList.get(getAdapterPosition()).getSchemeName());

                args.putString("status","3");

                Fragment fragment = new BuyActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }

            if (view.getId() == R.id.imgFactsheet) {
                Bundle args = new Bundle();

                args.putString("commanScriptCode", schemeResponseArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("factsheetStatus","2");
                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            } if (view.getId() == R.id.txtFundName) {
                Bundle args = new Bundle();

                args.putString("commanScriptCode", schemeResponseArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("factsheetStatus","2");
                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }

            if (view.getId() == R.id.txtSip)
            {
                Bundle args = new Bundle();
                args.putString("commanScriptCode", schemeResponseArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", schemeResponseArrayList.get(getAdapterPosition()).getSchemeName());
                args.putString("status","3");

                Fragment fragment = new SipActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }
        }
    }

    public abstract void getDetail(Fragment fragment);
}
