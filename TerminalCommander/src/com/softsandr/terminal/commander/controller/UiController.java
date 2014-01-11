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

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import com.softsandr.terminal.commander.prompt.TerminalPrompt;

/**
 * The container of all ui elements from commander interface
 */
public class UiController {
    private Activity mProcessActivity;
    private TerminalPrompt mPrompt;
    private EditText mTerminalInView;
    private TextView mTerminalOutView;
    private TextView mTerminalPromptView;

    private UiController() {}

    public UiController(Activity terminalActivity,
                        EditText terminalInView,
                        TextView terminalOutView,
                        TextView terminalPromptView) {
        mProcessActivity = terminalActivity;
        mPrompt = new TerminalPrompt(this);
        mTerminalInView = terminalInView;
        mTerminalOutView = terminalOutView;
        mTerminalPromptView = terminalPromptView;
    }

    public Activity getActivity() {
        return mProcessActivity;
    }

    public TerminalPrompt getPrompt() {
        return mPrompt;
    }

    public EditText getTerminalInView() {
        return mTerminalInView;
    }

    public TextView getTerminalPromptView() {
        return mTerminalPromptView;
    }

    public TextView getTerminalOutView() {
        return mTerminalOutView;
    }
}
