package com.bob.bobapp.adapters;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.AssetAllocationResponseObject;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssetAllocationAdpter extends RecyclerView.Adapter<AssetAllocationAdpter.ViewHolder> {

    private Context context;

    private Util util;

    private ArrayList<AssetAllocationResponseObject> arrayList;
    private DecimalFormat formatter;

    public AssetAllocationAdpter(Context context, ArrayList<AssetAllocationResponseObject> holdingArrayList) {

        this.context = context;

        this.arrayList = holdingArrayList;

        util = new Util(context);

        formatter = new DecimalFormat("###,###,##.##");
    }

    public void updateList(ArrayList<AssetAllocationResponseObject> list) {

        arrayList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem = layoutInflater.inflate(R.layout.asset_allocation_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        AssetAllocationResponseObject model = arrayList.get(position);

//        Toast.makeText(context,model.getDataSource().trim(),Toast.LENGTH_SHORT).show();

  //      if(model.getDataSource().equalsIgnoreCase("Mutual Fund"))

        holder.assetName.setText(model.getAssetClassName());

        double d = Double.parseDouble(model.getValuePercentage());

        d = util.truncateDecimal(d).doubleValue();

        int progress = (int) d;

        holder.progressBar.setProgress(progress);

        Drawable draw= model.getColorDrawable();

        holder.progressBar.setProgressDrawable(draw);

      //  holder.tvPercentage.setText(formatter.format(Double.parseDouble(model.getValuePercentage()))+ "%");

        holder.tvPercentage.setText(String.valueOf(d) + "%");
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView assetName, tvPercentage;

        ProgressBar progressBar;

        private LinearLayout linearData;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            assetName = itemView.findViewById(R.id.tv_asset_name);

            tvPercentage = itemView.findViewById(R.id.tv_percentage);

            progressBar = itemView.findViewById(R.id.progress_bar);

            linearData = itemView.findViewById(R.id.linearData);
        }
    }
}
