package com.application.android.data;

import java.util.ArrayList;
import java.util.List;

import com.application.android.data.vo.BookInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BooksDBAdapter {
	
	private Context context;
	private SQLiteDatabase database;
	private BooksDatabaseHelper dbHelper;

	private static final String DATABASE_TABLE = "books";
	
	public BooksDBAdapter(Context context) {
		this.context = context;
	}

	public BooksDBAdapter open() throws SQLException {
		dbHelper = new BooksDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Add a new song to the database 
	 * @param songInformation
	 * @return
	 */
	public long createBook(BookInfo bookInformation) {
		ContentValues initialValues = createContentValues(bookInformation);
		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	/** Update and existing song based on rowid 
	 * 
	 * @param rowId
	 * @param songInfo
	 * @return
	 */
	public boolean updateBook(long rowId, BookInfo bookInformation) {
		ContentValues updateValues = createContentValues(bookInformation);
		return database.update(DATABASE_TABLE, updateValues, BookInfo.KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	/**
	 * Deletes song
	 */
	public boolean deleteBook(long rowId) {
		return database.delete(DATABASE_TABLE, BookInfo.KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all todo in the database
	 * 
	 * @return Cursor over all notes
	 */
	public List<BookInfo> fetchAllBooks() {
		
		//createBook(new BookInfo("Agile Estimating and Planning", "Mike Cohn", "Paperback", "Book Description", 324, "2005-08-21", "Prentice Hall", "123455432"));
		
		List<BookInfo> bookList = new ArrayList<BookInfo> ();
		
		Cursor results = database.query(DATABASE_TABLE, null, null, 
					null, null, null, null);	
		
		results.moveToFirst();		
		while (!results.isAfterLast()) {
			BookInfo thisBook = new BookInfo();

			thisBook.setId(results.getInt(0));
			thisBook.setTitle(results.getString(1));
			thisBook.setAuthor(results.getString(2));
			thisBook.setEdition(results.getString(3));
			thisBook.setDescription(results.getString(4));
			thisBook.setPages(new Integer(results.getString(5)));
			thisBook.setReleaseDate(results.getString(6));
			thisBook.setPublisher(results.getString(7));
			thisBook.setIsbn(results.getString(8));	
			
			bookList.add(thisBook);
			
			results.moveToNext();
		}		
		
		results.close();
		
		return bookList;
	}

	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public BookInfo fetchBook(long rowId) throws SQLException {
		
		Log.d("LOOKUPSONG", ":::::" + rowId);
		
		BookInfo thisBook = null;
				
		Cursor result = database.query(true, DATABASE_TABLE, new String[] {
				BookInfo.KEY_ROWID, BookInfo.KEY_TITLE, BookInfo.KEY_AUTHOR, BookInfo.KEY_EDITION, 
				BookInfo.KEY_DESCRIPTION, BookInfo.KEY_PAGES, BookInfo.KEY_RELEASEDATE, BookInfo.KEY_PUBLISHER, BookInfo.KEY_ISBN },
				BookInfo.KEY_ROWID + "=" + rowId, null, null, null, null, null);
		
		if (result != null) {
			result.moveToFirst();
			
			thisBook = new BookInfo();
			
			thisBook.setId(result.getInt(0));
			thisBook.setTitle(result.getString(1));
			thisBook.setAuthor(result.getString(2));
			thisBook.setEdition(result.getString(3));
			thisBook.setDescription(result.getString(4));
			thisBook.setPages(new Integer(result.getString(5)));
			thisBook.setReleaseDate(result.getString(6));
			thisBook.setPublisher(result.getString(7));
			thisBook.setIsbn(result.getString(8));
			
			result.close();
		}		
		return thisBook;
	}

	private ContentValues createContentValues(BookInfo bookInfo) {
		
		ContentValues values = new ContentValues();
		
		values.put(BookInfo.KEY_TITLE, bookInfo.getTitle());
		values.put(BookInfo.KEY_AUTHOR, bookInfo.getAuthor());
		values.put(BookInfo.KEY_EDITION, bookInfo.getEdition());
		values.put(BookInfo.KEY_DESCRIPTION, bookInfo.getDescription());
		values.put(BookInfo.KEY_PAGES, bookInfo.getPages());
		values.put(BookInfo.KEY_RELEASEDATE, bookInfo.getReleaseDate());
		values.put(BookInfo.KEY_PUBLISHER, bookInfo.getPublisher());
		values.put(BookInfo.KEY_ISBN, bookInfo.getIsbn());
		
		return values;
	}	

}
