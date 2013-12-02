package com.softsandr.terminal.commander.command.filtered;

import android.content.res.Resources;
import android.util.Log;
import com.drk.terminal.util.utils.StringUtil;
import com.softsandr.terminal.R;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public enum FilteredCommands {
    LS("ls -l") {
        @Override
        public String alignResponse(Resources resources, String[] commandResponse) {
            StringBuilder alignResponse = new StringBuilder();
            for (String oneRowResponse : commandResponse) {
                String[] oneRowTokens = oneRowResponse.split("\\s+");
                StringBuilder oneRow = new StringBuilder();
                int i = 0;
                for (String oneRowToken : oneRowTokens) {
                    if (i < 6) {
                        oneRow.append(oneRowToken)
                                .append(resources.getString(R.string.tabulate))
                                .append(resources.getString(R.string.tabulate))
                                .append(resources.getString(R.string.tabulate));
                    } else {
                        oneRow.append(oneRowToken).append(resources.getString(R.string.whitespace));
                    }
                }
                alignResponse.append(oneRow.toString()).append(StringUtil.LINE_SEPARATOR);
                Log.d(LOG_TAG, oneRow.toString());
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

    public abstract String alignResponse(Resources resources, String[] commandResponse);

    public static boolean isFilteredCommand(String command) {
        boolean isFiltered = false;
        for (FilteredCommands fc : values()) {
            if (fc.text.equals(command.trim())) {
                isFiltered = true;
                break;
            }
        }
        return isFiltered;
    }

    public static FilteredCommands parseCommandTypeFromString(String command) {
        FilteredCommands filteredCommand = null;
        for (FilteredCommands fc : values()) {
            if (fc.text.equals(command.trim())) {
                filteredCommand = fc;
                break;
            }
        }
        return filteredCommand;
    }
}
