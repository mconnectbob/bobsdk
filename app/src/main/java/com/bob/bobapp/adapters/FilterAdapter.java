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
import com.bob.bobapp.api.response_object.LstRecommandationDebt;
import com.bob.bobapp.api.response_object.lstRecommandationCash;
import com.bob.bobapp.api.response_object.lstRecommandationEquity;
import com.bob.bobapp.api.response_object.lstRecommandationHybrid;
import com.bob.bobapp.listener.OnGenericListener;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private Context context;
    private ArrayList<? extends Object> arrayList;
    private String status;
    private OnGenericListener onGenericListener;

    public FilterAdapter(Context context, ArrayList<? extends Object> arrayList, String status,OnGenericListener onGenericListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.status = status;
        this.onGenericListener = onGenericListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new FilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object object = arrayList.get(position);

        if (object instanceof lstRecommandationEquity) {
            holder.title.setText(((lstRecommandationEquity) object).getClassification());
        }

        if (object instanceof LstRecommandationDebt) {
            holder.title.setText(((LstRecommandationDebt) object).getClassification());
        }
        if (object instanceof lstRecommandationCash) {
            holder.title.setText(((lstRecommandationCash) object).getClassification());
        }

        if (object instanceof lstRecommandationHybrid) {
            holder.title.setText(((lstRecommandationHybrid) object).getClassification());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object instanceof lstRecommandationEquity) {
                    onGenericListener.onItemDeleteListener(((lstRecommandationEquity) object).getClassification(), "", status,"","");
                }

                if (object instanceof LstRecommandationDebt) {
                    onGenericListener.onItemDeleteListener(((LstRecommandationDebt) object).getClassification(), "", status,"","");
                }

                if (object instanceof lstRecommandationCash) {
                    onGenericListener.onItemDeleteListener(((lstRecommandationCash) object).getClassification(), "", status,"","");
                }

                if (object instanceof lstRecommandationHybrid) {
                    onGenericListener.onItemDeleteListener(((lstRecommandationHybrid) object).getClassification(), "", status,"","");
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
