package com.example.firebaseauthenticationtwitter.Service;

import com.example.firebaseauthenticationtwitter.Model.DataItem;
import com.example.firebaseauthenticationtwitter.Model.TwitterUser;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IBMService {

    String IBM_URL_BASE_API = "https://service.avnhome.com/ibm";

    @POST("/pi?type=twitter")
    static Call<List<DataItem>> getData(@Body TwitterUser user);

}
