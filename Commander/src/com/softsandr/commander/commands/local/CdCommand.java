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
package com.softsandr.commander.commands.local;

import com.softsandr.commander.process.CommanderProcess;
import com.softsandr.terminal.R;
import com.softsandr.utils.file.FileUtil;
import com.softsandr.utils.string.StringUtil;

import static com.softsandr.utils.string.StringUtil.EMPTY;
import static com.softsandr.utils.string.StringUtil.PATH_SEPARATOR;

/**
 * The custom logic for execution cd command
 */
public class CdCommand extends LocalCommand {

    protected CdCommand(CommanderProcess commanderProcess, String commandText, String userLocation) {
        super(commanderProcess, commandText, userLocation);
    }

    @Override
    public String isExecutable() {
        String callbackString = EMPTY;
        String allCommand = commandText.trim();
        if (allCommand.indexOf(' ') > 0) {
            String targetDirectory = allCommand.substring(allCommand.indexOf(' ') + 1, allCommand.length());
            if (FileUtil.isDirectoryExist(userLocation, targetDirectory)) {
                if (!FileUtil.canChangeDirectory(userLocation, targetDirectory)) {
                    callbackString += commanderProcess.getCommander().getActivity().getString(R.string.not_enough_permission_for_filesystem_reading);
                }
            } else {
                callbackString += commanderProcess.getCommander().getActivity().getString(R.string.target_is_not_directory);
            }
        } else {
            callbackString += commanderProcess.getCommander().getActivity().getString(R.string.not_arguments);
        }
        return callbackString;
    }

    @Override
    public String onExecute() {
        String callbackString = EMPTY;
        try {
            String allCommand = commandText.trim();
            if (allCommand.indexOf(' ') > 0) {
                String targetDirectory = allCommand.substring(allCommand.indexOf(' ') + 1, allCommand.length());
                targetDirectory = FileUtil.trimLastSlash(targetDirectory);
                StringBuilder targetFullPath = new StringBuilder(EMPTY);
                if (PredefinedLocation.isPredefinedLocation(targetDirectory)) {
                    targetFullPath.append(PredefinedLocation.getType(targetDirectory).
                            getTransformedPath(userLocation, targetDirectory));
                } else {
                    targetFullPath.append(FileUtil.buildDirectoryPath(userLocation, targetDirectory));
                }
                commanderProcess.onChangeDirectory(targetFullPath.toString());
            }
        } catch (Exception e) {
            callbackString = commanderProcess.getCommander().getActivity().getString(R.string.cannot_start_main_process);
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
                if (processPath.lastIndexOf(StringUtil.PATH_SEPARATOR) == 0) {
                    return StringUtil.PATH_SEPARATOR;
                } else {
                    String parentPath = processPath.substring(0, processPath.lastIndexOf("/"));
                    return parentPath;
                }
            }
        },
        TWICE_DOT_SLASH("../") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                if (processPath.lastIndexOf(StringUtil.PATH_SEPARATOR) == 0) {
                    return StringUtil.PATH_SEPARATOR;
                } else {
                    String parentPath = processPath.substring(0, processPath.lastIndexOf("/"));
                    return parentPath;
                }
            }
        },
        MANY_TWICE_DOT_SLASH("../../") {
            @Override
            public String getTransformedPath(String processPath, String commandTrimmedPath) {
                if (processPath.lastIndexOf(StringUtil.PATH_SEPARATOR) == 0) {
                    return StringUtil.PATH_SEPARATOR;
                } else {
                    StringBuilder processPathBuilder = new StringBuilder();
                    processPathBuilder.append(processPath.substring(1, processPath.length()));
                    processPathBuilder.append(StringUtil.PATH_SEPARATOR);
                    String[] pathDirs = processPathBuilder.toString().split(StringUtil.PATH_SEPARATOR);
                    int countOfUp = StringUtil.countOccurrences(commandTrimmedPath, StringUtil.PATH_SEPARATE_CHAR);
                    if (countOfUp >= pathDirs.length) {
                        return StringUtil.PATH_SEPARATOR;
                    } else {
                        int subCount = countOfUp - pathDirs.length;
                        StringBuilder responsePath = new StringBuilder();
                        responsePath.append(StringUtil.PATH_SEPARATOR);
                        for (int i = 0; i < subCount; i++) {
                            responsePath.append(pathDirs[i]);
                            responsePath.append(StringUtil.PATH_SEPARATOR);
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
