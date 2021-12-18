
package com.bob.bobapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.activities.DematHoldingActivity;
import com.bob.bobapp.activities.DiscoverFundsActivity;
import com.bob.bobapp.activities.InvestmentCartActivity;
import com.bob.bobapp.activities.OrderStatusActivity;
import com.bob.bobapp.activities.StopSIPActivity;
import com.bob.bobapp.adapters.AddTransactListAdapter;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.listener.onSortItemListener;
import com.bob.bobapp.utility.BMSPrefs;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.PopUpClass;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;


public class AddTransactionFragment extends BaseFragment implements onSortItemListener {
    private RecyclerView rv;

    private AddTransactListAdapter adapter;

    private EditText etSearch;

    private String searchKey = "";

    private ArrayList<ClientHoldingObject> clientHoldingObjectArrayList;

    private Context context;
    private TextView txtSearch, txtExistingHolding;
    private String portStatus;

    private int screenWidth = 0, screenHeight = 0;

    int DRAWER_ITEMS_OPEN_TIME = 200;

    private LinearLayout drawerMenuView;

    private DrawerLayout drawerLayout;

    private Util util;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        util = new Util(context);

        return inflater.inflate(R.layout.fragment_add_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        String response = SettingPreferences.getHoldingResponse(context);

        Type listType = new TypeToken<List<ClientHoldingObject>>() {
        }.getType();

        clientHoldingObjectArrayList = new Gson().fromJson(response, listType);


    }

    @Override
    protected void getIds(View view) {
        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    BOBActivity.mTabHost.setCurrentTab(0);
                }

                return true;
            }
        });


        rv = view.findViewById(R.id.rvTransaction);

        etSearch = view.findViewById(R.id.etSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtExistingHolding = view.findViewById(R.id.txtExistingHolding);

//        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//
//        drawerMenuView = (LinearLayout) view.findViewById(R.id.drawerMenuLLayout);
    }

    @Override
    protected void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);

        txtSearch.setOnClickListener(this);

        txtExistingHolding.setOnClickListener(this);

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
        try {
            adapter.updateList(filteredList);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    protected void initializations() {
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.VISIBLE);
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.VISIBLE);

        BOBActivity.imageViewLogo.setVisibility(View.VISIBLE);
        BOBActivity.llMenu.setVisibility(View.VISIBLE);

        BOBActivity.title.setVisibility(View.GONE);
        BOBActivity.imgBack.setVisibility(View.GONE);

        BOBActivity.llMenu.setOnClickListener(this);
        BOBActivity.tvCartHeader.setOnClickListener(this);
        //   BOBActivity.llMenu.setVisibility(View.GONE);

        BOBActivity.title.setText("Invest Now");

        //  manageLeftSideDrawer();

    }

    @Override
    protected void setIcon(Util util) {

    }

    private void setAdapter() {

        adapter = new AddTransactListAdapter(getActivity(), clientHoldingObjectArrayList, "2") {

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

            getActivity().onBackPressed();
            //   BOBActivity.mTabHost.setCurrentTab(0);
        }

        if (id == R.id.txtExistingHolding)
        {
            rv.setVisibility(View.VISIBLE);
            setAdapter();
        }

        if (id == R.id.txtSearch) {

            DiscoverFundsActivity fragment = new DiscoverFundsActivity();

            Bundle bundle = new Bundle();

            bundle.putString("exploreName", "Equity Funds");

            fragment.setArguments(bundle);

            replaceFragment(fragment);
            //  replaceFragment(new DiscoverFundsActivity());
        }


        if (id == R.id.llMenu)
        {
            PopUpClass popUpClass = new PopUpClass();
            popUpClass.showPopupWindow(view, getContext(), this);
            //   menuButton();
        }

        if (id == R.id.tvCartHeader) {
            InvestmentCartActivity fragment = new InvestmentCartActivity();

            Bundle bundle = new Bundle();

            bundle.putString(Constants.COMING_FROMS, "Dashboard");

            fragment.setArguments(bundle);

            replaceFragment(fragment);
        }
    }


    @Override
    public void onStop() {

        super.onStop();

        BOBActivity.imageViewLogo.setVisibility(View.GONE);

        BOBActivity.title.setVisibility(View.VISIBLE);

        BOBActivity.imgBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        BOBActivity.imageViewLogo.setVisibility(View.VISIBLE);

        BOBActivity.title.setVisibility(View.GONE);

        BOBActivity.imgBack.setVisibility(View.GONE);

    }


//    public void menuButton() {
//
//        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//
//            drawerLayout.closeDrawer(Gravity.RIGHT);
//
//        } else {
//
//            drawerLayout.openDrawer(Gravity.RIGHT);
//        }
//    }


    public void manageLeftSideDrawer() {

        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();

        manager.getDefaultDisplay().getMetrics(metrics);

        screenWidth = metrics.widthPixels;

        screenHeight = metrics.heightPixels;

        View leftSideDrawerView = LayoutInflater.from(context).inflate(R.layout.left_side_drawer_layout, null);

        leftSideDrawerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        ImageView close = (ImageView) leftSideDrawerView.findViewById(R.id.close);

        ImageView imgIcon = (ImageView) leftSideDrawerView.findViewById(R.id.imgIcon);

        TextView home = leftSideDrawerView.findViewById(R.id.home);

        TextView dashboard = leftSideDrawerView.findViewById(R.id.dashboard);

        TextView portFolio = leftSideDrawerView.findViewById(R.id.portFolio);

        TextView report = leftSideDrawerView.findViewById(R.id.report);

        TextView transact = leftSideDrawerView.findViewById(R.id.transact);

        TextView orderStatus = leftSideDrawerView.findViewById(R.id.orderStatus);

        TextView dematHolding = leftSideDrawerView.findViewById(R.id.dematHolding);

        TextView stopSIP = leftSideDrawerView.findViewById(R.id.stopSIP);

        TextView setup = leftSideDrawerView.findViewById(R.id.setup);

        imgIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(0);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(0);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });


        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        util.alertboxExitFromApp("Alert!", "Are you sure? Do you want to exit from app?");
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(0);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        portFolio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(1);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new ReportFragment());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        transact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        BOBActivity.mTabHost.setCurrentTab(2);
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        orderStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new OrderStatusActivity());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        dematHolding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new DematHoldingActivity());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        stopSIP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new StopSIPActivity());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        setup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawer(Gravity.RIGHT);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        replaceFragment(new SetUpFragment());
                    }

                }, DRAWER_ITEMS_OPEN_TIME);
            }
        });

        drawerMenuView.addView(leftSideDrawerView);
    }

    @Override
    public void onSortItemListener(String name) {
        if (name.equalsIgnoreCase("home")) {
            // BOBActivity.mTabHost.setCurrentTab(0);
            Toast.makeText(getContext(), "This functionality will be available post-integration", Toast.LENGTH_LONG).show();
        }

        if (name.equalsIgnoreCase("profile")) {
            replaceFragment(new ProfileFragment());
        }

        if (name.equalsIgnoreCase("stop sip")) {
            replaceFragment(new StopSIPActivity());
        }

        if (name.equalsIgnoreCase("demat holding")) {
            replaceFragment(new DematHoldingActivity());
        }


    }
}
