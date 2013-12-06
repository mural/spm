package com.spm.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author agustin.sgarlata
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_USERS = "users";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TEL = "tel";
	public static final String COLUMN_USERS_UPDATE = "usersUpdate";
	
	private static final String DATABASE_NAME = "spm_02.db";
	private static final int DATABASE_VERSION = 4;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_USERS + "(" + COLUMN_ID
			+ " integer primary key, " + COLUMN_NAME + " text not null, " + COLUMN_TEL + " text, "
			+ COLUMN_USERS_UPDATE + " text);";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		onCreate(db);
	}
	
}
