package com.softsandr.terminal.commander.command.local;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public enum LocalCommands {
    CD("cd") {
        @Override
        public LocalCommand getCommand() {
            return new CdCommand();
        }
    },

    CLEAR("clear") {
        @Override
        public LocalCommand getCommand() {
            return new ClearCommand();
        }
    },

    EXIT("exit") {
        @Override
        public LocalCommand getCommand() {
            return new ExitCommand();
        }
    };

    String text;

    private LocalCommands(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract LocalCommand getCommand();

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
