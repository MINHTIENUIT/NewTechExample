package com.example.firebaseauthenticationtwitter.Utils;

import com.example.firebaseauthenticationtwitter.Service.IBMService;

public class IBMServiceFactory {
    public IBMService getIBMService(){
        return RetrofitFactory.getClient(IBMService.IBM_URL_BASE_API).create(IBMService .class);
    }
}
