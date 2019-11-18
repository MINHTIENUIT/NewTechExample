package com.example.firebaseauthenticationtwitter.Model;

public class TwitterUser{

	public static TwitterUser NONE_USER = new TwitterUser("");
	private String twitterId;

	public TwitterUser(String twitterId) {
		this.twitterId = twitterId;
	}

	public void setTwitterId(String twitterId){
		this.twitterId = twitterId;
	}

	public String getTwitterId(){
		return twitterId;
	}

	@Override
 	public String toString(){
		return 
			"TwitterUser{" + 
			"twitterId = '" + twitterId + '\'' + 
			"}";
		}
}
