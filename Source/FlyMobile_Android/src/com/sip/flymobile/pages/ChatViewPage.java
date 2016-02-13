package com.sip.flymobile.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sip.flymobile.Const;
import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BasePageDecorator;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.mvp.ChatPresenter;
import com.sip.flymobile.mvp.ChatView;
import com.sip.flymobile.mvp.InstanceChatPresenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.design.utils.ResourceUtils;
import common.image.load.CGSize;
import common.image.load.ImageUtils;
import common.library.utils.ActionUtils;
import common.library.utils.AndroidUtils;
import common.library.utils.CheckUtils;
import common.library.utils.MyTime;
import common.list.adapter.ItemCallBack;
import common.list.adapter.ItemResult;
import common.list.adapter.ViewHolder;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconTextView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;


public class ChatViewPage extends BasePageDecorator implements ChatView {
	PullToRefreshListView			m_listPullItems = null;
	ListView						m_listItems = null;
	ChatHistoryAdapter				m_adapterChatList = null;
	
	EmojiconEditText m_EmojiconEditText = null;
	ImageView m_EmojiButton = null;
	ImageView m_SubmitButton = null;
	
	// Header bar
	Button m_btnBackArrow = null;
	TextView m_txtNotify = null;
	
	TextView m_txtName = null;
	TextView m_txtState = null;
	ImageView m_imgAvastar = null;
	
	ImageView m_imgCallIcon = null;

	ImageView m_imgChatBack = null;
	
	int m_nPageNum = 50;
	
	ChatPresenter presenter = null;

	
	public ChatViewPage(BaseView view)
	{
		super(view);		
	}
	
