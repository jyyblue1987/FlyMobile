package com.sip.flymobile.data;

import java.util.ArrayList;
import java.util.List;

import org.doubango.imsdroid.Engine;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.json.JSONException;
import org.json.JSONObject;

import com.sip.flymobile.Const;

import android.content.Context;
import android.database.Cursor;
import common.library.utils.CheckUtils;
import common.library.utils.FileUtils;
import common.library.utils.MyTime;

public class DBManager {
	private static final String DATABASE_NAME = "flymobile.db";
	private static final String CONTACTLIST_TABLE = "ContactList";
	private static final String CALLHISTORY_TABLE = "CallHistory";
	private static final String SMSHISTORY_TABLE = "SMSHistory";
	private static final String GROUP_TABLE = "GroupList";
	private static final String CHATHISTORY_TABLE = "ChatHistory";
	private static final String NOTIFICATIONLIST_TABLE = "NotificationList";
	
	private static Context		m_context = null;
	
	static String[] contactlist_FieldNameArray = {"id", "name", "mobile", "reg_state", "thumb_image", "sinch_id",
													"pname", "email", "homeaddr", "housetel", "birthday", "class_id", 
													"group_id", "country_id", "photo_filename", "sync_flag", "username"};
	
	static String[] callhistory_FieldNameArray = {"id", "state", "starttime", "endtime", "to_mobile"};
	static String[] smshistory_FieldNameArray = {"id", "state", "sendtime", "msg_content", "to_mobile"};
	static String[] group_FieldNameArray = {"id", "name", "sync_flag"};

	public static void loadDB(Context context)
	{
		m_context = context;
		
		DatabaseManager db = new DatabaseManager(context);
		String path = db.getDatabaseFullPath(DATABASE_NAME);
		FileUtils.copyAssetFileToSDCard(context, "data/flymobile.db", path);
		
	}
	
	public static long addContact(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		long ret = -1;
		if( isExistContact(context, data) == true )
		{
			String username = data.optString(Const.USERNAME, "");
			int group_type = data.optInt(Const.GROUP_TYPE, 0);
			
			String whereClause = "username = ? and group_type = ?";
			String [] whereArgs = { username, group_type + ""};
			ret = updateRecord(context, CONTACTLIST_TABLE, data, whereClause, whereArgs);
		}
		else
		{
			ret = addRecord(context, CONTACTLIST_TABLE, data);	
		}
		
		return ret;
	}

	public static boolean isExistContact(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return false;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;
				
		String username = data.optString(Const.USERNAME, "");
		int group_type = data.optInt(Const.GROUP_TYPE, 0);
		if( CheckUtils.isEmpty(username) )
		{
			db.CloseDatabase();
			return false;
		}
	
		String whereClause = "username = ? and group_type = ?";
		String []whereArgs = {username, group_type + ""};
			
		Cursor	cursor = db.searchRecord(CONTACTLIST_TABLE, null, whereClause, whereArgs, null, null);
		
		List<JSONObject> list =  db.getRecordData(cursor);
	
		if( list == null || list.size() < 1 )
		{
			db.CloseDatabase();
			return false;			
		}
		
		db.CloseDatabase();

		return true;
	}
	
	public static List<JSONObject> getContactList(Context context, String search)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor	cursor = null;		
		
		String orderBy = "name COLLATE NOCASE ASC";
		String whereClause = "group_type = '0'";
		String []whereArgs = null;
		
		if( CheckUtils.isEmpty(search) == false )
		{
			search = "%" + search + "%";
			whereClause += " and (name LIKE ? or username LIKE ?)";
			String [] args = { search, search };
			whereArgs = args;
		}
		
		cursor = db.searchRecord(CONTACTLIST_TABLE, null, whereClause, whereArgs, orderBy, null);
		
		list = db.getRecordData(cursor);
					
		db.CloseDatabase();
		
