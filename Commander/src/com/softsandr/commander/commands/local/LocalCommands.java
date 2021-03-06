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
 * The container of constants that are all supported local command in App
 */
public enum LocalCommands {
    CD("cd") {
        @Override
        public LocalCommand newCommand(CommanderProcess commanderProcess, String commandText, String userLocation) {
            return new CdCommand(commanderProcess, commandText, userLocation);
        }
    },

    CLEAR("clear") {
        @Override
        public LocalCommand newCommand(CommanderProcess commanderProcess, String commandText, String userLocation) {
            return new ClearCommand(commanderProcess, commandText, userLocation);
        }
    },

    EXIT("exit") {
        @Override
        public LocalCommand newCommand(CommanderProcess commanderProcess, String commandText, String userLocation) {
            return new ExitCommand(commanderProcess, commandText, userLocation);
        }
    };

    private final String text;

    private LocalCommands(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract LocalCommand newCommand(CommanderProcess commanderProcess,
                                            String commandText, String userLocation);

    public static boolean isLocalCommand(String command) {
        boolean isFiltered = false;
        for (LocalCommands fc : values()) {
            if (fc.text.equals(parseCommandTextFromString(command))) {
                isFiltered = true;
                break;
            }
        }
        return isFiltered;
    }

    public static String parseCommandTextFromString(String command) {
        String commandPrefix;
        command = command.trim();
        if (command.contains(" ")) {
            commandPrefix = command.substring(0, command.indexOf(' '));
        } else {
            commandPrefix = command;
        }
        return commandPrefix;
    }

    public static LocalCommands parseCommandTypeFromString(String command) {
        LocalCommands filteredCommand = null;
        String onlyCommand = parseCommandTextFromString(command);
        for (LocalCommands fc : values()) {
            if (fc.text.equals(onlyCommand)) {
                filteredCommand = fc;
                break;
            }
        }
        return filteredCommand;
    }
}
