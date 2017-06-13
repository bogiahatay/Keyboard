package com.vinsofts.keyborad.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.vinsofts.keyborad.R;


/**
 * Created by Ha on 3/20/2017.
 */

public class PermissionUtils {


    public static void showDialog(Activity activity, int title, int mes, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(activity)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(mes)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Grant", listener).create().show();
    }


    public static boolean checkStorage(final Activity activity, final Integer REQUEST_ID) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_ID != null) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_ID);
            }
            return false;
        }
        return true;
    }

    public static boolean checkCamera(Activity activity, Integer REQUEST_ID) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_ID != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_ID);
            }
            return false;
        }
        return true;
    }

    public static boolean checkReadContacts(Activity activity, Integer REQUEST_ID) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_ID != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_ID);
            }
            return false;
        }
        return true;
    }

    public static boolean checkLocation(Activity activity, Integer REQUEST_ID) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED && permission1 != PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_ID != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ID);
            }
            return false;
        }
        return true;
    }

    public static boolean checkReadPhoneSate(Activity activity, Integer REQUEST_ID) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_ID != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_ID);
            }
            return false;
        }
        return true;
    }

    public static boolean checkWriteSetting(Activity activity, Integer REQUEST_ID) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_SETTINGS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_ID != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_SETTINGS}, REQUEST_ID);
            }
            return false;
        }
        return true;
    }

    public static String[] PERMISSIONS_APP = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static void requestAllPermission(Activity activity, int REQUEST_ID) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS_APP, REQUEST_ID);
    }
}