	private void createPresenter()
	{
		Bundle bundle = getContext().getIntent().getExtras();
		
		if( bundle != null )
		{
			String contactInfo = bundle.getString(INTENT_EXTRA, "");
			try {
				JSONObject contact = new JSONObject(contactInfo);
				
				presenter = new InstanceChatPresenter(this);
//				int group_type = contact.optInt(Const.GROUP_TYPE, 0);
//				if( group_type == 0 ) // 1:1 Chat
//					presenter = new InstanceChatPresenter(getContext());
//				else if( group_type == 2 )
//					presenter = new SMSChatPresenter(getContext());
//				else
//					presenter = new MultiUserChatPresenter(getContext());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void findViews()
	{
		super.findViews();
		
		createPresenter();
		
		m_imgChatBack = (ImageView) getContext().findViewById(R.id.img_chat_back);
		m_listPullItems = (PullToRefreshListView)getContext().findViewById(R.id.lv);
		m_listItems = m_listPullItems.getRefreshableView();
		
		m_EmojiconEditText = (EmojiconEditText) getContext().findViewById(R.id.emojicon_edit_text);		
		m_EmojiButton = (ImageView) getContext().findViewById(R.id.emoji_btn);
		m_SubmitButton = (ImageView) getContext().findViewById(R.id.submit_btn);

		m_btnBackArrow = (Button) getContext().findViewById(R.id.fragment_header).findViewById(R.id.btn_back);
		m_txtNotify = (TextView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.txt_notify_count);
		
		m_txtName = (TextView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.txt_name);
		m_txtState = (TextView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.txt_state);
		
		m_imgAvastar = (ImageView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.img_avastar);
		m_imgCallIcon = (ImageView) getContext().findViewById(R.id.fragment_header).findViewById(R.id.img_call_icon);		
	}
	
	public void layoutControls()
	{
		super.layoutControls();
		
		RelativeLayout layLeft = (RelativeLayout) getContext().findViewById(R.id.fragment_header).findViewById(R.id.lay_back);
		LayoutUtils.setSize(layLeft, 160, 125, true);
		LayoutUtils.setSize(m_btnBackArrow, 50, 44, true);
		
		LayoutUtils.setSize(m_txtNotify, 40, 40, true);
		m_txtNotify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(25));
		LayoutUtils.setMargin(m_txtNotify, 50, 0, 0, 0, true);
		
//		LayoutUtils.setMargin(getContext().findViewById(R.id.fragment_header).findViewById(R.id.lay_info), 0, 10, 0, 10, true);
		
		m_txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(54));
		m_txtState.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(30));
		LayoutUtils.setMargin(m_txtState, 0, 0, 0, 0, true);
		
		LayoutUtils.setSize(m_imgAvastar, 120, 120, true);
		
		LayoutUtils.setSize(m_imgCallIcon, 80, 80, true);
		LayoutUtils.setMargin(m_imgCallIcon, 60, 0, 60, 0, true);
		
		LayoutUtils.setSize(m_EmojiButton, 80, 80, true);
		LayoutUtils.setSize(m_SubmitButton, 120, 90, true);
		
		m_EmojiconEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));
		m_EmojiconEditText.setEmojiconSize(ScreenAdapter.computeHeight(60));
		LayoutUtils.setMargin(m_EmojiButton, 30, 0, 0, 0, true);
		LayoutUtils.setMargin(m_EmojiconEditText, 30, 10, 0, 10, true);
		LayoutUtils.setPadding(m_EmojiconEditText, 20, 0, 20, 8, true);
	}
	
	public void initData()
	{
		super.initData();
		
		getContext().findViewById(R.id.lay_input).setBackgroundColor(Color.rgb(164, 192, 213));	
		
		Bundle bundle = getContext().getIntent().getExtras();
		
		if( bundle != null )
		{
			String contactInfo = bundle.getString(INTENT_EXTRA, "");
			try {
				JSONObject contact = new JSONObject(contactInfo);
				
				presenter.createChatSession(contact);
				showChatUserInfo(contact);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		ActionUtils.closeSoftKeyboard(m_EmojiconEditText, getContext());	
	}
	
	public void initEvents()
	{
		super.initEvents();
		
		m_listPullItems.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView)
			{
				JSONObject last = m_adapterChatList.getLastItem();
				int id = Integer.MAX_VALUE;
				if( last != null )
					id = last.optInt(Const.ID, Integer.MAX_VALUE);
				
				presenter.addChatHistory(id);				
				
			}
			@Override
			public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
			{
				
			}
		});
		
		initEmoticonKeyboradEvents();

		//On submit, add the edittext text to listview and clear the edittext
		m_SubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String newText = m_EmojiconEditText.getText().toString();
				m_EmojiconEditText.getText().clear();
				
				presenter.sendTextMessage(newText);
			}
		});	
		
		ResourceUtils.addClickEffect(m_btnBackArrow);
		m_btnBackArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					
			}
		});
		
		getContext().findViewById(R.id.fragment_header).findViewById(R.id.lay_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				m_btnBackArrow.performClick();	
			}
		});
		m_txtNotify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				m_btnBackArrow.performClick();	
			}
		});
				
		m_imgAvastar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				gotoProfilePage();
			}
		});
		
		ResourceUtils.addClickEffect(m_imgCallIcon);
		m_imgCallIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
