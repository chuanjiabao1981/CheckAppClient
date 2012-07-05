package com.android.task.tools;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class EquipmentId 
{
	private final String TAG 					= EquipmentId.class.getName();

	private TelephonyManager    mTelephonyManager;
	private Activity ma;
	public EquipmentId(Activity a)
	{
		this.ma 	=	a;
		this.init();
	}
	public String getId()
	{
		if (this.mTelephonyManager == null){
			Log.d(TAG, "TelephoneManager is null");
			return "null";
		}
		return this.mTelephonyManager.getDeviceId() +"-" +this.mTelephonyManager.getSubscriberId();
	}
	private void init()
	{
		this.mTelephonyManager =  ( TelephonyManager )this.ma.getSystemService(Context.TELEPHONY_SERVICE );
	}
}
