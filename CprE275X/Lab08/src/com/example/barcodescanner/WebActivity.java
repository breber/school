package com.example.barcodescanner;

import com.google.zxing.integration.android.IntentIntegrator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebActivity extends Activity {
	/**
	 * Variable to store the main activity.
	 */
	private MainActivity activity;
	
	/**
	 * Variable to store the home button.
	 */
	public Button homeButton;
	
	/**
	 * Variable to store the scan button.
	 */
	public Button scanButton;
	
	/**
	 * Variable to store the web view.
	 */
	public WebView webView;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		/*
		 * This line stores the main activity for later use.
		 */
		activity = MainActivity.activity;
		
		 /*
         * This block of code creates the home button 
         * and designates what it does when clicked.
         */
		this.homeButton = (Button) this.findViewById(R.id.button1);
        this.homeButton.setOnClickListener(new OnClickListener(){

			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
	        	 /** This command ends this now obsolete activity. */
	        	finish();
			}
        });

        /*
         * This block of code creates the rescan button 
         * and designates what it does when clicked.
         */
        this.scanButton = (Button) this.findViewById(R.id.button2);
        this.scanButton.setOnClickListener(new OnClickListener(){

			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
		         
				/* This block of code initializes a new scan. */
			      IntentIntegrator integrator = new IntentIntegrator(activity);
		          integrator.initiateScan();
	        	 
	        	 /** This command ends this now obsolete activity. */
	        	 finish();
				
			}
        });

        	/* This line references the viewer with its properties. */
	        this.webView = (WebView) this.findViewById(R.id.webView1);
	        
	        /*
	         * This line enables JavaScript in the viewer for webpages 
	         * because it is disabled by default.
	         */
	        webView.getSettings().setJavaScriptEnabled(true);
	        
	        /* This line sets up the viewer so it's ready to be used. */
	        webView.setWebViewClient(new WebViewClient());
	        
	        /* This code checks if the content of the bar-code is a url. */
	        if(activity.getContent().contains("http://")||activity.getContent().contains("https://")){
	        	/* This line opens the bar-code as a url. */
	        	webView.loadUrl(activity.getContent());
	        }else{
	        	/* This line sets up the viewer to read the bar-code as text. */
	        	webView.loadData(activity.getContent(), "text/html", null);
	        }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web, menu);
		return true;
	}

}
