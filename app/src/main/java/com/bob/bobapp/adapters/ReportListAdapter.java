package com.bob.bobapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.activities.BuySIPRedeemSwitchActivity;
import com.bob.bobapp.activities.CorporateActivity;
import com.bob.bobapp.activities.HoldingsActivity;
import com.bob.bobapp.activities.InsuranceActivity;
import com.bob.bobapp.activities.InvestmentMaturityActivity;
import com.bob.bobapp.activities.RealizedGainLossActivity;
import com.bob.bobapp.activities.SIPSWPSTPDueActivity;
import com.bob.bobapp.activities.TransactionActivity;
import com.bob.bobapp.utility.IntentKey;

public abstract class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {

    private Context context;
    private String[] arrayTitle;
    private int[] arrayImage;

    public ReportListAdapter(Context context, String[] arrayTitle, int[] arrayImage) {
        this.context = context;
        this.arrayTitle = arrayTitle;
        this.arrayImage = arrayImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.explore_more_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.img_explore_more_icon.setBackgroundDrawable(context.getResources().getDrawable(arrayImage[position]));
        holder.tv_title.setText(arrayTitle[position]);

    }

    @Override
    public int getItemCount() {
        return arrayImage.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_explore_more_icon;
        private TextView tv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_explore_more_icon = itemView.findViewById(R.id.img_explore_more_icon);
            tv_title = itemView.findViewById(R.id.tv_title);

            img_explore_more_icon.setOnClickListener(this);
            tv_title.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            int id = view.getId();
            if (id == R.id.img_explore_more_icon || id == R.id.tv_title)
            {
                switch (getAdapterPosition()) {
                    case 0:
                        getDetail(new HoldingsActivity());
                        break;
                    case 1:
                        Bundle args = new Bundle();

                        args.putString("WhichActivity", "TransactionActivity");

                        Fragment fragment = new TransactionActivity();

                        fragment.setArguments(args);

                        getDetail(fragment);
                        break;

                    case 2:
                        getDetail(new SIPSWPSTPDueActivity());
                        break;

                    case 3:
                        getDetail(new InvestmentMaturityActivity());
                        break;

                    case 4:
                        getDetail(new RealizedGainLossActivity());
                        break;

                    case 5:

                        Bundle argsNew = new Bundle();

                        argsNew.putString("WhichActivity", "CorporateActionActivity");

                        Fragment fragmentNew = new CorporateActivity();

                        fragmentNew.setArguments(argsNew);

                        getDetail(fragmentNew);

                        break;

                    case 6:
                        getDetail(new InsuranceActivity());
                        break;

                }
            }
        }
    }

    public abstract void getDetail(Fragment fragment);
}
