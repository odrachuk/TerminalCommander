package com.softsandr.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.util.utils.FileUtil;
import com.drk.terminal.util.utils.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class MakeDirectoryCommand implements FileCommand {
    private static final String LOG_TAG = MakeDirectoryCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final String directoryName;
    private final String currentPath;
    private final String destinationPath;

    public MakeDirectoryCommand(TerminalActivity terminalActivity, String directoryName,
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
