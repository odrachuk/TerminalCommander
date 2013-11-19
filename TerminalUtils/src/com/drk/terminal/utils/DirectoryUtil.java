package com.drk.terminal.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryUtil {

    /**
     * Check if present filesystem in path
     *
     * @param curDirName The current filesystem
     * @param subDirName The subdirectory in curDirName filesystem
     * @return true if file exist and is filesystem file
     */
    public static boolean isDirectoryExist(String curDirName, String subDirName) {
        boolean isExist = false;
        File dir = new File(curDirName + StringUtil.PATH_SEPARATOR + subDirName);
        if (dir.exists() && dir.isDirectory()) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * Check if present filesystem in path
     *
     * @param path The target filesystem
     * @return true if file exist and is filesystem file
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
     * @param curDirName The current filesystem
     * @param subDirName The subdirectory in curDirName filesystem
     * @return true if can write
     */
    public static boolean checkWritePermissions(String curDirName, String subDirName) {
        boolean canWrite = false;
        File dir = new File(curDirName + StringUtil.PATH_SEPARATOR + subDirName);
        if (dir.canWrite()) {
            canWrite = true;
        }
        return canWrite;
    }

    /**
     * Check if have write permissions to file
     *
     * @param curDirName The current filesystem
     * @param subDirName The subdirectory in curDirName filesystem
     * @return true if can use cd to subdirectory
     */
    public static boolean canChangeDirectory(String curDirName, String subDirName) {
        boolean canChange = false;
        File dir = new File(curDirName + StringUtil.PATH_SEPARATOR + subDirName);
        if (dir.canRead()) {
            canChange = true;
        }
        return canChange;
    }

    public static String buildDirectoryPath(String curDirName, String subDirName) {
        StringBuilder subDirectoryPath = new StringBuilder(StringUtil.EMPTY);
        if (curDirName.equals(StringUtil.PATH_SEPARATOR)) {
            subDirectoryPath.append(StringUtil.PATH_SEPARATOR);
            subDirectoryPath.append(subDirName);
        } else {
            subDirectoryPath.append(curDirName);
            subDirectoryPath.append(StringUtil.PATH_SEPARATOR);
            subDirectoryPath.append(subDirName);
        }
        return subDirectoryPath.toString();
    }

    public static String trimLastSlash(String targetDirectory) {
        StringBuilder resultPath = new StringBuilder();
        int lastSlashIndex = targetDirectory.lastIndexOf(StringUtil.PATH_SEPARATOR);
        if (lastSlashIndex == targetDirectory.length()) {
            resultPath.append(targetDirectory.substring(0, lastSlashIndex));
        } else {
            resultPath.append(targetDirectory);
        }
        return resultPath.toString();
    }

    public static boolean isSymlink(String parentPath, File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        String presentPath = parentPath.equals(StringUtil.PATH_SEPARATOR)?
                StringUtil.PATH_SEPARATOR + file.getName() :
                parentPath + StringUtil.PATH_SEPARATOR + file.getName();
        String realPath = file.getAbsolutePath();
        return !presentPath.equals(realPath);
    }
}
