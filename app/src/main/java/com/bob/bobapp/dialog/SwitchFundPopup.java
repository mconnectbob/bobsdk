package com.bob.bobapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.adapters.GenericAdapter;
import com.bob.bobapp.adapters.SwitchFundGenericAdapter;
import com.bob.bobapp.listener.OnGenericListener;

import java.util.ArrayList;

public class SwitchFundPopup extends Dialog implements OnGenericListener {
    private Context context;
    private ArrayList<? extends Object> arrayList;
    private RecyclerView recyclerFolio;
    private SwitchFundGenericAdapter switchFundGenericAdapter;
    private OnGenericListener onGenericListener;

    public SwitchFundPopup(@NonNull Context context, ArrayList<? extends Object> arrayList, OnGenericListener onGenericListener) {
        super(context);
        this.arrayList = arrayList;
        this.context = context;
        this.onGenericListener = onGenericListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_dialog);
        findView();
        setAdapter();

    }

    // adapter
    private void setAdapter() {
        switchFundGenericAdapter = new SwitchFundGenericAdapter(getContext(), arrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerFolio.setLayoutManager(linearLayoutManager);
        recyclerFolio.setAdapter(switchFundGenericAdapter);
    }

    // get id here...
    private void findView() {
        recyclerFolio = findViewById(R.id.recyclerFolio);
    }


    @Override
    public void onItemDeleteListener(String name, String fundCode,String status,String bankSource,String accountNumber) {
        dismiss();
        onGenericListener.onItemDeleteListener(name, fundCode,status,"","");
    }
}
