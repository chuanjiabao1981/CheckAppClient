package com.android.task.main.function;

import com.android.task.tools.EquipmentId;

import android.app.Activity;
import android.app.AlertDialog;

public class IdShow 
{
	private final String ID_TITLE				= "设备编码";
	
	private Activity 			mA 				= null;
	private EquipmentId mEquipmentId			= null;
	private AlertDialog mIdDialog				= null;

	
	public IdShow(Activity a)
	{
		this.mA				=	a;
		this.mEquipmentId	=   new EquipmentId(this.mA);
		this.init();
	}
	public AlertDialog getIdDialog() {
		return mIdDialog;
	}
	private void init()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.mA);
		builder.setTitle(ID_TITLE);
		builder.setMessage(this.mEquipmentId.getId());
		builder.setPositiveButton("确定", null);
		this.mIdDialog = builder.create();
	}
}
