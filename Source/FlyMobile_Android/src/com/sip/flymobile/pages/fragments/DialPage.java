package com.sip.flymobile.pages.fragments;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;

import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;


public class DialPage extends BasePageDecorator {
	ImageView 	m_imgContactIcon = null;
	TextView 	m_txtMobile = null;
	TextView 	m_txtExpireDate = null;
	TextView	m_txtCredit = null;
	TextView	m_txtChargeCredit = null;
	
	Button		m_btnAddContact = null;
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
		m_txtChargeCredit = (TextView) getContext().findViewById(R.id.txt_charge_credit);
		
		m_btnAddContact = (Button) getContext().findViewById(R.id.btn_add_contact);
		m_txtPrefix = (TextView) getContext().findViewById(R.id.txt_prefix);
		m_txtDialNumber = (TextView) getContext().findViewById(R.id.txt_dial_number);
		m_btnDel = (Button) getContext().findViewById(R.id.btn_del);
		
		m_btnSwitchNumber = (Button) getContext().findViewById(R.id.btn_switch);
		m_txtContactNumber = (TextView) getContext().findViewById(R.id.txt_contact_number);
		
		m_btnDial = (Button) getContext().findViewById(R.id.btn_dial);
		m_btnNotification = (Button) getContext().findViewById(R.id.btn_noti_callback);
		
	}
		
	public void initData()
	{
		super.initData();
		
	}
	
	public void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_total_self_info), 0, 40, 0, 40, true);
		LayoutUtils.setSize(m_imgContactIcon, 200, 200, true);
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_self_info), 80, 0, 0, 0, true);
		
		m_txtMobile.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(72));
		
		int fontsize = 35;
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_expire), 0, 20, 0, 0, true);
		((TextView)getContext().findViewById(R.id.lbl_expire_date)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(fontsize));
		m_txtExpireDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(fontsize));
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_credit), 0, 20, 0, 0, true);
		((TextView)getContext().findViewById(R.id.lbl_credit_balance)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(fontsize));
		m_txtCredit.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(fontsize));
		LayoutUtils.setMargin(m_txtChargeCredit, 50, 0, 0, 0, true);
		m_txtChargeCredit.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(fontsize));
		
		LayoutUtils.setSize(m_btnAddContact, 62, 62, true);
		LayoutUtils.setMargin(m_txtPrefix, 43, 0, 0, 0, true);
		LayoutUtils.setMargin(m_txtDialNumber, 43, 0, 43, 0, true);
		LayoutUtils.setSize(m_btnDel, 84, 60, true);
		
		((TextView)getContext().findViewById(R.id.lbl_display_number)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(fontsize));
		LayoutUtils.setSize(m_btnSwitchNumber, 50, 50, true);
		LayoutUtils.setMargin(m_txtContactNumber, 30, 0, 0, 0, true);
		m_txtContactNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		
		
		LayoutUtils.setSize(m_btnDial, 116, 116, true);
		
		LayoutUtils.setMargin(m_btnNotification, 190, 0, 0, 0, true);		
		LayoutUtils.setSize(m_btnNotification, 116, 116, true);
		
	}
}
