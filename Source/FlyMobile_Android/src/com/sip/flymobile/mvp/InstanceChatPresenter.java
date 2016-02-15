package com.sip.flymobile.mvp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.doubango.imsdroid.Engine;
import org.doubango.ngn.model.NgnHistoryEvent.StatusType;
import org.doubango.ngn.model.NgnHistorySMSEvent;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnHistoryService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnMessagingSession;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.ngn.utils.NgnUriUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sip.flymobile.Const;
import com.sip.flymobile.FlyMobileUtils;
import com.sip.flymobile.data.DBManager;

import common.library.utils.BackgroundTaskUtils;
import common.library.utils.BackgroundTaskUtils.OnTaskProgress;
import common.library.utils.CheckUtils;
import common.library.utils.MyTime;

public class InstanceChatPresenter extends ChatPresenter {
	
	private INgnHistoryService mHistorytService;
	private INgnSipService mSipService;
	INgnConfigurationService mConfigurationService;
	
	public InstanceChatPresenter(ChatView view) {
		super(view);
	
		mHistorytService = Engine.getInstance().getHistoryService();
		mSipService = Engine.getInstance().getSipService();
		mConfigurationService = Engine.getInstance().getConfigurationService();
	}
	
	public void createChatSession(JSONObject contact)
	{
		super.createChatSession(contact);
		
		
		
//		JSONObject profile = ChatController.loadVCard(m_ToUsername);
//	
//		// update profile info
//		try {
//			profile.put(Const.USERNAME, m_ToUsername);
//			profile.put(Const.NICKNAME, contact.optString(Const.NICKNAME, ""));
//			DBManager.addContact(((BaseView)view).getContext(), profile);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		m_NickName = contact.optString(Const.NICKNAME, "");
		
		sendUnsendMessage();
		
		getChatHistory(0);
	}
		
		
	public void getChatHistory(int id)
	{
		String sipNumber = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, NgnConfigurationEntry.DEFAULT_IDENTITY_IMPI);
		String from = sipNumber;
		String to = m_ToUsername;
		
