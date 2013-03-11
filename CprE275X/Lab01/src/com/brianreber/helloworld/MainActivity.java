package com.brianreber.helloworld;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String[] messages;
	private Random random;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		random = new Random();
		messages = new String[10];
		messages[0] = "Hello Dave!";
		messages[1] = "These are not the Droids you're looking for.";
		messages[2] = "All your Androids are belong to us!";
		messages[3] = "Thank you! \nBut our Android is in another castle.";
		messages[4] = "It's a Me! \nAndroid!";
		messages[5] = "Would you kindly press the button again?";
		messages[6] = "It's dangerous to go alone, take this. \nThe screen doubles as a flashlight!";
		messages[7] = "Hey! Look! Listen! \nPress the button again!";
		messages[8] = "Fus Ro Droid! \n­unrelenting Android­";
		messages[9] = "The apple is a lie!";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void changeText(View view) {
		final TextView text = (TextView) findViewById(R.id.textView);
		text.setText(messages[random.nextInt(10)]);

		RotateAnimation animate = new RotateAnimation(0, 360, text.getWidth() / 2, text.getHeight() / 2);
		animate.setDuration(5000);
		text.setAnimation(animate);
		text.animate();
	}
}
