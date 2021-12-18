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
import com.bob.bobapp.api.response_object.SIPDueReportResponse;
import com.bob.bobapp.api.response_object.TransactionResponseModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SIPSWPSTPDueListAdapter extends RecyclerView.Adapter<SIPSWPSTPDueListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SIPDueReportResponse> arrayList;
    private DecimalFormat formatter;

    public SIPSWPSTPDueListAdapter(Context context, ArrayList<SIPDueReportResponse> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    public void updateList(ArrayList<SIPDueReportResponse> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.sip_swp_stp_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SIPDueReportResponse model = arrayList.get(position);

        holder.name.setText(model.getFund_Name());
        if (model.getType().equalsIgnoreCase("STP")) {
            holder.linearFundname.setVisibility(View.VISIBLE);
            holder.folioNo.setText(model.getTo_FundName());
        } else {
            holder.linearFundname.setVisibility(View.GONE);
        }

        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dt.parse(model.getDueDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // *** same for the format String below
            // SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
            System.out.println("fdfszfs" + dt1.format(date));


            holder.date.setText(dt1.format(date));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        holder.type.setText(model.getType());

        holder.amount.setText(formatter.format(Double.parseDouble("" + model.getAmount())));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, folioNo, date, type, amount;
        private LinearLayout linearFundname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            folioNo = itemView.findViewById(R.id.folioNo);
            date = itemView.findViewById(R.id.date);
            type = itemView.findViewById(R.id.type);
            amount = itemView.findViewById(R.id.amount);
            linearFundname = itemView.findViewById(R.id.linearFundname);

        }

        @Override
        public void onClick(View view) {

//            switch (view.getId()) {
//
//
//            }
        }

    }
}
