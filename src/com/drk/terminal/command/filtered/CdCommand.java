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
                targetDirectory = DirectoryUtils.trimLastSlash(targetDirectory);
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
        DOT_SLASH("./") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return processPath;
            }
        },
        MANY_DOT_SLASH("././") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return processPath;
            }
        },
        SLASH_DOT("/.") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return PATH_SEPARATOR;
            }
        },
        MANY_SLASH_DOT("/./.") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return PATH_SEPARATOR;
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
        TWICE_DOT_SLASH("../") {
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
        MANY_TWICE_DOT_SLASH("../../") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                if (processPath.lastIndexOf(StringUtils.PATH_SEPARATOR) == 0) {
                    return StringUtils.PATH_SEPARATOR;
                } else {
                    StringBuilder processPathBuilder = new StringBuilder();
                    processPathBuilder.append(processPath.substring(1, processPath.length()));
                    processPathBuilder.append(StringUtils.PATH_SEPARATOR);
                    String[] pathDirs = processPathBuilder.toString().split(StringUtils.PATH_SEPARATOR);
                    int countOfUp = StringUtils.countOccurrences(commandTrimmedPath, StringUtils.PATH_SEPARATE_CHAR);
                    if (countOfUp >= pathDirs.length) {
                        return StringUtils.PATH_SEPARATOR;
                    } else {
                        int subCount = countOfUp - pathDirs.length;
                        StringBuilder responsePath = new StringBuilder();
                        responsePath.append(StringUtils.PATH_SEPARATOR);
                        for (int i = 0; i < subCount; i++) {
                            responsePath.append(pathDirs[i]);
                            responsePath.append(StringUtils.PATH_SEPARATOR);
                        }
                        return responsePath.toString();
                    }
                }
            }
        },
        SLASH_TWICE_DOT("/..") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return PATH_SEPARATOR;
            }
        },
        MANY_SLASH_TWICE_DOT("/../../") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                return PATH_SEPARATOR;
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
                if (targetDirectory.equals(pl.getLocation()) ||
                        targetDirectory.startsWith(pl.getLocation())) {
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
            if (type == null) {
               if (targetDirectory.startsWith(MANY_DOT_SLASH.location)) {
                  type = MANY_DOT_SLASH;
               } else if (targetDirectory.startsWith(MANY_SLASH_DOT.location)) {
                  type = MANY_SLASH_DOT;
               } else if (targetDirectory.startsWith(MANY_TWICE_DOT_SLASH.location)) {
                  type = MANY_TWICE_DOT_SLASH;
               } else if (targetDirectory.startsWith(MANY_SLASH_TWICE_DOT.location)) {
                  type = MANY_SLASH_TWICE_DOT;
               }
            }
            return type;
        }
    }
}
