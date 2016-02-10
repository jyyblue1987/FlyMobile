package com.sip.flymobile.pages.fragments;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DialPage extends BasePageDecorator {
	ImageView 	m_imgContactIcon = null;
	TextView 	m_txtMobile = null;
	TextView 	m_txtExpireDate = null;
	TextView	m_txtCredit = null;
	TextView	m_txtChargeCredit = null;
	
	Button		m_btnPrefix = null;
	TextView 	m_txtPrefix = null;
	TextView	m_txtDialNumber = null;
	Button		m_btnDel = null;
	
	Button		m_btnSwitchNumber = null;
	TextView	m_txtContactNumber = null;
	
	Button		m_btnNotification = null;
	Button		m_btnDial = null;
	
	public DialPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		
		m_imgContactIcon = (ImageView) getContext().findViewById(R.id.img_contact_icon);
		m_txtMobile = (TextView) getContext().findViewById(R.id.txt_mobile);
		m_txtExpireDate = (TextView) getContext().findViewById(R.id.txt_expire_date);
		m_txtCredit = (TextView) getContext().findViewById(R.id.txt_credit_balance);
	}
		
	public void initData()
	{
		super.initData();
		
	}
	
	public void layoutControls()
	{
		super.layoutControls();
		
		
	}
}
