package com.example.btheartrate;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class SettingsActivity extends PreferenceActivity implements OnGestureListener {

	private GestureDetector gesture = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new MediaPreferencesFragment())
				.commit();

		gesture = new GestureDetector(this);
	}

	/**
	 * This fragment shows the preferences for the media player
	 */
	public static class MediaPreferencesFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// Make sure default values are applied.
			PreferenceManager.setDefaultValues(getActivity(), R.xml.settings,
					false);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.settings);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (gesture != null)
			return gesture.onTouchEvent(me);
		else
			return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// insert code here

		float startX = e1.getX();
		float stopX = e2.getX();

		if (Math.abs(startX - stopX) > 200) {
			finish();
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Sets the gesture detector for the activity
	 *
	 * @param gesture
	 *            the gesture detector specific to the activity
	 */
	public void setGestureDetector(GestureDetector gesture) {
		this.gesture = gesture;
	}
}

