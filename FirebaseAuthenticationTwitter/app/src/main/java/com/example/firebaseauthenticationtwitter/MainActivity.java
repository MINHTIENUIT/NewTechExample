package com.example.firebaseauthenticationtwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseAuth mAuth;
    private final SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
    private final TwitterSession twitterSession = sessionManager.getActiveSession();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout_button);

        long userId = twitterSession.getUserId();

        CustomTwitterApiClient  apiClient = new CustomTwitterApiClient(twitterSession);
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

                mAuth.signOut();;
                updateLoginUI();
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
        Toast.makeText(MainActivity.this, "You're logged out", Toast.LENGTH_LONG).show();
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    private void updateUserUI(User user){
        Toast.makeText(MainActivity.this, "User: " + user.screenName, Toast.LENGTH_LONG).show();
    }
}
