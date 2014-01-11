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
import com.softsandr.utils.file.FileUtil;
import com.softsandr.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * This class contain logic that used when make directory operation executing
 */
public class MakeDirectoryCommand implements FileCommand {
    private static final String LOG_TAG = MakeDirectoryCommand.class.getSimpleName();
    private final TerminalActivityImpl terminalActivity;
    private final String directoryName;
    private final String currentPath;
    private final String destinationPath;

    public MakeDirectoryCommand(TerminalActivityImpl terminalActivity, String directoryName,
                                String currentPath,
                                String destinationPath) {
        this.terminalActivity = terminalActivity;
        this.directoryName = directoryName;
        this.currentPath = currentPath;
        this.destinationPath = destinationPath;
    }

    @Override
    public void onExecute() {
        try {
            FileUtil.forceMakeDir(new File(directoryName));
            // clear selected and refresh directory after deleting
            makeClearSelection();
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeDirectory", e);
            Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void makeClearSelection() {
        if (currentPath.equals(destinationPath)) {
            terminalActivity.getLeftListAdapter().clearSelection();
            terminalActivity.getRightListAdapter().clearSelection();
            terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
            terminalActivity.getRightListAdapter().changeDirectory(currentPath);
        } else {
            String correctDirectoryName = directoryName.endsWith(StringUtil.PATH_SEPARATOR) ?
                    directoryName.substring(0, directoryName.length() - 1) : directoryName;
            int lastSlashIndex = correctDirectoryName.lastIndexOf(StringUtil.PATH_SEPARATOR);
            correctDirectoryName = correctDirectoryName.substring(0, lastSlashIndex);
            switch (terminalActivity.getActivePage()) {
                case LEFT:
                    if (correctDirectoryName.equals(destinationPath)) {
                        terminalActivity.getRightListAdapter().changeDirectory(destinationPath);
                    } else {
                        terminalActivity.getLeftListAdapter().clearSelection();
                        terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                    }
                    break;
                case RIGHT:
                    if (correctDirectoryName.equals(destinationPath)) {
                        terminalActivity.getLeftListAdapter().changeDirectory(destinationPath);
                    } else {
                        terminalActivity.getRightListAdapter().clearSelection();
                        terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                    }
                    break;
            }
        }
    }
}
