package com.android.task.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Environment;

public class Tools
{
	
	// media types
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	/*
	 * create file of different media type for saving purpose
	 */
	public static File getOutputMediaFile(int type) {
		// check if have space to save
		
		// file name
		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		File mediaFile;
		if( type == MEDIA_TYPE_VIDEO ){
			
			// save to default gallery dir
			mediaFile = new File(
					Environment.getExternalStorageDirectory() + 
					File.separator + 
					"DCIM" + File.separator + 
					"Camera" + File.separator + 
					timeStamp + ".mp4");
			
			/*mediaFile = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_MOVIES) + 
							File.separator + timeStamp + ".mp4"
							);*/
		}
		else if ( type == MEDIA_TYPE_IMAGE ){
			
			// save to default gallery dir
			mediaFile = new File(
					Environment.getExternalStorageDirectory() + 
					File.separator + 
					"DCIM" + File.separator + 
					"Camera" + File.separator + 
					timeStamp + ".jpg");
			
			// save to sd card
			/*File mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES), "clientpics");
			if(!mediaStorageDir.exists()) {
				try {
					mediaStorageDir.mkdir();
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("aa", "Fail to create directory: "+e.getMessage());
					return null;
				}
			}
			
			mediaFile = new File(
					mediaStorageDir.getPath() + 
							File.separator + timeStamp + ".jpg");
			*/
			
		}
		else {
			return null;
		}
		
		return mediaFile;
	}
}
