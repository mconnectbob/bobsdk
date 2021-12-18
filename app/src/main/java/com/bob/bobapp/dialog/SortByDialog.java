package com.bob.bobapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.bob.bobapp.R;
import com.bob.bobapp.listener.onSortItemListener;

public class SortByDialog extends Dialog {
    private ImageView imgClose;
    private onSortItemListener onSortItemListener;
    private AppCompatTextView txtSchemeName, txtSchemeQuantity;
    private String value = "";

    public SortByDialog(@NonNull Context context, onSortItemListener onSortItemListener, String type) {
        super(context);
        this.onSortItemListener = onSortItemListener;
        value = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_dialog);
        findView();
    }

    // initialize object here....
    private void findView() {
        imgClose = findViewById(R.id.imgClose);
        txtSchemeName = findViewById(R.id.txtSchemeName);
        txtSchemeQuantity = findViewById(R.id.txtSchemeQuantity);
        imgClose.setOnClickListener(view -> dismiss());

        if (value.equalsIgnoreCase("3M Value")) {
            txtSchemeQuantity.setVisibility(View.GONE);
        } else {
            txtSchemeQuantity.setVisibility(View.VISIBLE);

        }
        txtSchemeQuantity.setText(value);
        txtSchemeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("1");
                dismiss();
            }
        });

        txtSchemeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSortItemListener.onSortItemListener("2");
                dismiss();
            }
        });
    }
}
