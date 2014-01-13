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
import com.softsandr.utils.file.FileUtil;
import com.softsandr.utils.string.StringUtil;
import com.softsandr.terminal.model.listview.ListViewItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class contain logic that used when executing move or rename operations
 */
public class MoveRenameFileCommand implements FileManipulationCommand {
    private static final String LOG_TAG = MoveRenameFileCommand.class.getSimpleName();
    private final TerminalActivityImpl terminalActivity;
    private final List<ListViewItem> items;
    private final String destinationOldPath;
    private final String currentPath;
    private final boolean pathChanged;
    private String destinationPath;

    public MoveRenameFileCommand(TerminalActivityImpl terminalActivity,
                                 List<ListViewItem> items,
                                 String destinationPath,
                                 String destinationOldPath,
                                 String currentPath,
                                 boolean pathChanged) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.destinationPath = destinationPath;
        this.destinationOldPath = destinationOldPath;
        this.currentPath = currentPath;
        this.pathChanged = pathChanged;
    }

    @Override
    public void onExecute() {
        if (items != null && !items.isEmpty()) {
            try {
                String renamingString = null;
                if (destinationPath.contains(".")) { // determining rename situation for simple file - not directory
                    renamingString = FileUtil.getFileNameFromPath(destinationPath);
                    destinationPath = FileUtil.getDirectoryNameFromPath(destinationPath);
                }
                if (renamingString != null && items.size() > 1) {
                    Toast.makeText(terminalActivity, terminalActivity.getString(R.string.toast_cannot_rename_multiple_objects),
                            Toast.LENGTH_SHORT).show();
                    makeClearSelection();
                } else {
                    File dstDirectory = new File(destinationPath);
                    for (ListViewItem item : items) {
                        File srcFile = new File(item.getAbsPath());
                        if (renamingString != null) {
                            // rename simple file - not directory
                            File destinationFile = new File(destinationPath + StringUtil.PATH_SEPARATOR + renamingString);
                            FileUtil.renameFile(srcFile, destinationFile);
                        } else {
                            // rename or move directory
                            FileUtil.moveToDirectory(srcFile, dstDirectory, true);
                        }
                    }
                    // clear selected
                    makeClearSelection();
                    makeRefreshDirectory();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Move/Rename", e);
                Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                makeClearSelection();
            }
        } else {
            Toast.makeText(terminalActivity, terminalActivity.getString(R.string.toast_no_objects_selected),
                    Toast.LENGTH_SHORT).show();
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
