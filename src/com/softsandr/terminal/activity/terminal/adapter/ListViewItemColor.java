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

import android.webkit.MimeTypeMap;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivity;
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
        public int getColor(TerminalActivity terminalActivity, String itemText) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(itemText);
            if (FileExtensions.WEB.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.OFFICE_DOCUMENT.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.COMPUTER_PROGRAMS.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.BOOK_DOCUNET.categoryExtensions().keySet().contains(ext)) {
                // Documents
                return terminalActivity.getSettingsConfiguration().getDocItemColor();
            } else if (FileExtensions.ARCHIVE_OR_COMPRESSED.categoryExtensions().keySet().contains(ext)) {
                // Archives
                return terminalActivity.getSettingsConfiguration().getArchiveItemColor();
            } else if (FileExtensions.SHELL_PROGRAMS.categoryExtensions().keySet().contains(ext)) {
                // Shell-scripts
                return terminalActivity.getSettingsConfiguration().getShellItemColor();
            } else if (FileExtensions.IMAGES.categoryExtensions().keySet().contains(ext)) {
                // Images
                return terminalActivity.getSettingsConfiguration().getImageItemColor();
            } else if (FileExtensions.VIDEO.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.MUSIC.categoryExtensions().keySet().contains(ext)) {
                // Media
                return terminalActivity.getSettingsConfiguration().getMediaItemColor();
            } else {
                return terminalActivity.getContextResources().getColor(this.colorId);
            }
        }
    };

    final int colorId;

    ListViewItemColor(int colorId) {
        this.colorId = colorId;
    }

    public int getColor(TerminalActivity terminalActivity, String itemText) {
        return terminalActivity.getContextResources().getColor(this.colorId);
    }
}
