package com.android.task.video;


import com.android.task.tools.*;

import java.io.File;
import java.io.IOException;

import com.android.task.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class VideoRecorder extends Activity
{

	// size
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	// 定义系统所用的照相机
	Camera camera;
	// recorder
	MediaRecorder vRecorder;
	// max video duration (milliseconds)
	int max_video_duration = 10000;	// 5 min
	// save video path
	File save_vid_file;
	//是否在浏览中
	boolean isPreview = false;
	// if recording
	boolean isRecording = false;
	// left recording time
	int left_time = max_video_duration;
	final static int VIDEO_SHOW_REQUEST = 100;
	
	final  int VIDEO_WIDTH 	= 320;
	final  int VIDEO_HEIGHT	= 240;
	
	
	
	// recording timer
	private Handler handler = new Handler();
	private Runnable task = new Runnable()
	{
		public void run()
		{
			if (isRecording)
			{
				left_time -= 1000;  
				TextView vid_timer = (TextView)findViewById(R.id.vid_info_textview);
				vid_timer.setText("剩余时间: "+timeToString(left_time));
				
				if(left_time <= 0) {
					// 提示用户
					Toast.makeText(VideoRecorder.this, "超过录制时间限制", Toast.LENGTH_SHORT).show();
		        	StopRecording();
				}
				else {
					handler.postDelayed(this, 1000);
				}
			 }
		}
	};
	
	 @Override  
	 protected void onActivityResult(int requestCode, int resultCode,  Intent intent) 
	 {
	  if(requestCode==VIDEO_SHOW_REQUEST && resultCode == VideoPreviewer.VIDEO_IS_OK)  
	  {  
		  InsertFileToMediaStore insert_file = new InsertFileToMediaStore(VideoRecorder.this,this.save_vid_file,"video/mp4");

		  Uri uri = insert_file.insert();
		  if (this.save_vid_file != null)
			  this.save_vid_file.delete();
		  UploadMessage.set_upload_message(uri);
		  finish();
	  }
	  if (requestCode==VIDEO_SHOW_REQUEST && resultCode == VideoPreviewer.VIDEO_IS_NOT_OK)
	  {
		  Toast.makeText(VideoRecorder.this, "没得到结果", Toast.LENGTH_SHORT).show();
		  //do something
	  }
	 }
	
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam_vid_layout);
		// 设置全屏
		/*
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		WindowManager wm = (WindowManager) getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 获取屏幕的宽和高
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();*/
		// 获取界面中SurfaceView组件
		sView = (SurfaceView) findViewById(R.id.vid_view);
		// 获得SurfaceView的SurfaceHolder
		surfaceHolder = sView.getHolder();
		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback()
		{
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height)
			{
				Log.i("aa", "surface changed");
				surfaceHolder = holder;
			}

			public void surfaceCreated(SurfaceHolder holder)
			{	
				Log.d("aa", "surface created");
				
				// reset time
				left_time = max_video_duration;
				// reset caption info
				TextView vid_timer = (TextView)findViewById(R.id.vid_info_textview);
				vid_timer.setText("预览中");
				
				// 打开摄像头
				initCamera();
			}
			
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				Log.e("aa","surface destroyed.");

				// 如果camera不为null, 释放摄像头
				if (camera != null)
				{
					if (isPreview)
					{
						camera.stopPreview();
						isPreview = false;
					}
					camera.release();
					Log.e("aa", "camera released in surfacedestroyed");
					camera = null;
					Toast.makeText(VideoRecorder.this, "得到结果了2", Toast.LENGTH_SHORT).show();

				}
			}
		});
		// 设置该SurfaceView自己不维护缓冲 
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.setFixedSize(this.VIDEO_WIDTH, this.VIDEO_HEIGHT);
		// set button event
		final Button recBtn = (Button) findViewById(R.id.vid_rec_btn);
		recBtn.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				
				if(isRecording) {
					StopRecording();
				}
				else
				{
					StartRecording();
				}
				
			}
		});
		
		Button exitBtn = (Button) findViewById(R.id.vid_exit_btn);
		exitBtn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
					ReleaseRecorder();
					if(isPreview)
					{
						camera.stopPreview();
						isPreview = false;
					}
					if(camera != null) {
						camera.release();
						camera = null;
					}
					VideoRecorder.this.finish();
				}
		});
		
	}
    
    /*
     * init camera:
     * open camera and start preview
     */
	private void initCamera()
	{
		if(!isPreview && camera == null)
		{
			camera = Camera.open();
			Log.e("aa","opened camera");
		}
		
		if (camera != null && !isPreview)
		{
			try
			{	
				Camera.Parameters parameters = camera.getParameters();
				camera.setDisplayOrientation(90);
				parameters.setRotation(90);
				camera.setParameters(parameters);
				//通过SurfaceView显示取景画面
				camera.setPreviewDisplay(surfaceHolder);
				// 开始预览
				camera.startPreview();
				camera.autoFocus(null);

				Log.i("aa", "StartPreview");
			}
			catch (Exception e)
			{
				Log.e("aa", "exception in init camera");
				Toast.makeText(VideoRecorder.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			}
			
			isPreview = true;
		}
	}
    
    /*
     * start recording
     */
    private void StartRecording() {
    	// prepare recorder
    	if( prepareRecorder() ) {
			try {
				
				TextView vid_timer = (TextView)findViewById(R.id.vid_info_textview);
				vid_timer.setText("剩余时间: "+timeToString(left_time));
				Toast.makeText(
						VideoRecorder.this, "开始录制视频", Toast.LENGTH_SHORT).show();
				vRecorder.start();
				
			} catch (Exception e) {
				Log.e("aa", "fail to record: "+e.getMessage());
				Toast.makeText(
						VideoRecorder.this, 
						e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			// change button caption
			Button recBtn = (Button) findViewById(R.id.vid_rec_btn);
			recBtn.setText("停止拍摄");
			// change sign
			isRecording = true;
		}
		else
		{
			ReleaseRecorder();
		}
    }
    
    /*
     * stop recording
     */
    private void StopRecording() {
    	if(isRecording) {
    		// stop recording and save recorded video
			if( vRecorder != null) {
				// stop
				vRecorder.stop();
				Toast.makeText(
						VideoRecorder.this, "结束录制视频", Toast.LENGTH_SHORT).show();
				// release
				ReleaseRecorder();
				// reset button caption
				Button recBtn = (Button) findViewById(R.id.vid_rec_btn);
				recBtn.setText("开始拍摄");
				// reset recording time
				left_time = max_video_duration;
				// reset sign
				isRecording = false;
				// save
				SaveRecordedVideo();
			}
    	}
    }

    
	/*
	 * create recorder instance
	 * set recorder params
	 */
	private boolean prepareRecorder()
	{	
		// prepare media recorder
		try {
			vRecorder = new MediaRecorder(); 
			// unlock camera
			camera.unlock();
			vRecorder.setCamera(camera);
			// set sources
			vRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
			vRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); 
			vRecorder.setProfile( CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
		    Log.d("file format",String.valueOf(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).fileFormat));
		    Log.d("video Codec",String.valueOf(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).videoCodec));
		    Log.d("video BitRT",String.valueOf(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).videoBitRate));
		    Log.d("video BitRT",String.valueOf(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).videoFrameRate));

		    

			/*
//			vRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);*/
			vRecorder.setVideoSize(this.VIDEO_WIDTH, this.VIDEO_HEIGHT);
			// set frame rate
			vRecorder.setVideoFrameRate(20);
			vRecorder.setVideoEncodingBitRate(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW).videoBitRate *10);

			
			
			// set preview output
			vRecorder.setPreviewDisplay(sView.getHolder().getSurface());
			
			// set output file path
			save_vid_file = Tools.getOutputMediaFile(Tools.MEDIA_TYPE_VIDEO);
			vRecorder.setOutputFile( save_vid_file.getAbsolutePath() );
			// prepare recorder
			vRecorder.prepare();		
			
			// start timer
			handler.postDelayed(task, 1000);
			
		} catch (IOException e) {
			Toast.makeText(VideoRecorder.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
			Log.d("aa", "prepare error: " + e.getMessage());
			ReleaseRecorder();
			return false;
		}
		catch (IllegalStateException e) {
			Toast.makeText(VideoRecorder.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
			Log.d("aa", "prepare error: " + e.getMessage());
			ReleaseRecorder();
			return false;
		}
		
		return true;
	};
	
	
	private void ReleaseRecorder() {
		if(vRecorder!=null) {
			vRecorder.reset();
			vRecorder.release();
			vRecorder = null;
			if(camera!=null)
				camera.lock();
		}
	}
	
	/*
	 * preview saved video file
	 */
	private void SaveRecordedVideo() {
		
		// preview use videoview for saved file
		Intent i = new Intent(VideoRecorder.this, VideoPreviewer.class);
		i.putExtra("video_file", save_vid_file.getAbsolutePath());
		//startActivity(i);
		startActivityForResult(i, this.VIDEO_SHOW_REQUEST);

		Toast.makeText(VideoRecorder.this, "预览视频", Toast.LENGTH_SHORT).show();

		camera.stopPreview();
		camera.startPreview();
		isPreview = true;
	}

	/*
	 * convert millisecond to standard time format
	 */
	private String timeToString(int left_msec) { 
		int left_sec = left_msec / 1000;	// convert millisecond to second
		if (left_sec >= 60) {  
			int min = left_sec / 60; 
			String m = min > 9 ? min + "" : "0" + min;    
			int sec = left_sec % 60;     
			String s = sec > 9 ? sec + "" : "0" + sec;       
			return m + ":" + s;      
			}
		else
		{
			return "00:" + (left_sec > 9 ? left_sec + "" : "0" + left_sec);    
		}
	} 

}


