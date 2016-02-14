package com.sip.flymobile.data;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.sip.flymobile.R;
import com.sip.flymobile.data.table.ContactList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import common.library.utils.FileUtils;

public class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper {
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "flymobile.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	Context context = null;
	
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	// we do this so there is only one helper
	private static OrmDatabaseHelper helper = null;
	
	public OrmDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
		this.context = context;
	}
	
	/**
	 * Get the helper, possibly constructing it if necessary. For each call to this method, there should be 1 and only 1
	 * call to {@link #close()}.
	 */
	public static synchronized OrmDatabaseHelper getHelper(Context context) {
		if (helper == null) {
			helper = new OrmDatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}
	

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		String path = getDatabaseFullPath();
		FileUtils.copyAssetFileToSDCard(context, "data/flymobile.db", path);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	private String getDatabaseFullPath() {
		File file = context.getDatabasePath(DATABASE_NAME);       
		String path = file.getAbsolutePath();
		String dir_path = path.substring(0, path.length() - DATABASE_NAME.length() - 1);
		new File(dir_path).mkdirs();
		
		return path;
	}
	
	RuntimeExceptionDao<ContactList, Integer> contactlistRuntimeDao = null;
	public RuntimeExceptionDao<ContactList, Integer> getContactListDao() {
		if (contactlistRuntimeDao == null) {
			contactlistRuntimeDao = getRuntimeExceptionDao(ContactList.class);
		}
		return contactlistRuntimeDao;
	}
	
	@Override
	public void close() {
		if (usageCounter.decrementAndGet() == 0) {
			super.close();
			contactlistRuntimeDao = null;
			helper = null;
		}
	}
}