//		m_listItems.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				ActionUtils.closeSoftKeyboard(m_EmojiconEditText, getContext());
//				return false;
//			}
//		});
		
		getContext().registerReceiver(mHandleMessageReceiver, new IntentFilter(Const.RECEIVE_MESSAGE_ACTION));
		getContext().registerReceiver(mHandleMessageUpdate, new IntentFilter(Const.UPDATE_MESSAGE_ACTION));
		getContext().registerReceiver(mHandleRosterChangeListener, new IntentFilter(Const.ROSTER_CHANGE_ACTION));
		getContext().registerReceiver(mHandleRosterInfoChangeListener, new IntentFilter(Const.ROSTER_INFO_CHANGE_ACTION));

		
	}
	
	private void initEmoticonKeyboradEvents()
	{
		final View rootView = getContext().findViewById(R.id.root_view);
		
		// Give the topmost view of your activity layout hierarchy. getContext() will be used to measure soft keyboard height
		final EmojiconsPopup popup = new EmojiconsPopup(rootView, getContext());

		//Will automatically set size according to the soft keyboard size        
		popup.setSizeForSoftKeyboard();

		//If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				changeEmojiKeyboardIcon(m_EmojiButton, R.drawable.smiley);
			}
		});

		//If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

			@Override
			public void onKeyboardOpen(int keyBoardHeight) {
				setListToBottom();
			}

			@Override
			public void onKeyboardClose() {
				if(popup.isShowing())
					popup.dismiss();
			}
		});

		//On emoji clicked, add it to edittext
		popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
	            if (m_EmojiconEditText == null || emojicon == null) {
	                return;
	            }

	            int start = m_EmojiconEditText.getSelectionStart();
	            int end = m_EmojiconEditText.getSelectionEnd();
	            if (start < 0) {
	            	m_EmojiconEditText.append(emojicon.getEmoji());
	            } else {
	            	m_EmojiconEditText.getText().replace(Math.min(start, end),
	                        Math.max(start, end), emojicon.getEmoji(), 0,
	                        emojicon.getEmoji().length());
	            }
	        }
		});

		//On backspace clicked, emulate the KEYCODE_DEL key event
		popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(
						0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				m_EmojiconEditText.dispatchKeyEvent(event);
			}
		});
		
		// To toggle between text keyboard and emoji keyboard keyboard(Popup)
		m_EmojiButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//If popup is not showing => emoji keyboard is not visible, we need to show it
				if(!popup.isShowing()){
					
					//If keyboard is visible, simply show the emoji popup
					if(popup.isKeyBoardOpen()){
						popup.showAtBottom();
						changeEmojiKeyboardIcon(m_EmojiButton, R.drawable.ic_action_keyboard);
					}
					
					//else, open the text keyboard first and immediately after that show the emoji popup
					else{
//						m_EmojiButton.setFocusableInTouchMode(true);
//						m_EmojiButton.requestFocus();
						popup.showAtBottomPending();
						final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.showSoftInput(m_EmojiconEditText, InputMethodManager.SHOW_IMPLICIT);
						changeEmojiKeyboardIcon(m_EmojiButton, R.drawable.ic_action_keyboard);
					}
				}
				
				//If popup is showing, simply dismiss it to show the undelying text keyboard 
				else{
					popup.dismiss();
				}
				
				m_EmojiconEditText.setFocusableInTouchMode(true);
				m_EmojiconEditText.requestFocus();
			}
		});	
		
		popup.dismiss();
	}
	
	private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
		iconToBeChanged.setImageResource(drawableResourceId);
	}
	
	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	          String newMessage = intent.getExtras().getString(Const.EXTRA_MESSAGE);
	          if( presenter == null )
	        	  return;
	          presenter.onReceiveMessage(newMessage);				
	      }
	};
	
	private BroadcastReceiver mHandleMessageUpdate = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	    	  String message = intent.getExtras().getString(Const.EXTRA_MESSAGE);
	    	  updateMessageList(message);
	      }
	};
	
	private BroadcastReceiver mHandleRosterChangeListener = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	    	  m_txtState.setText(presenter.getChatState());	    	  
	      }
	};
	
	private BroadcastReceiver mHandleRosterInfoChangeListener = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	    	  String profile = intent.getStringExtra(Const.EXTRA_MESSAGE);
	    	  if( CheckUtils.isEmpty(profile) )
	    		  return;
	    	  
	    	  JSONObject data;
			  try {
					data = new JSONObject(profile);
					 m_txtName.setText("Alias");
			  } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  }    	 
	      }
	};
	
	private void showChatUserInfo(JSONObject contact)
	{
		if( contact == null )
			return;
		
		int group_type = contact.optInt(Const.GROUP_TYPE, 0);
		
		m_txtState.setText(presenter.getChatState());
		m_txtName.setText(presenter.getDisplayName());
		
		Intent intent = new Intent(Const.RECEIVE_MESSAGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, "");
        getContext().sendBroadcast(intent);
	}
	
	public void showChatHistory(List<JSONObject> list)
	{	
//		Contact2WUtils.changeDateFormat(list);
		m_adapterChatList = new ChatHistoryAdapter(getContext(), list, new ItemCallBack() {
			
			@Override
			public void doClick(ItemResult result) {
				// TODO Auto-generated method stub
			}
		});
		
		m_listItems.setAdapter(m_adapterChatList);
		setListToBottom();
	}
	
	public void addChatHistory(List<JSONObject> list)
	{
		m_listPullItems.onRefreshComplete();	
		if( list == null || list.size() < 1 )
			m_listPullItems.setMode(Mode.DISABLED);
		
//		Contact2WUtils.changeDateFormat(list);

		m_adapterChatList.addItemList(list);
		m_listItems.setSelection(list.size());	
	}
	
	public void addChat(JSONObject message)
	{
//		Contact2WUtils.changeDateFormat(message);

		m_adapterChatList.addItemToFirst(message);
		setListToBottom();
	}

	private void setListToBottom()
	{
		m_listItems.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	m_listItems.setSelection(m_adapterChatList.getCount());
		    }
		}, 100);
	}

	@Override
	public void onDestroy( ) {
		getContext().unregisterReceiver(mHandleMessageReceiver);
		getContext().unregisterReceiver(mHandleMessageUpdate);
		getContext().unregisterReceiver(mHandleRosterChangeListener);
		getContext().unregisterReceiver(mHandleRosterInfoChangeListener);
		
		presenter.onChatStop();
		
	}
	
	public class ChatHistoryAdapter extends BaseAdapter {
		private List<JSONObject> lstData = new ArrayList<JSONObject>();
		private LayoutInflater mInflater;
		protected ItemCallBack m_callBack = null;
		
		public ChatHistoryAdapter(Context context, List<JSONObject> data, ItemCallBack callback) {
			super();
			lstData = data;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_callBack = callback;			
		}
		
		public List<JSONObject> getData()
		{
			return lstData;
		}
		@Override
		public int getCount() {		
			int count = lstData.size();

			return count;
		}

		@Override
		public JSONObject getItem(int i) {
			if(lstData==null || lstData.size()<=i) {
				return null;
			}
			return lstData.get(lstData.size() - 1 - i);
		}

		public JSONObject getLastItem() {
			if(lstData==null || lstData.size() < 1) {
				return null;
			}
			return lstData.get(lstData.size() - 1);
		}
		
		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			JSONObject item = getItem(i);
			
			int direction = item.optInt(Const.DIRECTION, 0);
			int type = item.optInt(Const.TYPE, 0);
			if( view == null )
			{
				view = mInflater.inflate(R.layout.fragment_chat_item, null);
			}
			
			layoutControls(view, i);
			
			if( direction == 0 ) // incoming
			{
				setIncomingControls(view, i);
				setIncomingGroupControls(view, i);			
			}
			else	// outgoing
			{
				setOutgointControls(view, i);			
			}
			
			if( type == 0 )
				setTextControls(view, i);
			if( type == 1 ) // image
				setImageControls(view, i);
			else if( type == 2 || type == 3 ) // file
				setFileControls(view, i, type);
			
			setFileProgressControl(view, i);
			
			return view;
		}
		
		private void layoutControls(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			View container = ViewHolder.get(rowView, R.id.lay_container);
			ImageView avastar = (ImageView)ViewHolder.get(rowView, R.id.img_avastar);
			TextView nickname = (TextView) ViewHolder.get(rowView, R.id.txt_name);
			ImageView imgcontent = (ImageView) ViewHolder.get(rowView, R.id.img_content);
			
			TextView body = (TextView) ViewHolder.get(rowView, R.id.txt_body);
			TextView date = (TextView) ViewHolder.get(rowView, R.id.txt_date);
			View filecontainer = ViewHolder.get(rowView, R.id.lay_file_info);
			ImageView fileicon = (ImageView)ViewHolder.get(rowView, R.id.file_icon);
			TextView filename = (TextView) ViewHolder.get(rowView, R.id.txt_filename);
			
			ImageView result_icon = (ImageView) ViewHolder.get(rowView, R.id.icon_result);
			View resultcontainer = ViewHolder.get(rowView, R.id.lay_result_icon);
			
			ProgressBar fileprog = (ProgressBar) ViewHolder.get(rowView, R.id.prog_file);

			LayoutUtils.setSize(container, ScreenAdapter.getDeviceWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, false);
			
			LayoutUtils.setSize(avastar, 110, 110, true);
			((LinearLayout.LayoutParams)avastar.getLayoutParams()).weight = 0.0f;
			
			LayoutUtils.setMargin(container, 10, 10, 0, 10, true);
			
			LayoutUtils.setMargin(nickname, 15, 10, 15, 0, true);
			nickname.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(45));
			
			LayoutUtils.setMargin(imgcontent, 3, 3, 3, 3, true);
			LayoutUtils.setMargin(body, 15, 10, 15, 0, true);
			body.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(45));
			((EmojiconTextView)body).setEmojiconSize(ScreenAdapter.computeHeight(70));
			
			LayoutUtils.setMargin(filecontainer, 30, 10, 30, 0, true);
			LayoutUtils.setSize(fileicon, 81, 85, true);
			
			LayoutUtils.setMargin(filename, 30, 0, 0, 0, true);
			filename.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(45));
			
			LayoutUtils.setMargin(resultcontainer, 30, 10, 30, 2, true);
			
			date.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(35));
			
			LayoutUtils.setMargin(result_icon, 7, 0, 0, 0, true);
			LayoutUtils.setSize(result_icon, 48, 36, true);
			
			LayoutUtils.setSize(fileprog, 40, 40, true);
			
			filename.setTextColor(Color.rgb(14, 47, 85));
			date.setText(item.optString(Const.DISP_DATE, MyTime.getOnlyTime()));
			
			
