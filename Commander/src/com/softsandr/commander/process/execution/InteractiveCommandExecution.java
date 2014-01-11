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
import com.softsandr.commander.process.CommanderProcess;
import com.softsandr.commander.process.CommandsResponseHandler;

/**
 * This class used for execution all commands described in
 * {@link com.softsandr.commander.commands.interactive.InteractiveCommands}
 */
public class InteractiveCommandExecution extends CommandExecution {

    public InteractiveCommandExecution(CommandsResponseHandler responseHandler,
                                          String commandText, String userLocation,
                                          CommanderProcess commanderProcess) {
        super(responseHandler, commandText, userLocation, commanderProcess);
    }

    @Override
    public void prepareExecution() {

    }

    @Override
    public void makeExecution() {

    }

    @Override
    public void postExecution() {
        String[] resultArray = new String[]{"NOTHING!!!"};
        Bundle resultBundle = new Bundle();
        resultBundle.putStringArray(CommandsResponseHandler.COMMAND_EXECUTION_RESPONSE_KEY, resultArray);
        resultBundle.putString(CommandsResponseHandler.COMMAND_EXECUTION_STRING_KEY, commandText);
        Message resultMessage = responseHandler.obtainMessage();
        resultMessage.setData(resultBundle);
        responseHandler.sendMessage(resultMessage);
    }
}
