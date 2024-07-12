package com.skt.skillup.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skt.skillup.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(R.anim.scale_in, R.anim.fade_in);
                finish();
            }, 300);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.scale_in_simple, 0);
                finish();
            }, 400);
        }
    }
}