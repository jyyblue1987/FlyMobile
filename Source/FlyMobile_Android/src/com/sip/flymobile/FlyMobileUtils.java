package com.sip.flymobile;

import org.json.JSONObject;

import common.library.utils.CheckUtils;

public class FlyMobileUtils {
	public static String getDisplayName(JSONObject item)
	{
		if( item == null )
			return "";
		
		String name = item.optString(Const.REALNAME, "");
		if( CheckUtils.isNotEmpty(name) )
			return name;
		
		String mobile = item.optString(Const.USERNAME, "");
		if( CheckUtils.isNotEmpty(mobile) )
			return mobile;
		
		String to_mobile = item.optString(Const.TO, "");
		if( CheckUtils.isNotEmpty(to_mobile) )
			return to_mobile;
		
		return "";
	}
	
	
	public static String getMobieNumber(JSONObject item)
	{
		if( item == null )
			return "";
		
		String mobile = item.optString(Const.USERNAME, "");
		if( CheckUtils.isNotEmpty(mobile) )
			return mobile;
		
		String to_mobile = item.optString(Const.TO, "");
		if( CheckUtils.isNotEmpty(to_mobile) )
			return to_mobile;
		
		return "";
	}
}
