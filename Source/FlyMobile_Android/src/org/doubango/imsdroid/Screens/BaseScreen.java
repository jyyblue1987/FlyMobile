/* Copyright (C) 2010-2011, Mamadou Diop.
*  Copyright (C) 2011, Doubango Telecom.
*
* Contact: Mamadou Diop <diopmamadou(at)doubango(dot)org>
*	
* This file is part of imsdroid Project (http://code.google.com/p/imsdroid)
*
* imsdroid is free software: you can redistribute it and/or modify it under the terms of 
* the GNU General Public License as published by the Free Software Foundation, either version 3 
* of the License, or (at your option) any later version.
*	
* imsdroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*	
* You should have received a copy of the GNU General Public License along 
* with this program; if not, write to the Free Software Foundation, Inc., 
* 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package org.doubango.imsdroid.Screens;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.Services.IScreenService;
import org.doubango.ngn.utils.NgnStringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public abstract class BaseScreen extends Activity implements IBaseScreen {
	private static final String TAG = BaseScreen.class.getCanonicalName();
	public static enum SCREEN_TYPE {
		// Well-Known
		ABOUT_T,
		AV_QUEUE_T,
		CHAT_T,
		CHAT_QUEUE_T,
		CODECS_T,
		CONTACTS_T,
		DIALER_T,
		FILETRANSFER_QUEUE_T,
		FILETRANSFER_VIEW_T,
		HOME_T,
		IDENTITY_T,
		INTERCEPT_CALL_T,
		GENERAL_T,
		MESSAGING_T,
		NATT_T,
		NETWORK_T,
		PRESENCE_T,
		QOS_T,
		SETTINGS_T,
		SECURITY_T,
		SPLASH_T,
		
		TAB_CONTACTS, 
		TAB_HISTORY_T, 
		TAB_INFO_T, 
		TAB_ONLINE,
		TAB_MESSAGES_T,
		
		
		// All others
		AV_T
	}
	
	protected String mId;
	protected final SCREEN_TYPE mType;
	
	protected final IScreenService mScreenService;

	protected BaseScreen(SCREEN_TYPE type, String id) {
		super();
		mType = type;
		mId = id;
		mScreenService = ((Engine)Engine.getInstance()).getScreenService();
	}

	protected Engine getEngine(){
		return (Engine)Engine.getInstance();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!processKeyDown(keyCode, event)) {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	public String getId() {
		return mId;
	}

	@Override
	public SCREEN_TYPE getType() {
		return mType;
	}

	@Override
	public boolean hasBack() {
		return false;
	}

	@Override
	public boolean back() {
		return mScreenService.back();
	}

    protected String getPath(Uri uri) {
    	try{
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        final String path = cursor.getString(column_index);
	        cursor.close();
	        return path;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
	public static boolean processKeyDown(int keyCode, KeyEvent event) {
		final IScreenService screenService = ((Engine)Engine.getInstance()).getScreenService();
		final IBaseScreen currentScreen = screenService.getCurrentScreen();
		if (currentScreen != null) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
					&& currentScreen.getType() != SCREEN_TYPE.HOME_T) {
				if (currentScreen.hasBack()) {
					if (!currentScreen.back()) {
						return false;
					}
				} else {
					screenService.back();
				}
				return true;
			}
			else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP){
				if(currentScreen.getType() == SCREEN_TYPE.AV_T){
					Log.d(TAG, "intercepting volume changed event");
					if(((ScreenAV)currentScreen).onVolumeChanged((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))){
						return true;
					}
				}
			}			
		}
		return false;
	}
}

