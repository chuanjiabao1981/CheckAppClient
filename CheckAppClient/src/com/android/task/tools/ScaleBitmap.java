package com.android.task.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ScaleBitmap {
	private Uri mSrcImageUri = null;
	//private Bitmap mSrcBitmap = null;
	private Bitmap mDesBitmap = null;
	private Activity mActivity = null;
	private final String TAG			= ScaleBitmap.class.getName();
	private final int    SCALE_WIDTH    = 800;
	private final int    SCALE_HEIGHT	= 800;
	public ScaleBitmap (Activity a,Uri u)
	{
		this.mSrcImageUri = u;
		this.mActivity = a;
		//init();
	}
	public Bitmap scale()
	{
		return _scale();
		/*
		if (this.mSrcBitmap == null){
			return null;
		}
		try {
			this.mDesBitmap = Bitmap.createScaledBitmap(this.mSrcBitmap,SCALE_WIDTH,SCALE_HEIGHT,true);
		}catch(Exception e){
			Toast.makeText(this.mActivity, "缩放失败!!!",Toast.LENGTH_SHORT).show();
			return null;
		}
		return this.mDesBitmap;
		*/
	}
	public Bitmap _scale()
	{
		if (this.mSrcImageUri == null){
			return null;
		}
        try {
			InputStream input = this.mActivity.getContentResolver().openInputStream(mSrcImageUri);
	        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
	        onlyBoundsOptions.inJustDecodeBounds = true;
	        onlyBoundsOptions.inDither=true;//optional
	        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
	        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
	        input.close();
	        if (onlyBoundsOptions.outWidth <=0 || onlyBoundsOptions.outHeight <= 0){
	        	return null;
	        }
	        //Toast.makeText(this.mActivity, "1.得到了:"+String.valueOf(onlyBoundsOptions.outWidth) + "x" +String.valueOf(onlyBoundsOptions.outHeight), Toast.LENGTH_LONG).show();
	        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	        bitmapOptions.inSampleSize = calculateInSampleSize(onlyBoundsOptions,SCALE_WIDTH,SCALE_HEIGHT);
	        bitmapOptions.inDither=true;//optional
	        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
	        input = this.mActivity.getContentResolver().openInputStream(mSrcImageUri);
	        mDesBitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        	//Toast.makeText(this.mActivity, "2.得到了:"+String.valueOf(this.mDesBitmap.getWidth()) + "x" +String.valueOf(this.mDesBitmap.getHeight()), Toast.LENGTH_LONG).show();

	        return mDesBitmap;
	        
		} catch (FileNotFoundException e) {
			Toast.makeText(this.mActivity, "文件不存在!", Toast.LENGTH_SHORT).show();
			Log.e(TAG,"文件没找到:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this.mActivity, "文件读写发生错误!", Toast.LENGTH_SHORT).show();
			Log.e(TAG,"文件读写发生错误!"+e.getMessage());
			e.printStackTrace();
		}
        return null;

	}
	/*
	private void init()
	{
		if (this.mSrcImageUri == null){
			Log.d(TAG,"空文件");
			return;
		}
        try {
        	mSrcBitmap = MediaStore.Images.Media.getBitmap(this.mActivity.getContentResolver(), mSrcImageUri);
//        	Toast.makeText(this.mActivity, "得到了"+String.valueOf(this.mSrcBitmap.getHeight()), Toast.LENGTH_LONG).show();
//        	Toast.makeText(this.mActivity, "得到了"+String.valueOf(this.mSrcBitmap.getWidth()), Toast.LENGTH_LONG).show();
//        	Toast.makeText(this.mActivity, "得到了"+String.valueOf(this.mSrcBitmap.getDensity()), Toast.LENGTH_LONG).show();

		} catch (FileNotFoundException e) {
			Log.e(TAG,"文件没找到:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG,"读取文件发生错误:"+e.getMessage());
			e.printStackTrace();
		}

	}*/
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        if (width > height) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        } else {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
    }
    return inSampleSize;
}
	public void release()
	{
		if (mDesBitmap !=null && !mDesBitmap.isRecycled()){

			this.mDesBitmap.recycle();
			mDesBitmap = null;
			System.gc();
		}
	}

}
