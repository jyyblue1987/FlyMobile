package com.sip.flymobile.pages;

import com.sip.flymobile.mvp.BaseView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import common.library.utils.ActionUtils;
import common.manager.activity.ActivityManager;

public class BasePage implements BaseView {
	protected Activity context = null;
	
	protected  ProgressDialog progressDialog = null;

	boolean m_bActivityForground = false;
	
	protected Handler mMessageHandle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			processMessage(msg);
		}		
	};
	
	public BasePage()
	{
		
	}
	
	public void setContext(Activity context) {
		this.context = context;
		ActivityManager.getInstance().pushActivity(context);
		setBackgroundColor(Color.WHITE);
		initProgress();
		
		hideKeyboardTouchOutSideEditBox();
	}
	
	public Activity getContext()
	{
		return context;
	}

	public void findViews()
	{
		
	}
	
	public void layoutControls()
	{
		
	}
	
	public void initData()
	{
		
	}
	
	public void initEvents()
	{
		
	}
	public void hideKeyboardTouchOutSideEditBox()
	{
		FrameLayout rootView = (FrameLayout) context.findViewById(android.R.id.content);
		ActionUtils.hideKeyboardOutSideEditBox(rootView, context);
	}
	
	public void setBackgroundColor( int color )
	{
		View view = context.getWindow().getDecorView();
	    view.setBackgroundColor(color);

	}
	
	public void sendMessage(Message msg) 
	{
		mMessageHandle.sendMessage(msg);
	}
	
	public void sendMessageDelayed(Message msg, long delayMillis ) 
	{
		mMessageHandle.sendMessageDelayed(msg, delayMillis);
	}
		
	public void onFinishActivity()
	{
		ActivityManager.getInstance().popActivity();	
	}
	
	public void onBackPressed( ) {	
		onFinishActivity();
	}
	

	public void processMessage(Message msg)
	{		
		switch( msg.what )
		{
		
		}
	}

	@Override
	public void initProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
	}

	@Override
	public void showProgress(String title, String message) {
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		if(progressDialog.isShowing()){
			return;
		}
		progressDialog.show();
		
	}
	
	@Override
	public void changeProgress(String title, String message) {
		if( progressDialog.isShowing() == false )
			return;
		
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		
	}

	@Override
	public void hideProgress() {
		progressDialog.dismiss();	
	}

	@Override
	public void finishView() {
		onFinishActivity();		
	}
	    
    protected void showLoadingProgress()
    {
    	showProgress("", "Loading");
    }
    
	public void onResume( ) {
		m_bActivityForground = true;
	}
	
	public void onPause( ) {
		m_bActivityForground = false;
	}
    
    protected void quitProgram()
    {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
		alert_confirm.setMessage("Do you want to quit this program?").setCancelable(false).setPositiveButton("OK",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	ActivityManager.getInstance().popAllActivity();	        
		    }
		}).setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {		        
		        return;
		    }
		});
		AlertDialog alert = alert_confirm.create();
		alert.show();
    }

}
