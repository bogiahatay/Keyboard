package com.vinsofts.keyborad.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;

import java.io.File;


public class IntentUtils {

    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void message(Context context, String phone, String content) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", content);
        context.startActivity(it);
    }


    public static void share(Context context, String title, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(shareIntent, "habn"));
    }

    public static void view(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }


    public static void settingsLocation(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }

    public static void settings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    public static void viewVideo(Activity mActivity, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        intent.setDataAndType(Uri.fromFile(new File(path)), "video/*");
        mActivity.startActivity(intent);
    }

    public static void viewImage(Activity mActivity, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        mActivity.startActivity(intent);
    }

    public static void shareVideo(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        context.startActivity(Intent.createChooser(intent, "Share video"));
    }

    public static void shareImage(Context context, String path) {
        MLog.e(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        context.startActivity(Intent.createChooser(intent, "Share image"));
    }

    public static void showChooseKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showInputMethodPicker();
    }

    public static void showEnableKeyboard(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), requestCode);
    }
}
