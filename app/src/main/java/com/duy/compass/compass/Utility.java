package com.duy.compass.compass;

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

}
