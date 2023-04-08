package com.example.scouto.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.example.scouto.databinding.ActivitySplashScreenBinding;
import com.example.scouto.ui.authentication.AuthenticationActivity;
import com.example.scouto.ui.home.HomeActivity;
import com.example.scouto.utils.FunctionUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideStatusBar();
        FunctionUtils.animateView(binding.appNameTxt, 1000L, 0, Techniques.FadeIn);
        delayScreen();
    }

    private void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void delayScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(this::checkCurrentUser, 1500);
    }

    private void checkCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, AuthenticationActivity.class));
        }
        finish();
    }
}
