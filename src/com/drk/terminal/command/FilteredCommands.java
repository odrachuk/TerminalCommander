package com.drk.terminal.command;

import com.drk.terminal.command.filtered.CdCommand;
import com.drk.terminal.command.filtered.ClearCommand;
import com.drk.terminal.command.filtered.ExitCommand;
import com.drk.terminal.command.filtered.SuCommand;

import static com.drk.terminal.utils.StringUtils.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public enum FilteredCommands {
    CD("cd") {
        @Override
        public FilteredCommand getCommand() {
            return new CdCommand();
        }
    },

    SU("su") {
        @Override
        public FilteredCommand getCommand() {
            return new SuCommand();
        }
    },

    CLEAR("clear") {
        @Override
        public FilteredCommand getCommand() {
            return new ClearCommand();
        }
    },

    EXIT("exit") {
        @Override
        public FilteredCommand getCommand() {
            return new ExitCommand();
        }
    };

    String text;

    private FilteredCommands(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract FilteredCommand getCommand();

    public static boolean isFilteredCommand(String command) {
        for (FilteredCommands fc : values()) {
            String commandPrefix = EMPTY;
            command = command.trim();
            if (command.contains(" ")) {
                commandPrefix = command.substring(0, command.indexOf(' '));
            } else {
                commandPrefix = command;
            }
            if (fc.text.equals(commandPrefix)) {
                return true;
            }
        }
        return false;
    }

    public static FilteredCommands getByName(String command) {
        for (FilteredCommands fc : values()) {
            if (fc.text.equals(command)) {
                return fc;
            }
        }
        return null;
    }
}
