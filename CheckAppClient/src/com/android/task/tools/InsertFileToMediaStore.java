package com.android.task.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class InsertFileToMediaStore {
	private File 					mMediaFile 		 = null;
	private Bitmap					mBitMap 		 = null;
	private ContentResolver			mContentResolver = null; 
	private String                  mMimeType	     = null;
	private final String		    SPLITOR			 = "/";
	private boolean					mVideo			 = false;
	private boolean					mImage			 = false;
	private final String 			TAG				 = "InsertFileToMediaStore";
	private Activity				a				 = null;
	public InsertFileToMediaStore(Activity a,File media_file,String mime_type)
	{
		this.a						= a;
		this.mMediaFile 			= media_file;
		init(a,mime_type);
		
	}
	public InsertFileToMediaStore(Activity a,Bitmap b,String mime_type)
	{
		this.a							= a;
		this.mBitMap 			        = b;
		init(a,mime_type);
		
	}
	
	
	private void init(Activity a,String mime_type)
	{
		this.mContentResolver		= a.getContentResolver();
		this.mMimeType				= mime_type;
		if (this.mMimeType.split(SPLITOR)[0].equalsIgnoreCase("video")){
			this.mVideo 	=	true;
		}else if (this.mMimeType.split(SPLITOR)[0].equalsIgnoreCase("image")){
			this.mImage     = true;
		}
	}
	
	public Uri insert()
	{
		Uri content_uri = null;
		ContentValues values = new ContentValues();
		if (this.mVideo){
			values.put(MediaStore.Video.Media.MIME_TYPE, this.mMimeType);
			content_uri = this.mContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
			
		}else if (this.mImage){
			values.put(MediaStore.Images.Media.MIME_TYPE,this.mMimeType);
			content_uri = this.mContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		}
		try {
			BufferedOutputStream os;
			os = new BufferedOutputStream(this.mContentResolver.openOutputStream(content_uri));
			BufferedInputStream 	is  = null;
			if (this.mMediaFile != null){
				Log.d(TAG,"处理文本输入");

				is = new BufferedInputStream(new FileInputStream(this.mMediaFile));
				int byte_;
			    while ((byte_ = is.read()) != -1)
			    	os.write(byte_);

			    os.flush();
			    os.close();
			    is.close();
			}else if (this.mBitMap != null){
				Log.d(TAG,"处理 BitMap输入");
				OutputStream os2 = null;
				os2 = this.mContentResolver.openOutputStream(content_uri);
				this.mBitMap.compress(Bitmap.CompressFormat.JPEG, 85, os2);
				os2.flush();
				os2.close();
			}else{
				Log.e(TAG,"插入文件到media 中发生错误");
				Toast.makeText(this.a, "插入数据时发生错误，请重试！", Toast.LENGTH_LONG).show();
				return null;
			}
				
		    //this.a.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, content_uri));
		    return content_uri;
		} catch (FileNotFoundException e) {
			Log.e(this.TAG,e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e(this.TAG,e.getMessage());
			return null;
		}
	}

}
