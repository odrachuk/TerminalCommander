package com.softsandr.terminal.commander;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import com.softsandr.terminal.commander.command.filtered.FilteredCommands;

import static com.drk.terminal.util.utils.StringUtil.LINE_SEPARATOR;

/**
 * Date: 12/2/13
 *
 * @author Drachuk O.V.
 */
public class CommandResponseHandler extends Handler {
    private static final String LOG_TAG = CommandResponseHandler.class.getSimpleName();
    public static final String COMMAND_EXECUTION_RESPONSE_KEY = LOG_TAG + ".COMMAND_EXECUTION_RESPONSE";
    public static final String COMMAND_EXECUTION_STRING_KEY = LOG_TAG + ".COMMAND_EXECUTION_STRING";
    private final TextView mTerminalOutView;
    private final Resources mResources;
    private final int mScreenWidth;

    public CommandResponseHandler(int screenWidth, Resources resources, TextView terminalOutView) {
        mTerminalOutView = terminalOutView;
        mScreenWidth = screenWidth;
        mResources = resources;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle inputBundle = msg.getData();
        String[] results = inputBundle.getStringArray(COMMAND_EXECUTION_RESPONSE_KEY);
        String commandText = inputBundle.getString(COMMAND_EXECUTION_STRING_KEY);
        if (results != null && results.length != 0) {
            StringBuilder oldText = new StringBuilder(mTerminalOutView.getText());
            boolean isFilteredCommand = FilteredCommands.isFilteredCommand(commandText);
            if (isFilteredCommand) {
                FilteredCommands filteredCommand = FilteredCommands.parseCommandTypeFromString(commandText);
                if (filteredCommand != null) {
                    StringBuilder result = new StringBuilder(oldText).append(LINE_SEPARATOR);
                    result.append(filteredCommand.alignResponse(mTerminalOutView, mScreenWidth, mResources, results));
                    mTerminalOutView.setText(result.toString());
                }
            } else {
                // write result to console
                for (String s : results) {
                    oldText.append(LINE_SEPARATOR);
                    oldText.append(s);
                }
                mTerminalOutView.setText(oldText.toString());
            }
        }
    }
}
