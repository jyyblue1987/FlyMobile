package com.sip.flymobile.pages;

import org.doubango.imsdroid.Screens.BaseTabScreen;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.sip.SipController;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends BaseTabScreen {
	private static String TAG = MainActivity.class.getCanonicalName();
	ImageView m_imgSplash = null;
	
	BaseView	m_view = null;
	
	public MainActivity()
	{
		super(SCREEN_TYPE.HOME_T, TAG);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		m_view = new MainPage(new BasePage());
		
		m_view.setContext(this);
		m_view.findViews();
		m_view.layoutControls();
		m_view.initData();
		m_view.initEvents();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SipController.register(this);
	}
		
}
