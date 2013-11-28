package com.softsandr.terminal.commander.command;

import com.softsandr.terminal.commander.command.filtered.CdCommand;
import com.softsandr.terminal.commander.command.filtered.ClearCommand;
import com.softsandr.terminal.commander.command.filtered.ExitCommand;

import static com.drk.terminal.util.utils.StringUtil.EMPTY;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public enum FilteredCommand {
    CD("cd") {
        @Override
        public Command getCommand() {
            return new CdCommand();
        }
    },

    CLEAR("clear") {
        @Override
        public Command getCommand() {
            return new ClearCommand();
        }
    },

    EXIT("exit") {
        @Override
        public Command getCommand() {
            return new ExitCommand();
        }
    };

    String text;

    private FilteredCommand(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract Command getCommand();

    public static boolean isFilteredCommand(String command) {
        boolean isFiltered = false;
        for (FilteredCommand fc : values()) {
            if (fc.text.equals(parseCommandTextFromString(command))) {
                isFiltered = true;
                break;
            }
        }
        return isFiltered;
    }

    public static String parseCommandTextFromString(String command) {
        String commandPrefix = EMPTY;
        command = command.trim();
        if (command.contains(" ")) {
            commandPrefix = command.substring(0, command.indexOf(' '));
        } else {
            commandPrefix = command;
        }
        return commandPrefix;
    }

    public static FilteredCommand parseCommandTypeFromString(String command) {
        FilteredCommand filteredCommand = null;
        String onlyCommand = parseCommandTextFromString(command);
        for (FilteredCommand fc : values()) {
            if (fc.text.equals(onlyCommand)) {
                filteredCommand = fc;
                break;
            }
        }
        return filteredCommand;
    }
}
