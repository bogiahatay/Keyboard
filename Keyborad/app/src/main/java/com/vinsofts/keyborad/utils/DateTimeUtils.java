package com.vinsofts.keyborad.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateTimeUtils {
//    http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html


    private static final Locale LOCALE = Locale.ENGLISH;

    public static String formatLong(long timeStamp, String outFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(outFormat, LOCALE);
        try {
            return dateFormat.format(new Date(timeStamp));
        } catch (Exception e) {
            e.printStackTrace();
            return dateFormat.format(new Date());
        }
    }

    public static String formatString(String strDate, String inFormat, String outFormat) {
        SimpleDateFormat from = new SimpleDateFormat(inFormat, LOCALE);
        SimpleDateFormat to = new SimpleDateFormat(outFormat, LOCALE);
        try {
            return to.format(from.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
            return to.format(new Date());
        }
    }


    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long MONTH_MILLIS = 30 * DAY_MILLIS;
    private static final long YEAR_MILLIS = 365 * DAY_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = Calendar.getInstance().getTimeInMillis();
        if (time > now || time <= 0) {
            MLog.e("getTimeAgo ERROR");
            return "";
        }
        long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < HOUR_MILLIS) {
            return diff / MINUTE_MILLIS + " min ago";
        } else if (diff < DAY_MILLIS) {
            return diff / HOUR_MILLIS + " hour ago";
        } else if (diff < MONTH_MILLIS) {
            return diff / DAY_MILLIS + " day ago";
        } else if (diff < YEAR_MILLIS) {
            return diff / MONTH_MILLIS + " month ago";
        } else if (diff > YEAR_MILLIS) {
            return diff / YEAR_MILLIS + " year ago";
        }
        return "error";
    }

    private static final String[] DAY = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public static String getNameDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return DAY[calendar.get(Calendar.DAY_OF_WEEK) - 1];

//        String[] day = activity.getResources().getStringArray(R.array.days);
//        <string-array name="days">
//        <item>Chủ nhật</item>
//        <item>Thứ hai</item>
//        <item>Thứ ba</item>
//        <item>Thứ tư</item>
//        <item>Thứ năm</item>
//        <item>Thứ sáu</item>
//        <item>Thứ bảy</item>
//        </string-array>
//        MLog.e(day);
//        return day[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getTodayYesterday(long timeIn) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeIn);
        time.get(Calendar.DAY_OF_YEAR);

        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) == time.get(Calendar.YEAR)) {
            int diffDay = now.get(Calendar.DAY_OF_YEAR) - time.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 0) {
                return "Today";
            }
            if (diffDay == 1) {
                return "Yesterday";
            }
            if (diffDay == -1) {
                return "Tomorrow";
            }
        }
        MLog.e("getTodayYesterday ERROR ");
        return getNameDay(timeIn);
    }

    public static String getTime(long time) {
         long m = time / 60;
        long s = time % 60;
        return (m < 10 ? ("0" + m) : m) + ":" + (s < 10 ? ("0" + s) : s) ;
    }

}
