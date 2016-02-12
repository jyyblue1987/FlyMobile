package com.sip.flymobile.pages.fragments;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.Screens.ScreenAV;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.ngn.utils.NgnStringUtils;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.utils.DialerUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.design.utils.ResourceUtils;


public class DialPage extends BasePageDecorator {
	ImageView 	m_imgContactIcon = null;
	TextView 	m_txtMobile = null;
	TextView 	m_txtExpireDate = null;
	TextView	m_txtCredit = null;
	TextView	m_txtChargeCredit = null;
	
	Button		m_btnAddContact = null;
	TextView 	m_txtPrefix = null;
	EditText	m_editDialNumber = null;
	Button		m_btnDel = null;
	
	Button		m_btnSwitchNumber = null;
	TextView	m_txtContactNumber = null;
	
	Button		m_btnNotification = null;
	Button		m_btnDial = null;
	
	int			m_nSelContactNum = 0;
	
	static enum PhoneInputType{
		Numbers,
		Text
	}
	
	private PhoneInputType mInputType;
	private InputMethodManager mInputMethodManager;
	
	public DialPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		
		mInputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		m_imgContactIcon = (ImageView) getContext().findViewById(R.id.img_contact_icon);
		m_txtMobile = (TextView) getContext().findViewById(R.id.txt_mobile);
		m_txtExpireDate = (TextView) getContext().findViewById(R.id.txt_expire_date);
		m_txtCredit = (TextView) getContext().findViewById(R.id.txt_credit_balance);
		m_txtChargeCredit = (TextView) getContext().findViewById(R.id.txt_charge_credit);
		
		m_btnAddContact = (Button) getContext().findViewById(R.id.btn_add_contact);
		m_txtPrefix = (TextView) getContext().findViewById(R.id.txt_prefix);
		m_editDialNumber = (EditText) getContext().findViewById(R.id.edit_dial_number);
		m_btnDel = (Button) getContext().findViewById(R.id.btn_del);
		
		m_btnSwitchNumber = (Button) getContext().findViewById(R.id.btn_switch);
		m_txtContactNumber = (TextView) getContext().findViewById(R.id.txt_contact_number);
		
		m_btnDial = (Button) getContext().findViewById(R.id.btn_dial);
		m_btnNotification = (Button) getContext().findViewById(R.id.btn_noti_callback);
		
	}
		
	public void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_total_self_info), 0, 15, 0, 15, true);
		LayoutUtils.setSize(m_imgContactIcon, 170, 170, true);
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_self_info), 15, 0, 0, 0, true);
		
		m_txtMobile.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(65));
		
		int fontsize = 35;
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_expire), 0, 5, 0, 0, true);
		((TextView)getContext().findViewById(R.id.lbl_expire_date)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(fontsize));
		m_txtExpireDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(fontsize));
		
		LayoutUtils.setMargin(getContext().findViewById(R.id.lay_credit), 0, 5, 0, 0, true);
		((TextView)getContext().findViewById(R.id.lbl_credit_balance)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(fontsize));
		m_txtCredit.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(fontsize));
		LayoutUtils.setMargin(m_txtChargeCredit, 30, 0, 0, 0, true);
		
		m_txtChargeCredit.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(fontsize));
		LayoutUtils.setSize(m_txtChargeCredit, 50, 50, true);
		
		LayoutUtils.setPadding(getContext().findViewById(R.id.lay_dial_number), 20, 20, 20, 20, true);
		LayoutUtils.setSize(m_btnAddContact, 62, 62, true);
		LayoutUtils.setMargin(m_txtPrefix, 25, 0, 0, 0, true);
		LayoutUtils.setMargin(m_editDialNumber, 25, 0, 25, 0, true);
		m_txtPrefix.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(65));
		m_editDialNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(65));
		LayoutUtils.setSize(m_btnDel, 84, 60, true);
		
		((TextView)getContext().findViewById(R.id.lbl_display_number)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(fontsize));
		LayoutUtils.setSize(m_btnSwitchNumber, 50, 50, true);
		LayoutUtils.setMargin(m_txtContactNumber, 20, 0, 0, 0, true);
		m_txtContactNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(45));
		
		
		LayoutUtils.setSize(m_btnDial, 116, 116, true);
		
		LayoutUtils.setMargin(m_btnNotification, 190, 0, 0, 0, true);		
		LayoutUtils.setSize(m_btnNotification, 116, 116, true);
		
	}
	
	public void initData()
	{
		super.initData();

		INgnConfigurationService mConfigurationService = Engine.getInstance().getConfigurationService();
		
		m_editDialNumber.setText("");
		m_nSelContactNum = 0;
		
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_0, "0", "+", DialerUtils.TAG_0, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_1, "1", "", DialerUtils.TAG_1, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_2, "2", "ABC", DialerUtils.TAG_2, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_3, "3", "DEF", DialerUtils.TAG_3, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_4, "4", "GHI", DialerUtils.TAG_4, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_5, "5", "JKL", DialerUtils.TAG_5, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_6, "6", "MNO", DialerUtils.TAG_6, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_7, "7", "PQRS", DialerUtils.TAG_7, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_8, "8", "TUV", DialerUtils.TAG_8, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_9, "9", "WXYZ", DialerUtils.TAG_9, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_star, "*", "", DialerUtils.TAG_STAR, mOnDialerClick);
		DialerUtils.setDialerTextButton(getContext(), R.id.screen_tab_dialer_button_sharp, "#", "", DialerUtils.TAG_SHARP, mOnDialerClick);

		String sipNumber = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, NgnConfigurationEntry.DEFAULT_IDENTITY_IMPI);
		m_txtMobile.setText(sipNumber);
	}
	
	public void initEvents()
	{
		super.initEvents();
		
		m_editDialNumber.setInputType(InputType.TYPE_NULL);
		m_editDialNumber.setFocusable(false);
		m_editDialNumber.setFocusableInTouchMode(false);
		m_editDialNumber.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(mInputType == PhoneInputType.Numbers){
					final boolean bShowCaret = m_editDialNumber.getText().toString().length() > 0;
					m_editDialNumber.setFocusableInTouchMode(bShowCaret);
					m_editDialNumber.setFocusable(bShowCaret);
				}
			}
        });
		
		ResourceUtils.addClickEffect(m_btnDel);
		m_btnDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String number = m_editDialNumber.getText().toString();
				final int selStart = m_editDialNumber.getSelectionStart();
				if(selStart >0){
					final StringBuffer sb = new StringBuffer(number);
					sb.delete(selStart-1, selStart);
					m_editDialNumber.setText(sb.toString());
					m_editDialNumber.setSelection(selStart-1);
				}
			}
		});
		
		ResourceUtils.addClickEffect(m_btnSwitchNumber);
		m_btnSwitchNumber.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showContactNumbers();
			}
		});
		
		ResourceUtils.addClickEffect(m_btnDial);		
		m_btnDial.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				makeVoiceCall(m_editDialNumber.getText().toString());
			}
		});
		
		ResourceUtils.addClickEffect(m_btnNotification);		
		m_btnNotification.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				makeVoiceCall(m_editDialNumber.getText().toString());
			}
		});
		
		getContext().findViewById(R.id.screen_tab_dialer_button_0).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				appendText("+");
				return true;
			}
		});
	}
	
	private void showContactNumbers()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

		String items[] = {"019 000 0000", "015 510 5155"};
		dialog.setSingleChoiceItems(items, m_nSelContactNum, new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int whichButton) {
				m_nSelContactNum = whichButton;						
			
				dialog.dismiss();
			}
		});
			
		
		AlertDialog alertDialog = dialog.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(true);
	}
	
	private final View.OnClickListener mOnDialerClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = Integer.parseInt(v.getTag().toString());
