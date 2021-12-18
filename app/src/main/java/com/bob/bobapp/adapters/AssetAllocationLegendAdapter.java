package com.bob.bobapp.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.AssetAllocationPerformanceResponseObject;
import com.bob.bobapp.api.response_object.AssetAllocationResponseObject;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssetAllocationLegendAdapter extends RecyclerView.Adapter<AssetAllocationLegendAdapter.ViewHolder> {

    private Context context;

    private Util util;

    private ArrayList<AssetAllocationResponseObject> arrayList;
    ArrayList<AssetAllocationPerformanceResponseObject> AssetAllocationPerformanceArrayList;
    private DecimalFormat formatter;

    public AssetAllocationLegendAdapter(Context context, ArrayList<AssetAllocationResponseObject> holdingArrayList, ArrayList<AssetAllocationPerformanceResponseObject> AssetAllocationPerformanceArrayList) {

        this.context = context;

        this.arrayList = holdingArrayList;

        this.AssetAllocationPerformanceArrayList = AssetAllocationPerformanceArrayList;

        util = new Util(context);

        formatter = new DecimalFormat("###,###,###");
    }

    public void updateList(ArrayList<AssetAllocationResponseObject> list) {

        arrayList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem = layoutInflater.inflate(R.layout.asset_allocation_legend_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        AssetAllocationResponseObject model = arrayList.get(position);

        for (int i = 0; i < AssetAllocationPerformanceArrayList.size(); i++) {
            if (model.getAssetClassName().equalsIgnoreCase(AssetAllocationPerformanceArrayList.get(i).getAssetClass())) {
                double ds = Double.parseDouble(AssetAllocationPerformanceArrayList.get(i).getXIRRPercentage());

                ds = util.truncateDecimal(ds).doubleValue();

                holder.txtXirr.setText(ds + "%");
                break;
            } else {
                holder.txtXirr.setText("-");
            }
        }

        if (model.getAssetClassName().equalsIgnoreCase("Balanced")) {
            holder.assetName.setText("Hybrid");
        } else if (model.getAssetClassName().equalsIgnoreCase("Cash")) {
            holder.assetName.setText("Liquid");
        } else {

            holder.assetName.setText(model.getAssetClassName());
        }

        holder.txtMarketValue.setText(formatter.format(Double.parseDouble(String.valueOf(model.getValueAmount()))));

        double d = Double.parseDouble(model.getValuePercentage());

        d = util.truncateDecimal(d).doubleValue();

        holder.assetValue.setText(d + "%");

        if (position % 2 == 0) {

            holder.assetValue.setTextColor(context.getResources().getColor(R.color.bar_chart_color_orange));

            holder.txtMarketValue.setTextColor(context.getResources().getColor(R.color.bar_chart_color_orange));

            holder.txtXirr.setTextColor(context.getResources().getColor(R.color.bar_chart_color_orange));

        } else {

            holder.assetValue.setTextColor(context.getResources().getColor(R.color.btn_color));

            holder.txtMarketValue.setTextColor(context.getResources().getColor(R.color.btn_color));

            holder.txtXirr.setTextColor(context.getResources().getColor(R.color.btn_color));
        }
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView assetName, assetValue, txtMarketValue, txtXirr;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            assetName = itemView.findViewById(R.id.tv_asset_name);

            assetValue = itemView.findViewById(R.id.tv_asset_value);
            txtMarketValue = itemView.findViewById(R.id.txtMarketValue);
            txtXirr = itemView.findViewById(R.id.txtXirr);
        }
    }
}
