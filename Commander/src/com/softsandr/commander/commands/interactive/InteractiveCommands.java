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
package com.softsandr.commander.commands.interactive;

import java.util.HashMap;
import java.util.Map;

/**
 * The console commands that use console before not canceled
 */
public enum InteractiveCommands {
    TOP("top") {
        @Override
        public String getSpecificText() {
            return "top -n 1 -d 1"; // todo parsing real address and execution options
        }
    },
    PING("ping") {
        @Override
        public String getSpecificText() {
            return "ping localhost"; // todo parsing real address and execution options
        }
    },
    CAT("cat_man") {
        @Override
        public String getSpecificText() {
            return "cat_man"; // todo parsing real address and execution options
        }
    };

    private static final Map<String, InteractiveCommands> MAP_BY_VALUE = new HashMap<String, InteractiveCommands>();

    static {
        for (InteractiveCommands ic : values()) {
            MAP_BY_VALUE.put(ic.text, ic);
        }
    }

    private final String text;

    private InteractiveCommands(String text) {
        this.text = text;
    }

    public static boolean isInteractiveCommand(String command) {
        boolean isFiltered = false;
        for (InteractiveCommands fc : values()) {
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

    public static InteractiveCommands parseCommandTypeFromString(String command) {
        InteractiveCommands interactiveCommand = null;
        String onlyCommand = parseCommandTextFromString(command);
        for (InteractiveCommands ic : values()) {
            if (ic.text.equals(onlyCommand)) {
                interactiveCommand = ic;
                break;
            }
        }
        return interactiveCommand;
    }

    public abstract String getSpecificText();

    /**
     * Get constant by value
     * @param value
     * @return
     */
    public InteractiveCommands fromValue(String value) {
        return MAP_BY_VALUE.get(value);
    }

    /**
     * Get constant value
     * @return
     */
    public String getText() {
        return text;
    }
}
