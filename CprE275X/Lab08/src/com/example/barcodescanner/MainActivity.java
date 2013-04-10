package com.example.barcodescanner;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author jamiekujawa
 *
 */
public class MainActivity extends Activity {

	/**
	 * Stores the MainActivity to pass on to other activities.
	 */
	public static MainActivity activity;
	
	/**
	 * Stores the returned contents of the barcode.
	 */
	public static String contents;
	
	/**
	 * Represents the button that initiates the scan.
	 */
	public Button scanButton;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * This line stores the main activity in activity
		 * so that it can be passes to other activities.
		 */
		activity = this;
		
		/*
		 * This clock of code sets up the scan button which 
		 * initiates the scan.
		 */
		activity.scanButton = (Button) this.findViewById(R.id.button1);
        activity.scanButton.setOnClickListener(new OnClickListener(){

        	/** This function initializes a scan with the specified message to display if the scanner isn't installed. */
			@Override
			public void onClick(View v) {
		        IntentIntegrator integrator = new IntentIntegrator(activity);
				integrator.initiateScan();
			}
        });
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
	 * Runs on the return of the Barcode Activity.
	 * Gets the contents of the barcode and then 
	 * creates an instance of web activity with the contents.
	 * 
	 * This method is called automatically by android on the return
	 * of the scan intent.
	 * 
	 * @param requestCode represents the request code for the scan
	 * @param result	represents whether the scan was successful and the result ok
	 * @param intent	represent the intent of the scan
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK){
    		IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    		contents = res.getContents();
    	}else{
    		contents = "";
    	}
    	if(contents.length() > 0){
	        Intent i = new Intent(activity, WebActivity.class);
	        startActivity(i);
    	}
	}
	
	public static String getContent(){
    	return contents;
    }
}
