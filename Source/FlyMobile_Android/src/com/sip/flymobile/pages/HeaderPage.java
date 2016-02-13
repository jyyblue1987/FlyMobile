package com.sip.flymobile.pages;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.Services.IScreenService;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.fragments.AddContactActivity;

import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.design.utils.ResourceUtils;


public class HeaderPage extends BasePageDecorator {
	TextView 	m_txtPageTitle = null;
	RelativeLayout		m_layLeft = null;
	RelativeLayout		m_layRight = null;
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
		
		m_txtPageTitle = (TextView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.txt_navigate_bar_title);
		m_layLeft = (RelativeLayout) getContext().findViewById(R.id.fragment_header).findViewById(R.id.lay_left);
		m_layRight = (RelativeLayout) getContext().findViewById(R.id.fragment_header).findViewById(R.id.lay_right);		
		m_btnLeft = (Button) getContext().findViewById(R.id.fragment_header).findViewById(R.id.btn_left_button);
		m_btnRight = (Button) getContext().findViewById(R.id.fragment_header).findViewById(R.id.btn_right_button);		
		m_txtNotify = (TextView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.txt_notify_count);
	}
	public void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setSize(m_layLeft, 160, 125, true);
		LayoutUtils.setSize(m_layRight, 160, 125, true);
		
		m_btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		m_btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		LayoutUtils.setSize(m_btnLeft, 50, 44, true);
		
		LayoutUtils.setSize(m_txtNotify, 53, 53, true);
		m_txtNotify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(44));
		LayoutUtils.setMargin(m_txtNotify, 90, 60, 0, 0, true);

		m_txtPageTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(54));
		
	}
	
	public void initEvents()
	{
		m_layLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gotoBackPage();				
			}
		});
		
		m_layRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gotoNextPage();				
			}
		});
		
		m_txtNotify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				m_layLeft.performClick();	
			}
		});		
	}
	
	protected void gotoBackPage()
	{
		IScreenService screenService = ((Engine)Engine.getInstance()).getScreenService();
		screenService.back();
	}
	
	protected void gotoNextPage()
	{
		
	}
	
	public void setButtonText(int side, String text)
	{
		if( side == 0 )
			m_btnLeft.setText(text);
		if( side == 1 )
			m_btnRight.setText(text);		
	}
	
	public void setButtonVisible(int side, int visibility)
	{
		if( side == 0 )
			m_layLeft.setVisibility(visibility);
		if( side == 1 )
			m_layRight.setVisibility(visibility);		
	}
	
	public void setButtonEvents(int side, View.OnClickListener listener)
	{
		if( side == 0 )
			m_layLeft.setOnClickListener(listener);
		if( side == 1 )
			m_layRight.setOnClickListener(listener);
	}
	
	public void setTitle(String text)
	{
		m_txtPageTitle.setText(text);
	}
	
	public Button getNavigateButton(int side)
	{
		if( side == 0 )
			return m_btnLeft;
		else
			return m_btnRight;
	}
}
