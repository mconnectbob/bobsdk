package com.bob.bobapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bob.bobapp.R;
import com.bob.bobapp.listener.onSortItemListener;

public class DividendPopup extends Dialog{
    private ImageView imgClose;
    private TextView txtReinvest,txtPayout;
    private Context context;
    private onSortItemListener onSortItemListener;
    public DividendPopup(@NonNull Context context,onSortItemListener onSortItemListener) {
        super(context);
        this.onSortItemListener=onSortItemListener;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dividend_dialog);
        findView();

    }

    // to get id here...
    private void findView()
    {
        imgClose=findViewById(R.id.imgClose);
        txtReinvest=findViewById(R.id.txtReinvest);
        txtPayout=findViewById(R.id.txtPayout);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtReinvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("1");
                dismiss();
            }
        });

        txtPayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("2");
                dismiss();
            }
        });
    }


}
