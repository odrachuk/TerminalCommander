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
package com.drk.terminal.util.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * The utility for opening files on Android device
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
