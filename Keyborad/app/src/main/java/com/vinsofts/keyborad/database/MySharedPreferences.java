package com.vinsofts.keyborad.database;

import android.content.Context;

public class MySharedPreferences {

    private static final String HABN = "HABN";


    public static void putIntValue(Context context, String key, int n) {
        android.content.SharedPreferences pref = context.getSharedPreferences(HABN, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.apply();
    }

    public static int getIntValue(Context context, String key) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static int getIntValue(Context context, String key, int defaultValues) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValues);
    }


    public static void putStringValue(Context context, String key, String s) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.apply();
    }


    public static String getStringValue(Context context, String key) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }


    public static void putBooleanValue(Context context, String key, Boolean b) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }


    public static boolean getBooleanValue(Context context, String key) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValues) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValues);
    }


    public static void putLongValue(Context context, String key, long value) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongValue(Context context, String key) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getLong(key, 0);
    }

    public static long getLongValue(Context context, String key, long defaultValues) {
        android.content.SharedPreferences pref = context.getSharedPreferences(
                HABN, Context.MODE_PRIVATE);
        return pref.getLong(key, defaultValues);
    }

}
