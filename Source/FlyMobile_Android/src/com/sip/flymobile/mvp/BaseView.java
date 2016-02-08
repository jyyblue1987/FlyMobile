package com.sip.flymobile.mvp;

import android.app.Activity;

public interface BaseView {
	public static final String INTENT_EXTRA = "intent_extra";
	
	public void setContext(Activity context);
	public Activity getContext();
	public void findViews();
	public void layoutControls();
	public void initData();
	public void initEvents();
	
	public void initProgress();
	public void showProgress(String title, String message);
	public void changeProgress(String title, String message);
	public void hideProgress();
	public void finishView();
}
