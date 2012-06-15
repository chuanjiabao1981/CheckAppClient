package com.android.task.tools;


import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;

public class UploadMessage {

	final static String TAG = UploadMessage.class.getName();
	private static ValueCallback<Uri> mUri = null;
	
	private static Uri mFileUri = null;
	
	public static void set_upload_uri(ValueCallback<Uri> u)
	{
		UploadMessage.mUri = u;
	}
	public static void set_file_uri(Uri u)
	{
		UploadMessage.mFileUri = u;
	}
	public static Uri get_file_uri()
	{
		return UploadMessage.mFileUri;
	}
	public static void set_upload_uri(ValueCallback<Uri> u,Uri f)
	{
		UploadMessage.mUri = u;
		UploadMessage.mFileUri = f;
	}
	public static ValueCallback<Uri> get_upload_uri()
	{
		return UploadMessage.mUri;
	}
	public static void set_upload_message()
	{
		if (UploadMessage.get_upload_uri() != null)
		{
			Log.d(TAG,UploadMessage.get_file_uri()== null?"null":UploadMessage.get_file_uri().toString());
			UploadMessage.get_upload_uri().onReceiveValue(UploadMessage.get_file_uri());
		}
		UploadMessage.set_upload_uri(null);
		UploadMessage.set_file_uri(null);
	}
	public static void set_upload_message(Uri file_uri)
	{
		if (UploadMessage.get_upload_uri() != null)
		{
			Log.d(TAG,file_uri== null?"null":file_uri.toString());
			UploadMessage.get_upload_uri().onReceiveValue(file_uri);
		}
		UploadMessage.set_upload_uri(null);
		UploadMessage.set_file_uri(null);

	}


}
