package com.charlie.weatherapp.util;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

public class Utils {

    public static String uppercaseFirtLetter(String value) {
        if (TextUtils.isEmpty(value)) {
            return value;
        } else {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
    }

    public static double fromKevinToCelsius(double kelvin) {
        return kelvin - 273.15F;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

}
