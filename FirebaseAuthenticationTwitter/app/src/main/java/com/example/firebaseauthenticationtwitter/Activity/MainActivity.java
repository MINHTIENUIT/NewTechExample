package com.example.firebaseauthenticationtwitter.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebaseauthenticationtwitter.Model.Data;
import com.example.firebaseauthenticationtwitter.R;
import com.example.firebaseauthenticationtwitter.Utils.CustomTwitterApiClient;
import com.example.firebaseauthenticationtwitter.Utils.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseAuth mAuth;
    SessionManager<TwitterSession> sessionManager;
    TwitterSession twitterSession;


    private RadarChart radarChart;
    private Button refreshButton;

    private Map<String,RadarEntry> dataNeedChartOfMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setContentView(R.layout.activity_main);

        sessionManager = TwitterCore.getInstance().getSessionManager();
        twitterSession = sessionManager.getActiveSession();
        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout_button);
        radarChart = findViewById(R.id.radar_chart);
        refreshButton = findViewById(R.id.refresh);

        long userId = twitterSession.getUserId();
        CustomTwitterApiClient apiClient = new CustomTwitterApiClient(twitterSession);
        Call<User> call = apiClient.getTwitterService().show(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null){
                    System.out.println("TIEN: " + response.body().screenName);
                    updateUserUI(response.body());
                }else{
                    System.out.println("TIEN" + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Get User Failed",Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getActiveSession() != null){
                    sessionManager.clearActiveSession();
                    mAuth.signOut();
                }

                mAuth.signOut();
                updateLoginUI();
            }
        });

        Data data = loadData();
        dataNeedChartOfMe = data.getDataNeedChart();
        installRadarChart();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            updateLoginUI();
        }
    }

    private void updateLoginUI() {
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    private void updateUserUI(User user){
        Toast.makeText(MainActivity.this, "User: " + user.screenName, Toast.LENGTH_LONG).show();
    }

    private void installRadarChart(){
        radarChart.setBackgroundColor(Color.rgb(220, 240, 247));

        radarChart.getDescription().setEnabled(true);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.YELLOW);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);
        radarChart.setTouchEnabled(false);

//        radarChart.setMarker();

        setData();

        XAxis xAxis = radarChart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dataNeedChartOfMe.keySet()));
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);

        Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setTypeface(tfLight);
        l.setXEntrySpace(20f);
        l.setYEntrySpace(20f);
        l.setTextColor(Color.GREEN);

    }

    private void setData(){
        float mul = 80;
        float min = 20;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();

        entries1.addAll(dataNeedChartOfMe.values());

        RadarDataSet set1 = new RadarDataSet(entries1,"Last Week");
        set1.setColor(Color.rgb(255, 131, 151));
        set1.setFillColor(Color.rgb(255, 219, 222));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        set2.setColor(Color.rgb(196, 255, 138));
        set2.setFillColor(Color.rgb(194, 213, 177));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
//        data.setValueTypeface(tfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        radarChart.setData(data);
        radarChart.invalidate();

        radarChart.animateXY(1400,1400, Easing.EaseInOutQuad);

    }

    private Data loadData(){
        String json = Utils.getAssetJsonData(getApplicationContext());
        Type type = new TypeToken<Data>(){}.getType();
        Data data = new Gson().fromJson(json,type);
        if (data != null){
          return data;
        }
        Toast.makeText(MainActivity.this,"Data null",Toast.LENGTH_SHORT).show();
        return new Data();
    }
}
