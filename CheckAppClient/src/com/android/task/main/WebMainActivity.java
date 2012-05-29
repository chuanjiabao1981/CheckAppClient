package com.android.task.main;

import com.android.task.R;
import com.android.task.R.id;
import com.android.task.R.layout;
import com.android.task.picture.PhotoCapturer;
import com.android.task.tools.UploadMessage;
import com.android.task.video.VideoRecorder;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	// set function picker
	final CharSequence[] func_items = {"拍照", "摄像", "取消"};
	String myUrl; 
	protected void onActivityResult(int requestCode, int resultCode,  Intent intent) 
	{
		 System.err.println(requestCode);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);
        
        // init url
        myUrl = "http://192.168.1.103:3000/?format=mobile";
        myUrl = "http://10.32.105.241:3000/?format=mobile";
        System.err.print(myUrl);
        
        mWebView = (WebView) findViewById(R.id.main_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient(this));
        mWebView.loadUrl(myUrl);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebChromeClient(new MyWebChromeClient(this));

        
        
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
					Log.e("aa", "open function error: "+e.getMessage());
				}
				
			}
		});
        
        TextView config_text = (TextView)findViewById(R.id.main_config_text);
        config_text.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// pop config dialog to set url
				final View url_setting = getLayoutInflater().inflate(
						R.layout.url_config_layout, null);
				AlertDialog.Builder url_config = new AlertDialog.Builder(WebMainActivity.this);
				url_config.setTitle("请输入要设置的URL");
				url_config.setView(url_setting);
				url_config.setPositiveButton("确定", new OnClickListener()
				{
					public void onClick(DialogInterface dialog,
						int which)
					{
						// get custom url
						EditText url_edit = (EditText)url_setting.findViewById(R.id.url_edit);
						myUrl = url_edit.getText().toString();
						// prompt
						Toast.makeText(
								WebMainActivity.this, "已经保存URL: "+myUrl, Toast.LENGTH_LONG).show();
						Toast.makeText(
								WebMainActivity.this, "正在加载 "+myUrl, Toast.LENGTH_LONG).show();
						mWebView.loadUrl(myUrl);
						
					}
				});
				url_config.setNegativeButton("取消", new OnClickListener()
				{
					public void onClick(DialogInterface dialog,
						int which) {	
					}
				});
				url_config.show();

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
