package com.bob.bobapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;

import java.util.ArrayList;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList;
    private String clientName;

    public ConfirmOrderAdapter(Context context,ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList,String clientName) {
        this.context = context;
        this.requestBodyObjectArrayList = requestBodyObjectArrayList;
        this.clientName = clientName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_order_adapter, parent, false);
        return new ConfirmOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtSchemeName.setText(requestBodyObjectArrayList.get(position).getFundName());
        holder.txtAmount.setText(requestBodyObjectArrayList.get(position).getTxnAmountUnit());
        holder.txtAccountName.setText(clientName);
    }

    @Override
    public int getItemCount() {
        return requestBodyObjectArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView txtSchemeName,txtAmount,txtAccountName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSchemeName=itemView.findViewById(R.id.txtSchemeName);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            txtAccountName=itemView.findViewById(R.id.txtAccountName);
        }
    }
}
