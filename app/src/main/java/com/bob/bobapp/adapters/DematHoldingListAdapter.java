package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.response_object.TransactionResponseModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DematHoldingListAdapter extends RecyclerView.Adapter<DematHoldingListAdapter.ViewHolder> {

    private Context context;
    private List<ClientHoldingObject> arrayList;
    private DecimalFormat formatter;

    public DematHoldingListAdapter(Context context, ArrayList<ClientHoldingObject> holdingArrayList) {
        this.context = context;
        this.arrayList = holdingArrayList;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    public void updateList(ArrayList<ClientHoldingObject> arrayList) {

        this.arrayList = arrayList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.demat_holding_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClientHoldingObject model = arrayList.get(position);
        holder.name.setText(model.getSchemeName() + "");
        holder.quantity.setText(model.getQuantity() + "");
        holder.price.setText(formatter.format(Double.parseDouble(model.getCurrentPrice())));
        holder.marketValue.setText(formatter.format(Double.parseDouble(model.getMarketValue())));
//        if (model.getSource().equalsIgnoreCase("Direct Equity"))
//        {
//            holder.linearData.setVisibility(View.VISIBLE);
//            holder.name.setText(model.getSchemeName() + "");
//            holder.quantity.setText(model.getQuantity() + "");
//            holder.price.setText(formatter.format(Double.parseDouble(model.getCurrentPrice())));
//            holder.marketValue.setText(formatter.format(Double.parseDouble(model.getMarketValue())));
//
//        } else {
//            holder.linearData.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, quantity, price, marketValue;
        private LinearLayout linearData;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            marketValue = itemView.findViewById(R.id.marketValue);
            linearData = itemView.findViewById(R.id.linearData);

        }

    }
}
