package com.vinsofts.keyborad.utils;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isGoodField(String input) {
        if (input == null || input.isEmpty() || input.trim().length() < 6) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        return false;
    }

    public static String getAction(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    public static boolean isValidateUsername(String userName) {
        String USERNAME_PATTERN = "^[A-Za-z0-9_-]{1,33}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    /**
     * @param input
     * @return chuỗi ko có dấu, chữ tiếng việt
     */
    public static String removeAccent(String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toUpperCase().replaceAll("Đ", "D");
    }

    public static String formatPrice(long price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    public static void setHtml(final TextView textView, String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));

        } else {
            textView.setText(Html.fromHtml(html));

        }
    }

    public static String formatFile(long size) {
        if (size <= 0) {
            return "0";
        }
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String formatKm(double km) {
        if (km < 1) {
            return ((int) (km * 1000)) + " m";
        }
        NumberFormat formatter = new DecimalFormat("#0.0");
        return formatter.format(km) + " km";
    }
    public static String formatMet(double met) {
        if (met >= 1000) {
            NumberFormat formatter = new DecimalFormat("#0.0");
            return (formatter.format((double) (met / 1000))) + " km";
        }
        NumberFormat formatter = new DecimalFormat("#0");
        return formatter.format(met) + " m";
    }

}
