package com.android.task.picture;

import java.io.File;

import com.android.task.R;
import com.android.task.tools.InsertFileToMediaStore;
import com.android.task.tools.UploadMessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

public class PhotoSaveDialog {
	private Activity mA;
	private AlertDialog mPhotoSaveDialog;
	
	private View mPhotoSaveView;
	private Bitmap mBitmap;
	private File mPicFile;
	public PhotoSaveDialog(Activity a,Bitmap b,File f)
	{
		this.mA 		= a;
		this.mBitmap	= b;
		this.mPicFile	= f;
		
		init();
	}
	public AlertDialog getPhotoSaveDialog() {
		return mPhotoSaveDialog;
	}
	
	private void init()
	{
		this.mPhotoSaveView = this.mA.getLayoutInflater().inflate(R.layout.cam_pic_save_layout, null);
		ImageView pic_view = (ImageView)mPhotoSaveView.findViewById(R.id.pic_save_view);
		pic_view.setImageBitmap(this.mBitmap);
		AlertDialog.Builder save_diaglog_builder = new AlertDialog.Builder(this.mA);
		save_diaglog_builder.setView(this.mPhotoSaveView );
		save_diaglog_builder.setPositiveButton("±£´æ", new OnClickListener()
		{
			public void onClick(DialogInterface dialog,
				int which)
			{
				InsertFileToMediaStore insert_file = new InsertFileToMediaStore(PhotoSaveDialog.this.mA,PhotoSaveDialog.this.mPicFile,"image/jpeg");
				Uri uri = insert_file.insert();
				PhotoSaveDialog.this.mPicFile.delete();
				UploadMessage.set_upload_message(uri);
				PhotoSaveDialog.this.mA.finish();
			}
		});
		save_diaglog_builder.setNegativeButton("È¡Ïû", new OnClickListener()
		{
			public void onClick(DialogInterface dialog,
				int which)
			{
				// delete saved file
				if(PhotoSaveDialog.this.mPicFile != null) {
					PhotoSaveDialog.this.mPicFile.delete();
				}
			}
		});
		mPhotoSaveDialog = save_diaglog_builder.create();
	}
}