//			filename.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					showAttatchFile(item);					
//				}
//			});
//			fileicon.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					showAttatchFile(item);
//				}
//			});
		}
		
		private void setIncomingControls(View rowView, int position)
		{
			View container = ViewHolder.get(rowView, R.id.lay_container);
			TextView nickname = (TextView) ViewHolder.get(rowView, R.id.txt_name);
			TextView body = (TextView) ViewHolder.get(rowView, R.id.txt_body);
			TextView date = (TextView) ViewHolder.get(rowView, R.id.txt_date);
			ImageView result_icon = (ImageView) ViewHolder.get(rowView, R.id.icon_result);
			
			container.setBackgroundResource(R.drawable.in_bg);
//			((RelativeLayout.LayoutParams)container.getLayoutParams()).removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, R.id.img_avastar);

			nickname.setTextColor(Color.rgb(229, 17, 217));
			body.setTextColor(Color.rgb(14, 47, 85));
			
			result_icon.setVisibility(View.GONE);
		}
		
		private void setOutgointControls(View rowView, int position)
		{
			View container = ViewHolder.get(rowView, R.id.lay_container);
			ImageView avastar = (ImageView)ViewHolder.get(rowView, R.id.img_avastar);
			TextView nickname = (TextView) ViewHolder.get(rowView, R.id.txt_name);		
			TextView body = (TextView) ViewHolder.get(rowView, R.id.txt_body);
			TextView date = (TextView) ViewHolder.get(rowView, R.id.txt_date);
			ImageView result_icon = (ImageView) ViewHolder.get(rowView, R.id.icon_result);
			
			container.setBackgroundResource(R.drawable.out_bg);
//			((RelativeLayout.LayoutParams)container.getLayoutParams()).removeRule(RelativeLayout.RIGHT_OF);
//			((RelativeLayout.LayoutParams)container.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			
			nickname.setText("");
			nickname.setVisibility(View.GONE);
			avastar.setVisibility(View.INVISIBLE);
			LayoutUtils.setSize(avastar, 0, 110, true);
			((LinearLayout.LayoutParams)avastar.getLayoutParams()).weight = 1.0f;
			
			body.setTextColor(Color.rgb(14, 47, 85));
			
			result_icon.setVisibility(View.VISIBLE);
			
			JSONObject item = getItem(position);
			int sendstate = item.optInt(Const.SENT, 0);
			
//			DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.delivered_icon).cacheOnDisk(false).build();
			
			switch(sendstate)
			{
			case 0:	// not sent
				result_icon.setVisibility(View.VISIBLE);
				result_icon.setImageResource(R.drawable.fail_icon);
				break;
			case 1: // send ok
				result_icon.setVisibility(View.VISIBLE);
				result_icon.setImageResource(R.drawable.msg_state3);
				break;
			case 2:	// delivered
				result_icon.setVisibility(View.VISIBLE);
				result_icon.setImageResource(R.drawable.msg_state2);
				break;
			default:
				result_icon.setVisibility(View.INVISIBLE);
				break;
			}
			
			
		}
				
		private void setIncomingGroupControls(View rowView, int position)
		{
			JSONObject item = getItem(position);
			
			ImageView avastar = (ImageView)ViewHolder.get(rowView, R.id.img_avastar);
			TextView nickname = (TextView) ViewHolder.get(rowView, R.id.txt_name);		
			
			int group_type = item.optInt(Const.GROUP_TYPE, 0);
			if( group_type == 1 ) // group chatting
			{
				nickname.setText(item.optString(Const.NICKNAME, ""));
				nickname.setVisibility(View.VISIBLE);
				avastar.setVisibility(View.VISIBLE);
				
//				DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.contact_bg).build();
//				ImageLoader.getInstance().displayImage(ServerTask.PHONEBOOK_SERVER_URL + "uploads/" + item.optString(Const.AVASTAR, ""), avastar, options);
			}
			else
			{
				nickname.setText("");
				nickname.setVisibility(View.GONE);
				avastar.setVisibility(View.GONE);				
			}
		}
		private void setTextControls(View rowView, int position)
		{	
			JSONObject item = getItem(position);
		
			View container = ViewHolder.get(rowView, R.id.lay_container);
			ImageView imgcontent = (ImageView) ViewHolder.get(rowView, R.id.img_content);
			View filecontainer = ViewHolder.get(rowView, R.id.lay_file_info);
			TextView body = (TextView) ViewHolder.get(rowView, R.id.txt_body);
						
			imgcontent.setVisibility(View.GONE);
			filecontainer.setVisibility(View.GONE);
			body.setVisibility(View.VISIBLE);
			
			String message = item.optString(Const.BODY, "Welcome to contact2w");
			body.setText(message);
			
			Paint p = new Paint();
			p.setTextSize(ScreenAdapter.computeHeight(45));
			float mt = p.measureText(message);
			if( mt < ScreenAdapter.getDeviceWidth() * 4 / 5 - ScreenAdapter.computeWidth(30) )
				LayoutUtils.setSize(container, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			else 
				LayoutUtils.setSize(container, ScreenAdapter.getDeviceWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, false);
			
		}
		
		private void setImageControls(View rowView, int position)
		{
			JSONObject item = getItem(position);
			
			ImageView imgcontent = (ImageView) ViewHolder.get(rowView, R.id.img_content);
			View filecontainer = ViewHolder.get(rowView, R.id.lay_file_info);
			
			TextView body = (TextView) ViewHolder.get(rowView, R.id.txt_body);
			
			imgcontent.setVisibility(View.VISIBLE);
			filecontainer.setVisibility(View.GONE);
			body.setVisibility(View.GONE);
			
			adjustImageSize(rowView, item);			
		}
		
		private void setFileProgressControl(View rowView, int position)
		{
			JSONObject item = getItem(position);
			
			ProgressBar fileprog = (ProgressBar) ViewHolder.get(rowView, R.id.prog_file);
			
			int type = item.optInt(Const.TYPE, 0);
			if( type == 0 ) // chat
				fileprog.setVisibility(View.GONE);
			else
			{
				int sent = item.optInt(Const.SENT); 
				if( sent < 3 )
					fileprog.setVisibility(View.GONE);
				else
					fileprog.setVisibility(View.VISIBLE);
			}			
		}
		
		private void setFileControls(View rowView, int position, int type)
		{
			JSONObject item = getItem(position);
			View container = ViewHolder.get(rowView, R.id.lay_container);
			
			ImageView imgcontent = (ImageView) ViewHolder.get(rowView, R.id.img_content);
			View filecontainer = ViewHolder.get(rowView, R.id.lay_file_info);
			TextView body = (TextView) ViewHolder.get(rowView, R.id.txt_body);
			ImageView fileicon = (ImageView)ViewHolder.get(rowView, R.id.file_icon);
			TextView filename = (TextView) ViewHolder.get(rowView, R.id.txt_filename);
						
			imgcontent.setVisibility(View.GONE);
			filecontainer.setVisibility(View.VISIBLE);
			body.setVisibility(View.GONE);
			
			final String path = item.optString(Const.BODY, "Welcome to healthcare");
			File file = new File(path);
			
			filename.setText(file.getName());
			DisplayImageOptions options = null;
//			switch(type)
//			{
//			case 2: // record
//				options = ImageUtils.buildUILOption(R.drawable.record_icon).cacheOnDisk(false).build();
//				ImageLoader.getInstance().displayImage("drawable://" + R.drawable.record_icon, fileicon, options);
//				break;
//			case 3: // pdf
//				options = ImageUtils.buildUILOption(R.drawable.pdf_icon).cacheOnDisk(false).build();
//				ImageLoader.getInstance().displayImage("drawable://" + R.drawable.pdf_icon, fileicon, options);
//				break;
//			}
			
			Paint p = new Paint();
			p.setTextSize(ScreenAdapter.computeHeight(45));
			float mt = p.measureText(file.getName());
			if( mt < ScreenAdapter.getDeviceWidth() * 4 / 5 - ScreenAdapter.computeWidth(171) )
				LayoutUtils.setSize(container, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			else 
				LayoutUtils.setSize(container, ScreenAdapter.getDeviceWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, false);
		}
	
		private void adjustImageSize(View rowView, JSONObject item)
		{
			final ImageView imgcontent = (ImageView) ViewHolder.get(rowView, R.id.img_content);
			
			LayoutUtils.setSize(imgcontent, 200, 200, true);
			
			if( item == null )
			{
				ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_launcher, imgcontent);
				return;
			}
			
			final String path = item.optString(Const.BODY, "Welcome to contact2w");
			int sent = item.optInt(Const.SENT, 3);
			int direction = item.optInt(Const.DIRECTION, 0);
			
			
			
			View container = ViewHolder.get(rowView, R.id.lay_container);
			LayoutUtils.setSize(container, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			
			CGSize size = ImageUtils.getImageSize(path);
			
			if( sent != 2 && direction == 0 ) // incoming not ok
			{
				ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_launcher, imgcontent);
				return;
			}
			
			if( size.width > 0 && size.height > 0 )
			{	
				imgcontent.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						AndroidUtils.showImageInGallery(getContext(), path);
					}
				});
				
