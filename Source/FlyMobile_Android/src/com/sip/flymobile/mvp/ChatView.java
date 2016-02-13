package com.sip.flymobile.mvp;

import java.util.List;

import org.json.JSONObject;



public interface ChatView {
	public void showChatHistory(List<JSONObject> list);
	public void addChat(JSONObject message);
	public void addChatHistory(List<JSONObject> list);
	public void displayUnreadMessageCount(int count);
}
