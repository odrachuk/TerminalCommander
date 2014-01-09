package com.softsandr.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.util.utils.FileUtil;
import com.softsandr.terminal.model.listview.ListViewItem;
import com.softsandr.terminal.ui.activity.terminal.TerminalActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class CopyFileCommand implements FileCommand {
    private static final String LOG_TAG = CopyFileCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final List<ListViewItem> items;
    private final String destinationPath;
    private final boolean pathChanged;

    public CopyFileCommand(TerminalActivity terminalActivity,
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
        if (items != null && !items.isEmpty()) {
            try {
                for (ListViewItem item : items) {
                    if (item.isDirectory()) {
                        FileUtil.copyDirectoryToDirectory(new File(item.getAbsPath()), new File(destinationPath));
                    } else {
                        FileUtil.copyFileToDirectory(new File(item.getAbsPath()), new File(destinationPath), true);
                    }
                }
                // clear selected
                makeClearSelection();
                makeRefreshDirectory();
            } catch (IOException e) {
                Log.e(LOG_TAG, "copyFile", e);
                Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                makeClearSelection();
            }
        } else {
            Toast.makeText(terminalActivity, "No object selected for copy operation", Toast.LENGTH_SHORT).show();
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
