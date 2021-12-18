package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.BuyConfirmResponse;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;

import java.util.ArrayList;

public class ConfirmOrderStatusAdapter extends RecyclerView.Adapter<ConfirmOrderStatusAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList;
    private ArrayList<BuyConfirmResponse> buyConfirmResponseArrayList;
    private String clientName = "", status = "";

    public ConfirmOrderStatusAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList, ArrayList<BuyConfirmResponse> buyConfirmResponseArrayList, String clientName, String status) {
        this.context = context;
        this.requestBodyObjectArrayList = requestBodyObjectArrayList;
        this.buyConfirmResponseArrayList = buyConfirmResponseArrayList;
        this.clientName = clientName;
        this.status = status;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_order_status_adapter, parent, false);
        return new ConfirmOrderStatusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.txtSchemeName.setText(requestBodyObjectArrayList.get(position).getFundName());
            holder.txtAccountName.setText(clientName);
            holder.txtAmount.setText(requestBodyObjectArrayList.get(position).getTxnAmountUnit());

            if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("3") || status.equalsIgnoreCase("4")) {
                holder.txtOrderId.setText(buyConfirmResponseArrayList.get(position).getOrderNo());
            } else {
                holder.txtOrderId.setText(buyConfirmResponseArrayList.get(position).getRequestID());
            }


            if (buyConfirmResponseArrayList.get(position).getSuccessFlag().equalsIgnoreCase("true")) {
                holder.txtOrderStatus.setText("successful");
            } else {
                holder.txtOrderStatus.setText(buyConfirmResponseArrayList.get(position).getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return requestBodyObjectArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView txtSchemeName, txtAccountName, txtAmount, txtOrderId, txtOrderStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtAccountName = itemView.findViewById(R.id.txtAccountName);
        }
    }
}
