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
import com.softsandr.terminal.data.listview.ListViewItem;
import com.softsandr.utils.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class contain logic of copy operation
 */
public class CopyFileCommand implements FileManipulationCommand {
    private static final String LOG_TAG = CopyFileCommand.class.getSimpleName();
    private final TerminalActivityImpl terminalActivity;
    private final List<ListViewItem> items;
    private final String destinationPath;
    private final boolean pathChanged;

    public CopyFileCommand(TerminalActivityImpl terminalActivity,
                           List<ListViewItem> items,
                           String destinationPath,
                           boolean pathChanged) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.destinationPath = destinationPath;
        this.pathChanged = pathChanged;
    }

    @Override
    public void onExecute() {
        try {
            for (ListViewItem item : items) {
                if (item.isDirectory()) {
                    FileUtil.copyDirectoryToDirectory(new File(item.getAbsPath()), new File(destinationPath));
                } else {
                    FileUtil.copyFileToDirectory(new File(item.getAbsPath()), new File(destinationPath), true);
                }
            }
            makeRefresh();
        } catch (IOException e) {
            Log.e(LOG_TAG, "onExecute", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
            makeRefresh();
        }
    }

    private void makeRefresh() {
        terminalActivity.getLeftListAdapter().clearSelection();
        terminalActivity.getRightListAdapter().clearSelection();
        if (!pathChanged) {
            switch (terminalActivity.getActivePage()) {
                case LEFT:
                    terminalActivity.getRightListAdapter().changeDirectory(destinationPath);
                    break;
                case RIGHT:
                    terminalActivity.getLeftListAdapter().changeDirectory(destinationPath);
                    break;
            }
        }
    }
}
