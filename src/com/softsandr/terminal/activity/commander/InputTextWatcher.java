/*******************************************************************************
 * Created by o.drachuk on 13/01/2014. 
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
package com.softsandr.terminal.activity.commander;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.softsandr.utils.string.StringUtil;

/**
 * This class used for control input text for displaying tab button if this possible
 */
public final class InputTextWatcher implements TextWatcher {
    private final View tabulateButton;

    public InputTextWatcher(View tabulateButton) {
        this.tabulateButton = tabulateButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // ignored
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // ignored
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (text.isEmpty()) {
            tabulateButton.setVisibility(View.GONE);
        } else if (!text.startsWith(StringUtil.WHITESPACE)
                && !text.endsWith(StringUtil.WHITESPACE)
                && text.contains(StringUtil.WHITESPACE)) {
            tabulateButton.setVisibility(View.VISIBLE);
        } else {
            tabulateButton.setVisibility(View.GONE);
        }
    }
}
