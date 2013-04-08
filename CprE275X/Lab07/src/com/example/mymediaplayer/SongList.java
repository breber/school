package com.example.mymediaplayer;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mymediaplayer.R;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SongList extends ListActivity {
	
	// Songs list
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_list);

		// get all songs from MainActivity's query
		this.songsList = MyMediaPlayerActivity.getSongsList();

		// Adding song_list_items to ListView
		ListAdapter adapter = new SimpleAdapter(this, songsList,
				R.layout.song_list_item, new String[] { "songTitle" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);

		ListView lv = getListView();		
		// listening to single song_list_item click
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// getting song_list_item index
				int songIndex = position;
				
				// Starting new intent
				Intent intent = new Intent(getApplicationContext(), MyMediaPlayerActivity.class);
				
				// Sending songIndex to PlayerActivity
				intent.putExtra("songIndex", songIndex);
				setResult(RESULT_OK, intent);
				
				// Closing SongListView
				finish();
			}
		});

	}
}

