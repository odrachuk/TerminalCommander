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
package com.softsandr.commander.process;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import com.softsandr.commander.Commander;
import com.softsandr.commander.commands.filtered.FilteredCommands;

import static com.softsandr.utils.string.StringUtil.LINE_SEPARATOR;

/**
 * The {@link android.os.Handler} extends for handling response from console input after execution an command
 */
public class CommandsResponseHandler extends Handler {
    private static final String LOG_TAG = CommandsResponseHandler.class.getSimpleName();
    public static final String COMMAND_EXECUTION_RESPONSE_KEY = LOG_TAG + ".COMMAND_EXECUTION_RESPONSE";
    public static final String COMMAND_EXECUTION_STRING_KEY = LOG_TAG + ".COMMAND_EXECUTION_STRING";
    public static final String COMMAND_EXECUTION_CLEAR_KEY = LOG_TAG + ".COMMAND_EXECUTION_CLEAR";
    public static final String COMMAND_EXECUTION_HIDE_KEY = LOG_TAG + ".COMMAND_EXECUTION_HIDE";
    private final Commander commander;
    private final int screenWidth;

    public CommandsResponseHandler(Commander commander) {
        DisplayMetrics dm = new DisplayMetrics();
        commander.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.screenWidth = dm.widthPixels;
        this.commander = commander;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle inputBundle = msg.getData();
        if (inputBundle != null) {
            String[] results = inputBundle.getStringArray(COMMAND_EXECUTION_RESPONSE_KEY);
            String commandText = inputBundle.getString(COMMAND_EXECUTION_STRING_KEY);
            if (inputBundle.containsKey(COMMAND_EXECUTION_HIDE_KEY)) {
                // situation when we hide input elements and display cancel button
                // todo
            } else {
                if (inputBundle.containsKey(COMMAND_EXECUTION_CLEAR_KEY)) {
                    commander.getOutTextView().setText("");
                }
                if (results != null && results.length != 0) {
                    StringBuilder oldText = new StringBuilder(commander.getOutTextView().getText());
                    boolean isFilteredCommand = FilteredCommands.isFilteredCommand(commandText);
                    if (isFilteredCommand) {
                        FilteredCommands filteredCommand = FilteredCommands.parseCommandTypeFromString(commandText);
                        if (filteredCommand != null) {
                            commander.getOutTextView().setText(oldText
                                    + LINE_SEPARATOR
                                    + filteredCommand.processResponse(
                                    commander.getOutTextView(), screenWidth,
                                    commander.getActivity().getResources(), results));
                        }
                    } else {
                        for (String s : results) {
                            oldText.append(LINE_SEPARATOR);
                            oldText.append(s);
                        }
                        commander.getOutTextView().setText(oldText.toString());
                    }
                }
            }
        }
    }
}
