package com.example.mymediaplayer;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PrefActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new MediaPreferencesFragment())
				.commit();
	}

	/**
	 * This fragment shows the preferences for the media player
	 */
	public static class MediaPreferencesFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// Make sure default values are applied.
			PreferenceManager.setDefaultValues(getActivity(),
					R.xml.media_preferences, false);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.media_preferences);
		}
	}

}
