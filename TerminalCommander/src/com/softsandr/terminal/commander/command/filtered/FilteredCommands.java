package com.softsandr.terminal.commander.command.filtered;

import android.content.res.Resources;
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
            LsRowsList rowRecords = new LsRowsList();
            for (String oneRowResponse : commandResponse) {
                String[] oneRowTokens = oneRowResponse.split("\\s+");
                LsRowRecord rowRecord = null;
                for (int i = 0; i < oneRowTokens.length; i++) {
                    String oneRowToken = oneRowTokens[i];
                    switch (i) {
                        case 0:
                            rowRecord = new LsRowRecord();
                            rowRecord.setPermissionsToken(oneRowToken);
                            break;
                        case 1:
                            rowRecord.setOwnerToken(oneRowToken);
                            break;
                        case 2:
                            rowRecord.setGroupToken(oneRowToken);
                            break;
                        case 3:
                            if (oneRowToken.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                rowRecord.setDateToken(oneRowToken);
                            } else {
                                rowRecord.setSizeToken(oneRowToken);
                            }
                            break;
                        case 4:
                            if (oneRowToken.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                rowRecord.setDateToken(oneRowToken);
                            } else if (oneRowToken.matches("\\d{2}:\\d{2}")) {
                                rowRecord.setDateToken(rowRecord.getDateToken() +
                                        resources.getString(R.string.whitespace) + oneRowToken);
                            }
                            break;
                        default:
                            if (rowRecord.getSizeToken() != null && oneRowToken.matches("\\d{2}:\\d{2}")) {
                                rowRecord.setDateToken(rowRecord.getDateToken() +
                                        resources.getString(R.string.whitespace) + oneRowToken);
                            } else {
                                if (rowRecord.getNameToken() != null) {
                                    rowRecord.setNameToken(rowRecord.getNameToken() +
                                            resources.getString(R.string.whitespace) + oneRowToken);
                                } else {
                                    rowRecord.setNameToken(oneRowToken);
                                }
                            }
                            break;
                    }
                    if (i == oneRowTokens.length - 1) {
                        rowRecords.add(rowRecord);
                    }
                }
            }
            rowRecords.alignTokens(resources);
            return rowRecords.toString(resources);
        }
    };

    String text;

    private FilteredCommands(String text) {
        this.text = text;
    }

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

    public String getText() {
        return text;
    }

    public abstract String alignResponse(Resources resources, String[] commandResponse);
}
