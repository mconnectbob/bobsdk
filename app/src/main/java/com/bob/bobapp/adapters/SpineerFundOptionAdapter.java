package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bob.bobapp.R;

import java.util.ArrayList;

public class SpineerFundOptionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> fundOptionArrayList;

    public SpineerFundOptionAdapter(Context context, ArrayList<String> fundOptionArrayList) {
        this.context = context;
        this.fundOptionArrayList = fundOptionArrayList;
    }


    @Override
    public int getCount() {
        return fundOptionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.spinner_fund_house_item, parent, false);
        TextView title = convertView.findViewById(R.id.title);
        title.setText(fundOptionArrayList.get(position));
        return convertView;
    }
}
