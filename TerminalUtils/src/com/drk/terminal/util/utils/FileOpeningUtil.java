package com.drk.terminal.util.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Date: 12/8/13
 *
 * @author Drachuk O.V.
 */
public final class FileOpeningUtil {

    private FileOpeningUtil() {
    }

    public static void openFile(Activity activity, String filePath) {
        File file = new File(filePath);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            type = "*/*";
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);

        activity.startActivity(intent);
    }
}
