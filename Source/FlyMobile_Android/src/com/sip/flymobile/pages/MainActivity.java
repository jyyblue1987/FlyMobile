package com.sip.flymobile.pages;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.ImageView;
import common.manager.activity.ActivityManager;

public class MainActivity extends TabActivity {
	ImageView m_imgSplash = null;
	
	BaseView	m_view = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityManager.getInstance().popAllActivity();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		m_view = new MainPage(new BasePage());
		
		m_view.setContext(this);
		m_view.findViews();
		m_view.layoutControls();
		m_view.initData();
		m_view.initEvents();
	}
		
}
