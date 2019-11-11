package com.example.firebaseauthenticationtwitter;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class CustomTwitterApiClient extends TwitterApiClient {
    public CustomTwitterApiClient(TwitterSession session){
        super(session);
    }

    public TwitterService getTwitterService(){
        return getService(TwitterService.class);
    }

}
