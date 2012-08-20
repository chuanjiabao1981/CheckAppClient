package com.android.task.main.function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.android.task.tools.EquipmentId;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class SysUpdate 
{
	private final String TAG			 				= SysUpdate.class.getName();
	private final String LOCAL_PACKAGE_FILE				= ".checkapp.apk";
	private final String REMOTE_PACKAGE_FILE			= "check.apk";
	private Activity     a					= null;
	private UrlConfigure mUrlConfigure 		= null;
	private EquipmentId	mEquipmentId		= null;
	
	public SysUpdate(Activity a,UrlConfigure u)
	{
		this.mUrlConfigure			= 		u;
		this.a						=       a;
		this.mEquipmentId			=       new EquipmentId(a);
	}
	
	public void update()
	{
		if (this.downloadNewPackage()){
			this.installPackage();
			Toast.makeText(this.a, "安装新版本成功！", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(this.a, "安装新版本失败！", Toast.LENGTH_SHORT).show();

	}
	
	private boolean downloadNewPackage() 
	{
		String packageUrl				 	= this.getNewPackageUrl();
		HttpURLConnection urlConnection 	= null;
        URL url;
		try {
			url = new URL(packageUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);
	        urlConnection.connect();
	        File SDCardRoot = Environment.getExternalStorageDirectory();
	        File file = new File(SDCardRoot,LOCAL_PACKAGE_FILE);
	        FileOutputStream fileOutput = new FileOutputStream(file);
	        InputStream inputStream = urlConnection.getInputStream();

	        int totalSize = urlConnection.getContentLength();
	        Log.d(TAG, "Total size" + totalSize);
	        int downloadedSize = 0;

	        byte[] buffer = new byte[1024*10];
	        int bufferLength = 0; //used to store a temporary size of the buffer
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
    	        Log.d(TAG, "Download size" + downloadedSize);
    	        
    			Toast.makeText(this.a, "程序更新中，请稍后...", Toast.LENGTH_SHORT).show();

//    			Toast.makeText(this.a, String.valueOf(downloadedSize/(float)totalSize * 100) + "%", Toast.LENGTH_SHORT).show();
	        }
        fileOutput.close();

		} catch (MalformedURLException e) {
			Log.e(TAG,"package update error ! "+ packageUrl );
			Toast.makeText(this.a, "更新地址有误！", Toast.LENGTH_LONG).show();
			return false;
		}  catch (IOException e) {
			Log.e(TAG,"net work error!");
			Toast.makeText(this.a, "连接服务器失败！", Toast.LENGTH_LONG).show();
			return false;
		}
		Log.d(TAG,"DownLoad Success!");
		return true;
	}
	private void installPackage()
	{
		File apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LOCAL_PACKAGE_FILE);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		this.a.startActivity(intent);
	}
	private String getNewPackageUrl()
	{
		return "http://"+mUrlConfigure.getHost() 			+ "/" +
				this.mEquipmentId.getAndroidVersion() 		+ "/" +
				REMOTE_PACKAGE_FILE;
	}
}
