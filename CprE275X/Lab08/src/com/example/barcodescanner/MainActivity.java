package com.example.barcodescanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * @author breber
 */
public class MainActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_scan) {
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.initiateScan();
		} else if (item.getItemId() == android.R.id.home) {
			WebView webView = (WebView) this.findViewById(R.id.webView1);
			webView.setVisibility(View.INVISIBLE);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@SuppressLint("SetJavaScriptEnabled") 
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String contents;
		if (resultCode == RESULT_OK) {
			IntentResult res = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			contents = res.getContents();
		} else {
			contents = "";
		}

		if (contents != null && contents.length() > 0) {
			Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
			WebView webView = (WebView) this.findViewById(R.id.webView1);
			webView.setVisibility(View.VISIBLE);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new WebViewClient());
			
			if (contents.matches("http(s)?://.*")) {
				webView.loadUrl(contents);
			} else {
				webView.loadData(contents, "text/html", null);
			}
			
			getActionBar().setHomeButtonEnabled(true);
		}
	}

}
