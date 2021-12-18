package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.AssetTypesResponse;
import com.bob.bobapp.listener.OnItemDeleteListener;

import java.util.ArrayList;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<AssetTypesResponse> assetTypesResponseArrayList;
    private int lastCheckedPosition = -1;
    private OnItemDeleteListener OnItemDeleteListener;
    public AssetAdapter(Context context, ArrayList<AssetTypesResponse> assetTypesResponseArrayList,OnItemDeleteListener OnItemDeleteListener) {
        this.context = context;
        this.assetTypesResponseArrayList = assetTypesResponseArrayList;
        this.OnItemDeleteListener = OnItemDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item_adapter, parent, false);
        return new AssetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.radioName.setText(assetTypesResponseArrayList.get(position).getAssetClassName());

        holder.radioName.setChecked(position == lastCheckedPosition);

        holder.radioName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = position;
                OnItemDeleteListener.onItemDeleteListener(assetTypesResponseArrayList.get(position).getAssetClassCode(),position,assetTypesResponseArrayList.get(position).getAssetClassName());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return assetTypesResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioName=itemView.findViewById(R.id.radioName);
        }
    }
}
