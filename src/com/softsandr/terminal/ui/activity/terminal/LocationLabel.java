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
package com.softsandr.terminal.ui.activity.terminal;

import android.widget.TextView;
import com.drk.terminal.util.utils.StringUtil;

/**
 * The class as String location of user
 */
public class LocationLabel {
    private String fullPath = StringUtil.PATH_SEPARATOR;
    private final TextView pathLabel;

    public LocationLabel(TextView ownLabel) {
        this.pathLabel = ownLabel;
    }

    public void setPath(String path) {
        fullPath = path;
        if (path != null) {
            pathLabel.setText(path);
        }
    }

    public String getPath() {
        return fullPath;
    }
}
