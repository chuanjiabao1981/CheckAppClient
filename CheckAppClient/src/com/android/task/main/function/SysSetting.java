package com.android.task.main.function;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.WebView;


public class SysSetting {
	private final String TAG 					= SysSetting.class.getName();
	private final String TITLE					= "系统设置";
	private final CharSequence[] FUNCTION_ITEMS = {"云服务器", "设备编码", "系统升级", "取消"};


	private Activity mA;
	private WebView mWebView;
	private AlertDialog mSettingDialog;
	private UrlConfigure mUrlConf;
	private IdShow mIdShow;
	private SysUpdate mSysupdate;


	

	public SysSetting(Activity a,WebView w)
	{
		this.mA 			= a;
		this.mWebView		= w;
		this.mUrlConf		= new UrlConfigure(a,this.mWebView);
		this.mIdShow		= new IdShow(a);
		this.mSysupdate		= new SysUpdate(a,this.mUrlConf);
		this.init();
	}
	
	public AlertDialog getSettingDialog() {
		return mSettingDialog;
	}
	
	private void init()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.mA);
		builder.setTitle(TITLE);
		builder.setItems(FUNCTION_ITEMS, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					Log.d(TAG,"云服务器");
					SysSetting.this.mUrlConf.getUrlDialog().show();
					break;
				case 1:
					Log.d(TAG,"设备编码");
					SysSetting.this.mIdShow.getIdDialog().show();
					break;
				case 2:
					Log.d(TAG,"系统更新");
					SysSetting.this.mSysupdate.update();
					break;
				case 3:
					Log.d(TAG,"取消");
				default:
					break;
				}
			};
		});
		mSettingDialog = builder.create();
	}
}
