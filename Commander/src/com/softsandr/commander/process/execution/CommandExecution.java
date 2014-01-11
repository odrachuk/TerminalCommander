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

import com.softsandr.commander.process.CommanderProcess;
import com.softsandr.commander.process.CommandsResponseHandler;

/**
 * This class describe command execution skeleton
 */
public abstract class CommandExecution {
    protected final CommandsResponseHandler responseHandler;
    protected final CommanderProcess commanderProcess;
    protected final String commandText, userLocation;

    protected CommandExecution(CommandsResponseHandler responseHandler,
                               String commandText, String userLocation,
                               CommanderProcess commanderProcess) {
        this.commanderProcess = commanderProcess;
        this.responseHandler = responseHandler;
        this.commandText = commandText;
        this.userLocation = userLocation;
    }

    public final void execute() {
        prepareExecution();
        makeExecution();
        postExecution();
    }

    /**
     * Prepare logic before execute an command
     */
    public abstract void prepareExecution();

    /**
     * Make execution an command logic
     */
    public abstract void makeExecution();

    /**
     * Parsing response execution an command and sending response
     */
    public abstract void postExecution();
}
