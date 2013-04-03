package com.example.json;

public class TwitterRecord {
	
	private String date;
	private String tweet;
	
	public TwitterRecord(String date, String tweet) {
		super();
		this.date = date;
		this.tweet = tweet;
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

}
