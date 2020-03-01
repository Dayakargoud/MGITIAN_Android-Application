package com.dayakar.mgitian.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.dayakar.mgitian.R;

public class WinnouActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar progressBar;
    private String mTitle,pageurl;
    private TextView netWorkInfo;
    private ImageView networkImage;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnnou);
        Intent intent=getIntent();
        if(intent.hasExtra("page")){
           mTitle =intent.getStringExtra("page");
           pageurl=intent.getStringExtra("pageURL");
        }
        setUpUI();
        if(isConnectedtoInternet())
                setWebView();

        
    }

    private void setUpUI(){
        netWorkInfo =findViewById(R.id.networkTextWinnou);
        networkImage=findViewById(R.id.networkImage_winnou);
        // initializing toolbar
        Toolbar mToolbar= findViewById(R.id.winnou_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mTitle);
        //setting web view and progressbar
        mWebView = findViewById(R.id.webwiew1);
        progressBar = findViewById(R.id.winnouProgressbar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

    }
    private void setWebView(){
        mWebView.setWebViewClient(new HelpClient());
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(pageurl);
        mWebView.getSettings().getAllowFileAccess();
        mWebView.getSettings().setLoadsImagesAutomatically(true);

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setScrollbarFadingEnabled(true);

        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        mWebView.setWebChromeClient(new WebChromeClient(){


            public void onProgressChanged(WebView view,int progress){
                progressBar.setProgress(progress);
                if(progress==100){
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view,progress);
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
            }
        });

    }

    private boolean isConnectedtoInternet(){

        ConnectivityManager
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            netWorkInfo.setText("No Internet connection..!");
            netWorkInfo.setVisibility(View.VISIBLE);
            networkImage.setVisibility(View.VISIBLE);
            return false;


        }
    }

    private class HelpClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;

        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}