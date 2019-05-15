package com.volive.klueapp.Utils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.volive.klueapp.R;

 public class Webview_Activity extends AppCompatActivity {

    WebView webView;
     SwipeRefreshLayout swip;
    ProgressDialog progressDialog;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        swip = findViewById(R.id.swip);
        webView = (WebView) findViewById(R.id.webview);
        // Get url from main activity
        url = getIntent().getExtras().getString("URL");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Open browser inside the App
        if (url != null || url.toString().length() != 0) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("http://www.google.com");
        }

        progressDialog = new ProgressDialog(Webview_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        });
    }

//    // Now override default browser
//    private class MyBrowser extends WebViewClient{
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url){
//            return false;
//        }
//    }

    @Override
    public void onBackPressed() {
        if(webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
