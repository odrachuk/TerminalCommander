/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.utils.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

/**
 * The utility for opening files on Android device
 */
public final class FileOpeningUtil {

    private FileOpeningUtil() {
    }

    public static void openFile(Activity activity, String filePath) {
        File file = new File(filePath);
        // determine file extension and type
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);
        // set default extension and type if necessary
        if (type == null) {
            type = "*/*";
        }
        // prepare intent
        Intent openIntent = new Intent();
        openIntent.setAction(Intent.ACTION_VIEW);
        openIntent.setDataAndType(Uri.fromFile(file), type);
        // Verify that the intent will resolve to an activity
        if (openIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(openIntent);
        } else {
            Toast.makeText(activity, "Not found any installed application for opening file", Toast.LENGTH_LONG).show();
        }
    }
}
