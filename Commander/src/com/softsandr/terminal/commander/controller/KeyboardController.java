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
package com.softsandr.terminal.commander.controller;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import static com.softsandr.utils.string.StringUtil.EMPTY;
import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * The implementation logic for soft keyboard listener {@link android.widget.TextView.OnEditorActionListener}
 * used for console input.
 */
public class KeyboardController implements TextView.OnEditorActionListener {
    private ProcessController mProcessController;
    private UiController mUiController;
    private boolean inFirst = true;

    public KeyboardController(UiController uiController, ProcessController processController) {
        mProcessController = processController;
        mUiController = uiController;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                mUiController.getTerminalOutView().setVisibility(View.VISIBLE);
                String fullCommandText = mUiController.getTerminalInView().getText().toString();
                StringBuilder resultText = new StringBuilder(mUiController.getTerminalOutView().getText().toString());
                if (inFirst) {
                    if (!fullCommandText.isEmpty()) {
                        resultText.append(mUiController.getPrompt().getPromptText());
                        resultText.append(fullCommandText);
                        mProcessController.getProcess().execCommand(fullCommandText);
                    } else {
                        resultText.append(mUiController.getPrompt().getPromptText());
                    }
                    inFirst = false;
                } else {
                    if (!fullCommandText.isEmpty()) {
                        if (!TextUtils.isEmpty(mUiController.getTerminalOutView().getText())) {
                            resultText.append(LINE_SEPARATOR);
                        }
                        resultText.append(mUiController.getPrompt().getPromptText());
                        resultText.append(fullCommandText);
                        mProcessController.getProcess().execCommand(fullCommandText);
                    } else {
                        if (!TextUtils.isEmpty(mUiController.getTerminalOutView().getText())) {
                            resultText.append(LINE_SEPARATOR);
                        }
                        resultText.append(mUiController.getPrompt().getPromptText());
                    }
                }
                mUiController.getTerminalOutView().setText(resultText);
                mUiController.getTerminalInView().setText(EMPTY);
            }
            handled = true;
        }
        return handled;
    }
}
