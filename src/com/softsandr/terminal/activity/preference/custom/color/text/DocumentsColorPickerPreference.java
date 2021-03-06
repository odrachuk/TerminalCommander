/*******************************************************************************
 * Created by o.drachuk on 20/01/2014. 
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
package com.softsandr.terminal.activity.preference.custom.color.text;

import android.content.Context;
import android.util.AttributeSet;

/**
 * This class customize {@link android.preference.DialogPreference} and display
 * {@link android.widget.SeekBar} for setup preference int value color of textual items
 */
public class DocumentsColorPickerPreference extends ColorPickerPreference {
    public static final String DEFAULT_VALUE = "{\"red\":208,\"green\":152,\"blue\":88}";

    public DocumentsColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setDefaultJson() {
        defaultValue = DEFAULT_VALUE;
    }
}
