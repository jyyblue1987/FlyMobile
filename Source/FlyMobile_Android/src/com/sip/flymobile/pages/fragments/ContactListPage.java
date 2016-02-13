package com.sip.flymobile.pages.fragments;

import java.util.ArrayList;
import java.util.List;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.Services.IScreenService;
import org.json.JSONException;
import org.json.JSONObject;

import com.sip.flymobile.Const;
import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.HeaderPage;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.design.utils.ResourceUtils;
import common.library.utils.CheckUtils;
import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;


public class ContactListPage extends BasePageDecorator {
	ListView	m_listItems = null;
	MyListAdapter	m_adapterContactList = null;
	EditText		m_editSearchText = null;
	
	public ContactListPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		m_listItems = (ListView)getContext().findViewById(R.id.list_items);
		m_editSearchText = (EditText) getContext().findViewById(R.id.search_bar).findViewById(R.id.edit_search);
	}
		
	public void layoutControls()
	{
		super.layoutControls();
		
		layoutNavigateControls();
		layoutSearchbarControls();
	}
	
	private void layoutNavigateControls()
	{
		HeaderPage header = (HeaderPage) decorator;
		header.setButtonVisible(0, View.VISIBLE);
		header.setButtonVisible(1, View.VISIBLE);
		
		Button btnLeft = header.getNavigateButton(0);
		LayoutUtils.setSize(btnLeft, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		btnLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(90));
		btnLeft.setBackgroundColor(Color.TRANSPARENT);
		
		Button btnRight = header.getNavigateButton(1);
		LayoutUtils.setSize(btnRight, 65, 65, true);
	}
	
	public void layoutSearchbarControls()
	{
		RelativeLayout layLeft = (RelativeLayout) getContext().findViewById(R.id.search_bar).findViewById(R.id.lay_left);
		Button btnLeft = (Button) getContext().findViewById(R.id.search_bar).findViewById(R.id.btn_left_button);

		LayoutUtils.setSize(layLeft, 160, 125, true);
		LayoutUtils.setSize(btnLeft, 50, 44, true);
		
		LayoutUtils.setPadding(getContext().findViewById(R.id.search_bar).findViewById(R.id.lay_search), 20, 0, 20, 0, true);
		LayoutUtils.setSize(getContext().findViewById(R.id.search_bar).findViewById(R.id.img_search_icon), 45, 45, true);
		
		m_editSearchText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(50));
		LayoutUtils.setMargin(m_editSearchText, 10, 0, 10, 0, true);
		LayoutUtils.setSize(getContext().findViewById(R.id.search_bar).findViewById(R.id.img_del_search), 40, 40, true);		
	}
	
	private void changeNavigateButton()
	{
		HeaderPage header = (HeaderPage) decorator;
		
		header.setTitle("Contact");

		header.setButtonText(0, "+");
		
		Button btnRight = header.getNavigateButton(1);
		btnRight.setBackgroundResource(R.drawable.search);
	}
	
	public void initData()
	{
		super.initData();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < 20; i++)
		{
			JSONObject item = new JSONObject();
			try {
				item.put(Const.ID, i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			list.add(item);
		}
		
		
		changeNavigateButton();		
		showHeaderBar();
		
		m_adapterContactList = new ContactListAdapter(getContext(), list, R.layout.fragment_contact_list_item, null);
		
		m_listItems.setAdapter(m_adapterContactList);
	}
	
	public void initEvents()
	{
		super.initEvents();

		HeaderPage header = (HeaderPage) decorator;
		
		header.setButtonEvents(0, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gotoAddContactPage();
			}
		});
		
		header.setButtonEvents(1, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSearchBar();
			}
		});
		
		getContext().findViewById(R.id.search_bar).findViewById(R.id.lay_left).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showHeaderBar();
			}
		});
		
		ResourceUtils.addClickEffect(getContext().findViewById(R.id.search_bar).findViewById(R.id.img_del_search));
		getContext().findViewById(R.id.search_bar).findViewById(R.id.img_del_search).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_editSearchText.setText("");
			}
		});
		
	}
	
	private void showSearchBar()
	{
		getContext().findViewById(R.id.fragment_header).setVisibility(View.GONE);
		getContext().findViewById(R.id.search_bar).setVisibility(View.VISIBLE);
	}
	
	private void showHeaderBar()
	{
		getContext().findViewById(R.id.fragment_header).setVisibility(View.VISIBLE);
		getContext().findViewById(R.id.search_bar).setVisibility(View.GONE);		
	}
	
	private void gotoAddContactPage()
	{
		IScreenService screenService = ((Engine)Engine.getInstance()).getScreenService();
		screenService.show(AddContactActivity.class);
	}
	class ContactListAdapter extends MyListAdapter implements SectionIndexer{
		public ContactListAdapter(Context context, List<JSONObject> data,
				int resource, ItemCallBack callback) {
			super(context, data, resource, callback);
		}
		@Override
		protected void loadItemViews(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.content), 10, 8, 10, 8, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_photo), 160, 160, true);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_favorite_icon), 100, 100, 0, 0, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_favorite_icon), 50, 50, true);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.lay_contact_info), 0, 20, 0, 20, true);
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.lay_mobile_info), 60, 0, 0, 0, true);
			
			((TextView)ViewHolder.get(rowView, R.id.txt_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(56));
			((TextView)ViewHolder.get(rowView, R.id.txt_mobile)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(35));
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_mobile), 0, 0, 0, 0, true);
			
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_sms_icon), 65, 60, true);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_call_icon), 60, 0, 60, 0, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_call_icon), 60, 60, true);

			String pName = item.optString("pname", "Alias");
			((TextView)ViewHolder.get(rowView, R.id.txt_name)).setText(pName);
			
			String mobile = item.optString("mobile", "");
			((TextView)ViewHolder.get(rowView, R.id.txt_mobile)).setText(mobile);

			LinearLayout header = (LinearLayout) ViewHolder.get(rowView, R.id.section);
			if( CheckUtils.isEmpty(pName) )
				pName = "Alias";
			
			if( item.optInt("favourite", 0) == 0 )
				ViewHolder.get(rowView, R.id.img_favorite_icon).setVisibility(View.GONE);
			else
				ViewHolder.get(rowView, R.id.img_favorite_icon).setVisibility(View.VISIBLE);
			
			
			char firstChar = '0';
			if( CheckUtils.isEmpty(pName) == false )
				firstChar = pName.toUpperCase().charAt(0);
			if (position == 0) {
				setSection(header, pName);
			}
			else
			{
				JSONObject prev_item = getItem(position - 1);
				String preLabel = prev_item.optString("pname", "");
				if( CheckUtils.isEmpty(preLabel) )
					preLabel = "Alias";
				char preFirstChar = preLabel.toUpperCase().charAt(0);
				if (firstChar != preFirstChar) {
					setSection(header, pName);
				}
				else
				{
					header.setVisibility(View.GONE);
				}
			}
			
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.devided_line), 40, 0, 40, 0, true);
			if (position == getCount() -1 ) {
				ViewHolder.get(rowView, R.id.devided_line).setVisibility(View.VISIBLE);
			}
			else
			{
				JSONObject next_item = getItem(position + 1);
				String nextLabel = next_item.optString("pname", "");
				
				char nextFirstChar = '0';
				if( CheckUtils.isEmpty(nextLabel) == false )
					nextFirstChar = nextLabel.toUpperCase().charAt(0);
				
				if (firstChar != nextFirstChar) {
					ViewHolder.get(rowView, R.id.devided_line).setVisibility(View.GONE);
				}
				else
				{
					ViewHolder.get(rowView, R.id.devided_line).setVisibility(View.VISIBLE);
				}
			}

			
			ViewHolder.get(rowView, R.id.img_call_icon).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					gotoCallingPage(item);
				}
			});
			
			ViewHolder.get(rowView, R.id.img_sms_icon).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					gotoSMSPage(item);
				}
			});
			
		}	
		
		private void setSection(LinearLayout header, String label) 
		{
			header.setVisibility(View.VISIBLE);
			TextView text = (TextView)header.findViewById(R.id.txt_section_name);
			header.setBackgroundColor(Color.rgb(145, 145, 145));
			text.setTextColor(Color.WHITE);
			text.setText(label.substring(0, 1).toUpperCase());
			text.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
			text.setGravity(Gravity.CENTER_VERTICAL);
			LayoutUtils.setPadding(text, 80, 0, 0, 0, true);

		}

		@Override
		public int getPositionForSection(int section) {

			for (int i = 0; i < getCount(); i++) {
				JSONObject item = getItem(i);
				String l = item.optString("pname", "");
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
			
			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			return null;
		}
	}
	

	
}
