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
package com.softsandr.terminal.commander.command.local;

import com.softsandr.terminal.commander.Commander;
import com.softsandr.terminal.commander.TerminalCommander;

import static com.softsandr.utils.string.StringUtil.EMPTY;

/**
 * The custom logic of execution exit command from console
 */
public class ExitCommand implements LocalCommand {
    @Override
    public String isExecutable(TerminalCommander terminalCommander) {
        return EMPTY;
    }

    @Override
    public String onExecute(TerminalCommander terminalCommander) {
        if (terminalCommander.getUiController().getActivity() instanceof Commander) {
            ((Commander) terminalCommander.getUiController().getActivity()).exitActivity();
        }
        return EMPTY;
    }
}
