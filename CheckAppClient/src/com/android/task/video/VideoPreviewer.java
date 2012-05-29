package com.android.task.video;

import java.io.File;

import com.android.task.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPreviewer extends Activity {

	private VideoView vid_view;
	private String video_file;	// = "/sdcard/DCIM/Camera/2012_05_24_00_34_22.3gp";
	public final static int VIDEO_IS_OK 		= 100;
	public final static int VIDEO_IS_NOT_OK 	= -100;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cam_vid_save_layout);
        
        Button vid_save_btn = (Button)findViewById(R.id.vid_save_btn);
        vid_save_btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if(vid_view.isPlaying())
					vid_view.stopPlayback();
				
				Toast.makeText(
						VideoPreviewer.this, "视频已经保存到: "+video_file, Toast.LENGTH_LONG).show();
				VideoPreviewer.this.setResult(VIDEO_IS_OK);
				
				finish();
				
				/*Intent i = new Intent();
				i.setClass(VideoPreviewer.this, VideoRecorder.class);
				startActivity(i);*/
			}
		});
        
        Button vid_cancel_btn = (Button)findViewById(R.id.vid_cancel_btn);
        vid_cancel_btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if(vid_view.isPlaying())
					vid_view.stopPlayback();
				// delete saved file
				if(video_file != null) {
					File vid = new File(video_file);
					if(vid != null) {
						vid.delete();
					}
				}
				VideoPreviewer.this.setResult(VIDEO_IS_NOT_OK);
				finish();
			}
		});
        
        StartPreview();
        
	}
	
	private void StartPreview() {
		
		// start to preview
		video_file = getIntent().getStringExtra("video_file");
		vid_view = (VideoView)findViewById(R.id.vid_save_view);
        // set file path
 		vid_view.setVideoURI(Uri.parse(video_file));
 		// set controller
 		vid_view.setMediaController(new MediaController(this));
 		// set error info
 		vid_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
 			
 			public boolean onError(MediaPlayer mp, int what, int extra) {
 				Toast.makeText(VideoPreviewer.this, "播放视频错误 ", Toast.LENGTH_SHORT).show();
 				return false;
 			}
 		});
 		
 		vid_view.start();
 		//vid_view.requestFocus();
 		
	}
}
