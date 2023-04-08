package com.example.scouto.utils;

import android.content.Context;

import com.example.scouto.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {

    /**
     * the email is not validated if...
     * ...email is empty
     * ...email regex do not match
     */
    public static String validateEmail(Context context, String email) {
        if (email.isEmpty())
            return context.getString(R.string.please_enter_email_address);
        Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches())
            return context.getString(R.string.invalid_email_address);
        return "";
    }

    /**
     * the password is not validated if...
     * ...password is empty
     * ...password less than 6 chars
     */
    public static String validatePassword(Context context, String password) {
        if (password.isEmpty())
            return context.getString(R.string.please_enter_password);
        if (password.length() < 6)
            return context.getString(R.string.password_must_be_atleast_six_chars);
        return "";
    }

    /**
     * the confirm password is not validated if...
     * ...password and confirm password do not match
     */
    public static Boolean validateConfirmPassword(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }

}
