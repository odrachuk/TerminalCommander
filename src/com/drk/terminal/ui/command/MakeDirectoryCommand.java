package com.drk.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.utils.FileUtil;

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
            if (directoryName.contains(destinationPath)) {
                switch (terminalActivity.getActivePage()) {
                    case LEFT:
                        terminalActivity.getLeftListAdapter().clearSelection();
                        terminalActivity.getRightListAdapter().clearSelection();
                        terminalActivity.getRightListAdapter().changeDirectory(destinationPath);
                        break;
                    case RIGHT:
                        terminalActivity.getLeftListAdapter().clearSelection();
                        terminalActivity.getRightListAdapter().clearSelection();
                        terminalActivity.getLeftListAdapter().changeDirectory(destinationPath);
                        break;
                }
            } else {
                switch (terminalActivity.getActivePage()) {
                    case LEFT:
                        terminalActivity.getLeftListAdapter().clearSelection();
                        terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                        break;
                    case RIGHT:
                        terminalActivity.getRightListAdapter().clearSelection();
                        terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                        break;
                }
            }
        }
    }
}
