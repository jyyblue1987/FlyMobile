package com.sip.flymobile.pages.fragments;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.BasePage;
import com.sip.flymobile.pages.HeaderPage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CallHistoryActivity extends Activity {
	BaseView	m_view = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_message_history_page);
		
		m_view = new CallHistoryPage(new HeaderPage(new BasePage()));
		
		m_view.setContext(this, false);
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
	
	@Override
	protected void onDestroy() {
		m_view.onDestroy();
       super.onDestroy();
	}

}
