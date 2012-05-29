package com.android.task.main;

import com.android.task.picture.PhotoCapturer;
import com.android.task.tools.UploadMessage;
import com.android.task.video.VideoRecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;


public class MyWebChromeClient extends WebChromeClient{
	ValueCallback<Uri> mUploadMessage = null;
	Activity		   mActivity	  = null;	
	Builder 	   	   mBuilder		  = null;
	private void init_dialog()
	{
		mBuilder.setItems(
		new String[]{"≈ƒ’’"," ”∆µ"}, 
		new OnClickListener()
		{
				public void onClick(DialogInterface dialog, int which) 
				{
					int i = -1;
					switch(which)
					{
						case 0:
							i  =0;
							break;
						case 1:
							i = 1;
							break;
						default :
							break;
					}
					if (i == 0){
						Log.i("aa","≈ƒ’’");
						Intent intent = new Intent(MyWebChromeClient.this.mActivity,PhotoCapturer.class);
						UploadMessage.set_upload_uri(mUploadMessage);
						MyWebChromeClient.this.mActivity.startActivity(intent);
					}else if (i == 1){
						Log.i("aa","…„œÒ");
						Intent intent = new Intent(MyWebChromeClient.this.mActivity, VideoRecorder.class);
						UploadMessage.set_upload_uri(mUploadMessage);
						MyWebChromeClient.this.mActivity.startActivity(intent);
					}else{
						mUploadMessage.onReceiveValue(null);
					}
				}
			}
				);

	}
	public MyWebChromeClient(Activity a)
	{
		this.mActivity = a;
		this.mBuilder  = new AlertDialog.Builder(a);
		this.init_dialog();

	}
	
	public void openFileChooser(ValueCallback<Uri> uploadMsg)
	{
		mUploadMessage = uploadMsg;
		mBuilder.create().show();
	}
}
