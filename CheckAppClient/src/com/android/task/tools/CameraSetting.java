package com.android.task.tools;

import java.io.IOException;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraSetting 
{
	private final static int DISPLAY_DEGREE = 90;
	private final static String TAG 		 = CameraSetting.class.getName();
	private final static int PIC_WIDTH		= 800;
	private final static int PIC_HEIGHT	    = 800;
	public  static void setCameraPicParameter(Camera camera,SurfaceHolder surface_holder) throws IOException
	{
		Log.d(TAG,"Seting Camera Parameter");
		Camera.Parameters parameters = camera.getParameters();
		camera.setDisplayOrientation(DISPLAY_DEGREE);
        parameters.setPictureSize(PIC_WIDTH, PIC_HEIGHT);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		camera.setParameters(parameters);
		camera.setPreviewDisplay(surface_holder);
	}
}
