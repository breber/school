package com.example.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Sample Stopwatch Android activity
 *
 */
public class StopWatchActivity extends Activity {

	/**
	 * REFRESH_RATE defines how often we should update the timer to show how
	 * much time has elapsed. refresh every 100 milliseconds
	 */
	private final int REFRESH_RATE = 100;

	private final Handler mHandler = new Handler();

	private long startTime = 0;
	private long accumulatedTime = 0;

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stopwatch);
	}

	/**
	 * This method will start the clock on the main display.
	 *
	 * @param view the current view
	 */
	public void startClick(View view) {
		startTime = System.currentTimeMillis();

		mHandler.post(startTimer);

		showStopButton();
	}

	/**
	 * This method will reset the current stopwatch clock
	 *
	 * @param view
	 *            the current view
	 */
	public void resetClick(View view) {
		accumulatedTime = 0;
		startTime = 0;

		updateTimer(0);
	}

	/**
	 * This method will stop the current stopwatch.
	 *
	 * @param view the current view
	 */
	public void stopClick(View view) {
		accumulatedTime += System.currentTimeMillis() - startTime;
		mHandler.removeCallbacks(startTimer);

		hideStopButton();
	}

	/**
	 * This method will show the stop button when called by hiding the start and
	 * reset button and making the stop button visible.
	 */
	private void showStopButton() {
		Button b = (Button) findViewById(R.id.stopButton);
		b.setVisibility(View.VISIBLE);

		b = (Button) findViewById(R.id.startButton);
		b.setVisibility(View.GONE);

		b = (Button) findViewById(R.id.resetButton);
		b.setVisibility(View.GONE);
	}

	/**
	 * This method will show the start and reset buttons by hiding the stop
	 * button and making the start and reset buttons visible.
	 */
	private void hideStopButton() {
		Button b = (Button) findViewById(R.id.stopButton);
		b.setVisibility(View.GONE);

		b = (Button) findViewById(R.id.startButton);
		b.setVisibility(View.VISIBLE);

		b = (Button) findViewById(R.id.resetButton);
		b.setVisibility(View.VISIBLE);
	}

	/**
	 * Converts the elapsed given time and updates the display
	 *
	 * @param time the time to update the current display to
	 */
	private void updateTimer(long time) {
		System.out.println(time);
		// Convert the input time to hours, minutes, and seconds
		long seconds = (time / 1000) % 60;
		long minutes = (time / 60000) % 60;
		long hours = time / 3600000;

		// Convert the seconds to String and format to ensure it has a leading
		// zero when required
		String sHours = String.format("%02d", hours);
		String sMinutes = String.format("%02d", minutes);
		String sSeconds = String.format("%02d", seconds);

		// Setting the timer text to the elapsed time
		TextView timer = (TextView) findViewById(R.id.timer);
		timer.setText(String.format("%s:%s:%s", sHours, sMinutes, sSeconds));

		String msTime = String.format("%.1f", (time % 1000) / 1000.0);
		TextView ms = (TextView) findViewById(R.id.timerMs);
		ms.setText(msTime.substring(msTime.indexOf('.') + 1));
	}

	/**
	 * Create a Runnable startTimer that makes timer runnable.
	 */
	private final Runnable startTimer = new Runnable() {
		@Override
		public void run() {
			updateTimer((System.currentTimeMillis() - startTime) + accumulatedTime);

			mHandler.postDelayed(this, REFRESH_RATE);
		}
	};

}
