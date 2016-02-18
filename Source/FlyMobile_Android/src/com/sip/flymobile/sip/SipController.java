package com.sip.flymobile.sip;

import org.doubango.imsdroid.Engine;
import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnSipSession.ConnectionState;
import org.doubango.ngn.utils.NgnConfigurationEntry;

import android.content.Context;
import android.os.Handler;
import common.library.utils.CheckUtils;


public class SipController {
	private final static String SIP_SERVER_HOST = "14.102.151.153";
	public final static String SIP_DOMAIN = "14.102.151.153";
	private final static int SIP_SERVER_PORT = 5060;
	
	private final static String SIP_USERNAME = "01548768269";
	private final static String SIP_PASSWORD = "123456";
	
	public static void initSipAccount()
	{
		INgnConfigurationService mConfigurationService = Engine.getInstance().getConfigurationService();
		
		// Set credentials
//		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI, SIP_USERNAME);
//		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU, String.format("sip:%s@%s", SIP_USERNAME, SIP_DOMAIN));
//		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD, SIP_PASSWORD);
		mConfigurationService.putString(NgnConfigurationEntry.NETWORK_PCSCF_HOST, SIP_SERVER_HOST);
		mConfigurationService.putInt(NgnConfigurationEntry.NETWORK_PCSCF_PORT, SIP_SERVER_PORT);
		mConfigurationService.putString(NgnConfigurationEntry.NETWORK_REALM, SIP_DOMAIN);
		mConfigurationService.putBoolean(NgnConfigurationEntry.NETWORK_USE_3G, true);
		// VERY IMPORTANT: Commit changes
		mConfigurationService.commit();
		// register (log in)
	}
	
	public static boolean isValidAccount()
	{
		INgnConfigurationService mConfigurationService = Engine.getInstance().getConfigurationService();
		
		String sipNumber = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, "");
		String password = mConfigurationService.getString(NgnConfigurationEntry.IDENTITY_IMPI, "");
		
		if( CheckUtils.isEmpty(sipNumber) || CheckUtils.isEmpty(password) )
			return false;
		
		return true;
	}
	public static boolean isConnectedAccount()
	{
		NgnEngine mEngine = Engine.getInstance();
		INgnSipService mSipService = mEngine.getSipService();
		
		if(mEngine.isStarted() && mSipService.isRegistered() == false )
			return false;
		
		return true;
	}
	public static void register(Context context)
	{
		NgnEngine mEngine = Engine.getInstance();
		INgnSipService mSipService = mEngine.getSipService();
		
		// Starts the engine
		if(!mEngine.isStarted()){
			if(mEngine.start()){
//				mTvLog.setText("Engine started :)");
			}
			else{
//				mTvLog.setText("Failed to start the engine :(");
			}
		}
		// Register
		if(mEngine.isStarted()){
			if(!mSipService.isRegistered()){
				// register (log in)
				mSipService.register(context);
			}
		}
	}
	
	public static void changeAccount(final Context context)
	{
		unregister();
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				register(context);
			}
		}, 2000);
		
	}
	public static void unregister()
	{
		INgnSipService mSipService = Engine.getInstance().getSipService();
		if(mSipService.getRegistrationState() == ConnectionState.CONNECTING || mSipService.getRegistrationState() == ConnectionState.TERMINATING){
			mSipService.stopStack();
		}
		else if(mSipService.isRegistered()){
			mSipService.unRegister();
		}		
	}
	
	public static void setAccount(String username, String password)
	{
		INgnConfigurationService mConfigurationService = Engine.getInstance().getConfigurationService();
		
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI, username);
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU, String.format("sip:%s@%s", username, SIP_DOMAIN));
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD, password);
		
		mConfigurationService.commit();
	}
	
	
}
