package com.bob.bobapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.AddTransactListAdapter;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.fragments.QuickTransactionFragment;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExistingPortfolioActivity extends BaseFragment {
    private RecyclerView rv;

    private AddTransactListAdapter adapter;

    private EditText etSearch;

    private String searchKey = "";

    private ArrayList<ClientHoldingObject> clientHoldingObjectArrayList;

    private Context context;
    private TextView txtSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        return inflater.inflate(R.layout.fragment_existing_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        String response = SettingPreferences.getHoldingResponse(context);

        Type listType = new TypeToken<List<ClientHoldingObject>>() {
        }.getType();

        clientHoldingObjectArrayList = new Gson().fromJson(response, listType);


        setAdapter();
    }

    @Override
    protected void getIds(View view) {
//        view.setFocusableInTouchMode(true);
//
//        view.requestFocus();
//
//        view.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    BOBActivity.mTabHost.setCurrentTab(0);
//                }
//
//                return true;
//            }
//        });


        rv = view.findViewById(R.id.rvTransaction);

        etSearch = view.findViewById(R.id.etSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
    }

    @Override
    protected void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);
        txtSearch.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKey = etSearch.getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });
    }

    private void filter(String text) {

        ArrayList<ClientHoldingObject> filteredList = new ArrayList<>();

        for (ClientHoldingObject item : clientHoldingObjectArrayList) {

            if (item.getSchemeName() != null) {

                if (item.getSchemeName().toLowerCase().startsWith(text.toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }

        adapter.updateList(filteredList);
    }


    @Override
    protected void initializations() {

        BOBActivity.llMenu.setVisibility(View.GONE);

        BOBActivity.title.setText("Invest Now");

    }

    @Override
    protected void setIcon(Util util) {

    }

    private void setAdapter() {

        adapter = new AddTransactListAdapter(getActivity(), clientHoldingObjectArrayList, "1") {

            @Override
            public void getDetail(Fragment fragment) {

                replaceFragment(fragment);
            }
        };

        rv.setAdapter(adapter);
    }

    public void replaceFragment(Fragment fragment) {

        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.imgBack) {
//            BOBActivity.mTabHost.setCurrentTab(0);
            getActivity().onBackPressed();
        }
        if (id == R.id.txtSearch) {
            replaceFragment(new QuickTransactionFragment());
        }
    }

}
