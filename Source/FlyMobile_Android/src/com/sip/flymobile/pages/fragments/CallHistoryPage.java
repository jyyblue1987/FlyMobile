package com.sip.flymobile.pages.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.Screens.ScreenAV;
import org.doubango.imsdroid.Utils.DateTimeUtils;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.model.NgnHistoryAVCallEvent.HistoryEventAVFilter;
import org.doubango.ngn.model.NgnHistoryEvent;
import org.doubango.ngn.services.INgnHistoryService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.utils.NgnStringUtils;
import org.doubango.ngn.utils.NgnUriUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sip.flymobile.Const;
import com.sip.flymobile.FlyMobileUtils;
import com.sip.flymobile.R;
import com.sip.flymobile.data.DBManager;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.pages.ChatViewActivity;
import com.sip.flymobile.pages.HeaderPage;
import com.sip.flymobile.pages.MainActivity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.library.utils.AlgorithmUtils;
import common.library.utils.CheckUtils;
import common.library.utils.MessageUtils;
import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;
import common.manager.activity.ActivityManager;


public class CallHistoryPage extends BasePageDecorator {
	ListView	m_listItems = null;
	MyListAdapter	m_adapterCallList = null;
	
	boolean 	m_bEditMode = false;
	private final INgnHistoryService mHistorytService;
	
	public CallHistoryPage(BaseView view)
	{
		super(view);
		mHistorytService = Engine.getInstance().getHistoryService();		
	}
	public void findViews()
	{
		super.findViews();
		
		m_listItems = (ListView)getContext().findViewById(R.id.list_items);
	}
		
	public void layoutControls()
	{
		super.layoutControls();
		
		HeaderPage header = (HeaderPage) decorator;
		header.setButtonVisible(1, View.VISIBLE);
	}
	
	private void changeNavigateButton()
	{
		HeaderPage header = (HeaderPage) decorator;
		if( m_bEditMode == false )
			header.setButtonText(1, "Edit");
		else
			header.setButtonText(1, "Done");
	}
	public void initData()
	{
		super.initData();
		
		HeaderPage header = (HeaderPage) decorator;
		header.setTitle("Recent/Call log");
		
		m_bEditMode = false;
		
		changeNavigateButton();
		
		initCallHistory();
	}
	
	private void initCallHistory()
	{
		List<NgnHistoryEvent> events = mHistorytService.getObservableEvents().filter(new HistoryEventAVFilter());
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < events.size(); i++ )
		{
			NgnHistoryEvent event = events.get(i);
			NgnMediaType type = event.getMediaType();
			if( type != NgnMediaType.Audio && type != NgnMediaType.AudioVideo )
				continue;
			
			String date = DateTimeUtils.getFriendlyDateString(new Date(event.getStartTime()));
			String remoteParty = NgnUriUtils.getDisplayName(event.getRemoteParty());
			String phoneNumber = NgnUriUtils.getValidPhoneNumber(event.getRemoteParty());
			
			JSONObject data = new JSONObject();
			
			try {
				data.put(Const.ID, i);
				data.put(Const.REALNAME, remoteParty);
				if( CheckUtils.isNotEmpty(phoneNumber) )
					data.put(Const.USERNAME, phoneNumber);
				else
					data.put(Const.USERNAME, remoteParty);
					
				data.put(Const.DATE, date);
				data.put(Const.STATE, event.getStatus());
				data.put(Const.TAG, event);
			} catch (JSONException e) {
				e.printStackTrace();
			}		
			
			list.add(data);			
		}
		
		m_adapterCallList = new CallListAdapter(getContext(), list, R.layout.fragment_list_call_history_item, null);
		
