package com.example.tabs;

import java.util.HashMap;
import java.util.Map;

import com.example.data.SongInformation;
import com.example.data.SongsList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter {

	private Map<Integer, Integer> idMapping = new HashMap<Integer, Integer>();
	/**
	 * Remember our context so we can use it when constructing views.
	 */
	private Context appContext;
	private SongsList songList = null;

	public SongListAdapter(Context context, SongsList songList) {
		super();
		this.songList = songList;
		this.appContext = context;
	}
	
	@Override
	public int getCount() {
		return songList.getSongsList().size();
	}

	@Override
	public Object getItem(int arg0) {
		return songList.getSongsList().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return idMapping.get(arg0);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v;
		LayoutInflater vi = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = vi.inflate(R.layout.artist_row, null);				

		SongInformation availableData = songList.getSongsList().get(arg0);
		
		if (availableData == null) {
			Log.d("TEAST", "NULL availableData");
		}
		
		TextView tt = (TextView) v.findViewById(R.id.label);

		if (tt == null) {
			Log.d("TEAST", "NULL view");
		}
		
		
		tt.setText(availableData.getTitle());
		
		// Add the onclick listener for the row.
		//v.setOnClickListener(this);
		
		idMapping.put(arg0, availableData.getId());
		
		return v;
	}

}
