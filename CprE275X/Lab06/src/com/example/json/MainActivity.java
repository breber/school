package com.example.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button search = (Button)findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {
			
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				String username = ((EditText)findViewById(R.id.username)).getText().toString();
				
				JSONRequest request = new JSONRequest();
				request.execute("https://api.twitter.com/1/statuses/user_timeline.json?screen_name=%s&count=20".replace("%s", username));
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * @author jamiekujawa
	 * 
	 */
	class JSONRequest extends AsyncTask<String, Integer, List<TwitterRecord>> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		protected void onProgressUpdate(Integer... progress) {
			// not used
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(List<TwitterRecord> r) {
			Log.e("result:", r.toString());

			setListAdapter(new TwitterAdapter(MainActivity.this, R.layout.rowlayout, r));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<TwitterRecord> doInBackground(String... params) {
			Log.e("", "---doInBackground---");
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			try {
				Log.e("", params[0]);
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				// Good code
				if (statusCode == 200) {
					Log.e("", "---Status Code 200---");
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;

					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}

				} else {
					Log.e(JSONRequest.class.toString(), "Failed to get request");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			List<TwitterRecord> data = new ArrayList<TwitterRecord>();
			try {
				JSONArray result = new JSONArray(builder.toString());

				for (int i = 0; i < result.length(); i++) {
					JSONObject row = result.getJSONObject(i);

					String date = row.getString("created_at");
					String tweet = row.getString("text");
					JSONObject user = row.getJSONObject("user");
					String imageUrl = user.getString("profile_image_url");
					
					InputStream in = new java.net.URL(imageUrl).openStream();
					TwitterRecord record = new TwitterRecord(date, tweet, BitmapFactory.decodeStream(in));
					
					data.add(record);
					Log.e("---Result---:", builder.toString());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			Log.e("---Result---:", builder.toString());
			return data;
		}
	}

}
