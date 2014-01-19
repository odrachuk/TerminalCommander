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
    private final String fileName;
    private final String dstPath;
    private final String curPath;

    public RenameFileCommand(TerminalActivityImpl terminalActivity,
                             ListViewItem item,
                             String fileName,
                             String curPath,
                             String dstPath) {
        this.terminalActivity = terminalActivity;
        this.item = item;
        this.fileName = fileName;
        this.curPath = curPath;
        this.dstPath = dstPath;
    }

    @Override
    public void onExecute() {
        try {
            File srcFile = new File(item.getAbsPath());
            File destinationFile = new File(curPath + StringUtil.PATH_SEPARATOR + fileName);
            FileUtil.renameFile(srcFile, destinationFile);
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
        switch (terminalActivity.getActivePage()) {
            case LEFT:
                terminalActivity.getLeftListAdapter().changeDirectory(curPath);
                if (dstPath.equals(curPath)) {
                    terminalActivity.getRightListAdapter().changeDirectory(dstPath);
                }
                break;
            case RIGHT:
                terminalActivity.getRightListAdapter().changeDirectory(curPath);
                if (dstPath.equals(curPath)) {
                    terminalActivity.getLeftListAdapter().changeDirectory(dstPath);
                }
                break;
        }
    }
}
