package com.android.task.main.function;

import com.android.task.picture.PhotoCapturer;
import com.android.task.video.VideoRecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

public class MainPage {
	private final CharSequence[] FUNCTION_ITEMS = {"拍照", "摄像", "取消"};
	private final String TAG				    = MainPage.class.getName();
	private final String TITLE					= "选择功能";
	private Activity mA;
	private AlertDialog mMainPageDialog;

	
	public MainPage(Activity a)
	{
		this.mA = a;
		init();
	}
	public AlertDialog getMainPageDialog() 
	{
		return mMainPageDialog;
	}
	private void init()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.mA);
		builder.setTitle(TITLE);
		builder.setItems(FUNCTION_ITEMS, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Intent i;
				switch (item) {
				case 0:
					i = new Intent(MainPage.this.mA, PhotoCapturer.class);
					MainPage.this.mA.startActivity(i);
					Log.d(TAG,"Photo");
					break;
				case 1:
					i = new Intent(MainPage.this.mA, VideoRecorder.class);
					MainPage.this.mA.startActivity(i);
					Log.d(TAG,"Video");
					break;
				case 2:
				default:
					break;
				}
			};
		});
		mMainPageDialog = builder.create();
	}

}
