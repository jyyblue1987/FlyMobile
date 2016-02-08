package com.sip.flymobile.pages;

import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.mvp.BasePageDecorator;

import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.design.utils.ResourceUtils;


public class HeaderPage extends BasePageDecorator {
	TextView 	m_txtPageTitle = null;
	Button		m_btnLeft = null;
	Button		m_btnRight = null;
	
	TextView m_txtNotify = null;
	
	public HeaderPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		
//		m_txtPageTitle = (TextView) context.findViewById(R.id.fragment_header).findViewById(R.id.txt_navigate_bar_title);
//		m_btnLeft = (Button) context.findViewById(R.id.fragment_header).findViewById(R.id.btn_left_button);
//		m_btnRight = (Button) context.findViewById(R.id.fragment_header).findViewById(R.id.btn_right_button);		
//		m_txtNotify = (TextView) context.findViewById(R.id.fragment_header).findViewById(R.id.txt_notify_count);
	}
	public void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setSize(m_btnLeft, 160, 125, true);
		LayoutUtils.setSize(m_btnRight, 160, 125, true);
		ResourceUtils.addClickEffect(m_btnLeft);
		ResourceUtils.addClickEffect(m_btnRight);
		
		LayoutUtils.setSize(m_txtNotify, 53, 53, true);
		m_txtNotify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
		LayoutUtils.setMargin(m_txtNotify, 90, 60, 0, 0, true);

		m_txtPageTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(54));
		
	}
	
	public void initEvents()
	{
		m_btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gotoBackPage();				
			}
		});
		
		m_btnRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gotoNextPage();				
			}
		});
		
		m_txtNotify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				m_btnLeft.performClick();	
			}
		});		
	}
	
	protected void gotoBackPage()
	{
		super.finishView();		
	}
	
	protected void gotoNextPage()
	{
		
	}
	
	public void initData()
	{
		super.initData();
	}
}
