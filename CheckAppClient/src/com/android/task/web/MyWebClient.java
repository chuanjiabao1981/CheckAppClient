package com.android.task.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebClient extends WebViewClient{
	private Activity a;
	public MyWebClient(Activity a)
	{
		super();
		this.a  = a;
	}
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.endsWith(".3gp")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url),"video/3gp");
			a.startActivity(intent);
			return true;
		}else if(url.endsWith(".mp4")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url),"video/mp4");
			a.startActivity(intent);
			return true;
		}
        view.loadUrl(url);
        return true;
    }
}
