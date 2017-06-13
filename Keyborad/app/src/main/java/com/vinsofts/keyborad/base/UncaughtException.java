package com.vinsofts.keyborad.base;

import android.app.Activity;


public class UncaughtException implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public UncaughtException(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(activity, SplashActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                activity.startActivity(intent);
                activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).start();
    }

}