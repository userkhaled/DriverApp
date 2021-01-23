package com.eeducationgo.markoupdrivers.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.eeducationgo.markoupdrivers.R;


public class AppShareData {
    private static String AppName = "Uper";

    public AppShareData() {
    }
    //TODO:EMAIL VALID

    public static boolean checkIsEmptyEmailEdieText(EditText editText) {

        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    //TODO:EMAIL VALID
    public static boolean matcherTemplateEmailEditText(EditText editText) {

        return !Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
    }

    //TODO:PASSWORD VALID
    public static boolean checkIsEmptyPasswordEdieText(EditText editText) {

        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    //TODO:PASSWORD Length
    public static boolean checkLengthOFTextPass(EditText editText) {

        return editText.getText().toString().trim().length() < 8;
    }

    //TODO:GET TEXT
    public static String returnTextFromEditText(EditText editText) {
        return editText.getText().toString();
    }//end


    //TODO:Name VALID
    public static boolean isEmptyEditText(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    public static void errorInput(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    //TODO:PHONE VALID
    public static boolean checkIsEmptyPhoneEdieText(EditText editText) {

        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    //TODO:Otp VALID
    public static boolean checkIsEmptyOtpEdieText(String editText) {
        return TextUtils.isEmpty(editText.toString().trim());
    }

    //TODO:String VALID
    public static boolean checkIsEmptyString(String editText) {
        return TextUtils.isEmpty(editText.toString().trim());
    }

    //TODO:password matches VALID
    public static boolean checkIsPasswordFiledIsMatches(EditText editTextPassFirst, EditText editTextPassSecond) {
        return !editTextPassFirst.getText().toString().trim().equals(editTextPassSecond.getText().toString().trim());
    }

    public static void lightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white, activity.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
        }
    }

    public static void statusBarHide(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void setUserName(String name) {
        UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).edit().putString("name", name).apply();
    }

    public static String getUserName() {
        return UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).getString("name", "");
    }

    public static void setUserType(String name) {
        UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).edit().putString("type", name).apply();
    }

    public static String getUserType() {
        return UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).getString("type", "");
    }

    public static void setUserID(String name) {
        UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).edit().putString("id", name).apply();
    }

    public static String getUserID() {
        return UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).getString("id", "");
    }

    public static void setActivityChatOpen(boolean name) {
        UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).edit().putBoolean("open", name).apply();
    }

    public static boolean getActivityChatOpen() {
        return UberDriver.getInstance().getSharedPreferences(AppName, Context.MODE_PRIVATE).getBoolean("open", false);
    }
}
