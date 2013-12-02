package com.softsandr.terminal.commander.command.filtered;

import android.util.Log;
import com.softsandr.terminal.commander.command.local.*;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public enum FilteredCommands {
    LS("ls") {
        @Override
        public LocalCommand getCommand() {
            return new LsCommand();
        }

        @Override
        public String getAlignResponse(String[] commandResponse) {
            StringBuilder alignResponse = new StringBuilder();
            for (int i = 0; i < commandResponse.length; i++) {
                String oneRowResponse = commandResponse[i];
                String[] oneRowTokens = oneRowResponse.split("\\s+");
                String response = "";
                for (int j = 0; j < oneRowTokens.length; j++) {
                    response += "<>" + oneRowTokens[j];
                }
                Log.d(LOG_TAG, response);
            }
            return alignResponse.toString();
        }
    };

    private static final String LOG_TAG = FilteredCommands.class.getSimpleName();

    String text;

    private FilteredCommands(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract LocalCommand getCommand();
    public abstract String getAlignResponse(String[] commandResponse);

    public static boolean isFilteredCommand(String command) {
        boolean isFiltered = false;
        for (FilteredCommands fc : values()) {
            if (fc.text.equals(parseCommandTextFromString(command))) {
                isFiltered = true;
                break;
            }
        }
        return isFiltered;
    }

    private static String parseCommandTextFromString(String command) {
        String commandPrefix;
        command = command.trim();
        if (command.contains(" ")) {
            commandPrefix = command.substring(0, command.indexOf(' '));
        } else {
            commandPrefix = command;
        }
        return commandPrefix;
    }

    public static FilteredCommands parseCommandTypeFromString(String command) {
        FilteredCommands filteredCommand = null;
        String onlyCommand = parseCommandTextFromString(command);
        for (FilteredCommands fc : values()) {
            if (fc.text.equals(onlyCommand)) {
                filteredCommand = fc;
                break;
            }
        }
        return filteredCommand;
    }
}
