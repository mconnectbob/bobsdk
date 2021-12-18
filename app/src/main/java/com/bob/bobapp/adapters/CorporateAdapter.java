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
import com.bob.bobapp.api.response_object.TransactionResponseModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CorporateAdapter extends RecyclerView.Adapter<CorporateAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TransactionResponseModel> arrayList;
    private String whichActivity;
    private DecimalFormat formatter;

    public CorporateAdapter(Context context, ArrayList<TransactionResponseModel> arrayList, String whichActivity) {
        this.context = context;
        this.arrayList = arrayList;
        this.whichActivity = whichActivity;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    public void updateList(ArrayList<TransactionResponseModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.corporate_item, parent, false);
     ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransactionResponseModel model = arrayList.get(position);

        String oldDate = model.getDate();

        String[] arrayString = oldDate.split("T");

        String fromDate = arrayString[0];

        String[] arrayStrings = fromDate.split("-");

        String year = arrayStrings[0];
        String month = arrayStrings[1];
        String day = arrayStrings[2];

        String finalDateTo = day + "-" + month + "-" + year;

        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = dt.parse(finalDateTo);
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



        holder.name.setText(model.getSecurity());
        holder.folioNo.setText(model.getSource());
        holder.unit.setText(model.getTransactionType());
        holder.nav.setText(""+model.getQuantity());
      //  holder.date.setText(finalDateTo);
        holder.transactionType.setText(""+model.getTransactionRate());
        //holder.amount.setText(new DecimalFormat("##.##").format(Double.valueOf(model.getAmount())));
        holder.amount.setText(formatter.format(Double.parseDouble(""+model.getAmount())));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, folioNo, unit, nav, date, transactionType, amount;
        LinearLayout corporateAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            folioNo = itemView.findViewById(R.id.folioNo);
            unit = itemView.findViewById(R.id.unit);
            nav = itemView.findViewById(R.id.nav);
            date = itemView.findViewById(R.id.date);
            transactionType = itemView.findViewById(R.id.transactionType);
            amount = itemView.findViewById(R.id.amount);
            corporateAction = itemView.findViewById(R.id.corporateAction);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

            }
        }

    }
}
