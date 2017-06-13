package com.vinsofts.keyborad.utils;

import android.util.Log;

import com.google.gson.Gson;


/**
 * Created by Ha on 8/5/2016.
 */
public class MLog {
    private static String TAG = "RECORD SCREEN";

    public static void e(String s) {
        if (s == null) {
            Log.e(TAG, "null");
        } else {
            Log.e(TAG, s);
        }
    }

    public static void e(Object obj) {
        if (obj == null) {
            Log.e(TAG, "null obj");
        } else {
            Log.e(TAG, new Gson().toJson(obj));
        }
    }

    public static void e(String TAG, String s) {
//        Log.e(TAG, s);
    }

    public static void se(String msg) {
        if (msg.length() > 1024) {
            Log.e(TAG, msg.substring(0, 1023));
            se(msg.substring(1023));
        } else {
            Log.e(TAG, msg);
        }
    }
}
