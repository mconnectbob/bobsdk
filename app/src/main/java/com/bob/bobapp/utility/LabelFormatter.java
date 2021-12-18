package com.bob.bobapp.utility;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class LabelFormatter extends ValueFormatter {
    private final ArrayList<String> mLabels;

    public LabelFormatter(ArrayList<String> labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value) {

        System.out.println("FORMATER VALUE :" + value);
        if((int) value < mLabels.size()){

            return mLabels.get((int) (value));

        }else {

            return "";
        }
    }
}