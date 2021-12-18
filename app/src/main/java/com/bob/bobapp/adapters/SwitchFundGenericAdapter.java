package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.api.response_object.TranferSchemeResponse;
import com.bob.bobapp.listener.OnGenericListener;

import java.util.ArrayList;

public class SwitchFundGenericAdapter extends RecyclerView.Adapter<SwitchFundGenericAdapter.ViewHolder> {
    private Context context;
    private ArrayList<? extends Object> arrayList;
    private OnGenericListener onGenericListener;


    public SwitchFundGenericAdapter(Context context, ArrayList<? extends Object> arrayList, OnGenericListener onGenericListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onGenericListener = onGenericListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_fund_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object object = arrayList.get(position);

        if (object instanceof TranferSchemeResponse) {
            holder.title.setText(((TranferSchemeResponse) object).getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object instanceof TranferSchemeResponse) {
                    onGenericListener.onItemDeleteListener(((TranferSchemeResponse) object).getName(), ((TranferSchemeResponse) object).getCode(),"","","");
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
