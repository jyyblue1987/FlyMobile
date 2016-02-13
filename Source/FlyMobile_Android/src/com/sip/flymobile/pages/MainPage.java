package com.sip.flymobile.pages;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.fragments.CallHistoryActivity;
import com.sip.flymobile.pages.fragments.ContactListActivity;
import com.sip.flymobile.pages.fragments.DialActivity;
import com.sip.flymobile.pages.fragments.MessageHistoryActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
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
	private static int		g_nTabNum = 0;
	
	private int [] tab_image_res = {
		R.drawable.call_icon, R.drawable.msg_icon, R.drawable.recent_icon, R.drawable.contact_icon, R.drawable.setting_icon	
	};
		
	private String [] tab_label = {
		"Call", "Messaging", "Recent", "Contact", "Setting"
	};
	
	Class<?> [] destAct = {
		DialActivity.class,
		MessageHistoryActivity.class,
		CallHistoryActivity.class,
		ContactListActivity.class,
		DialActivity.class		
	};
	
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
		for(int i = 0; i < tab_label.length; i++ )
		{
			Intent intent = new Intent().setClass(view, destAct[i]);
			TabSpec tabSpecDial = tabHost
				.newTabSpec(tab_label[i])
				.setIndicator(createTabIndicator(i))
				.setContent(intent);
			
			tabHost.addTab(tabSpecDial);
		}
		
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(g_nTabNum);
	}
	public void layoutControls()
	{
		super.layoutControls();
			
	}
	
	public void initEvents()
	{
		super.initEvents();
				
		view = (TabActivity)getContext();
		TabHost tabHost = view.getTabHost();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				for(int i = 0; i < tab_label.length; i++ )
				{
					if( tab_label[i].equals(tabId) )
					{
						g_nTabNum = i;
						break;
					}
				}
			}
		});
	}
	
	public void initData()
	{
		super.initData();
	}
	

	
	private View createTabIndicator(int num) {
	    View tabIndicator = view.getLayoutInflater().inflate(R.layout.fragment_tabindicator, null);
	    
	    ImageView icon = (ImageView) tabIndicator.findViewById(R.id.img_tab_icon);
	    LayoutUtils.setSize(icon, 55, 55, true);
	    LayoutUtils.setMargin(icon, 0, 20, 0, 0, true);
	    icon.setBackgroundResource(tab_image_res[num]);
	    
	    TextView tv = (TextView) tabIndicator.findViewById(R.id.label);
	    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(33));
	    LayoutUtils.setMargin(tv, 0, 10, 0, 15, true);
	    tv.setText(tab_label[num]);
	    
	    return tabIndicator;
	}	 
}
