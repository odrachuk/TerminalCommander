package com.drk.terminal.util.utils;

import android.content.res.Resources;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class OrientationUtil {

    public static boolean isLandscapeOrientation(Resources resources) {
        if (resources.getDisplayMetrics().widthPixels > resources.getDisplayMetrics().heightPixels) {
            return true;
        } else {
            return false;
        }
    }
}
