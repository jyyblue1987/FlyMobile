package com.sip.flymobile.pages;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.fragments.DialActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;


public class MainPage extends BasePageDecorator {
	TextView 	m_txtPageTitle = null;
	Button		m_btnLeft = null;
	Button		m_btnRight = null;
	
	TextView m_txtNotify = null;
	
	TabActivity view; 
	
	public MainPage(BaseView view)
	{
		super(view);		
	}
	public void findViews()
	{
		view = (TabActivity)getContext();
		super.findViews();
		
		TabHost tabHost = view.getTabHost(); 
		
		// Message tab
		Intent intentDial = new Intent().setClass(view, DialActivity.class);
		TabSpec tabSpecDial = tabHost
			.newTabSpec("Call")
			.setIndicator(createTabIndicator("Call"))
			.setContent(intentDial);

		// Message tab
		Intent intentMessage = new Intent().setClass(view, DialActivity.class);
		TabSpec tabSpecMessage = tabHost
			.newTabSpec("Messaging")
			.setIndicator(createTabIndicator("Messaing"))
			.setContent(intentMessage);
		
		// Recent tab
		Intent intentRecent = new Intent().setClass(view, DialActivity.class);
		TabSpec tabSpecRecent = tabHost
			.newTabSpec("Recent")
			.setIndicator(createTabIndicator("Recent"))
			.setContent(intentRecent);
		
		// Contact tab
		Intent intentContact = new Intent().setClass(view, DialActivity.class);
		TabSpec tabSpecContact = tabHost
			.newTabSpec("Contact")
			.setIndicator(createTabIndicator("Contact"))
			.setContent(intentContact);
		
		// Setting tab
		Intent intentSetting = new Intent().setClass(view, DialActivity.class);
		TabSpec tabSpecSetting = tabHost
			.newTabSpec("Setting")
			.setIndicator(createTabIndicator("Setting"))
			.setContent(intentSetting);

		// add all tabs 
		tabHost.addTab(tabSpecDial);
		tabHost.addTab(tabSpecMessage);
		tabHost.addTab(tabSpecRecent);
		tabHost.addTab(tabSpecContact);
		tabHost.addTab(tabSpecSetting);
		
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}
	public void layoutControls()
	{
		super.layoutControls();
			
	}
	
	public void initEvents()
	{
		super.initEvents();
				
	}
	
	public void initData()
	{
		super.initData();
	}
	
	private View createTabIndicator(String label) {
	    View tabIndicator = view.getLayoutInflater().inflate(R.layout.fragment_tabindicator, null);
	    TextView tv = (TextView) tabIndicator.findViewById(R.id.label);
	    
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(46));
	    LayoutUtils.setPadding(tv, 0, 20, 0, 20, true);
	    
	    tv.setText(label);
	    return tabIndicator;
	}	 
}