		return list;
	}
	
	public static long addCallHistory(Context context, String mobile, int state)
	{
		if( context == null )
			return -1;
		
		INgnConfigurationService mConfigurationService = Engine.getInstance().getConfigurationService();
		String sipNumber = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, NgnConfigurationEntry.DEFAULT_IDENTITY_IMPI);
		
		JSONObject data = new JSONObject();
		
		try {
			data.put(Const.FROM, sipNumber);
			data.put(Const.TO, mobile);
			data.put(Const.STATE, state);
			data.put(Const.DATE, MyTime.getCurrentTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
						
		return addRecord(context, CALLHISTORY_TABLE, data);
	}
	
	public static List<JSONObject> getCallHistory(Context context, String mobile, String search)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor	cursor = null;		
		
		String whereClause = "from_id = ?";
		String []whereArgs = null;
	
		if( CheckUtils.isEmpty(search) == false )
		{
			search = "%" + search + "%";
			whereClause += " and (to_id LIKE ? or name LIKE ?) ";
			String [] args = {mobile, search, search};
			whereArgs = args;
		}
		else
		{	
			String [] args = {mobile};
			whereArgs = args;			
		}
		
		
		String column [] = { Const.TO + " as username", "*", "count(*) as count" };
		cursor = db.searchRecord(CHATHISTORY_TABLE, column, whereClause, whereArgs, Const.TO, "id DESC", null);
		
		list = db.getRecordData(cursor);
		
		db.CloseDatabase();
		
		return list;		
	}
	
	
	public static long addChat(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		
		return addRecord(context, CHATHISTORY_TABLE, data);
	}
	
	public static List<JSONObject> getIndividualChatHistory(Context context, String from, String to, int type, int id, int count)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor	cursor = null;		
		
		String orderBy = "id DESC";
		String limit = "";
		
		if( id == 0 )	// latest
			limit = Integer.MAX_VALUE + "";
		else
			limit = id + "";
			
		String whereClause = "(from_id = ? and to_id = ?) and group_type = ? and id < ?";
		String []whereArgs = {from, to, type + "", limit};
		
		cursor = db.searchRecord(CHATHISTORY_TABLE, null, whereClause, whereArgs, orderBy, count + "");
		
		list = db.getRecordData(cursor);
		
		db.CloseDatabase();
		
		return list;
	}
	
	public static long setFlagReadChatToAll(Context context, String username, int group_type)
	{
		if( context == null || CheckUtils.isEmpty(username) )
			return -1;
		
		String whereClause = Const.TO + " = ? and " + Const.GROUP_TYPE + " = ?";
		String [] whereArgs = { username, group_type + ""};
		
		JSONObject data = new JSONObject();
		try {
			data.put(Const.UNREAD, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return updateRecord(context, CHATHISTORY_TABLE, data, whereClause, whereArgs);
	}
	
	public static int getUnreadMessageCount(Context context, String username, boolean self)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return 0;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return 0;
		
		Cursor	cursor = null;		
		
		String whereClause = "";
		if( self == true )
			whereClause = "to_id = ?";
		else
			whereClause = "to_id != ?";
		String []whereArgs = {username};
			
		String column [] = { "sum(unread) as count"};
		cursor = db.searchRecord(CHATHISTORY_TABLE, column, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
		
		if( list == null || list.size() < 1 )
			return 0;
		
		JSONObject data = list.get(0);
		int count = data.optInt("count", 0);
		
		return count;
	}
	
	public static List<JSONObject> getUnsendChatHistory(Context context, String from, String to, int type)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null )
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
		
		Cursor	cursor = null;		
		
		String whereClause = "(from_id = ? and to_id = ?) and group_type = ? and sent = 0 and direction = 1 and type = 0";
		String []whereArgs = {from, to, type + ""};
		
		cursor = db.searchRecord(CHATHISTORY_TABLE, null, whereClause, whereArgs, null, null);
		
		list = db.getRecordData(cursor);
	
		db.CloseDatabase();
		
		return list;
	}
	
	public static long setFlagSendChat(Context context, JSONObject data, int sent)
	{
		if( context == null || data == null )
			return -1;
		
		String whereClause = Const.ID + " = ?";
		String [] whereArgs = { data.optString(Const.ID, "") };
		
		try {
			data.put(Const.SENT, sent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, data, whereClause, whereArgs);
	}
	
	public static long setFlagReadChat(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		String whereClause = Const.ID + " = ?";
		String [] whereArgs = { data.optString(Const.ID, "") };
		
		JSONObject setdata = new JSONObject();
		
		try {
			data.put(Const.UNREAD, 0);
			setdata.put(Const.UNREAD, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return updateRecord(context, CHATHISTORY_TABLE, setdata, whereClause, whereArgs);
	}
	
	public static long updateRecord(Context context, String table, JSONObject data, String whereClause, String []whereArgs)
	{
		if( context == null || data == null )
			return -1;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;
		
		long result = db.updateRecord(table, data, whereClause, whereArgs);
        
		db.CloseDatabase();
		
		return result;
	}
	
	
	public static long addRecord(Context context, String table, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;
		
		long result = db.addRecord(table, data);
        
		db.CloseDatabase();
		
		return result;
	}
	
	
}
