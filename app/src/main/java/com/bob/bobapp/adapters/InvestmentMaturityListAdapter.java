package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.InvestmentMaturityModel;
import com.bob.bobapp.api.response_object.SIPDueReportResponse;
import com.bob.bobapp.api.response_object.TransactionResponseModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvestmentMaturityListAdapter extends RecyclerView.Adapter<InvestmentMaturityListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<InvestmentMaturityModel> arrayList;

    public InvestmentMaturityListAdapter(Context context, ArrayList<InvestmentMaturityModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void updateList(ArrayList<InvestmentMaturityModel> arrayList)
    {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.investment_maturity_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        InvestmentMaturityModel model = arrayList.get(position);

        String oldDate = model.getValueDate();

        String[] arrayString = oldDate.split("T");

        String fromDate = arrayString[0];

        String[] arrayStrings = fromDate.split("-");

        String year = arrayStrings[0];
        String month = arrayStrings[1];
        String day = arrayStrings[2];

       // String finalDateTo = day + "-" + month + "-" + year;

        String finalDateTo = year + "-" + month + "-" + day;

        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dt.parse(finalDateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // *** same for the format String below
        // SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println("fdfszfs"+dt1.format(date));


        holder.name.setText(model.getSchName());
        holder.folioNo.setText(model.getClientname());
        holder.maturityDate.setText(dt1.format(date));
        holder.currentValue.setText(new DecimalFormat("###,###,##0.00").format(Double.valueOf(model.getCostOnInvestment())));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, folioNo, currentValue, maturityDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            folioNo = itemView.findViewById(R.id.folioNo);
            currentValue = itemView.findViewById(R.id.currentValue);
            maturityDate = itemView.findViewById(R.id.maturityDate);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

            }
        }

    }
}
