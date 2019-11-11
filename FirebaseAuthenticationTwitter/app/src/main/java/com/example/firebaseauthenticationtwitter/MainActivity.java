package com.example.firebaseauthenticationtwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
                if (sessionManager.getActiveSession() != null){
                    sessionManager.clearActiveSession();
                    mAuth.signOut();
                }

                mAuth.signOut();;
                updateUI();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            updateUI();
        }
    }

    private void updateUI() {
        Toast.makeText(MainActivity.this, "You're logged out", Toast.LENGTH_LONG);
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }
}
