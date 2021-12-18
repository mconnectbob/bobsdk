package com.bob.bobapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.adapters.FilterAdapter;
import com.bob.bobapp.adapters.GenericAdapter;
import com.bob.bobapp.listener.OnGenericListener;

import java.util.ArrayList;

public class FilterDialog extends Dialog implements OnGenericListener {
    private Context context;
    private ArrayList<? extends Object> arrayList;
    private RecyclerView recyclerFolio;
    private String status;
    private FilterAdapter filterAdapter;
    private OnGenericListener onGenericListener;

    public FilterDialog(@NonNull Context context, ArrayList<? extends Object> arrayList, String status,OnGenericListener onGenericListener) {
        super(context);
        this.arrayList = arrayList;
        this.context = context;
        this.status = status;
        this.onGenericListener = onGenericListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_filter_dialog);
        findView();
        setAdapter();
    }

    // findView by id
    private void findView() {
        recyclerFolio=findViewById(R.id.recyclerFolio);
    }


    // set adapter
    private void setAdapter()
    {
        filterAdapter = new FilterAdapter(getContext(), arrayList, status,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerFolio.setLayoutManager(linearLayoutManager);
        recyclerFolio.setAdapter(filterAdapter);
    }

    @Override
    public void onItemDeleteListener(String name, String fundCode, String status,String bankSource,String accountNumber) {
        dismiss();
        onGenericListener.onItemDeleteListener(name,fundCode,status,"","");
    }
}
