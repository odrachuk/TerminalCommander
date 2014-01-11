/*******************************************************************************
 * Created by o.drachuk on 11/01/2014. 
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
package com.softsandr.commander.process.execution;

import android.os.Bundle;
import android.os.Message;
import com.softsandr.commander.commands.local.LocalCommand;
import com.softsandr.commander.commands.local.LocalCommands;
import com.softsandr.commander.process.CommanderProcess;
import com.softsandr.commander.process.CommandsResponseHandler;

import static com.softsandr.utils.string.StringUtil.EMPTY;

/**
 * This class used for execution local commands from {@link com.softsandr.commander.commands.local.LocalCommands}
 */
public class LocalCommandExecution extends CommandExecution {
    private String responseText;
    private LocalCommand command;

    public LocalCommandExecution(CommandsResponseHandler responseHandler,
                                    String commandText, String userLocation,
                                    CommanderProcess commanderProcess) {
        super(responseHandler, commandText, userLocation, commanderProcess);
    }

    @Override
    public void prepareExecution() {
        responseText = EMPTY;
        command = LocalCommands.parseCommandTypeFromString(this.commandText)
                .newCommand(commanderProcess, commandText, userLocation);
    }

    @Override
    public void makeExecution() {
        String isExecutableCallback = command.isExecutable();
        if (isExecutableCallback.isEmpty()) {
            // executable
            responseText += command.onExecute();
        } else {
            // not executable
            responseText += isExecutableCallback;
        }
    }

    @Override
    public void postExecution() {
        if (!responseText.isEmpty()) {
            String[] resultArray = new String[]{responseText};
            Bundle resultBundle = new Bundle();
            resultBundle.putStringArray(CommandsResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
            resultBundle.putString(CommandsResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
            Message resultMessage = responseHandler.obtainMessage();
            resultMessage.setData(resultBundle);
            responseHandler.sendMessage(resultMessage);
        }
    }
}
