package com.sip.flymobile.mvp;

import org.json.JSONObject;

import com.sip.flymobile.Const;

public class ChatPresenter implements BasePresenter {
	int m_nPageNum = 30;
	ChatView view = null;
	
	String m_ToUsername = "";
	String m_NickName = "";
	
	boolean m_bUnSendMessage = false;
	
	JSONObject m_ChatUserInfo = null;
	
	public  ChatPresenter(ChatView view)
	{
		this.view = view;
	}
	@Override
	public void initData() {
		
	}
	
	public void createChatSession(JSONObject contact)
	{
		m_ToUsername = contact.optString(Const.USERNAME, "");
		m_ChatUserInfo = contact;		
	}
	
	public void addChatHistory(int id)
	{
		
	}

	public void getChatHistory(int id)
	{
		
	}
	
	public void sendTextMessage(String message)
	{
		
	}
	
	public void sendImageMessage(String path, int source)
	{
		
	}
	
	public void sendAudioMessage(String path)
	{
		
	}
	
	public void sendPDFMessage(String path)
	{
	}
	
	
	public void sendFileMessage(String path, int type)
	{
		
	}
	
	public void onReceiveMessage(String newMessage)
	{
		
	}
	
	public void onChatStart()
	{
		
	}
	
	public void onChatStop()
	{
		
	}
	
	public String getChatUserName()
	{
		return m_ToUsername;
	}
	
	public String getChatState()
	{
		return "";
	}
	
	public String getDisplayName()
	{
		return "";
	}
	
}
