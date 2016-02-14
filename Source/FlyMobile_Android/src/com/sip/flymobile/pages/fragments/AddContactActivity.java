package com.sip.flymobile.pages.fragments;

import org.doubango.imsdroid.Screens.BaseScreen;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.BasePage;
import com.sip.flymobile.pages.HeaderPage;
import com.sip.flymobile.pages.MainActivity;

import android.os.Bundle;

public class AddContactActivity extends BaseScreen {
	private static String TAG = MainActivity.class.getCanonicalName();
	BaseView	m_view = null;
	
	public AddContactActivity()
	{
		super(SCREEN_TYPE.ADDCONTACT_T, TAG);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_addcontact_page);
		
		m_view = new AddContactPage(new HeaderPage(new BasePage()));
		
		m_view.setContext(this, true);
		m_view.findViews();
		m_view.layoutControls();
		m_view.initData();
		m_view.initEvents();
	}	
	
	@Override
	protected void onResume() {
		super.onResume();		
		m_view.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		m_view.onPause();
	}
}
