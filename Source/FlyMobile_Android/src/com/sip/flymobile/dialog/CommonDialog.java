package com.sip.flymobile.dialog;

import org.json.JSONObject;

import com.sip.flymobile.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import common.list.adapter.ItemCallBack;


public class CommonDialog extends Dialog implements CommonDialogInterface
{
	public static final String 	DIALOG_WIDTH = "dialog_width";
	public static final String 	DIALOG_HEIGHT = "dialog_height";
	public static final String 	DIALOG_POS_X = "dialog_x";
	public static final String 	DIALOG_POS_Y = "dialog_y";
	public static final String 	DIALOG_GRAVITY = "dialog_gravity";
	public static final String 	DIALOG_DATA = "dialog_data";
	
	protected JSONObject	m_Param = null;
	
	protected Window		m_Dialog = null;
	
	protected	ItemCallBack m_Callback;
	
	Context context = null;

	public CommonDialog(Context context, int layoutResID, JSONObject param, ItemCallBack callback )
	{
		super(context, R.style.CustomDialog);
		
		this.context = context;
		
		m_Dialog = getWindow();		
		m_Param = param;
		m_Callback = callback;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layoutResID);
		

		findViews();
		layoutDialog();
		initDialogData();
		
		setCanceledOnTouchOutside(true);
	}
	
	protected void layoutDialog()
	{
		if( m_Dialog == null )
			return;
		
		if( m_Param == null )
			return;
			
		JSONObject dialogData = m_Param.optJSONObject(DIALOG_DATA);

		int dialogWidth = dialogData.optInt(DIALOG_WIDTH, LayoutParams.MATCH_PARENT);
		int dialogHeight = dialogData.optInt(DIALOG_HEIGHT, LayoutParams.WRAP_CONTENT);
		int dialogGravity = dialogData.optInt(DIALOG_GRAVITY, Gravity.CENTER);
		int x = dialogData.optInt(DIALOG_POS_X, 0);
		int y = dialogData.optInt(DIALOG_POS_Y, 0);
		
		
		m_Dialog.setLayout(dialogWidth, dialogHeight);
		m_Dialog.setGravity(dialogGravity);
		
		m_Dialog.getAttributes().x = x;
		m_Dialog.getAttributes().y = y;
		
		switch (dialogGravity) {
		case Gravity.CENTER:
			m_Dialog.setWindowAnimations(R.style.dialogWindowAnimFromCenter);
			break;

		default:
			break;
		}		
	}
	
	protected void processMessage(Message msg)
	{		
		switch( msg.what )
		{
		
		}
	}

	
	protected Handler mMessageHandle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			processMessage(msg);
		}		
	};
	
	public void sendMessage(Message msg) 
	{
		mMessageHandle.sendMessage(msg);
	}
	
	public void sendMessageDelayed(Message msg, long delayMillis ) 
	{
		mMessageHandle.sendMessageDelayed(msg, delayMillis);
	}
	
	protected void findViews() 
	{
		
	}
	
	protected void initDialogData()
	{
		
	}

	@Override
	public void showDialog() {
		show();
	}

	@Override
	public void hideDialog() {
		hide();
	}

	class OnClickListener implements View.OnClickListener {
		CommonDialog m_parent;
		public OnClickListener(CommonDialog dialog)
		{
			m_parent = dialog;
		}
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
