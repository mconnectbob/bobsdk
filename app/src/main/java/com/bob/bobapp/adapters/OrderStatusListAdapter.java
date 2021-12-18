package com.bob.bobapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.OrderStatusResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderStatusListAdapter extends RecyclerView.Adapter<OrderStatusListAdapter.ViewHolder> {

    private Context context;

    private ArrayList<OrderStatusResponse> arrayList;
    private DecimalFormat formatter;

    public OrderStatusListAdapter(Context context, ArrayList<OrderStatusResponse> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    public void updateList(ArrayList<OrderStatusResponse> arrayList) {

        this.arrayList = arrayList;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItem = layoutInflater.inflate(R.layout.order_status_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderStatusResponse model = arrayList.get(position);

        holder.fundName.setText(model.getFundName());

        holder.name.setText(model.getClientName());

//       holder.date.setText(model.getOrderDate().substring(0, 10));

        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dt.parse(model.getOrderDate().substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // *** same for the format String below
        // SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println("fdfszfs"+dt1.format(date));

        holder.date.setText(dt1.format(date));



        //  holder.status.setPadding(10, 10, 10, 10);

        holder.status.setText(model.getOrderStatus());

        if(model.getOrderNo()!=null)
        {
            holder.txtOrderId.setText(""+model.getOrderNo());
        }
        else
        {
            holder.txtOrderId.setText("-");
        }
        if (model.getOrderStatus() != null && model.getOrderStatus().equalsIgnoreCase("Pending")) {

            holder.status.setTextColor(Color.parseColor("#FF3100"));
          //  holder.status.setBackgroundResource(R.drawable.rounded_inner_orange);
        }

        else if (model.getOrderStatus() != null && model.getOrderStatus().equalsIgnoreCase("Placed")) {
            holder.status.setTextColor(Color.parseColor("#1F3C66"));
           // holder.status.setBackgroundResource(R.drawable.rounded_inner_green);
        }  else if (model.getOrderStatus() != null && model.getOrderStatus().equalsIgnoreCase("Cancelled")) {
            holder.status.setTextColor(Color.parseColor("#FF3100"));
          //  holder.status.setBackgroundResource(R.drawable.rounded_red_rect);
        }
        else
        {
            holder.status.setBackgroundResource(R.drawable.rounded_inner_green);
        }


        if (model.getOrderType().equalsIgnoreCase("Switch") || model.getOrderType().equalsIgnoreCase("STP")) {
            holder.linearSwitch.setVisibility(View.VISIBLE);
            holder.txtSwitchToFund.setText(model.getFromFundName());
        } else {
            holder.linearSwitch.setVisibility(View.GONE);

        }

        if (model.getOrderType().equalsIgnoreCase("Redemption") || model.getOrderType().equalsIgnoreCase("Switch") || model.getOrderType().equalsIgnoreCase("STP") || model.getOrderType().equalsIgnoreCase("SWP")) {
            holder.txtAmountChange.setText("Amount/Unit");
            holder.linearOrderBasis.setVisibility(View.VISIBLE);
        } else {
            holder.linearOrderBasis.setVisibility(View.GONE);
            holder.txtAmountChange.setText("Amount");
        }

        double amt = model.getOrderAmount();

        if (amt > 0) {
            holder.amount.setText(formatter.format(Double.parseDouble("" + model.getOrderAmount())));
            holder.txtOrderBasis.setText("Amount");

        } else {
            holder.amount.setText(formatter.format(Double.parseDouble("" + model.getOrderQty())));
            holder.txtOrderBasis.setText("Unit");
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fundName, name, date, amount, status, txtSwitchToFund, txtAmountChange, txtOrderBasis,txtOrderId;
        private LinearLayout linearSwitch, linearOrderBasis;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            fundName = itemView.findViewById(R.id.fundName);

            name = itemView.findViewById(R.id.name);

            date = itemView.findViewById(R.id.date);

            amount = itemView.findViewById(R.id.amount);

            status = itemView.findViewById(R.id.status);

            linearSwitch = itemView.findViewById(R.id.linearSwitch);

            txtSwitchToFund = itemView.findViewById(R.id.txtSwitchToFund);

            txtAmountChange = itemView.findViewById(R.id.txtAmountChange);

            linearOrderBasis = itemView.findViewById(R.id.linearOrderBasis);

            txtOrderBasis = itemView.findViewById(R.id.txtOrderBasis);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
        }
    }
}
