package com.drk.terminal.command.filtered;

import com.drk.terminal.command.Command;
import com.drk.terminal.process.TerminalProcess;
import com.drk.terminal.utils.DirectoryUtils;
import com.drk.terminal.utils.StringUtils;

import static com.drk.terminal.utils.StringUtils.EMPTY;
import static com.drk.terminal.utils.StringUtils.PATH_SEPARATOR;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 9/25/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class CdCommand implements Command {
    @Override
    public String isExecutable(TerminalProcess terminalProcess) {
        String callbackString = EMPTY;
        String allCommand = terminalProcess.getCommandText().trim();
        if (allCommand.indexOf(' ') > 0) {
            String targetDirectory = allCommand.substring(allCommand.indexOf(' ') + 1, allCommand.length());
            if (DirectoryUtils.isDirectoryExist(terminalProcess.getProcessPath(), targetDirectory)) {
                if (!DirectoryUtils.canChangeDirectory(terminalProcess.getProcessPath(), targetDirectory)) {
                    callbackString += "Cannot read directory";
                }
            } else {
                callbackString += "Target is not directory";
            }
        } else {
            callbackString += "Not arguments";
        }
        return callbackString;
    }

    @Override
    public String onExecute(TerminalProcess terminalProcess) {
        String callbackString = EMPTY;
        try {
            String allCommand = terminalProcess.getCommandText().trim();
            if (allCommand.indexOf(' ') > 0) {
                String targetDirectory = allCommand.substring(allCommand.indexOf(' ') + 1, allCommand.length());
                StringBuilder targetFullPath = new StringBuilder(EMPTY);
                if (PredefinedLocation.isPredefinedLocation(targetDirectory)) {
                    targetFullPath.append(PredefinedLocation.getType(targetDirectory).
                            getTransformedPath(terminalProcess.getProcessPath(), targetDirectory));
                } else {
                    targetFullPath.append(DirectoryUtils.buildDirectoryPath(terminalProcess.getProcessPath(),
                            targetDirectory));
                }
                terminalProcess.onChangeDirectory(targetFullPath.toString());
            }
        } catch (Exception e) {
            callbackString += "Command execution exception";
        }
        return callbackString;
    }

    public enum PredefinedLocation {
        DOT(".") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return processPath;
            }
        },
        TWICE_DOT("..") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                if (processPath.lastIndexOf(StringUtils.PATH_SEPARATOR) == 0) {
                    return StringUtils.PATH_SEPARATOR;
                } else {
                    String parentPath = processPath.substring(0, processPath.lastIndexOf("/"));
                    return parentPath;
                }
            }
        },
        SLASH(PATH_SEPARATOR) {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return PATH_SEPARATOR;
            }
        };

        private String location;

        private PredefinedLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return this.location;
        }

        public abstract String getTransformedPath(String processPath, String commandTrimmedPath);

        public static boolean isPredefinedLocation(String targetDirectory) {
            boolean isPredefined = false;
            for (PredefinedLocation pl : PredefinedLocation.values()) {
                if (pl.getLocation().equals(targetDirectory)) {
                    isPredefined = true;
                    break;
                }
            }
            return isPredefined;
        }

        public static PredefinedLocation getType(String targetDirectory) {
            PredefinedLocation type = null;
            for (PredefinedLocation pl : PredefinedLocation.values()) {
                if (pl.getLocation().equals(targetDirectory)) {
                    type = pl;
                    break;
                }
            }
            return type;
        }
    }
}
