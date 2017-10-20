package com.duy.compass.compass;

import java.util.Locale;

/**
 * Created by Duy on 10/16/2017.
 */

public class Utility {
    public static String getDirectionText(float degree) {
        final float step = 22.5f;
        if (degree >= 0 && degree < step || degree > 360 - step) {
            return "N";
        }
        if (degree >= step && degree < step * 3) {
            return "NE";
        }
        if (degree >= step * 3 && degree < step * 5) {
            return "E";
        }
        if (degree >= step * 5 && degree < step * 7) {
            return "SE";
        }
        if (degree >= step * 7 && degree < step * 9) {
            return "S";
        }
        if (degree >= step * 9 && degree < step * 11) {
            return "SW";
        }
        if (degree >= step * 11 && degree < step * 13) {
            return "W";
        }
        if (degree >= step * 13 && degree < step * 15) {
            return "NW";
        }
        return "";
    }

    public static String formatDms(float decimal) {
        long d = (long) decimal;
        long m = (long) ((decimal - d) * 60);
        float s = (decimal - d - m / 60) * 3600;
        return String.format(Locale.US, "%d°%d'%.2f\"", d, m, s);
    }
}
