package com.drk.terminal.util.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Date: 12/8/13
 *
 * @author Drachuk O.V.
 */
public final class FileOpeningUtil {

    private FileOpeningUtil() {
    }

    public static boolean checkOpening(String fileName, PackageManager packageManager) {
        Intent searchIntent = new Intent(Intent.ACTION_VIEW);
        searchIntent.setType("application/" + parseFileExtension(fileName));
        List<ResolveInfo> possibleAppList = packageManager.
                queryIntentActivities(searchIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return !possibleAppList.isEmpty();
    }

    public static String parseFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(dotIndex + 1, fileName.length());
    }
}
