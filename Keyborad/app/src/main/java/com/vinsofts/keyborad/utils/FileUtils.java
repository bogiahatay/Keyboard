package com.vinsofts.keyborad.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Ha on 6/2/2017.
 */

public class FileUtils {
    public static boolean renameFile(Context context,File from, String newName) {
        if (from.exists()) {
            File to = new File(from.getAbsoluteFile().getParent(), newName);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(to)));
            return from.renameTo(to);
        }
        return false;
    }

    public static boolean deleteFile(Context context, File file) {
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                return true;
            }
        }
        return false;
    }
}
