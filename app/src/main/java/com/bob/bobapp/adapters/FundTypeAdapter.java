package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.FundTypesResponse;
import com.bob.bobapp.listener.OnFilterItemListener;

import java.util.ArrayList;

public class FundTypeAdapter extends RecyclerView.Adapter<FundTypeAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<FundTypesResponse> fundTypesResponseArrayList;
    private OnFilterItemListener OnFilterItemListener;
    private int lastCheckedPosition = -1;

    public FundTypeAdapter(Context context,ArrayList<FundTypesResponse> fundTypesResponseArrayList,OnFilterItemListener OnFilterItemListener) {
        this.context = context;
        this.fundTypesResponseArrayList = fundTypesResponseArrayList;
        this.OnFilterItemListener = OnFilterItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item_adapter, parent, false);
        return new FundTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.radioName.setText(fundTypesResponseArrayList.get(position).getFundTypeName());

        holder.radioName.setChecked(position == lastCheckedPosition);

        holder.radioName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = position;
                OnFilterItemListener.onFundTypeItemListener(position,fundTypesResponseArrayList.get(position).getFundTypeCode());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fundTypesResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioName=itemView.findViewById(R.id.radioName);
        }
    }
}
