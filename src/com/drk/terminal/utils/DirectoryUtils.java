package com.drk.terminal.utils;

import android.content.Context;
import com.drk.terminal.command.filtered.CdCommand;

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
        boolean isExist = false;
        File dir = new File(curDirName + StringUtils.PATH_SEPARATOR + subDirName);
        if (dir.exists() && dir.isDirectory()) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * Check if present directory in path
     *
     * @param path The target directory
     * @return true if file exist and is directory file
     */
    public static boolean isDirectoryExist(String path) {
        boolean isExist = false;
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * Check if have write permissions to file
     *
     * @param curDirName The current directory
     * @param subDirName The subdirectory in curDirName directory
     * @return true if can write
     */
    public static boolean checkWritePermissions(String curDirName, String subDirName) {
        boolean canWrite = false;
        File dir = new File(curDirName + StringUtils.PATH_SEPARATOR + subDirName);
        if(dir.canWrite()) {
            canWrite = true;
        }
        return canWrite;
    }

    /**
     * Check if have write permissions to file
     *
     * @param curDirName The current directory
     * @param subDirName The subdirectory in curDirName directory
     * @return true if can use cd to subdirectory
     */
    public static boolean canChangeDirectory(String curDirName, String subDirName) {
        boolean canChange = false;
        File dir = new File(curDirName + StringUtils.PATH_SEPARATOR + subDirName);
        if(dir.canRead()) {
            canChange = true;
        }
        return canChange;
    }

    public static String buildDirectoryPath(String curDirName, String subDirName) {
        StringBuilder subDirectoryPath = new StringBuilder(StringUtils.EMPTY);
        if (curDirName.equals(StringUtils.PATH_SEPARATOR)) {
            subDirectoryPath.append(StringUtils.PATH_SEPARATOR);
            subDirectoryPath.append(subDirName);
        } else {
            subDirectoryPath.append(curDirName);
            subDirectoryPath.append(StringUtils.PATH_SEPARATOR);
            subDirectoryPath.append(subDirName);
        }
        return subDirectoryPath.toString();
    }
}
