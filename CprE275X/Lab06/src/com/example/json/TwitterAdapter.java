package com.example.json;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter<TwitterRecord>{

	private Context context;
	private int layoutResourceId;
	private List<TwitterRecord> data = null;

	public TwitterAdapter(Context context, int layoutResourceId, List<TwitterRecord> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TwitterHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new TwitterHolder();
			
			//TODO set the holder view id's

			row.setTag(holder);
		} else {
			holder = (TwitterHolder) row.getTag();
		}
		
		TwitterRecord tweet = data.get(position);

		//TODO set the text for the row

		return row;
	}

	static class TwitterHolder
	{
		TextView twitterDate;
		TextView twitterTweet;
	}
}