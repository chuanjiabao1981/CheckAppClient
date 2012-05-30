package com.android.task.tools;

import java.io.IOException;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraSetting 
{
	private final static int DISPLAY_DEGREE = 90;
	private final static String TAG 		 = CameraSetting.class.getName();
	public  static void setCameraParameter(Camera camera,SurfaceHolder surface_holder) throws IOException
	{
		Log.d(TAG,"Seting Camera Parameter");
		Camera.Parameters parameters = camera.getParameters();
		camera.setDisplayOrientation(DISPLAY_DEGREE);
		parameters.setRotation(DISPLAY_DEGREE);
		camera.setParameters(parameters);
		camera.setPreviewDisplay(surface_holder);
	}
}
