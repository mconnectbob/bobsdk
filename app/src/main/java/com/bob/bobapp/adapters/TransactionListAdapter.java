package com.bob.bobapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.response_object.TransactionResponseModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransactionResponseModel> arrayList;
    private String whichActivity;
    private DecimalFormat formatter;

    public TransactionListAdapter(Context context, ArrayList<TransactionResponseModel> arrayList, String whichActivity) {
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
        View listItem = layoutInflater.inflate(R.layout.transaction_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransactionResponseModel model = arrayList.get(position);



        if(model.getSource().equalsIgnoreCase("Equity"))
        {
            holder.linearData.setVisibility(View.GONE);
        }
        else
        {
            holder.linearData.setVisibility(View.VISIBLE);
            if (whichActivity.equalsIgnoreCase("TransactionActivity")) {
                holder.corporateAction.setVisibility(View.VISIBLE);
            } else {
                holder.corporateAction.setVisibility(View.GONE);
            }

            String oldDate = model.getDate();


            String[] arrayString = oldDate.split("T");

            String fromDate = arrayString[0];

            String[] arrayStrings = fromDate.split("-");

            String year = arrayStrings[0];
            String month = arrayStrings[1];
            String day = arrayStrings[2];

          //  String finalDateTo = day + "-" + month + "-" + year;
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



            holder.name.setText(model.getSecurity());
            holder.folioNo.setText(model.getAccountName());
            holder.unit.setText(new DecimalFormat("##.##").format(Double.valueOf(model.getQuantity())));
            holder.nav.setText(String.valueOf(model.getTransactionRate()));
            holder.date.setText(dt1.format(date));
            holder.transactionType.setText(model.getTransactionType());
            //holder.amount.setText(new DecimalFormat("##.##").format(Double.valueOf(model.getAmount())));
            holder.amount.setText(formatter.format(Double.parseDouble(""+model.getAmount())));

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, folioNo, unit, nav, date, transactionType, amount;
        LinearLayout corporateAction,linearData;

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
            linearData = itemView.findViewById(R.id.linearData);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

            }
        }

    }
}
