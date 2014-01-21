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

import android.graphics.Color;
import android.util.JsonReader;
import android.webkit.MimeTypeMap;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.preference.custom.*;
import com.softsandr.terminal.activity.terminal.TerminalActivity;
import com.softsandr.utils.file.FileExtensions;

import java.io.Reader;
import java.io.StringReader;

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
                String colorString = terminalActivity.getPreference().getPreferences().
                        getString(terminalActivity.getContextResources().
                                getString(R.string.pref_doc_item_color_key),
                                DocumentsColorPickerPreference.DEFAULT_VALUE);
                Reader reader = new StringReader(colorString);
                JsonReader jsonReader = new JsonReader(reader);
                int[] defArray = ArchiveColorPickerPreference.readColorJson(jsonReader);
                return Color.argb(defArray[3], defArray[0], defArray[1], defArray[2]);
            } else if (FileExtensions.ARCHIVE_OR_COMPRESSED.categoryExtensions().keySet().contains(ext)) {
                String colorString = terminalActivity.getPreference().getPreferences().
                        getString(terminalActivity.getContextResources().
                                getString(R.string.pref_archive_item_color_key),
                                ArchiveColorPickerPreference.DEFAULT_VALUE);
                Reader reader = new StringReader(colorString);
                JsonReader jsonReader = new JsonReader(reader);
                int[] defArray = ArchiveColorPickerPreference.readColorJson(jsonReader);
                return Color.argb(defArray[3], defArray[0], defArray[1], defArray[2]);
            } else if (FileExtensions.SHELL_PROGRAMS.categoryExtensions().keySet().contains(ext)) {
                String colorString = terminalActivity.getPreference().getPreferences().
                        getString(terminalActivity.getContextResources().
                                getString(R.string.pref_shell_item_color_key),
                                ShellColorPickerPreference.DEFAULT_VALUE);
                Reader reader = new StringReader(colorString);
                JsonReader jsonReader = new JsonReader(reader);
                int[] defArray = ArchiveColorPickerPreference.readColorJson(jsonReader);
                return Color.argb(defArray[3], defArray[0], defArray[1], defArray[2]);
            } else if (FileExtensions.IMAGES.categoryExtensions().keySet().contains(ext)) {
                String colorString = terminalActivity.getPreference().getPreferences().
                        getString(terminalActivity.getContextResources().
                                getString(R.string.pref_images_item_color_key),
                                ImagesColorPickerPreference.DEFAULT_VALUE);
                Reader reader = new StringReader(colorString);
                JsonReader jsonReader = new JsonReader(reader);
                int[] defArray = ArchiveColorPickerPreference.readColorJson(jsonReader);
                return Color.argb(defArray[3], defArray[0], defArray[1], defArray[2]);
            } else if (FileExtensions.VIDEO.categoryExtensions().keySet().contains(ext)
                    || FileExtensions.MUSIC.categoryExtensions().keySet().contains(ext)) {
                String colorString = terminalActivity.getPreference().getPreferences().
                        getString(terminalActivity.getContextResources().
                                getString(R.string.pref_media_item_color_key),
                                MediaColorPickerPreference.DEFAULT_VALUE);
                Reader reader = new StringReader(colorString);
                JsonReader jsonReader = new JsonReader(reader);
                int[] defArray = ArchiveColorPickerPreference.readColorJson(jsonReader);
                return Color.argb(defArray[3], defArray[0], defArray[1], defArray[2]);
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
