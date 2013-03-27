package com.example.gestures;

import com.example.customgestures.CustomGestureListener;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;


/**
 * Main activity which represents the middle view.
 */
public class FirstGestureActivity extends CustomGestureListener{
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		super.setGestureDetector(new GestureDetector(this));
		super.setLeftRight(ThirdActivity.class, SecondActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
