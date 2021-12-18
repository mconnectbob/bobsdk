package com.bob.bobapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.adapters.FundHouseAdapter;
import com.bob.bobapp.adapters.GenericAdapter;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.listener.OnGenericListener;

import java.util.ArrayList;

public class PopupDialog extends Dialog implements OnGenericListener {
    private Context context;
    private ArrayList<? extends Object> arrayList;
    private RecyclerView recyclerFolio;
    private GenericAdapter genericAdapter;
    private OnGenericListener onGenericListener;
    private String status;

    public PopupDialog(@NonNull Context context, ArrayList<? extends Object> arrayList, OnGenericListener onGenericListener,String status) {
        super(context);
        this.arrayList = arrayList;
        this.context = context;
        this.onGenericListener = onGenericListener;
        this.status = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_dialog);
        findView();
        setAdapter();
    }

    // adapter
    private void setAdapter() {
        genericAdapter = new GenericAdapter(getContext(), arrayList, this,status);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerFolio.setLayoutManager(linearLayoutManager);
        recyclerFolio.setAdapter(genericAdapter);
    }

    // get id here...
    private void findView() {
        recyclerFolio = findViewById(R.id.recyclerFolio);
    }

    // folio listener
    @Override
    public void onItemDeleteListener(String name,String fundCode,String status,String bankSource,String accountNumber) {
        dismiss();
        //  Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
        onGenericListener.onItemDeleteListener(name,fundCode,status,bankSource,accountNumber);
    }
}
