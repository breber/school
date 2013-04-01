package com.example.agenda.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class helps with database setup. None of its methods (except for the constructor)
 * should be called directly.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	/*
	 * Constants
	 */

	// Table name
	public static final String TABLE_EVENTS = "events";

	// Table columns
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_START = "start";
	public static final String COLUMN_END = "end";
	public static final String COLUMN_DETAILS = "details";

	// Database name
	private static final String DATABASE_NAME = "agenda.db";

	// Increment this number to clear everything in database
	private static final int DATABASE_VERSION = 3;

	// Constructor for helper. Creates database.
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Called when database is created
	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * COLUMN_ID should be of type "integer primary key autoincrement"
		 * All other columns should be of type "text not null"
		 */
		db.execSQL("CREATE TABLE " + TABLE_EVENTS + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
				COLUMN_TITLE + " TEXT, " +
				COLUMN_LOCATION + " TEXT, " +
				COLUMN_START + " TEXT," +
				COLUMN_END + " TEXT," +
				COLUMN_DETAILS + " TEXT" +
				");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		onCreate(db);
	}

}