		m_listItems.setAdapter(m_adapterCallList);
	}
	public void initEvents()
	{
		super.initEvents();

		HeaderPage header = (HeaderPage) decorator;
		header.setButtonEvents(1, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_bEditMode = !m_bEditMode;					
				changeNavigateButton();
				m_adapterCallList.notifyDataSetChanged();
			}
		});
		
		m_listItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {		
				gotoCallingPage(arg2);
			}
		});

	}

	private void deleteCallHistory(JSONObject data)
	{
		List<JSONObject> list = m_adapterCallList.getData();
		
		for(int i = 0; i < list.size(); i++)
		{
			JSONObject item = list.get(i);
			
			if( item.optInt(Const.ID, -1) == data.optInt(Const.ID, -1) )
			{
				list.remove(i);
				 mHistorytService.getObservableEvents().remove((NgnHistoryEvent)item.opt(Const.TAG));
				m_adapterCallList.notifyDataSetChanged();
				break;
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(mHistorytService.isLoading()){
			Toast.makeText(getContext(), "Loading history...", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void gotoCallingPage(int pos)
	{
		if( pos < 0 )
			return;
		
		JSONObject item = m_adapterCallList.getData().get(pos);
		gotoCallingPage(item);
	}
	
	private void gotoCallingPage(JSONObject item)
	{
		String mobile = item.optString(Const.USERNAME, ""); 
		INgnSipService mSipService = Engine.getInstance().getSipService();
		if(mSipService.isRegistered() && !NgnStringUtils.isNullOrEmpty(mobile))
			ScreenAV.makeCall(mobile, NgnMediaType.Audio);
		else
			MessageUtils.showMessageDialog(getContext().getParent(), "Not connect to SIP service");
	}

	private void gotoDialPage(JSONObject item)
	{
		String mobile = FlyMobileUtils.getMobieNumber(item); 
		MainActivity tab = (MainActivity)getContext().getParent();
		
		if( tab == null )
			return;
		
		tab.selectTab(0);
		
		Intent intent = new Intent(Const.DIAL_NUMBER_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, mobile);
        getContext().sendBroadcast(intent);
	}

	private void gotoSMSPage(JSONObject item)
	{
		Bundle bundle = new Bundle();				
		
		JSONObject cotnact = DBManager.getContact(getContext(), item.optString(Const.USERNAME, ""), 0);
		AlgorithmUtils.bindJSONObject(cotnact, item);
		bundle.putString(INTENT_EXTRA, cotnact.toString());
		ActivityManager.changeActivity(getContext().getParent(), ChatViewActivity.class, bundle, false, null );
	}
	
	class CallListAdapter extends MyListAdapter {
		public CallListAdapter(Context context, List<JSONObject> data,
				int resource, ItemCallBack callback) {
			super(context, data, resource, callback);
		}
		@Override
		protected void loadItemViews(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.lay_edit_call), 180, 200, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_remove_icon), 55, 55, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_remove_divider), 3, 145, true);			
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_contact_icon), 40, 20, 0, 20, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_contact_icon), 160, 160, true);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.lay_call_info), 40, 0, 40, 0, true);
			
			((TextView)ViewHolder.get(rowView, R.id.txt_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(50));
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_call_state), 35, 35, true);
			((TextView)ViewHolder.get(rowView, R.id.txt_call_time)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeWidth(40));
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_call_time), 10, 0, 0, 0, true);
			
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_sms_icon), 92, 80, true);
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_call_icon), 60, 0, 60, 0, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_call_icon), 80, 80, true);
			
			((TextView)ViewHolder.get(rowView, R.id.txt_name)).setText(item.optString(Const.REALNAME, ""));
			((TextView)ViewHolder.get(rowView, R.id.txt_call_time)).setText(item.optString(Const.DATE, ""));
			
			if( m_bEditMode == true )
				ViewHolder.get(rowView, R.id.lay_edit_call).setVisibility(View.VISIBLE);
			else
				ViewHolder.get(rowView, R.id.lay_edit_call).setVisibility(View.GONE);
			
			NgnHistoryEvent event = (NgnHistoryEvent)item.opt(Const.TAG);
			
			switch(event.getStatus()){
				case Outgoing:
					((ImageView)ViewHolder.get(rowView, R.id.img_call_state)).setImageResource(R.drawable.call_state1);
					break;
				case Incoming:
					((ImageView)ViewHolder.get(rowView, R.id.img_call_state)).setImageResource(R.drawable.call_state2);
					break;
				case Failed:
				case Missed:
					((ImageView)ViewHolder.get(rowView, R.id.img_call_state)).setImageResource(R.drawable.call_state3);
					break;
			}
			ViewHolder.get(rowView, R.id.lay_edit_call).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext().getParent());
					alert_confirm.setMessage("Do you want to delete this call history?").setCancelable(false).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						    	deleteCallHistory(item);
						    }
						}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {		        
						        return;
						    }
					});
					AlertDialog alert = alert_confirm.create();
					alert.show();
				}
			});
			
			ViewHolder.get(rowView, R.id.img_call_icon).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					gotoDialPage(item);
				}
			});
			
			ViewHolder.get(rowView, R.id.img_sms_icon).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					gotoSMSPage(item);
				}
			});
		}	
	}

	
}
