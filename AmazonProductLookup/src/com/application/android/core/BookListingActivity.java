package com.application.android.core;

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
	private static final int DETAIL_ID = Menu.FIRST + 1;
	private static final int DELETE_ID = Menu.FIRST + 2;
	
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

//	// Reaction to the menu selection
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.insert:
//			addNewBook();
//			return true;
//		}
//		return super.onMenuItemSelected(featureId, item);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.insert:
//			addNewBook();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	  case DETAIL_ID:
	  	showDetailDialog();
		  fillData();
		  return true;
		case DELETE_ID:
			  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					  .getMenuInfo();
			  dbHelper.deleteBook(info.id);
			  fillData();
			  return true;
		}
		return super.onContextItemSelected(item);
	}

	// ListView and view (row) on which was clicked, position and
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);		
		showDetailDialog();
	}

	private void showDetailDialog() {
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
  }

	private void fillData() {
		
		List<BookInfo> bookList = dbHelper.fetchAllBooks();

		Log.d("APPPPPPPPYTEST", bookList.toString());		
		// Now create an array adapter and set it to display using our row
		BookListAdapter songListAdapter = new BookListAdapter(this, bookList);
		
		setListAdapter(songListAdapter);		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DETAIL_ID, 0, R.string.menu_detail);
		menu.add(0, DELETE_ID, 1, R.string.menu_delete);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	}
	
}
