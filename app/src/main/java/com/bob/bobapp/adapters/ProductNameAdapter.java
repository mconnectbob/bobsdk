package com.bob.bobapp.adapters;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.bean.ProductValueBean;
import com.bob.bobapp.api.response_object.AssetAllocationResponseObject;
import com.bob.bobapp.utility.Util;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ProductNameAdapter extends RecyclerView.Adapter<ProductNameAdapter.ViewHolder> {

    private Context context;

    private Util util;

    private ArrayList<ProductValueBean> arrayList;

    private int[] VORDIPLOM_COLORS;

    public ProductNameAdapter(Context context, ArrayList<ProductValueBean> holdingArrayList, int[] VORDIPLOM_COLORS) {

        this.context = context;

        this.arrayList = holdingArrayList;

        this.VORDIPLOM_COLORS = VORDIPLOM_COLORS;

        util = new Util(context);
    }

    public void updateList(ArrayList<ProductValueBean> list) {

        arrayList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem = layoutInflater.inflate(R.layout.product_item_name, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        ProductValueBean model = arrayList.get(position);

        holder.productName.setText(model.getLabel());

        holder.productNameCheckBox.setBackgroundColor(VORDIPLOM_COLORS[position]);

        double d = Double.parseDouble(model.getPercentage());

        d = util.truncateDecimal(d).doubleValue();

        holder.productPercentage.setText(d + "%");
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPercentage, productNameCheckBox;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            productName = itemView.findViewById(R.id.tv_product_name);

            productNameCheckBox = itemView.findViewById(R.id.tv_product_name_check_box);

            productPercentage = itemView.findViewById(R.id.tv_product_percentage);
        }
    }
}
