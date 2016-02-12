package com.sip.flymobile.pages.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.HeaderPage;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;


public class MessageHistoryPage extends BasePageDecorator {
	ListView	m_listItems = null;
	MyListAdapter	m_adapterMessageList = null;
	
	boolean 	m_bEditMode = false;
	public MessageHistoryPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		m_listItems = (ListView)getContext().findViewById(R.id.list_items);
	}
		
	public void layoutControls()
	{
		super.layoutControls();
		
		if( decorator instanceof HeaderPage )
		{
			HeaderPage header = (HeaderPage) decorator;
			header.setButtonVisible(1, View.VISIBLE);
			header.setButtonText(1, "Done");
		}
	}
	
	public void initData()
	{
		super.initData();

		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < 20; i++)
		{
			list.add(new JSONObject());
		}
		
		m_adapterMessageList = new MessageListAdapter(getContext(), list, R.layout.fragment_list_message_history_item, null);
		
		m_listItems.setAdapter(m_adapterMessageList);
	}
	
	public void initEvents()
	{
		super.initEvents();
		
	}
	
	private void deleteMessageHistory(JSONObject item)
	{
		
	}
	
	class MessageListAdapter extends MyListAdapter {
		public MessageListAdapter(Context context, List<JSONObject> data,
				int resource, ItemCallBack callback) {
			super(context, data, resource, callback);
		}
		@Override
		protected void loadItemViews(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.lay_edit_message), 200, 200, true);
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.lay_edit_message), 10, 0, 0, 0, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_remove_icon), 55, 55, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_remove_divider), 3, 145, true);			
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_contact_icon), 40, 20, 0, 20, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_contact_icon), 160, 160, true);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.lay_message_info), 40, 0, 40, 0, true);
			
			((TextView)ViewHolder.get(rowView, R.id.txt_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(50));
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_sent_flag), 72, 40, true);
			((TextView)ViewHolder.get(rowView, R.id.txt_content)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(50));
			
			if( m_bEditMode == true )
				ViewHolder.get(rowView, R.id.lay_edit_message).setVisibility(View.VISIBLE);
			else
				ViewHolder.get(rowView, R.id.lay_edit_message).setVisibility(View.GONE);
			
			ViewHolder.get(rowView, R.id.lay_edit_message).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deleteMessageHistory(item);
				}
			});
		}	
	}
	

	
}
