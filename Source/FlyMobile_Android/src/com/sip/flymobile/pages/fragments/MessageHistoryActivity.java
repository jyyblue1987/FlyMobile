package com.sip.flymobile.pages.fragments;

import org.doubango.imsdroid.Screens.BaseScreen;
import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.BasePage;
import com.sip.flymobile.pages.HeaderPage;
import com.sip.flymobile.sip.SipController;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MessageHistoryActivity extends BaseScreen {
	private static String TAG = MessageHistoryActivity.class.getCanonicalName();
	BaseView	m_view = null;
	
	private BroadcastReceiver mSipBroadCastRecv;
	
	public MessageHistoryActivity() {
		super(SCREEN_TYPE.HOME_T, TAG);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_message_history_page);
		
		m_view = new MessageHistoryPage(new HeaderPage(new BasePage()));
		
		m_view.setContext(this);
		m_view.findViews();
		m_view.layoutControls();
		m_view.initData();
		m_view.initEvents();
	}	

}