		List<JSONObject> list = DBManager.getIndividualChatHistory(((BaseView)view).getContext(), from, to, 0, id, m_nPageNum);
		view.showChatHistory(list);
		DBManager.setFlagReadChatToAll(((BaseView)view).getContext(), m_ToUsername, 0);
		int count = DBManager.getUnreadMessageCount(((BaseView)view).getContext(), from, false);
		view.displayUnreadMessageCount(count);
	}
	
	public void addChatHistory(int id)
	{
		final int lastID = id;
		new BackgroundTaskUtils(new OnTaskProgress() {
			List<JSONObject> list = null;
			@Override
			public void onProgress() {
				String from = "";
				String to = "";
//				list = DBManager.getIndividualChatHistory(((BaseView)view).getContext(), from, to, 0, lastID, m_nPageNum);
			}
			
			@Override
			public void onFinished() {
				view.addChatHistory(list);
			}
		}).execute();
	}
	
	private void sendUnsendMessage()
	{
		String from = "";
		String to = "";
		List<JSONObject> list = DBManager.getUnsendChatHistory(((BaseView)view).getContext(), from, to, 0);
		for(int i = 0; i < list.size(); i++ )
		{
			JSONObject data = list.get(i);
			if( data == null )
				continue;
			
			int sent = 0;
			
			int type = data.optInt(Const.TYPE, 0);
			switch(type)
			{
			case 0: // text message
				try {
					String message = data.optString(Const.BODY, "");
					
					JSONObject textMessage = new JSONObject();
					textMessage.put(Const.TYPE, 0);
					textMessage.put(Const.BODY, message);
					
//					Message newMessage = new Message();
//					newMessage.setBody(textMessage.toString());
//					newMessage.setSubject("Text");
//					newMessage.setType(Type.chat);
//					
//					if( m_Chat != null )
//					{
//						m_Chat.sendMessage(newMessage);    
//						sent = 2;
//					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				break;
//			case 1: // image file
//			case 2: // audio file
//			case 3: // pdf file
//				String path = data.optString(Const.BODY, "");
//				int ret = ChatController.sendFile(((BaseView)view).getContext(), m_ToUsername, m_ToDomain, path, type + "");
//				if( ret == 0 )
//					sent = 2;
//				break;	
			}
			if( sent == 0 )
				continue;
			
			DBManager.setFlagSendChat(((BaseView)view).getContext(), data, sent);			
		}
	}
	public void sendTextMessage(String message)
	{
		if( CheckUtils.isEmpty(message) )
			return;

		if(mSipService == null || !mSipService.isRegistered()){
			return;
		}

		if( m_bUnSendMessage == true )
			sendUnsendMessage();
		
		int sent = 2;
		boolean ret = false;
		final NgnHistorySMSEvent event = new NgnHistorySMSEvent(m_ToUsername, StatusType.Outgoing);
		event.setContent(message);
		
		final String remotePartyUri = NgnUriUtils.makeValidSipUri(m_ToUsername);
		final NgnMessagingSession imSession = NgnMessagingSession.createOutgoingSession(mSipService.getSipStack(), 
				remotePartyUri);
		if(!(ret = imSession.sendTextMessage(message))){
			event.setStatus(StatusType.Failed);
			sent = 0;
		}
		NgnMessagingSession.releaseSession(imSession);
		
		mHistorytService.addEvent(event);
		
		String sipNumber = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, NgnConfigurationEntry.DEFAULT_IDENTITY_IMPI);
		
		JSONObject data = new JSONObject();
		try {
			data.put(Const.FROM, sipNumber);
			data.put(Const.TO, m_ToUsername);
			data.put(Const.NICKNAME, m_ToUsername);
			data.put(Const.BODY, message);
			data.put(Const.TYPE, 0);	// text message
			data.put(Const.UNREAD, 0);  // unread flag
			data.put(Const.SENT, sent);    // sended
			data.put(Const.DIRECTION, 1); // outgoing
			data.put(Const.GROUP_TYPE, 0);	// 1:1 chatting
			data.put(Const.DATE, MyTime.getCurrentTime());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		long id = DBManager.addChat(((BaseView)view).getContext(), data);
		
		try {
			data.put(Const.ID, id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if( sent == 0)
			m_bUnSendMessage = true;
		else
			m_bUnSendMessage = false;
		
		view.addChat(data);
	}
	
	public void sendImageMessage(String path, int source)
	{
		if( CheckUtils.isEmpty(path) )
			return;
		
		if( source == 1 ) // camera
		{
			String copy_path = ""; 
			try {
				FileUtils.copyFile(new File(path), new File(copy_path));
				path = copy_path;
			} catch (IOException e) {			
				e.printStackTrace();
			}
		}
		
		sendFileMessage(path, 1);
	}
	
	public void sendAudioMessage(String path)
	{
		sendFileMessage(path, 2);
	}
	
	public void sendPDFMessage(String path)
	{
		sendFileMessage(path, 3);
	}
	
	public void sendFileMessage(final String path, final int type)
	{
		if( CheckUtils.isEmpty(path) )
			return;	
	}
	
	public void onReceiveMessage(String newMessage)
	{
		try {
			JSONObject data = new JSONObject(newMessage);
			
			String from = data.optString(Const.TO, "");					
			String chatUser = m_ToUsername;
			
			int count = DBManager.getUnreadMessageCount(((BaseView)view).getContext(), "", false);
			
			if( from.equals(chatUser) == false )	// other user 
			{
				view.displayUnreadMessageCount(count);
				return;
			}
			
			int group_type = data.optInt(Const.GROUP_TYPE, 0);
			if( group_type != 0 )
			{
				view.displayUnreadMessageCount(count);
				return;
			}
			
			DBManager.setFlagReadChat(((BaseView)view).getContext(), data);
			
			view.addChat(data);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getChatState()
	{
		return "";

	}
	
	public String getDisplayName()
	{
		return FlyMobileUtils.getDisplayName(m_ChatUserInfo);
	}

}
