package com.sip.flymobile.pages;

import org.doubango.imsdroid.Engine;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.ngn.utils.NgnStringUtils;
import org.doubango.tinyWRAP.SipCallback;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.sip.SipController;

import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.library.utils.CheckUtils;
import common.library.utils.MessageUtils;


public class AccountSettingPage extends BasePageDecorator {
	boolean 	m_bEditMode = false;
	INgnConfigurationService mConfigurationService = null;
	
	EditText	m_editNumber = null;
	EditText	m_editName = null;
	EditText	m_editPassword = null;
	
	public AccountSettingPage(BaseView view)
	{
		super(view);
		mConfigurationService = Engine.getInstance().getConfigurationService();
	}
	public void findViews()
	{
		super.findViews();
		
		m_editNumber = (EditText)getContext().findViewById(R.id.fragment_number).findViewById(R.id.edit_content);
		m_editName = (EditText)getContext().findViewById(R.id.fragment_display).findViewById(R.id.edit_content);
		m_editPassword = (EditText)getContext().findViewById(R.id.fragment_password).findViewById(R.id.edit_content);

	}
		
	public void layoutControls()
	{
		super.layoutControls();
		
		HeaderPage header = (HeaderPage) decorator;
		header.setButtonVisible(0, View.VISIBLE);
		header.setButtonVisible(1, View.VISIBLE);
		
		LayoutUtils.setPadding(getContext().findViewById(R.id.fragment_number), 50, 80, 50, 0, true);
		LayoutUtils.setPadding(getContext().findViewById(R.id.fragment_display), 50, 80, 50, 0, true);
		LayoutUtils.setPadding(getContext().findViewById(R.id.fragment_password), 50, 80, 50, 0, true);

		LayoutUtils.setPadding(getContext().findViewById(R.id.fragment_number).findViewById(R.id.lay_info), 0, 0, 0, 60, true);
		LayoutUtils.setPadding(getContext().findViewById(R.id.fragment_display).findViewById(R.id.lay_info), 0, 0, 0, 60, true);
		LayoutUtils.setPadding(getContext().findViewById(R.id.fragment_password).findViewById(R.id.lay_info), 0, 0, 0, 60, true);
		
		((TextView)getContext().findViewById(R.id.fragment_number).findViewById(R.id.txt_label)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		((TextView)getContext().findViewById(R.id.fragment_display).findViewById(R.id.txt_label)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		((TextView)getContext().findViewById(R.id.fragment_password).findViewById(R.id.txt_label)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		
		m_editNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		m_editName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		m_editPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));

	}
	
	private void changeNavigateButton()
	{
		HeaderPage header = (HeaderPage) decorator;
		
		header.setTitle("Account Settings");
		if( m_bEditMode == false )
			header.setButtonText(1, "Edit");
		else
			header.setButtonText(1, "Save");
	}
	public void initData()
	{
		super.initData();
		
		changeNavigateButton();
		
		((TextView)getContext().findViewById(R.id.fragment_number).findViewById(R.id.txt_label)).setText("Number:");
		((TextView)getContext().findViewById(R.id.fragment_display).findViewById(R.id.txt_label)).setText("Display");
		((TextView)getContext().findViewById(R.id.fragment_password).findViewById(R.id.txt_label)).setText("Password:");

		m_editNumber.setInputType(InputType.TYPE_CLASS_PHONE);
		m_editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		String sipNumber = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, NgnConfigurationEntry.DEFAULT_IDENTITY_IMPI);
		
		m_editNumber.setText(sipNumber);
		changeEditProperty();
	}
	
	private void changeEditProperty()
	{
		m_editNumber.setEnabled(m_bEditMode);
		m_editPassword.setEnabled(m_bEditMode);
		m_editName.setEnabled(m_bEditMode);
	}
	
	private void showAccountInfo()
	{
		m_editNumber.setText(mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, NgnConfigurationEntry.DEFAULT_IDENTITY_IMPI));
        m_editName.setText(mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_DISPLAY_NAME, NgnConfigurationEntry.DEFAULT_IDENTITY_DISPLAY_NAME));
        m_editPassword.setText(mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_PASSWORD, NgnStringUtils.emptyValue()));		
	}
	
	private boolean saveAccountInfo()
	{
		String number = m_editNumber.getText().toString().trim();
		String name = m_editName.getText().toString().trim();
		String password = m_editPassword.getText().toString().trim();
		
		if( CheckUtils.isEmpty(number) || CheckUtils.isEmpty(name) || CheckUtils.isEmpty(password) )
		{
			MessageUtils.showMessageDialog(getContext(), "Please input account information");
			return false;
		}
		
        mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU, String.format("sip:%s@%s", number, SipController.SIP_DOMAIN));        
    	mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_DISPLAY_NAME, name);
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI, number);
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD, password);
		
		mConfigurationService.commit();
		
		SipController.changeAccount(getContext());
		
		return true;
	}
	public void initEvents()
	{
		super.initEvents();

		HeaderPage header = (HeaderPage) decorator;
		
		header.setButtonEvents(0, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishView();
			}
		});
		
		header.setButtonEvents(1, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( m_bEditMode == true )
					if( saveAccountInfo() == false)
						return;
				m_bEditMode = !m_bEditMode;					
				changeNavigateButton();
				changeEditProperty();
			}
		});
		
		

	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		showAccountInfo();
	}
}
