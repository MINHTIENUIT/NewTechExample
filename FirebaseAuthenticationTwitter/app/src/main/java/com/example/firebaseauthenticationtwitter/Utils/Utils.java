package com.example.firebaseauthenticationtwitter.Utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String getAssetJsonData(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("TestData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }

        Log.e("data", json);
        return json;

    }
}
