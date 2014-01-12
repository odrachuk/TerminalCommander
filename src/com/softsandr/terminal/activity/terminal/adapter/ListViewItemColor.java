/*******************************************************************************
 * Created by o.drachuk on 12/01/2014. 
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
package com.softsandr.terminal.activity.terminal.adapter;

import android.content.res.Resources;
import android.webkit.MimeTypeMap;
import com.softsandr.terminal.R;
import com.softsandr.utils.file.FileExtensions;

/**
 * This class used for...
 */
public enum ListViewItemColor {
    // for all selected objects
    SELECTED(R.color.COLOR_FECE0A),
    // for all regular directories that can open
    DIRECTORY(android.R.color.white),
    // for all closed for us directories
    ROOT_DIRECTORY(R.color.COLOR_TRANSLUCENT_WHITE),
    // for all files
    FILE(R.color.COLOR_B2B2B2) {
        @Override
        public int getColor(Resources resources, String itemText) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(itemText);
            if (FileExtensions.WEB.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.OFFICE_DOCUMENT.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.COMPUTER_PROGRAMS.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.BOOK_DOCUNET.categoryExtensions().keySet().contains(ext)) {
                return resources.getColor(R.color.COLOR_b26818);
            } else if (FileExtensions.ARCHIVE_OR_COMPRESSED.categoryExtensions().keySet().contains(ext)) {
                return resources.getColor(R.color.COLOR_ff50ec);
            } else if (FileExtensions.SHELL_PROGRAMS.categoryExtensions().keySet().contains(ext)) {
                return resources.getColor(R.color.COLOR_189fb2);
            } else if (FileExtensions.IMAGES.categoryExtensions().keySet().contains(ext)) {
                return resources.getColor(R.color.COLOR_46f1ff);
            } else if (FileExtensions.VIDEO.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.MUSIC.categoryExtensions().keySet().contains(ext)) {
                return resources.getColor(R.color.COLOR_18a818);
            } else {
                return resources.getColor(this.colorId);
            }
        }
    };

    final int colorId;

    ListViewItemColor(int colorId) {
        this.colorId = colorId;
    }

    public int getColor(Resources resources, String itemText) {
        return resources.getColor(this.colorId);
    }
}
