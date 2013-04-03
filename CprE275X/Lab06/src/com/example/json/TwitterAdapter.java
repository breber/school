package com.example.json;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
			
			holder.twitterDate = (TextView) row.findViewById(R.id.date);
			holder.twitterTweet = (TextView) row.findViewById(R.id.tweet);
			holder.twitterImage = (ImageView) row.findViewById(R.id.image);

			row.setTag(holder);
		} else {
			holder = (TwitterHolder) row.getTag();
		}
		
		TwitterRecord tweet = data.get(position);
		holder.twitterDate.setText(tweet.getDate());
		holder.twitterTweet.setText(tweet.getTweet());
		holder.twitterImage.setImageBitmap(tweet.getImage());
		
		return row;
	}
	
	static class TwitterHolder
	{
		ImageView twitterImage;
		TextView twitterDate;
		TextView twitterTweet;
	}
}