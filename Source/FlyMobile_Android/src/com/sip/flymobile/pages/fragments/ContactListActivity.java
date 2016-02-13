package com.sip.flymobile.pages.fragments;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.BasePage;
import com.sip.flymobile.pages.HeaderPage;

import android.app.Activity;
import android.os.Bundle;

public class ContactListActivity extends Activity {
	BaseView	m_view = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact_page);
		
		m_view = new ContactListPage(new HeaderPage(new BasePage()));
		
		m_view.setContext(this);
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
