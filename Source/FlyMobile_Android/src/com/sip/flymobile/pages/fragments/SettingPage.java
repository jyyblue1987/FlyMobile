package com.sip.flymobile.pages.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sip.flymobile.Const;
import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.HeaderPage;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;


public class SettingPage extends BasePageDecorator {
	ListView	m_listItems = null;
	MyListAdapter	m_adapterSettingList = null;
	
	public SettingPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		m_listItems = (ListView)getContext().findViewById(R.id.list_items);
	}
		
	public void initData()
	{
		super.initData();
		
		HeaderPage header = (HeaderPage) decorator;
		header.setTitle("Setting");
		
		String [] item_name = {
			"Account Settings",
			"Top Up",
			"Privacy",
			"General",
			"Customer Support",
			"Leave Feedback",
			"About"
		};
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < item_name.length; i++)
		{
			JSONObject item = new JSONObject();
			try {
				item.put(Const.REALNAME, item_name[i]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			list.add(item);
		}
				
		m_adapterSettingList = new SettingListAdapter(getContext(), list, R.layout.fragment_list_setting_item, null);
		
		m_listItems.setAdapter(m_adapterSettingList);
	}
	
	class SettingListAdapter extends MyListAdapter {
		public SettingListAdapter(Context context, List<JSONObject> data,
				int resource, ItemCallBack callback) {
			super(context, data, resource, callback);
		}
		@Override
		protected void loadItemViews(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_setting_icon), 135, 135, true);
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_setting_icon), 60, 30, 60, 30, true);
			
			((TextView)ViewHolder.get(rowView, R.id.txt_setting_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(50));
			((TextView)ViewHolder.get(rowView, R.id.txt_setting_name)).setText(item.optString(Const.REALNAME, ""));
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_setting_name), 60, 0, 60, 0, true);
		}	
	}
	

	
}
