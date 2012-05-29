package com.android.task.main;

import com.android.task.R;
import com.android.task.main.function.CheckAppClientExit;
import com.android.task.main.function.MainPage;
import com.android.task.main.function.UrlConfigure;
import com.android.task.web.MyWebChromeClient;
import com.android.task.web.MyWebClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class WebMainActivity extends Activity {
	private final boolean	   APP_DEBUG    = false;
	private final String TAG = WebMainActivity.class.getName();

	private WebView mWebView;
	private UrlConfigure mUrlConf;
	private CheckAppClientExit mExit;
	private MainPage		   mMpage;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);
        
        
        mWebView = (WebView) findViewById(R.id.main_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebChromeClient(new MyWebChromeClient(this));
        mWebView.setWebViewClient(new MyWebClient(this));
        
        
        mUrlConf = new UrlConfigure(this,this.mWebView);
        
        
        mWebView.loadUrl(mUrlConf.getUrl());
        
        mExit	= new CheckAppClientExit(this);
        mMpage  = new MainPage(this);

        
        TextView menu_text = (TextView)findViewById(R.id.main_menu_text);
        menu_text.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if (WebMainActivity.this.APP_DEBUG){
					Log.d(TAG,"DEBUGÄ£Ê½");
					mMpage.getMainPageDialog().show();
				}else{
			        mWebView.loadUrl(mUrlConf.getUrl());
			        Toast.makeText(WebMainActivity.this, "·µ»Ø...", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        TextView config_text = (TextView)findViewById(R.id.main_config_text);
        config_text.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				mUrlConf.getUrlDialog().show();
			}
		});
        
        TextView exit_text = (TextView)findViewById(R.id.main_exit_text);
        exit_text.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				mExit.getCheckAppExitDialog().show();
			}
		});
        
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {  
    		mWebView.goBack();       
    		return true;
    		}    
    	return super.onKeyDown(keyCode, event);
    	}
}
