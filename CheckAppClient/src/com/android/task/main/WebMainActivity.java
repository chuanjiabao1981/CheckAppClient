package com.android.task.main;

import com.android.task.R;
import com.android.task.R.id;
import com.android.task.R.layout;
import com.android.task.main.function.UrlConfigure;
import com.android.task.picture.PhotoCapturer;
import com.android.task.tools.UploadMessage;
import com.android.task.video.VideoRecorder;
import com.android.task.web.MyWebChromeClient;
import com.android.task.web.MyWebClient;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.app.Dialog;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WebMainActivity extends Activity {
	
	private WebView mWebView;
	private UrlConfigure mUrlConf;
	// set function picker
	final CharSequence[] func_items = {"拍照", "摄像", "取消"};
	final String TAG = WebMainActivity.class.getName();
	String myUrl; 
	protected void onActivityResult(int requestCode, int resultCode,  Intent intent) 
	{
		 System.err.println(requestCode);
	}
	
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

        
        // set button click handler
        TextView menu_text = (TextView)findViewById(R.id.main_menu_text);
        menu_text.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 // popup a list view to choose functions
					AlertDialog.Builder builder = new AlertDialog.Builder(WebMainActivity.this);
					builder.setTitle("选择功能");
					builder.setItems(func_items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							Intent i;
							switch (item) {
							case 0:
								i = new Intent(WebMainActivity.this, PhotoCapturer.class);
								startActivity(i);
								break;
							case 1:
								i = new Intent(WebMainActivity.this, VideoRecorder.class);
								startActivity(i);
								break;
							case 2:
							default:
								break;
							}
						};
					});
					builder.show();
					
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, "open function error: "+e.getMessage());
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
				// pop a confirmation dialog
				AlertDialog.Builder exit_diag = new AlertDialog.Builder(WebMainActivity.this);
				exit_diag.setTitle("确定要退出吗");
				exit_diag.setPositiveButton("确定", new OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
							WebMainActivity.this.finish();
							return;
						}
				});
				exit_diag.setNegativeButton("取消", new OnClickListener()
				{
					public void onClick(DialogInterface dialog,
						int which) {	
					}
				});
				exit_diag.show();
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
