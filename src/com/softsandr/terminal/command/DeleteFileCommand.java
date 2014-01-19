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
import com.softsandr.terminal.activity.terminal.TerminalActivityImpl;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.utils.file.FileUtil;

import java.io.File;
import java.util.List;

/**
 * This class contain logic of delete file operation
 */
public class DeleteFileCommand implements FileManipulationCommand {
    private static final String LOG_TAG = DeleteFileCommand.class.getSimpleName();
    private final TerminalActivityImpl terminalActivity;
    private final List<ListViewItem> items;
    private final String currentPath;
    private final String destinationPath;

    public DeleteFileCommand(TerminalActivityImpl terminalActivity, List<ListViewItem> items,
                             String currentPath,
                             String destinationPath) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.currentPath = currentPath;
        this.destinationPath = destinationPath;
    }

    @Override
    public void onExecute() {
        try {
            for (ListViewItem item : items) {
                File file = new File(item.getAbsPath());
                if (file.canRead()) {
                    FileUtil.deleteQuietly(file);
                } else {
                    Toast.makeText(terminalActivity, "" + file.getName(), Toast.LENGTH_SHORT).show();
                }
                makeRefresh();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "onExecute", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
            makeRefresh();
        }
    }

    private void makeRefresh() {
        terminalActivity.getLeftListAdapter().clearSelection();
        terminalActivity.getRightListAdapter().clearSelection();
        if (currentPath.equals(destinationPath)) {
            terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
            terminalActivity.getRightListAdapter().changeDirectory(currentPath);
        } else {
            switch (terminalActivity.getActivePage()) {
                case LEFT:
                    terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                    if (destinationPath.contains(currentPath)) {
                        terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                        terminalActivity.getRightListAdapter().makeBackHistory(currentPath);
                    }
                    break;
                case RIGHT:
                    terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                    if (destinationPath.contains(currentPath)) {
                        terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                        terminalActivity.getLeftListAdapter().makeBackHistory(currentPath);
                    }
                    break;
            }
        }
    }
}
