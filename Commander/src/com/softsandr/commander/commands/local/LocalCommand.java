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
package com.softsandr.commander.commands.local;

import com.softsandr.commander.process.CommanderProcess;

/**
 * The instantiation of the interface represent command logic of that will be customized
 */
public abstract class LocalCommand {
    protected final String commandText, userLocation;
    protected final CommanderProcess commanderProcess;

    protected LocalCommand(CommanderProcess commanderProcess, String commandText, String userLocation) {
        this.commanderProcess = commanderProcess;
        this.commandText = commandText;
        this.userLocation = userLocation;
    }

    /**
     * Check execution possibility
     * @return true if execution specific command is possible and false in other case_man
     */
    public abstract String isExecutable();

    /**
     * Make execute of specific command
     * @return String result of execution command
     */
    public abstract String onExecute();
}
