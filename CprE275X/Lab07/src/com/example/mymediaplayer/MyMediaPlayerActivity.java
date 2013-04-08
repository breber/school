package com.example.mymediaplayer;


import java.util.ArrayList;
import java.util.HashMap;

import com.example.mymediaplayer.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * CPRE 388 - Labs
 * 
 * Copyright 2013
 */
public class MyMediaPlayerActivity extends Activity {

	// List of Sounds that can be played
	private static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player_main);

		// Populate Songs List (method needs to be implemented)
		populateSongsList();
		
		//TODO initialize MyMediaPlayerActivity
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_player_menu, menu);
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_choose_song:
			// Open SongList to display a list of audio files to play
			//TODO
			
			
			return true;
		case R.id.menu_preferences:
			// Display Settings page
			//TODO
			
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}




	/** 
	 * Get list of info for all sounds to be played
	 */
	public void populateSongsList(){
		//TODO add all songs from audio content URI to this.songsList

		
		
	}

	/**
	 * Get songsList to display in ListView
	 * @return list of Songs 
	 */
	public static ArrayList<HashMap<String, String>> getSongsList(){
		return songsList;
	}

}
