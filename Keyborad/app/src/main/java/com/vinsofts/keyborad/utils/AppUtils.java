package com.vinsofts.keyborad.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.security.MessageDigest;


public class AppUtils {

    public static void getFacebookKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                MLog.e("Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                MLog.e("Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                MLog.e("Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                MLog.e("Key hash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showKeyboard(Context ctx, EditText editText) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    public static boolean isMyServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static boolean isConnectInternet(Context context) {
        try {
            NetworkInfo info = ((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
            if (info.isRoaming()) {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String getPacketName() {
        return this.getClass().getPackage().getName();
    }

    public static void saveJson(Activity mActivity, final String json, final String name) {
        if (!PermissionUtils.checkStorage(mActivity, 1)) {
            Toast.makeText(mActivity, "Permission Storage", Toast.LENGTH_SHORT).show();
            return;
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    File folder = new File(Environment.getExternalStorageDirectory() + "/json/");
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    Writer output = null;
                    File file = new File(Environment.getExternalStorageDirectory() + "/json/" + name + ".json");
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(json);
                    output.close();
                    MLog.e("Composition saved");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void addShortcut(Activity activity) {
//        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//        Intent shortcutIntent = new Intent(activity, MusicActivity.class);
//        shortcutIntent.setAction(Intent.ACTION_MAIN);
//        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
//        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(activity, R.mipmap.ic_launcher));
//        activity.sendBroadcast(intent);
    }


    public static void setWebView(WebView webView, String data) {
        try {
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webView.loadData("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + data, "text/html; charset=utf-8", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//
//    public static void shareImageFacebook(Activity mActivity, Bitmap image) {
//        SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
//        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
//        ShareDialog dialog = new ShareDialog(mActivity);
//        if (ShareDialog.canShow(SharePhotoContent.class)) {
//            dialog.show(content);
//        } else {
//            Log.e("Activity", "you cannot share photos :(");
//        }
//    }
//
//    public static void shareFacebook(Activity mActivity) {
//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            Uri myUri = Uri.parse("http://genknews.genkcdn.vn/zoom/310_200/2017/lol-1494511534535.png");
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentTitle(mActivity.getString(R.string.app_name))
//                    .setContentDescription("HAY")
//                    .setContentUrl(myUri)
//                    .build();
//
//            new ShareDialog(mActivity).show(linkContent);
//        }
//    }


    public static String getFolderSaveRecord() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/MyRecordScreen/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File folderRecord = new File(Environment.getExternalStorageDirectory() + "/MyRecordScreen/Video/");
        if (!folderRecord.exists()) {
            folderRecord.mkdir();
        }

        return Environment.getExternalStorageDirectory() + "/MyRecordScreen/Video/";
    }

    public static String getFolderSaveCapture() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/MyRecordScreen/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File folderRecord = new File(Environment.getExternalStorageDirectory() + "/MyRecordScreen/Image/");
        if (!folderRecord.exists()) {
            folderRecord.mkdir();
        }

        return Environment.getExternalStorageDirectory() + "/MyRecordScreen/Image/";
    }


//    public static List<Video> getAllVideoRecord(Context context) {
//        File folder = new File(AppUtils.getFolderSaveRecord());
//
//        File[] files = folder.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isFile() && file.getPath().contains(".mp4");
//            }
//        });
//        List<Video> list = new ArrayList<>();
//        for (int i = 0; i < files.length; i++) {
//            Video video = new Video();
//            video.setName(files[i].getName());
//            video.setPath(files[i].getPath());
//            list.add(video);
//        }
//
//
//
//
//        return list;
//    }


    private static long getDuration(File file) throws Exception {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mediaMetadataRetriever.release();
        return Long.parseLong(durationStr);
    }

    public static void setRingtone(Activity activity, File file) throws Exception {

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.ARTIST, "Vinsofts  Mp3Cutter");
        values.put(MediaStore.Audio.Media.DURATION, getDuration(file));
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);


        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        int temp = activity.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + " = ?", new String[]{file.getAbsolutePath()});
        Uri newUri = activity.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_RINGTONE, newUri);
    }

    public static void setNotifications(Activity activity, File file) throws Exception {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.ARTIST, "Vinsofts  Mp3Cutter");
        values.put(MediaStore.Audio.Media.DURATION, getDuration(file));
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        int temp = activity.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + " = ?", new String[]{file.getAbsolutePath()});
        Uri newUri = activity.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_NOTIFICATION, newUri);
    }


    public static void setAlarm(Activity activity, File file) throws Exception {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, file.getName());
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.ARTIST, "Vinsofts  Mp3Cutter");
        values.put(MediaStore.Audio.Media.DURATION, getDuration(file));
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);


        Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        int temp = activity.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + " = ?", new String[]{file.getAbsolutePath()});
        Uri newUri = activity.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM, newUri);
    }

}
