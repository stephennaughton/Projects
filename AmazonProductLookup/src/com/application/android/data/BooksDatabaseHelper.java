package com.application.android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BooksDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "booksdata";
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = 
				"create table books (_id integer primary key autoincrement, "
										+ "title text not null,"	
										+ "author text not null,"
										+ "edition text not null,"
										+ "description text not null,"
										+ "pages text not null,"
										+ "releaseDate text not null,"
										+ "publisher text not null,"
										+ "isbn text not null"
										+");";
	
	public BooksDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
	
	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(BooksDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS books");
		onCreate(database);
	}

}
