package com.volive.klueapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.volive.klueapp.Activities.Login;

public class PreferenceUtils {
    public static String City = "city";
    Intent intent;

    public static String API_KEY = "apikey";
    public static String BASE_PATH = "base_path";
    public static String Language = "lang";
    public static String Currency = "";

    public static String Token_id = "token";
    //user informatio
    public static String User_name = "fname";
    public static String Email = "email";
    public static String Mobile = "mobile";
    public static String Password = "password";
    public static String IMAGE = "image";
    public static String USER_ID = "user_id";
    public static String Shipping_id = "shipping_id";
    public static String LOG_IN = "login";
    public static String LOG_OUT = "logout";
    public static String MINIMUM_PRICE = "min_price";
    public static String MAX_PRICE = "max_price";
    Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public PreferenceUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public void saveString(String strKey, String strValue) {
        editor.putString(strKey, strValue);
        editor.commit();
    }

    public void saveInt(String strKey, int value) {
        editor.putInt(strKey, value);
        editor.commit();
    }

    public void saveLong(String strKey, Long value) {
        editor.putLong(strKey, value);
        editor.commit();
    }

    public void saveFloat(String strKey, float value) {
        editor.putFloat(strKey, value);
        editor.commit();
    }

    public void saveDouble(String strKey, String value) {
        editor.putString(strKey, value);
        editor.commit();
    }

    public void saveBoolean(String strKey, boolean value) {
        editor.putBoolean(strKey, value);
        editor.commit();
    }

    public void removeFromPreference(String strKey) {
        editor.remove(strKey);
    }

    public String getStringFromPreference(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getbooleanFromPreference(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

    public int getIntFromPreference(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }

    public long getLongFromPreference(String strKey) {
        return preferences.getLong(strKey, 0);
    }

    public float getFloatFromPreference(String strKey, float defaultValue) {
        return preferences.getFloat(strKey, defaultValue);
    }

    public double getDoubleFromPreference(String strKey, double defaultValue) {
        return Double.parseDouble(preferences.getString(strKey, "" + defaultValue));
    }

    public String getLanguage() {
        return preferences.getString(Language, "ar");
    }

    public void setLanguage(String lang) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(Language, lang);
        editor.commit();
    }
    public void setCurrency(String currency) {
        // Storing login value as TRUE

        // Storing name in pref
        editor.putString(Language, currency);
        editor.commit();
    }
    public String getCurrency() {
        return preferences.getString(Currency, "0");
    }

    public void logOut() {
        editor.remove(API_KEY);
        editor.remove(BASE_PATH);
        editor.remove(Language);
        editor.remove(User_name);
        editor.remove(Email);
        editor.remove(Mobile);
        editor.remove(Password);
        editor.remove(IMAGE);
        editor.remove(USER_ID);
        editor.remove(LOG_OUT);
        editor.remove(LOG_IN);
        editor.clear();
      //  editor.putString(Language, "ar");
        editor.commit();


    }


}
