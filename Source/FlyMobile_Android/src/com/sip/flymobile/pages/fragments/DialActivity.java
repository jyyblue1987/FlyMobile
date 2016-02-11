package com.sip.flymobile.pages.fragments;

import org.doubango.imsdroid.Screens.BaseScreen;
import org.doubango.ngn.services.INgnSipService;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.BasePage;
import com.sip.flymobile.pages.HeaderPage;

import android.os.Bundle;

public class DialActivity extends BaseScreen {
	private static String TAG = DialActivity.class.getCanonicalName();
	BaseView	m_view = null;
	private final INgnSipService mSipService;
	
	public DialActivity() {
		super(SCREEN_TYPE.HOME_T, TAG);
		mSipService = getEngine().getSipService();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dial_page);
		
		m_view = new DialPage(new HeaderPage(new BasePage()));
		
		m_view.setContext(this);
		m_view.findViews();
		m_view.layoutControls();
		m_view.initData();
		m_view.initEvents();
	}	
}
