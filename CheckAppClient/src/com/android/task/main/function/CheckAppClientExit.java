package com.android.task.main.function;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

public class CheckAppClientExit {
	private final String TAG 	= CheckAppClientExit.class.getName();
	private final String TITLE	= "确定要退出吗?";
	private Activity mA;
	private AlertDialog mCheckAppExitDialog;
	
	
	public CheckAppClientExit(Activity a)
	{
		this.mA = a;
		init();
	}
	public AlertDialog getCheckAppExitDialog() 
	{
		return mCheckAppExitDialog;
	}
	private void init()
	{
		AlertDialog.Builder exit_diag = new AlertDialog.Builder(this.mA);
		exit_diag.setTitle(TITLE);
		exit_diag.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {
					CheckAppClientExit.this.mA.finish();
					Log.d(TAG,"exit!");
					return;
				}
		});
		exit_diag.setNegativeButton("取消", new OnClickListener()
		{
			public void onClick(DialogInterface dialog,
				int which) {	
			}
		});
		mCheckAppExitDialog = exit_diag.create();
	}
}
