package com.example.scouto.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

public class FunctionUtils {
    public static void animateView(View view, Long duration, Integer repeat, Techniques techniques) {
        YoYo.with(techniques).duration(duration).repeat(repeat).playOn(view);
    }

    public static void vibrateDevice(Context context, Long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(time);
        }
    }

    public static void focusScreen(View view) {
        view.setOnApplyWindowInsetsListener((view1, windowInsets) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                int imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom;
                view1.setPadding(0, 0, 0, imeHeight);
            }
            return windowInsets;
        });
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void navigate(View view, @Nullable NavDirections action, @Nullable Integer id) {
        if (action == null && id != null)
            Navigation.findNavController(view).navigate(id);
        else if (id == null && action != null)
            Navigation.findNavController(view).navigate(action);
    }

    public static void toast(Context context, String msg, int time) {
        Toast.makeText(context, msg, time).show();
    }

    public static Snackbar snackBar(View view, String msg, int time) {
        return Snackbar.make(view, msg, time);
    }

    public static String getMonthNameFromMonthNumber(int monthNumber) {
        ArrayList<String> monthList = new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        if (monthNumber >= 1 && monthNumber <= 12)
            return monthList.get(monthNumber);
        else return "error";
    }
}
