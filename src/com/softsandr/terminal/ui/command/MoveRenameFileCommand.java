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
public class MoveRenameFileCommand implements FileCommand {
    private static final String LOG_TAG = MoveRenameFileCommand.class.getSimpleName();
    private final TerminalActivity terminalActivity;
    private final List<ListViewItem> items;
    private final String destinationPath;
    private final String destinationOldPath;
    private final String currentPath;
    private final boolean pathChanged;

    public MoveRenameFileCommand(TerminalActivity terminalActivity,
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
                File dstDirectory = new File(destinationPath);
                if (dstDirectory.exists()) {
                    if (dstDirectory.canWrite()) {
                        for (ListViewItem item : items) {
                            File srcFile = new File(item.getAbsPath());
                            if (srcFile.canRead()) {
                                FileUtil.moveToDirectory(srcFile, dstDirectory, true);
                            } else {
                                Toast.makeText(terminalActivity, "No enough permission to read source file.", Toast.LENGTH_SHORT).show();
                                makeClearSelection();
                            }
                        }
                        // clear selected
                        makeClearSelection();
                        makeRefreshDirectory();
                    } else {
                        Toast.makeText(terminalActivity, "No enough permission to write in directory " + destinationPath + ".", Toast.LENGTH_SHORT).show();
                        makeClearSelection();
                    }
                } else {
                    Toast.makeText(terminalActivity, "Destination directory " + destinationPath + " not exists.", Toast.LENGTH_SHORT).show();
                    makeClearSelection();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Move/Rename", e);
                Toast.makeText(terminalActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                makeClearSelection();
            }
        } else {
            // todo show message about "Not object selected for copy operation"
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