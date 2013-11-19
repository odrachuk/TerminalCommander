package com.drk.terminal.utils;

import android.content.res.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 11/19/13
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
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
