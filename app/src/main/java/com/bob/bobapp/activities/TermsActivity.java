package com.bob.bobapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bob.bobapp.R;

public class TermsActivity extends AppCompatActivity {
    private WebView webview;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        webview = findViewById(R.id.webview);
        imgBack = findViewById(R.id.imgBack);
        webview.setWebViewClient(new MyWebViewClient());

        String ENROLLMENT_URL = "https://wmsuat.bankofbaroda.co.in/MoneywarePortal/Portal/TnC/TnC.html";

        webview.loadUrl(ENROLLMENT_URL);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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