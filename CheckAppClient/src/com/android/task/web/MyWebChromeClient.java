package com.android.task.web;


import com.android.task.main.WebMainActivity;
import com.android.task.picture.PhotoCapturer;
import com.android.task.tools.UploadMessage;
import com.android.task.video.VideoRecorder;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;


public class MyWebChromeClient extends WebChromeClient{
	ValueCallback<Uri> mUploadMessage = null;
	WebMainActivity	   mActivity	  = null;	
	Builder 	   	   mBuilder		  = null;
	private final String	TAG		  = MyWebChromeClient.class.getName();
	public  final static int       FILECHOOSER_IMAG_RESULTCODE 		= 101;
	public  final static int       FILECHOOSER_VIDEO_RESULTCODE 	= 201;
	public  final static int       CAPTURE_PICTURE_INTENT			= 301;
	public  final static int       CAPTURE_VIDEO_INTENT				= 401;
	final CharSequence[] func_items   = {"现场拍照", "现场摄像", "选取照片","取消"};
	final String         DIALOG_TITLE = "选择功能";

	private void init_dialog()
	{
		mBuilder.setTitle(DIALOG_TITLE);
		mBuilder.setOnKeyListener(
				new OnKeyListener(){

					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						
						UploadMessage.set_upload_message(null);
						dialog.dismiss();
						return true;
					}
					
				}
		);
		mBuilder.setItems(
		func_items, 
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
						case 2:
							i = 2;
							break;
						default :
							break;
					}
					if (i == 0){
						Log.i(TAG,"拍照");
						/*
						Intent intent = new Intent(MyWebChromeClient.this.mActivity,PhotoCapturer.class);
						UploadMessage.set_upload_uri(mUploadMessage);
						MyWebChromeClient.this.mActivity.startActivity(intent);
						*/
						
				        String fileName = "temp.jpg";  

				        ContentValues values = new ContentValues();  

				        values.put(MediaStore.Images.Media.TITLE, fileName); 

				        Uri mCapturedImageURI = MyWebChromeClient.this.mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  

				        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  

				        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);  

				        MyWebChromeClient.this.mActivity.startActivityForResult(intent, CAPTURE_PICTURE_INTENT);

						UploadMessage.set_upload_uri(mUploadMessage,mCapturedImageURI);


					}else if (i == 1){
						Log.i(TAG,"摄像");
						/*
						Intent intent = new Intent(MyWebChromeClient.this.mActivity, VideoRecorder.class);
						UploadMessage.set_upload_uri(mUploadMessage);
						MyWebChromeClient.this.mActivity.startActivity(intent);
						*/
						
						/*
						String fileName ="temp.3gp";
						ContentValues values = new ContentValues();  
						values.put(MediaStore.Video.Media.TITLE, fileName); 
						Uri cameraVideoURI = MyWebChromeClient.this.mActivity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);  
						*/

				        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); 
//				        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraVideoURI);  
						intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//						intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10);   
						intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 8);
						MyWebChromeClient.this.mActivity.startActivityForResult(intent, CAPTURE_VIDEO_INTENT);
						UploadMessage.set_upload_uri(mUploadMessage);

				        
					}else if (i == 2) {
						Log.i(TAG,"选择照片");
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
						intent.addCategory(Intent.CATEGORY_OPENABLE);  
						intent.setType("image/*");  
						UploadMessage.set_upload_uri(mUploadMessage);
						MyWebChromeClient.this.mActivity.startActivityForResult(Intent.createChooser(intent,"File Chooser"), FILECHOOSER_IMAG_RESULTCODE);
					}else if(i == 3){
						Log.i(TAG,"选取视频");
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE); 
						intent.setType("video/*");  
						UploadMessage.set_upload_uri(mUploadMessage);
						MyWebChromeClient.this.mActivity.startActivityForResult(Intent.createChooser(intent,"File Chooser"), FILECHOOSER_VIDEO_RESULTCODE);
						
					}else{
						mUploadMessage.onReceiveValue(null);
					}
				}
			}
				);

	}
	public MyWebChromeClient(WebMainActivity a)
	{
		this.mActivity = a;
		this.mBuilder  = new AlertDialog.Builder(a);
		this.init_dialog();

	}
	public void onProgressChanged(WebView view, int progress)   
    {
		this.mActivity.ProgressDialog.show();
		
		this.mActivity.ProgressDialog.setProgress(0);
		this.mActivity.ProgressDialog.incrementProgressBy(progress);
		//this.mActivity.ProgressDialog.i
		if(progress == 100 && this.mActivity.ProgressDialog.isShowing())
			this.mActivity.ProgressDialog.dismiss();
    }
	
	public void openFileChooser(ValueCallback<Uri> uploadMsg)
	{
		mUploadMessage = uploadMsg;
		UploadMessage.set_upload_uri(mUploadMessage);
		mBuilder.create().show();
	}
}
