package com.example.gestures;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.customgestures.CustomGestureListener;

/**
 * Main activity which represents the middle view.
 */
public class MainActivity extends CustomGestureListener {

	private TextView mTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		super.setGestureDetector(new GestureDetector(this.getApplicationContext(), this));
		super.setLeftRight(ThirdActivity.class, SecondActivity.class);

		mTv = (TextView) findViewById(R.id.tv1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (me.getAction() == MotionEvent.ACTION_UP) {
			mTv.setText("Action UP");
		} else {
			mTv.setText("X: " + me.getX() + " Y: " + me.getY());
		}

		return super.onTouchEvent(me);
	}
}
