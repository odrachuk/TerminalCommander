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
package com.softsandr.commander;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.softsandr.utils.string.StringUtil;

import static com.softsandr.utils.string.StringUtil.EMPTY;
import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * The implementation logic for soft keyboard listener {@link android.widget.TextView.OnEditorActionListener}
 * used for console input.
 */
public class KeyboardController implements TextView.OnEditorActionListener {
    private boolean inFirst = true;
    private Commander commander;

    public KeyboardController(Commander commander) {
        this.commander = commander;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                commander.getOutTextView().setVisibility(View.VISIBLE);
                if (v.getText() != null) {
                    String fullCommandText = v.getText().toString();
                    StringBuilder resultText = new StringBuilder(
                            commander.getOutTextView().getText() != null?
                                    commander.getOutTextView().getText().toString() : StringUtil.EMPTY);
                    if (inFirst) {
                        if (!fullCommandText.isEmpty()) {
                            resultText.append(commander.getPrompt().getCompoundString());
                            resultText.append(fullCommandText);
                            commander.getProcess().execCommand(fullCommandText);
                        } else {
                            resultText.append(commander.getPrompt().getCompoundString());
                        }
                        inFirst = false;
                    } else {
                        if (!fullCommandText.isEmpty()) {
                            if (!TextUtils.isEmpty(commander.getOutTextView().getText())) {
                                resultText.append(LINE_SEPARATOR);
                            }
                            resultText.append(commander.getPrompt().getCompoundString());
                            resultText.append(fullCommandText);
                            commander.getProcess().execCommand(fullCommandText);
                        } else {
                            if (!TextUtils.isEmpty(commander.getOutTextView().getText())) {
                                resultText.append(LINE_SEPARATOR);
                            }
                            resultText.append(commander.getPrompt().getCompoundString());
                        }
                    }
                    commander.getOutTextView().setText(resultText);
                    commander.getInputEditText().setText(EMPTY);
                }
            }
            handled = true;
        }
        return handled;
    }
}
