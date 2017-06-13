package com.vinsofts.keyborad.utils;

import android.content.Context;

import com.vinsofts.keyborad.database.MySharedPreferences;


public class GlobalFunction {

    private static final String KEY_FIRST_OPEN = "KEY_FIRST_OPEN";

    public static boolean isFirstOpen(Context context) {
        return MySharedPreferences.getBooleanValue(context, KEY_FIRST_OPEN, true);
    }

    public static void putFirstOpen(Context context) {
        MySharedPreferences.putBooleanValue(context, KEY_FIRST_OPEN, false);
    }

    //----------------------------------


    //    --------------------------------
    private static final String Sounds = "Sounds";

    public static boolean getSounds(Context context) {
        return MySharedPreferences.getBooleanValue(context, Sounds, true);
    }

    public static void putSounds(Context context, boolean values) {
        MySharedPreferences.putBooleanValue(context, Sounds, values);
    }

    //    --------------------------------
    private static final String Vibrate = "Vibrate";

    public static boolean getVibrate(Context context) {
        return MySharedPreferences.getBooleanValue(context, Vibrate, false);
    }

    public static void putVibrate(Context context, boolean values) {
        MySharedPreferences.putBooleanValue(context, Vibrate, values);
    }

    //    --------------------------------
    private static final String extraRow = "extraRow";

    public static boolean getExtraRow(Context context) {
        return MySharedPreferences.getBooleanValue(context, extraRow, false);
    }

    public static void putExtraRow(Context context, boolean values) {
        MySharedPreferences.putBooleanValue(context, extraRow, values);
    }

    //    --------------------------------
    private static final String pupup = "pupup";

    public static boolean getPopup(Context context) {
        return MySharedPreferences.getBooleanValue(context, pupup, true);
    }

    public static void putPopup(Context context, boolean values) {
        MySharedPreferences.putBooleanValue(context, pupup, values);
    }


    //    --------------------------------
    private static final String Width = "Width";

    public static int getWidth(Context context) {
        return MySharedPreferences.getIntValue(context, Width, DeviceUtils.convertDpToPixel(context, 40));
    }

    public static void putWidth(Context context, int values) {
        MySharedPreferences.putIntValue(context, Width, values);
    }

    //    --------------------------------
    private static final String Height = "Height";

    public static int getHeight(Context context) {
        return MySharedPreferences.getIntValue(context, Height, DeviceUtils.convertDpToPixel(context, 40));
    }

    public static void putHeight(Context context, int values) {
        MySharedPreferences.putIntValue(context, Height, values);
    }
}
