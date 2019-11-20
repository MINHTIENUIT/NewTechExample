package com.example.firebaseauthenticationtwitter.Service;

import com.example.firebaseauthenticationtwitter.Utils.RetrofitFactory;

public class IBMServiceFactory {
     public static IBMService getIBMService(){
        return RetrofitFactory.getClient(IBMService.IBM_URL_BASE_API).create(IBMService .class);
    }
}
