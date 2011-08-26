package com.application.android.amazon;

import java.util.List;

import com.application.android.data.BooksDBAdapter;
import com.application.android.data.vo.BookInfo;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BookListingActivity extends ListActivity {
	
	private BooksDBAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	public void onCreate(Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_list);
		this.getListView().setDividerHeight(2);
		dbHelper = new BooksDBAdapter(this);
		dbHelper.open();
		fillData();
		registerForContextMenu(getListView());  
	}
	
	// Create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		return true;
	}

	// Reaction to the menu selection
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			addNewBook();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			addNewBook();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			dbHelper.deleteBook(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void addNewBook() {
		//Intent i = new Intent(this, SongDetails.class);
		//startActivityForResult(i, ACTIVITY_CREATE);
	}

	// ListView and view (row) on which was clicked, position and
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.dialog_layout);
		dialog.setTitle("Custom Dialog");
		
		Button thisButton = (Button) dialog.findViewById(R.id.closedialog);
		thisButton.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        	dialog.dismiss();
            }
        });
		dialog.show();
		
		//Intent i = new Intent(this, SongDetails.class);
		//i.putExtra("SONG_ROWID", id);
		// Activity returns an result if called with startActivityForResult
		
		//startActivityForResult(i, ACTIVITY_EDIT);
	}

	// Called with the result of the other activity
	// requestCode was the origin request code send to the activity
	// resultCode is the return code, 0 is everything is ok
	// intend can be use to get some data from the caller
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

	private void fillData() {
		
		List<BookInfo> bookList = dbHelper.fetchAllBooks();

		Log.d("APPPPPPPPY", bookList.toString());		
		// Now create an array adapter and set it to display using our row
		BookListAdapter songListAdapter = new BookListAdapter(this, bookList);
		
		setListAdapter(songListAdapter);
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	
}
