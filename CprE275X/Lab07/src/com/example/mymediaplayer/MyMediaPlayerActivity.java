package com.example.mymediaplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * CPRE 388 - Labs
 *
 * Copyright 2013
 */
public class MyMediaPlayerActivity extends Activity {

	private static final int CHOOSE_SONG = 100;

	// List of Sounds that can be played
	private static List<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	private boolean isPlaying = false;
	private int currentIndex = -1;
	private MediaPlayer mPlayer;
	private boolean buttonPressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyMediaPlayerActivity.this);
		Resources res = getResources();

		String theme = prefs.getString(res.getString(R.string.mp_theme_pref), "dark");

		if ("dark".equals(theme)) {
			setTheme(android.R.style.Theme_Holo);
		} else {
			setTheme(android.R.style.Theme_Holo_Light);
		}

		setContentView(R.layout.media_player_main);

		// Populate Songs List (method needs to be implemented)
		populateSongsList();

		final Button playPauseButton = (Button) findViewById(R.id.playpausebutton);
		playPauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (currentIndex != -1) {
					if (isPlaying) {
						playPauseButton.setBackgroundResource(R.drawable.play);
					} else {
						playPauseButton.setBackgroundResource(R.drawable.pause);
					}

					isPlaying = !isPlaying;
					updatePlaying();
				}
			}
		});

		final Button forwardButton = (Button) findViewById(R.id.forwardbutton);
		forwardButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (currentIndex != -1) {
					// Setup preferences and resources
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyMediaPlayerActivity.this);
					Resources res = getResources();
					boolean shuffle = prefs.getBoolean(res.getString(R.string.mp_shuffle_pref), false);

					if (!shuffle) {
						currentIndex = (currentIndex + 1) % songsList.size();
					} else {
						Random rand = new Random();
						currentIndex = rand.nextInt(songsList.size());
					}

					buttonPressed = true;
					mPlayer.pause();
					mPlayer.stop();
					updatePlaying();
				}
			}
		});

		final Button backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (currentIndex != -1) {
					// Setup preferences and resources
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyMediaPlayerActivity.this);
					Resources res = getResources();
					boolean shuffle = prefs.getBoolean(res.getString(R.string.mp_shuffle_pref), false);

					if (!shuffle) {
						if (currentIndex == 0) {
							currentIndex = (songsList.size() - 1);
						} else {
							currentIndex -= 1;
						}
					} else {
						Random rand = new Random();
						currentIndex = rand.nextInt(songsList.size());
					}

					buttonPressed = true;
					mPlayer.pause();
					mPlayer.stop();
					updatePlaying();
				}
			}
		});

		mPlayer = new MediaPlayer();

		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (!buttonPressed) {
					currentIndex = (currentIndex + 1) % songsList.size();
				}
				updatePlaying();
				buttonPressed = false;
			}
		});
	}

	private void updatePlaying() {
		// Setup preferences and resources
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyMediaPlayerActivity.this);
		Resources res = getResources();

		if (currentIndex != -1) {
			if (!isPlaying) {
				mPlayer.pause();
			} else {
				try {
					mPlayer.setDataSource(songsList.get(currentIndex).get("songPath"));
					mPlayer.prepare();
				} catch (Exception e) {
					e.printStackTrace();
				}

				boolean image = prefs.getBoolean(res.getString(R.string.mp_image_pref), false);

				if (image) {
					ImageView iv = (ImageView)findViewById(R.id.image);
					iv.setImageResource(R.drawable.screenshot075);
				}


				TextView tv = (TextView) findViewById(R.id.songTitle);
				tv.setText(songsList.get(currentIndex).get("songTitle"));
				mPlayer.start();
			}
		}
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
			startActivityForResult(new Intent(this, SongList.class), CHOOSE_SONG);

			return true;
		case R.id.menu_preferences:
			// Display Settings page
			startActivityForResult(new Intent(this, PrefActivity.class), 0);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO:
		if (CHOOSE_SONG == requestCode) {
			currentIndex = data.getIntExtra("songIndex", -1);
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyMediaPlayerActivity.this);
		Resources res = getResources();

		String theme = prefs.getString(res.getString(R.string.mp_theme_pref), "dark");

		if ("dark".equals(theme)) {
			setTheme(android.R.style.Theme_Holo);
		} else {
			setTheme(android.R.style.Theme_Holo_Light);
		}

		recreate();

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Get list of info for all sounds to be played
	 */
	public void populateSongsList() {
		// Queries the External storage audio files and returns results
		Cursor mCursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // The content URI of Audio files
				null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

		mCursor.moveToFirst();

		while (!mCursor.isLast() && !mCursor.isAfterLast()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("songTitle", mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			map.put("songPath", mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
			songsList.add(map);

			mCursor.moveToNext();
		}

		mCursor.close();
	}

	/**
	 * Get songsList to display in ListView
	 *
	 * @return list of Songs
	 */
	public static List<HashMap<String, String>> getSongsList() {
		return songsList;
	}

}
