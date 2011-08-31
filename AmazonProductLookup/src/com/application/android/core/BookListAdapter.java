package com.application.android.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.application.android.data.vo.BookInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

	private static LayoutInflater mInflater;
	
	private Map<Integer, Integer> idMapping = new HashMap<Integer, Integer>();
	/**
	 * Remember our context so we can use it when constructing views.
	 */
	private Context appContext;
	private List<BookInfo> bookList = null;

	public BookListAdapter(Context context, List<BookInfo> bookList) {
		super();
		this.bookList = bookList;
		this.appContext = context;
		
		int i = 0;
		for (BookInfo b:bookList) {
			idMapping.put(i++, b.getId());
		}

		// Setup Layout inflater
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	

	}

	@Override
	public int getCount() {
		return bookList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return bookList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		Log.d("APPPP", "looking for index: " + arg0);
		
		return idMapping.get(arg0);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		if (arg1 == null) {
		  arg1 = mInflater.inflate(R.layout.book_row, null);
		}

		BookInfo availableData = bookList.get(arg0);

		TextView tt = (TextView) arg1.findViewById(R.id.label);

		tt.setText(availableData.getTitle());

		idMapping.put(arg0, availableData.getId());

		return arg1;
	}

}
