package com.sip.flymobile.mvp;

import android.app.Activity;

public class BasePageDecorator implements BaseView {
	protected BaseView decorator = null;
	
	public BasePageDecorator(BaseView view)
	{
		decorator = view;
	}
	
	@Override
	public void setContext(Activity context) {
		decorator.setContext(context);		
	}
	
	@Override
	public Activity getContext() {
		return decorator.getContext();
	}

	@Override
	public void findViews() {
		decorator.findViews();		
	}

	@Override
	public void layoutControls() {
		decorator.layoutControls();		
	}

	@Override
	public void initData() {
		decorator.initData();
	}

	@Override
	public void initEvents() {
		decorator.initEvents();
	}
	
	@Override
	public void initProgress() {
		decorator.initProgress();
	}
	
	@Override
	public void showProgress(String title, String message) {
		decorator.showProgress(title, message);
	}

	@Override
	public void changeProgress(String title, String message) {
		decorator.changeProgress(title, message);
	}

	@Override
	public void hideProgress() {
		decorator.hideProgress();
	}

	@Override
	public void finishView() {
		decorator.finishView();
	}

	@Override
	public void onDestroy() {
		decorator.onDestroy();		
	}
}
