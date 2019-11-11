package com.example.firebaseauthenticationtwitter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TwitterLoginButton mTwitterButton;
    private ProgressBar mIndeterminateProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();

        Twitter.initialize(twitterConfig);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mTwitterButton = findViewById(R.id.twitter_login_button);
        mIndeterminateProgressBar = findViewById(R.id.indeterminateProgressBar);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
            }
        };

        UpdateTwitterButton();

        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(LoginActivity.this,"Signed in to twitter successful", Toast.LENGTH_LONG);
                signInToFirebaseWithTwitterSession(result.data);
                mTwitterButton.setVisibility(View.VISIBLE);
                mIndeterminateProgressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Login failed. No internet or No Twitter app found on you phone", Toast.LENGTH_LONG);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mIndeterminateProgressBar.setVisibility(View.GONE);
                UpdateTwitterButton();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterButton.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI();
        }

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void updateUI() {
        Toast.makeText(LoginActivity.this,"You're logged in", Toast.LENGTH_LONG);
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void signInToFirebaseWithTwitterSession(TwitterSession data) {
        AuthCredential credential = TwitterAuthProvider.getCredential(data.getAuthToken().token,
                data.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, "Signed in firebase twitter successful", Toast.LENGTH_LONG);
                        if (!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Auth firebase twitter failed", Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    private void UpdateTwitterButton() {
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null){
            mTwitterButton.setVisibility(View.VISIBLE);
        }else{
            mTwitterButton.setVisibility(View.GONE);
        }
    }
}
