package com.android.task.main;

import com.android.task.R;
import com.android.task.main.function.CheckAppClientExit;
import com.android.task.main.function.MainPage;
import com.android.task.main.function.UrlConfigure;
import com.android.task.tools.UploadMessage;
import com.android.task.web.MyWebChromeClient;
import com.android.task.web.MyWebClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class WebMainActivity extends Activity {
	public  ProgressDialog ProgressDialog = null;

	private final boolean	   APP_DEBUG    = false;
	private final String TAG = WebMainActivity.class.getName();
	
	private WebView mWebView;
	private UrlConfigure mUrlConf;
	private CheckAppClientExit mExit;
	private MainPage		   mMpage;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        
        this.getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        this.setProgressBarVisibility(true);
        ProgressDialog = new ProgressDialog(this);
    	
        ProgressDialog.setCancelable(false);
        ProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


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
					Log.d(TAG,"DEBUGģʽ");
					mMpage.getMainPageDialog().show();
				}else{
			        mWebView.loadUrl(mUrlConf.getUrl());
			        Toast.makeText(WebMainActivity.this, "����...", Toast.LENGTH_LONG).show();
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
    protected void onActivityResult(int requestCode, int resultCode,  Intent intent) 
    {
    	if(requestCode== MyWebChromeClient.FILECHOOSER_IMAG_RESULTCODE)  
    	{  

    		Uri result = intent == null || resultCode != RESULT_OK ? null  : intent.getData();  
    		UploadMessage.set_upload_message(result);
    	}else if (requestCode == MyWebChromeClient.FILECHOOSER_VIDEO_RESULTCODE){
    		Uri result = intent == null || resultCode != RESULT_OK ? null  : intent.getData();  
    		UploadMessage.set_upload_message(result);
    	}

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
