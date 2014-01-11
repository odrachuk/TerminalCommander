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
package com.softsandr.terminal.commander;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.softsandr.terminal.commander.commands.filtered.FilteredCommands;

import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * The {@link android.os.Handler} extends for handling response from console input after execution an command
 */
public class CommandResponseHandler extends Handler {
    private static final String LOG_TAG = CommandResponseHandler.class.getSimpleName();
    public static final String COMMAND_EXECUTION_RESPONSE_KEY = LOG_TAG + ".COMMAND_EXECUTION_RESPONSE";
    public static final String COMMAND_EXECUTION_STRING_KEY = LOG_TAG + ".COMMAND_EXECUTION_STRING";
    private final TextView mTerminalOutView;
    private final Resources mResources;
    private final int mScreenWidth;

    public CommandResponseHandler(int screenWidth, Resources resources, TextView terminalOutView) {
        mTerminalOutView = terminalOutView;
        mScreenWidth = screenWidth;
        mResources = resources;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle inputBundle = msg.getData();
        if (inputBundle != null) {
            String[] results = inputBundle.getStringArray(COMMAND_EXECUTION_RESPONSE_KEY);
            String commandText = inputBundle.getString(COMMAND_EXECUTION_STRING_KEY);
            if (results != null && results.length != 0) {
                StringBuilder oldText = new StringBuilder(mTerminalOutView.getText());
                boolean isFilteredCommand = FilteredCommands.isFilteredCommand(commandText);
                if (isFilteredCommand) {
                    FilteredCommands filteredCommand = FilteredCommands.parseCommandTypeFromString(commandText);
                    if (filteredCommand != null) {
                        mTerminalOutView.setText(oldText + LINE_SEPARATOR
                                + filteredCommand.processResponse(mTerminalOutView, mScreenWidth, mResources, results));
                    }
                } else {
                    // write result to console
                    for (String s : results) {
                        oldText.append(LINE_SEPARATOR);
                        oldText.append(s);
                    }
                    mTerminalOutView.setText(oldText.toString());
                }
            }
        }
    }
}
