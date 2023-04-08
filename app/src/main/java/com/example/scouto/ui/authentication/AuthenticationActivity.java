package com.example.scouto.ui.authentication;

import static com.example.scouto.utils.Constants.BACK_BTN_TIME_OUT;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scouto.R;
import com.example.scouto.databinding.ActivityAuthenticationBinding;
import com.example.scouto.utils.FunctionUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthenticationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideStatusBar();
    }

    private void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private Boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if ((Objects.requireNonNull(getSupportFragmentManager().getPrimaryNavigationFragment()).getChildFragmentManager().getBackStackEntryCount() > 0)) {
            super.onBackPressed();
        } else if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true;
            FunctionUtils.toast(this, getString(R.string.please_click_back_to_exit_app), Toast.LENGTH_SHORT);
            new Handler().postDelayed(this::doubleBackToExitPressedOnce, BACK_BTN_TIME_OUT);
        } else {
            super.onBackPressed();
        }
    }

    private void doubleBackToExitPressedOnce() {
        doubleBackToExitPressedOnce = false;
    }
}