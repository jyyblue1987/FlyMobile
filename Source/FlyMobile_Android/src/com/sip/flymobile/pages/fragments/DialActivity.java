package com.sip.flymobile.pages.fragments;

import org.doubango.imsdroid.Screens.BaseScreen;
import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.BasePage;
import com.sip.flymobile.pages.HeaderPage;
import com.sip.flymobile.sip.SipController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class DialActivity extends BaseScreen {
	private static String TAG = DialActivity.class.getCanonicalName();
	BaseView	m_view = null;
	
	private BroadcastReceiver mSipBroadCastRecv;
	
	public DialActivity() {
		super(SCREEN_TYPE.HOME_T, TAG);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dial_page);
		
//		SipController.register(this);
		
		m_view = new DialPage(new HeaderPage(new BasePage()));
		
		m_view.setContext(this);
		m_view.findViews();
		m_view.layoutControls();
		m_view.initData();
		m_view.initEvents();
		
		// Listen for registration events
		mSipBroadCastRecv = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				
				// Registration Event
				if(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT.equals(action)){
					NgnRegistrationEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
					if(args == null){
//						mTvLog.setText("Invalid event args");
						return;
					}
					switch(args.getEventType()){
						case REGISTRATION_NOK:
//							mTvLog.setText("Failed to register :(");
							break;
						case UNREGISTRATION_OK:
//							mTvLog.setText("You are now unregistered :)");
							break;
						case REGISTRATION_OK:
//							mTvLog.setText("You are now registered :)");
							break;
						case REGISTRATION_INPROGRESS:
//							mTvLog.setText("Trying to register...");
							break;
						case UNREGISTRATION_INPROGRESS:
//							mTvLog.setText("Trying to unregister...");
							break;
						case UNREGISTRATION_NOK:
//							mTvLog.setText("Failed to unregister :(");
							break;
					}
//					mAdapter.refresh();
				}
			}
		};
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
	    registerReceiver(mSipBroadCastRecv, intentFilter);
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SipController.register(this);
	}
}