//				if( m_isPhotoPreview == true )
				{
					try {
						
						int maxWidth = ScreenAdapter.getDeviceWidth() * 2 / 3;
						int maxHeight = ScreenAdapter.getDeviceHeight() / 4;
						
						float standardRatio = (float)maxHeight / maxWidth;
						float ratio = (float)size.height / size.width;
						
						int width = 0;
						int height = 0;
						
						if( ratio > standardRatio ) // vertial narrow
						{
							width = size.width * maxHeight / size.height;
							height = maxHeight;
						}
						else
						{
							width = maxWidth;
							height = size.height * maxWidth / size.width;
						}
						
						LayoutUtils.setSize(imgcontent, width, height, false);
						
//						ImageSize targetImageSize = new ImageSize(width, height);
						
						ImageLoader.getInstance().displayImage("file://" + path, imgcontent, new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_launcher, imgcontent);								
							}
							
							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
							}
						});
					} catch(OutOfMemoryError e){
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}			
//				else
//					ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_launcher, imgcontent);
			}
			else
				ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_launcher, imgcontent);
		}
		public void addItemList(List<JSONObject> data)
		{
			if( data == null || data.size() < 1 )
				return;
			
			if( lstData == null )
			{
				lstData = data;
			}
			else
			{
				for( int i = 0; i < data.size(); i++ )
					lstData.add(data.get(i));			
			}
			notifyDataSetChanged();
		}
		
		public void addItemToFirst(JSONObject data)
		{
			if( data == null )
				return;
			
			if( lstData == null )
			{
				lstData = new ArrayList<JSONObject>();
				lstData.add(data);
			}
			else
			{
				lstData.add(0, data);			
			}
			notifyDataSetChanged();
		}
		
		public void replaceItemList(List<JSONObject> data, int start)
		{
			if( data == null || data.size() < 1 )
				return;
			
			if( lstData == null )
			{
				lstData = data;
			}
			else
			{
				if( start >= lstData.size() )
					lstData.addAll(data);
				else
				{
					int count = Math.min(lstData.size() - start, data.size());
					for(int i = 0; i < count; i++ )
					{
						lstData.remove(start);
					}
					lstData.addAll(start, data);
				}
				
			}
			notifyDataSetChanged();
		}

	}

	@Override
	public void displayUnreadMessageCount(int count) {
		if( count < 1 )
			m_txtNotify.setVisibility(View.INVISIBLE);
		else
		{
			m_txtNotify.setVisibility(View.VISIBLE);
			if( count < 10 )
				m_txtNotify.setText(count + "");
			else
				m_txtNotify.setText("!");
		}
			
	}
	
	private void gotoProfilePage()
	{
		Bundle bundle = new Bundle();				
		
//		String to_username = presenter.getChatUserName();
//		bundle.putString(INTENT_EXTRA, to_username);
//		ActivityManager.changeActivity(getContext(), ProfileActivity.class, bundle, false, null );

	}
	
	private void updateMessageList(String message)
	{
		if(CheckUtils.isEmpty(message) )
			return;
		
	    try {
			JSONObject data = new JSONObject(message);
			if( m_adapterChatList == null )
				return;
			
			List<JSONObject> list = m_adapterChatList.getData();
			if( list == null )
				return;
			
			int messageID = data.optInt(Const.ID, -1);
			int sent = data.optInt(Const.SENT, -1);
			String body = data.optString(Const.BODY, "");
			
			for(int i = 0; i < list.size(); i++ )
			{
				JSONObject item = list.get(i);
				if( item == null )
					continue;
				
				if( item.optInt(Const.ID) == messageID )
				{
					item.put(Const.BODY, body);
					item.put(Const.SENT, sent);
					break;
				}
			}
			  
			m_adapterChatList.notifyDataSetChanged();				
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    }
	}	
}
