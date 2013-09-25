package com.drk.terminal.utils;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryUtils {

    public static String getCurrentDirectory(Context context) {
        return context.getApplicationInfo().sourceDir;
    }
}