//			final String number = m_editDialNumber.getText().toString();
//			if(tag == DialerUtils.TAG_CHAT){
//				if(mSipService.isRegistered() && !NgnStringUtils.isNullOrEmpty(number)){
//					// ScreenChat.startChat(number);
//					m_editDialNumber.setText(NgnStringUtils.emptyValue());
//				}
//			}
//			else if(tag == DialerUtils.TAG_DELETE){
//				final int selStart = m_editDialNumber.getSelectionStart();
//				if(selStart >0){
//					final StringBuffer sb = new StringBuffer(number);
//					sb.delete(selStart-1, selStart);
//					m_editDialNumber.setText(sb.toString());
//					m_editDialNumber.setSelection(selStart-1);
//				}
//			}
//			else if(tag == DialerUtils.TAG_AUDIO_CALL){
//				if(mSipService.isRegistered() && !NgnStringUtils.isNullOrEmpty(number)){
//					ScreenAV.makeCall(number, NgnMediaType.Audio);
//					m_editDialNumber.setText(NgnStringUtils.emptyValue());
//				}
//			}
//			else if(tag == DialerUtils.TAG_VIDEO_CALL){
//				if(mSipService.isRegistered() && !NgnStringUtils.isNullOrEmpty(number)){
//					ScreenAV.makeCall(number, NgnMediaType.AudioVideo);
//					m_editDialNumber.setText(NgnStringUtils.emptyValue());
//				}
//			}
//			else
			{
				final String textToAppend = tag == DialerUtils.TAG_STAR ? "*" : (tag == DialerUtils.TAG_SHARP ? "#" : Integer.toString(tag));
				appendText(textToAppend);
			}
		}
	};
	
	private void appendText(String textToAppend){
		final int selStart = m_editDialNumber.getSelectionStart();
		final StringBuffer sb = new StringBuffer(m_editDialNumber.getText().toString());
		sb.insert(selStart, textToAppend);
		m_editDialNumber.setText(sb.toString());
		m_editDialNumber.setSelection(selStart+1);
	}
	
	boolean makeVoiceCall(String phoneNumber){
		INgnSipService mSipService = Engine.getInstance().getSipService();
		if(mSipService.isRegistered() && !NgnStringUtils.isNullOrEmpty(phoneNumber)){
			ScreenAV.makeCall(phoneNumber, NgnMediaType.Audio);
			m_editDialNumber.setText(NgnStringUtils.emptyValue());
		}
		return true;
	}
}
