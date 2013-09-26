package com.drk.terminal.utils;

import android.content.Context;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryUtils {

    /**
     * Check if present directory in path
     *
     * @param curDirName The current directory
     * @param subDirName The subdirectory in curDirName directory
     * @return true if file exist and is directory file
     */
    public static boolean isDirectoryExist(String curDirName, String subDirName) {
        File dir = new File(curDirName + StringUtils.PATH_SEPARATOR + subDirName);
        if (dir.exists() && dir.isDirectory()) {
            return true;
        }
        return false;
    }
}
