package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.bean.ProductValueBean;
import com.bob.bobapp.utility.ProductAllocationObject;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAllocationAdapter extends RecyclerView.Adapter<ProductAllocationAdapter.ViewHolder> {

    private Context context;

    private Util util;

    private ArrayList<ProductAllocationObject> productAllocationObjectArrayList;

    private DecimalFormat formatter;

    public ProductAllocationAdapter(Context context, ArrayList<ProductAllocationObject> productAllocationObjectArrayList) {

        this.context = context;

        this.productAllocationObjectArrayList = productAllocationObjectArrayList;

        util = new Util(context);
        formatter = new DecimalFormat("###,###,##.##");
    }

    public void updateList(ArrayList<ProductAllocationObject> list) {

        productAllocationObjectArrayList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem = layoutInflater.inflate(R.layout.layout_product_allocation_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        ProductAllocationObject model = productAllocationObjectArrayList.get(position);

        holder.productName.setText(model.getProductName());

//        holder.productCost.setText(model.getProductCost());
        holder.productCost.setText(formatter.format(Double.parseDouble(model.getProductCost())));
     //   holder.productCost.setText(new DecimalFormat("##.##").format(Double.valueOf(model.getProductCost())));


        double d = Double.parseDouble(model.getProductPercentage());

        d = util.truncateDecimal(d).doubleValue();

        holder.productPercentage.setText(d + "%");

        if(position % 2 == 0){

            holder.productCost.setTextColor(context.getResources().getColor(R.color.bar_chart_color_orange));

            holder.productPercentage.setTextColor(context.getResources().getColor(R.color.bar_chart_color_orange));

        }else{

            holder.productCost.setTextColor(context.getResources().getColor(R.color.btn_color));

            holder.productPercentage.setTextColor(context.getResources().getColor(R.color.btn_color));
        }
    }

    @Override
    public int getItemCount() {

        return productAllocationObjectArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productCost, productPercentage;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            productName = itemView.findViewById(R.id.tv_product_item_name);

            productCost = itemView.findViewById(R.id.tv_product_item_cost);

            productPercentage = itemView.findViewById(R.id.tv_product_item_percentage);
        }
    }
}
