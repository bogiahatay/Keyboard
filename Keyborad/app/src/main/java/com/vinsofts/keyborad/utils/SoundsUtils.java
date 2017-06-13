package com.vinsofts.keyborad.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Ha on 6/8/2017.
 */

public class SoundsUtils {

    public static void play(Context context, int id) {
        MediaPlayer mp = MediaPlayer.create(context, id);
        mp.start();
        mp.setOnCompletionListener(mp1 -> mp1.release());
    }
}
