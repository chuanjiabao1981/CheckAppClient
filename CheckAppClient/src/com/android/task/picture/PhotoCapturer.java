package com.android.task.picture;

import com.android.task.tools.*;
import com.android.task.video.VideoRecorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import com.android.task.R;
import com.android.task.R.id;
import com.android.task.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class PhotoCapturer extends Activity
{
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	int imgWidth, imgHeight;
	// 定义系统所用的照相机
	Camera camera;
	//是否在浏览中
	boolean isPreview = false;
	private final int  GET_CONTENT_URI = 123;
	
	protected void onActivityResult(int requestCode, int resultCode,  Intent intent) {
		if(requestCode==GET_CONTENT_URI)  
		{
			Uri result = intent == null || resultCode != RESULT_OK ? null  : intent.getData();  
			Log.i("aa",result.toString());
			finish();
		}
	}
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cam_pic_layout);
		
		WindowManager wm = (WindowManager) getSystemService(
			Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 获取屏幕的宽和高
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		// 获取界面中SurfaceView组件
		sView = (SurfaceView) findViewById(R.id.pic_view);
		// 获得SurfaceView的SurfaceHolder
		surfaceHolder = sView.getHolder();
		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback()
		{
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height)
			{
			}

			public void surfaceCreated(SurfaceHolder holder)
			{
				// 打开摄像头
				initCamera();
			}
			
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				Log.i("aa","surface destroyed");

				// 如果camera不为null ,释放摄像头
				if (camera != null)
				{
					if (isPreview)
					{
						camera.stopPreview();
						isPreview = false;
					}
					camera.release();
					camera = null;
				}
			}		
		});
		// 设置该SurfaceView自己不维护缓冲    
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		// set button event
		Button recBtn = (Button) findViewById(R.id.pic_rec_btn);
		recBtn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				if(isPreview){
					try {
						// take picture and save
						camera.takePicture(null, null, myPicture);
					} catch (Exception e) {
						// TODO: handle exception
						Log.e("aa", "Error take picture: " + e.getMessage());
					}
				}
			}
		});
		
		Button exitBtn = (Button) findViewById(R.id.pic_exit_btn);
		exitBtn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
					UploadMessage.set_upload_message(null);
					PhotoCapturer.this.finish();
					return;
				}
		});
		
	}

    private void initCamera()
	{
		if (!isPreview)
		{
			camera = Camera.open();
			Log.i("aa","open camera");
		}
		if (camera != null && !isPreview)
		{
			try
			{
				Camera.Parameters parameters = camera.getParameters();
				// 设置预览照片的大小
				parameters.setPreviewSize(screenWidth, screenHeight);
				Log.i("aa",String.valueOf(screenWidth));
				Log.i("aa",String.valueOf(screenHeight));
				camera.setDisplayOrientation(90);
				parameters.setRotation(90);


				// 每秒显示4帧
				/*parameters.setPreviewFrameRate(4);
				// 设置图片格式
				parameters.setPictureFormat(PixelFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 85);
				//设置照片的大小
				imgWidth = screenWidth;
				imgHeight = screenHeight;
				parameters.setPictureSize(imgWidth, imgHeight);*/
				camera.setParameters(parameters);
				//通过SurfaceView显示取景画面
				camera.setPreviewDisplay(surfaceHolder);
				// 开始预览
				camera.startPreview();
				// set autofocus
				camera.autoFocus(null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	
	PictureCallback myPicture = new PictureCallback()
	{
		public void onPictureTaken(byte[] data, Camera camera)
		{
			// first save temp picture to dir
			final File picFile = Tools.getOutputMediaFile(
					Tools.MEDIA_TYPE_IMAGE);
			if( picFile == null ){
				Log.e("aa", "Fail to generate image, check storage permission.");
				return;
			}
			
			// save to file
			try {
				FileOutputStream fos = new FileOutputStream(picFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.e("aa", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.e("aa", "Error accessing file: " + e.getMessage());
				Toast.makeText(
						PhotoCapturer.this, 
						"无法保存图像，请检查存储容量", Toast.LENGTH_SHORT).show();
			}
			
			// show layout
			final Bitmap bm = BitmapFactory.decodeByteArray(data
					, 0, data.length);	// null
			if(bm == null) {
				Log.e("aa", "bitmap is null.");
				return;
			}
			View pic_save_dialog = getLayoutInflater().inflate(
					R.layout.cam_pic_save_layout, null);
			ImageView pic_view = (ImageView)pic_save_dialog.findViewById(
					R.id.pic_save_view);
			pic_view.setImageBitmap(bm);	// error
			// show in dialog
			AlertDialog.Builder save_diaglog = new AlertDialog.Builder(
					PhotoCapturer.this);
			save_diaglog.setView(pic_save_dialog);
			save_diaglog.setPositiveButton("保存", new OnClickListener()
			{
				public void onClick(DialogInterface dialog,
					int which)
				{
					InsertFileToMediaStore insert_file = new InsertFileToMediaStore(PhotoCapturer.this,picFile,"image/jpeg");
					Uri uri = insert_file.insert();
					picFile.delete();
					UploadMessage.set_upload_message(uri);
					finish();
				}
			});
			save_diaglog.setNegativeButton("取消", new OnClickListener()
			{
				public void onClick(DialogInterface dialog,
					int which)
				{
					// delete saved file
					if(picFile != null) {
						picFile.delete();
					}
				}
			});
			
			save_diaglog.show();

			//重新浏览
			camera.stopPreview();
			camera.startPreview();
			isPreview = true;
		}
	};
}


