package com.android.task.main.function;


import com.android.task.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class UrlConfigure {
	private final String URL_PREFER_NAME 			= "CHECK_APP_PRE";
	private final String URL_PREFER_ITEM_URL		= "SERVER_URL";
	private final String URL_PREFER_ITEM_URL_DEFAULT = "42.121.13.219";
	private final String URL_TITLE		 = "输入服务器地址";
	private final String TAG			 = UrlConfigure.class.getName();
	private Activity mA;
	private View mUrlSettingView;
	private WebView mWebView;
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;
	private AlertDialog.Builder mUrlConfBuilder;
	private AlertDialog mUrlDialog;
	
	
	

	

	public UrlConfigure(Activity a,WebView w)
	{
		this.mA = a;
		this.mWebView = w;
		this.init();
	}
	
	public AlertDialog getUrlDialog() {
		return mUrlDialog;
	}
	public String getUrl()
	{
		String url = "http://"+this.mPreferences.getString(URL_PREFER_ITEM_URL, URL_PREFER_ITEM_URL_DEFAULT)+"/?format=mobile";
		Log.d(TAG,"get url\t"+url);
		return url;
	}
	
	private void init()
	{
		this.mUrlConfBuilder    = new AlertDialog.Builder(this.mA);
		this.mUrlSettingView 	= this.mA.getLayoutInflater().inflate(R.layout.url_config_layout, null);
		this.mPreferences 		= this.mA.getSharedPreferences(URL_PREFER_NAME, android.content.Context.MODE_WORLD_WRITEABLE);
		this.mEditor 			= this.mPreferences.edit();
		this.mUrlConfBuilder.setView(this.mUrlSettingView);
		this.mUrlConfBuilder.setTitle(URL_TITLE);
		
		EditText url_edit = (EditText)this.mUrlSettingView.findViewById(R.id.url_edit);
		url_edit.setText(this.mPreferences.getString(URL_PREFER_ITEM_URL, URL_PREFER_ITEM_URL_DEFAULT));
		
		
		mUrlConfBuilder.setPositiveButton("确定", new OnClickListener()
		{
			public void onClick(DialogInterface dialog,
				int which)
			{
				EditText url_edit = (EditText)UrlConfigure.this.mUrlSettingView.findViewById(R.id.url_edit);
				String myUrl = url_edit.getText().toString();
				Toast.makeText(UrlConfigure.this.mA, "已经保存服务器地址: "+myUrl, Toast.LENGTH_LONG).show();
				Toast.makeText(UrlConfigure.this.mA, "正在加载...", Toast.LENGTH_LONG).show();
				UrlConfigure.this.mEditor.putString(URL_PREFER_ITEM_URL, myUrl);
				UrlConfigure.this.mEditor.commit();
				Log.d(TAG,"LOAD URL\t"+UrlConfigure.this.getUrl());
				UrlConfigure.this.mWebView.loadUrl(UrlConfigure.this.getUrl());
			}
		});
		mUrlConfBuilder.setNegativeButton("取消", new OnClickListener()
		{
			public void onClick(DialogInterface dialog,
				int which) {
			}
		});
		this.mUrlDialog = this.mUrlConfBuilder.create();
	}

}
