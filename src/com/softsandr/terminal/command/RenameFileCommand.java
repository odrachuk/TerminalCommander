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
package com.softsandr.terminal.command;

import android.util.Log;
import android.widget.Toast;
import com.softsandr.terminal.R;
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.utils.file.FileUtil;
import com.softsandr.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * This class contain logic that used when executing rename operations
 */
public class RenameFileCommand implements FileManipulationCommand {
    private static final String LOG_TAG = RenameFileCommand.class.getSimpleName();
    private final TerminalActivityImpl terminalActivity;
    private final ListViewItem item;
    private final String destinationOldPath;
    private final String currentPath;
    private final boolean pathChanged;
    private String destinationPath;

    public RenameFileCommand(TerminalActivityImpl terminalActivity,
                             ListViewItem item,
                             String destinationPath,
                             String destinationOldPath,
                             String currentPath,
                             boolean pathChanged) {
        this.terminalActivity = terminalActivity;
        this.item = item;
        this.destinationPath = destinationPath;
        this.destinationOldPath = destinationOldPath;
        this.currentPath = currentPath;
        this.pathChanged = pathChanged;
    }

    @Override
    public void onExecute() {
        try {
            String renamingString = null;
            // determining rename situation for simple file - not directory
            if (destinationPath.contains(".")) {
                renamingString = FileUtil.getFileNameFromPath(destinationPath);
            } else {
                if (destinationPath.lastIndexOf(StringUtil.PATH_SEPARATOR) == destinationPath.length()) {
                    String correctPath = destinationPath.substring(0, destinationPath.length() - 1);
                    int lastPathSeparator = correctPath.lastIndexOf(StringUtil.PATH_SEPARATOR);
                    renamingString = correctPath.substring(lastPathSeparator, correctPath.length());
                } else {
                    int lastPathSeparator = destinationPath.lastIndexOf(StringUtil.PATH_SEPARATOR);
                    renamingString = destinationPath.substring(lastPathSeparator, destinationPath.length());
                }
            }
            // get parent directory name
            destinationPath = FileUtil.getParentDirectoryNameFromPath(destinationPath);
            File srcFile = new File(item.getAbsPath());
            if (renamingString != null) {
                File destinationFile = new File(destinationPath + StringUtil.PATH_SEPARATOR + renamingString);
                FileUtil.renameFile(srcFile, destinationFile);
            }
            // clear selected
            makeClearSelection();
            makeRefreshDirectory();
        } catch (IOException e) {
            Log.e(LOG_TAG, "onExecute", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
            makeClearSelection();
        }
    }

    private void makeClearSelection() {
        switch (terminalActivity.getActivePage()) {
            case LEFT:
                terminalActivity.getLeftListAdapter().clearSelection();
                break;
            case RIGHT:
                terminalActivity.getRightListAdapter().clearSelection();
                break;
        }
    }

    private void makeRefreshDirectory() {
        switch (terminalActivity.getActivePage()) {
            case LEFT:
                if (destinationOldPath.equals(currentPath)) {
                    terminalActivity.getRightListAdapter().changeDirectory(destinationOldPath);
                } else if (!pathChanged) {
                    terminalActivity.getRightListAdapter().changeDirectory(destinationPath);
                }
                terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                break;
            case RIGHT:
                if (destinationOldPath.equals(currentPath)) {
                    terminalActivity.getLeftListAdapter().changeDirectory(destinationOldPath);
                } else if (!pathChanged) {
                    terminalActivity.getLeftListAdapter().changeDirectory(destinationPath);
                }
                terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                break;
        }
    }
}
