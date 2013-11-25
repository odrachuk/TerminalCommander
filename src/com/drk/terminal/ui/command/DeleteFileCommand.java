package com.drk.terminal.ui.command;

import android.util.Log;
import android.widget.Toast;
import com.drk.terminal.model.listview.ListViewItem;
import com.drk.terminal.ui.activity.terminal.TerminalActivity;
import com.drk.terminal.utils.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Date: 11/24/13
 *
 * @author Drachuk O.V.
 */
public class DeleteFileCommand implements FileCommand {
    private static final String LOG_TAG = DeleteFileCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final List<ListViewItem> items;
    private final String currentPath;
    private final String destinationPath;

    public DeleteFileCommand(TerminalActivity terminalActivity, List<ListViewItem> items,
                             String currentPath,
                             String destinationPath) {
        this.terminalActivity = terminalActivity;
        this.items = items;
        this.currentPath = currentPath;
        this.destinationPath = destinationPath;
    }

    @Override
    public void onExecute() {
        if (items != null && !items.isEmpty()) {
            try {
                for (ListViewItem item : items) {
                    File file = new File(item.getAbsPath());
                    if (file.canRead()) {
                        FileUtil.deleteQuietly(file);
                    } else {
                        Toast.makeText(terminalActivity, "No enough permission to delete source file " + file.getName(), Toast.LENGTH_SHORT).show();
                    }
                    // clear selected and refresh directory after deleting
                    makeClearSelection();
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "copyFile", e);
                Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                makeClearSelection();
            }
        } else {
            // todo show message about "Not object selected for copy operation"
            Toast.makeText(terminalActivity, "No object selected for copy operation", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeClearSelection() {
        if (currentPath.equals(destinationPath)) {
            terminalActivity.getLeftListAdapter().clearSelection();
            terminalActivity.getRightListAdapter().clearSelection();
            terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
            terminalActivity.getRightListAdapter().changeDirectory(currentPath);
        } else {
            switch (terminalActivity.getActivePage()) {
                case LEFT:
                    terminalActivity.getLeftListAdapter().clearSelection();
                    terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                    if (destinationPath.contains(currentPath)) {
                        terminalActivity.getRightListAdapter().clearSelection();
                        terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                        terminalActivity.getRightListAdapter().goBackToPath(currentPath);
                    }
                    break;
                case RIGHT:
                    terminalActivity.getRightListAdapter().clearSelection();
                    terminalActivity.getRightListAdapter().changeDirectory(currentPath);
                    if (destinationPath.contains(currentPath)) {
                        terminalActivity.getLeftListAdapter().clearSelection();
                        terminalActivity.getLeftListAdapter().changeDirectory(currentPath);
                        terminalActivity.getLeftListAdapter().goBackToPath(currentPath);
                    }
                    break;
            }
        }
    }
}
