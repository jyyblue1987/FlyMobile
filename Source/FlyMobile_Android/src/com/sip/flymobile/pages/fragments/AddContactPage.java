package com.sip.flymobile.pages.fragments;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.Services.IScreenService;
import org.json.JSONException;
import org.json.JSONObject;

import com.sip.flymobile.Const;
import com.sip.flymobile.R;
import com.sip.flymobile.data.DBManager;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.HeaderPage;

import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.design.utils.ResourceUtils;


public class AddContactPage extends BasePageDecorator {
	EditText m_editName = null;
	EditText m_editMobile = null;
	Button		m_btnAddContact = null;
	
	IScreenService mScreenService = null;
	public AddContactPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		
		mScreenService = ((Engine)Engine.getInstance()).getScreenService();
		
		m_editName = (EditText) getContext().findViewById(R.id.edit_contact_name);
		m_editMobile = (EditText) getContext().findViewById(R.id.edit_mobile);
		m_btnAddContact = (Button) getContext().findViewById(R.id.btn_add_contact);
	}
		
	public void layoutControls()
	{
		super.layoutControls();
		
		HeaderPage header = (HeaderPage) decorator;
		header.setButtonVisible(0, View.VISIBLE);
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_name), 63, 135, 63, 0, true);
		LayoutUtils.setSize(getContext().findViewById(R.id.img_contact_icon), 160, 160, true);
		LayoutUtils.setMargin(m_editName, 50, 0, 0, 0, true);
		m_editName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(56));
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_divider_line), 45, 36, 45, 0, true);
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_mobile), 63, 60, 63, 0, true);
		LayoutUtils.setSize(getContext().findViewById(R.id.img_call_icon), 130, 130, true);
		LayoutUtils.setMargin(m_editMobile, 80, 0, 0, 0, true);
		m_editMobile.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(56));
		
		LayoutUtils.setMargin(m_btnAddContact, 0, 300, 0, 0, true);
		LayoutUtils.setSize(m_btnAddContact, 542, 136, true);
		m_btnAddContact.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(56));
	}
	
	private void changeNavigateButton()
	{
		HeaderPage header = (HeaderPage) decorator;
		
		header.setTitle("Add New Contact");
	}
	
	public void initData()
	{
		super.initData();		
		
		changeNavigateButton();
	}
	
	public void initEvents()
	{
		super.initEvents();

		ResourceUtils.addClickEffect(m_btnAddContact);
		m_btnAddContact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addContact();
			}
		});
		
		HeaderPage header = (HeaderPage) decorator;
		header.setButtonEvents(0, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishView();
			}
		});
	}
		
	private void addContact()
	{
		String name = m_editName.getText().toString();
		String mobile = m_editMobile.getText().toString();
		
		JSONObject data = new JSONObject();
		
		try {
			data.put(Const.USERNAME, mobile);
			data.put(Const.REALNAME, name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		DBManager.addContact(getContext(), data);
		
		finishView();
	}
}
