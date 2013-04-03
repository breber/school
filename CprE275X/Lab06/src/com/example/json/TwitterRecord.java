package com.example.json;

import android.graphics.Bitmap;

public class TwitterRecord {
	
	private String date;
	private String tweet;
	private Bitmap image;
	
	public TwitterRecord(String date, String tweet, Bitmap image) {
		super();
		this.date = date;
		this.tweet = tweet;
		this.image = image;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
