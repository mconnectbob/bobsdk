package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.AccountsResponse;
import com.bob.bobapp.api.response_object.BankAccountResponse;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.listener.OnGenericListener;

import java.util.ArrayList;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {
    private Context context;
    private ArrayList<? extends Object> arrayList;
    private OnGenericListener onGenericListener;
    private String status;

    public GenericAdapter(Context context, ArrayList<? extends Object> arrayList, OnGenericListener onGenericListener, String status) {
        this.context = context;
        this.arrayList = arrayList;
        this.onGenericListener = onGenericListener;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new GenericAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object object = arrayList.get(position);
        if (object instanceof FolioWisePendingUnitsCollection) {
            holder.title.setText(((FolioWisePendingUnitsCollection) object).getFolioNo());
        }

        if (object instanceof String) {
            holder.title.setText((CharSequence) object);
        }

        if (object instanceof AccountsResponse) {
            holder.title.setText(((AccountsResponse) object).getClientName());

        }

        if (object instanceof BankAccountResponse) {
            holder.title.setText(((BankAccountResponse) object).getBankName());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object instanceof FolioWisePendingUnitsCollection) {
                    onGenericListener.onItemDeleteListener(((FolioWisePendingUnitsCollection) object).getFolioNo(), "", status,"","");
                }
                if (object instanceof String) {
                    onGenericListener.onItemDeleteListener(((CharSequence) object).toString(), "" + position, status,"","");
                }

                if (object instanceof AccountsResponse) {
                    onGenericListener.onItemDeleteListener(((AccountsResponse) object).getClientName(), ((AccountsResponse) object).getClientCode(), status,"","");
                }
                if (object instanceof BankAccountResponse) {
                    onGenericListener.onItemDeleteListener(((BankAccountResponse) object).getBankName(), ((BankAccountResponse) object).getBankCode(), status, ((BankAccountResponse) object).getBankSource(),((BankAccountResponse) object).getBankAccountNo());
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
