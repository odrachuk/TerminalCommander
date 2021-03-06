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

import static com.softsandr.utils.string.StringUtil.EMPTY;

/**
 * The custom logic for execution clear_man command from console
 */
public class ClearCommand extends LocalCommand {

    protected ClearCommand(CommanderProcess commanderProcess, String commandText, String userLocation) {
        super(commanderProcess, commandText, userLocation);
    }

    @Override
    public String isExecutable() {
        return EMPTY;
    }

    @Override
    public String onExecute() {
        commanderProcess.onClear();
        return EMPTY;
    }
}
