package com.bob.bobapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bob.bobapp.R;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Util;

public class InformationActivity extends BaseFragment
{
    private Util util;
    private Context context;
    private WebView webview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void getIds(View view) {
        webview=view.findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebViewClient());

        String ENROLLMENT_URL = "https://barodawealth.com/MoneywarePortal/Portal/TnC/TnC1.html";
        webview.loadUrl(ENROLLMENT_URL);
    }


    @Override
    protected void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        BOBActivity.mTabHost.getTabWidget().setVisibility(View.GONE);
        BOBActivity.imgInfo.setVisibility(View.GONE);
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.tvCartHeader.setVisibility(View.GONE);
        BOBActivity.title.setText("Information");
    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.imgBack)
        {
            getActivity().onBackPressed();
        }

    }

    public final class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Intent i = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                i = new Intent(Intent.ACTION_VIEW, request.getUrl());
            }
            startActivity(i);
            return true;
        }
    }

}
