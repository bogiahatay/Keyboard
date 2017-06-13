package com.vinsofts.keyborad.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Ha on 5/4/2017.
 */

public class MapUtils {
    public static String getAddress(Activity activity, double latitude, double longitude) {
        try {
            Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());
            StringBuilder builder = new StringBuilder();
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }
            String roadName = address.get(0).getAddressLine(0);
            String xaName = address.get(0).getAddressLine(1);
            String huyenName = address.get(0).getAddressLine(2);
            String cityName = address.get(0).getAddressLine(3);
            String countryName = address.get(0).getAddressLine(4);

            MLog.e(builder.toString());

            return cityName;
//            return builder.toString().replace("Unnamed Road", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Location getLocation(Activity activity) {
        if (!isGPSEnabled(activity)) {
            return null;
        }
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        Location location = null;
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.ACCURACY_MEDIUM);
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        String bestProvider = locationManager.getBestProvider(criteria, true);

        location = locationManager.getLastKnownLocation(bestProvider);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;

    }

    public static Bitmap getBitmapView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static boolean isGPSEnabled(Activity mActivity) {
        return ((LocationManager) mActivity.getSystemService(Activity.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
