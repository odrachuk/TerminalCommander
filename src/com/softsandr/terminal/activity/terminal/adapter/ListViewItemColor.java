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
            MimeTypeMap map = MimeTypeMap.getSingleton();
            String ext = MimeTypeMap.getFileExtensionFromUrl(itemText);
            // todo different colors for different file extensions
            return resources.getColor(this.colorId);
        }
    };

    private final int colorId;

    ListViewItemColor(int colorId) {
        this.colorId = colorId;
    }

    public int getColor(Resources resources, String itemText) {
        return resources.getColor(this.colorId);
    }
}
